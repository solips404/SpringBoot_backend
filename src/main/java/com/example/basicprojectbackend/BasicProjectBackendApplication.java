package com.example.basicprojectbackend;

import Service.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "*")
public class BasicProjectBackendApplication {
    @Autowired
    private UserDao userDao;
    @Autowired
    private FileDao fileDao;
    private EmailSender emailSender;
    private final String path = "C:\\Users\\李杰軒\\Desktop\\Image";

    public static void main(String[] args) {
        SpringApplication.run(BasicProjectBackendApplication.class, args);
    }

    @PostMapping("/SubmitPage")
    public ResponseEntity<?> submitUser(@RequestBody JavaUserBean user) {
        try {
            if (user.getUserName() != null && user.getPassWord() != null) {
                user.setUserName(user.getUserName());
                user.setPassWord(user.getPassWord());
                userDao.save(user);
                return ResponseEntity.ok("註冊成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("註冊失敗");
    }

    @PostMapping("/LoginPage")
    public ResponseEntity<?> adminLogin(@RequestBody JavaUserBean searchAdmin) {
        JavaUserBean admin = userDao.findByUserName(searchAdmin.getUserName());
        if (admin != null && admin.getPassWord().equals(searchAdmin.getPassWord())) {
            return ResponseEntity.ok(true);
        } else {
            if (admin == null) {
                return ResponseEntity.ok("此帳號不存在");
            } else {
                return ResponseEntity.ok("帳號密碼錯誤");
            }
        }
    }

    @GetMapping("/IndexPage")
    public ResponseEntity<?> userSearch(@RequestParam(defaultValue = "0") int page, JavaUserBean user) {
        Pageable pageable = PageRequest.of(page, 12);
        Page<JavaUserBean> userPage = userDao.findAll(pageable);
        List<JavaUserBean> userList = userPage.getContent();
        int totalPage = userPage.getTotalPages();
        Map<String, Object> IndexPage = new HashMap<>();
        IndexPage.put("userList", userList);
        IndexPage.put("totalPages", totalPage);
        return ResponseEntity.ok(IndexPage);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> editUser(@PathVariable("id") Long id, @RequestBody JavaUserBean user) {
        JavaUserBean userSet = userDao.findById(id).orElse(null);
        if (user != null) {
            userSet.setUserName(user.getUserName());
            userSet.setPassWord(user.getPassWord());
            userDao.save(userSet);
            return ResponseEntity.ok("儲存成功");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Long id) {
        JavaUserBean getUser = userDao.findById(id).orElse(null);
        if (getUser != null) {
            return ResponseEntity.ok(getUser);
        } else {
            return ResponseEntity.ok("查無資料");
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        JavaUserBean user = userDao.findById(id).orElse(null);
        if (user != null) {
            userDao.delete(user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            File imageFile = new File(path, fileName);
            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(file.getBytes());
            fos.close();
            JavaFileBean fileBean = new JavaFileBean();
            fileBean.setFileName(fileName);
            fileBean.setFilePath(imageFile.getAbsolutePath());
            fileDao.save(fileBean);
            Map<String, String> imageUrl = new HashMap<>();
            imageUrl.put("fileName", fileName);
            System.out.println(fileBean);
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to upload image");
        }
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadImage(@RequestParam("imageName") String imageName) {
        try {
            File file = new File(path, imageName);

            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            // 使用 FileSystemResource 來讀取檔案內容
            //Resource resource = new FileSystemResource(file);
            FileSystemResource resource = new FileSystemResource(file);

            // 使用 ResponseEntity 回傳檔案內容
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageName + "\"")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/downloadList")
    public ResponseEntity<?> downloadAllData(@RequestBody JavaFileBean fileBean) {
        File files = new File(path);
        File[] fileList = files.listFiles();
        Map<String,Object> FileUrl = new HashMap<>();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isFile()) {
                JavaFileBean javaFileBean = new JavaFileBean();
                javaFileBean.setFileName(fileList[i].getName());
                javaFileBean.setFilePath(fileList[i].getAbsolutePath());
                fileDao.save(javaFileBean);
            }
        }
        FileUrl.put("Test",fileDao.findAll());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"file_urls.json\"").body(FileUrl);
    }
    @PostMapping("/SearchQuery")
    public ResponseEntity<?>searchQuery(@RequestParam(defaultValue = "0") int page,@RequestBody JavaUserBean user){
        Pageable pageable = PageRequest.of(page, 12);
        Page<JavaUserBean> userPage = userDao.findAll(pageable);
        List<JavaUserBean> list = userDao.findByUserNameContaining(user.getUserName());
        int totalPage = userPage.getTotalPages();
        Map<String, Object> IndexPage = new HashMap<>();
        IndexPage.put("userList", list);
        IndexPage.put("totalPages", totalPage);
        return ResponseEntity.ok(IndexPage);
    }

    @PostMapping("/SendEmail")
    public ResponseEntity<?> sendEmail() {
        emailSender.setMailSender("11010111@gm.chihlee.edu.tw", "Hi", "ok success!");
        return ResponseEntity.ok("ok");
    }
}

package com.example.basicprojectbackend;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UserDao extends JpaRepository<JavaUserBean,Long>{
    JavaUserBean findByUserName(String username);

    @Query("SELECT u FROM JavaUserBean u WHERE u.userName LIKE %?1%")
    List<JavaUserBean> findByUserNameContaining(String keyword);
}
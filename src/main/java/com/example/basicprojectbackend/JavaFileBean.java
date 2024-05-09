package com.example.basicprojectbackend;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name="filedata")
public class JavaFileBean implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name="file_name")
    private String fileName;
    @Column(name="file_path")
    private String filePath;
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}

package com.example.basicprojectbackend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FileDao extends JpaRepository<JavaFileBean,Long> {
    JavaFileBean findByFilePath(String path);
}

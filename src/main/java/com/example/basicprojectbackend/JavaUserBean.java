package com.example.basicprojectbackend;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "userdata")
public class JavaUserBean implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "pass_word")
    private String passWord;
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {this.userName = userName;}
    public String getPassWord() {
        return passWord;
    }
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

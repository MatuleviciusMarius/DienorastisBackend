package com.marius.dienorastis.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {
    private Integer id;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Entry> entries;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

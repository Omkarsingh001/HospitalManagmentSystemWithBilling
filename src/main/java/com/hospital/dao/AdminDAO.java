package com.hospital.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDAO {

    @Autowired
    private JdbcTemplate jdbc;

    public boolean validate(String username, String password) {
        Integer count = jdbc.queryForObject(
            "SELECT COUNT(*) FROM admin WHERE username=? AND password=?",
            Integer.class, username, password);
        return count != null && count > 0;
    }

    public int changePassword(String username, String newPassword) {
        return jdbc.update(
            "UPDATE admin SET password=? WHERE username=?", newPassword, username);
    }
}

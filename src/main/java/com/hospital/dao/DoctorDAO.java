package com.hospital.dao;

import com.hospital.model.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class DoctorDAO {

    @Autowired
    private JdbcTemplate jdbc;

    private final RowMapper<Doctor> mapper = (rs, i) -> new Doctor(
            rs.getInt("id"), rs.getString("name"), rs.getString("specialization"),
            rs.getString("contact") == null ? "" : rs.getString("contact"),
            rs.getBoolean("available")
    );

    public List<Doctor> getAll() {
        return jdbc.query("SELECT * FROM doctors ORDER BY id", mapper);
    }

    public Doctor getById(int id) {
        List<Doctor> list = jdbc.query("SELECT * FROM doctors WHERE id=?", mapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Doctor> search(String keyword) {
        return jdbc.query(
            "SELECT * FROM doctors WHERE name LIKE ? OR specialization LIKE ?",
            mapper, "%" + keyword + "%", "%" + keyword + "%");
    }

    public int add(Doctor d) {
        return jdbc.update(
            "INSERT INTO doctors(name,specialization,contact) VALUES(?,?,?)",
            d.getName(), d.getSpecialization(), d.getContact());
    }

    public int update(Doctor d) {
        return jdbc.update(
            "UPDATE doctors SET name=?,specialization=?,contact=?,available=? WHERE id=?",
            d.getName(), d.getSpecialization(), d.getContact(), d.isAvailable(), d.getId());
    }

    public int delete(int id) {
        return jdbc.update("DELETE FROM doctors WHERE id=?", id);
    }

    public int count() {
        Integer c = jdbc.queryForObject("SELECT COUNT(*) FROM doctors", Integer.class);
        return c == null ? 0 : c;
    }
}

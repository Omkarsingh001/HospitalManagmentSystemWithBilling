package com.hospital.dao;

import com.hospital.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class PatientDAO {

    @Autowired
    private JdbcTemplate jdbc;

    private final RowMapper<Patient> mapper = (rs, i) -> new Patient(
            rs.getInt("id"), rs.getString("name"), rs.getInt("age"),
            rs.getString("gender"), rs.getString("contact"),
            rs.getString("address") == null ? "" : rs.getString("address")
    );

    public List<Patient> getAll() {
        return jdbc.query("SELECT * FROM patients ORDER BY id DESC", mapper);
    }

    public Patient getById(int id) {
        List<Patient> list = jdbc.query("SELECT * FROM patients WHERE id=?", mapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Patient> search(String keyword) {
        return jdbc.query(
            "SELECT * FROM patients WHERE name LIKE ? OR contact LIKE ?",
            mapper, "%" + keyword + "%", "%" + keyword + "%");
    }

    public int add(Patient p) {
        return jdbc.update(
            "INSERT INTO patients(name,age,gender,contact,address) VALUES(?,?,?,?,?)",
            p.getName(), p.getAge(), p.getGender(), p.getContact(), p.getAddress());
    }

    public int update(Patient p) {
        return jdbc.update(
            "UPDATE patients SET name=?,age=?,gender=?,contact=?,address=? WHERE id=?",
            p.getName(), p.getAge(), p.getGender(), p.getContact(), p.getAddress(), p.getId());
    }

    public int delete(int id) {
        return jdbc.update("DELETE FROM patients WHERE id=?", id);
    }

    public int count() {
        Integer c = jdbc.queryForObject("SELECT COUNT(*) FROM patients", Integer.class);
        return c == null ? 0 : c;
    }
}

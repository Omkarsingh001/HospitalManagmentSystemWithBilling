package com.hospital.dao;

import com.hospital.model.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class AppointmentDAO {

    @Autowired
    private JdbcTemplate jdbc;

    private final RowMapper<Appointment> mapper = (rs, i) -> {
        Appointment a = new Appointment();
        a.setId(rs.getInt("id"));
        a.setPatientId(rs.getInt("patient_id"));
        a.setDoctorId(rs.getInt("doctor_id"));
        a.setApptDate(rs.getString("appt_date"));
        a.setApptTime(rs.getString("appt_time"));
        a.setStatus(rs.getString("status"));
        a.setNotes(rs.getString("notes"));
        try { a.setPatientName(rs.getString("patient_name")); } catch (Exception ignored) {}
        try { a.setDoctorName(rs.getString("doctor_name")); }  catch (Exception ignored) {}
        return a;
    };

    private static final String JOIN =
        "SELECT a.*, p.name AS patient_name, d.name AS doctor_name " +
        "FROM appointments a " +
        "JOIN patients p ON a.patient_id=p.id " +
        "JOIN doctors  d ON a.doctor_id=d.id ";

    public List<Appointment> getAll() {
        return jdbc.query(JOIN + "ORDER BY a.appt_date DESC, a.appt_time", mapper);
    }

    public Appointment getById(int id) {
        List<Appointment> list = jdbc.query(JOIN + "WHERE a.id=?", mapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Appointment> getByPatient(int patientId) {
        return jdbc.query(JOIN + "WHERE a.patient_id=? ORDER BY a.appt_date DESC", mapper, patientId);
    }

    public int add(Appointment a) {
        return jdbc.update(
            "INSERT INTO appointments(patient_id,doctor_id,appt_date,appt_time,notes) VALUES(?,?,?,?,?)",
            a.getPatientId(), a.getDoctorId(), a.getApptDate(), a.getApptTime(), a.getNotes());
    }

    public int updateStatus(int id, String status) {
        return jdbc.update("UPDATE appointments SET status=? WHERE id=?", status, id);
    }

    public int delete(int id) {
        return jdbc.update("DELETE FROM appointments WHERE id=?", id);
    }

    public int count() {
        Integer c = jdbc.queryForObject("SELECT COUNT(*) FROM appointments", Integer.class);
        return c == null ? 0 : c;
    }
}

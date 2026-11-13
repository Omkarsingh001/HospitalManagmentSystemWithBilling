package com.hospital.dao;

import com.hospital.model.Billing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class BillingDAO {

    @Autowired
    private JdbcTemplate jdbc;

    private final RowMapper<Billing> mapper = (rs, i) -> {
        Billing b = new Billing();
        b.setId(rs.getInt("id"));
        b.setPatientId(rs.getInt("patient_id"));
        b.setTreatment(rs.getString("treatment"));
        b.setAmount(rs.getDouble("amount"));
        b.setPaymentStatus(rs.getString("payment_status"));
        b.setBillDate(rs.getString("bill_date") == null ? "" : rs.getString("bill_date"));
        try { b.setPatientName(rs.getString("patient_name")); } catch (Exception ignored) {}
        return b;
    };

    public List<Billing> getAll() {
        return jdbc.query(
            "SELECT b.*, p.name AS patient_name FROM billing b " +
            "JOIN patients p ON b.patient_id=p.id ORDER BY b.id DESC", mapper);
    }

    public Billing getById(int id) {
        List<Billing> list = jdbc.query(
            "SELECT b.*, p.name AS patient_name FROM billing b " +
            "JOIN patients p ON b.patient_id=p.id WHERE b.id=?", mapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Billing> getByPatient(int patientId) {
        return jdbc.query(
            "SELECT b.*, p.name AS patient_name FROM billing b " +
            "JOIN patients p ON b.patient_id=p.id WHERE b.patient_id=? ORDER BY b.id DESC",
            mapper, patientId);
    }

    public int add(Billing b) {
        return jdbc.update(
            "INSERT INTO billing(patient_id,treatment,amount) VALUES(?,?,?)",
            b.getPatientId(), b.getTreatment(), b.getAmount());
    }

    public int addCost(int id, double extra) {
        return jdbc.update("UPDATE billing SET amount=amount+? WHERE id=?", extra, id);
    }

    public int updateStatus(int id, String status) {
        return jdbc.update("UPDATE billing SET payment_status=? WHERE id=?", status, id);
    }

    public double getTotalByPatient(int patientId) {
        Double t = jdbc.queryForObject(
            "SELECT COALESCE(SUM(amount),0) FROM billing WHERE patient_id=?",
            Double.class, patientId);
        return t == null ? 0 : t;
    }

    public double getPendingRevenue() {
        Double t = jdbc.queryForObject(
            "SELECT COALESCE(SUM(amount),0) FROM billing WHERE payment_status='Pending'",
            Double.class);
        return t == null ? 0 : t;
    }
}

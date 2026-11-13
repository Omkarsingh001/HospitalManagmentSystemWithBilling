package com.hospital.controller;

import com.hospital.dao.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired private PatientDAO patientDAO;
    @Autowired private DoctorDAO doctorDAO;
    @Autowired private AppointmentDAO appointmentDAO;
    @Autowired private BillingDAO billingDAO;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/login";
        model.addAttribute("totalPatients",      patientDAO.count());
        model.addAttribute("totalDoctors",       doctorDAO.count());
        model.addAttribute("totalAppointments",  appointmentDAO.count());
        model.addAttribute("pendingRevenue",     billingDAO.getPendingRevenue());
        model.addAttribute("recentPatients",     patientDAO.getAll().stream().limit(5).toList());
        model.addAttribute("recentAppointments", appointmentDAO.getAll().stream().limit(5).toList());
        model.addAttribute("admin", session.getAttribute("admin"));
        return "dashboard";
    }
}

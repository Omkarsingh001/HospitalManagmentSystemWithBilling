package com.hospital.controller;

import com.hospital.dao.AppointmentDAO;
import com.hospital.dao.DoctorDAO;
import com.hospital.dao.PatientDAO;
import com.hospital.model.Appointment;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired private AppointmentDAO appointmentDAO;
    @Autowired private PatientDAO patientDAO;
    @Autowired private DoctorDAO doctorDAO;

    private boolean notLoggedIn(HttpSession s) { return s.getAttribute("admin") == null; }

    @GetMapping
    public String list(HttpSession session, Model model) {
        if (notLoggedIn(session)) return "redirect:/login";
        model.addAttribute("appointments", appointmentDAO.getAll());
        return "appointments/list";
    }

    @GetMapping("/add")
    public String addForm(HttpSession session, Model model) {
        if (notLoggedIn(session)) return "redirect:/login";
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("patients", patientDAO.getAll());
        model.addAttribute("doctors",  doctorDAO.getAll());
        return "appointments/form";
    }

    @PostMapping("/add")
    public String addSubmit(@ModelAttribute Appointment appointment, RedirectAttributes ra) {
        appointmentDAO.add(appointment);
        ra.addFlashAttribute("success", "Appointment booked successfully!");
        return "redirect:/appointments";
    }

    @GetMapping("/status/{id}")
    public String updateStatus(@PathVariable int id,
                               @RequestParam String status, RedirectAttributes ra) {
        appointmentDAO.updateStatus(id, status);
        ra.addFlashAttribute("success", "Status updated to: " + status);
        return "redirect:/appointments";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes ra) {
        appointmentDAO.delete(id);
        ra.addFlashAttribute("success", "Appointment cancelled successfully.");
        return "redirect:/appointments";
    }
}

package com.hospital.controller;

import com.hospital.dao.DoctorDAO;
import com.hospital.model.Doctor;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired private DoctorDAO doctorDAO;

    private boolean notLoggedIn(HttpSession s) { return s.getAttribute("admin") == null; }

    @GetMapping
    public String list(@RequestParam(required = false) String search,
                       HttpSession session, Model model) {
        if (notLoggedIn(session)) return "redirect:/login";
        model.addAttribute("doctors",
            (search != null && !search.isBlank()) ? doctorDAO.search(search) : doctorDAO.getAll());
        model.addAttribute("search", search);
        return "doctors/list";
    }

    @GetMapping("/add")
    public String addForm(HttpSession session, Model model) {
        if (notLoggedIn(session)) return "redirect:/login";
        model.addAttribute("doctor", new Doctor());
        return "doctors/form";
    }

    @PostMapping("/add")
    public String addSubmit(@ModelAttribute Doctor doctor, RedirectAttributes ra) {
        doctorDAO.add(doctor);
        ra.addFlashAttribute("success", "Doctor added successfully!");
        return "redirect:/doctors";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, HttpSession session, Model model) {
        if (notLoggedIn(session)) return "redirect:/login";
        model.addAttribute("doctor", doctorDAO.getById(id));
        return "doctors/form";
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable int id,
                             @ModelAttribute Doctor doctor, RedirectAttributes ra) {
        doctor.setId(id);
        doctorDAO.update(doctor);
        ra.addFlashAttribute("success", "Doctor updated successfully!");
        return "redirect:/doctors";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes ra) {
        doctorDAO.delete(id);
        ra.addFlashAttribute("success", "Doctor deleted successfully.");
        return "redirect:/doctors";
    }
}

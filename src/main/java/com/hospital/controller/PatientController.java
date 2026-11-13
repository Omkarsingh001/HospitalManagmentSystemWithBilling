package com.hospital.controller;

import com.hospital.dao.PatientDAO;
import com.hospital.model.Patient;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/patients")
public class PatientController {

    @Autowired private PatientDAO patientDAO;

    private boolean notLoggedIn(HttpSession s) { return s.getAttribute("admin") == null; }

    @GetMapping
    public String list(@RequestParam(required = false) String search,
                       HttpSession session, Model model) {
        if (notLoggedIn(session)) return "redirect:/login";
        model.addAttribute("patients",
            (search != null && !search.isBlank()) ? patientDAO.search(search) : patientDAO.getAll());
        model.addAttribute("search", search);
        return "patients/list";
    }

    @GetMapping("/add")
    public String addForm(HttpSession session, Model model) {
        if (notLoggedIn(session)) return "redirect:/login";
        model.addAttribute("patient", new Patient());
        return "patients/form";
    }

    @PostMapping("/add")
    public String addSubmit(@ModelAttribute Patient patient, RedirectAttributes ra) {
        patientDAO.add(patient);
        ra.addFlashAttribute("success", "Patient added successfully!");
        return "redirect:/patients";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, HttpSession session, Model model) {
        if (notLoggedIn(session)) return "redirect:/login";
        Patient p = patientDAO.getById(id);
        if (p == null) return "redirect:/patients";
        model.addAttribute("patient", p);
        return "patients/form";
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable int id,
                             @ModelAttribute Patient patient, RedirectAttributes ra) {
        patient.setId(id);
        patientDAO.update(patient);
        ra.addFlashAttribute("success", "Patient updated successfully!");
        return "redirect:/patients";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes ra) {
        patientDAO.delete(id);
        ra.addFlashAttribute("success", "Patient deleted successfully.");
        return "redirect:/patients";
    }
}

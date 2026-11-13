package com.hospital.controller;

import com.hospital.dao.BillingDAO;
import com.hospital.dao.PatientDAO;
import com.hospital.model.Billing;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/billing")
public class BillingController {

    @Autowired private BillingDAO billingDAO;
    @Autowired private PatientDAO patientDAO;

    private boolean notLoggedIn(HttpSession s) { return s.getAttribute("admin") == null; }

    @GetMapping
    public String list(HttpSession session, Model model) {
        if (notLoggedIn(session)) return "redirect:/login";
        model.addAttribute("bills", billingDAO.getAll());
        model.addAttribute("pendingRevenue", billingDAO.getPendingRevenue());
        return "billing/list";
    }

    @GetMapping("/add")
    public String addForm(HttpSession session, Model model) {
        if (notLoggedIn(session)) return "redirect:/login";
        model.addAttribute("bill", new Billing());
        model.addAttribute("patients", patientDAO.getAll());
        return "billing/form";
    }

    @PostMapping("/add")
    public String addSubmit(@ModelAttribute Billing bill, RedirectAttributes ra) {
        billingDAO.add(bill);
        ra.addFlashAttribute("success", "Bill generated successfully!");
        return "redirect:/billing";
    }

    @GetMapping("/patient/{id}")
    public String patientBills(@PathVariable int id, HttpSession session, Model model) {
        if (notLoggedIn(session)) return "redirect:/login";
        model.addAttribute("bills",   billingDAO.getByPatient(id));
        model.addAttribute("patient", patientDAO.getById(id));
        model.addAttribute("total",   billingDAO.getTotalByPatient(id));
        return "billing/patient-bills";
    }

    @GetMapping("/status/{id}")
    public String updateStatus(@PathVariable int id,
                               @RequestParam String status, RedirectAttributes ra) {
        billingDAO.updateStatus(id, status);
        ra.addFlashAttribute("success", "Payment status updated to: " + status);
        return "redirect:/billing";
    }

    @PostMapping("/add-cost/{id}")
    public String addCost(@PathVariable int id,
                          @RequestParam double extra, RedirectAttributes ra) {
        billingDAO.addCost(id, extra);
        ra.addFlashAttribute("success", String.format("₹%.2f added to bill.", extra));
        return "redirect:/billing";
    }
}

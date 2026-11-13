package com.hospital.controller;

import com.hospital.dao.AdminDAO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired private AdminDAO adminDAO;

    @GetMapping("/")
    public String root() { return "redirect:/login"; }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("admin") != null) return "redirect:/dashboard";
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session, Model model) {
        if (adminDAO.validate(username, password)) {
            session.setAttribute("admin", username);
            return "redirect:/dashboard";
        }
        model.addAttribute("error", "Invalid username or password!");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/change-password")
    public String changePasswordPage(HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/login";
        return "change-password";
    }

    @PostMapping("/change-password")
    public String doChangePassword(@RequestParam String currentPassword,
                                   @RequestParam String newPassword,
                                   @RequestParam String confirmPassword,
                                   HttpSession session, Model model) {
        String admin = (String) session.getAttribute("admin");
        if (admin == null) return "redirect:/login";
        if (!adminDAO.validate(admin, currentPassword)) {
            model.addAttribute("error", "Current password is incorrect!");
            return "change-password";
        }
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New passwords do not match!");
            return "change-password";
        }
        adminDAO.changePassword(admin, newPassword);
        model.addAttribute("success", "Password changed successfully!");
        return "change-password";
    }
}

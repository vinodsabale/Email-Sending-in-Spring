package com.controller; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.service.EmailService;
import com.validation.RegistrationForm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Assignment 4 — Form Validation + Email Controller
 *
 * GET  /register  → show blank form
 * POST /register  → validate form
 *                  → if errors: redisplay form WITH error messages
 *                  → if valid:  send email → redirect to success page
 *
 * Key annotations:
 *  @Valid         → triggers Bean Validation on the form object
 *  BindingResult  → holds validation errors (MUST come right after @Valid)
 *  @ModelAttribute → binds form fields to the Java object
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class FormController {

    private final EmailService emailService;

    /** Show registration form */
    @GetMapping({"/", "/register"})
    public String showForm(Model model) {
        model.addAttribute("form", new RegistrationForm());
        return "register";   // templates/register.html
    }

    /** Handle form submission */
    @PostMapping("/register")
    public String submitForm(@Valid @ModelAttribute("form") RegistrationForm form,
                             BindingResult result,              // ← must be right after @Valid
                             Model model,
                             RedirectAttributes ra) {

        // Step 1 — Check for validation errors
        if (result.hasErrors()) {
            // Return to form; Thymeleaf will display error messages
            log.warn("Form validation failed: {} errors", result.getErrorCount());
            return "register";   // redisplay form with errors
        }

        // Step 2 — Form is valid, send registration email
        try {
            emailService.sendRegistrationEmail(form.getEmail(), form.getName(), form.getPhno());
            log.info("Registration successful for: {}", form.getEmail());

            // Pass data to success page via flash attributes
            ra.addFlashAttribute("name", form.getName());
            ra.addFlashAttribute("email", form.getEmail());
            ra.addFlashAttribute("phone", form.getPhno());
            return "redirect:/success";   // redirect to success page

        } catch (Exception e) {
            // Email failed — show error on same form
            model.addAttribute("emailError",
                    "Registration saved, but email could not be sent: " + e.getMessage());
            model.addAttribute("form", form);
            return "register";
        }
    }

    /** Success page after registration */
    @GetMapping("/success")
    public String successPage(Model model) {
        // If user navigates directly to /success without registering, redirect to form
        if (!model.containsAttribute("name")) {
            return "redirect:/register";
        }
        return "success";   // templates/success.html
    }
}
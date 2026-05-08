package com.freakeshop.freak_e_shop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(HttpSession session) {
        // Se sincroniza la sesión del servidor con el login exitoso del frontend
        session.setAttribute("adminLogueado", true);
        return ResponseEntity.ok(Map.of("status", "success"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutAdmin(HttpSession session) {
        session.removeAttribute("adminLogueado");
        session.invalidate();
        return ResponseEntity.ok(Map.of("status", "success"));
    }
}

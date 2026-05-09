package com.freakeshop.freak_e_shop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(HttpSession session) {
        session.setAttribute("adminLogueado", true);
        return ResponseEntity.ok(Map.of("status", "success"));
    }

    @PostMapping("/login-usuario")
    public ResponseEntity<?> loginUsuario(@org.springframework.web.bind.annotation.RequestBody Map<String, Object> userData, HttpSession session) {
        session.setAttribute("usuarioLogueado", userData);
        return ResponseEntity.ok(Map.of("status", "success"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutAdmin(HttpSession session) {
        session.removeAttribute("adminLogueado");
        session.removeAttribute("usuarioLogueado");
        session.invalidate();
        return ResponseEntity.ok(Map.of("status", "success"));
    }

    @GetMapping("/check-session")
    public ResponseEntity<?> checkSession(HttpSession session) {
        Object admin = session.getAttribute("adminLogueado");
        Object user = session.getAttribute("usuarioLogueado");
        
        boolean logged = (admin != null && (boolean) admin) || (user != null);
        
        if (logged) {
            return ResponseEntity.ok(Map.of("logged", true, "role", admin != null ? "admin" : "user"));
        }
        return ResponseEntity.status(401).body(Map.of("logged", false));
    }
}

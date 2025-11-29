package com.brincadeiras.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<String> home() {
        String html = """
                <div style='font-family: Arial; padding:40px; text-align:center;'>
                    <h1>🚀 Backend Ativo!</h1>
                    <p>O servidor está funcionando corretamente.</p>
                    <p>Acesse o frontend da aplicação:</p>
                    <a href="https://brincadeiras-one.vercel.app/" target="_blank"
                       style="font-size:18px; color:#007bff; text-decoration:none;'>
                       👉 https://brincadeiras-one.vercel.app/
                    </a>
                </div>
                """;

        return ResponseEntity.ok().body(html);
    }
}

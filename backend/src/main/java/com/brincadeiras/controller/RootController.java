package com.brincadeiras.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<String> home() {
        String html = """
                <div style="
                    font-family: Arial, sans-serif; 
                    padding: 60px; 
                    text-align:center; 
                    background-color:#f7f7f7;
                    min-height:100vh;
                    display:flex;
                    flex-direction:column;
                    justify-content:center;
                    align-items:center;
                ">
                    <h1 style="color:#28a745; font-size:48px; margin-bottom:20px;">🚀 Backend Ativo!</h1>
                    <p style="font-size:18px; margin-bottom:30px;">O servidor está funcionando corretamente.</p>
                    <p style="font-size:18px; margin-bottom:20px;">Acesse o frontend da aplicação:</p>
                    <a href="https://brincadeiras-one.vercel.app/" target="_blank" style="
                        display:inline-block;
                        background-color:#007bff;
                        color:#fff;
                        font-size:20px;
                        padding:12px 30px;
                        border-radius:8px;
                        text-decoration:none;
                        transition: background-color 0.3s;
                    " onmouseover="this.style.backgroundColor='#0056b3';" onmouseout="this.style.backgroundColor='#007bff';">
                        👉 Abrir Frontend
                    </a>
                </div>
                """;

        return ResponseEntity.ok().body(html);
    }
}

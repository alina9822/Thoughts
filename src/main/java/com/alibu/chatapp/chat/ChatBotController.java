
package com.alibu.chatapp.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ai")
public class ChatBotController {

    @Autowired
    private OllamaService ollamaService;

    @PostMapping("/generate")
    public String generateResponse(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        if (prompt == null || prompt.isEmpty()) {
            return "Prompt must not be empty";
        }
        return ollamaService.sendOllamaRequest(prompt);
    }
}

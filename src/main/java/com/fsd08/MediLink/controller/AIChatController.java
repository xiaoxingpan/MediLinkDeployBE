package com.fsd08.MediLink.controller;

import java.util.Map;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fsd08.MediLink.service.GPTService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("aichat")
public class AIChatController {
    private GPTService gptService;

    public AIChatController(GPTService gptService) {
        this.gptService = gptService;
    }

    @CrossOrigin
    @PostMapping("/messages")
    public ResponseEntity<Map<String, Object>> processMessages(@RequestBody Map<String, Object> requestBody) {
        if (!requestBody.containsKey("messages")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Request body should contain 'messages' array."));
        }
        List<Map<String, String>> messages = (List<Map<String, String>>) requestBody.get("messages");

        // Check if all messages have role as "user" or "assistant"
        for (Map<String, String> message : messages) {
            String role = message.get("role");
            if (!"user".equals(role) && !"assistant".equals(role)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error",
                                "Invalid role in messages. Role should be either 'user' or 'assistant'."));
            }
        }

        // add a system message at the beginning
        Map<String, String> systemMessage = Map.of("role", "system", "content",
                "Throughout the chat, you should assume the role of a medical guidance assistant. The user assumes the role of the patient. When the user describes symptoms, you should make preliminary analysis and suggestions based on the symptoms. If the user describes the symptoms, immediately tell the user which department they should make an appointment with. Notify the user if their message is not related to the symptom and tell the user that you decline to respond to messages that are not related to the symptoms.");
        messages.add(0, systemMessage);
        // post messages to gpt api and get answer
        messages = gptService.getGPTAnswer(messages);
        if (messages == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // remove system message and return
        // messages.removeIf(m -> {
        // String role = m.get("role");
        // return !"user".equals(role) && !"assistant".equals(role);
        // });
        messages.remove(0);
        return ResponseEntity.ok(Map.of("messages", messages));
    }
}

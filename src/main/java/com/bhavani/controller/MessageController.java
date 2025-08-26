package com.bhavani.controller;

import com.bhavani.model.Chat;
import com.bhavani.model.Message;
import com.bhavani.model.User;
import com.bhavani.request.CreateMessageRequest;
import com.bhavani.service.MessageService;
import com.bhavani.service.ProjectService;
import com.bhavani.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody CreateMessageRequest request) {
        try {
            User user = userService.findUserById(request.getSenderId());
            Chat chat = projectService.getChatByProjectId(request.getProjectId()).getChat();
            if (chat == null) {
                return ResponseEntity.status(404).body("Chat not found for the project.");
            }

            Message sentMessage = messageService.sendMessage(
                    request.getSenderId(),
                    request.getProjectId(),
                    request.getContent());

            return ResponseEntity.ok(sentMessage);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send message: " + e.getMessage());
        }
    }

    @GetMapping("/chat/{projectId}")
    public ResponseEntity<List<Message>> getMessagesByChatId(@PathVariable Long projectId) {
        try {
            List<Message> messages = messageService.getMessagesByProjectId(projectId);
            return ResponseEntity.ok(messages); // Always returns array, even if empty
        } catch (Exception e) {
            return ResponseEntity.status(500).build(); // Optional: or send an error body
        }
    }
}

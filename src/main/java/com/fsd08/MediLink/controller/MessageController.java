package com.fsd08.MediLink.controller;

import com.fsd08.MediLink.dto.MessageDto;
import com.fsd08.MediLink.service.JwtService;
import com.fsd08.MediLink.service.MessageService;
import com.fsd08.MediLink.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST})
@RequiredArgsConstructor
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private MessageService messageService;
    private UserService userService;

    @Autowired
    public MessageController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @GetMapping("/messages")
    public List<MessageDto> getAllMessages() {
            return messageService.findAllMessages();
    }

    @PostMapping("/messages")
    public ResponseEntity<MessageDto> createMessage(@RequestBody MessageDto messageDto, @RequestHeader("Authorization") String accessToken) {
        if (messageDto.getBody() == null || messageDto.getBody().isEmpty()) {
            throw new IllegalArgumentException("Message body cannot be empty");
        }
        if(!JwtService.validateToken(accessToken)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String username = JwtService.extractUsernameFromToken(accessToken);
        int sender_id =  userService.findUserByUsername(username).get().getId();
        messageDto.setSender_id(sender_id);
        MessageDto result = messageService.addMessage(messageDto);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<MessageDto> getOneMessage(@PathVariable int id) {
        MessageDto message = messageService.getMessageById(id);
        return message != null ?
                ResponseEntity.ok().body(message) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/messages/{id}")
    public ResponseEntity<MessageDto> updateMessage(@PathVariable int id,  @RequestHeader("Authorization") String accessToken, @RequestBody MessageDto messageDto) {
        if (messageDto.getBody() == null || messageDto.getBody().isEmpty()) {
            throw new IllegalArgumentException("Message body cannot be empty");
        }
        if (!messageService.messageExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  //404
        }
        if (!JwtService.validateToken(accessToken) || !isAdmin(accessToken)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        messageDto.setId(id);
        MessageDto result = messageService.updateMessage(id, messageDto);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<MessageDto> deleteMessage(@PathVariable int id, @RequestHeader("Authorization") String accessToken) {
        if (!messageService.messageExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  //404
        }
        if (!JwtService.validateToken(accessToken) || !isAdmin(accessToken)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        MessageDto result = messageService.deleteMessage(id);
        return result != null ? ResponseEntity.ok(result) :
                new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    private boolean isAdmin(String accessToken) {
        String username = JwtService.extractUsernameFromToken(accessToken);
        String role = userService.findUserByUsername(username).get().getAuthority();
        return "admin".equals(role);
    }
}

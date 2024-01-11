package com.fsd08.MediLink.service;

import com.fsd08.MediLink.dto.MessageDto;
import com.fsd08.MediLink.entity.Message;
import com.fsd08.MediLink.repository.MessageRepository;
import com.fsd08.MediLink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public MessageService(MessageRepository messageRepository, JwtService jwtService, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.jwtService = jwtService;
        this.userRepository=userRepository;
    }

    public List<MessageDto> findAllMessages() {
        List<Message> messages = messageRepository.findAll();
        return messages.stream()
                .map(MessageDto::fromEntity)
                .collect(Collectors.toList());
    }

    public MessageDto getMessageById(int id) {
        Message message = messageRepository.findById(id).orElse(null);
        return (message != null) ? MessageDto.fromEntity(message) : null;
    }

    public MessageDto addMessage(MessageDto messageDto) {
        Message message = messageDto.toEntity();
        message = messageRepository.save(message);
        return MessageDto.fromEntity(message);
    }

    public MessageDto updateMessage(int id, MessageDto messageDto) {
            Message updatedMessage = messageDto.toEntity();
            updatedMessage.setId(id);
            updatedMessage = messageRepository.save(updatedMessage);
            return MessageDto.fromEntity(updatedMessage);
    }

    public MessageDto deleteMessage(int id) {
        messageRepository.deleteById(id);
        Optional<Message> deletedMessage = messageRepository.findById(id);
        return deletedMessage.map(MessageDto::fromEntity).orElse(null);
    }

    public boolean messageExists(int id) {
        return messageRepository.existsById(id);
    }
}

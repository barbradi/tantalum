package com.tantalum.test.message.controllers;

import com.tantalum.test.message.dtos.MessageDto;
import com.tantalum.test.message.entities.Message;
import com.tantalum.test.message.exceptions.NoModificableMessageException;
import com.tantalum.test.message.services.MessageService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDto addMessage(@RequestBody MessageDto messageDto) {
        Message messageSaved = messageService.save(convertToMessageEntity(messageDto));
        return convertToMessageDto(messageSaved);
    }

    @GetMapping("/{uuid}")
    public MessageDto getMessage(@PathVariable String uuid) {
        Message message = messageService.getMessage(uuid);
        return convertToMessageDto(message);
    }

    @GetMapping
    public List<MessageDto> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return convertToMessageDtoList(messages);
    }

    @GetMapping("/top")
    public List<MessageDto> getTopMessages(@RequestParam("number") int numOfMessages) {
        List<Message> messages = messageService.getAllMessages(numOfMessages);
        return convertToMessageDtoList(messages);
    }

    @PutMapping("/{uuid}")
    public MessageDto updateMessage(@PathVariable String uuid, @RequestBody MessageDto messageDto) throws NoModificableMessageException {
        Message message = messageService.updateMessage(uuid, convertToMessageEntity(messageDto));
        return convertToMessageDto(message);
    }

    @DeleteMapping("/{uuid}")
    public void deleteMessage(@PathVariable String uuid) throws NoModificableMessageException {
        messageService.deleteMessage(uuid);
    }

    private Message convertToMessageEntity (MessageDto messageDto){
        return modelMapper.map(messageDto, Message.class);
    }

    private MessageDto convertToMessageDto (Message message){
        return modelMapper.map(message, MessageDto.class);
    }

    private List<MessageDto> convertToMessageDtoList(List<Message> messages) {
        Type listType = new TypeToken<List<MessageDto>>() {}.getType();
        return modelMapper.map(messages, listType);
    }
}

package com.tantalum.test.message.services;

import com.tantalum.test.message.dtos.UuidDto;
import com.tantalum.test.message.entities.Message;
import com.tantalum.test.message.exceptions.NoModificableMessageException;
import com.tantalum.test.message.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {

    private static final long UPDATE_THRESHOLD_TIME_MILISECONDS = 5000;
    private static final long DELETE_THRESHOLD_TIME_MILISECONDS = 2*60*1000;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${uuidApp.Name}")
    private String uuidAppName;

    @Value("${uuidApp.uuidPath}")
    private String uuidPath;

    public URI serviceUrl() {
        List<ServiceInstance> services = discoveryClient.getInstances(uuidAppName);
        return services.get(0).getUri();
    }

    public Message save(Message message){
        setDate(message);
        setUuid(message);
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> getAllMessages(int numOfMessages) {
        Page<Message> messagesPage=  messageRepository.findMessagesTop(new PageRequest(0, numOfMessages));
        return messagesPage.getContent();
    }

    public Message getMessage(String uuid) {
        return messageRepository.getOne(uuid);
    }

    public Message updateMessage(String uuid, Message message) throws NoModificableMessageException {
        Message messageToUpdate = messageRepository.getOne(uuid);
        if (IsMessageCouldBeUpdated(messageToUpdate)) {
            messageToUpdate.setMessage(message.getMessage());
        }
        else {
            throw new NoModificableMessageException("Message with UUID: " + uuid + " is too old to be modified");
        }

        return messageRepository.save(messageToUpdate);
    }

    public void deleteMessage(String uuid) throws NoModificableMessageException {
        Message messageToDelete = messageRepository.getOne(uuid);
        if (IsMessageCouldBeDeleted(messageToDelete)) {
            messageRepository.delete(messageToDelete);
        }
        else {
            throw new NoModificableMessageException("Message with UUID: " + uuid + " is too new to be deleted");
        }
        messageRepository.deleteById(uuid);
    }

    private boolean IsMessageCouldBeDeleted(Message messageToDelete) {
        return messageToDelete.getCreationDate().getTime() + DELETE_THRESHOLD_TIME_MILISECONDS < Calendar.getInstance().getTimeInMillis();
    }

    private boolean IsMessageCouldBeUpdated(Message messageToModify) {
        return messageToModify.getCreationDate().getTime() + UPDATE_THRESHOLD_TIME_MILISECONDS > Calendar.getInstance().getTimeInMillis();
    }

    private void setUuid(Message message) {
        String url = serviceUrl().toString() + uuidPath;
        ResponseEntity<UuidDto> uuidDto = restTemplate.getForEntity(url, UuidDto.class);
        message.setUuid(uuidDto.getBody().getUuid());
    }

    private void setDate(Message message) {
        java.sql.Timestamp date = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
        message.setCreationDate(date);
    }
}

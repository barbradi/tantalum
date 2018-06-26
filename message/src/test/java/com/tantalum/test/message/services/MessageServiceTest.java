package com.tantalum.test.message.services;

import com.tantalum.test.message.dtos.UuidDto;
import com.tantalum.test.message.entities.Message;
import com.tantalum.test.message.repositories.MessageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceTest {

    private static final String UUID_APP_NAME = "uuidName";
    private static final String UUID_PATH = "path";
    private static final String SERVICE_INSTANCE_URI = "http://serviceInstance.com";
    private static final String UUID = "AnUUID";
    private static final String MESSAGE_CONTENT = "blah blah";

    @InjectMocks
    MessageService messageService;

    @Mock
    MessageRepository messageRepository;

    @Mock
    RestTemplate restTemplate;

    @Mock
    DiscoveryClient discoveryClient;


    @Test
    public void save() throws MalformedURLException, URISyntaxException {
        // Given
        ReflectionTestUtils.setField(messageService, "uuidAppName", UUID_APP_NAME);
        ReflectionTestUtils.setField(messageService, "uuidPath", UUID_PATH);
        Message inputMessage = new Message();
        inputMessage.setMessage(MESSAGE_CONTENT);

        // mock discoveryClient, restemplate and repository helpers
        List<ServiceInstance> expectedServices = getServiceInstances();
        when(discoveryClient.getInstances(eq(UUID_APP_NAME))).thenReturn(expectedServices);

        UuidDto expectedUuidDto = new UuidDto();
        expectedUuidDto.setUuid(UUID);
        ResponseEntity<UuidDto> expectedResponseEntity = new ResponseEntity<>(expectedUuidDto, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(UuidDto.class))).thenReturn(expectedResponseEntity);

        when(messageRepository.save(any(Message.class))).then(returnsFirstArg());

        // When
        Message result = messageService.save(inputMessage);

        // Then
        assertEquals(UUID, result.getUuid());
        assertEquals(MESSAGE_CONTENT, result.getMessage());
    }

    private List<ServiceInstance> getServiceInstances() throws URISyntaxException {
        List<ServiceInstance> expectedServices = new ArrayList<>();
        URI uuidUri = new URI(SERVICE_INSTANCE_URI);
        ServiceInstance serviceInstance =new DefaultServiceInstance(
                "serviceId",
                uuidUri.getHost(),
                uuidUri.getPort(),
                false);
        expectedServices.add(serviceInstance);
        return expectedServices;
    }

    @Test
    public void getAllMessages() {
    }

    @Test
    public void getMessage() {
    }

    @Test
    public void updateMessage() {
    }

    @Test
    public void deleteMessage() {
    }
}
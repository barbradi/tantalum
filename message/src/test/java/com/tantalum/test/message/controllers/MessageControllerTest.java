package com.tantalum.test.message.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tantalum.test.message.dtos.MessageDto;
import com.tantalum.test.message.entities.Message;
import com.tantalum.test.message.services.MessageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.util.Calendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class MessageControllerTest {

    private final static String UUID = "some uuid";
    private final static String MESSAGE_CONTENT = "a message";
    private final static Timestamp A_DATE = new Timestamp(Calendar.getInstance().getTimeInMillis());

    @InjectMocks
    private MessageController messageController;

    @Mock
    private MessageService messageService;

    // Instead of mocking modelMapper behavior lets simplify just this one and use the real implementation
    @Spy
    private ModelMapper modelMapper = new ModelMapper();

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
    }

    @Test
    public void addMessageTest() throws Exception {
        // Given
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage(MESSAGE_CONTENT);

        Message expectedMessage = modelMapper.map(messageDto, Message.class);
        expectedMessage.setUuid(UUID);
        expectedMessage.setCreationDate(A_DATE);
        when(messageService.save(any(Message.class))).thenReturn(expectedMessage);

        // When
        mockMvc
                .perform(post("/message")
                        .content(asJsonString(messageDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message").value(MESSAGE_CONTENT));

        // Then
        verify(messageService, times(1)).save(any(Message.class));
    }

    @Test
    public void getMessage() throws Exception {
        // Given
        Message expectedMessage = new Message();
        expectedMessage.setUuid(UUID);
        expectedMessage.setCreationDate(A_DATE);
        expectedMessage.setMessage(MESSAGE_CONTENT);
        when(messageService.getMessage(eq(UUID))).thenReturn(expectedMessage);

        // When
        mockMvc
                .perform(get("/message/" + UUID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.uuid").value(UUID))
                .andExpect(jsonPath("$.message").value(MESSAGE_CONTENT));

        // Then
        verify(messageService, times(1)).getMessage(eq(UUID));
    }

    @Test
    public void getAllMessages() {
        // TODO
    }

    @Test
    public void updateMessage() {
        // TODO
    }

    @Test
    public void deleteMessage() {
        // TODO
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
//
//@RunWith(MockitoJUnitRunner.class)
//public class OfferControllerTest {
//
//    private static final String OFFER_1_NAME = "offer1";
//    private static final String OFFER_2_NAME = "offer1";
//    private static final long OFFER_1_ID = 1L;
//    private static final long OFFER_2_ID = 2L;
//
//    @InjectMocks
//    private OfferController offerController;
//
//    @Mock
//    private OfferService offerService;
//
//    // Instead of mocking modelMapper behavior lets simplify just this one and use the real implementation
//    @Spy
//    private ModelMapper modelMapper = new ModelMapper();
//
//    private MockMvc mockMvc;
//
//    @Before
//    public void setup() {
//        mockMvc = MockMvcBuilders.standaloneSetup(offerController).build();
//    }
//
//    @Test
//    public void getOffers() throws Exception {
//
//        // Given
//        List<Offer> expectedOffers = getExpectedOfferList();
//        when(offerService.getOffers()).thenReturn(expectedOffers);
//
//        // When
//        mockMvc
//                .perform(get("/offer"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$[0].id").value(OFFER_1_ID))
//                .andExpect(jsonPath("$[0].name").value(OFFER_1_NAME))
//                .andExpect(jsonPath("$[1].id").value(OFFER_2_ID))
//                .andExpect(jsonPath("$[1].name").value(OFFER_2_NAME));
//
//        // Then
//        verify(offerService, times(1)).getOffers();
//    }
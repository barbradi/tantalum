package com.tantalum.test.uuid.controllers;

import com.tantalum.test.uuid.dtos.UuidDto;
import com.tantalum.test.uuid.services.UuidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message-uuid")
public class UuidController {

    @Autowired
    UuidService uuidService;

    @GetMapping
    public UuidDto getUuid() {
        return uuidService.getUuid();
    }
}

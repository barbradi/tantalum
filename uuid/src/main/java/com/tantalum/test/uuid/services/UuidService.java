package com.tantalum.test.uuid.services;

import com.tantalum.test.uuid.dtos.UuidDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UuidService {

    @Value("${prefix}")
    private String prefix;

    @Value("${suffix}")
    private String suffix;

    public UuidDto getUuid() {
        UuidDto uuidDto = new UuidDto();
        uuidDto.setUuid(prefix + "-" +  UUID.randomUUID() + "-" + suffix);
        return uuidDto;
    }
}

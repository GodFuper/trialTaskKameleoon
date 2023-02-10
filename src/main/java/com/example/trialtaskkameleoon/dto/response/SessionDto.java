package com.example.trialtaskkameleoon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link com.example.trialtaskkameleoon.model.Session} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionDto implements Serializable {
    private String cookie;
    private UserDto user;
}
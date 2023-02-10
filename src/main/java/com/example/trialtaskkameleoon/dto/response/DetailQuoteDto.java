package com.example.trialtaskkameleoon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link com.example.trialtaskkameleoon.model.Quote} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailQuoteDto implements Serializable {
    private Long id;
    private String text;
}
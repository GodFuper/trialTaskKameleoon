package com.example.trialtaskkameleoon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link com.example.trialtaskkameleoon.model.Quote} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListQuoteDto implements Serializable {
    private List<QuoteDto> quotes;
    private long count;
}
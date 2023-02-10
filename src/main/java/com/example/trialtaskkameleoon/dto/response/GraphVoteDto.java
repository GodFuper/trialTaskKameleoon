package com.example.trialtaskkameleoon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

/**
 * A DTO for the {@link com.example.trialtaskkameleoon.model.Quote} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphVoteDto implements Serializable {
    private Map<LocalDate, Long> map;
}
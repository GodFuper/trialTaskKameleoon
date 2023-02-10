package com.example.trialtaskkameleoon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link com.example.trialtaskkameleoon.model.Quote} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuoteRequest implements Serializable {
    @NotNull
    @NotBlank
    private String text;
}
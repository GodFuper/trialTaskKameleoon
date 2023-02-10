package com.example.trialtaskkameleoon.model;

/**
 * A Projection for the {@link com.example.trialtaskkameleoon.model.Quote} entity
 */
public interface QuoteScore {
    Quote getQuote();

    Long getScore();
}
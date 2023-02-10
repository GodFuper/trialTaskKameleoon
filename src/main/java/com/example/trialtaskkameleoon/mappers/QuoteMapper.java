package com.example.trialtaskkameleoon.mappers;

import com.example.trialtaskkameleoon.dto.request.QuoteRequest;
import com.example.trialtaskkameleoon.dto.response.DetailQuoteDto;
import com.example.trialtaskkameleoon.dto.response.GraphVoteDto;
import com.example.trialtaskkameleoon.dto.response.ListQuoteDto;
import com.example.trialtaskkameleoon.dto.response.QuoteDto;
import com.example.trialtaskkameleoon.model.Quote;
import com.example.trialtaskkameleoon.model.QuoteScore;
import com.example.trialtaskkameleoon.model.User;
import com.example.trialtaskkameleoon.model.Vote;
import org.mapstruct.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface QuoteMapper {
    @Mapping(target = "user", source = "user")
    @Mapping(target = "text", source = "request.text")
    Quote toQuote(User user, QuoteRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Quote updateQuote(QuoteRequest quoteRequest, @MappingTarget Quote quote);


    @Mapping(target = "score", expression = "java(0L)")
    QuoteDto toQuoteDto(Quote quote);

    @Mapping(target = "id", source = "quote.id")
    @Mapping(target = "user", source = "quote.user")
    @Mapping(target = "score", source = "score")
    QuoteDto toQuoteDto(QuoteScore quote);

    List<QuoteDto> toQuoteDto(Collection<QuoteScore> quotes);

    @Mapping(target = "count", source = "count")
    @Mapping(target = "quotes", source = "quotes")
    ListQuoteDto toListQuoteDto(Collection<QuoteScore> quotes, long count);

    DetailQuoteDto toDetailQuoteDto(Quote quote);

    @Mapping(target = "map", source = "map")
    GraphVoteDto toGraphVoteDto(Map<LocalDate, Long> map);
}
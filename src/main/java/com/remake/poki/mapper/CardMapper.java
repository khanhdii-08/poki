package com.remake.poki.mapper;

import com.remake.poki.dto.CardDTO;
import com.remake.poki.model.Card;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardMapper extends BaseMapper<CardDTO, Card> {

    List<CardDTO> toDTO(List<Card> cards);
}

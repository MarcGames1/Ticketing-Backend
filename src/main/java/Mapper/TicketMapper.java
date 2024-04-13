package Mapper;

import Entities.Ticket;
import Ticket.DTO.CreateTicketDTO;
import Ticket.DTO.UpdateTicketDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TicketMapper {

    TicketMapper INSTANCE = Mappers.getMapper( TicketMapper.class );

    Ticket create(CreateTicketDTO dto);
    Ticket update(@MappingTarget Ticket ticket, UpdateTicketDTO dto);
}
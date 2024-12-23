package Mapper;

import Entities.Ticket;
import Ticket.DTO.BasicTicketDTO;
import Ticket.DTO.CreateTicketDTO;
import Ticket.DTO.TicketDTO;
import Ticket.DTO.UpdateTicketDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TicketMapper {

    TicketMapper INSTANCE = Mappers.getMapper( TicketMapper.class );

    Ticket create(CreateTicketDTO dto);
    Ticket update(@MappingTarget Ticket ticket, UpdateTicketDTO dto);
    List<TicketDTO> getAll(List<Ticket> model);
    TicketDTO get(Ticket model);

    List<BasicTicketDTO> getAllBasic(List<Ticket> models);
}
package Mapper;

import Entities.Ticket;
import Enums.TaskStatus;
import Ticket.DTO.CreateTicketDTO;
import Ticket.DTO.TicketDTO;
import Ticket.DTO.UpdateTicketDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TaskStatusMapper {

    TaskStatusMapper INSTANCE = Mappers.getMapper( TaskStatusMapper.class );

    @ValueMapping(target = "Pending", source = "Pending")
    @ValueMapping(target = "InProgress", source = "InProgress")
    @ValueMapping(target = "Completed", source = "Completed")
    TaskStatus stringToEnum(String source);
}
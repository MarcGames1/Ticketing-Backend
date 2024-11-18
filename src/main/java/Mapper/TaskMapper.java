package Mapper;

import Entities.Task;
import Task.DTO.CreateTaskDTO;
import Task.DTO.TaskDTO;
import Task.DTO.UpdateTaskDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper( TaskMapper.class );

    Task create(CreateTaskDTO dto);
    Task update(@MappingTarget Task task, UpdateTaskDTO dto);
    List<TaskDTO> getAll(List<Task> model);
    TaskDTO get(Task model);
}
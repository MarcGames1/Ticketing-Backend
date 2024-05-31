package Mapper;


import Entities.Attachment;

import com.example.demo.Attachments.DTO.CreateAttachmentDTO;
import com.example.demo.Attachments.DTO.UpdateAttachmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AttachmentMapper {
    AttachmentMapper INSTANCE = Mappers.getMapper(AttachmentMapper.class);

    Attachment create (CreateAttachmentDTO dto);

    Attachment update(@MappingTarget Attachment attachment, UpdateAttachmentDTO dto);
}

package com.example.demo.Attachments;

import Entities.Attachment;
import Mapper.AttachmentMapper;
import com.example.demo.Attachments.DTO.CreateAttachmentDTO;
import com.example.demo.awsServices.S3Uploader;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.io.InputStream;

@ApplicationScoped
@Transactional
public class AttachmentService {
    @PersistenceContext
    private EntityManager entityManager;



    @Inject
    private S3Uploader uploader;

    public AttachmentService() {}

    public Long create(CreateAttachmentDTO dto) {
        Attachment attachment = AttachmentMapper.INSTANCE.create(dto);
        entityManager.persist(attachment);
        return attachment.getId();
    }
}

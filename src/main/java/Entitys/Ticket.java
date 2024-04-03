package Entitys;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;

    @OneToOne
    @JoinColumn(name = "attachment_id")
    private Attachment attachment;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @OneToMany(mappedBy = "ticket")
    private List<Task> tasks;
    @Column(name = "status")
    private String status;

    public Ticket() {}

    public Ticket(User createdBy, User assignedTo, Attachment attachment, String title, String content, String status) {
        this.createdBy = createdBy;
        this.assignedTo = assignedTo;
        this.attachment = attachment;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Attachment getAttachId() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        // todo remove previous attachment from s3bucket
        this.attachment = attachment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // TODO Tasks Methods
}

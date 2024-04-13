package Entities;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @OneToMany(mappedBy = "ticket",fetch = FetchType.LAZY)
    private List<Attachment> attachments;
    @OneToMany(mappedBy = "ticket",fetch = FetchType.LAZY)
    private List<Task> tasks;
    public Ticket() {}

    public Ticket(Long id, String title, String content, List<Attachment> attachments, List<Task> tasks) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.attachments = attachments;
        this.tasks = tasks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
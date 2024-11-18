package Entities;

import Enums.TaskStatus;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "tasks")
public class Task extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Enumerated(EnumType.ORDINAL)
    private TaskStatus status;

    @Column (name = "title")
    private String title;

    @Column (name = "description")
    private String description;

    @Column(name = "tracking_id", nullable = false, unique = true)
    private String trackingId;

    @OneToMany(mappedBy = "task",fetch = FetchType.LAZY)
    private List<Attachment> attachments;

    public Task(){
        this.trackingId = generateTrackingID();
    }

    public Task(Long id, User user, Ticket ticket, List<Attachment> attachments,TaskStatus status, String title, String description) {
        this.id = id;
        this.user = user;
        this.ticket = ticket;
        this.status = status;
        this.title = title;
        this.attachments = attachments;
        this.description = description;
        this.trackingId = generateTrackingID();
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }
    public Long getUserId(){
        if(user == null) return null;
        return user.getId();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    private static String generateTrackingID() {
        LocalDateTime now = LocalDateTime.now();

        String timestamp = now.toString().replace("-", "")
                .replace(":", "")
                .replace("T", "")
                .replace(".", "");

        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;

        return timestamp + randomNumber;
    }
}
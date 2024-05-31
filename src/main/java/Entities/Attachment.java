package Entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "attachments")
public class Attachment implements Serializable {
//    TODO Attachment with url and id from s3bucket
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "S3bucketId")
    private String s3Id;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;


    // Added s3bucket ID instead of name
    // TODO add name field if you think is necesary


    public String getUrl() {
        return url;
    }

    private void setUrl(String url) {
        this.url = url;
    }

    public String getS3Id() {
        return s3Id;
    }

    public void setS3Id(String s3Id) {
        this.s3Id = s3Id;
    }

    public static void delete (Attachment attachment) {
        // TODO attachment.s3Id  delete
    }

    public Long getId() {
        return this.id;
    }
}
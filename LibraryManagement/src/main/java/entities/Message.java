package entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender", referencedColumnName = "username", nullable = false)
    private Person sender;

    @ManyToOne
    @JoinColumn(name = "receiver", referencedColumnName = "username", nullable = false)
    private Person receiver;

    @Column(name = "content")
    private String content;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    public Message() {}

    public Message(Person senderId, Person receiverId, String content, Timestamp timestamp) {
        this.sender = senderId;
        this.receiver = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getSenderId() {
        return sender;
    }

    public void setSenderId(Person senderId) {
        this.sender = senderId;
    }

    public Person getReceiverId() {
        return receiver;
    }

    public void setReceiverId(Person receiverId) {
        this.receiver = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


}
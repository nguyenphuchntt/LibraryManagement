package entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "announcement")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int announcement_id;

    private String content;

    private LocalDate start_date;

    private LocalDate end_date;

    public Announcement() {}

    public Announcement(String content, LocalDate start_date, LocalDate end_date) {
        this.content = content;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public int getAnnouncement_id() {
        return announcement_id;
    }

    public void setAnnouncement_id(int announcement_id) {
        this.announcement_id = announcement_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }


}

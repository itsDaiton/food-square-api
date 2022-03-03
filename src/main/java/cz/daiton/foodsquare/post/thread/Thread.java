package cz.daiton.foodsquare.post.thread;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "thread")
public class Thread {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    private String header;

    private String content;

    public Thread() {

    }

    public Thread(Long id, String header, String content) {
        this.id = id;
        this.header = header;
        this.content = content;
    }

    public Thread(String header, String content) {
        this.header = header;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

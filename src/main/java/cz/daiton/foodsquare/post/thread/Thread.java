package cz.daiton.foodsquare.post.thread;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.daiton.foodsquare.post.Post;

import javax.persistence.*;

@Entity(name = "thread")
public class Thread {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    private String header;

    private String content;

    @JsonIgnore
    @OneToOne(
            mappedBy = "thread"
    )
    private Post post;

    public Thread() {

    }

    public Thread(Long id, String header, String content, Post post) {
        this.id = id;
        this.header = header;
        this.content = content;
        this.post = post;
    }

    public Thread(String header, String content, Post post) {
        this.header = header;
        this.content = content;
        this.post = post;
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}

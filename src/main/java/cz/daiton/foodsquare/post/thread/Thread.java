package cz.daiton.foodsquare.post.thread;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.daiton.foodsquare.appuser.AppUser;
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

    @ManyToOne
    @JoinColumn(
            name = "app_user_id",
            nullable = false
    )
    private AppUser appUser;

    public Thread() {

    }

    public Thread(Long id, String header, String content, Post post, AppUser appUser) {
        this.id = id;
        this.header = header;
        this.content = content;
        this.post = post;
        this.appUser = appUser;
    }

    public Thread(String header, String content, Post post, AppUser appUser) {
        this.header = header;
        this.content = content;
        this.post = post;
        this.appUser = appUser;
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

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}

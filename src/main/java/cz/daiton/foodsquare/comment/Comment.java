package cz.daiton.foodsquare.comment;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.post.Post;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "user_comment")
public class Comment {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(
            length = 100,
            nullable = false
    )
    private String text;

    @Column(
            name = "commented_at"
    )
    private LocalDateTime commentedAt;

    @ManyToOne
    @JoinColumn(name = "appuser_id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment() {

    }

    public Comment(Long id, String text, LocalDateTime commentedAt, AppUser appUser, Post post) {
        this.id = id;
        this.text = text;
        this.commentedAt = commentedAt;
        this.appUser = appUser;
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(LocalDateTime commentedAt) {
        this.commentedAt = commentedAt;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}
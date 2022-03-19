package cz.daiton.foodsquare.comment;

import java.time.LocalDateTime;

public class CommentDto {

    private Long id;

    private String text;

    private LocalDateTime commentedAt;

    private Long appUser;

    private Long post;

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

    public Long getAppUser() {
        return appUser;
    }

    public void setAppUser(Long appUser) {
        this.appUser = appUser;
    }

    public Long getPost() {
        return post;
    }

    public void setPost(Long post) {
        this.post = post;
    }
}

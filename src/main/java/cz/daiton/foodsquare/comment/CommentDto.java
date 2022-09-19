package cz.daiton.foodsquare.comment;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class CommentDto {

    private final String required = "This field is required.";

    @NotEmpty(message = required)
    @Size(max = 500, message = "Comment can contain a maximum of 500 characters.")
    private String text;

    private LocalDateTime commentedAt;

    private Long appUser;

    private Long post;

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

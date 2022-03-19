package cz.daiton.foodsquare.like;

import java.time.LocalDateTime;

public class LikeDto {

    private Long id;

    private LocalDateTime likedAt;

    private Long appUser;

    private Long post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getLikedAt() {
        return likedAt;
    }

    public void setLikedAt(LocalDateTime likedAt) {
        this.likedAt = likedAt;
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

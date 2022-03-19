package cz.daiton.foodsquare.like;

import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.post.Post;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "user_like")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"appuser_id", "post_id"})
})
public class Like {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(
            name = "liked_at"
    )
    private LocalDateTime likedAt;

    @ManyToOne
    @JoinColumn(name = "appuser_id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Like() {

    }

    public Like(Long id, LocalDateTime likedAt, AppUser appUser, Post post) {
        this.id = id;
        this.likedAt = likedAt;
        this.appUser = appUser;
        this.post = post;
    }

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
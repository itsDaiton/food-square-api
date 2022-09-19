package cz.daiton.foodsquare.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.daiton.foodsquare.appuser.AppUser;
import cz.daiton.foodsquare.comment.Comment;
import cz.daiton.foodsquare.like.Like;
import cz.daiton.foodsquare.post.meal.Meal;
import cz.daiton.foodsquare.post.review.Review;
import cz.daiton.foodsquare.post.thread.Thread;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "post")
public class Post {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(
            name = "created_at"
    )
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(
            name = "appuser_id",
            nullable = false,
            updatable = false
    )
    private AppUser appUser;

    @OneToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "meal_id",
            referencedColumnName = "id",
            unique = true
    )
    private Meal meal;

    @OneToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "review_id",
            referencedColumnName = "id",
            unique = true
    )
    private Review review;

    @OneToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "thread_id",
            referencedColumnName = "id",
            unique = true
    )
    private Thread thread;

    @OneToMany(
            mappedBy = "id",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(
            mappedBy = "id",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private Set<Like> likes = new HashSet<>();

    public Post() {

    }

    public Post(Long id, LocalDateTime createdAt, AppUser appUser, Meal meal, Review review, Thread thread, Set<Comment> comments, Set<Like> likes) {
        this.id = id;
        this.createdAt = createdAt;
        this.appUser = appUser;
        this.meal = meal;
        this.review = review;
        this.thread = thread;
        this.comments = comments;
        this.likes = likes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Like> getLikes() {
        return likes;
    }

    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }
}

package cz.daiton.foodsquare.post;

import java.time.LocalDateTime;

public class PostDto {

    private Long id;

    private LocalDateTime createdAt;

    private Long appUser;

    private Long meal;

    private Long review;

    private Long thread;

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

    public Long getAppUser() {
        return appUser;
    }

    public void setAppUser(Long appUser) {
        this.appUser = appUser;
    }

    public Long getMeal() {
        return meal;
    }

    public void setMeal(Long meal) {
        this.meal = meal;
    }

    public Long getReview() {
        return review;
    }

    public void setReview(Long review) {
        this.review = review;
    }

    public Long getThread() {
        return thread;
    }

    public void setThread(Long thread) {
        this.thread = thread;
    }
}

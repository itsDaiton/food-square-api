package cz.daiton.foodsquare.post.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.daiton.foodsquare.post.Post;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Entity(name = "review")
public class Review {

    @Transient
    private final String required = "This field is required.";

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = required)
    private String header;

    @Column(nullable = false)
    @NotEmpty(message = required)
    private String content;

    @Column(nullable = false)
    @NotNull(message = required)
    @DecimalMin(value = "1.00", message = "You must rate at least 1 star.")
    @DecimalMax(value = "5.00", message = "You cannot rate more than 5 stars.")
    @Digits(integer = 1, fraction = 0, message = "Rating cannot be a decimal number.")
    private BigDecimal rating;

    @Column(
            name = "path_to_image"
    )
    private String pathToImage;

    @JsonIgnore
    @OneToOne(
            mappedBy = "review"
    )
    private Post post;

    public Review() {

    }

    public Review(Long id, String header, String content, BigDecimal rating, String pathToImage, Post post) {
        this.id = id;
        this.header = header;
        this.content = content;
        this.rating = rating;
        this.pathToImage = pathToImage;
        this.post = post;
    }

    public Review(String header, String content, BigDecimal rating, String pathToImage, Post post) {
        this.header = header;
        this.content = content;
        this.rating = rating;
        this.pathToImage = pathToImage;
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

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public String getPathToImage() {
        return pathToImage;
    }

    public void setPathToImage(String pathToImage) {
        this.pathToImage = pathToImage;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}

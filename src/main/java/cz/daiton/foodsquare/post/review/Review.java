package cz.daiton.foodsquare.post.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.daiton.foodsquare.post.Post;

import javax.persistence.*;

@Entity(name = "review")
public class Review {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    private String header;

    private String content;

    private Integer rating;

    //TODO: omezit hodnoty ratingu jen na 1 - 5

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

    public Review(Long id, String header, String content, Integer rating, String pathToImage, Post post) {
        this.id = id;
        this.header = header;
        this.content = content;
        this.rating = rating;
        this.pathToImage = pathToImage;
        this.post = post;
    }

    public Review(String header, String content, Integer rating, String pathToImage, Post post) {
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
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

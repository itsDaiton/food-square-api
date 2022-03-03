package cz.daiton.foodsquare.post.review;

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

    @Column(
            name = "path_to_image"
    )
    private String pathToImage;


    public Review() {

    }

    public Review(Long id, String header, String content, Integer rating, String pathToImage) {
        this.id = id;
        this.header = header;
        this.content = content;
        this.rating = rating;
        this.pathToImage = pathToImage;
    }

    public Review(String header, String content, Integer rating, String pathToImage) {
        this.header = header;
        this.content = content;
        this.rating = rating;
        this.pathToImage = pathToImage;
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
}

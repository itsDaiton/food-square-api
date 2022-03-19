package cz.daiton.foodsquare.appuser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.daiton.foodsquare.comment.Comment;
import cz.daiton.foodsquare.like.Like;
import cz.daiton.foodsquare.post.Post;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(
            unique = true
    )
    private String email;

    @Column(
            unique = true,
            name = "username"
    )
    private String userName;

    @Column(
            name = "first_name"
    )
    private String firstName;

    @Column(
            name = "last_name"
    )
    private String lastName;

    private String password;

    @Column(
            name = "path_to_image"
    )
    private String pathToImage;

    @Enumerated(
            EnumType.STRING
    )
    private AppUserRole role;

    @OneToMany(mappedBy = "appUser")
    @JsonIgnore
    private Set<Post> posts;

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

    public AppUser() {

    }

    public AppUser(Long id, String email, String userName, String firstName, String lastName, String password, String pathToImage, AppUserRole role, Set<Post> posts, Set<Comment> comments, Set<Like> likes) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.pathToImage = pathToImage;
        this.role = role;
        this.posts = posts;
        this.comments = comments;
        this.likes = likes;
    }

    public AppUser(String email, String userName, String firstName, String lastName, String password, String pathToImage, AppUserRole role, Set<Post> posts, Set<Comment> comments, Set<Like> likes) {
        this.email = email;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.pathToImage = pathToImage;
        this.role = role;
        this.posts = posts;
        this.comments = comments;
        this.likes = likes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPathToImage() {
        return pathToImage;
    }

    public void setPathToImage(String pathToImage) {
        this.pathToImage = pathToImage;
    }

    public AppUserRole getRole() {
        return role;
    }

    public void setRole(AppUserRole role) {
        this.role = role;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
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

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", pathToImage='" + pathToImage + '\'' +
                ", role=" + role +
                '}';
    }
}
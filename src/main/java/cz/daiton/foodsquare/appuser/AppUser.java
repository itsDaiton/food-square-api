package cz.daiton.foodsquare.appuser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.daiton.foodsquare.comment.Comment;
import cz.daiton.foodsquare.follow.Follow;
import cz.daiton.foodsquare.recipe.Recipe;
import cz.daiton.foodsquare.review.Review;
import cz.daiton.foodsquare.role.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "app_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Class representing a user registered in the application.")
public class AppUser {

    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String required = "This field is required.";

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Schema(description = "Unique identifier for user.", example = "1")
    private Long id;

    @Column(
            unique = true
    )
    @NotNull(message = required)
    @Email(message = "This is not a valid e-mail address.")
    @Schema(description = "Email of the user.", example = "user@example.com")
    private String email;

    @Column(
            unique = true,
            name = "username"
    )
    @NotNull(message = required)
    @Size(min = 2, max = 30, message = "Username must be between 2 and 30 characters long.")
    @Schema(description = "Username of the user.", example = "user123")
    private String userName;

    @Column(
            name = "first_name"
    )
    @Schema(description = "First name of the user.", example = "John")
    private String firstName;

    @Column(
            name = "last_name"
    )
    @Schema(description = "Last name of the user.", example = "Doe")
    private String lastName;

    @JsonIgnore
    @NotNull(message = required)
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    private String password;

    @Column(
            name = "path_to_image"
    )
    @Schema(description = "Path to a image file representing profile picture of a user.", example = "/img/users/1.png")
    private String pathToImage;

    @OneToMany(mappedBy = "appUser")
    @JsonIgnore
    private Set<Recipe> recipes;

    @OneToMany(mappedBy = "appUser")
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @JsonIgnore
    private Set<Review> reviews = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "app_user_roles",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_role_id")
    )
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "follower")
    @JsonIgnore
    private Set<Follow> followers;

    @OneToMany(mappedBy = "followed")
    @JsonIgnore
    private Set<Follow> followed;

    @ManyToMany
    @JoinTable(
            name = "comment_likes",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id"),
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"app_user_id", "comment_id"})
            }
    )
    @JsonIgnore
    private Set<Comment> likedComments;

    @ManyToMany
    @JoinTable(
            name = "review_likes",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "review_id"),
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"app_user_id", "review_id"})
            }
    )
    @JsonIgnore
    private Set<Review> likedReviews;

    @ManyToMany
    @JoinTable(
            name = "favorite_recipes",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"),
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"app_user_id", "recipe_id"})
            }
    )
    @JsonIgnore
    private Set<Recipe> favoriteRecipes;

    public AppUser(String email, String userName, String password) {
        this.email = email;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return id.equals(appUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
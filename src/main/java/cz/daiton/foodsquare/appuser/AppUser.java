package cz.daiton.foodsquare.appuser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.daiton.foodsquare.comment.Comment;
import cz.daiton.foodsquare.follow.Follow;
import cz.daiton.foodsquare.recipe.Recipe;
import cz.daiton.foodsquare.review.Review;
import cz.daiton.foodsquare.role.Role;
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
public class AppUser {

    @Transient
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String required = "This field is required.";

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(
            unique = true
    )
    @NotNull(message = required)
    @Email(message = "This is not valid e-mail address.")
    private String email;

    @Column(
            unique = true,
            name = "username"
    )
    @NotNull(message = required)
    @Size(min = 2, max = 30, message = "Username must be between 2 and 30 characters long.")
    private String userName;

    @Column(
            name = "first_name"
    )
    private String firstName;

    @Column(
            name = "last_name"
    )
    private String lastName;

    @JsonIgnore
    @NotNull(message = required)
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    private String password;

    @Column(
            name = "path_to_image"
    )
    private String pathToImage;

    @OneToMany(mappedBy = "appUser")
    @JsonIgnore
    private Set<Recipe> recipes;

    @OneToMany(
            mappedBy = "id",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();

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

    public AppUser(@NotNull(message = required) @Email(message = "This is not valid e-mail address.") String email, @NotNull(message = required) @Size(min = 2, max = 30, message = "Username must be between 2 and 30 characters long.") String userName, @NotNull(message = required) @Size(min = 6, message = "Password must be at least 6 characters long.") String password) {
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
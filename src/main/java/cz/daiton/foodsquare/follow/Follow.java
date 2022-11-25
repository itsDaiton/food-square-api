package cz.daiton.foodsquare.follow;

import cz.daiton.foodsquare.appuser.AppUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "follow")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"follower_id", "followed_id"})
})
@Check(constraints = "follower_id <> followed_id")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Class representing follow relation between two users.")
public class Follow {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Schema(description = "Unique identifier for follow relation.", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private AppUser follower;

    @ManyToOne
    @JoinColumn(name = "followed_id")
    private AppUser followed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Follow follow = (Follow) o;
        return id.equals(follow.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

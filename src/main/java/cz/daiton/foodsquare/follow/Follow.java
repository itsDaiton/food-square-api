package cz.daiton.foodsquare.follow;

import cz.daiton.foodsquare.appuser.AppUser;
import lombok.*;
import org.hibernate.annotations.Check;

import javax.persistence.*;

@Entity(name = "follow")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"follower_id", "followed_id"})
})
@Check(constraints = "follower_id <> followed_id")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Follow {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private AppUser follower;

    @ManyToOne
    @JoinColumn(name = "followed_id")
    private AppUser followed;
}

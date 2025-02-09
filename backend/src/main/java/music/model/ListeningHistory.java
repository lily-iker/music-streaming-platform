package music.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "listening_history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListeningHistory extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "song_id")
    private Song song;
}

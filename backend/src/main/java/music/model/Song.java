package music.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "song")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Song extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "duration")
    private int duration;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Column(name = "song_url", length = 512)
    private String songUrl;

    @Column(name = "like_count")
    private long likeCount = 0;

    @ManyToMany(mappedBy = "songs")
    @JsonIgnore
    private Set<Genre> genres;

    @ManyToMany
    @JoinTable(
            name = "artist_songs",
            joinColumns = @JoinColumn(name = "song_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    @JsonIgnore
    private Set<Artist> artists = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    @JsonIgnore
    private Album album;

    @ManyToMany
    @JsonIgnore
    private Set<Playlist> playlists = new HashSet<>();

    @OneToMany(mappedBy = "song")
    @JsonIgnore
    private Set<ListeningHistory> listeningHistories = new HashSet<>();
}

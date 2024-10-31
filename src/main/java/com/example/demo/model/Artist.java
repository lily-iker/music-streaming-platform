package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "artist")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Artist extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "bio", length = 2048)
    private String bio;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Column(name = "followers")
    private long followers = 0;

    @ManyToMany
    private Set<Song> songs = new HashSet<>();

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Album> albums = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void saveSong(Song song) {
        if (song != null) {
            if (this.songs == null) {
                this.songs = new HashSet<>();
            }
            if (song.getArtists() == null) {
                song.setArtists(new HashSet<>());
            }
            this.songs.add(song);
            song.getArtists().add(this);
        }
    }

    public void saveAlbum(Album album) {
        if (album != null) {
            if (this.albums == null) {
                this.albums = new HashSet<>();
            }
            this.albums.add(album);
            album.setArtist(this);
        }
    }

    public void saveUser(User user) {
        if (user != null) {
            this.user = user;
        }
    }

}

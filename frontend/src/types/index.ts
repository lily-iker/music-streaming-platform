export interface Song {
	id: number;
	name: string;
	artistIds: number[];
    artistNames: string[];
	artistImageUrls: string[];
	albumId: number | null;
    albumName: string | null;
	albumImageUrl: string | null;
    genreIds: number[];
    genreNames: string[];
	imageUrl: string;
	songUrl: string;
	duration: number;
    likeCount: number;
}

export interface Artist {
	id: number;
	name: string;
	imageUrl: string;
	followers: number;
	albumIds: number[];
	albumNames: string[];
	albumImageUrls: string[];
	songs: Song[];
}

export interface Album {
	id: number;
	name: string;
	artistName: string;
    artistId: number;
	imageUrl: string;
	songs: Song[];
}

export interface Genre {
	id: number;
	name: string;
}

export interface Stats {
	totalSongs: number;
	totalAlbums: number;
	totalUsers: number;
	totalArtists: number;
}


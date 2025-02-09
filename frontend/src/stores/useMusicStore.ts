import { Album, Artist, Genre, Song, Stats } from "@/types";
import { create } from "zustand";
import axiosInstance from '../lib/axios-custom'
import { toast } from "react-hot-toast";

interface PaginationState {
  pageNo: number
  pageSize: number
  totalPages: number
}

interface MusicStore {
  // General state
  isLoading: boolean;
  error: string | null;
  
  // Pagination state
  songPagination: PaginationState
  albumPagination: PaginationState
  artistPagination: PaginationState
  
  // Music data
  songs: Song[];
  albums: Album[];
  artists: Artist[];
  genres: Genre[];
  song: Song | null;
  currentArtist: Artist | null;
  currentAlbum: Album | null;
  featuredSongs: Song[];
  madeForYouSongs: Song[];
  trendingSongs: Song[];
  topArtists: Artist[];
  stats: Stats;
  
  // Search state
  searchQuery: string;
  searchSongs: Song[];
  searchArtists: Artist[];
  searchAlbums: Album[];
  
  // Actions
  setSongPageNo: (pageNo: number) => void;
  setAlbumPageNo: (pageNo: number) => void;
  setArtistPageNo: (pageNo: number) => void;
  setSearchQuery: (query: string) => void;
  
  // Fetch functions
  fetchSongs: (pageNo: number, pageSize: number) => Promise<void>;
  fetchSongById: (id: number) => Promise<void>;
  fetchArtists: (pageNo: number, pageSize: number) => Promise<void>;
  fetchArtistById: (id: number) => Promise<void>;
  fetchAlbums: (pageNo: number, pageSize: number) => Promise<void>;
  fetchAlbumById: (id: number) => Promise<void>;
  fetchGenres: () => Promise<void>;
  fetchFeaturedSongs: () => Promise<void>;
  fetchMadeForYouSongs: () => Promise<void>;
  fetchTrendingSongs: () => Promise<void>;
  fetchTopArtists: () => Promise<void>;
  fetchStats: () => Promise<void>;
  
  // Search functions
  searchSongByName: (name: string, pageNo: number, pageSize: number) => Promise<void>;
  searchArtistByName: (name: string, pageNo: number, pageSize: number) => Promise<void>;
  searchAlbumByName: (name: string, pageNo: number, pageSize: number) => Promise<void>;
  
  // Other actions
  clearSearch: () => void;
  addSong: (formData: FormData) => Promise<void>;
  addAlbum: (formData: FormData) => Promise<void>;
  deleteSong: (id: number) => Promise<void>;
  deleteSearchSong: (id: number) => void;
  deleteAlbum: (id: number) => Promise<void>;
  deleteSearchAlbum: (id: number) => void;
}

export const useMusicStore = create<MusicStore>((set) => ({
  // Initialize state
  isLoading: false,
  error: null,
  songPagination: {
    pageNo: 0,
    pageSize: 10,
    totalPages: 0,
  },
  albumPagination: {
    pageNo: 0,
    pageSize: 10,
    totalPages: 0,
  },
  artistPagination: {
    pageNo: 0,
    pageSize: 10,
    totalPages: 0,
  },
  songs: [],
  artists: [],
  albums: [],
  genres: [],
  song: null,
  currentArtist: null,
  currentAlbum: null,
  featuredSongs: [],
  madeForYouSongs: [],
  trendingSongs: [],
  topArtists: [],
  stats: {
    totalSongs: 0,
    totalAlbums: 0,
    totalUsers: 0,
    totalArtists: 0,
  },
  searchQuery: "",
  searchSongs: [],
  searchArtists: [],
  searchAlbums: [],

  // Actions
  setSongPageNo: (pageNo) =>
    set((state) => ({
      songPagination: { ...state.songPagination, pageNo },
    })),

  setAlbumPageNo: (pageNo) =>
    set((state) => ({
      albumPagination: { ...state.albumPagination, pageNo },
    })),

  setArtistPageNo: (pageNo) =>
    set((state) => ({
      albumPagination: { ...state.albumPagination, pageNo },
    })),
  setSearchQuery: (query) => set({ searchQuery: query }),

  // Fetch functions
  fetchSongs: async (pageNo, pageSize) => {
    set({ isLoading: true, error: null });
    try {
      const response = await axiosInstance.get("/api/song/all", {
        params: {
          pageNo: pageNo,
          pageSize: pageSize,
          sortBy: 'id:asc',
        }
      });
      set(() => ({
        songPagination: {
          pageNo: response.data.result.pageNo,
          pageSize: response.data.result.pageSize,
          totalPages: response.data.result.totalPages,
        },
        songs: response.data.result.items,
      }))
    } catch (error: any) {
      set({ error: error.response.data.message });
    } finally {
      set({ isLoading: false });
    }
  },

  fetchSongById: async (id) => {
    set({ isLoading: true, error: null });
    try {
      const response = await axiosInstance.get(`/api/song/${id}`);
      set({ song: response.data.result });
    } catch (error: any) {
      set({ error: error.response.data.message });
    } finally {
      set({ isLoading: false });
    }
  },

  fetchArtists: async (pageNo, pageSize) => {
    set({ isLoading: true, error: null });
    try {
      const response = await axiosInstance.get("/api/artist/all", {
        params: {
          pageNo: pageNo,
          pageSize: pageSize,
          sortBy: 'id:asc',
        }
      });
      set(() => ({
        artistPagination: {
          pageNo: response.data.result.pageNo,
          pageSize: response.data.result.pageSize,
          totalPages: response.data.result.totalPages,
        },
        artists: response.data.result.items,
      }))
    } catch (error: any) {
      set({ error: error.response.data.message });
    } finally {
      set({ isLoading: false });
    }
  },

  fetchArtistById: async (id) => {
    set({ isLoading: true, error: null });
    try {
      const response = await axiosInstance.get(`/api/artist/${id}`);
      set({ currentArtist: response.data.result });
    } catch (error: any) {
      set({ error: error.response.data.message });
    } finally {
      set({ isLoading: false });
    }
  },

  fetchAlbums: async (pageNo, pageSize) => {
    set({ isLoading: true, error: null });
    try {
      const response = await axiosInstance.get("/api/album/all", {
        params: {
          pageNo: pageNo,
          pageSize: pageSize,
          sortBy: 'id:asc',
        }
      });
      set(() => ({
        albumPagination: {
          pageNo: response.data.result.pageNo,
          pageSize: response.data.result.pageSize,
          totalPages: response.data.result.totalPages,
        },
        albums: response.data.result.items,
      }))
    } catch (error: any) {
      set({ error: error.response.data.message });
    } finally {
      set({ isLoading: false });
    }
  },

  fetchAlbumById: async (id) => {
    set({ isLoading: true, error: null });
    try {
      const response = await axiosInstance.get(`/api/album/${id}`);
      set({ currentAlbum: response.data.result });
    } catch (error: any) {
      set({ error: error.response.data.message });
    } finally {
      set({ isLoading: false });
    }
  },

  fetchFeaturedSongs: async () => {
    set({ isLoading: true, error: null });
    try {
      const response = await axiosInstance.get("/api/song/featured-songs");
      set({ featuredSongs: response.data.result });
    } catch (error: any) {
      set({ error: error.response.data.message });
    } finally {
      set({ isLoading: false });
    }
  },

  fetchGenres: async () => {
    set({ isLoading: true, error: null });
    try {
      const response = await axiosInstance.get("/api/genre/all");
      set({ genres: response.data.result });
    } catch (error: any) {
      set({ error: error.response.data.message });
    } finally {
      set({ isLoading: false });
    }
  },

  fetchMadeForYouSongs: async () => {
    set({ isLoading: true, error: null });
    try {
      const response = await axiosInstance.get("/api/song/made-for-you-songs");
      set({ madeForYouSongs: response.data.result });
    } catch (error: any) {
      set({ error: error.response.data.message });
    } finally {
      set({ isLoading: false });
    }
  },

  fetchTrendingSongs: async () => {
    set({ isLoading: true, error: null });
    try {
      const response = await axiosInstance.get("/api/song/trending-songs");
      set({ trendingSongs: response.data.result });
    } catch (error: any) {
      set({ error: error.response.data.message });
    } finally {
      set({ isLoading: false });
    }
  },

  fetchTopArtists: async () => {
    set({ isLoading: true, error: null });
    try {
      const response = await axiosInstance.get("/api/artist/top-artists");
      set({ topArtists: response.data.result });
    } catch (error: any) {
      set({ error: error.response.data.message });
    } finally {
      set({ isLoading: false });
    }
  },

  fetchStats: async () => {
    set({ isLoading: true, error: null });
    try {
      const response = await axiosInstance.get("/api/stats");
      set({ stats: response.data.result });
    } catch (error: any) {
      set({ error: error.message });
    } finally {
      set({ isLoading: false });
    }
  },

  // Search functions
  searchSongByName: async (name, pageNo, pageSize) => {
    set({ isLoading: true, error: null });
    try {
      const response = await axiosInstance.get(`api/song/find-by-name`, {
        params: {
          songName: name,
          pageNo,
          pageSize,
        }
      });
      set(() => ({
        searchSongs: response.data.result.items,
        songPagination: {
        pageNo: response.data.result.pageNo,
        pageSize: response.data.result.pageSize,
        totalPages: response.data.result.totalPages,
        },
      }))
    } catch (error: any) {
      set({ error: error.response.data.message });
    } finally {
      set({ isLoading: false });
    }
  },

  searchArtistByName: async (name, pageNo, pageSize) => {
    set({ isLoading: true, error: null });
    try {
      const response = await axiosInstance.get(`api/artist/find-by-name`, {
        params: { 
          artistName: name,
          pageNo,
          pageSize,
	    	}
      });
      set(() => ({
        searchArtists: response.data.result.items,
        artistPagination: {
        pageNo: response.data.result.pageNo,
        pageSize: response.data.result.pageSize,
        totalPages: response.data.result.totalPages,
        },
      }))
    } catch (error: any) {
      set({ error: error.response.data.message });
    } finally {
      set({ isLoading: false });
    }
  },

  searchAlbumByName: async (name, pageNo, pageSize) => {
    set({ isLoading: true, error: null });
    try {
      const response = await axiosInstance.get(`api/album/find-by-name`, {
        params: { 
          albumName: name,
          pageNo,
          pageSize,
        }
      });
      set(() => ({
        searchAlbums: response.data.result.items,
        albumPagination: {
        pageNo: response.data.result.pageNo,
        pageSize: response.data.result.pageSize,
        totalPages: response.data.result.totalPages,
        },
      }))
    } catch (error: any) {
      set({ error: error.response.data.message });
    } finally {
      set({ isLoading: false });
    }
  },

  // Other actions
  clearSearch: () => set({
    searchQuery: "",
    searchSongs: [],
    searchArtists: [],
    searchAlbums: [],
    songPagination: {
      pageNo: 0,
      pageSize: 10,
      totalPages: 0,
    },
    albumPagination: {
      pageNo: 0,
      pageSize: 10,
      totalPages: 0,
    },
    artistPagination: {
      pageNo: 0,
      pageSize: 10,
      totalPages: 0,
    },
  }),

  addSong: async (formData: FormData) => {
    set({ isLoading: true, error: null })
    try {
      await axiosInstance.post("/api/song", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      set((state) => ({
        stats: {
          ...state.stats,
          totalSongs: state.stats.totalSongs + 1,
        },
      }))
      toast.success("Song added successfully")
    } catch (error: any) {
      console.error("Error in addSong", error)
      toast.error("Error adding song: " + error.response?.data?.message || "Unknown error")
    } finally {
      set({ isLoading: false })
    }
  },

  addAlbum: async (formData: FormData) => {
    set({ isLoading: true, error: null })
    try {
      await axiosInstance.post("/api/album", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      set((state) => ({
        stats: {
          ...state.stats,
          totalAlbums: state.stats.totalAlbums + 1,
        },
      }))
			toast.success("Album created successfully");
    } catch (error: any) {
      console.error("Error in addAlbum", error)
      toast.error("Error adding album: " + error.response?.data?.message || "Unknown error")
    } finally {
      set({ isLoading: false })
    }
  },

  deleteSong: async (id) => {
    set({ isLoading: true, error: null });
    try {
      await axiosInstance.delete(`/api/song/${id}`);
      set((state) => ({
        songs: state.songs.filter((song) => song.id !== id),
        stats: {
          ...state.stats,
          totalSongs: state.stats.totalSongs - 1,
        },
      }));
      toast.success("Song deleted successfully");
    } catch (error: any) {
      console.log("Error in deleteSong", error);
      toast.error("Error deleting song");
    } finally {
      set({ isLoading: false });
    }
  },

  deleteSearchSong: (id) => {
    set((state) => ({
      searchSongs: state.searchSongs.filter((song) => song.id !== id),
    }));
  },

  deleteAlbum: async (id) => {
    set({ isLoading: true, error: null });
    try {
      await axiosInstance.delete(`/api/album/${id}`);
      set((state) => ({
        albums: state.albums.filter((album) => album.id !== id),
        stats: {
          ...state.stats,
          totalAlbums: state.stats.totalAlbums - 1,
        },
      }));
      toast.success("Album deleted successfully");
    } catch (error: any) {
      console.log("Error in deleteAlbum", error);
      toast.error("Error deleting album");
    } finally {
      set({ isLoading: false });
    }
  },

  deleteSearchAlbum: (id) => {
    set((state) => ({
      searchAlbums: state.searchAlbums.filter((album) => album.id !== id),
    }));
  },
}));


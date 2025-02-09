import { create } from "zustand";
import { persist } from "zustand/middleware";
import { Song } from "@/types";

interface PlayerStore {
    currentSong: Song | null;
    isPlaying: boolean;
    isMuted: boolean;
    volume: number;
    queue: Song[];
    currentIndex: number;

    initializeQueue: (songs: Song[]) => void;
    playAlbum: (songs: Song[], startIndex?: number) => void;
    setSongAndPlay: (song: Song) => void;
    setCurrentSong: (song: Song | null) => void;
    togglePlay: () => void;
    toggleMute: () => void;
    setVolume: (volume: number) => void;
    playNext: () => void;
    playPrevious: () => void;
}

export const usePlayerStore = create(persist<PlayerStore>((set, get) => ({
    currentSong: null,
    isPlaying: false,
    isMuted: false,
    volume: 75,
    queue: [],
    currentIndex: -1,

    initializeQueue: (songs: Song[]) => {
        set({
            queue: songs,
            currentSong: get().currentSong || songs[0],
            currentIndex: get().currentIndex === -1 ? 0 : get().currentIndex,
        });
    },

    playAlbum: (songs: Song[], startIndex = 0) => {
        if (songs.length === 0) return;

        const song = songs[startIndex];
        set({
            queue: songs,
            currentSong: song,
            currentIndex: startIndex,
            isPlaying: true,
        });
    },

    setCurrentSong: (song: Song | null) => {
        if (!song) return;

        const songIndex = get().queue.findIndex((s) => s.id === song.id);
        set({
            currentSong: song,
            isPlaying: true,
            currentIndex: songIndex !== -1 ? songIndex : get().currentIndex,
        });
    },

    setSongAndPlay: (song: Song) => {
        set({
            currentSong: song,
            isPlaying: true,
            queue: [song],
            currentIndex: 0,
        });
    },

    togglePlay: () => {
        set({
            isPlaying: !get().isPlaying,
        });
    },

    toggleMute: () => {
        set({
            isMuted: !get().isMuted,
        });
    },

    setVolume: (volume: number) => {
        set({
            volume,
            isMuted: volume === 0,
        });
    },

    playNext: () => {
        const { currentIndex, queue } = get();
        const nextIndex = currentIndex + 1;

        if (nextIndex < queue.length) {
            const nextSong = queue[nextIndex];

            set({
                currentSong: nextSong,
                currentIndex: nextIndex,
                isPlaying: true,
            });
        } else {
            set({ isPlaying: false });
        }
    },

    playPrevious: () => {
        const { currentIndex, queue } = get();
        const prevIndex = currentIndex - 1;

        if (prevIndex >= 0) {
            const prevSong = queue[prevIndex];

            set({
                currentSong: prevSong,
                currentIndex: prevIndex,
                isPlaying: true,
            });
        } else {
            set({ isPlaying: false });
        }
    },
}),
    {
        name: 'player-storage', // name of the item in the storage (must be unique)
    }
));


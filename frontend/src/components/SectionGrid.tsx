import { Song } from "@/types";
import { Button } from "@/components/ui/button";
import SectionGridSkeleton from "./skeletons/SectionGridSkeleton";
import { useNavigate } from "react-router-dom";
import PlayButton from "./PlayButton";
import { useMusicStore } from "@/stores/useMusicStore";

type SectionGridProps = {
    title: string;
    songs: Song[];
    isLoading: boolean;
};

const SectionGrid = ({ songs, title, isLoading }: SectionGridProps) => {
	const { searchSongs } = useMusicStore();
    const navigate = useNavigate();

    if (isLoading) return <SectionGridSkeleton />;

    const handleSongClick = (songId: number) => {
        navigate(`/song/${songId}`);
    };

    const handleArtistClick = (e: React.MouseEvent<HTMLSpanElement>, artistId: number) => {
        e.stopPropagation();
        navigate(`/artist/${artistId}`);
    };

    return (
        <div className='mb-8'>
            <div className='flex items-center justify-between mb-4'>
                <h2 className='text-xl sm:text-2xl font-bold'>{title}</h2>
                {searchSongs.length > 6 && 
					<Button variant='link' className='text-sm text-zinc-400 hover:text-white'>
						Show all
					</Button>
				}
            </div>

            <div className='grid grid-cols-2 sm:grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4'>
                {songs.slice(0, 6).map((song) => (
                    <div
                        key={song.id}
                        className='bg-zinc-800/40 p-4 rounded-md hover:bg-zinc-700/40 transition-all group cursor-pointer'
                        onClick={() => handleSongClick(song.id)}
                    >
                        <div className='relative mb-4'>
                            <div className='aspect-square rounded-md shadow-lg overflow-hidden'>
                                <img
                                    src={song.imageUrl}
                                    alt={song.name}
                                    className='w-full h-full object-cover transition-transform duration-300 
                                    group-hover:scale-105'
                                />
                            </div>
                            <div onClick={(e) => e.stopPropagation()}>
                                <PlayButton song={song} />
                            </div>
                        </div>
                        <h3 className='font-medium mb-2 truncate'>{song.name}</h3>
                        <p className='text-sm text-zinc-400 truncate'>
                            {song.artistIds?.length && song.artistNames?.length ? (
                                <>
                                    {song.artistNames.length === 1 ? (
                                        <span 
                                            className="text-theme-400 hover:underline cursor-pointer"
                                            onClick={(e) => song.artistIds && handleArtistClick(e, song.artistIds[0])}
                                        >
                                            {song.artistNames[0]}
                                        </span>
                                    ) : song.artistNames.length === 2 ? (
                                        <>
                                            {song.artistNames.map((artist, index) => (
                                                <span key={index}>
                                                    {song.artistIds && song.artistIds[index] && (
                                                        <span 
                                                            className="text-theme-400 hover:underline cursor-pointer"
                                                            onClick={(e) => song.artistIds && handleArtistClick(e, song.artistIds[index])}
                                                        >
                                                            {artist}
                                                        </span>
                                                    )}
                                                    {index === 0 && (<span className="text-lg">, </span>)}
                                                </span>
                                            ))}
                                        </>
                                    ) : (
                                        <>
                                            {song.artistNames.slice(0, 3).map((artist, index) => (
                                                <span key={index}>
                                                    {song.artistIds && song.artistIds[index] && (
                                                        <span 
                                                            className="text-theme-400 hover:underline cursor-pointer"
                                                            onClick={(e) => song.artistIds && handleArtistClick(e, song.artistIds[index])}
                                                        >
                                                            {artist}
                                                        </span>
                                                    )}
                                                    {index < 2 && (<span className="text-lg">, </span>)}
                                                    {index === 2 && (<span className="text-lg">,....</span>)}
                                                </span>
                                            ))}
                                        </>
                                    )}
                                </>
                            ) : (
                                <span>No artists available</span>
                            )}
                        </p>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default SectionGrid;


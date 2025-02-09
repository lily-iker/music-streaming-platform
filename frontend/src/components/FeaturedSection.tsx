import { useMusicStore } from "@/stores/useMusicStore";
import FeaturedGridSkeleton from "@/components/skeletons/FeaturedGridSkeleton";
import { useNavigate } from "react-router-dom";
import PlayButton from "./PlayButton";

const FeaturedSection = () => {
    const { isLoading, featuredSongs } = useMusicStore();
    const navigate = useNavigate();

    if (isLoading) return <FeaturedGridSkeleton />;

    const handleSongClick = (songId: number) => {
        navigate(`/song/${songId}`);
    };

    const handleArtistClick = (e: React.MouseEvent<HTMLSpanElement>, artistId: number) => {
        e.stopPropagation();
        navigate(`/artist/${artistId}`);
    };

    return (
        <div className='grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 mb-8'>
            {featuredSongs.slice(0, 6).map((song) => (
                <div
                    key={song.id}
                    className='flex items-center bg-zinc-800/50 rounded-md overflow-hidden 
                    hover:bg-zinc-700/50 transition-colors group cursor-pointer relative'
                    onClick={() => handleSongClick(song.id)}
                >
                    <img
                        src={song.imageUrl}
                        alt={song.name}
                        className='w-16 sm:w-20 h-16 sm:h-20 object-cover flex-shrink-0'
                    />
                    <div className='flex-1 p-4'>
                        <p className='font-medium truncate'>{song.name}</p>
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
                    <div onClick={(e) => e.stopPropagation()}>
                        <PlayButton song={song} />
                    </div>
                </div>
            ))}
        </div>
    );
};

export default FeaturedSection;


import AlbumGrid from "@/components/AlbumGrid";
import { Button } from "@/components/ui/button";
import { ScrollArea } from "@/components/ui/scroll-area";
import { useMusicStore } from "@/stores/useMusicStore";
import { usePlayerStore } from "@/stores/usePlayerStore";
import { Play, Pause } from "lucide-react";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom"
import { gradientClasses } from "@/utils/gradientUtils";
import { formatDuration, formatLikeCount } from "@/utils/formatUtils";
import ArtistGrid from "@/components/ArtistGrid";
import NotFoundPage from "./NotFoundPage";
import Loading from "@/components/Loading";

const SongPage = () => {
    const { songId } = useParams();
    const { fetchSongById, song, isLoading } = useMusicStore();
    const [gradientClass, setGradientClass] = useState(gradientClasses[0]);
    const { currentSong, setSongAndPlay, isPlaying, togglePlay } = usePlayerStore();
    const navigate = useNavigate();

    useEffect(() => {
		if (songId) fetchSongById(Number(songId));
        const randomIndex = Math.floor(Math.random() * gradientClasses.length);
        setGradientClass(gradientClasses[randomIndex]);
	}, [fetchSongById, songId]);

    if (isLoading) return (<Loading/>);

    if (!song) return(<NotFoundPage/>)

    const handlePlaySong = () => {
        if (!song) return;
        if (currentSong?.id !== song.id) {
            setSongAndPlay(song);
        } else {
            togglePlay();
        }
    };

    const handleArtistClick = (e: React.MouseEvent<HTMLSpanElement>, artistId: number) => {
        e.stopPropagation();
        navigate(`/artist/${artistId}`);
    };


    return (
        <div className='h-full'>
			<ScrollArea className='h-[calc(100vh-188px)] rounded-lg'>
				{/* Main Content */}
				<div className='min-h-full'>
					{/* bg gradient */}
					<div
						className={`absolute inset-0 bg-gradient-to-b ${gradientClass} via-zinc-900/80 to-zinc-900 pointer-events-none`}
						aria-hidden='true'
					/>

					{/* Content */}
					<div className='relative z-10'>
						<div className='flex p-6 gap-6 pb-8'>
							<img
								src={song?.imageUrl}
								alt={song?.name}
								className='w-[240px] h-[240px] shadow-xl rounded'
							/>
							<div className='flex flex-col justify-end'>
								<p className='text-sm font-medium'>Song</p>
								<h1 className='text-7xl font-bold my-4'>{song?.name}</h1>
								<div className='flex items-center gap-2 text-sm text-zinc-100'>
                                <p className='text-sm text-zinc-400 truncate'>
                                    {song?.artistIds?.length && song?.artistNames?.length ? (
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
                                                            {song?.artistIds && index < song?.artistIds?.length - 1 && (
                                                                <span className="text-lg">, </span>
                                                            )}
                                                        </span>
                                                    ))}
                                                </>
                                            )}
                                        </>
                                    ) : (
                                        <span>No artists available</span>
                                    )}
                                    </p>
                                    {song && song.likeCount >= 0 &&
                                        <span>• {formatLikeCount(song?.likeCount)} likes</span>
                                    }
                                    {song?.duration &&
                                        <span>• {formatDuration(song?.duration)}</span>
                                    }
                                </div>
                            </div>
                        </div>
                        
                        {/* play button */}
						<div className='px-6 pb-4 flex items-center gap-6'>
							<Button
                                onClick={handlePlaySong}
								size='icon'
								className='w-14 h-14 rounded-full bg-theme-400 hover:bg-theme-500 
                            hover:scale-105 transition-all'
							>
								{isPlaying && song && song.id === currentSong?.id ? (
									<Pause className='h-7 w-7 text-black' />
								) : (
									<Play className='h-7 w-7 text-black' />
								)}
							</Button>
						</div>

                        {/* Table Section */}
						{song?.artistIds 
                        && song.artistNames 
                        && song.artistImageUrls 
                        && (
                            <div className='p-4 sm:p-6'>
                                <ArtistGrid title="Artists" 
                                    isLoading={false} 
                                    artistIds={song.artistIds} 
                                    artistNames={song.artistNames}
                                    artistImageUrls={song.artistImageUrls}
                                />
                            </div>
                        )}

                        {song?.albumId
                        && song.albumName
                        && song.albumImageUrl
                        && (
                            <div className='p-4 sm:p-6'>
                                <AlbumGrid title="Albums" 
                                    isLoading={false} 
                                    albumIds={[song.albumId]} 
                                    albumNames={[song.albumName]}
                                    albumImageUrls={[song.albumImageUrl]}
                                />
                            </div>
                        )}
                    </div>
                </div>
            </ScrollArea>
        </div>
    )
}

export default SongPage
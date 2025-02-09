import AlbumGrid from "@/components/AlbumGrid";
import { Button } from "@/components/ui/button";
import { ScrollArea } from "@/components/ui/scroll-area";
import { useMusicStore } from "@/stores/useMusicStore";
import { usePlayerStore } from "@/stores/usePlayerStore";
import { Clock, Music, Play, Pause } from "lucide-react";
import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom"
import { gradientClasses } from "@/utils/gradientUtils";
import { formatDuration, formatLikeCount } from "@/utils/formatUtils";
import NotFoundPage from "./NotFoundPage";
import Loading from "@/components/Loading";

const ArtistPage = () => {
    const { artistId } = useParams();
    const { fetchArtistById, currentArtist, isLoading } = useMusicStore();
    const [gradientClass, setGradientClass] = useState(gradientClasses[0]);
    const { currentSong, isPlaying, playAlbum, togglePlay } = usePlayerStore();

    useEffect(() => {
		if (artistId) fetchArtistById(Number(artistId));
        const randomIndex = Math.floor(Math.random() * gradientClasses.length);
        setGradientClass(gradientClasses[randomIndex]);
	}, [fetchArtistById, artistId]);

    if (isLoading) return (<Loading/>);

    if (!currentArtist) return(<NotFoundPage/>)

    const handlePlayArtistSongs = () => {
		if (!currentArtist) return;
		const isCurrentArtistSongsPlaying = currentArtist?.songs.some((song) => song.id === currentSong?.id);
		if (isCurrentArtistSongsPlaying) togglePlay();
		else {
			// start playing the album from the beginning
			playAlbum(currentArtist?.songs, 0);
		}
	};

	const handlePlaySong = (index: number) => {
		if (!currentArtist) return;
		playAlbum(currentArtist?.songs, index);
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
								src={currentArtist?.imageUrl}
								alt={currentArtist?.name}
								className='w-[240px] h-[240px] shadow-xl rounded-full'
							/>
							<div className='flex flex-col justify-end'>
								<p className='text-sm font-medium'>Artist</p>
								<h1 className='text-7xl font-bold my-4'>{currentArtist?.name}</h1>
								<div className='flex items-center gap-2 text-sm text-zinc-100'>
									{currentArtist?.followers &&
                                        <span>{formatLikeCount(currentArtist?.followers)} followers</span>
                                    }
                                    <span>• {currentArtist?.songs?.length} songs</span>
                                    <span>• {currentArtist?.albumIds?.length} albums</span>
                                </div>
                            </div>
                        </div>
                        
                        {/* play button */}
						{currentArtist?.songs?.length !== 0 && (
                            <div className='px-6 pb-4 flex items-center gap-6'>
                                <Button
                                    onClick={handlePlayArtistSongs}
                                    size='icon'
                                    className='w-14 h-14 rounded-full bg-theme-400 hover:bg-theme-500 
                                hover:scale-105 transition-all'
                                >
                                    {isPlaying && currentArtist?.songs.some((song) => song.id === currentSong?.id) ? (
                                        <Pause className='h-7 w-7 text-black' />
                                    ) : (
                                        <Play className='h-7 w-7 text-black' />
                                    )}
                                </Button>
                            </div>
                        )}

                        {/* Table Section */}
						<div className='bg-black/20 backdrop-blur-sm'>
							{/* table header */}
							{currentArtist?.songs?.length !== 0 && (
                                <div
								className='grid grid-cols-[16px_4fr_2fr_1fr] gap-4 px-10 py-2 text-sm 
                                text-zinc-400 border-b border-white/5'>

                                    <div>#</div>
                                    <div>Title</div>
                                    <div>Like Count</div>
                                    <div className="flex">
                                        <span>&nbsp;</span>
                                        <Clock className='h-4 w-4' />
                                    </div>
                                </div>
                            )}

                            {/* songs list */}
                            <div className="px-6">
                                
                                {currentArtist?.songs.map((song, index) => {
                                    const isCurrentSong = currentSong?.id === song.id;
                                    return(
                                        <div key={song.id} className="space-y-2 py-2">
                                            <div  
                                                onClick={() => handlePlaySong(index)}
                                                className={`grid grid-cols-[16px_4fr_2fr_1fr] gap-4 px-4 py-2 text-sm 
                                                text-zinc-400 hover:bg-white/5 rounded-md group cursor-pointer`}>
                                                <div className="flex items-center justify-center">
                                                    {isCurrentSong && isPlaying ? (
                                                        <Music className="text-theme-400"/>
                                                    ) : (
                                                        <span className='group-hover:hidden'>{index + 1}</span>
                                                    )}
                                                    {!isCurrentSong && (
                                                        <Play className='h-4 w-4 hidden group-hover:block' />
                                                    )}
                                                </div>
                                                <div className='flex items-center gap-3'>
                                                    <img src={song.imageUrl} alt={song.name} className='size-10' />

                                                    <div onClick={(e) => e.stopPropagation()}>
                                                        <div className={`font-medium text-white hover:underline`}>
                                                            <Link to={`/song/${song.id}`}>
                                                                {song.name}
                                                            </Link>
                                                        </div>
                                                        <div>
                                                        {song.artistIds && song.artistNames && song.artistNames.length > 0 ? (
                                                            <>
                                                                {song.artistNames.length === 1 ? (
                                                                    <Link 
                                                                        to={`/artist/${song.artistIds[0]}`}
                                                                        className="text-theme-400 hover:underline">
                                                                        {song.artistNames[0]}
                                                                    </Link>
                                                                ) : song.artistNames.length === 2 ? (
                                                                    <>
                                                                        {song.artistNames.map((artist, index) => (
                                                                            <span key={index}>
                                                                                {song.artistIds && (
                                                                                    <Link 
                                                                                        to={`/artist/${song.artistIds[index]}`}
                                                                                        className="text-theme-400 hover:underline">
                                                                                        {artist}
                                                                                    </Link>
                                                                                )}
                                                                                {index === 0 && (<span className="text-lg">, </span>)}
                                                                            </span>
                                                                        ))}
                                                                    </>
                                                                ) : (
                                                                    <>
                                                                        {song.artistNames.slice(0, 3).map((artist, index) => (
                                                                            <span key={index}>
                                                                                {song.artistIds && (
                                                                                    <Link 
                                                                                        to={`/artist/${song.artistIds[index]}`}
                                                                                        className="text-theme-400 hover:underline">
                                                                                        {artist}
                                                                                    </Link>
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
                                                        </div>
                                                    </div>
                                                </div>
                                                <div className='flex items-center'>{formatLikeCount(song?.likeCount)}</div>
                                                <div className='flex items-center'>{formatDuration(song.duration)}</div>
                                            </div>
                                        </div>
                                    )})}
                             
                            </div>
                            {currentArtist?.albumIds 
                            && currentArtist.albumIds.length > 0 
                            && currentArtist.albumNames.length > 0 
                            && currentArtist.albumImageUrls.length > 0 
                            && (
                                <div className='p-4 sm:p-6'>
                                    <AlbumGrid title="Albums" 
                                        isLoading={false} 
                                        albumIds={currentArtist?.albumIds} 
                                        albumNames={currentArtist?.albumNames}
                                        albumImageUrls={currentArtist?.albumImageUrls}
                                    />
                                </div>
                            )}     
                        </div>
                    </div>
                </div>
            </ScrollArea>
        </div>
    )
}

export default ArtistPage
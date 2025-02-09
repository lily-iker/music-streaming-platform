import { useMusicStore } from '@/stores/useMusicStore';
import { Song } from '@/types'
import { useNavigate } from 'react-router-dom';

type DisplayArtistsProps = {
	song: Song,
};

const DisplayArtists = ({song}: DisplayArtistsProps) => {
    const navigate = useNavigate();
    const { clearSearch } = useMusicStore();

    const handleArtistClick = (artistId: number) => {
        clearSearch();
        navigate(`/artist/${artistId}`);
    };

    return (
        <p className='text-sm text-zinc-400 truncate'>
            {song?.artistIds?.length && song?.artistNames?.length ? (
                <>
                    {song.artistNames.map((artist, index) => (
                        <span key={index}>
                            {song.artistIds && song.artistIds[index] && (
                                <span 
                                    className="text-white font-medium hover:underline cursor-pointer"
                                    onClick={() => song.artistIds && handleArtistClick(song.artistIds[index])}
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
            ) : (
                <span>No artists available</span>
            )}
        </p>
    )
}

export default DisplayArtists
import { Artist } from "@/types";
import SectionGridSkeleton from "./skeletons/SectionGridSkeleton";
import { Link } from "react-router-dom";
import { Button } from "./ui/button";
import { useMusicStore } from "@/stores/useMusicStore";

type ArtistSectionGridProps = {
    title: string;
    artists: Artist[];
    isLoading: boolean;
};

const ArtistSectionGrid = ({ title, artists, isLoading }: ArtistSectionGridProps) => {
	const { searchArtists } = useMusicStore();
    
    if (isLoading) return <SectionGridSkeleton />;
    
    return (
		<div className='mb-8'>
			<div className='flex items-center justify-between mb-4'>
				<h2 className='text-xl sm:text-2xl font-bold'>{title}</h2>
				{searchArtists.length > 6 && 
					<Button variant='link' className='text-sm text-zinc-400 hover:text-white'>
						Show all
					</Button>
				}
			</div>

			<div className='grid grid-cols-2 sm:grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4'>
				{artists ? (
					artists.slice(0, 6).map((artist) => (
						<div
							key={artist.id}
							className='p-4 rounded-md bg-inherit hover:bg-zinc-700/40 transition-all group cursor-pointer'
						>
                            <Link to={`/artist/${artist.id}`}>
                                <div className='relative mb-4'>
                                    <div className='aspect-square rounded-full shadow-lg overflow-hidden'>
                                        <img
                                            src={artist.imageUrl}
                                            alt={artist.name}
                                            className='w-full h-full object-cover transition-transform duration-300 
                                            group-hover:scale-105'
                                        />
                                    </div>
                                </div>
                                <h3 className='font-medium mb-2 truncate'>{artist.name}</h3>
                            </Link>

						</div>
					))
				) : (
					<span>No artists available</span>
				)}
			</div>
		</div>
	);
}

export default ArtistSectionGrid
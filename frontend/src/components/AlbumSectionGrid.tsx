import { Album } from "@/types";
import SectionGridSkeleton from "./skeletons/SectionGridSkeleton";
import { Link } from "react-router-dom";
import { Button } from "./ui/button";
import { useMusicStore } from "@/stores/useMusicStore";

type AlbumSectionGridProps = {
    title: string;
    albums: Album[];
    isLoading: boolean;
};

const AlbumSectionGrid = ({ title, albums, isLoading }: AlbumSectionGridProps) => {
	const { searchAlbums } = useMusicStore();
    
    if (isLoading) return <SectionGridSkeleton />;
    
    return (
		<div className='mb-8'>
			<div className='flex items-center justify-between mb-4'>
				<h2 className='text-xl sm:text-2xl font-bold'>{title}</h2>
				{searchAlbums.length > 6 && 
					<Button variant='link' className='text-sm text-zinc-400 hover:text-white'>
						Show all
					</Button>
				}
			</div>

			<div className='grid grid-cols-2 sm:grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4'>
				{albums ? (
					albums.slice(0, 6).map((album) => (
						<div
							key={album.id}
							className='bg-zinc-800/40 p-4 rounded-md hover:bg-zinc-700/40 transition-all group cursor-pointer'
						>
                            <Link to={`/album/${album.id}`}>
                                <div className='relative mb-4'>
                                    <div className='aspect-square rounded-md shadow-lg overflow-hidden'>
                                        <img
                                            src={album.imageUrl}
                                            alt={album.name}
                                            className='w-full h-full object-cover transition-transform duration-300 
                                            group-hover:scale-105'
                                        />
                                    </div>
                                </div>
                                <h3 className='font-medium mb-2 truncate'>{album.name}</h3>
                            </Link>
							<Link to={`/artist/${album.artistId}`}>
								<p className='text-sm text-zinc-400 truncate'>
									<span className="text-theme-400 hover:underline cursor-pointer">
										{album.artistName}
									</span>
								</p>
							</Link>

						</div>
					))
				) : (
					<span>No albums available</span>
				)}
			</div>
		</div>
	);
}

export default AlbumSectionGrid
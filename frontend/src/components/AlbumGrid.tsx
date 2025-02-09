import SectionGridSkeleton from "./skeletons/SectionGridSkeleton";
import { Link } from "react-router-dom";

type AlbumGridProps = {
	title: string;
	albumIds: number[] | null;
	albumNames: string[] | null;
	albumImageUrls: string[] | null;
	isLoading: boolean;
};

const AlbumGrid = ({ title, albumIds, albumNames, albumImageUrls, isLoading }: AlbumGridProps) => {
	if (isLoading) return <SectionGridSkeleton />;

	return (
		<div className='mb-8'>
			<div className='flex items-center justify-between mb-4'>
				<h2 className='text-xl sm:text-2xl font-bold'>{title}</h2>
			</div>

			<div className='grid grid-cols-2 sm:grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4'>
				{albumIds && albumNames && albumImageUrls && albumIds.length > 0 && albumNames.length > 0 && albumImageUrls.length > 0 ? (
					albumIds.map((albumId, index) => (
						<div
							key={albumId}
							className='bg-zinc-800/40 p-4 rounded-md hover:bg-zinc-700/40 transition-all group cursor-pointer'
						>
                            <Link to={`/album/${albumId}`}>
                                <div className='relative mb-4'>
                                    <div className='aspect-square rounded-md shadow-lg overflow-hidden'>
                                        <img
                                            src={albumImageUrls[index]}
                                            alt={albumNames[index]}
                                            className='w-full h-full object-cover transition-transform duration-300 
                                            group-hover:scale-105'
                                        />
                                    </div>
                                </div>
                                <h3 className='font-medium mb-2 truncate'>{albumNames[index]}</h3>
                            </Link>

						</div>
					))
				) : (
					<span>No albums available</span>
				)}
			</div>
		</div>
	);
};

export default AlbumGrid;
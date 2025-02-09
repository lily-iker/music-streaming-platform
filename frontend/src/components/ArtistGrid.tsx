import SectionGridSkeleton from "./skeletons/SectionGridSkeleton";
import { Link } from "react-router-dom";

type ArtistGridProps = {
	title: string;
	artistIds: number[] | null;
	artistNames: string[] | null;
	artistImageUrls: string[] | null;
	isLoading: boolean;
};

const ArtistGrid = ({ title, artistIds, artistNames, artistImageUrls, isLoading }: ArtistGridProps) => {
	if (isLoading) return <SectionGridSkeleton />;

	return (
		<div className='mb-8'>
			<div className='flex items-center justify-between mb-4'>
				<h2 className='text-xl sm:text-2xl font-bold'>{title}</h2>
			</div>

			<div className='grid grid-cols-2 sm:grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4'>
				{artistIds && artistNames && artistImageUrls && artistIds.length > 0 
				&& artistNames.length > 0 && artistImageUrls.length > 0 ? (
					artistIds.map((artistId, index) => (
						<div
							key={artistId}
							className='p-4 rounded-md bg-inherit hover:bg-zinc-700/40 transition-all group cursor-pointer'
						>
                            <Link to={`/artist/${artistId}`}>
                                <div className='relative mb-4'>
                                    <div className='aspect-square rounded-full shadow-lg overflow-hidden'>
                                        <img
                                            src={artistImageUrls[index]}
                                            alt={artistNames[index]}
                                            className='w-full h-full object-cover transition-transform duration-300 
                                            group-hover:scale-105'
                                        />
                                    </div>
                                </div>
                                <h3 className='font-medium mb-2 truncate'>{artistNames[index]}</h3>
                            </Link>

						</div>
					))
				) : (
					<span>No artists available</span>
				)}
			</div>
		</div>
	);
};

export default ArtistGrid;
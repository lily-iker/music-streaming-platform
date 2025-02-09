import { Button } from "@/components/ui/button";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { useMusicStore } from "@/stores/useMusicStore";
import { Song } from "@/types";
import { Edit, Trash2 } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import ReactPaginate from "react-paginate";
import UpdateSongDialog from "./UpdateSongDialog";


const SongsTable = () => {
	const { 
		songs, 
		error, 
		fetchSongs, 
		songPagination,
		setSongPageNo,
		deleteSong, 
		searchQuery, 
		searchSongs, 
		clearSearch, 
		deleteSearchSong,
	} = useMusicStore();
	const navigate = useNavigate();
	
	useEffect(() => {
		if (!searchQuery) fetchSongs(songPagination.pageNo, songPagination.pageSize)
	  }, [songPagination.pageNo, searchQuery])

	const handleSongClick = (songId: number) => {
        clearSearch();
        navigate(`/song/${songId}`);
    };

	const handleArtistClick = (artistId: number) => {
        clearSearch();
        navigate(`/artist/${artistId}`);
    };

	const handlePageClick = (data: { selected: number }) => {
		setSongPageNo(data.selected);
	};

	const [selectedSongId, setSelectedSongId] = useState<number|null>(null);

	if (error) {
		return (
			<div className='flex items-center justify-center py-8'>
				<div className='text-red-400'>{error}</div>
			</div>
		);
	}

	const displaySongs: Song[] = searchQuery ? searchSongs : songs;

	return (
		<div>
			<Table>
				<TableHeader>
					<TableRow className='hover:bg-zinc-800/50'>
						<TableHead className='w-[60px]'></TableHead>
						<TableHead>Songs</TableHead>
						<TableHead className="text-center">Artists</TableHead>
						<TableHead className='text-right'>Update</TableHead>
						<TableHead className='text-right'>Delete</TableHead>
					</TableRow>
				</TableHeader>

				<TableBody>
					{displaySongs.map((song) => (
						<TableRow key={song.id} className='hover:bg-zinc-800/50'>
							<TableCell>
								<img src={song.imageUrl} alt={song.name} className='size-10 rounded object-cover' />
							</TableCell>
							<TableCell>
								<p onClick={() => handleSongClick(song.id)} 
								className='font-medium hover:underline hover:cursor-pointer'>
									{song.name}
								</p>
							</TableCell>
							<TableCell className="text-center">
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
							</TableCell>

							<TableCell className='text-right'>
								<div className='flex gap-2 justify-end'>
									<Button
										variant={"ghost"}
										size={"sm"}
										className='text-blue-400 hover:text-blue-300 hover:bg-blue-400/10'
										onClick={async() => setSelectedSongId(song.id)}
									>
										<Edit className='size-4' />
									</Button>
								</div>
							</TableCell>

							<TableCell className='text-right'>
								<div className='flex gap-2 justify-end'>
									<Button
										variant={"ghost"}
										size={"sm"}
										className='text-red-400 hover:text-red-300 hover:bg-red-400/10'
										onClick={() => {
											deleteSong(song.id)
											if (searchQuery) deleteSearchSong(song.id)
										}}
									>
										<Trash2 className='size-4' />
									</Button>
								</div>
							</TableCell>
						</TableRow>
					))}
				</TableBody>
			</Table>

			{selectedSongId && (
				<div className="hidden">
					<UpdateSongDialog
					songId={selectedSongId}
					isOpen={!!selectedSongId}
					onClose={() => setSelectedSongId(null)}
				/>
				</div>
			)}

			<div className="mb-8"/>

			<ReactPaginate
				previousLabel={"<"}
				nextLabel={">"}
				breakLabel={"..."}
				pageCount={songPagination.totalPages}
				marginPagesDisplayed={2}
				pageRangeDisplayed={5}
				onPageChange={handlePageClick}
				containerClassName="flex justify-center space-x-2"
				activeClassName="bg-zinc-700 text-white"
				pageClassName="inline-flex items-center justify-center w-10 h-10 border rounded-full"
				pageLinkClassName="hover:text-theme-500 focus:outline-none"
				previousClassName="inline-flex items-center justify-center w-10 h-10 border rounded-full"
				previousLinkClassName="hover:text-theme-500 focus:outline-none"
				nextClassName="inline-flex items-center justify-center w-10 h-10 border rounded-full"
				nextLinkClassName="hover:text-theme-500 focus:outline-none"
				breakClassName="inline-flex items-center justify-center w-10 h-10 border rounded-full"
				breakLinkClassName="hover:text-theme-500 focus:outline-none"
				forcePage={Math.min(songPagination.pageNo, songPagination.totalPages - 1)}
			/>
		</div>
	);
};
export default SongsTable;
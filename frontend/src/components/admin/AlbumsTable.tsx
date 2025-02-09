import { Button } from "@/components/ui/button";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { useMusicStore } from "@/stores/useMusicStore";
import { Album } from "@/types";
import { Edit, Trash2 } from "lucide-react";
import { useEffect, useState } from "react";
import ReactPaginate from "react-paginate";
import { useNavigate } from "react-router-dom";
import UpdateAlbumDialog from "./UpdateAlbumDialog";

const AlbumsTable = () => {
	const { 
        albums, 
        fetchAlbums, 
        clearSearch, 
        searchQuery, 
        albumPagination, 
        setAlbumPageNo, 
        searchAlbums, 
        deleteAlbum, 
        deleteSearchAlbum 
    } = useMusicStore();
    const navigate = useNavigate();
    const [selectedAlbumId, setSelectedAlbumId] = useState<number | null>(null)

    useEffect(() => {
        if (!searchQuery) fetchAlbums(albumPagination.pageNo, albumPagination.pageSize)
    }, [albumPagination.pageNo, searchQuery])

    const handleAlbumClick = (albumId: number) => {
        clearSearch();
        navigate(`/album/${albumId}`);
    };

    const handleArtistClick = (artistId: number) => {
        clearSearch();
        navigate(`/artist/${artistId}`);
    };

    const handlePageClick = (data: { selected: number }) => {
        setAlbumPageNo(data.selected)
    }

    const displayAlbums: Album[] = searchQuery ? searchAlbums : albums;

	return (
        <div>
            <Table>
                <TableHeader>
                    <TableRow className='hover:bg-zinc-800/50'>
                        <TableHead className='w-[50px]'></TableHead>
                        <TableHead>Albums</TableHead>
                        <TableHead className="text-center">Artists</TableHead>
                        <TableHead className='text-right'>Update</TableHead>
                        <TableHead className='text-right'>Delete</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    {displayAlbums.map((album) => (
                        <TableRow key={album.id} className='hover:bg-zinc-800/50'>
                            <TableCell>
                                <img src={album.imageUrl} alt={album.name} className='w-10 h-10 rounded object-cover' />
                            </TableCell>
                            <TableCell>
                                <p onClick={() => handleAlbumClick(album.id)} 
                                className='font-medium hover:underline hover:cursor-pointer'>
                                    {album.name}
                                </p>
                            </TableCell>
                            <TableCell className="text-center">
                                <p className='text-sm text-zinc-400 truncate'>
                                    {album?.artistId && album?.artistName ? (
                                        <>
                                            <span 
                                                className="text-white font-medium hover:underline cursor-pointer"
                                                onClick={() => handleArtistClick(album.artistId)}
                                            >
                                                {album.artistName}
                                            </span>
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
                                        onClick={() => setSelectedAlbumId(album.id)}
                                    >
                                        <Edit className='size-4' />
                                    </Button>
                                </div>
                            </TableCell>
                            <TableCell className='text-right'>
                                <div className='flex gap-2 justify-end'>
                                    <Button
                                        variant='ghost'
                                        size='sm'
                                        onClick={() => {
                                            deleteAlbum(album.id)
                                            if (searchQuery) deleteSearchAlbum(album.id)
                                        }}
                                        className='text-red-400 hover:text-red-300 hover:bg-red-400/10'
                                    >
                                        <Trash2 className='h-4 w-4' />
                                    </Button>
                                </div>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>

            <div className="mb-8"/>

            <ReactPaginate
                previousLabel={"<"}
                nextLabel={">"}
                breakLabel={"..."}
                pageCount={albumPagination.totalPages}
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
                forcePage={Math.min(albumPagination.pageNo, albumPagination.totalPages - 1)}
            />
            {selectedAlbumId && (
                <UpdateAlbumDialog
                albumId={selectedAlbumId}
                isOpen={!!selectedAlbumId}
                onClose={() => setSelectedAlbumId(null)}
                />
            )}    
        </div>
	);
};
export default AlbumsTable;
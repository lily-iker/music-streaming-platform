import { cn } from "@/lib/utils"
import { HomeIcon, Library } from "lucide-react"
import { Link } from "react-router-dom"
import { buttonVariants } from "./ui/button"
import { ScrollArea } from "./ui/scroll-area"
import PlaylistSkeleton from "./skeletons/PlaylistSkeleton"
import { useMusicStore } from "@/stores/useMusicStore"
import { useEffect } from "react"

const LeftSidebar = () => {
    const { albums, fetchAlbums, isLoading, clearSearch } = useMusicStore();

    useEffect(() => {
		fetchAlbums(0, 20);
	}, [fetchAlbums]);

    const handleHomeClick = () => {
        clearSearch();
    };
    
    return (
        <div className='h-full flex flex-col gap-2'>
            {/* Navigation menu */}
            <div className="rounded-lg bg-zinc-900 p-4">
                <div className="space-y-2">
                    <Link to={"/"} className={cn(buttonVariants({
                        variant: "ghost",
                        className: "w-full justify-start text-white hover:bg-zinc-800"
                    }))}
                    onClick={handleHomeClick}
                    >
                        <HomeIcon className="mr-2 sie-5"/>
                        <span className="hidden md:inline">Home</span>
                    </Link>
                </div>
            </div>

            {/* Library section */}
            <div className='flex-1 rounded-lg bg-zinc-900 p-4'>
				<div className='flex items-center justify-between mb-4'>
					<div className='flex items-center text-white px-2'>
						<Library className='size-5 mr-2' />
						<span className='hidden md:inline'>Playlists</span>
					</div>
				</div>
                
                <ScrollArea className="h-[calc(100vh-300px)]">
                    <div className="space-y-2">
                        {isLoading
                         
                        ? (
                            <PlaylistSkeleton/>
                        )

                        : (
                            albums.map((album) => (
								<Link
									to={`/album/${album.id}`}
									key={album.id}
									className='p-2 hover:bg-zinc-800 rounded-md flex items-center gap-3 group cursor-pointer'
								>
									<img
										src={album.imageUrl}
										alt='Playlist img'
										className='size-12 rounded-md flex-shrink-0 object-cover'
									/>

									<div className='flex-1 min-w-0 hidden md:block'>
										<p className='font-medium truncate'>{album.name}</p>
										<p className='text-sm text-zinc-400 truncate'>Album • {album.artistName}</p>
									</div>
								</Link>
							))
                        )}
                    </div>

                </ScrollArea>
            </div>
        </div>
    )
}

export default LeftSidebar
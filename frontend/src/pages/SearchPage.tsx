import AlbumSectionGrid from "@/components/AlbumSectionGrid";
import ArtistSectionGrid from "@/components/ArtistSectionGrid";
import SectionGrid from "@/components/SectionGrid";
import { ScrollArea } from "@/components/ui/scroll-area"
import { useMusicStore } from "@/stores/useMusicStore";

const SearchPage = () => {
    const { searchSongs, searchArtists, searchAlbums } = useMusicStore();

    return (
        <div>
          <main className='rounded-md overflow-hidden h-full bg-gradient-to-b from-zinc-800 to-zinc-900'>
            <ScrollArea className='h-[calc(100vh-188px)] rounded-lg'>
              <div className='p-4 sm:p-6'>  

                {searchSongs.length > 0 &&
                  <SectionGrid title='Matched Songs' songs={searchSongs} isLoading={false} />
                }

                {searchArtists.length > 0 &&
                  <ArtistSectionGrid title="Matched Artists" artists={searchArtists} isLoading={false} />
                }

                {searchAlbums.length > 0 &&
                  <AlbumSectionGrid title="Matched Albums" albums={searchAlbums} isLoading={false} />
                }

                {searchSongs.length === 0 && 
                searchArtists.length === 0 && 
                searchAlbums.length === 0 && (
                  <div className="text-center mt-12 text-white text-2xl italic">
                    <p>Nothing found</p>
                  </div>
                  )
                }

              </div>
            </ScrollArea>
          </main>
        </div>
      )
}

export default SearchPage
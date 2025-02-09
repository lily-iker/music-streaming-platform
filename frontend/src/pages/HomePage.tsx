import ArtistSectionGrid from "@/components/ArtistSectionGrid";
import FeaturedSection from "@/components/FeaturedSection";
import SectionGrid from "@/components/SectionGrid";
import { ScrollArea } from "@/components/ui/scroll-area";
import { useMusicStore } from "@/stores/useMusicStore";
import { usePlayerStore } from "@/stores/usePlayerStore";
import { useEffect } from "react";

const HomePage = () => {
  const {
    fetchFeaturedSongs,
    fetchMadeForYouSongs,
    fetchTrendingSongs,
    isLoading,
    madeForYouSongs,
    featuredSongs,
    trendingSongs,
    topArtists,
    fetchTopArtists
  } = useMusicStore();

  const { initializeQueue } = usePlayerStore();

  useEffect(() => {
    fetchFeaturedSongs()
    fetchMadeForYouSongs()
    fetchTrendingSongs()
    fetchTopArtists()
  }, [fetchFeaturedSongs, fetchMadeForYouSongs, fetchTrendingSongs, fetchTopArtists])

  useEffect(() => {
    if (madeForYouSongs.length > 0 && featuredSongs.length > 0 && trendingSongs.length > 0) {
      const allSongs = [...featuredSongs, ...madeForYouSongs, ...trendingSongs];
      initializeQueue(allSongs);
    }
  }, [initializeQueue, madeForYouSongs, trendingSongs, featuredSongs]);

  const currentHour = new Date().getHours();
  let greeting;

  if (currentHour < 12) {
      greeting = 'Good morning';
  } else if (currentHour < 18) {
      greeting = 'Good afternoon';
  } else {
      greeting = 'Good evening';
  }

  return (
    <div>
      <main className='rounded-lg h-full bg-gradient-to-b from-zinc-800 to-zinc-900'>
        <ScrollArea className='h-[calc(100vh-188px)] rounded-lg'>
          <div className='p-4 sm:p-6'>
            <h1 className='text-2xl sm:text-3xl font-bold mb-6'>{greeting}</h1>
            <FeaturedSection />
            <div className='space-y-8'>
              <SectionGrid title='Made For You' songs={madeForYouSongs} isLoading={isLoading} />
              <SectionGrid title='Trending' songs={trendingSongs} isLoading={isLoading} />
              <ArtistSectionGrid title="Top Artist" artists={topArtists} isLoading={isLoading} />
              {/* <SectionGrid title='New Release' songs={newReleaseSongs} isLoading={isLoading} /> */}
            </div>
          </div>
        </ScrollArea>
      </main>
    </div>
  )
}

export default HomePage


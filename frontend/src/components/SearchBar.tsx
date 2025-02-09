import { Search } from 'lucide-react';
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from 'react-router-dom';
import { Input } from './ui/input';
import { useMusicStore } from '@/stores/useMusicStore';

const SearchTest = () => {
    const {
        searchQuery,
        setSearchQuery,
        searchSongByName,
        searchArtistByName,
        searchAlbumByName,
        clearSearch,
        songPagination,
        albumPagination,
        artistPagination,
        setSongPageNo,
        setAlbumPageNo,
        setArtistPageNo,
    } = useMusicStore()
    const [debouncedQuery, setDebouncedQuery] = useState(searchQuery)
    const navigate = useNavigate()
    const location = useLocation()

    useEffect(() => {
        const timer = setTimeout(() => setDebouncedQuery(searchQuery), 300)
        return () => clearTimeout(timer)
    }, [searchQuery])

    useEffect(() => {
    if (debouncedQuery.trim() !== "") {
        searchSongByName(debouncedQuery, songPagination.pageNo, songPagination.pageSize)
        searchArtistByName(debouncedQuery, artistPagination.pageNo, artistPagination.pageSize)
        searchAlbumByName(debouncedQuery, albumPagination.pageNo, albumPagination.pageSize)

        if (location.pathname !== "/admin" && location.pathname !== "/search") {
            navigate(`/search`)
        }
    } else if (location.pathname === "/search") {
        clearSearch()
        navigate(`/`)
    }
    }, [debouncedQuery, clearSearch])

    useEffect(() => {
        if (debouncedQuery.trim() !== "") {
            searchSongByName(debouncedQuery, songPagination.pageNo, songPagination.pageSize)
        }
    }, [songPagination.pageNo])

    useEffect(() => {
        if (debouncedQuery.trim() !== "") {
            searchAlbumByName(debouncedQuery, albumPagination.pageNo, albumPagination.pageSize)
        }
    }, [albumPagination.pageNo])

    useEffect(() => {
        if (debouncedQuery.trim() !== "") {
            searchArtistByName(debouncedQuery, artistPagination.pageNo, artistPagination.pageSize)
        }
    }, [artistPagination.pageNo])

	return (	
        <div className='relative w-full max-w-sm'>
            <Search className='absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4' />
            <Input
                type='text'
                placeholder='Search...'
                className='pl-10 pr-16 py-2 w-full bg-zinc-700 placeholder-muted-foreground rounded-full'
                value={searchQuery}
                onChange={(e) => {
                    setSearchQuery(e.target.value)
                    setSongPageNo(0)
                    setAlbumPageNo(0)
                    setArtistPageNo(0)
                }}
            />
        </div>
	);
};

export default SearchTest;
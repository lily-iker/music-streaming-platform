import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogFooter,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Checkbox } from "@/components/ui/checkbox";
import axiosInstance from '../../lib/axios-custom'
import { useMusicStore } from "@/stores/useMusicStore";
import { Plus, Upload } from 'lucide-react';
import { useEffect, useRef, useState } from "react";
import toast from "react-hot-toast";
import { Album } from "@/types";

interface NewSong {
  name: string;
  duration: number;
  genreNames: string[];
  artistNames: string[];
  albumName: string;
}

const AddSongDialog = () => {
  const { genres, artists, fetchArtists, fetchGenres, addSong } = useMusicStore();
  const [songDialogOpen, setSongDialogOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [allAlbums, setAllAlbums] = useState<Album[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      if (songDialogOpen) {
        try {
          const albumsRes = await axiosInstance.get("/api/album/all", {
            params: {
              pageNo: 0,
              pageSize: 10000,
              sortBy: 'id:asc',
            },
          });
          setAllAlbums(albumsRes.data.result.items);

          await fetchArtists(0, 1000000);

          await fetchGenres();

        } catch (error: any) {
          console.error('Error fetching data:', error);
        }
      }
    };
    fetchData();
  }, [songDialogOpen]);

  const [newSong, setNewSong] = useState<NewSong>({
    name: "",
    duration: 60,
    genreNames: [],
    artistNames: [],
    albumName: "",
  });

  const [files, setFiles] = useState<{ audio: File | null; image: File | null }>({
    audio: null,
    image: null,
  });

  const audioInputRef = useRef<HTMLInputElement>(null);
  const imageInputRef = useRef<HTMLInputElement>(null);

  const handleSubmit = async () => {
    setIsLoading(true);

    try {
      if (!files.audio || !files.image) {
        return toast.error("Please upload both audio and image files");
      }

      const formData = new FormData();

      // Append SongRequest as JSON
      const songRequest = JSON.stringify({
        name: newSong.name,
        duration: newSong.duration,
        genreNames: newSong.genreNames,
        artistNames: newSong.artistNames,
        albumName: newSong.albumName,
      });

      formData.append("request", new Blob([songRequest], { type: "application/json" }));
      formData.append("imageFile", files.image);
      formData.append("songFile", files.audio);
      
      await addSong(formData)

      setNewSong({
        name: "",
        duration: 60,
        genreNames: [],
        artistNames: [],
        albumName: "" ,
      });

      setFiles({
        audio: null,
        image: null,
      });
      
      setSongDialogOpen(false);
    } catch (error: any) {
      console.log(error)
      toast.error("Failed to add song: " + error.response.result.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleGenreChange = (genreName: string, checked: boolean) => {
    setNewSong(prev => ({
      ...prev,
      genreNames: checked
        ? [...prev.genreNames, genreName]
        : prev.genreNames.filter(g => g !== genreName)
    }));
  };

  const handleArtistChange = (artistName: string, checked: boolean) => {
    setNewSong(prev => ({
      ...prev,
      artistNames: checked
        ? [...prev.artistNames, artistName]
        : prev.artistNames.filter(a => a !== artistName)
    }));
  };

  return (
    <Dialog open={songDialogOpen} onOpenChange={setSongDialogOpen}>
      <DialogTrigger asChild>
        <Button className='bg-emerald-400 hover:bg-emerald-500 text-black'>
          <Plus className='mr-2 h-4 w-4' />
          Add Song
        </Button>
      </DialogTrigger>

      <DialogContent className='bg-zinc-900 border-zinc-700 max-h-[80vh] overflow-auto'>
        <DialogHeader>
          <DialogTitle>Add New Song</DialogTitle>
          <DialogDescription>Add a new song to your music library</DialogDescription>
        </DialogHeader>

        <div className='space-y-4 py-4'>
          <input
            type='file'
            accept='mp4'
            ref={audioInputRef}
            hidden
            onChange={(e) => setFiles((prev) => ({ ...prev, audio: e.target.files![0] }))}
          />

          <input
            type='file'
            ref={imageInputRef}
            className='hidden'
            accept='image/*'
            onChange={(e) => setFiles((prev) => ({ ...prev, image: e.target.files![0] }))}
          />

          {/* image upload area */}
          <div
            className='flex items-center justify-center p-6 border-2 border-dashed border-zinc-700 rounded-lg cursor-pointer'
            onClick={() => imageInputRef.current?.click()}
          >
            <div className='text-center'>
              {files.image ? (
                <div className='space-y-2'>
                  <div className='text-sm text-emerald-500'>Image selected:</div>
                  <div className='text-xs text-zinc-400'>{files.image.name.slice(0, 20)}</div>
                </div>
              ) : (
                <>
                  <div className='p-3 bg-zinc-800 rounded-full inline-block mb-2'>
                    <Upload className='h-6 w-6 text-zinc-400' />
                  </div>
                  <div className='text-sm text-zinc-400 mb-2'>Upload artwork</div>
                  <Button variant='outline' size='sm' className='text-xs'>
                    Choose File
                  </Button>
                </>
              )}
            </div>
          </div>

          {/* Audio upload */}
          <div className='space-y-2'>
            <label className='text-sm font-medium'>Audio File</label>
            <div className='flex items-center gap-2'>
              <Button variant='outline' onClick={() => audioInputRef.current?.click()} className='w-full'>
                {files.audio ? files.audio.name.slice(0, 20) : "Choose Audio File"}
              </Button>
            </div>
          </div>

          {/* other fields */}
          <div className='space-y-2'>
            <label className='text-sm font-medium'>Name</label>
            <Input
              value={newSong.name}
              onChange={(e) => setNewSong({ ...newSong, name: e.target.value })}
              className='bg-zinc-800 border-zinc-700'
            />
          </div>

          <div className='space-y-2'>
            <label className='text-sm font-medium'>Duration (seconds)</label>
            <Input
              type='number'
              min='60'
              value={newSong.duration}
              onChange={(e) => setNewSong({ ...newSong, duration: parseInt(e.target.value) || 60 })}
              className='bg-zinc-800 border-zinc-700'
            />
          </div>

          <div className='space-y-2'>
            <label className='text-sm font-medium'>Genres (at least one)</label>
            <div className='space-y-2 max-h-40 overflow-y-auto'>
              {genres.map((genre) => (
                <div key={genre.id} className='flex items-center space-x-2'>
                  <Checkbox
                    id={`genre-${genre.id}`}
                    checked={newSong.genreNames.some(g => g === genre.name)}
                    onCheckedChange={(checked: boolean) => handleGenreChange(genre.name, checked as boolean)}
                  />
                  <label htmlFor={`genre-${genre.id}`} className='text-md text-zinc-300'>
                    {genre.name}
                  </label>
                </div>
              ))}
            </div>
          </div>

          <div className='space-y-2'>
            <label className='text-sm font-medium'>Artists (at least one)</label>
            <div className='space-y-2 max-h-80 overflow-y-auto'>
              {artists.map((artist) => (
                <div key={artist.id} className='flex items-center space-x-2'>
                  <Checkbox
                    id={`artist-${artist.id}`}
                    checked={newSong.artistNames.some(a => a === artist.name)}
                    onCheckedChange={(checked: boolean) => handleArtistChange(artist.name, checked as boolean)}
                  />
                  <label htmlFor={`artist-${artist.id}`} className='text-md text-zinc-300'>
                    {artist.name}
                  </label>
                </div>
              ))}
            </div>
          </div>

          <div className='space-y-2'>
            <label className='text-sm font-medium'>Album (Optional)</label>
            <Select
              value={newSong.albumName}
              onValueChange={(value: string) => setNewSong({ ...newSong, albumName: value })}
            >
              <SelectTrigger className='bg-zinc-800 border-zinc-700'>
                <SelectValue placeholder='Select album' />
              </SelectTrigger>
              <SelectContent className='bg-zinc-800 border-zinc-700'>
                <SelectItem value="no-album">
                  <div className="ml-14">No Album (Single)</div>
                </SelectItem>
                {allAlbums.map((album) => (
                  <SelectItem key={album.id} value={album.name}>
                    <div className="flex items-center justify-between w-full">
                        <div className="flex items-center gap-4">
                            <img src={album.imageUrl} alt={album.name} className="size-10 object-cover rounded"/>
                            <span>{album.name}</span>
                        </div>
                        <div className="ml-10">
                            {album.artistName}
                        </div>
                    </div>
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>
        </div>

        <DialogFooter className="sm: gap-2">
          <Button variant='outline' onClick={() => setSongDialogOpen(false)} disabled={isLoading}>
            Cancel
          </Button>
          <Button className="bg-emerald-400 hover:bg-emerald-500" onClick={handleSubmit} disabled={isLoading}>
            {isLoading ? "Uploading..." : "Add Song"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default AddSongDialog;


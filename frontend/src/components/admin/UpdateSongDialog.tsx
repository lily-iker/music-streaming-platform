import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Checkbox } from "@/components/ui/checkbox";
import axiosInstance from '../../lib/axios-custom'
import { useMusicStore } from "@/stores/useMusicStore";
import { Upload } from 'lucide-react';
import { useRef, useState, useEffect } from "react";
import toast from "react-hot-toast";
import { Song } from "@/types";

interface UpdateSongDialogProps {
  songId: number;
  isOpen: boolean;
  onClose: () => void;
}

const UpdateSongDialog: React.FC<UpdateSongDialogProps> = ({ songId, isOpen, onClose }) => {
  const { genres } = useMusicStore();
  const [isLoading, setIsLoading] = useState(false);
  const [updatedSong, setUpdatedSong] = useState<Song|null>(null);

  const [files, setFiles] = useState<{ audio: File | null; image: File | null }>({
    audio: null,
    image: null,
  });

  const audioInputRef = useRef<HTMLInputElement>(null);
  const imageInputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    const fetchSong = async () => {
      if (isOpen && songId) {
        setIsLoading(true);
        try {
          const response = await axiosInstance.get(`/api/song/${songId}`);
          setUpdatedSong(response.data.result);
        } catch (error: any) {
            toast.error("Failed to add song: " + error.response.result.message);
        } finally {
          setIsLoading(false);
        }
      }
    };

    fetchSong();
  }, [songId, isOpen]);

  const handleSubmit = async () => {
    if (!updatedSong) return;

    setIsLoading(true);
    try {
      const formData = new FormData();

      // Append SongRequest as JSON
      const songRequest = JSON.stringify({
        name: updatedSong.name,
        duration: updatedSong.duration,
        genreNames: updatedSong.genreNames.map(g => g),
        artistNames: updatedSong.artistNames.map(a => a),
        albumName: updatedSong.albumName || "",
      });
      formData.append("request", new Blob([songRequest], { type: "application/json" }));

      if (files.image) formData.append("imageFile", files.image);
      if (files.audio) formData.append("songFile", files.audio);

      await axiosInstance.put(`/api/song/${songId}`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      toast.success("Song updated successfully");
      onClose();
    } catch (error: any) {
        toast.error("Failed to add song: " + error.response.result.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleGenreChange = (genreName: string, checked: boolean) => {
    if (!updatedSong) return;
    setUpdatedSong(prev => {
      if (!prev) return prev;
      return {
        ...prev,
        genreNames: checked
          ? [...prev.genreNames, genreName]
          : prev.genreNames.filter(g => g !== genreName)
      };
    });
  };

  if (!updatedSong) {
    return null;
  }

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className='bg-zinc-900 border-zinc-700 max-h-[80vh] overflow-auto'>
        <DialogHeader>
          <DialogTitle>Update Song</DialogTitle>
          <DialogDescription>Update the details of {updatedSong.name}</DialogDescription>
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
                  <div className='text-sm text-emerald-500'>New image selected:</div>
                  <div className='text-xs text-zinc-400'>{files.image.name.slice(0, 20)}</div>
                </div>
              ) : (
                <>
                  <div className='p-3 bg-zinc-800 rounded-full inline-block mb-2'>
                    <Upload className='h-6 w-6 text-zinc-400' />
                  </div>
                  <div className='text-sm text-zinc-400 mb-2'>Upload new artwork (Optional)</div>
                  <Button variant='outline' size='sm' className='text-xs'>
                    Choose File
                  </Button>
                </>
              )}
            </div>
          </div>

          {/* Audio upload */}
          <div className='space-y-2'>
            <label className='text-sm font-medium'>New Audio File (Optional)</label>
            <div className='flex items-center gap-2'>
              <Button variant='outline' onClick={() => audioInputRef.current?.click()} className='w-full'>
                {files.audio ? files.audio.name.slice(0, 20) : "Choose New Audio File"}
              </Button>
            </div>
          </div>

          {/* other fields */}
          <div className='space-y-2'>
            <label className='text-sm font-medium'>Name</label>
            <Input
              value={updatedSong.name}
              onChange={(e) => setUpdatedSong({ ...updatedSong, name: e.target.value })}
              className='bg-zinc-800 border-zinc-700'
            />
          </div>

          <div className='space-y-2'>
            <label className='text-sm font-medium'>Duration (seconds)</label>
            <Input
              type='number'
              min='60'
              value={updatedSong.duration}
              onChange={(e) => setUpdatedSong({ ...updatedSong, duration: parseInt(e.target.value) || 60 })}
              className='bg-zinc-800 border-zinc-700'
            />
          </div>

          <div className='space-y-2'>
            <label className='text-sm font-medium'>Genres</label>
            <div className='space-y-2 max-h-40 overflow-y-auto'>
              {genres.map((genre) => (
                <div key={genre.id} className='flex items-center space-x-2'>
                  <Checkbox
                    id={`genre-${genre.id}`}
                    checked={updatedSong.genreNames.some(g => g === genre.name)}
                    onCheckedChange={(checked: boolean) => handleGenreChange(genre.name, checked)}
                  />
                  <label htmlFor={`genre-${genre.id}`} className='text-sm text-zinc-300'>
                    {genre.name}
                  </label>
                </div>
              ))}
            </div>
          </div>
        </div>

        <DialogFooter>
          <Button variant='outline' onClick={onClose} disabled={isLoading}>
            Cancel
          </Button>
          <Button onClick={handleSubmit} disabled={isLoading}>
            {isLoading ? "Updating..." : "Update Song"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default UpdateSongDialog;


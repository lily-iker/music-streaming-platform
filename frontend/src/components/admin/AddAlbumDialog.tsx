import { Button } from "@/components/ui/button";
import {
	Dialog,
	DialogContent,
	DialogDescription,
	DialogFooter,
	DialogHeader,
	DialogTitle,
	DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Plus, Upload } from "lucide-react";
import { useEffect, useRef, useState } from "react";
import toast from "react-hot-toast";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { useMusicStore } from "@/stores/useMusicStore";

const AddAlbumDialog = () => {
    const { artists, fetchArtists, addAlbum } = useMusicStore();
	const [albumDialogOpen, setAlbumDialogOpen] = useState(false);
	const [isLoading, setIsLoading] = useState(false);
	const fileInputRef = useRef<HTMLInputElement>(null);

    useEffect(() => {
        if (albumDialogOpen) {
            fetchArtists(0, 1000000);
        }
      }, [albumDialogOpen, fetchArtists]);

	const [newAlbum, setNewAlbum] = useState({
		name: "",
		artistName: "",
	});

	const [imageFile, setImageFile] = useState<File | null>(null);

	const handleImageSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
		const file = e.target.files?.[0];
		if (file) {
			setImageFile(file);
		}
	};

	const handleSubmit = async () => {
		setIsLoading(true);

		try {
			if (!imageFile) {
				return toast.error("Please upload an image");
			}

			const formData = new FormData();

            // Append SongRequest as JSON
            const albumRequest = JSON.stringify({
                name: newAlbum.name,
                artistName: newAlbum.artistName
            });

			formData.append("albumRequest", new Blob([albumRequest], { type: "application/json" }));
			formData.append("imageFile", imageFile);

			await addAlbum(formData)

			setNewAlbum({
				name: "",
				artistName: "",
			});
			setImageFile(null);

			setAlbumDialogOpen(false);
		} catch (error: any) {
			toast.error("Failed to create album: " + error.response.result.message);
		} finally {
			setIsLoading(false);
		}
	};

	return (
		<Dialog open={albumDialogOpen} onOpenChange={setAlbumDialogOpen}>
			<DialogTrigger asChild>
				<Button className='bg-violet-500 hover:bg-violet-600 text-white'>
					<Plus className='mr-2 h-4 w-4' />
					Add Album
				</Button>
			</DialogTrigger>
			<DialogContent className='bg-zinc-900 border-zinc-700'>
				<DialogHeader>
					<DialogTitle>Add New Album</DialogTitle>
					<DialogDescription>Add a new album to your collection</DialogDescription>
				</DialogHeader>
				<div className='space-y-4 py-4'>
					<input
						type='file'
						ref={fileInputRef}
						onChange={handleImageSelect}
						accept='image/*'
						className='hidden'
					/>
					<div
						className='flex items-center justify-center p-6 border-2 border-dashed border-zinc-700 rounded-lg cursor-pointer'
						onClick={() => fileInputRef.current?.click()}
					>
						<div className='text-center'>
							<div className='p-3 bg-zinc-800 rounded-full inline-block mb-2'>
								<Upload className='h-6 w-6 text-zinc-400' />
							</div>
							<div className='text-sm text-zinc-400 mb-2'>
								{imageFile ? imageFile.name : "Upload album artwork"}
							</div>
							<Button variant='outline' size='sm' className='text-xs'>
								Choose File
							</Button>
						</div>
					</div>
					<div className='space-y-2'>
						<label className='text-sm font-medium'>Album Name</label>
						<Input
							value={newAlbum.name}
							onChange={(e) => setNewAlbum({ ...newAlbum, name: e.target.value })}
							className='bg-zinc-800 border-zinc-700'
							placeholder='Enter album name'
						/>
					</div>

                    <div className='space-y-2'>
                        <label className='text-sm font-medium'>Artist</label>
                        <Select
                        value={newAlbum.artistName}
                        onValueChange={(value: string) => setNewAlbum({ ...newAlbum, artistName: value })}
                        >
                            <SelectTrigger className='bg-zinc-800 border-zinc-700'>
                                <SelectValue placeholder='Select artist' />
                            </SelectTrigger>
                            <SelectContent className='bg-zinc-800 border-zinc-700'>
                                {artists.map((artist) => (
                                <SelectItem key={artist.id} value={artist.name}>
                                    <div className="flex items-center justify-between w-full">
                                        <div className="flex items-center gap-4">
                                            <img src={artist.imageUrl} alt={artist.name} className="size-10 object-cover rounded"/>
                                            <span>{artist.name}</span>
                                        </div>
                                    </div>
                                </SelectItem>
                                ))}
                            </SelectContent>
                        </Select>
                    </div>
				</div>
                
				<DialogFooter>
					<Button variant='outline' onClick={() => setAlbumDialogOpen(false)} disabled={isLoading}>
						Cancel
					</Button>
					<Button
						onClick={handleSubmit}
						className='bg-violet-500 hover:bg-violet-600'
						disabled={isLoading || !imageFile || !newAlbum.name || !newAlbum.artistName}
					>
						{isLoading ? "Creating..." : "Add Album"}
					</Button>
				</DialogFooter>
			</DialogContent>
		</Dialog>
	);
};
export default AddAlbumDialog;
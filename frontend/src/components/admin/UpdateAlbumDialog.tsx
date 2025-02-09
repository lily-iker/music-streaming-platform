import { Button } from "@/components/ui/button"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import axiosInstance from "../../lib/axios-custom"
import { Upload } from "lucide-react"
import { useRef, useState, useEffect } from "react"
import toast from "react-hot-toast"
import type { Album } from "@/types"

interface UpdateAlbumDialogProps {
  albumId: number
  isOpen: boolean
  onClose: () => void
}

const UpdateAlbumDialog: React.FC<UpdateAlbumDialogProps> = ({ albumId, isOpen, onClose }) => {
  const [isLoading, setIsLoading] = useState(false)
  const [updatedAlbum, setUpdatedAlbum] = useState<Album | null>(null)
  const [imageFile, setImageFile] = useState<File | null>(null)
  const imageInputRef = useRef<HTMLInputElement>(null)

  useEffect(() => {
    const fetchAlbum = async () => {
      if (isOpen && albumId) {
        setIsLoading(true)
        try {
          const response = await axiosInstance.get(`/api/album/${albumId}`)
          setUpdatedAlbum(response.data.result)
        } catch (error: any) {
          toast.error("Failed to fetch album: " + error.response?.result?.message || "Unknown error")
        } finally {
          setIsLoading(false)
        }
      }
    }

    fetchAlbum()
  }, [albumId, isOpen])

  const handleSubmit = async () => {
    if (!updatedAlbum) return

    setIsLoading(true)
    try {
      const formData = new FormData()

      const updateAlbumRequest = JSON.stringify({
        name: updatedAlbum.name,
      })
      formData.append("updateAlbumRequest", new Blob([updateAlbumRequest], { type: "application/json" }))

      if (imageFile) formData.append("imageFile", imageFile)

      await axiosInstance.put(`/api/album/${albumId}`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })

      toast.success("Album updated successfully")
      onClose()
    } catch (error: any) {
      toast.error("Failed to update album: " + error.response?.result?.message || "Unknown error")
    } finally {
      setIsLoading(false)
    }
  }

  if (!updatedAlbum) {
    return null
  }

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="bg-zinc-900 border-zinc-700 max-h-[80vh] overflow-auto">
        <DialogHeader>
          <DialogTitle>Update Album</DialogTitle>
          <DialogDescription>Update the details of {updatedAlbum.name}</DialogDescription>
        </DialogHeader>

        <div className="space-y-4 py-4">
          <input
            type="file"
            ref={imageInputRef}
            className="hidden"
            accept="image/*"
            onChange={(e) => setImageFile(e.target.files?.[0] || null)}
          />

          {/* image upload area */}
          <div
            className="flex items-center justify-center p-6 border-2 border-dashed border-zinc-700 rounded-lg cursor-pointer"
            onClick={() => imageInputRef.current?.click()}
          >
            <div className="text-center">
              {imageFile ? (
                <div className="space-y-2">
                  <div className="text-sm text-emerald-500">New image selected:</div>
                  <div className="text-xs text-zinc-400">{imageFile.name.slice(0, 20)}</div>
                </div>
              ) : (
                <>
                  <div className="p-3 bg-zinc-800 rounded-full inline-block mb-2">
                    <Upload className="h-6 w-6 text-zinc-400" />
                  </div>
                  <div className="text-sm text-zinc-400 mb-2">Upload new artwork (Optional)</div>
                  <Button variant="outline" size="sm" className="text-xs">
                    Choose File
                  </Button>
                </>
              )}
            </div>
          </div>

          {/* Album name field */}
          <div className="space-y-2">
            <label className="text-sm font-medium">Album Name</label>
            <Input
              value={updatedAlbum.name}
              onChange={(e) => setUpdatedAlbum({ ...updatedAlbum, name: e.target.value })}
              className="bg-zinc-800 border-zinc-700"
            />
          </div>
        </div>

        <DialogFooter>
          <Button variant="outline" onClick={onClose} disabled={isLoading}>
            Cancel
          </Button>
          <Button onClick={handleSubmit} disabled={isLoading}>
            {isLoading ? "Updating..." : "Update Album"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}

export default UpdateAlbumDialog


package music.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return (String) uploadResult.get("url");
    }

    public String uploadAudio(MultipartFile file) throws IOException {
        Map param = ObjectUtils.asMap("resource_type", "video");
//                "folder", "audio_files/", // Optional: Specify the folder where the audio file will be stored
//                "public_id", file.getOriginalFilename(), // Optional: Use the original filename as the public ID
//                "overwrite", true;
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), param);
        return (String) uploadResult.get("url");
    }
}

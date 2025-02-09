package music.utils;

import music.exception.AudioUploadException;
import music.exception.ImageUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MultipartFileUtil {

    @Value("${CLOUDINARY_MAX_IMAGE_SIZE}")
    private int maxImageSize;

    @Value("${CLOUDINARY_MAX_AUDIO_SIZE}")
    private int maxAudioSize;

    public void isImageValid(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty())
            throw new ImageUploadException("Failed to upload image");
        if (imageFile.getSize() > maxImageSize)
            throw new ImageUploadException("Image file size exceeds the maximum limit of " +
                    (maxImageSize / (1024 * 1024)) + " MB");
    }

    public void isAudioValid(MultipartFile songFile) {
        if (songFile == null || songFile.isEmpty())
            throw new AudioUploadException("Failed to upload audio");
        if (songFile.getSize() > maxAudioSize)
            throw new AudioUploadException("Audio file size exceeds the maximum limit of " +
                    (maxAudioSize / (1024 * 1024)) + " MB");
    }
}

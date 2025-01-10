package com.project.app.imageStorage;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;


@Service
public class ImageService {

    @Autowired
    private Cloudinary cloudinary;

    public String saveImage(MultipartFile file) throws IOException{
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", "auto"));
        
        return uploadResult.get("url").toString();
    }

    public void deleteImage(String imageUrl) {
        try {
            // Extract public id from the URL
            String publicId = extractPublicIdFromUrl(imageUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image from Cloudinary", e);
        }
    }

    private String extractPublicIdFromUrl(String imageUrl) {
        // Extract the public ID from the Cloudinary URL
        String[] urlParts = imageUrl.split("/");
        String fileName = urlParts[urlParts.length - 1];
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }
    
}
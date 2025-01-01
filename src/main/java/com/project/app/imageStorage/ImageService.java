package com.project.app.imageStorage;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ImageService {
    
    @Value("${file.upload-dir}")
    private String uploadDir;

    public String saveImage(MultipartFile file) throws IOException{
        
        File directory = new File(uploadDir);

        if (!directory.exists()){
            directory.mkdirs();
        }

        String filename = LocalDateTime.now().toString().replace(":", "-") + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, filename);

        file.transferTo(filePath);

        return "/uploads/" + filename;
    }
    
}

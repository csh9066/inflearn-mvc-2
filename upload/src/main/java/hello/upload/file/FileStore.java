package hello.upload.file;

import hello.upload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class FileStore {
    
    @Value("${file.dir}") 
    private String fileDir;
    
    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException{
        List<UploadFile> result = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                result.add(storeFile(multipartFile));
            }
        }
        return result;
    }
    
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFIleName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFIleName)));

        return new UploadFile(originalFilename, storeFIleName);
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        UUID uuid = UUID.randomUUID();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int post = originalFilename.lastIndexOf(".");
        return originalFilename.substring(post + 1);
    }

}

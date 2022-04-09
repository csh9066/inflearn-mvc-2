package hello.upload.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UploadFile {
    private String uploadFileName;
    private String storeFileName;
}

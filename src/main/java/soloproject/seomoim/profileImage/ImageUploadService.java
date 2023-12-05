package soloproject.seomoim.profileImage;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import io.grpc.Context;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageUploadService {

    @Value("${file.dir}")
    private String fileDir;

    @Value("${cloud.bucket}")
    private String bucketName;

    private  final Storage storage;

    private String getFullPath(MultipartFile file){
        String storeFileName = createStoreFileName(file.getOriginalFilename());
        return fileDir+storeFileName;
    }

    /*todo 이미지 리사이징 유효성검사 파일 형식검증  */

    public String save(MultipartFile file) throws IOException {
        String fullPath = getFullPath(file);
        if (!file.isEmpty()) {
            try (OutputStream outputStream = new FileOutputStream(fullPath)) {
                InputStream inputStream = file.getInputStream();

                byte[] buffer = new byte[1024];//읽어서 버퍼에 저장할구야  실제 읽은 바이트 수를 반환하니까 ㅎㅎ 읽은 바이트수가 -1 이되기전따비 뎨곧 닝ㄷ능더도
                int bytesRead; //읽은 바이트수
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                return fullPath;
            }
        }
        return fullPath;
    }

    public String uploadFileToGCS(MultipartFile file) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String ext = file.getContentType();

        Blob blob = storage.create(BlobInfo.newBuilder(bucketName, uuid)
                .setContentType(ext)
                .build(), file.getInputStream());
        return  uuid;
    }


    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }


}

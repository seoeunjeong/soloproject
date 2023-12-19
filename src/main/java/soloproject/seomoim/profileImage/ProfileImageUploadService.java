package soloproject.seomoim.profileImage;

import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import soloproject.seomoim.member.entity.Member;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProfileImageUploadService {


    @Value("${cloud.bucket}")
    private String bucketName;

    private final Storage storage;

    private final ProfileImageRepository profileImageRepository;
    private static final String IMAGE_DEFAULT_URL = "https://storage.googleapis.com/seomoim/";

    /*todo 이미지 리사이징 유효성검사 파일 형식검증  */

    public void uploadFileToGCS(MultipartFile file, Member member) throws IOException {

        Optional<ProfileImage> profileImage1 = profileImageRepository.findByMember(member);
        profileImage1.ifPresent(profileImage ->
        {
            String uuid1 = profileImage.getUuid();

            if(profileImage.getProfileImageUrl().startsWith("https://lh3")){
                try {
                    uploadDataFromUrl(profileImage.getProfileImageUrl(),bucketName,uuid1);
                }catch (IOException e){
                    throw  new RuntimeException(e);
                }
            }

            try {
                uploadFile(bucketName, uuid1, file.getContentType(), file.getInputStream());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        if (profileImage1.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            uploadFile(bucketName, uuid, file.getContentType(), file.getInputStream());


            ProfileImage profileImage = new ProfileImage();
            profileImage.setMember(member);
            profileImage.setProfileImageUrl(IMAGE_DEFAULT_URL + uuid);
            profileImage.setUuid(uuid);
            profileImageRepository.save(profileImage);
        }

    }

    @Transactional
    public void deleteFile(Member findMember){
        if(findMember.getProfileImage()!=null) {
            log.info("findMember.getProfileImage()={}",findMember.getProfileImage());
            String uuid1 = findMember.getProfileImage().getUuid();
            BlobId blobId = BlobId.of(bucketName, uuid1);
            storage.delete(blobId);
            profileImageRepository.delete(findMember.getProfileImage());
        }
    }

    private void uploadFile(String bucketName, String uuid, String ext, InputStream inputStream) throws IOException {
        BlobId blobId = BlobId.of(bucketName, uuid);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(ext)
                .setCacheControl("no-cache")
                .build();

        storage.create(blobInfo, inputStream);
    }


    public void uploadDataFromUrl(String remoteUrl, String bucketName, String uuid) throws IOException {
        try {
            // URL에서 데이터 읽기
            URL url = new URL(remoteUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();


            BlobId blobId = BlobId.of(bucketName, uuid);

            // Google Cloud Storage로 데이터 업로드
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

            storage.create(blobInfo, inputStream);

            inputStream.close();
            connection.disconnect();

            System.out.println("Data uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



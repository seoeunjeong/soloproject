package soloproject.seomoim.member.profileImage;

import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import soloproject.seomoim.member.entity.Member;
import soloproject.seomoim.member.repository.MemberRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ProfileImageUploadService {

    @Value("${cloud.bucket}")
    private String bucketName;

    private final Storage storage;

    private final MemberRepository memberRepository;
    private static final String IMAGE_DEFAULT_URL = "https://storage.googleapis.com/seomoim/";

    /*todo 이미지 리사이징 */
    public void uploadFileToGCS(MultipartFile file, Member member) throws IOException {

        if (member.getProfileImage() != null) {
            deleteFile(member);
        }

        String uuid = UUID.randomUUID().toString();
        uploadFile(bucketName, uuid, file.getContentType(), file.getInputStream());

        ProfileImage profileImage = new ProfileImage();
        profileImage.setProfileImageUrl(IMAGE_DEFAULT_URL + uuid);
        profileImage.setUuid(uuid);
        member.setProfileImage(profileImage);
        memberRepository.save(member);
    }


    @Transactional
    public void deleteFile(Member member) {
        ProfileImage profileImage = member.getProfileImage();
        String uuid1 = profileImage.getUuid();
        BlobId blobId = BlobId.of(bucketName, uuid1);
        storage.delete(blobId);
        member.setProfileImage(null);
    }

    private void uploadFile(String bucketName, String uuid, String ext, InputStream inputStream) {
        BlobId blobId = BlobId.of(bucketName, uuid);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(ext)
                .setCacheControl("no-cache")
                .build();

        storage.create(blobInfo, inputStream);
    }

}



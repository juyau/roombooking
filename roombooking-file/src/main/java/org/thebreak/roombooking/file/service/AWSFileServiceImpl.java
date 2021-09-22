package org.thebreak.roombooking.file.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thebreak.roombooking.common.exception.CustomException;
import org.thebreak.roombooking.common.response.CommonCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AWSFileServiceImpl implements AWSFileService {

    @Value("${bookingBucket.name}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String s3Region;

    @Autowired
    private AmazonS3Client s3Client;
    @Override
    public String uploadImage(MultipartFile file) {
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String key = UUID.randomUUID().toString() + "." + extension;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        metadata.setContentType(file.getContentType());
        try {
            s3Client.putObject(bucketName, key, file.getInputStream(), metadata);
        }catch (IOException e){
            CustomException.cast(CommonCode.S3_FILE_UPLOAD_FAILED);
        }
        s3Client.setObjectAcl(bucketName, key, CannedAccessControlList.PublicReadWrite);
        return s3Client.getResourceUrl(bucketName,key);
    }

    @Override
    public void deleteImage(String url) {
        try {
            s3Client.deleteObject(bucketName, url.substring(url.lastIndexOf("/") + 1));
        }catch (Exception e){
            CustomException.cast(CommonCode.S3_FILE_UPLOAD_FAILED);
        }
    }

    @Override
    public List<String> listImages() {
        String awsBucketUrl = "https://" + bucketName + ".s3." + s3Region + ".amazonaws.com/";
        List<String> list = new ArrayList<>();
        try {
            ObjectListing objectListing = s3Client.listObjects(bucketName);
            for(S3ObjectSummary os : objectListing.getObjectSummaries()){
                list.add(awsBucketUrl + os.getKey());
            }
            return list;
        } catch (Exception e){
            CustomException.cast(CommonCode.S3_FILE_UPLOAD_FAILED);
        }
        return null;
    }
}

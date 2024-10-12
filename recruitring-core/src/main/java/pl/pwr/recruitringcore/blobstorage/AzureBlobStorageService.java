package pl.pwr.recruitringcore.blobstorage;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface AzureBlobStorageService {

    ResponseEntity<String> upload(MultipartFile multipartFile, String blobName) throws IOException;

    ResponseEntity<String> getFileUrlToDownload(String blobName);

    ResponseEntity<String> removeFileFromBlobStorage(String blobName);
}

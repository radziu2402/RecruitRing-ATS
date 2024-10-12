package pl.pwr.recruitringcore.blobstorage;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.pwr.recruitringcore.blobstorage.client.AzureBlobClient;

import java.io.IOException;


@Component
public class AzureBlobStorageServiceImpl implements AzureBlobStorageService {

    private static final String QUERY_PARAM_DIVIDER = "?";

    private final BlobClientBuilder blobClientBuilder;

    private final AzureBlobPermission azureBlobPermission;

    public AzureBlobStorageServiceImpl(AzureBlobClient azureBlobClient, AzureBlobPermission azureBlobPermission) {
        this.blobClientBuilder = azureBlobClient.getBlobStorageClient();
        this.azureBlobPermission = azureBlobPermission;
    }

    @Override
    public ResponseEntity<String> upload(MultipartFile file, String blobName) {
        BlobClient blobClient = blobClientBuilder
                .blobName(blobName)
                .buildClient();

        try {
            blobClient.upload(BinaryData.fromStream(file.getInputStream()), true);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
        }

        return ResponseEntity.ok(blobClient.getBlobName());
    }

    @Override
    public ResponseEntity<String> getFileUrlToDownload(String blobName) {
        BlobClient blobClient = blobClientBuilder.blobName(blobName).buildClient();

        if (Boolean.FALSE.equals(blobClient.exists())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found.");
        }

        BlobServiceSasSignatureValues readPermissionSas = azureBlobPermission.getReadPermissionSas();
        String fileUrl = blobClient.getBlobUrl() + QUERY_PARAM_DIVIDER + blobClient.generateSas(readPermissionSas);

        return ResponseEntity.ok(fileUrl);
    }

    @Override
    public ResponseEntity<String> removeFileFromBlobStorage(String blobName) {
        BlobClient blobClient = blobClientBuilder.blobName(blobName).buildClient();

        if (Boolean.FALSE.equals(blobClient.exists())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found.");
        }

        blobClient.delete();
        return ResponseEntity.ok("File deleted successfully.");
    }
}

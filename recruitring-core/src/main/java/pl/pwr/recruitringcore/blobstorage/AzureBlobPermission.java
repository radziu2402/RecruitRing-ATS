package pl.pwr.recruitringcore.blobstorage;

import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;


@Component
public class AzureBlobPermission {

    private static final String READ_PERMISSION = "r";
    private static final Integer MINUTES = 10;

    public BlobServiceSasSignatureValues getReadPermissionSas() {
        BlobSasPermission permission = BlobSasPermission.parse(READ_PERMISSION);
        return new BlobServiceSasSignatureValues(OffsetDateTime.now().plusMinutes(MINUTES), permission);
    }
}

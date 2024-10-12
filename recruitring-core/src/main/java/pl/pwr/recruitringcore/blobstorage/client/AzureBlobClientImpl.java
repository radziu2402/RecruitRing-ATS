package pl.pwr.recruitringcore.blobstorage.client;

import com.azure.storage.blob.BlobClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class AzureBlobClientImpl implements AzureBlobClient {

    @Value("${azure.storage.account-url}")
    private String connectionString;

    @Value("${azure.storage.container-name}")
    private String blobStorageContainerName;

    @Override
    public BlobClientBuilder getBlobStorageClient() {
        return new BlobClientBuilder()
                .connectionString(connectionString)
                .containerName(blobStorageContainerName);
    }
}

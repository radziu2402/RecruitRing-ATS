package pl.pwr.recruitringcore.blobstorage.client;

import com.azure.storage.blob.BlobClientBuilder;


public interface AzureBlobClient {

    BlobClientBuilder getBlobStorageClient();

}

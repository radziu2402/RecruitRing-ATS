package pl.pwr.recruitringcore.blobstorage;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import pl.pwr.recruitringcore.blobstorage.client.AzureBlobClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AzureBlobStorageServiceImplTest {

    @Mock
    private BlobClientBuilder blobClientBuilder;

    @Mock
    private AzureBlobPermission azureBlobPermission;

    @Mock
    private BlobClient blobClient;

    @Mock
    private MultipartFile file;

    @Mock
    private AzureBlobClient azureBlobClient;

    private AzureBlobStorageServiceImpl azureBlobStorageServiceImpl;

    @BeforeEach
    void setUp() {
        when(azureBlobClient.getBlobStorageClient()).thenReturn(blobClientBuilder);
        when(blobClientBuilder.buildClient()).thenReturn(blobClient);

        azureBlobStorageServiceImpl = new AzureBlobStorageServiceImpl(azureBlobClient, azureBlobPermission);
    }

    @Test
    void shouldUploadFileSuccessfully() throws IOException {
        // GIVEN
        String blobName = "testBlob";
        InputStream fileStream = new ByteArrayInputStream("test content".getBytes());
        when(file.getInputStream()).thenReturn(fileStream);
        when(blobClientBuilder.blobName(any(String.class))).thenAnswer(invocation -> {
            String passedBlobName = invocation.getArgument(0, String.class);
            when(blobClient.getBlobName()).thenReturn(passedBlobName);
            return blobClientBuilder;
        });

        // WHEN
        ResponseEntity<String> response = azureBlobStorageServiceImpl.upload(file, blobName);

        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(blobName, response.getBody());
        verify(blobClient).upload(any(BinaryData.class), eq(true));
    }

    @Test
    void shouldReturnInternalServerErrorOnUploadFailure() throws IOException {
        // GIVEN
        String blobName = "testBlob";
        when(file.getInputStream()).thenThrow(IOException.class);
        when(blobClientBuilder.blobName(any(String.class))).thenReturn(blobClientBuilder);

        // WHEN
        ResponseEntity<String> response = azureBlobStorageServiceImpl.upload(file, blobName);

        // THEN
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to upload file.", response.getBody());
    }

    @Test
    void shouldReturnFileUrlToDownloadWhenFileExists() {
        // GIVEN
        String blobName = "testBlob";
        String fileUrl = "http://bloburl/testBlob";
        BlobServiceSasSignatureValues sasValues = mock(BlobServiceSasSignatureValues.class);
        when(blobClient.exists()).thenReturn(true);
        when(blobClientBuilder.blobName(any(String.class))).thenReturn(blobClientBuilder);
        when(blobClient.getBlobUrl()).thenReturn(fileUrl);
        when(blobClient.generateSas(any(BlobServiceSasSignatureValues.class))).thenReturn("sasToken");
        when(azureBlobPermission.getReadPermissionSas()).thenReturn(sasValues);

        // WHEN
        ResponseEntity<String> response = azureBlobStorageServiceImpl.getFileUrlToDownload(blobName);

        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fileUrl + "?sasToken", response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenFileDoesNotExist() {
        // GIVEN
        String blobName = "nonExistentBlob";
        when(blobClient.exists()).thenReturn(false);
        when(blobClientBuilder.blobName(any(String.class))).thenReturn(blobClientBuilder);

        // WHEN
        ResponseEntity<String> response = azureBlobStorageServiceImpl.getFileUrlToDownload(blobName);

        // THEN
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("File not found.", response.getBody());
    }

    @Test
    void shouldDeleteFileSuccessfully() {
        // GIVEN
        String blobName = "testBlob";
        when(blobClient.exists()).thenReturn(true);
        when(blobClientBuilder.blobName(any(String.class))).thenReturn(blobClientBuilder);

        // WHEN
        ResponseEntity<String> response = azureBlobStorageServiceImpl.removeFileFromBlobStorage(blobName);

        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("File deleted successfully.", response.getBody());
        verify(blobClient).delete();
    }

    @Test
    void shouldReturnNotFoundOnDeleteWhenFileDoesNotExist() {
        // GIVEN
        String blobName = "nonExistentBlob";
        when(blobClient.exists()).thenReturn(false);
        when(blobClientBuilder.blobName(any(String.class))).thenReturn(blobClientBuilder);

        // WHEN
        ResponseEntity<String> response = azureBlobStorageServiceImpl.removeFileFromBlobStorage(blobName);

        // THEN
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("File not found.", response.getBody());
    }

}

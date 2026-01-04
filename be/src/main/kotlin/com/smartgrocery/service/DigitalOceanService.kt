package com.smartgrocery.service

import com.smartgrocery.exception.ApiException
import com.smartgrocery.exception.ErrorCode
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.net.URI
import java.util.*

@Service
class DigitalOceanService(
    @Value("\${digitalocean.spaces.access-key}") private val accessKey: String,
    @Value("\${digitalocean.spaces.secret-key}") private val secretKey: String,
    @Value("\${digitalocean.spaces.endpoint}") private val endpoint: String,
    @Value("\${digitalocean.spaces.region:nyc3}") private val region: String,
    @Value("\${digitalocean.spaces.bucket}") private val bucket: String,
    @Value("\${digitalocean.spaces.folder:smart-grocery}") private val baseFolder: String,
    @Value("\${file.max-size:5242880}") private val maxFileSize: Long
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val s3Client: S3Client by lazy {
        val credentials = AwsBasicCredentials.create(accessKey, secretKey)
        S3Client.builder()
            .endpointOverride(URI(endpoint))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .region(Region.of(region))
            .build()
    }

    companion object {
        private val ALLOWED_IMAGE_TYPES = setOf(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/webp"
        )

        private val ALLOWED_EXTENSIONS = setOf(
            "jpg", "jpeg", "png", "gif", "webp"
        )
    }

    /**
     * Upload a file to DigitalOcean Spaces and return the full URL
     * @param file The file to upload
     * @param subFolder Subfolder within the base folder (e.g., "users", "families")
     * @return Full URL of the uploaded image
     */
    fun uploadFile(file: MultipartFile, subFolder: String = ""): String {
        validateFile(file)

        val folder = if (subFolder.isNotBlank()) {
            "$baseFolder/$subFolder"
        } else {
            baseFolder
        }

        // Generate unique key
        val originalFilename = StringUtils.cleanPath(file.originalFilename ?: "file")
        val uniqueName = "${UUID.randomUUID()}_${originalFilename}"
        val s3Key = "$folder/$uniqueName"

        try {
            val putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(s3Key)
                .contentType(file.contentType)
                .build()

            s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.bytes))

            val fileUrl = "$endpoint/$bucket/$s3Key"
            logger.info("File uploaded successfully to DigitalOcean Spaces: $fileUrl")
            return fileUrl

        } catch (ex: Exception) {
            logger.error("Failed to upload file to DigitalOcean Spaces", ex)
            throw ApiException(ErrorCode.FILE_UPLOAD_FAILED, "Could not upload file: ${ex.message}")
        }
    }

    /**
     * Delete a file from DigitalOcean Spaces by its URL
     * @param imageUrl The full URL or S3 key
     * @return true if deleted successfully
     */
    fun deleteFile(imageUrl: String): Boolean {
        if (imageUrl.isBlank()) return false

        return try {
            val s3Key = extractS3Key(imageUrl)
            if (s3Key != null) {
                val deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(s3Key)
                    .build()

                s3Client.deleteObject(deleteObjectRequest)
                logger.info("File deleted from DigitalOcean Spaces: $s3Key")
                true
            } else {
                logger.warn("Could not extract S3 key from URL: $imageUrl")
                false
            }
        } catch (ex: Exception) {
            logger.error("Failed to delete file from DigitalOcean Spaces: $imageUrl", ex)
            false
        }
    }

    /**
     * Extract S3 key from DigitalOcean Spaces URL
     * URL format: https://bucket-name.endpoint/path/to/file
     */
    private fun extractS3Key(url: String): String? {
        return try {
            // If it's already a key (doesn't contain endpoint), return as is
            if (!url.contains(endpoint) && !url.contains(bucket)) {
                return url
            }

            // Parse the URL to extract S3 key
            val regex = Regex("""$bucket/(.+)$""")
            val matchResult = regex.find(url)
            matchResult?.groupValues?.get(1)
        } catch (ex: Exception) {
            logger.error("Failed to extract S3 key from URL: $url", ex)
            null
        }
    }

    /**
     * Check if DigitalOcean Spaces is configured properly
     */
    fun isConfigured(): Boolean {
        return accessKey.isNotBlank() && secretKey.isNotBlank() && 
               endpoint.isNotBlank() && bucket.isNotBlank()
    }

    private fun validateFile(file: MultipartFile) {
        // Check if file is empty
        if (file.isEmpty) {
            throw ApiException(ErrorCode.FILE_UPLOAD_FAILED, "Cannot upload empty file")
        }

        // Check file size
        if (file.size > maxFileSize) {
            throw ApiException(
                ErrorCode.FILE_TOO_LARGE,
                "File size exceeds maximum allowed size of ${maxFileSize / 1024 / 1024}MB"
            )
        }

        // Check content type
        val contentType = file.contentType
        if (contentType == null || contentType !in ALLOWED_IMAGE_TYPES) {
            throw ApiException(
                ErrorCode.INVALID_FILE_TYPE,
                "Only image files are allowed (JPEG, PNG, GIF, WebP)"
            )
        }

        // Check file extension
        val filename = StringUtils.cleanPath(file.originalFilename ?: "")
        val extension = filename.substringAfterLast('.', "").lowercase()
        if (extension !in ALLOWED_EXTENSIONS) {
            throw ApiException(
                ErrorCode.INVALID_FILE_TYPE,
                "Invalid file extension. Allowed: ${ALLOWED_EXTENSIONS.joinToString(", ")}"
            )
        }
    }
}



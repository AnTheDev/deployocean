package com.smartgrocery.service

import com.smartgrocery.exception.ApiException
import com.smartgrocery.exception.ErrorCode
import com.smartgrocery.exception.ResourceNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

@Service
class FileStorageService(
    @Value("\${file.upload-dir:uploads}") private val uploadDir: String,
    @Value("\${file.max-size:5242880}") private val maxFileSize: Long // 5MB default
) {

    private val fileStorageLocation: Path = Paths.get(uploadDir).toAbsolutePath().normalize()

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

    init {
        try {
            Files.createDirectories(fileStorageLocation)
        } catch (ex: Exception) {
            throw RuntimeException("Could not create the directory where the uploaded files will be stored.", ex)
        }
    }

    /**
     * Store a file and return the generated filename
     */
    fun storeFile(file: MultipartFile, subDirectory: String = ""): String {
        // Validate file
        validateFile(file)

        // Generate unique filename
        val originalFilename = StringUtils.cleanPath(file.originalFilename ?: "file")
        val extension = getFileExtension(originalFilename)
        val newFilename = "${UUID.randomUUID()}.$extension"

        // Determine target directory
        val targetLocation = if (subDirectory.isNotBlank()) {
            val subDir = fileStorageLocation.resolve(subDirectory)
            Files.createDirectories(subDir)
            subDir.resolve(newFilename)
        } else {
            fileStorageLocation.resolve(newFilename)
        }

        try {
            // Copy file to target location
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)

            // Return relative path for storage in database
            return if (subDirectory.isNotBlank()) {
                "$subDirectory/$newFilename"
            } else {
                newFilename
            }
        } catch (ex: IOException) {
            throw ApiException(ErrorCode.FILE_UPLOAD_FAILED, "Could not store file: ${ex.message}")
        }
    }

    /**
     * Load a file as Resource
     */
    fun loadFileAsResource(filename: String): Resource {
        try {
            val filePath = fileStorageLocation.resolve(filename).normalize()
            val resource = UrlResource(filePath.toUri())

            if (resource.exists() && resource.isReadable) {
                return resource
            } else {
                throw ResourceNotFoundException(ErrorCode.FILE_NOT_FOUND, "File not found: $filename")
            }
        } catch (ex: Exception) {
            when (ex) {
                is ResourceNotFoundException -> throw ex
                else -> throw ResourceNotFoundException(ErrorCode.FILE_NOT_FOUND, "File not found: $filename")
            }
        }
    }

    /**
     * Delete a file
     */
    fun deleteFile(filename: String): Boolean {
        return try {
            val filePath = fileStorageLocation.resolve(filename).normalize()
            Files.deleteIfExists(filePath)
        } catch (ex: IOException) {
            false
        }
    }

    /**
     * Get the content type of a file
     */
    fun getContentType(filename: String): String {
        val extension = getFileExtension(filename).lowercase()
        return when (extension) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "webp" -> "image/webp"
            else -> "application/octet-stream"
        }
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
        val extension = getFileExtension(filename).lowercase()
        if (extension !in ALLOWED_EXTENSIONS) {
            throw ApiException(
                ErrorCode.INVALID_FILE_TYPE,
                "Invalid file extension. Allowed: ${ALLOWED_EXTENSIONS.joinToString(", ")}"
            )
        }
    }

    private fun getFileExtension(filename: String): String {
        val dotIndex = filename.lastIndexOf('.')
        return if (dotIndex > 0 && dotIndex < filename.length - 1) {
            filename.substring(dotIndex + 1)
        } else {
            ""
        }
    }
}


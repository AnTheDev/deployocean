package com.smartgrocery.controller

import com.smartgrocery.service.FileStorageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/files")
@Tag(name = "Files", description = "Public file serving APIs")
class FileController(
    private val fileStorageService: FileStorageService
) {

    @GetMapping("/{filename:.+}")
    @Operation(summary = "Get a file by filename (public)")
    fun getFile(@PathVariable filename: String): ResponseEntity<Resource> {
        val resource = fileStorageService.loadFileAsResource(filename)
        val contentType = fileStorageService.getContentType(filename)

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"$filename\"")
            .header(HttpHeaders.CACHE_CONTROL, "max-age=86400") // Cache for 1 day
            .body(resource)
    }

    @GetMapping("/{subdir}/{filename:.+}")
    @Operation(summary = "Get a file from subdirectory by filename (public)")
    fun getFileFromSubdir(
        @PathVariable subdir: String,
        @PathVariable filename: String
    ): ResponseEntity<Resource> {
        val fullPath = "$subdir/$filename"
        val resource = fileStorageService.loadFileAsResource(fullPath)
        val contentType = fileStorageService.getContentType(filename)

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"$filename\"")
            .header(HttpHeaders.CACHE_CONTROL, "max-age=86400")
            .body(resource)
    }
}


package com.smartgrocery.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * @deprecated This controller is deprecated. Images are now served directly from Cloudinary CDN.
 * All image URLs (avatarUrl, imageUrl) returned from API are now full Cloudinary URLs.
 * 
 * Example Cloudinary URL:
 * https://res.cloudinary.com/{cloud_name}/image/upload/v{version}/{folder}/{public_id}.{format}
 * 
 * This endpoint is kept for backward compatibility with old local images (if any).
 */
@RestController
@RequestMapping("/files")
@Tag(name = "Files", description = "Legacy file serving APIs (deprecated - use Cloudinary URLs directly)")
class FileController {

    @GetMapping("/{filename:.+}")
    @Operation(
        summary = "[DEPRECATED] Get a file by filename",
        description = "This endpoint is deprecated. Images are now served directly from Cloudinary CDN."
    )
    fun getFile(@PathVariable filename: String): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.GONE)
            .body(mapOf(
                "message" to "This endpoint is deprecated. Images are now served from Cloudinary CDN.",
                "hint" to "Use the full URL returned in imageUrl or avatarUrl fields directly."
            ))
    }

    @GetMapping("/{subdir}/{filename:.+}")
    @Operation(
        summary = "[DEPRECATED] Get a file from subdirectory",
        description = "This endpoint is deprecated. Images are now served directly from Cloudinary CDN."
    )
    fun getFileFromSubdir(
        @PathVariable subdir: String,
        @PathVariable filename: String
    ): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.GONE)
            .body(mapOf(
                "message" to "This endpoint is deprecated. Images are now served from Cloudinary CDN.",
                "hint" to "Use the full URL returned in imageUrl or avatarUrl fields directly."
            ))
    }
}


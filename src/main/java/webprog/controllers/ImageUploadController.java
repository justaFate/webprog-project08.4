package webprog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import webprog.services.IStorageService;

@Controller
@Tag(name = "product-controller", description = "Serve static images for products and categories")
public class ImageUploadController {

    @Autowired
    private IStorageService storageService;

    @Operation(summary = "Xem hình ảnh sản phẩm")
    @GetMapping("/admin/products/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getProductImage(@PathVariable String filename) {
        try {
            Resource file = storageService.loadAsResource(filename);
            MediaType mediaType = determineMediaType(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                    .contentType(mediaType)
                    .body(file);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Xem hình ảnh danh mục")
    @GetMapping("/admin/categories/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getCategoryImage(@PathVariable String filename) {
        try {
            Resource file = storageService.loadAsResource(filename);
            MediaType mediaType = determineMediaType(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                    .contentType(mediaType)
                    .body(file);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private MediaType determineMediaType(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (lower.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}


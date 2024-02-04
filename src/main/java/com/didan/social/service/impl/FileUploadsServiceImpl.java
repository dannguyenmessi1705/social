package com.didan.social.service.impl;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadsServiceImpl {
    boolean validateFile(MultipartFile file);
    boolean isSupportedExtension(String extension);
    boolean isSupportedContentType(String contentType);
    String storeFile(MultipartFile file, String typeFile, String id) throws Exception;
    boolean deleteFile(String fileName) throws Exception;
}

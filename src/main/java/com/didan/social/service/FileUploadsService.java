package com.didan.social.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadsService {
    boolean validateFile(MultipartFile file);
    boolean isSupportedExtension(String extension);
    boolean isSupportedContentType(String contentType);
    String storeFile(MultipartFile file, String typeFile, String id) throws Exception;
    boolean deleteFile(String fileName) throws Exception;
}

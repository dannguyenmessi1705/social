package com.didan.social.service;

import com.didan.social.service.impl.FileUploadsServiceImpl;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

@Service
public class FileUploadsService implements FileUploadsServiceImpl {
    private static Logger logger = LoggerFactory.getLogger(FileUploadsService.class);
    private final Environment env;
    @Autowired
    public FileUploadsService(Environment env){
        this.env = env;
    }
    private Path rootPath1; // 1 đường dẫn tạo folder ở trong src
    private Path rootPath2; // 1 đường dẫn tạo folder ở ngoài src (trong ./target) (ko cần khởi động lại server, truy cập ngay lập tức)

    public void init(String typeFile){
        this.rootPath1 = Paths.get(env.getProperty("app.file.upload-dir","./src/main/resources/static/uploads/images")+"/"+typeFile).toAbsolutePath().normalize();
        this.rootPath2 = Paths.get(env.getProperty("app.file.no-reload", "./target/classes/static/uploads/images")+"/"+typeFile).toAbsolutePath().normalize();
        try{
            Files.createDirectories(this.rootPath1);
            Files.createDirectories(this.rootPath2);
        }catch (Exception e){
            logger.error("Could not create the directory where the uploaded files will be stored");
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored", e);
        }
    }

    @Override
    public boolean validateFile(MultipartFile file) {
        try{
            if(file.getSize() >= (10 * 1024 * 1e6)){
                System.out.println(file.getSize());
                logger.error("File size should be less than 10MB");
                throw new Exception("File size should be less than 10MB");
            }
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            if(!isSupportedExtension(extension)){
                logger.error("File extension is not support");
                throw new Exception("File extension is not support");
            }
            Tika tika = new Tika();
            String mimeType = tika.detect(file.getInputStream());
            if(!isSupportedContentType(mimeType)){
                logger.error("Content type of file is not support");
                throw new Exception("Content type of file is not support");
            }
            return true;
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isSupportedExtension(String extension) {
        return extension != null &&
                extension.equals("png")
                || extension.equals("jpg")
                || extension.equals("jpeg");
    }

    @Override
    public boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png")
                || contentType.equals("image/jpg")
                || contentType.equals("image/jpeg");
    }

    @Override
    public String storeFile(MultipartFile file, String typeFile, String id) throws Exception {
        init(typeFile);
        String fileName = id + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        try{
            if(validateFile(file)){
                if (fileName.contains("..")) { // Kiểm tra xem tên file có chứa các kí tự đặc biệt không
                    logger.error("Sorry! Filename contains invalid path sequence " + fileName);
                    throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName); // Nếu có thì sẽ báo lỗi
                }
                Path targetLocation = this.rootPath1.resolve(fileName); // Tạo đường dẫn tới nơi lưu trữ file (trong static của src)
                Path targetLocation2 = this.rootPath2.resolve(fileName); //Tạo đường dẫn tới nơi lưu trữ file (trong target ngoài src)
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING); // Tạo file trong src
                Files.copy(file.getInputStream(), targetLocation2, StandardCopyOption.REPLACE_EXISTING); // Tạo file trong target

            } else {
                logger.error("Could not upload file");
                throw new Exception("Could not upload file");
            }
            return fileName;
        }catch (Exception e){
            logger.error("Could not store file " + fileName + ". Please try again!");
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", e); // Nếu không lưu được file thì sẽ báo lỗi
        }
    }

    @Override
    public boolean deleteFile(String fileName) throws Exception {
        String[] pathFile = fileName.split("/");
        init(pathFile[0]);
        Path filePath1 = rootPath1.resolve(pathFile[1]);
        Path filePath2 = rootPath2.resolve(pathFile[1]);
        try{
            if(Files.exists(filePath1)) {
                Files.delete(filePath1);
            }
            if(Files.exists(filePath2)) {
                Files.delete(filePath2);
            }
            return true;
        }catch (Exception e){
            logger.error("Could not delete file " + fileName + ". Please try again!");
            throw new RuntimeException("Could not delete file " + fileName + ". Please try again!", e); // Nếu không lưu được file thì sẽ báo lỗi
        }
    }
}

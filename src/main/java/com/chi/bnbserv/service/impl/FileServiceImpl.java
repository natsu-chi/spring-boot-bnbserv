package com.chi.bnbserv.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chi.bnbserv.exception.FileNotFoundException;
import com.chi.bnbserv.exception.InvalidInputException;
import com.chi.bnbserv.service.FileService;
import com.chi.bnbserv.util.FileUtil;


@Service
public class FileServiceImpl implements FileService {

    // 讀取設定檔
    @Value("${upload-dir.about}")
    private String aboutDir;
    @Value("${upload-dir.dataset}")
    private String datasetDir;

    /**
     * 根據指定名稱、類型，下載對應檔案 (資料集使用)
     * 
     * @param filePath
     * @param fileType
     *  resource: static/ 目錄檔案
     *  url: 取得線上檔案
     *  local: 下載 server 本地檔案
     * @return byte[]
     * @throws Exception
     */
    @Override
    public byte[] download(String filePath, String fileType) throws Exception {
        if(filePath.isEmpty() || filePath.trim().isBlank()) throw new FileNotFoundException("檔案名稱空白");

        // 根據名稱下載對應檔案
        switch (fileType) {
            case "resource":
                byte[] resourceBytes = FileUtil.getByteFromFileNIO(datasetDir + filePath);

                if(resourceBytes != null && resourceBytes.length > 0) {
                    return resourceBytes;
                } 
                break;
        
            default:
                throw new FileNotFoundException(String.format("指定檔案類型不存在: '%s'", fileType));
        }
        return null;
    }
    @Override
    public String handleUpload(MultipartFile file, String targetLocation) {
        String errorMsg = "";
        Path uploadPath = Paths.get(targetLocation).toAbsolutePath().normalize();

        try {
            if(file.isEmpty()) {
                throw new InvalidInputException("file", "inputFile", "沒有檔案");
            }

            if(!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 生成唯一的檔名並附加副檔名
            String fileExtension = getFileExtensionFromContentType(file.getContentType());
            String newFileName = createFileName() + fileExtension;
            Path filePath = uploadPath.resolve(newFileName);

            file.transferTo(filePath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("無法儲存檔案: " + file.getOriginalFilename(), e);
        }
        
        return errorMsg;
    }
    @Override
    public String createFileName() {
        return Long.toString(System.currentTimeMillis() * 10 + new Random().nextInt(10)+1);
    }
    @Override
    public String getFileExtensionFromContentType(String contentType) {
        if (contentType == null) return "";

        switch (contentType) {
            case "image/jpeg": return ".jpg";
            case "image/png": return ".png";
            case "image/gif": return ".gif";
            default: return "";
        }
    }
}

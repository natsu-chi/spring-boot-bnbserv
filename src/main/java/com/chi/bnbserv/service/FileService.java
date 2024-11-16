package com.chi.bnbserv.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     * 下載檔案 (資料集使用)
     * @param String fileName
     * @param String fileType
     * @return byte[]
     * @throws Exception
     */
    byte[] download(String fileName, String fileType) throws Exception;

    /**
     * 處理檔案上傳
     * @param MultipartFile 檔案
     * @param String 儲存路徑
     * @return 檔案名稱
     */
    public String handleUpload(MultipartFile file, String targetLocation);

    /**
     * 生成唯一的檔名（時間戳 + 隨機數字）
     * @param 
     * @return 檔案名稱
     */
    public String createFileName();

    /**
     * 檔案 MIME 類型對應的副檔名
     * @param contentType
     * @return
     */
    public String getFileExtensionFromContentType(String contentType);
}

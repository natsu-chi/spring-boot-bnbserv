package com.chi.bnbserv.service;

public interface FileService {
    /**
     * 下載檔案
     * @param String fileName
     * @return
     */
    byte[] download(String fileName, String fileType) throws Exception;
}

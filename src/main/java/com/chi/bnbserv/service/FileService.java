package com.chi.bnbserv.service;

public interface FileService {
    /**
     * 下載檔案
     * @param String fileName
     * @param String fileType
     * @return byte[]
     * @throws Exception
     */
    byte[] download(String fileName, String fileType) throws Exception;
}

package com.chi.bnbserv.service;

import java.io.File;
import java.io.IOException;

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

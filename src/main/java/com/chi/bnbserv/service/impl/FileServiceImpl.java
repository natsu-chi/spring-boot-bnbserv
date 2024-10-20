package com.chi.bnbserv.service.impl;

import org.springframework.stereotype.Service;

import com.chi.bnbserv.exception.FileNotFoundException;
import com.chi.bnbserv.service.FileService;
import com.chi.bnbserv.util.FileUtil;

@Service
public class FileServiceImpl implements FileService {

    private static final String DIR = "src/main/resources/static/dataset/";

    /**
     * 根據指定名稱、類型，下載對應檔案
     * 
     * @param fileName
     * @param fileType
     *  resource: static/ 目錄檔案
     *  url: 取得線上檔案
     *  local: 下載 server 本地檔案
     * @return byte[]
     * @throws Exception
     */
    @Override
    public byte[] download(String fileName, String fileType) throws Exception {
        if(fileName.isEmpty() || fileName.trim().isBlank()) throw new FileNotFoundException("檔案名稱空白");

        // 根據名稱下載對應檔案
        switch (fileType) {
            case "resource":
                byte[] resourceBytes = FileUtil.getByteFromFileNIO(DIR + fileName);
                if(resourceBytes != null && resourceBytes.length > 0) {
                    return resourceBytes;
                } 
                break;
        
            default:
                throw new FileNotFoundException(String.format("指定檔案類型不存在: '%s'", fileType));
        }
        return null;
    }
}

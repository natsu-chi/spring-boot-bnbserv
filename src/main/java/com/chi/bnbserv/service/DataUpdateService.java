package com.chi.bnbserv.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.chi.bnbserv.entity.CsvBean;

public interface DataUpdateService {
    /**
     * 下載指定城市的 csv 資料
     * @param String tableName
     * @param Object data
     * @return boolean
     */
    String fetchCsvDataByCity(String city);

    /**
     * 下載指定城市的 gz 資料
     * @param String tableName
     * @param Object data
     * @return boolean
     */
    String fetchGzDataByCity(String city);

    /**
     * 解壓縮 Gzip 檔案
     * @param String savedFilePath 存放路徑
     * @return boolean
     */
    public boolean decompressGz(String savedFilePath) throws IOException;
}

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

    /**
     * 讀取 csv，並 map 為指定的 bean (陣列形式回傳，預設限制回傳數量)
     * @param String path 檔案名稱
     * @return List<T>
     */
    List<? extends CsvBean> mapFileToListingBean(Path filePath);

    /**
     * 讀取 csv，並 map 為指定的 bean (陣列形式回傳)
     * @param String path 檔案名稱
     * @param Integer maxSize 限制回傳數量
     * @return List<T>
     */
    List<? extends CsvBean> mapFileToListingBean(Path filePath, Integer maxSize);
}

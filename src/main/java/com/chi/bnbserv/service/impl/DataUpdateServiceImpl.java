package com.chi.bnbserv.service.impl;

import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.chi.bnbserv.entity.ListingColumnBean;
import com.chi.bnbserv.exception.FileNotFoundException;
import com.chi.bnbserv.service.DataUpdateService;
import com.chi.bnbserv.util.FileUtil;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

@Service
public class DataUpdateServiceImpl implements DataUpdateService {
    private String targetUrl = "https://insideairbnb.com/get-the-data/"; // 下載檔案的 domain 前綴
    private String savedRootPath = "src/main/resources/static/dataset";

    /**
     * 下載指定城市的 csv 資料
     * 
     * @param String city
     * @return savedDir
     */
    @Override
    public String fetchCsvDataByCity(String city) {
        String savedDir; // 詳細存放路徑
        String replaceString = "src/main/resources/static/dataset/csv/";

        // 處理 city 字串: 確保轉小寫
        String cityLowerCase = city.toLowerCase();

        // 讀取 url，將要下載的網址存成陣列
        List<String> urlsToDownload = getUrlsByCity(cityLowerCase);

        // 下載 csv & geojson 檔
        savedDir = downloadCsvAndGeojson(urlsToDownload);
        return savedDir.replace(replaceString, "");
    }

    /**
     * 下載指定城市的 gz 資料
     * 
     * @param String city
     * @return savedDir
     */
    @Override
    public String fetchGzDataByCity(String city) {
        String savedDir; // 詳細存放路徑
        String replaceString = "src/main/resources/static/dataset/gz/";

        // 處理 city 字串: 確保轉小寫
        String cityLowerCase = city.toLowerCase();

        // 讀取 url，將要下載的網址存成陣列
        List<String> urlsToDownload = getUrlsByCity(cityLowerCase);

        // 下載 csv & geojson 檔
        savedDir = downloadGz(urlsToDownload);

        // Gz 需另外解壓縮為 csv 檔
        try {
            decompressGz(savedDir + "/listings.csv.gz");
        } catch (IOException e) {
            
        }
        return savedDir.replace(replaceString, "");
    }

    /**
     * 爬取網站，過濾 city 名稱取得網址
     * 
     * @param String city
     * @return urlList
     */
    private List<String> getUrlsByCity(String city) {
        List<String> urlList = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(targetUrl).get();
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String fileUrl = link.absUrl("href");
                if (fileUrl.contains(city)) {
                    urlList.add(fileUrl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlList;
    }

    /**
     * 下載 url 資源 (csv 格式)
     * 
     * @param List<String> urlList 爬取網站取得的 url 陣列
     * @return savedDir
     */
    private String downloadCsvAndGeojson(List<String> urlList) {
        String savedDir = savedRootPath + "/csv";
        String savedFilePath = "";
        String file = "";
        ListIterator<String> it = urlList.listIterator();
        try {
            System.out.println("====== Executing: Download CSV and GEOJSON ======");
            while (it.hasNext()) {
                file = it.next();
                if (file.endsWith(".csv") || file.endsWith(".geojson")) {
                    System.out.println("Downloading: " + file);
                    // 下載檔案
                    savedFilePath = download(file, savedDir);
                    System.out.println("Downloaded: " + savedFilePath);
                }
            }
            System.out.println("====== Executed : Download CSV and GEOJSON ======");
        } catch (Exception e) {
            e.printStackTrace();
        }

        savedDir = savedFilePath.substring(0, savedFilePath.lastIndexOf("/"));
        return savedDir;
    }

    /**
     * 下載 url 資源 (gzip 格式)
     * 
     * @param List<String> urlList 爬取網站取得的 url 陣列
     * @return savedDir
     */
    private String downloadGz(List<String> urlList) {
        String savedDir = savedRootPath + "/gz"; // 存放資料夾
        String savedFilePath = ""; // 存放絕對路徑
        String file = ""; // 指定下載檔案位址
        ListIterator<String> it = urlList.listIterator();
        try {
            while (it.hasNext()) {
                file = it.next();
                if (file.endsWith(".gz")
                        && file.substring(file.lastIndexOf("/") + 1).equals("listings.csv.gz")) {
                    System.out.println("Downloading: " + file);
                    savedFilePath = download(file, savedDir);
                    System.out.println("Downloaded: " + savedFilePath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        savedDir = savedFilePath.substring(0, savedFilePath.lastIndexOf("/"));
        return savedDir;
    }

    /**
     * 給定下載檔案 url，存取至指定資料夾
     * 
     * @param fileUrl
     * @param dir
     * @return
     * @throws IOException
     */
    private String download(String fileUrl, String dir) throws IOException {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        String savePath = "";
        // 移除不必要的路徑文字
        List<String> replaceStr = List.of(
                "https://data.insideairbnb.com", // baseUrl
                "visualisations/", // 中間不需要的目錄
                "data/", // 中間不需要的目錄
                fileName);
        String layerPath = fileUrl;
        ListIterator<String> it = replaceStr.listIterator();
        String outputPath = "";

        // 處理中間層級路徑
        // 輸入路徑範例:
        // https://data.insideairbnb.com/united-states/tx/austin/2024-09-13/data/listings.csv.gz
        // 輸出路徑範例:
        // src/main/resources/static/dataset/gz/united-states/tx/austin/2024-06-26/reviews.csv.gz
        while (it.hasNext()) {
            layerPath = layerPath.replaceAll(it.next(), "");
        }
        outputPath = dir + layerPath + fileName;

        // 判斷有無目錄，沒有就新增
        Path uploadPath = Paths.get(dir + layerPath).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }

        try (InputStream input = new URL(fileUrl).openStream();
                FileOutputStream fos = new FileOutputStream(outputPath)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.close();
            savePath = dir + layerPath + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return savePath;
    }

    /**
     * 解壓縮 Gzip
     * 
     * @param String filePathString 檔案路徑
     * @return boolean
     */
    public boolean decompressGz(String filePathString) throws IOException {
        System.out.println("===== 解壓縮開始 =====");
        System.out.println("Decompressing: " + filePathString);
        FileUtil.decompressGzip(new File(filePathString), new File(filePathString.replace(".gz", "")));
        System.out.println("===== 解壓縮完成 =====");
        return true;
    }

    /**
     * 讀取 csv，並 map 為指定的 bean (陣列形式回傳，預設限制回傳數量)
     * @param String path 檔案名稱
     * @return List<T>
     */
    @Override
    public List<ListingColumnBean> mapFileToListingBean(Path filePath) {
        return mapFileToListingBean(filePath, 100);
    }

    /**
     * 讀取 csv，並 map 為指定的 bean (陣列形式回傳)
     * @param String path 檔案名稱
     * @param Integer maxSize 限制回傳數量
     * @return List<T>
     */
    @Override
    public List<ListingColumnBean> mapFileToListingBean(Path filePath, Integer maxSize) {
        try {
            if (!Files.exists(filePath)) throw new FileNotFoundException(String.format("指定檔案不存在: '%s'", filePath));

            // 讀取 csv 檔案，並 map 成 bean
            Reader reader = Files.newBufferedReader(filePath);
            List<ListingColumnBean> beans = new CsvToBeanBuilder<ListingColumnBean>(reader)
                                    .withType(ListingColumnBean.class)
                                    .withSeparator(',')
                                    .withQuoteChar('"')
                                    .withEscapeChar('\\')
                                    .withSkipLines(0)
                                    .withFieldAsNull(CSVReaderNullFieldIndicator.BOTH)
                                    .build()
                                    .parse();
            if (maxSize != null) {
                return beans.subList(0, Math.min(maxSize, beans.size()));
            } else {
                return beans;
            }
        } catch (Exception e) {
            // e.printStackTrace();
            throw new RuntimeException("csv 檔案讀取失敗: " + e.getMessage());
        }
    }
}

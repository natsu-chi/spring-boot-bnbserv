package com.chi.bnbserv.controller.api;

import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chi.bnbserv.dto.RequestListingDto;
import com.chi.bnbserv.dto.ResponseDto;
import com.chi.bnbserv.entity.ConfigDataPath;
import com.chi.bnbserv.entity.ListingColumnBean;
import com.chi.bnbserv.exception.FileNotFoundException;
import com.chi.bnbserv.exception.ResourceNotFoundException;
import com.chi.bnbserv.repository.ConfigDataPathRepo;
import com.chi.bnbserv.repository.ListingCsvBeanRepo;
import com.chi.bnbserv.service.impl.DataUpdateServiceImpl;
import com.chi.bnbserv.service.impl.FileServiceImpl;

import io.micrometer.common.util.StringUtils;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(path = "/api", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
public class DataApiController {
    @Autowired
    ConfigDataPathRepo configDataPathRepo;
    @Autowired
    ListingCsvBeanRepo listingCsvBeanRepo;

    DataUpdateServiceImpl dataUpdateService = new DataUpdateServiceImpl();

    @GetMapping("/data/listing/download/list")
    public ResponseEntity<Object> downloadListingData(@RequestParam String city, @RequestParam String dataType) {
        FileServiceImpl fsi = new FileServiceImpl();
        String path = null;
        String fileName = null;
        String version = null;
        if (dataType.equals("opt1")) {
            version = "basic";
        } else if (dataType.equals("opt2")) {
            version = "advanced";
        } else {
            throw new IllegalArgumentException("無效的 dataType 輸入值");
        }

        // 查詢城市對應檔案路徑
        if (!city.isEmpty()) {
            // 以 city_id 查詢
            ConfigDataPath configDataPath = configDataPathRepo.findByCity_Id(Integer.valueOf(city))
                    .orElseThrow(() -> new ResourceNotFoundException("data_path <- city", "city_id", city));
            path = configDataPath.getPath();
        }

        try {
            if (!path.isEmpty()) {
                switch (version) {
                    case "basic":
                        path = "csv/" + path;
                        break;
                    case "advanced":
                        path = "gz/" + path;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid version: " + version);
                }
                System.out.println("path: " + path);
                String[] part = path.split("/");
                fileName = URLEncoder.encode(part[part.length - 3], StandardCharsets.UTF_8.toString()) + "_" + URLEncoder.encode(part[part.length - 1], StandardCharsets.UTF_8.toString());
                byte[] fileByteArray = fsi.download(path + "/listings.csv", "resource");
                
                if (fileByteArray == null) throw new FileNotFoundException("無指定檔案");
                
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDisposition(ContentDisposition
                        .attachment()
                        .filename(fileName + ".csv")
                        .build());
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .headers(headers)
                        .body(fileByteArray);
            }
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("404", "查無相關檔案", null));
        } catch (Exception e) {
            // e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto("400", "下載失敗", null));
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto("403", "請求無效，請聯繫管理員", null));
    }

    @PostMapping("/data/listing/fetch")
    public ResponseEntity<ResponseDto> fetchListingData(@Validated @RequestBody RequestListingDto requestDto) {
        String savedDir = "";
        String city = requestDto.getCity();
        String dataType = requestDto.getDataType();
        String fileExtension;

        switch (dataType) {
            case "opt1":
                fileExtension = "csv";
                break;
            case "opt2":
                fileExtension = "gz";
                break;
            default:
                throw new IllegalArgumentException("無效的 dataType 輸入值");
        }

        if (fileExtension != null && fileExtension.equals("csv")) {
            savedDir = dataUpdateService.fetchCsvDataByCity(city);
        } else if (fileExtension != null && fileExtension.equals("gz")) {
            savedDir = dataUpdateService.fetchGzDataByCity(city);
        }

        // 更新存放路徑資料表
        if (savedDir != null && !savedDir.isEmpty()) {
            ConfigDataPath cdp = configDataPathRepo.findByCity_NameIgnoreCase(city)
                    .orElseThrow(() -> new ResourceNotFoundException("dataset_path -> city", "name", city));
            cdp.setPath(savedDir);
            ConfigDataPath updatedCdp = configDataPathRepo.save(cdp);

            if (updatedCdp != null) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new ResponseDto("200", "資料更新成功", null));
            }

            // 下載成功，但資料表更新失敗
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ResponseDto("403", "路徑更新授權失敗", null));
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto("400", "資料下載失敗", null));
    }

    @PostMapping("/data/listing/update")
    public ResponseEntity<ResponseDto> updateFetchListingData(@RequestBody RequestListingDto requestDto) {
        String city = requestDto.getCity();

        int counter = 1;
        String rootPath = "static/dataset/gz";
        String file = "listings.csv";

        List<ListingColumnBean> errorBeans = new ArrayList<ListingColumnBean>();
        System.out.println(requestDto.getCity());

        try {
            if(city.isEmpty() || city.isBlank()) {
                throw new IllegalArgumentException("無效的查詢變數: " + requestDto.getCity());
            }

            ConfigDataPath cdp = configDataPathRepo.findByCity_NameIgnoreCase(city)
                    .orElseThrow(() -> new ResourceNotFoundException("dataset_path -> city", "name", city));
            if (StringUtils.isNotEmpty(cdp.getPath())) {
                Path filePath;
                filePath = Paths.get(
                        ClassLoader.getSystemResource(rootPath + "/" + cdp.getPath() + "/" + file).toURI());
                List<ListingColumnBean> cdpList = dataUpdateService.mapFileToListingBean(filePath, null);
                for (ListingColumnBean bean : cdpList) {
                    System.out.println("row: " + counter);
                    try {
                        bean.setCity_id(cdp.getCityId().toString());
                        listingCsvBeanRepo.save(bean);
                    } catch (Exception e) {
                        System.out.println("ERROR ROW: " + counter);
                        errorBeans.add(bean);
                    }
                    counter++;
                }
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto("200", 
                                          String.format("處理 %s 筆：成功 %s 筆，失敗 %s 筆", counter, (counter-errorBeans.size()), errorBeans.size()), 
                                          cdp.getCity()));
        } catch (URISyntaxException e) {
            // e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto("500", "資料更新失敗", null));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto("400", e.getMessage(), null));
        }
    }
}

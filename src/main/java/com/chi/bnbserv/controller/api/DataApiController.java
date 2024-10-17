package com.chi.bnbserv.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chi.bnbserv.dto.ResponseDto;
import com.chi.bnbserv.entity.ConfigDataPath;
import com.chi.bnbserv.exception.ResourceNotFoundException;
import com.chi.bnbserv.repository.ConfigDataPathRepo;
import com.chi.bnbserv.service.FileServiceImpl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(path = "/api", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
public class DataApiController {
    @Autowired
    ConfigDataPathRepo configDataPathRepo;
    
    @GetMapping("/data/listing/download")
    public ResponseEntity<Object> downloadListingData(@RequestParam String city, @RequestParam String version) {
        FileServiceImpl fsi = new FileServiceImpl();
        String path = null;
        String fileName = null;
        version = (version.isEmpty()) ? "basic" : "advanced";

        // 查詢城市對應檔案路徑
        if (!city.isEmpty()) {
            ConfigDataPath configDataPath = configDataPathRepo.findByCity_NameIgnoreCase(city)
                                                              .orElseThrow(() -> new ResourceNotFoundException("data_path <- city", "name", city));
            path = configDataPath.getPath();
        }

        try {
            if (!path.isEmpty()) {
                switch (version) {
                    case "basic":
                        path = "csv/" + path;
                        break;
                    case "advanced":
                        path = "g-csv/" + path;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid version: " + version);
                }
                String[] part = path.split("/");
                fileName = part[part.length - 3] + "_" + part[part.length -1];
                byte[] fileByteArray = fsi.download(path, "resource");                
                HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition
                    .attachment()
                    .filename(fileName)
                    .build());
            
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(headers)
                    .body(fileByteArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto("400", "下載失敗", null));
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDto("403", "請求無效，請聯繫管理員", null));
    }
    
}

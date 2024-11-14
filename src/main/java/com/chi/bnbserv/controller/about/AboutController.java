package com.chi.bnbserv.controller.about;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.chi.bnbserv.dto.ResponseDto;
import com.chi.bnbserv.entity.AboutItem;
import com.chi.bnbserv.entity.AboutType;
import com.chi.bnbserv.exception.ResourceNotFoundException;
import com.chi.bnbserv.repository.AboutItemRepo;
import com.chi.bnbserv.repository.AboutTypeRepo;


@Controller
@RequestMapping("/setting/about/about")
public class AboutController {
    @Autowired
    private AboutItemRepo aboutItemRepo;
    @Autowired
    private AboutTypeRepo aboutTypeRepo;

    @Value("${upload-dir.about}") // 配置檔案上傳的目錄
    private String uploadDir;
    
    @GetMapping("/list")
    public String list(Model model, @RequestParam(defaultValue = "2") Integer typeId, @RequestParam(defaultValue = "1") Integer page) {
        int pageSize = 20;
        int pageNumber = page - 1;

        // 查詢 about_type
        AboutType aboutType = aboutTypeRepo.findById(typeId).orElseThrow(
            () -> new ResourceNotFoundException("about_type", "id", String.valueOf(typeId))
        );
        
        if (aboutType.getItems() != 'Y') {
            // 非子類別類型內容
            throw new ResourceNotFoundException("about_type", "items", "Y");
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending());
        Page<AboutItem> resultByPage = aboutItemRepo.findByActiveAndTypeId("Y", typeId, pageable);
        
        // 取得資料詳情
        int totalPages = resultByPage.getTotalPages(); // 總頁數
        int currentPage = page; // 目前頁數

        List<AboutItem> aboutItemList = resultByPage.getContent();
        if (aboutItemList.size() == 0) {
            throw new ResourceNotFoundException("about_item", "active", "Y");
        }

        model.addAttribute("list", aboutItemList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("showPageNumber", (currentPage > 3) ? 3 : currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("title", "頁面管理 | 關於我們 - " + aboutType.getTitle());
        model.addAttribute("path", "about/about/list :: list");
        model.addAttribute("pathSrc", "");
        model.addAttribute("withSrc", "N");
        return "main";
    }

    @GetMapping("/edit/{typeId}")
    public String edit(Model model, @PathVariable int typeId, @RequestParam int id) {
        AboutType aboutType = aboutTypeRepo.findById(typeId).orElseThrow(
            () -> new ResourceNotFoundException("about_type", "id", String.valueOf(typeId))
        );
        AboutItem data = aboutItemRepo.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("about_tile", "id", String.valueOf(id))
        );
        model.addAttribute("data", data);

        model.addAttribute("title", "頁面管理 | 關於我們 - " + aboutType.getTitle());
        model.addAttribute("path", "about/about/edit :: edit");
        model.addAttribute("pathSrc", "about/about/edit :: src");
        model.addAttribute("withSrc", "Y");
        return "main";
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseDto> postMethodName(@RequestParam("id") Integer id,
                                 @RequestParam("title") String title,
                                 @RequestParam("content") String content,
                                 @RequestParam(value = "photo", required = false) MultipartFile photo
                                ) {
        // 處理文字資料
        AboutItem aboutItem = aboutItemRepo.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("about_item", "id", String.valueOf(id))
        );
        if (! aboutItem.getActive().equals("Y")) {
            throw new ResourceNotFoundException("about_item", "active", "Y");
        }
        aboutItem.setItemName(title);
        aboutItem.setContent(content);

        // 儲存圖片檔案
        if (photo != null && !photo.isEmpty()) {
            String fileName = StringUtils.cleanPath(photo.getOriginalFilename());
            Path targetLocation = Paths.get(uploadDir).resolve(fileName);
        
            try {
                Files.copy(photo.getInputStream(), targetLocation);
                aboutItem.setPhoto(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity
                        .status(500)
                        .body(new ResponseDto("500", "檔案上傳失敗: ", e.getMessage()));
            }
        }

        aboutItemRepo.save(aboutItem);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto("200", "內容修改成功", null));
    }
}

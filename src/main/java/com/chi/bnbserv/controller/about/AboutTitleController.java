package com.chi.bnbserv.controller.about;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chi.bnbserv.dto.RequestAboutTitleDto;
import com.chi.bnbserv.dto.ResponseDto;
import com.chi.bnbserv.entity.AboutTitle;
import com.chi.bnbserv.exception.ResourceNotFoundException;
import com.chi.bnbserv.repository.AboutTitleRepo;
import com.chi.bnbserv.service.AboutTitleService;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/setting/about/title")
public class AboutTitleController {
    @Autowired
    private AboutTitleRepo aboutTitleRepo;
    @Autowired
    private AboutTitleService aboutTitleService;
    
    @GetMapping("/list")
    public String list(Model model, @RequestParam(defaultValue = "1") Integer page) {
        int pageSize = 20;
        int pageNumber = page - 1;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending());
        Page<AboutTitle> resultByPage = aboutTitleRepo.findByActive("Y", pageable);
        
        // 取得資料詳情
        int totalPages = resultByPage.getTotalPages(); // 總頁數
        int currentPage = page; // 目前頁數

        List<AboutTitle> aboutTitleList = resultByPage.getContent();
        if (aboutTitleList.size() == 0) {
            throw new ResourceNotFoundException("about_title", "active", "Y");
        }

        model.addAttribute("list", aboutTitleList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("showPageNumber", (currentPage > 3) ? 3 : currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("title", "頁面管理 | 關於我們 - 標題設定");
        model.addAttribute("path", "about/title/list :: list");
        model.addAttribute("pathSrc", "");
        model.addAttribute("withSrc", "N");
        return "main";
    }

    @GetMapping("/edit")
    public String edit(Model model, @RequestParam int id) {
        AboutTitle data = aboutTitleRepo.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("about_tile", "id", String.valueOf(id))
        );
        model.addAttribute("data", data);

        model.addAttribute("title", "頁面管理 | 關於我們 - 標題設定");
        model.addAttribute("path", "about/title/edit :: edit");
        model.addAttribute("pathSrc", "about/title/edit :: src");
        model.addAttribute("withSrc", "Y");
        return "main";
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<ResponseDto> update(@RequestBody RequestAboutTitleDto aboutTitleDto) {
        AboutTitle aboutTitle = aboutTitleRepo.findById(aboutTitleDto.getId()).orElseThrow(
            () -> new ResourceNotFoundException("about_title", "id", String.valueOf(aboutTitleDto.getId()))
        );
        System.out.println("aboutTitle: " + aboutTitle.toString());
        if (!aboutTitle.getActive().equals("Y")) {
            throw new ResourceNotFoundException("about_title", "active", "Y");
        }

        // 驗證資料
        String title = aboutTitleDto.getTitle();
        String content = aboutTitleDto.getContent();

        aboutTitle.setTitle(title);
        aboutTitle.setContent(content);
        aboutTitleRepo.save(aboutTitle);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto("200", "內容修改成功", null));
    }
    
    @PostMapping("/delete")
    public String delete(@RequestParam("id[]") List<Integer> idList) {
        aboutTitleService.updateActiveByIds(idList, "");
        return "redirect:/setting/about/title/list";
    }
}

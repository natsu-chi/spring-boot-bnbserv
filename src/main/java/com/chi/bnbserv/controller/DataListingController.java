package com.chi.bnbserv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chi.bnbserv.repository.CityRepo;
import com.chi.bnbserv.repository.ListingRepo;
import com.chi.bnbserv.dto.CityDetailsDto;
import com.chi.bnbserv.entity.Listing;
import com.chi.bnbserv.exception.ResourceNotFoundException;

@Controller
@RequestMapping("/data/listing/")
public class DataListingController {
    @Autowired
    CityRepo cityRepo;
    @Autowired
    ListingRepo listingRepo;


    @GetMapping("download")
    public String download(Model model) {
        // 查詢所有城市 (回傳城市、區域、國家名稱)
        List<CityDetailsDto> cities = cityRepo.fetchActiveCityDetails();
        if (cities.size() == 0) throw new ResourceNotFoundException("city, region, country", "active", "Y");
        
        model.addAttribute("title", "資料管理 | 房屋資料下載");
        model.addAttribute("cities", cities);
        model.addAttribute("path", "listing/download :: download");
        model.addAttribute("pathSrc", "listing/download :: src");
        model.addAttribute("withSrc", "Y");
        return "main";
    }

    @GetMapping("list/query")
    public String queryListingData(Model model) {
        // 查詢所有城市 (回傳城市、區域、國家名稱)
        List<CityDetailsDto> cities = cityRepo.fetchActiveCityDetails();
        if (cities.size() == 0)
            throw new ResourceNotFoundException("city, region, country", "active", "Y");
        model.addAttribute("title", "資料管理 | 房屋資料查詢");
        model.addAttribute("cities", cities);
        model.addAttribute("path", "listing/query :: query");
        model.addAttribute("pathSrc", "listing/query :: src");
        model.addAttribute("withSrc", "Y");
        return "main";
    }

    @GetMapping("list/results")
    public String getListingDataByCityId(Model model, @RequestParam Integer cityId, @RequestParam Integer page) {
        int pageSize = 20;
        int pageNumber = page - 1;

        List<CityDetailsDto> resultCityDetails = cityRepo.fetchActiveCityDetailsById(Integer.valueOf(cityId));
        if (resultCityDetails.size() == 0) {
            throw new ResourceNotFoundException("city, region, country", "active", "Y");
        }

        // 以 city_id 查詢 listing 資料
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending());
        Page<Listing> resultByPage = listingRepo.findByCityId(cityId, pageable);
        // 取得資料詳情
        int totalPages = resultByPage.getTotalPages(); // 總頁數
        int currentPage = page; // 目前頁數

        List<Listing> listingList = resultByPage.getContent();
        if (listingList.size() == 0) {
            throw new ResourceNotFoundException("listing", "id", cityId.toString());
        }
        model.addAttribute("title", "資料管理 | 房屋資料列表");
        model.addAttribute("listingList", listingList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("path", "listing/list :: list");
        model.addAttribute("pathSrc", "listing/list :: src");
        model.addAttribute("withSrc", "Y");
        return "main";
    }

    @GetMapping("list/detail")
    public String getMethodName(Model model, @RequestParam Long id) {
        // 以 id 查詢 listing 資料
        Listing listing = listingRepo.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("listing", "id", id.toString())
        );
        
        model.addAttribute("title", "資料管理 | 房屋資料詳情");
        model.addAttribute("listing", listing);
        model.addAttribute("path", "listing/detail :: detail");
        model.addAttribute("pathSrc", "listing/detail :: src");
        model.addAttribute("withSrc", "Y");
        return "main";
    }

    @GetMapping("fetch")
    public String fetch(Model model) {
        // 查詢所有城市 (回傳城市、區域、國家名稱)
        List<CityDetailsDto> cities = cityRepo.fetchActiveCityDetails();
        if (cities.size() == 0)
            throw new ResourceNotFoundException("city, region, country", "active", "Y");

        model.addAttribute("title", "資料管理 | 房屋資料更新");
        model.addAttribute("cities", cities);
        model.addAttribute("path", "listing/fetch :: fetch");
        model.addAttribute("pathSrc", "listing/fetch :: src");
        model.addAttribute("withSrc", "Y");
        return "main";
    }

}

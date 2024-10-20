package com.chi.bnbserv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chi.bnbserv.repository.CityRepo;
import com.chi.bnbserv.dto.CityDetailsDto;
import com.chi.bnbserv.exception.ResourceNotFoundException;

@Controller
@RequestMapping("/data/listing/")
public class DataListingController {
    @Autowired
    CityRepo cityRepository;

    @GetMapping("download")
    public String download(Model model) {
        // 查詢所有城市 (回傳城市、區域、國家名稱)
        List<CityDetailsDto> cities = cityRepository.fetchActiveCityDetails();
        if (cities.size() == 0) throw new ResourceNotFoundException("city, region, country", "active", "Y");
        
        model.addAttribute("title", "資料管理 | 房屋資料下載");
        model.addAttribute("cities", cities);
        model.addAttribute("path", "listing/download :: download");
        model.addAttribute("pathSrc", "listing/download :: src");
        model.addAttribute("withSrc", "Y");
        return "main";
    }
}

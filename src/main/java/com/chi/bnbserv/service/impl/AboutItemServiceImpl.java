package com.chi.bnbserv.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chi.bnbserv.entity.AboutItem;
import com.chi.bnbserv.repository.AboutItemRepo;
import com.chi.bnbserv.service.AboutItemService;

@Service
public class AboutItemServiceImpl implements AboutItemService {
    @Autowired
    private AboutItemRepo aboutItemRepo;
    
    @Override
    public List<AboutItem> findAllByIds(List<Integer> ids) {
        return aboutItemRepo.findAllById(ids);
    }

    @Override
    @Transactional
    public void updateActiveByIds(List<Integer> ids, String active) {
        List<AboutItem> items = aboutItemRepo.findAllById(ids);
        items.forEach(item -> item.setActive(active));
        aboutItemRepo.saveAll(items);
    }

}

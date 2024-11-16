package com.chi.bnbserv.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chi.bnbserv.entity.AboutTitle;
import com.chi.bnbserv.repository.AboutTitleRepo;
import com.chi.bnbserv.service.AboutTitleService;

@Service
public class AboutTitleServiceImpl implements AboutTitleService {
    @Autowired
    private AboutTitleRepo aboutTitleRepo;

    public List<AboutTitle> findAllByIds(List<Integer> ids) {
        return aboutTitleRepo.findAllById(ids);
    }

    @Override
    @Transactional
    public void updateActiveByIds(List<Integer> ids, String active) {
        List<AboutTitle> titles = aboutTitleRepo.findAllById(ids);
        titles.forEach(title -> title.setActive(active));
        aboutTitleRepo.saveAll(titles);
    }
}

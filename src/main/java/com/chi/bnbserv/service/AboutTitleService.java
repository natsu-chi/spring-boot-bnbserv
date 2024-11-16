package com.chi.bnbserv.service;

import java.util.List;

import com.chi.bnbserv.entity.AboutTitle;

public interface AboutTitleService {
    public List<AboutTitle> findAllByIds(List<Integer> ids);
    public void updateActiveByIds(List<Integer> ids, String active);
}

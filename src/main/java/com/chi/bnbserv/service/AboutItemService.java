package com.chi.bnbserv.service;

import java.util.List;

import com.chi.bnbserv.entity.AboutItem;

public interface AboutItemService {
    public List<AboutItem> findAllByIds(List<Integer> ids);
    public void updateActiveByIds(List<Integer> ids, String active);
}

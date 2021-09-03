package com.kartaca.slcm.api.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableGenerator{
    public static Pageable generatePageable(String sortby, int limit, int offset){
        Pageable firstPageWithTwoElements;
        if (sortby!=null && limit!=0 && offset!=0){
            Sort sort_obj = Sort.by(Sort.Direction.DESC,sortby);
            firstPageWithTwoElements = PageRequest.of(offset/limit, limit,sort_obj);
        }
        else if (limit!=0 && offset!=0){
            firstPageWithTwoElements = PageRequest.of(offset/limit, limit);
        }
        else if (sortby!=null){
        Sort sort_obj = Sort.by(Sort.Direction.DESC, sortby);
            firstPageWithTwoElements = PageRequest.of(offset, Integer.MAX_VALUE, sort_obj);
        }
        else{
            firstPageWithTwoElements = PageRequest.of(offset, Integer.MAX_VALUE);
            }
        return firstPageWithTwoElements;
        
        
    }
}

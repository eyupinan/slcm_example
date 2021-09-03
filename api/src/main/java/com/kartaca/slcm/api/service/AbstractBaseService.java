package com.kartaca.slcm.api.service;

import java.util.List;

import com.kartaca.slcm.api.exception.NotFoundException;
import com.kartaca.slcm.data.repository.postgresql.AbstractBaseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractBaseService<T, ID> {

    @Autowired
    private AbstractBaseRepository<T, ID> abstractBaseRepository;

    public T save(T entity) {
        return (T) abstractBaseRepository.save(entity);
    }

    public Page<T> getAllPaginated(int page, int size) {
        Page<T> resultPage = abstractBaseRepository.findAll(PageRequest.of(page, size));
        if (page > resultPage.getTotalPages() - 1) {
            throw new NotFoundException();
        }
        return resultPage;
    }

    public List<T> findAll() {
        return abstractBaseRepository.findAll();
    }

    public T findById(ID entityId) {
        return abstractBaseRepository.findById(entityId).orElseThrow(() -> new NotFoundException());
    }

    public T update(T entity) {
        return (T) abstractBaseRepository.save(entity);
    }

    public void deleteById(ID entityId) {
        abstractBaseRepository.deleteById(entityId);
    }
}

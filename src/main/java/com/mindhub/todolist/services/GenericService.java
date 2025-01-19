package com.mindhub.todolist.services;

import com.mindhub.todolist.exceptions.NotFoundException;

import java.util.List;

public interface GenericService<E> {

    E findById(Long Id) throws NotFoundException;
    List<E> findAll();
    void deleteById(long id);
    E save(E entity);
}

package com.bachoco.service;

import java.util.List;
import java.util.Optional;

public interface CrudUseCase<T, ID> {

	void save(T entidad);
    void update(T entidad);
    void delete(ID id);
    Optional<T> findById(ID id);
    List<T> findAll();
}

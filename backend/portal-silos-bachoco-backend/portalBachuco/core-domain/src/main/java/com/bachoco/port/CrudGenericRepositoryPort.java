package com.bachoco.port;

import java.util.List;
import java.util.Optional;

public interface CrudGenericRepositoryPort<T,ID> {

	public void save(T model);
	public void update(T model);
	public void delete(ID id);
	public Optional<T> findBydId(Integer id);
	public List<T> findAll();
}

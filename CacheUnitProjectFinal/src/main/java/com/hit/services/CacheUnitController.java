package com.hit.services;

import java.io.IOException;

import com.hit.dm.DataModel;

public class CacheUnitController<T> {
	private  CacheUnitService<T> cacheUnitService;

	public CacheUnitController() {
		cacheUnitService = new CacheUnitService<>();
	}

	public boolean delete(DataModel<T>[] dataModels) throws IOException {
		return cacheUnitService.delete(dataModels);

	}

	public DataModel<T>[] get(DataModel<T>[] dataModels) throws IOException {
		return cacheUnitService.get(dataModels);
	}

	public boolean update(DataModel<T>[] dataModels) throws IOException {
		return cacheUnitService.update(dataModels);

	}

	public String getStatistics() {
		return cacheUnitService.getStatistics();
	}
}

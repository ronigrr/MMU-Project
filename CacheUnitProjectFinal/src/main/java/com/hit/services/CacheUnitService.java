package com.hit.services;

import com.hit.dao.IDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dm.DataModel;
import com.hit.memory.CacheUnit;

public class CacheUnitService<T> {

	private IDao<Long, DataModel<T>> iDao;
	private CacheUnit<T> cacheUnit;
	// statistics
	private int capacitiy = 20;
	private String algoName = new String("LRU");
	private AtomicInteger reqNum = new AtomicInteger(0);
	private AtomicInteger totalDataModelReq = new AtomicInteger(0);
	private AtomicInteger swapCount = new AtomicInteger(0);

	public CacheUnitService() {

		iDao = new DaoFileImpl<>(System.getProperty("user.dir") + "\\src\\main\\resources\\DataSource.txt");
		cacheUnit = new CacheUnit<>(new LRUAlgoCacheImpl<>(capacitiy));
	}

	public boolean delete(DataModel<T>[] dataModels) throws IOException {

		reqNum.incrementAndGet();
		totalDataModelReq.addAndGet(dataModels.length);

		Long[] ids = new Long[dataModels.length];
		int i = 0;

		for (DataModel<T> data : dataModels) {
			ids[i++] = data.getDataModelId();
			try {
				iDao.delete(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		cacheUnit.removeDataModels(ids);

		return true;

	}

	@SuppressWarnings("unchecked")
	public DataModel<T>[] get(DataModel<T>[] dataModels) throws IOException {

		reqNum.incrementAndGet();
		totalDataModelReq.addAndGet(dataModels.length);

		Long[] ids = new Long[dataModels.length];
		int i = 0;
		for (DataModel<T> data : dataModels) {
			ids[i++] = data.getDataModelId();
		}

		DataModel<T>[] dataModelsInCache = cacheUnit.getDataModels(ids);
		DataModel<T> tempData = null;
		DataModel<T>[] emptyChache = new DataModel[dataModels.length];
		i = 0;
		if (dataModelsInCache == null) {
			for (DataModel<T> data : dataModels) {
				tempData = iDao.find(data.getDataModelId());
				if (tempData == null) {
					DataModel<T> dModel = new DataModel<>(data.getDataModelId(), null);
					emptyChache[i++] = dModel;
				} else {
					emptyChache[i++] = tempData;
				}

			}

			cacheUnit.putDataModels(emptyChache);
			return emptyChache;
		} else {

			if (dataModelsInCache.length == dataModels.length) {
				return dataModelsInCache;

			} else {
				ArrayList<DataModel<T>> listDataModelsInCache = new ArrayList<>();
				ArrayList<DataModel<T>> listDataModelsNotInCache = new ArrayList<>();
				ArrayList<DataModel<T>> listDataModelsToReturn = new ArrayList<>();

				for (DataModel<T> data : dataModelsInCache) {
					listDataModelsInCache.add(data);
					listDataModelsToReturn.add(data);

				}
				for (DataModel<T> data : dataModels) {
					if (!listDataModelsInCache.contains(data)) {
						listDataModelsNotInCache.add(data);
						DataModel<T> dataFromFile = iDao.find(data.getDataModelId());
						if (dataFromFile == null) {
							DataModel<T> dModel = new DataModel<>(data.getDataModelId(), null);
							listDataModelsToReturn.add(dModel);
						} else {
							listDataModelsToReturn.add(dataFromFile);
						}

					}
				}
				DataModel<T>[] dataModelsNotInCache = new DataModel[listDataModelsNotInCache.size()];
				dataModelsNotInCache = listDataModelsNotInCache.toArray(dataModelsNotInCache);

				DataModel<T>[] popUpDataModels = cacheUnit.putDataModels(dataModelsNotInCache);
				if (popUpDataModels != null) {
					for (DataModel<T> data : popUpDataModels) {
						iDao.save(data);
						swapCount.incrementAndGet();
					}
				}

				DataModel<T>[] dataModelsToReturn = new DataModel[dataModels.length];
				dataModelsToReturn = listDataModelsToReturn.toArray(dataModelsToReturn);

				return dataModelsToReturn;
			}
		}
	}

	public boolean update(DataModel<T>[] dataModels) throws IOException {

		reqNum.incrementAndGet();
		totalDataModelReq.addAndGet(dataModels.length);

		Long[] ids = new Long[dataModels.length];
		int i = 0;

		for (DataModel<T> data : dataModels) {
			ids[i++] = data.getDataModelId();
		}

		for (DataModel<T> data : dataModels) {
			iDao.save(data);
			swapCount.incrementAndGet();
		}
		cacheUnit.removeDataModels(ids);
		cacheUnit.putDataModels(dataModels);
		return true;

	}

	public String getStatistics() {

		return "Capacitiy = " + capacitiy + "\n" + "Algorithem:" + algoName + "\n" + "Total number of requests:"
				+ reqNum.get() + "\n" + "Total number of DataModels(GET,DELETE,UPDATE requsts):"
				+ totalDataModelReq.get() + "\n" + "Total number of dataModels swaps (from Cache to Disk):"
				+ swapCount.get() + "\n";

	}

}

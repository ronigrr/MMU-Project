package com.hit.memory;

import java.util.ArrayList;

import com.hit.algorithm.IAlgoCache;
import com.hit.dm.DataModel;

public  class CacheUnit<T> {
	private IAlgoCache<Long, DataModel<T>> currentAlgo;

	public CacheUnit(IAlgoCache<Long, DataModel<T>> algo) {
		currentAlgo = algo;
	}

	public DataModel<T>[] getDataModels(Long[] ids) {
		ArrayList<DataModel<T>> returnDataModels = new ArrayList<>();

		for (Long id : ids) {
			if (currentAlgo.getElement(id) != null)
				returnDataModels.add(currentAlgo.getElement(id));
		}

		@SuppressWarnings("unchecked")
		DataModel<T>[] finalReturnDataModelsList = new DataModel[returnDataModels.size()];
		finalReturnDataModelsList = returnDataModels.toArray(finalReturnDataModelsList);
		if (returnDataModels.isEmpty()) {
			return null;

		}
		return finalReturnDataModelsList;
	}

	public DataModel<T>[] putDataModels(DataModel<T>[] datamodels) {

		ArrayList<DataModel<T>> returnDataModels = new ArrayList<>();
		DataModel<T> tempData;

		for (DataModel<T> data : datamodels) {
			tempData = currentAlgo.putElement(data.getDataModelId(), data);
			if (tempData != null) {
				returnDataModels.add(tempData);
			}
		}

		@SuppressWarnings("unchecked")
		DataModel<T>[] finalReturnDataModelsList = new DataModel[returnDataModels.size()];
		finalReturnDataModelsList = returnDataModels.toArray(finalReturnDataModelsList);

		return finalReturnDataModelsList;
	}

	public void removeDataModels(Long[] ids) {
		for (Long id : ids)
			currentAlgo.removeElemnt(id);

	}

}

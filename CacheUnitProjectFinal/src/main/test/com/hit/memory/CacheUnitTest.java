package com.hit.memory;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.DataModel;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class CacheUnitTest {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	final void test() throws IOException {

		IDao<Long, DataModel<String>> testDao = new DaoFileImpl<>("src\\main\\resources\\DataSource.txt");

		IAlgoCache<Long, DataModel<String>> algo = new LRUAlgoCacheImpl<>(3);

		CacheUnit<String> testCacheUnit = new CacheUnit<>(algo);

		// Create a list of data models for test
		
		ArrayList<DataModel<String>> dataModels = new ArrayList<>();
		for (long i = 0; i < 100; ++i) {
			dataModels.add(new DataModel<String>(i, "" + i * 10));
			testDao.save(dataModels.get((int) i));
		}
		// Test for Dao Delete func & find function
		testDao.delete(dataModels.get(8));
		DataModel<String> dataModelFound = testDao.find((long) 8);
		Assert.assertNull(dataModelFound);
		// Cache unit test
		// Create list of data models and put into cache unit
		ArrayList<DataModel<String>> dataModelsForCache = new ArrayList<>();
		dataModelsForCache.add(new DataModel<String>((long) 1, "a"));
		dataModelsForCache.add(new DataModel<String>((long) 2, "b"));
		dataModelsForCache.add(new DataModel<String>((long) 3, "c"));
		dataModelsForCache.add(new DataModel<String>((long) 4, "d"));
		dataModelsForCache.add(new DataModel<String>((long) 5, "e"));
		dataModelsForCache.add(new DataModel<String>((long) 6, "f"));

		// Convert arrayList to primitive array
		DataModel[] putList = new DataModel[dataModelsForCache.size()];
		putList = dataModelsForCache.toArray(putList);

		// Create new array to collect dataModels from "putDataModels" method
		// Test put function
		DataModel<String>[] returnDataModelsList;
		returnDataModelsList = testCacheUnit.putDataModels(putList);

		Assert.assertEquals("a", returnDataModelsList[0].getContent());
		Assert.assertEquals("b", returnDataModelsList[1].getContent());

		// Create List of ids to send to get function
		Long[] idsList = new Long[] { Long.valueOf(1), Long.valueOf(2), Long.valueOf(3), Long.valueOf(4),
				Long.valueOf(5), Long.valueOf(6) };
		ArrayList<Long> arrayListId = new ArrayList<>();
		Collections.addAll(arrayListId, idsList);
		ArrayList<DataModel<String>> getReturnDataModelArray = new ArrayList<>();
		getReturnDataModelArray.addAll(Arrays.asList(testCacheUnit.getDataModels(idsList)));

		// Testing "getDataModel" method
		long temp;
		for (DataModel<String> dataModel : getReturnDataModelArray) {
			temp = dataModel.getDataModelId();

			Assert.assertTrue(arrayListId.contains(temp));
		}

		// Testing "removeDataModels" method
		testCacheUnit.removeDataModels(idsList);
		// Assert.assertNull(testCacheUnit.getDataModels(idsList));
	}
}
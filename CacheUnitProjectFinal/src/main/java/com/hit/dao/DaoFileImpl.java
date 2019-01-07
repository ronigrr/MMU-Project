package com.hit.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import com.hit.dm.DataModel;

public class DaoFileImpl<T> implements IDao<Long, DataModel<T>> {

	private String filePath;
	private HashMap<Long, DataModel<T>> dataModelMap;

	public DaoFileImpl(String filePath) {
		this.filePath = filePath;
		dataModelMap = new HashMap<Long, DataModel<T>>(1000);
		try {
			first_load();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	public DaoFileImpl(String filePath, int capacity) {
		this.filePath = filePath;
		dataModelMap = new HashMap<Long, DataModel<T>>(capacity);
		try {
			first_load();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(DataModel<T> entity) throws IOException {
		dataModelMap.remove(entity.getDataModelId());
		this.Update_file();
	}

	@Override
	public DataModel<T> find(Long id) throws IOException {
		DataModel<T> dataModel = dataModelMap.get(id);
		return dataModel;
	}

	@Override
	public void save(DataModel<T> entity) throws IOException {
		if (dataModelMap.containsKey(entity.getDataModelId())) {
			dataModelMap.remove(entity.getDataModelId());
		}
		dataModelMap.put(entity.getDataModelId(), entity);
		this.Update_file();

	}

	private void Update_file() throws IOException {
		// this method takes the dataModelMap after a change has been made and updates
		// the file.
		File mFile = new File(filePath);
		FileOutputStream fileOutputStream = new FileOutputStream(mFile);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(dataModelMap);
		objectOutputStream.flush();
		objectOutputStream.close();
		fileOutputStream.close();

	}

	@SuppressWarnings("unchecked")
	private void first_load() throws ClassNotFoundException, IOException {
		File mFile = new File(filePath);
		FileInputStream fileinputStream = new FileInputStream(mFile);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileinputStream);
		dataModelMap = (HashMap<Long, DataModel<T>>) objectInputStream.readObject();
		objectInputStream.close();
		fileinputStream.close();
	}
}

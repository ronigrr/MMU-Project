package com.hit.dm;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DataModel<T> implements Serializable {
	private Long id;
	private T content;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public DataModel(Long id, T content) {
		this.id = id;
		this.content = content;
	}

	public T getContent() {
		return this.content;
	}

	public void setContent(T content) {
		this.content = content;
	}

	public void setDataModelId(Long id) {
		this.id = id;
	}

	public Long getDataModelId() {
		return this.id;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DataModel))
			return false;
		DataModel<?> other = (DataModel<?>) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "id:" + id + " content:" + content +"\n";
	}
}
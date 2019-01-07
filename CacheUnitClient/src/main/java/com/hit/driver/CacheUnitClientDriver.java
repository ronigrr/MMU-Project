package com.hit.driver;

import com.hit.client.CacheUnitClientObserver;
import com.hit.view.CacheUnitView;

public class CacheUnitClientDriver {

	public static void main(String[] args) {

		CacheUnitView view = new CacheUnitView();
		CacheUnitClientObserver cacheUnitClientObserver = new CacheUnitClientObserver(view);
		view.addPropertyChangeListener(cacheUnitClientObserver);
		view.start();

	}

}

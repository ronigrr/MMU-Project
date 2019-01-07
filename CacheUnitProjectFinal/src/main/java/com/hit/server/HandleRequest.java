package com.hit.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.DataModel;
import com.hit.services.CacheUnitController;

public class HandleRequest<T> implements Runnable {

	private Socket socket;
	private CacheUnitController<T> controller;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;

	public HandleRequest(Socket s, CacheUnitController<T> controller) throws IOException {
		this.controller = controller;
		socket = s;
		reader = new ObjectInputStream(socket.getInputStream());
		writer = new ObjectOutputStream(socket.getOutputStream());
	}

	@Override
	public void run() {
		try {
			writer.writeObject("hello client\n");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Type ref = new TypeToken<Request<DataModel<T>[]>>() {
		}.getType();

		String req = null;
		try {
			req = new String((String) reader.readObject());
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}
		if (req.equalsIgnoreCase("s")) {
			String statistics = controller.getStatistics();
			try {
				writer.writeObject(statistics);
				writer.flush();
				writer.writeObject("-1");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(req);
			Gson json = new Gson();
			Request<DataModel<T>[]> request = json.fromJson(req, ref);

			Map<String, String> headers = request.getHeaders();
			DataModel<T>[] body = request.getBody();

			String action = headers.get("action");

			if (action.equalsIgnoreCase("UPDATE")) {
				try {
					controller.update(body);
					writer.writeObject("Update was a success\n");
					writer.flush();
					writer.writeObject("-1");
					writer.flush();
				} catch (IOException e) {

					e.printStackTrace();
				}

			} else if (action.equalsIgnoreCase("GET")) {
				try {
					DataModel<T>[] dataModels = controller.get(body);
					writer.writeObject("Get was a success\n");
					writer.flush();
					for (DataModel<T> data : dataModels) {
						if (data != null) {
							writer.writeObject(data.toString());
							writer.flush();
						}
					}
					writer.writeObject("-1");
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					controller.delete(body);
					writer.writeObject("Delete was a success\n");
					writer.flush();
					writer.writeObject("-1");
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

//		try {
//			reader.close();
//			writer.close();
//			socket.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}

package utils;

import java.util.ArrayList;

import com.google.gson.Gson;

import spark.ResponseTransformer;

public class JsonUtil {

	public static String toJson(Object object) {

		// Checker om input er en streng eller boolean, og hvis det er bliver
		// det pakket ind i en klasse
		// så det kan konverteres til Json
		if (object instanceof String) {
			NestedStringConverter key = new NestedStringConverter((String) object);
			return new Gson().toJson(key);
		} else if (object instanceof Boolean) {
			NestedBooleanConverter key = new NestedBooleanConverter((Boolean) object);
			return new Gson().toJson(key);
		} else if (object instanceof ArrayList<?>) {
			NestedListConverter key = new NestedListConverter((ArrayList<String>) object);
			return new Gson().toJson(key);
		}
		return new Gson().toJson(object);
	}

	public static ResponseTransformer json() {
		return JsonUtil::toJson;
	}

}

class NestedStringConverter {
	String key;

	NestedStringConverter(String key) {
		this.key = key;
	}
}

class NestedBooleanConverter {
	Boolean key;

	NestedBooleanConverter(Boolean key) {
		this.key = key;
	}
}

class NestedListConverter {
	ArrayList<String> key;

	NestedListConverter(ArrayList<String> key) {
		this.key = key;
	}
}

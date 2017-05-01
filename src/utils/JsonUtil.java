package utils;

import com.google.gson.Gson;

import spark.ResponseTransformer;

public class JsonUtil {
	 
	  public static String toJson(Object object) {	
		  if(object instanceof String){
			 NestedUser navn=new NestedUser((String)object);
			 return new Gson().toJson(navn);
		  }
	    return new Gson().toJson(object);
	  }
	 
	  public static ResponseTransformer json() {
	    return JsonUtil::toJson;
	  }
	  
	}
class NestedUser {
    String key;
    NestedUser(String key){
    	this.key=key;
    }
}
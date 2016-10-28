package slh.capture.common;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import slh.capture.domain.fun.FunChannel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonParserUtils {

  public static List<?> parseFromJsonForList(String json, Type type) {
    Gson gson = new Gson();
    List<?> list = gson.fromJson(json, type);
    return list;
  }

  public static Object parseFromJsonForObject(String json, Class<?> clazz) {
    Gson gson = new Gson();
    Object obj = gson.fromJson(json, clazz);
    return obj;
  }

  public static void main(String[] args) {
    Gson gson = new Gson();
    FunChannel fc = new FunChannel();
    fc.setChannelId("aaa");
    fc.setSubChannelId("1");
    List<FunChannel> fcList = new ArrayList<FunChannel>();
    fcList.add(fc);
    String json = gson.toJson(fcList);
    System.out.println("json:" + json);

    Type type = new TypeToken<List<FunChannel>>() {
    }.getType();
    System.out.println(JsonParserUtils.parseFromJsonForList(json, type));
  }
}

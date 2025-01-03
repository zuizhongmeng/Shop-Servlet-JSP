package com.shop.utils;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.net.URLDecoder;

import com.alibaba.fastjson.JSONObject;

import jakarta.servlet.http.HttpServletRequest;

public class RequestBodyParser {

    public static <T> T parseJsonFromRequestBody(HttpServletRequest request, Class<T> clazz) throws Exception {
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
	       while ((line = reader.readLine()) != null) {
	           jsonBuffer.append(line);
	       }
        String jsonString = jsonBuffer.toString();
        // 将查询字符串格式转换为JSON对象
        JSONObject jsonObject = new JSONObject();
        String[] params = jsonString.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            // 确保是有效的键值对
            if (keyValue.length == 2) {
                jsonObject.put(URLDecoder.decode(keyValue[0], "UTF-8"), URLDecoder.decode(keyValue[1], "UTF-8"));

            }
        }

        // 创建泛型对象并填充字段
        T object = clazz.newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true); // 允许访问私有字段
          
            String fieldName = field.getName();
            if (jsonObject.containsKey(fieldName)) {
            	 Object value = jsonObject.get(fieldName);
                 setFieldValue(object, field, value);
            }
        }

        return object;
    }
    
    private static void setFieldValue(Object object, Field field, Object value) throws Exception {
        Class<?> fieldType = field.getType();

        if (value == null) {
            field.set(object, null);
            return;
        }

        if (fieldType == String.class) {
            field.set(object, value.toString());
        } else if (fieldType == int.class || fieldType == Integer.class) {
            field.set(object, Integer.parseInt(value.toString()));
        } else if (fieldType == long.class || fieldType == Long.class) {
            field.set(object, Long.parseLong(value.toString()));
        } else if (fieldType == double.class || fieldType == Double.class) {
            field.set(object, Double.parseDouble(value.toString()));
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            field.set(object, Boolean.parseBoolean(value.toString()));
        } else {
            // 对于其他类型，如果需要，可以扩展更多类型转换
            field.set(object, value);
        }
    }
}


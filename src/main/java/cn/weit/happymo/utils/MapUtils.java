package cn.weit.happymo.utils;

import com.google.common.collect.Maps;

import java.lang.reflect.Field;
import java.util.Map;

public class MapUtils {


    public static Map<String, Object> obj2Map(Object object) throws IllegalAccessException {
        Map<String, Object> map = Maps.newHashMap();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            String varName = field.getName();
            boolean accessFlag = field.isAccessible();
            field.setAccessible(true);
            Object obj = field.get(object);
            if (obj != null) {
                map.put(varName, obj);
            }
            field.setAccessible(accessFlag);
        }
        return map;
    }


}

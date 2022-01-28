package com.topideal.system;

import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by weixiaolei on 2018/11/22.
 */
public class JsonUtils {

    /**
     * json转实体，集合映射类型
     * @param key
     * @param className
     * @return
     */
    public static Map<String,Class> getMappingMap(String key,Class className){
        Map<String, Class> map = new HashMap<String,Class>();
        map.put(key,className);
        return map;
    }

    /**
     * json 转实体
     * @param json
     * @param className
     * @return
     */
    public static Object toBean(JSONObject json,Class className){
        return JSONObject.toBean(json, className);
    }

    /**
     * json 转实体
     * @param json
     * @param className  实体类型
     * @param mapping  实体中集合映射关系
     * @return
     */
    public static Object toBean(JSONObject json,Class className,Map mapping){
        return JSONObject.toBean(json, className,mapping);
    }



}

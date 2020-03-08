package com.ceresdata.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * fastjson 格式json工具类
 *
 * @author xielijun
 *
 */
public class FastJSONUtil {

    public static String toJSONString(Object object){
        String str = JSON.toJSONString(object, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteMapNullValue);
        //  把json null 替换成 空字符串
//		str = str.replace(":null", ":\"\"");
        return str;
    }

    public static JSONObject toJSONObject(Object entity){
        JSONObject json = JSONObject.parseObject(FastJSONUtil.toJSONString(entity));
        return json;
    }

    /**
     * 给对象添加参数
     *
     * @param entity
     * @param pname
     * @param pvalue
     * @return
     */
    public static JSONObject addJSONProperty(Object entity, String pname, Object pvalue){
        JSONObject json = JSONObject.parseObject(FastJSONUtil.toJSONString(entity));
        json.put(pname, pvalue);
        return json;
    }

    public static void main(String[] args) {
        System.out.println(888);
    }
}


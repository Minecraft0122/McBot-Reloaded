package cn.evolvefield.onebot.sdk.util.json;

import cn.evolvefield.onebot.sdk.event.message.MessageEvent;
import cn.evolvefield.onebot.sdk.util.BotUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/1 16:36
 * Version: 1.0
 */
public class GsonUtil {
    //线程安全的
    private static final Gson GSON;
    private static final Gson GSON_NULL; // 不过滤空值
    static {
        GSON = new GsonBuilder().enableComplexMapKeySerialization() //当Map的key为复杂对象时,需要开启该方法
//                .serializeNulls() //当字段值为空或null时，依然对该字段进行转换
//                .excludeFieldsWithoutExposeAnnotation()//打开Export注解，但打开了这个注解,副作用，要转换和不转换都要加注解
                .setDateFormat("yyyy-MM-dd HH:mm:ss")//序列化日期格式  "yyyy-MM-dd"
                .setPrettyPrinting() //自动格式化换行
                .disableHtmlEscaping() //防止特殊字符出现乱码
                .create();
        GSON_NULL = new GsonBuilder().enableComplexMapKeySerialization() //当Map的key为复杂对象时,需要开启该方法
                .serializeNulls() //当字段值为空或null时，依然对该字段进行转换
//                .excludeFieldsWithoutExposeAnnotation()//打开Export注解，但打开了这个注解,副作用，要转换和不转换都要加注解
                .setDateFormat("yyyy-MM-dd HH:mm:ss")//序列化日期格式  "yyyy-MM-dd"
                .setPrettyPrinting() //自动格式化换行
                .disableHtmlEscaping() //防止特殊字符出现乱码
                .create();
    }

    //获取gson解析器
    public static Gson getGson() {
        return GSON;
    }

    //获取gson解析器 有空值 解析
    public static Gson getWriteNullGson() {
        return GSON_NULL;
    }


    /**
     * 根据对象返回json  过滤空值字段
     */
    public static String toJsonStringIgnoreNull(Object object) {
        return GSON.toJson(object);
    }

    /**
     * 根据对象返回json  不过滤空值字段
     */
    public static String toJsonString(Object object) {
        return GSON_NULL.toJson(object);
    }


    /**
     * 将字符串转化对象
     *
     * @param json     源字符串
     * @param classOfT 目标对象类型
     * @param <T>
     * @return
     */
    public static <T> T strToJavaBean(String json, Class<T> classOfT) {
        return GSON.fromJson(normalizeMessage(json, classOfT), classOfT);
    }

    /**
     * OneBot 11 允许 message 使用 CQ 字符串或消息段数组。旧版事件模型使用
     * String 字段，因此在反序列化前把数组无损转换为 CQ 字符串。
     */
    private static String normalizeMessage(String json, Class<?> classOfT) {
        if (!MessageEvent.class.isAssignableFrom(classOfT)) return json;

        JsonElement rootElement = new JsonParser().parse(json);
        if (!rootElement.isJsonObject()) return json;
        JsonObject root = rootElement.getAsJsonObject();
        JsonElement message = root.get("message");
        if (message == null || !message.isJsonArray()) return json;

        root.addProperty("message", arrayMessageToCode(message.getAsJsonArray()));
        return root.toString();
    }

    private static String arrayMessageToCode(JsonArray segments) {
        StringBuilder result = new StringBuilder();
        for (JsonElement segmentElement : segments) {
            if (!segmentElement.isJsonObject()) continue;
            JsonObject segment = segmentElement.getAsJsonObject();
            JsonElement typeElement = segment.get("type");
            JsonElement dataElement = segment.get("data");
            if (typeElement == null || dataElement == null || !dataElement.isJsonObject()) continue;

            String type = typeElement.getAsString();
            JsonObject data = dataElement.getAsJsonObject();
            if ("text".equals(type)) {
                JsonElement text = data.get("text");
                if (text != null && !text.isJsonNull()) {
                    result.append(BotUtils.escape2(text.getAsString()));
                }
                continue;
            }

            result.append("[CQ:").append(type);
            for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
                if (entry.getValue() == null || entry.getValue().isJsonNull()) continue;
                result.append(',').append(entry.getKey()).append('=')
                        .append(BotUtils.escape(entry.getValue().getAsString()));
            }
            result.append(']');
        }
        return result.toString();
    }

    /**
     * 将json转化为对应的实体对象
     * new TypeToken<List<T>>() {}.getType()
     * new TypeToken<Map<String, T>>() {}.getType()
     * new TypeToken<List<Map<String, T>>>() {}.getType()
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }

    /**
     * 转成list
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> List<T> strToList(String gsonString, Class<T> cls) {
        return GSON.fromJson(gsonString, new TypeToken<List<T>>() {
        }.getType());
    }

    /**
     * 转成list中有map的
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> strToListMaps(String gsonString) {
        return GSON.fromJson(gsonString, new TypeToken<List<Map<String, String>>>() {
        }.getType());
    }

    /**
     * 转成map
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> strToMaps(String gsonString) {
        return GSON.fromJson(gsonString, new TypeToken<Map<String, T>>() {
        }.getType());
    }

}

package com.cn.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

@Slf4j
public class JacksonUtil {

    public static ObjectMapper objectMapper;
    public static YAMLMapper yamlMapper;

    /**
     * 序列化级别，默认只序列化属性值发生过改变的字段
     */
    private static JsonInclude.Include DEFAULT_PROPERTY_INCLUSION = JsonInclude.Include.NON_NULL;

    /**
     * 是否缩进JSON格式
     */
    private static boolean IS_ENABLE_INDENT_OUTPUT = false;


    static {
        try {
            //初始化
            initMapper();
            //配置序列化级别
            configPropertyInclusion();
            //配置JSON缩进支持
            configIndentOutput();
            //配置普通属性
            configCommon();
            //配置特殊属性
            configSpecial();
        } catch (Exception e) {
            log.error("jackson config error", e);
        }
    }

    private static void initMapper() {
        objectMapper = new ObjectMapper();
        yamlMapper = new YAMLMapper();
    }

    private static void configCommon() {
        config(objectMapper);
        config(yamlMapper);
    }

    private static void configPropertyInclusion() {
        objectMapper.setSerializationInclusion(DEFAULT_PROPERTY_INCLUSION);
        yamlMapper.setSerializationInclusion(DEFAULT_PROPERTY_INCLUSION);
    }

    private static void configIndentOutput() {
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, IS_ENABLE_INDENT_OUTPUT);
        yamlMapper.configure(SerializationFeature.INDENT_OUTPUT, IS_ENABLE_INDENT_OUTPUT);
    }

    private static void configSpecial() {
        //使用系统换行符
        yamlMapper.enable(YAMLGenerator.Feature.USE_PLATFORM_LINE_BREAKS);
        //允许注释
        yamlMapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
        yamlMapper.enable(JsonParser.Feature.ALLOW_YAML_COMMENTS);
    }

    private static void config(ObjectMapper objectMapper) {
        //序列化BigDecimal时之间输出原始数字还是科学计数, 默认false, 即是否以toPlainString()科学计数方式来输出
        objectMapper.disable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        //允许将JSON空字符串强制转换为null对象值
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        //允许单个数值当做数组处理
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        //禁止重复键, 抛出异常
        objectMapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        //禁止使用int代表Enum的order()來反序列化Enum, 抛出异常
        objectMapper.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
        //有属性不能映射的时候不报错
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //使用null表示集合类型字段是时不抛异常
        objectMapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
        //对象为空时不抛异常
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        //允许在JSON中使用c/c++风格注释
        objectMapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
        //允许未知字段
        objectMapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
        //在JSON中允许未引用的字段名
        objectMapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        //时间格式
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //识别单引号
        objectMapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        //识别Java8时间
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * 设置序列化级别
     * NON_NULL：序列化非空的字段
     * NON_EMPTY：序列化非空字符串和非空的字段
     * NON_DEFAULT：序列化属性值发生过改变的字段
     */
    public static void setSerializationInclusion(JsonInclude.Include inclusion) {
        JacksonUtil.DEFAULT_PROPERTY_INCLUSION = inclusion;
        configPropertyInclusion();
    }

    /**
     * 设置是否开启JSON格式美化
     *
     * @param isEnable 为true表示开启, 默认false, 有些场合为了便于排版阅读则需要对输出做缩放排列
     */
    public static void setIndentOutput(boolean isEnable) {
        JacksonUtil.IS_ENABLE_INDENT_OUTPUT = isEnable;
        configIndentOutput();
    }

    /**
     * 远程内容转java对象
     * @param url 远程地址
     * @param clazz java对象类型
     * @return V
     */
    public static <V> V toObject(URL url, Class<V> clazz) {
        try {
            return objectMapper.readValue(url, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 远程内容转集合类型
     * @param url 远程地址
     * @param type 集合类型
     * @return V
     */
    public static <V> V toCollection(URL url, TypeReference<V> type) {
        try {
            return objectMapper.readValue(url, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 输入流转java对象
     * @param inputStream 输入流
     * @param clazz java对象类型
     * @return V
     */
    public static <V> V toObject(InputStream inputStream, Class<V> clazz) {
        try {
            return objectMapper.readValue(inputStream, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 输入流转集合类型
     * @param inputStream 输入流
     * @param type 集合类型
     * @return V
     */
    public static <V> V toCollection(InputStream inputStream, TypeReference<V> type) {
        try {
            return objectMapper.readValue(inputStream, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 文件内容转java对象
     * @param file 文件
     * @param clazz java对象类型
     * @return V
     */
    public static <V> V toObject(File file, Class<V> clazz) {
        try {
            return objectMapper.readValue(file, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 文件内容转集合类型
     * @param file 文件
     * @param type 集合类型
     * @return V
     */
    public static <V> V toCollection(File file, TypeReference<V> type) {
        try {
            return objectMapper.readValue(file, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json转java对象
     * @param json json字符串
     * @param clazz java对象类型
     * @return V
     */
    public static <V> V toObject(String json, Class<V> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json转为集合类型
     *
     * 使用demo：
     * String json1 = "{\"userName\":\"小李飞刀\",\"age\":18,\"addTime\":1591851786568}";
     * String json2 = "[{\"userName\":\"小李飞刀\",\"age\":18,\"addTime\":123}, {\"userName\":\"小李飞刀2\",\"age\":182,\"addTime\":1234}]";
     * 1. Map模式, 必须使用 TypeReference
     * Map<String, Object> userBaseMap =  readValue(json1, new TypeReference<Map<String, Object>>() {});
     * 2. list<Bean>模式，必须用 TypeReference
     * List<UserBase> userBaseList = objectMapper.readValue(json2, new TypeReference<List<UserBase>>() {});
     * 3. Bean[] 数组，必须用 TypeReference
     * UserBase[] userBaseAry = objectMapper.readValue(json2, new TypeReference<UserBase[]>() {});
     *
     * @param json json字符串
     * @param type 集合类型
     * @return V
     */
    public static <V> V toCollection(String json, TypeReference<V> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将Resources目录下的Yaml文件转为java对象
     * @param name 文件名
     * @param clazz 对象类型
     * @return V
     */
    public static <V> V yamlToObject(String name, Class<V> clazz) {
        try (InputStream inputStream = getResourceStream(name); InputStreamReader reader = getResourceReader(inputStream)) {
            if (reader == null) {
                return null;
            }
            return yamlMapper.readValue(reader, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Yaml文件转为java对象
     * @param file 文件
     * @param clazz 对象类型
     * @return V
     */
    public static <V> V yamlToObject(File file, Class<V> clazz) {
        try {
            return yamlMapper.readValue(file, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将Resources目录下的Yaml文件转为集合类型
     * @param name 文件名
     * @param type 集合类型
     * @return V
     */
    public static <V> V yamlToCollection(String name, TypeReference<V> type) {
        try (InputStream inputStream = getResourceStream(name); InputStreamReader reader = getResourceReader(inputStream)) {
            if (reader == null) {
                return null;
            }
            return yamlMapper.readValue(reader, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Yaml文件转为集合类型
     * @param file 文件
     * @param type 集合类型
     * @return V
     */
    public static <V> V yamlToCollection(File file, TypeReference<V> type) {
        try {
            return yamlMapper.readValue(file, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * yaml对象转为字符串
     * @param v yaml对象
     * @return String
     */
    public static <V> String yamlStringify(V v) {
        try {
            return yamlMapper.writeValueAsString(v);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Object转为json字符串
     * @param value java对象
     * @return String
     */
    public static String stringify(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Object转字符串(美化)
     * @param value Java对象
     * @return String
     */
    public static String prettyStringify(Object value) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json字符串转为json对象
     * @param json json字符串
     * @return JsonNode
     */
    public static JsonNode jsonStrToJsonNode(String json) {
        JsonNode node;
        try {
            node = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return node;
    }


    /**
     * 向json中添加属性
     *
     * @return json
     */
    public static <T> String add(String json, String key, T value) {
        try {
            JsonNode node = objectMapper.readTree(json);
            add(node, key, value);
            return node.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 向json中添加属性
     */
    private static <T> void add(JsonNode jsonNode, String key, T value) {
        if (value instanceof String) {
            ((ObjectNode) jsonNode).put(key, (String) value);
        } else if (value instanceof Short) {
            ((ObjectNode) jsonNode).put(key, (Short) value);
        } else if (value instanceof Integer) {
            ((ObjectNode) jsonNode).put(key, (Integer) value);
        } else if (value instanceof Long) {
            ((ObjectNode) jsonNode).put(key, (Long) value);
        } else if (value instanceof Float) {
            ((ObjectNode) jsonNode).put(key, (Float) value);
        } else if (value instanceof Double) {
            ((ObjectNode) jsonNode).put(key, (Double) value);
        } else if (value instanceof BigDecimal) {
            ((ObjectNode) jsonNode).put(key, (BigDecimal) value);
        } else if (value instanceof BigInteger) {
            ((ObjectNode) jsonNode).put(key, (BigInteger) value);
        } else if (value instanceof Boolean) {
            ((ObjectNode) jsonNode).put(key, (Boolean) value);
        } else if (value instanceof byte[]) {
            ((ObjectNode) jsonNode).put(key, (byte[]) value);
        } else {
            ((ObjectNode) jsonNode).put(key, stringify(value));
        }
    }

    /**
     * 除去json中的某个属性
     *
     * @return json
     */
    public static String remove(String json, String key) {
        try {
            JsonNode node = objectMapper.readTree(json);
            ((ObjectNode) node).remove(key);
            return node.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改json中的属性
     */
    public static <T> String update(String json, String key, T value) {
        try {
            JsonNode node = objectMapper.readTree(json);
            ((ObjectNode) node).remove(key);
            add(node, key, value);
            return node.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断字符串是否是json
     *
     * @return json
     */
    public static boolean isJson(String json) {
        try {
            objectMapper.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static <T> T convertValue(Object obj, Class<T> toValueType) {
        return objectMapper.convertValue(obj, toValueType);
    }

    private static InputStream getResourceStream(String name) {
        return JacksonUtil.class.getClassLoader().getResourceAsStream(name);
    }

    private static InputStreamReader getResourceReader(InputStream inputStream) {
        if (null == inputStream) {
            return null;
        }
        return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    }

}



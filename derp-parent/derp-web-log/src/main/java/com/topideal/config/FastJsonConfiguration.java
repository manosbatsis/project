package com.topideal.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**
 * 	只要防止后台返回前台json 为net.sf.json.JSONObject类型时，
 * 	若对象中存在空值，该对象会解析成JSONEmpty类型，返回前台报错
 * @author gy
 *
 */
@Configuration
public class FastJsonConfiguration {
	
	@Bean
	public StringHttpMessageConverter stringHttpMessageConverter(){
	     StringHttpMessageConverter converter  = new StringHttpMessageConverter(Charset.forName("UTF-8"));
	     return converter;
	}
	
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters(){
	    //1.需要定义一个convert转换消息的对象;
	    FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
	    //2:添加fastJson的配置信息;
	    FastJsonConfig fastJsonConfig = new FastJsonConfig();
	    fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,
	    		SerializerFeature.WriteMapNullValue,
	    		SerializerFeature.WriteNullStringAsEmpty);
	    //3处理中文乱码问题
	    List<MediaType> fastMediaTypes = new ArrayList<>();
	    fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
	    //4.在convert中添加配置信息.
	    fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
	    fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
	    HttpMessageConverter<?> converter = fastJsonHttpMessageConverter;
	    return new HttpMessageConverters(converter);

	}
}

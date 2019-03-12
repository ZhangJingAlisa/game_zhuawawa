package com.zhua.game.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.izhaowo.code.spring.plus.converter.RpcConverter;
import com.zhua.game.intereptor.AccountInterceptor;

/**
 * 支持spring mvc 的配
 * 
 * @author liuyijiang
 *
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter { 


	/**
	 * 配置返回值控�? 支持RPC �? restful
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
	     converters.add(new RpcConverter());
	}
	
	/**
	 * 配置拦截�?
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(initAccountInterceptor());
	}
	
	@Bean
	public AccountInterceptor initAccountInterceptor(){
		return new AccountInterceptor();
	}
	
	/**
	 * 配置资源地址
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/**").addResourceLocations("classpath:/webapp/"); //指定页面地址
	}
	
}

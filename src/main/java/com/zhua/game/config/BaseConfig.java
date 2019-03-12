package com.zhua.game.config;

import javax.sql.DataSource;

import org.logicalcobwebs.proxool.ProxoolDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.alibaba.fastjson.JSON;
import com.izhaowo.code.spring.plus.hander.GlobalExceptionResolver;
/**
 * 基础配置 这里配置 本系统需要一些配置信
 * @author liuyijiang
 *
 */
public class BaseConfig {

	
	@Bean(name="datasourceBean")
	@ConfigurationProperties(prefix = "jdbc")
	public DatasourceBean buildDatasourceBean() {
		return new DatasourceBean();
	}
	
	@Bean
	@ConfigurationProperties(prefix = "config")
	public ConfigBean configBean(){
		return new ConfigBean();
	}
	
	@Bean
	@ConfigurationProperties(prefix = "wechatapplet")
	public WechatAppletConfigBean initWechatAppletConfigBean(){
		return new WechatAppletConfigBean();
	}
	
	@Bean(name="dataSource")
	public ProxoolDataSource buildDataSource(DatasourceBean datasourceBean){
		System.out.println(JSON.toJSONString(datasourceBean));
		ProxoolDataSource proxoolDataSource = new ProxoolDataSource();
		proxoolDataSource.setDriver(datasourceBean.getDriverClassName());
		proxoolDataSource.setDriverUrl(datasourceBean.getUrl());
		proxoolDataSource.setUser(datasourceBean.getUsername());
		proxoolDataSource.setPassword(datasourceBean.getPassword());
		proxoolDataSource.setHouseKeepingSleepTime(90000);
		proxoolDataSource.setPrototypeCount(5);
		proxoolDataSource.setMaximumActiveTime(5);
		proxoolDataSource.setMinimumConnectionCount(datasourceBean.getMinimumConnection());
		proxoolDataSource.setMaximumConnectionCount(datasourceBean.getMaximumConnection());
		proxoolDataSource.setSimultaneousBuildThrottle(100);
		proxoolDataSource.setHouseKeepingTestSql("select now()");
		return proxoolDataSource;
	}
	
	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactoryBean initSqlSessionFactoryBean(DataSource dataSource){
		SqlSessionFactoryBean s = new SqlSessionFactoryBean();
		s.setDataSource(dataSource);
		return s;
	}
	
	@Bean
	public MapperScannerConfigurer initMapperScannerConfigurer(){
		MapperScannerConfigurer m = new MapperScannerConfigurer();
		m.setBasePackage("com.zhua.game.service.*.mapper");
		return m;
	}
	
	@Bean(name="transactionManager")
	public PlatformTransactionManager annotationDrivenTransactionManager(ProxoolDataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name="jdbcTemplate")
	public JdbcTemplate buildJdbcTemplate(ProxoolDataSource dataSource){
		JdbcTemplate j = new JdbcTemplate();
		j.setDataSource(dataSource);
		return j;
	}
	
	@Bean
	public CorsFilter initCorsFilter(){
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true); // 允许cookies跨域
	    config.addAllowedOrigin("*");// #允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
	    config.addAllowedHeader("*");// #允许访问的头信息,*表示全部
	    config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
	    config.addAllowedMethod("OPTIONS");// 允许提交请求的方法，*表示全部允许
	    config.addAllowedMethod("HEAD");
	    config.addAllowedMethod("GET");// 允许Get的请求方法
	    config.addAllowedMethod("PUT");
	    config.addAllowedMethod("POST");
	    config.addAllowedMethod("DELETE");
	    config.addAllowedMethod("PATCH");
	    source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
	
	/**
	 * 统一异常配置
	 */
	@Bean
	public GlobalExceptionResolver buildGlobalRestExceptionResolver(){
		return new GlobalExceptionResolver();
	}
}

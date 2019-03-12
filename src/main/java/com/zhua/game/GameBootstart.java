package com.zhua.game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.zhua.game.config.BaseConfig;
import com.zhua.game.config.UndertowConfig;
import com.zhua.game.config.WebAppConfig;

@Configuration
@EnableConfigurationProperties
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class}) 
@ComponentScan
@Import(value = {BaseConfig.class, WebAppConfig.class,UndertowConfig.class}) 
public class GameBootstart {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(GameBootstart.class);
		app.setAddCommandLineProperties(false); //禁止从命令行输入 参数替换变量
		app.run(args); 
	}
	 
}

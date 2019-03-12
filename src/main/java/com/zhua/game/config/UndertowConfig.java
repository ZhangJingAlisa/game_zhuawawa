package com.zhua.game.config;

import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

public class UndertowConfig {

	@Bean
	public UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory() {
	    UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();
	    factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404.html"));
//	    factory.addBuilderCustomizers(new UndertowBuilderCustomizer() {
//
//	        @Override
//	        public void customize(Builder builder) {
//	            builder.addHttpListener(8080, "0.0.0.0");
//	        }
//
//	    });
	    return factory;
	}

	
}

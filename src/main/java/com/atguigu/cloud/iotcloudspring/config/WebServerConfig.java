package com.atguigu.cloud.iotcloudspring.config;


import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//public class WebServerConfig {
//    @Bean
//    public TomcatServletWebServerFactory servletContainer() {
//        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
//        // 添加 HTTP 端口 8080 支持
//        factory.addAdditionalTomcatConnectors(createHttpConnector());
//        return factory;
//    }
//
//    private Connector createHttpConnector() {
//        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
//        connector.setScheme("http");
//        connector.setSecure(false);
//        connector.setPort(8000);
//        return connector;
//    }
//}

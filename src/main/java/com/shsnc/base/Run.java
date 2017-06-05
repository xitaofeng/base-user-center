package com.shsnc.base;

import com.shsnc.api.core.HttpServer;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Hello world!
 */
public class Run
{
    public static void main( String[] args ) throws IOException {

        ResourceBundle bundle = ResourceBundle.getBundle("server");

        HttpServer server = new HttpServer.HttpServerBuilder()
                .bindAddress(bundle.getString("HOST"))
                .listenPort(Integer.valueOf(bundle.getString("PORT")))
                .contextPath(bundle.getString("CONTEXT"))
                .configPath("classpath:bean.xml")
                .testMode(true)
                .build();
        server.start();
    }
}

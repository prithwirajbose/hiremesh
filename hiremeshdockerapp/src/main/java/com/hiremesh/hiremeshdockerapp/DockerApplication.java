package com.hiremesh.hiremeshdockerapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Prithwiraj Bose
 */
@SpringBootApplication
public class DockerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockerApplication.class, args);
        System.out.println("HireMesh Docker App application running");
    }
}

package com.kartaca.slcm.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.kartaca.slcm.core","com.kartaca.slcm.data","com.kartaca.slcm.api"})
@EnableCassandraRepositories(basePackages = { "com.kartaca.slcm.data.repository.cassandra"})
@EnableJpaRepositories(basePackages = {"com.kartaca.slcm.data"})
@EntityScan(basePackages = {"com.kartaca.slcm.data.model"})
public class SlcmApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlcmApiApplication.class, args);
    }

}
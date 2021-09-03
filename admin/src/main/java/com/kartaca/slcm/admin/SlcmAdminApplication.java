package com.kartaca.slcm.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.kartaca.slcm.admin","com.kartaca.slcm.data"})
@EnableCassandraRepositories(basePackages = { "com.kartaca.slcm.data.repository.cassandra"})
@EnableJpaRepositories(basePackages = {"com.kartaca.slcm.data"})
@EntityScan(basePackages = {"com.kartaca.slcm.data.model"})
public class SlcmAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlcmAdminApplication.class, args);
	}

}
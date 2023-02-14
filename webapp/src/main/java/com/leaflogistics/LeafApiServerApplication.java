package com.leaflogistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages={"com.leaflogistics"})
public class LeafApiServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeafApiServerApplication.class, args);
	}

}

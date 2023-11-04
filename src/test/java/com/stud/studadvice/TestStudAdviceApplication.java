package com.stud.studadvice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestStudAdviceApplication {

	public static void main(String[] args) {
		SpringApplication.from(StudAdviceApplication::main).with(TestStudAdviceApplication.class).run(args);
	}

}

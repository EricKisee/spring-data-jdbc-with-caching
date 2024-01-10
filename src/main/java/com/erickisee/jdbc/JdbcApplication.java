package com.erickisee.jdbc;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.ListCrudRepository;

@SpringBootApplication
public class JdbcApplication {

	public static void main(String[] args) {
		SpringApplication.run(JdbcApplication.class, args);
	}

	@Bean
	ApplicationRunner demo (CustomerRepository repository){
		return args-> {
			var cust = repository.save(new Customer(null, "Eric Kisee"));
			repository.findAll().forEach(System.out::println);
		};
	}

}

record Customer (@Id Integer id, String name){}

interface CustomerRepository extends ListCrudRepository <Customer, Integer>{}
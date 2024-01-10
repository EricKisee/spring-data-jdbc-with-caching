package com.erickisee.jdbc;

import java.util.Collection;
import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.ListCrudRepository;

@SpringBootApplication
public class JdbcApplication {

	public static void main(String[] args) {
		SpringApplication.run(JdbcApplication.class, args);
	}

	@Bean
	ApplicationRunner demo (CustomerRepository repository){
		return args-> {
			var profile = new Profile(null, "EricKisee", "Password");
			var orders = Set.of(new Order(null,"A"), new Order(null, "B"));
			var cust = repository.save(new Customer(null, "Eric Kisee", profile, orders));
			repository.findAll().forEach(System.out::println);
			System.out.println(repository.findByName("Eric Kisee"));
		};
	}

}

@Table ("customer_profiles")
record Profile (@Id Integer id, String username, String password){}

@Table("customer_orders")
record Order(@Id Integer id, String name){}

record Customer (@Id Integer id, String name, Profile profile, Set<Order> orders){}

interface CustomerRepository extends ListCrudRepository <Customer, Integer>{
	Collection <Customer> findByName (String name);
}
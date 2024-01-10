package com.erickisee.jdbc;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.util.Assert;

@EnableCaching
@SpringBootApplication
public class JdbcApplication {

	public static void main(String[] args) {
		SpringApplication.run(JdbcApplication.class, args);
	}

	@Bean
	ConcurrentMapCacheManager concurrentMapCacheManager () {
		var cache = new ConcurrentMapCacheManager();
		cache.setAllowNullValues(true);
		cache.setStoreByValue(true);
		return cache;
	}
	
	@Bean
	ApplicationRunner demo (CustomerRepository repository){
		return args-> {
			var profile = new Profile(null, "EricKisee", "Password");
			var orders = Set.of(new Order(null,"A"), new Order(null, "B"));
			var customer = repository.save(new Customer(null, "Eric Kisee", profile, orders));
			repository.findAll().forEach(System.out::println);
			// var result = repository.findByName("Eric Kisee");			
			System.out.println("Getting the original value with the first request");
			repository.findById(customer.id()).get();	
			System.out.println("Getting the cached value with the Second request");
			var result = repository.findById(customer.id()).get();

			Assert.state( result != customer, "The two references should not be same. one should be a copy of the other");
		};
	}

}

@Table ("customer_profiles")
record Profile (@Id Integer id, String username, String password) implements Serializable {} 

@Table("customer_orders")
record Order(@Id Integer id, String name) implements Serializable {}

record Customer (@Id Integer id, String name, Profile profile, Set<Order> orders) implements Serializable {}

interface CustomerRepository extends ListCrudRepository <Customer, Integer>{

	String CUSTOMER_CACHE_KEY = "Customers";

	@Override
	@Cacheable(CUSTOMER_CACHE_KEY)
	Optional<Customer> findById(Integer id );

	Collection <Customer> findByName (String name);

	@Override
	@CacheEvict(value=CUSTOMER_CACHE_KEY, key="#result.id")
	<S extends Customer> S save (S entity);
}
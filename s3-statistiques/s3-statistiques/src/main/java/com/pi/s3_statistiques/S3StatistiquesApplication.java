package com.pi.s3_statistiques;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class S3StatistiquesApplication {

	public static void main(String[] args) {
		SpringApplication.run(S3StatistiquesApplication.class, args);
	}

}
// http://localhost:8083/graphiql?path=/graphql
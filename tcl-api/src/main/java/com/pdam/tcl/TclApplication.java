package com.pdam.tcl;

import com.pdam.tcl.config.StorageProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
@OpenAPIDefinition(info =
	@Info(description = "Una api para el proyecto TCL.Venta de tickets de cine online.",
			version = "1.0",
			contact = @Contact(email = "deluna.rodan21@triana.salesianos.edi", name = "Daniel De Luna Rodríguez"),
			title = "The cinema live Api PDAM"
	)
)
public class TclApplication {

	public static void main(String[] args) {
		SpringApplication.run(TclApplication.class, args);
	}


}

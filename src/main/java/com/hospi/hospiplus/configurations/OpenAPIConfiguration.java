package com.hospi.hospiplus.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi(){

        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Zaharia Andreia-Olga");
        myContact.setEmail("MyDevMail@gmail.com");

        Info information = new Info()
                .title("Medical Stock Management System API")
                .version("1.0")
                .description("This API exposes endpoints to manage the stock.")
                .contact(myContact);

        return new OpenAPI().info(information).servers(List.of(server));

    }

}

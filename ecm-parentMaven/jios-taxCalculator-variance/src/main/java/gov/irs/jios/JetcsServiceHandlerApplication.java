package gov.irs.jios;

import java.io.IOException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.extern.slf4j.Slf4j;

@OpenAPIDefinition(
		info = @Info(
				title = "ETCS Form1040 Rest APIs",
				description = "Enterprise Tax Calculation Service for Form1040", 
				version = "v1.0.0", 
				contact = @Contact(name = "ETCS Team", url = "need.valid.url.gov.irs.etcs", email = "etcs@irsgov.com")),
		servers = @Server(description = "dev", url = "http://localhost:8080")
)
@EnableAsync
@SpringBootApplication
@Slf4j
@PropertySource({
    "classpath:/application.properties",  // parent's properties
    "classpath:/common/application.properties"  // common module's properties
})
public class JetcsServiceHandlerApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(JetcsServiceHandlerApplication.class, args);
	}
	
	@Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
        	log.debug("************************************** Beans from ApplicationContext **************************************");
            String[] beanNames = ctx.getBeanDefinitionNames();
            for (String beanName : beanNames) {
            	log.debug(beanName);
            }
            log.debug("*************************************************** END ***************************************************");
        };
    }
}
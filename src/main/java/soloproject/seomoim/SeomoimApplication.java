package soloproject.seomoim;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import java.io.IOException;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class SeomoimApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeomoimApplication.class, args);
	}
	@Bean
	JPAQueryFactory jpaQueryFactory(EntityManager em){
		return new JPAQueryFactory(em);
	}

	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Bean
	public Storage storage() throws IOException {

		ClassPathResource resource = new ClassPathResource("third-impact-407104-07e059ead5a3.json");
		GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
		String projectId = "third-impact-407104";
		return StorageOptions.newBuilder()
				.setProjectId(projectId)
				.setCredentials(credentials)
				.build()
				.getService();
	}

}

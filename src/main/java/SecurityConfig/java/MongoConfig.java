package SecurityConfig.java;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@EnableMongoRepositories(basePackages = "chatapp.carolineeklund.repositories")
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(MongoClients.create(), "chatapp");
    }
}



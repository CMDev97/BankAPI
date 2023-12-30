package it.dcm.bank.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "banking")
public class BankingProps {
    private ClientProps client;

    @Getter
    @Setter
    public static class ClientProps {
        private String baseUrl;
        private String authSchema;
        private String apiKey;
    }
}
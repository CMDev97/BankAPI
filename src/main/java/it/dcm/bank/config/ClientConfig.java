package it.dcm.bank.config;

import it.dcm.bank.generated.client.fabrick.v4.ApiClient;
import it.dcm.bank.generated.client.fabrick.v4.api.BankAccountApi;
import it.dcm.bank.generated.client.fabrick.v4.api.PaymentsMoneyTransferApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
    private static final String API_KEY_HEADER = "apikey";
    private static final String AUTH_SCHEMA_HEADER = "Auth-Schema";


    @Bean
    public BankAccountApi getBankAccountApiClient(ApiClient apiClient){
        return new BankAccountApi(apiClient);
    }

    @Bean
    public PaymentsMoneyTransferApi getPaymentMoneyTransferApiClient(ApiClient apiClient){
        return new PaymentsMoneyTransferApi(apiClient);
    }

    @Bean
    public ApiClient getApiClient(BankingProps bankingProps){
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(bankingProps.getClient().getBaseUrl());
        apiClient.addDefaultHeader(API_KEY_HEADER, bankingProps.getClient().getApiKey());
        apiClient.addDefaultHeader(AUTH_SCHEMA_HEADER, bankingProps.getClient().getAuthSchema());
        return apiClient;
    }
}

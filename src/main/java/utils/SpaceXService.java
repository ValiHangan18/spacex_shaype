package utils;

import exception.MyCustomException;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.HttpComponentsClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class SpaceXService {

    private SpaceXService() {
    }

    private static GraphQlClient graphQlClient;

    /**
     * Get SpaceX GraphQL client
     *
     * @return SpaceX GraphQL client
     */
    public static GraphQlClient getSpaceXClient() {
        if (graphQlClient == null) {
            CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();
            graphQlClient = HttpGraphQlClient.builder()
                    .webClient(builder -> builder.clientConnector(new HttpComponentsClientHttpConnector(httpClient))
                            .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                            .baseUrl("https://spacex-production.up.railway.app/")
                            .filter(ExchangeFilterFunction.ofResponseProcessor(response -> {
                                if (response.statusCode().isError()) {
                                    // Capture the status code and body if there’s an HTTP error
                                    return response.bodyToMono(String.class).flatMap(body -> Mono.error(new MyCustomException(response.statusCode(), body)));
                                }
                                return Mono.just(response);
                            }))
                            .filter((request, next) -> next.exchange(request)
                                    .timeout(Duration.ofSeconds(10))))
                    .build();
        }
        return graphQlClient;
    }


}
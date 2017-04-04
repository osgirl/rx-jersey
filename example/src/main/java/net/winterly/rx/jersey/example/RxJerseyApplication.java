package net.winterly.rx.jersey.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.winterly.rx.jersey.client.RxJerseyClientFeature;
import net.winterly.rx.jersey.server.RxJerseyServerFeature;

import javax.ws.rs.client.Client;

public class RxJerseyApplication extends Application<RxJerseyConfiguration> {

    public static void main(String[] args) throws Exception {
        new RxJerseyApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<RxJerseyConfiguration> bootstrap) {
        bootstrap.getObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public void run(RxJerseyConfiguration configuration, Environment environment) throws Exception {
        RxJerseyServerFeature rxJerseyServerFeature = new RxJerseyServerFeature();
        rxJerseyServerFeature.register(HeaderInterceptor.class);
        environment.jersey().register(rxJerseyServerFeature);

        RxJerseyClientFeature rxJerseyClientFeature = new RxJerseyClientFeature();
        rxJerseyClientFeature.register(getClient(configuration, environment));
        environment.jersey().register(rxJerseyClientFeature);

        environment.jersey().register(GithubResource.class);
    }

    private Client getClient(RxJerseyConfiguration configuration, Environment environment) {
        return new JerseyClientBuilder(environment)
                .using(configuration.jerseyClient)
                .build(RxJerseyClientFeature.RX_JERSEY_CLIENT_NAME);
    }
}

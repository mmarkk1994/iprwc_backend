package src;

import com.github.toastshaman.dropwizard.auth.jwt.JwtAuthFilter;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.jdbi.v3.core.Jdbi;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import src.models.User;
import src.core.JwtAuthenticator;
import src.core.JwtHelper;
import src.resources.ProductResource;
import src.resources.UserResource;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class WebshopApplication extends Application<WebshopConfiguration> {

    public static Jdbi jdbiCon;

    public static void main(final String[] args) throws Exception {
        new WebshopApplication().run("server", "config.yml");
    }

    @Override
    public String getName() {
        return "webshop";
    }

    @Override
    public void initialize(final Bootstrap<WebshopConfiguration> bootstrap) {
        bootstrap.addBundle(new MultiPartBundle());
    }

    @Override
    public void run(final WebshopConfiguration configuration,
                    final Environment environment) {
        registerJwtAuthentication(configuration, environment);
        setupJdbiConnection(environment, configuration.getDatabase());

        // set jwtSecret
        JwtHelper.jwtSecret = configuration.getJwtSecret();

        // register resources
        environment.jersey().register(new UserResource());
        environment.jersey().register(new ProductResource());

        configureCors(environment);
    }

    private void setupJdbiConnection(final Environment environment,
                                     DataSourceFactory dataSourceFactory){
        final JdbiFactory jdbiFactory = new JdbiFactory();
        jdbiCon = jdbiFactory.build(environment, dataSourceFactory, "postgresql");
    }

    private void registerJwtAuthentication(final WebshopConfiguration configuration, final Environment environment){
        final byte[] jwtSecret = configuration.getJwtSecret();
        final JwtConsumer consumer = new JwtConsumerBuilder()
                .setAllowedClockSkewInSeconds(30)
                .setRequireExpirationTime()
                .setVerificationKey(new HmacKey(jwtSecret))
                .build();

        environment.jersey().register(new AuthDynamicFeature(
                new JwtAuthFilter.Builder<User>()
                .setJwtConsumer(consumer)
                .setPrefix("Bearer")
                .setAuthenticator(new JwtAuthenticator())
                .buildAuthFilter()
        ));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    }

    private void configureCors(Environment environment) {
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // configure CORS parameters
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin,Authorization");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");

        // add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }

}

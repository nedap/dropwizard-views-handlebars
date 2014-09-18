package io.dward.views.handlebars;

import com.codahale.metrics.MetricRegistry;
import com.google.common.collect.ImmutableList;
import io.dropwizard.logging.LoggingFactory;
import io.dropwizard.views.ViewMessageBodyWriter;
import io.dropwizard.views.ViewRenderer;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class HandlebarsViewRendererTest extends JerseyTest {
    static {
        LoggingFactory.bootstrap();
    }

    @Path("/test/")
    @Produces(MediaType.TEXT_HTML)
    public static class ExampleResource {
        @GET
        @Path("/absolute")
        public AbsoluteView showAbsolute() {
            return new AbsoluteView("yay");
        }

        @GET
        @Path("/relative")
        public RelativeView showRelative() {
            return new RelativeView();
        }

        @GET
        @Path("/bad")
        public BadView showBad() {
            return new BadView();
        }

        @GET
        @Path("/partial")
        public PartialView showPartial() {
            return new PartialView();
        }
    }

    @Override
    protected Application configure() {
        ResourceConfig config = new ResourceConfig();
        final ViewRenderer renderer = new HandlebarsViewRenderer();
        config.register(new ViewMessageBodyWriter(new MetricRegistry(), ImmutableList.of(renderer)));
        config.register(new ExampleResource());
        return config;
    }

    @Test
    public void rendersViewsWithAbsoluteTemplatePaths() throws Exception {
        final String response = target("/test/absolute").request().get(String.class);
        assertThat(response).isEqualTo("Woop woop. yay");
    }

    @Test
    public void rendersViewsWithRelativeTemplatePaths() throws Exception {
        final String response = target("/test/relative").request().get(String.class);
        assertThat(response).isEqualTo("Ok.");
    }

    @Test
    public void returnsA500ForViewsWithBadTemplatePaths() throws Exception {
        try {
            target("/test/bad").request().get(String.class);
            failBecauseExceptionWasNotThrown(WebApplicationException.class);
        } catch (WebApplicationException e) {
            assertThat(e.getResponse().getStatus())
                    .isEqualTo(500);

            assertThat(e.getResponse().readEntity(String.class))
                    .isEqualTo("<html><head><title>Missing Template</title></head><body><h1>Missing Template</h1><p>Template /woo-oo-ahh.txt.hbs not found.</p></body></html>");
        }
    }

    @Test
    public void testPartialsComposePerHandlebarsSpec() {
        assertThat(target("/test/partial").request().get(String.class))
                .isEqualTo("<h1>Base</h1><p>Partial</p>");
    }
}

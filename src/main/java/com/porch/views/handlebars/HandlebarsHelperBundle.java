package com.porch.views.handlebars;

import com.github.jknack.handlebars.Helper;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * This class is the entry point to all configuration of the HandlebarsViewRender. For example, this allows the registration
 * of Handlebars Helpers on application initialization. The body of {@link #configureHandlebars(io.dropwizard.Configuration)}
 * should contain any initialization of Handlebars helpers. Register these helpers with
 * {@link #registerHelper(String, com.github.jknack.handlebars.Helper)}.
 * <p/>
 * Example :
 * <p/>
 * <pre> {@code
 * public class HelperBundle extends HandlebarsHelperBundler<Configuration> {
 *      public void configureHandlebars(Configuration config) {
 *          DateHelper dateHelper = new DateHelper(config.getTimeZone());
 *          registerHelper("date", dateHelper);
 *          setPrettyPrint(true);
 *      }
 * }
 * </pre>
 * <p/>
 */
public abstract class HandlebarsHelperBundle<C extends Configuration> implements ConfiguredBundle<C> {

    private final HandlebarsViewRenderer viewRenderer;

    protected abstract void configureHandlebars(C configuration);

    public HandlebarsHelperBundle(HandlebarsViewRenderer renderer) {
        this.viewRenderer = renderer;
    }
    
    @Override
    public final void initialize(Bootstrap<?> bootstrap) {/* empty */}

    @Override
    public final void run(C configuration, Environment environment) {
        configureHandlebars(configuration);
    }

    /**
     * {@link com.github.jknack.handlebars.Handlebars#registerHelperMissing(com.github.jknack.handlebars.Helper)}
     */
    public <H> void registerHelperMissing(Helper<H> helper) {
        viewRenderer.getHandlebars().registerHelperMissing(helper);
    }

    /**
     * {@link com.github.jknack.handlebars.Handlebars#registerHelper(String, com.github.jknack.handlebars.Helper)}
     */
    public <H> void registerHelper(String name, Helper<H> helper) {
        viewRenderer.getHandlebars().registerHelper(name, helper);
    }

    /**
     * {@link com.github.jknack.handlebars.Handlebars#setPrettyPrint(boolean)}
     */
    public void setPrettyPrint(boolean prettyPrint) {
        viewRenderer.getHandlebars().setPrettyPrint(prettyPrint);
    }

    /**
     * {@link com.github.jknack.handlebars.Handlebars#setInfiniteLoops(boolean)}
     */
    public void setInfiniteLoops(boolean infiniteLoops) {
        viewRenderer.getHandlebars().setInfiniteLoops(infiniteLoops);
    }

    public HandlebarsViewRenderer getViewRenderer() {
        return viewRenderer;
    }
}

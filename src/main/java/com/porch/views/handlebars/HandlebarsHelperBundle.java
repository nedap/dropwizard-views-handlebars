package com.porch.views.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.cache.TemplateCache;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.google.common.base.Verify;
import com.google.common.collect.Lists;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.Arrays;
import java.util.List;

/**
 * This class is the entry point to all configuration of the HandlebarsViewRender. For example, this allows the registration
 * of Handlebars Helpers on application initialization. The body of {@link #run(io.dropwizard.Configuration, io.dropwizard.setup.Environment)}
 * should contain any initialization of Handlebars helpers. Register these helpers with
 * {@link #registerHelper(String, com.github.jknack.handlebars.Helper)}.
 * <p/>
 * Example :
 * <p/>
 * <pre> {@code
 * public class HelperBundle extends HandlebarsHelperBundler<Configuration> {
 *      public void run(Configuration config, Environment environment) {
 *          DateHelper dateHelper = new DateHelper(config.getTimeZone());
 *          registerHelper("date", dateHelper);
 *          setPrettyPrint(true);
 *      }
 * }
 * </pre>
 * <p/>
 */
public abstract class HandlebarsHelperBundle<C extends Configuration> implements ConfiguredBundle<C> {

    /**
     * {@inheritDoc}
     */
    @Override
    public final void initialize(Bootstrap<?> bootstrap) {/* empty */}

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void run(C configuration, Environment environment);


    /**
     * Sets the template cache to the given instance.
     *
     * If you wish to not have caching at all, use a {@link com.github.jknack.handlebars.cache.NullTemplateCache}
     * @param cache
     */
    public static void setTemplateCache(TemplateCache cache) {
        HandlebarsViewRenderer.HANDLEBARS.with(Verify.verifyNotNull(cache));
    }

    /**
     * Prepends the list of template loaders to the Handlebars search path in the order given.
     * @param loader
     */
    public static void prependTemplateLoaders(TemplateLoader ...loader) {
        Verify.verifyNotNull(loader);
        Verify.verify(loader.length > 0);
        Handlebars hbs = HandlebarsViewRenderer.HANDLEBARS;
        List<TemplateLoader> loaders = Lists.newArrayList();
        loaders.addAll(Arrays.asList(loader));
        loaders.add(hbs.getLoader());
        hbs.with(loaders.toArray(new TemplateLoader[loaders.size()]));
    }

    /**
     * Appends the list of template loaders to the Handlebars search path in the order given
     * @param loader
     */
    public static void appendTemplateLoaders(TemplateLoader ...loader) {
        Verify.verifyNotNull(loader);
        Verify.verify(loader.length > 0);
        Handlebars hbs = HandlebarsViewRenderer.HANDLEBARS;
        List<TemplateLoader> loaders = Lists.newArrayList();
        loaders.add(hbs.getLoader());
        loaders.addAll(Arrays.asList(loader));
        hbs.with(loaders.toArray(new TemplateLoader[loaders.size()]));
    }

    /**
     * Overwrites any template loaders configured on the Handlebars instance with the ones given.
     * @param loader
     */
    public static void setTemplateLoaders(TemplateLoader ...loader) {
        Verify.verifyNotNull(loader);
        Verify.verify(loader.length > 0);
        HandlebarsViewRenderer.HANDLEBARS.with(loader);
    }

    /**
     * {@link com.github.jknack.handlebars.Handlebars#registerHelperMissing(com.github.jknack.handlebars.Helper)}
     */
    public static <H> void registerHelperMissing(Helper<H> helper) {
        HandlebarsViewRenderer.HANDLEBARS.registerHelperMissing(helper);
    }

    /**
     * {@link com.github.jknack.handlebars.Handlebars#registerHelper(String, com.github.jknack.handlebars.Helper)}
     */
    public static <H> void registerHelper(String name, Helper<H> helper) {
        HandlebarsViewRenderer.HANDLEBARS.registerHelper(name, helper);
    }

    /**
     * {@link com.github.jknack.handlebars.Handlebars#setPrettyPrint(boolean)}
     */
    public static void setPrettyPrint(boolean prettyPrint) {
        HandlebarsViewRenderer.HANDLEBARS.setPrettyPrint(prettyPrint);
    }

    /**
     * {@link com.github.jknack.handlebars.Handlebars#setInfiniteLoops(boolean)}
     */
    public static void setInfiniteLoops(boolean infiniteLoops) {
        HandlebarsViewRenderer.HANDLEBARS.setInfiniteLoops(infiniteLoops);
    }
}

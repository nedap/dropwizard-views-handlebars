package com.porch.views.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.google.common.collect.Lists;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;

import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Verify.verifyNotNull;

/**
 * This class is the entry point to all configuration of the HandlebarsViewRender. For example, this allows the registration
 * of Handlebars Helpers on application initialization. The body of {@link #run(Object, io.dropwizard.setup.Environment)}
 * should contain any initialization of Handlebars helpers.
 * <p>
 * Example :
 * </p>
 * <pre> {@code
 * public class HelperBundle extends HandlebarsHelperBundler<Configuration> {
 *      public void run(Configuration config, Environment environment) {
 *          DateHelper dateHelper = new DateHelper(config.getTimeZone());
 *          handlebars().registerHelper("date", dateHelper);
 *          handlebars().setPrettyPrint(true);
 *      }
 * }
 * }
 * </pre>
 * </p>
 */
public abstract class HandlebarsHelperBundle<C extends Configuration> implements ConfiguredBundle<C> {

    /**
     * {@inheritDoc}
     */
    @Override
    public final void initialize(Bootstrap<?> bootstrap) {/* empty */}

    /**
     * Prepends the list of template loaders to the Handlebars search path in the order given
     * @param loaders
     */
    protected void prependTemplateLoaders(List<TemplateLoader> loaders) {
        List<TemplateLoader> combinedLoaders = Lists.newArrayList();
        combinedLoaders.addAll(loaders);
        combinedLoaders.add(handlebars().getLoader());
        setTemplateLoaders(combinedLoaders);
    }

    /**
     * Appends the list of template loaders to the Handlebars search path in the order given.
     * @param loaders
     */
    protected void appendTemplateLoaders(List<TemplateLoader> loaders) {
        List<TemplateLoader> combinedLoaders = Lists.newArrayList();
        combinedLoaders.add(handlebars().getLoader());
        combinedLoaders.addAll(loaders);
        setTemplateLoaders(combinedLoaders);
    }

    /**
     * Convenience method for {@link Handlebars#with(com.github.jknack.handlebars.io.TemplateLoader...)} with a {@link List}
     * @param loaders
     */
    protected void setTemplateLoaders(List<TemplateLoader> loaders) {
        handlebars().with(loaders.toArray(new TemplateLoader[loaders.size()]));

    }

    /**
     * @see #prependTemplateLoaders(java.util.List)
     */
    protected void prependTemplateLoaders(TemplateLoader ...loader) {
        prependTemplateLoaders(Arrays.asList(verifyNotNull(loader)));
    }

    /**
     * @see #appendTemplateLoaders(java.util.List)
     */
    protected void appendTemplateLoaders(TemplateLoader ...loader) {
        appendTemplateLoaders(Arrays.asList(verifyNotNull(loader)));
    }

    /**
     * Returns the handlebars instance used by the view renderer
     * @return The handlebars instance used by the view renderer
     */
    protected Handlebars handlebars() {
        return HandlebarsViewRenderer.HANDLEBARS;
    }
}

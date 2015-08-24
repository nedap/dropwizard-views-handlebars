package com.porch.views.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.cache.GuavaTemplateCache;
import com.github.jknack.handlebars.io.TemplateSource;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.dropwizard.views.View;
import io.dropwizard.views.ViewRenderer;

import javax.ws.rs.WebApplicationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * A {@link ViewRenderer} which renders Handlebars ({@code .hbs}) templates.
 */
public class HandlebarsViewRenderer implements ViewRenderer {
    /**
     * For use by Handlebars.java internally.
     */
    private Cache<TemplateSource, Template> templateCache = CacheBuilder
            .newBuilder()
            .build();

    /**
     * Handlebars.java does not cache reads of Template content from resources.
     */
    @VisibleForTesting
    LoadingCache<String, Template> compilationCache = CacheBuilder
            .newBuilder()
            .build(new CacheLoader<String, Template>() {
                @Override
                public Template load(String srcUrl) throws Exception {
                    return handlebars.compile(srcUrl.replaceAll(".hbs$", ""));
                }
            });

    /**
     * Exposed for use in {@link HandlebarsHelperBundle} for miscellaneous configuration.
     */
    public Handlebars handlebars = new Handlebars()
            .with(new GuavaTemplateCache(templateCache));

    public HandlebarsViewRenderer() {
    }

    public Handlebars getHandlebars() {
        return handlebars;
    }

    @Override
    public boolean isRenderable(View view) {
        return view.getTemplateName().endsWith(".hbs");
//                || view.getTemplateName().endsWith(".mustache"); // we can replace dropwizard-views-mustache with this.
    }

    @Override
    public void render(View view, Locale locale, OutputStream output) throws IOException, WebApplicationException {
        try (Writer writer = new OutputStreamWriter(output, view.getCharset().or(Charsets.UTF_8))) {
            compilationCache.get(view.getTemplateName()).apply(view, writer);
        } catch (FileNotFoundException | ExecutionException e) {
            throw new FileNotFoundException("Template " + view.getTemplateName() + " not found.");
        }
    }

    @Override
    public void configure(Map<String, String> map) {
        if(map.containsKey("cache") && map.get("cache").equals("false")) {
            compilationCache = CacheBuilder
                    .newBuilder().expireAfterWrite(10, TimeUnit.MILLISECONDS)
                    .build(new CacheLoader<String, Template>() {
                        @Override
                        public Template load(String srcUrl) throws Exception {
                            return handlebars.compile(srcUrl.replaceAll(".hbs$", ""));
                        }
                    });
            templateCache = CacheBuilder
                    .newBuilder().expireAfterWrite(10, TimeUnit.MILLISECONDS)
                    .build();
        }

    }

    @Override
    public String getSuffix() {
        return "hbs";
    }
}

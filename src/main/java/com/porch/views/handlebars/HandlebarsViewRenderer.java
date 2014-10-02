package com.porch.views.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.HandlebarsException;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.github.jknack.handlebars.io.TemplateSource;
import com.google.common.base.Charsets;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.dropwizard.views.View;
import io.dropwizard.views.ViewRenderer;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * A {@link ViewRenderer} which renders Handlebars ({@code .hbs}) templates.
 */
public class HandlebarsViewRenderer implements ViewRenderer {
    /**
     * For use by Handlebars.java internally.
     */
    private static final Cache<TemplateSource, Template> templateCache = CacheBuilder
            .newBuilder()
            .build();

    /**
     * Exposed for use in {@link HandlebarsHelperBundle} for miscellaneous configuration.
     */
    static Handlebars HANDLEBARS = new Handlebars();
    static {
        HANDLEBARS
                // Cache the result of compilation
                .with(new FixedGuavaTemplateCache(templateCache))
                // Allow views to specify the complete filename "foo.hbs" in addition to "foo"
                // We need the former for Dropwizard View support and the latter for writing idiomatic handlebars
                .with(HANDLEBARS.getLoader(), new ClassPathTemplateLoader(TemplateLoader.DEFAULT_PREFIX, ""));
    }


    @Override
    public boolean isRenderable(View view) {
        return view.getTemplateName().endsWith(".hbs");
//                || view.getTemplateName().endsWith(".mustache"); // we can replace dropwizard-views-mustache with this.
    }

    @Override
    public void render(View view, Locale locale, OutputStream output) throws IOException, WebApplicationException {
        try {
            Template template = HANDLEBARS.compile(view.getTemplateName());
            Charset charset = view.getCharset().or(Charsets.UTF_8);
            //We are NOT supposed to close the output stream which means not closing the writer.
            OutputStreamWriter writer = new OutputStreamWriter(output, charset);
            template.apply(view, writer);
            //No output will be written if we do not flush the writer.
            writer.flush();
        } catch (HandlebarsException e) {
            throw new WebApplicationException(
                    Response.serverError().type(MediaType.TEXT_HTML_TYPE).entity(e.getError()).build()
            );
        } catch (FileNotFoundException e) {
            FileNotFoundException ex = new FileNotFoundException("Template " + view.getTemplateName() + " not found.");
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }
    }
}
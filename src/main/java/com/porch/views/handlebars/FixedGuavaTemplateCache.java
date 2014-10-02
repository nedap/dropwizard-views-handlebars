package com.porch.views.handlebars;

import com.github.jknack.handlebars.Parser;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.cache.GuavaTemplateCache;
import com.github.jknack.handlebars.io.TemplateSource;
import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.util.concurrent.UncheckedExecutionException;

import java.io.IOException;

/**
 * Fixes the GuavaTemplateCache to throw the proper exception type on {@link com.google.common.util.concurrent.UncheckedExecutionException}s
 */
public class FixedGuavaTemplateCache extends GuavaTemplateCache {

    /**
     * Creates a new {@link FixedGuavaTemplateCache}.
     *
     * @param cache The guava cache to use. Required.
     */
    public FixedGuavaTemplateCache(Cache<TemplateSource, Template> cache) {
        super(cache);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Template get(TemplateSource source, Parser parser) throws IOException {
        try {
            return super.get(source, parser);
        } catch (UncheckedExecutionException ex) {
            Throwables.propagateIfInstanceOf(ex.getCause(), IOException.class);
            throw Throwables.propagate(ex.getCause());
        }
    }
}

package com.porch.views.handlebars;

import com.github.jknack.handlebars.HandlebarsError;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.MessageFormat;

@Provider
@Produces(MediaType.TEXT_HTML)
public class HandlebarsErrorMessageWriter implements MessageBodyWriter<HandlebarsError> {
    private static final String ERROR_TEMPLATE =
            "<html>" +
                    "<head><title>Handlebars Error</title></head>" +
                    "<body><h1>Error Compiling Template</h1><p>{0}</p></body>" +
                    "</html>";
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return HandlebarsError.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(HandlebarsError handlebarsError, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }


    @Override
    public void writeTo(HandlebarsError handlebarsError, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        final String msg = MessageFormat.format(ERROR_TEMPLATE, handlebarsError.toString());
        Writer w = new OutputStreamWriter(entityStream);
        w.write(msg);
        w.flush();
        // Do NOT Close the writer. See entityStream docs for writeTo
    }
}

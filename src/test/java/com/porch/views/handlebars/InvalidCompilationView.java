package com.porch.views.handlebars;

import io.dropwizard.views.View;

public class InvalidCompilationView extends View {
    public InvalidCompilationView() {
        super("/doesntcompile.hbs");
    }
}

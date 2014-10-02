package com.porch.views.handlebars;

import io.dropwizard.views.View;

public class AbsoluteView extends View {
    private final String name;

    public AbsoluteView(String name) {
        super("/example.hbs");
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

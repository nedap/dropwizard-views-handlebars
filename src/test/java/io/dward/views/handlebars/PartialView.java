package io.dward.views.handlebars;

import io.dropwizard.views.View;

public class PartialView extends View {
    protected PartialView() {
        super("/partial.hbs");
    }
}

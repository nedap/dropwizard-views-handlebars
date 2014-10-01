## Handlebars View Rendering for [Dropwizard](http://dropwizard.io)
===========================

This project adds Handlebar support to `dropwizard-views`. `dropwizard-views-handlebars` has the same API as 
`dropwizard-views-mustache` & `dropwizard-views-mustache` which is documented [here](http://dropwizard.io/manual/views.html).
The view render uses [Handlebars.java](https://github.com/jknack/handlebars.java) under the hood.

Releases versions correspond with compatible version of Dropwizard and are published to Maven Central.

```
<dependency>
    <groupId>com.porch</groupId>
    <artifactId>dropwizard-views-handlebars</artifactId>
    <version>0.7.1</version>
</dependency>
```

`dropwizard-views-handlebars` also exposes the Handlebars.java Helper API and certain configurations through the [HandlebarsHelperBundler](src/main/java/com/porch/views/handlebars/HandlebarsHelperBundle.java).

```java
public class HelperBundle extends HandlebarsHelperBundler<Configuration> {
    public void configureHandlebars(Configuration config) {
        DateHelper dateHelper = new DateHelper(config.getTimeZone());
        registerHelper("date", dateHelper);
        setPrettyPrint(true);
    }
}
```

Then  add the `HelperBundle` in the `initialize` method of your Application class.

```java
public void initialize(Bootstrap<MyConfiguration> bootstrap) {
    bootstrap.addBundle(new ViewBundle());
    ...
    bootstrap.addBundle(new HelperBundle());
}
```


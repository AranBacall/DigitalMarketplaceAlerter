package uk.andrewgorton.digitalmarketplace.alerter.views.email;

import io.dropwizard.views.View;
import io.dropwizard.views.ViewRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Exposes the DropWizard {@link ViewRenderer} capability
 * to custom code (as opposed to the access provided natively
 * for rendering views on endpoints
 */
public class EmailViewRenderer {

    private ViewRendererLoader loader;

    public EmailViewRenderer(ViewRendererLoader loader) {
        this.loader = loader;
    }

    public String renderEmail(View view) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            loader.getApplicableRendererFor(view)
                    .render(view, Locale.getDefault(), out);
            return out.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new IOException(String.format("Failed to render view from template %s",
                    view.getTemplateName()), e);
        }
    }
}

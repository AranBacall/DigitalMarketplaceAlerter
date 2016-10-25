package uk.andrewgorton.digitalmarketplace.alerter.views.email;

import io.dropwizard.views.View;
import io.dropwizard.views.ViewRenderer;

import java.util.ServiceLoader;

/**
 * Responsible for finding the appropriate ViewRenderer to use
 * when rendering a given view. Multiple renderers can be used
 * (freemarker or mustache for instance), so it is important to
 * decouple from these implementation details by using the
 * {@link ServiceLoader}.
 */
public class ViewRendererLoader {

    private ServiceLoader<ViewRenderer> renderers;

    public ViewRendererLoader(ServiceLoader<ViewRenderer> renderers) {
        this.renderers = renderers;
    }

    /**
     * @return the {@link ViewRenderer} that is able to render the
     * provided view, or throws {@link IllegalArgumentException} if none can be found
     */
    public ViewRenderer getApplicableRendererFor(View view) {
        for (ViewRenderer renderer : renderers) {
            if (renderer.isRenderable(view)) {
                return renderer;
            }
        }

        throw new IllegalArgumentException("No renderer found for " + view.getTemplateName());
    }
}

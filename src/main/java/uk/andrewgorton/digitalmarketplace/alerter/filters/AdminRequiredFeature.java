package uk.andrewgorton.digitalmarketplace.alerter.filters;

import org.glassfish.jersey.server.model.AnnotatedMethod;
import uk.andrewgorton.digitalmarketplace.alerter.annotations.AdminRequired;
import uk.andrewgorton.digitalmarketplace.alerter.annotations.LoginRequired;
import uk.andrewgorton.digitalmarketplace.alerter.dao.UserDAO;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.FeatureContext;

public class AdminRequiredFeature implements DynamicFeature {
    @Context
    HttpServletRequest webRequest;

    @Inject
    UserDAO userDAO;

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        AnnotatedMethod am = new AnnotatedMethod(resourceInfo.getResourceMethod());
        if (resourceInfo.getResourceMethod().getAnnotation(AdminRequired.class) != null) {
            context.register(new AdminRequiredFilter(webRequest,userDAO));
        }
    }
}

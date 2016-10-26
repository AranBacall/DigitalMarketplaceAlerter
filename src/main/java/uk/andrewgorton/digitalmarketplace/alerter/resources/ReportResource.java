package uk.andrewgorton.digitalmarketplace.alerter.resources;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.andrewgorton.digitalmarketplace.alerter.annotations.LoginRequired;
import uk.andrewgorton.digitalmarketplace.alerter.dao.OpportunityDAO;
import uk.andrewgorton.digitalmarketplace.alerter.dao.report.PieChartDAO;
import uk.andrewgorton.digitalmarketplace.alerter.views.opportunity.ListView;
import uk.andrewgorton.digitalmarketplace.alerter.views.report.ReportView;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 * Created by koskinasm on 25/10/2016.
 */

@Path("/report")
public class ReportResource {

    private final Logger LOGGER = LoggerFactory.getLogger(OpportunityResource.class);
    private final PieChartDAO pieChartDAO;

    public ReportResource(PieChartDAO pieChartDAO) {
        this.pieChartDAO = pieChartDAO;
    }

    @GET
    @Timed
    @LoginRequired
    public Object getReportView(@Context HttpServletRequest request) {
        return new ReportView();
    }

    @GET
    @Path("/opc")
    @Timed
    @LoginRequired
    @Produces("application/json")
    public Object getOppurtunitiesPerCustomer(@Context HttpServletRequest request) {

        return pieChartDAO.findOpportunitiesPerCustomer();
    }

    @GET
    @Path("/opl")
    @Timed
    @LoginRequired
    @Produces("application/json")
    public Object getOppurtunitiesPerLocation(@Context HttpServletRequest request) {

        return pieChartDAO.findOpportunitiesPerLocation();
    }
}

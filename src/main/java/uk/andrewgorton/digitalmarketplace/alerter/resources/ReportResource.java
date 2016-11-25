package uk.andrewgorton.digitalmarketplace.alerter.resources;

import com.codahale.metrics.annotation.Timed;
import uk.andrewgorton.digitalmarketplace.alerter.annotations.LoginRequired;
import uk.andrewgorton.digitalmarketplace.alerter.dao.report.PieChartDAO;
import uk.andrewgorton.digitalmarketplace.alerter.views.report.ReportView;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

@Path("/report")
public class ReportResource {

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

    @GET
    @Path("/responses")
    @Timed
    @LoginRequired
    @Produces("application/json")
    public Object responses() {
        return pieChartDAO.responses();
    }

    @GET
    @Path("/bids-against-opportunities")
    @Timed
    @LoginRequired
    @Produces("application/json")
    public Object bidsAgainstOpportunities() {
        return pieChartDAO.bidsAgainstOpportunities();
    }
}

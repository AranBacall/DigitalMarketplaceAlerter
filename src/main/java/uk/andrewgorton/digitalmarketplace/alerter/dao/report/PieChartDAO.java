package uk.andrewgorton.digitalmarketplace.alerter.dao.report;

import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import uk.andrewgorton.digitalmarketplace.alerter.dao.mappers.PieChartMapper;
import uk.andrewgorton.digitalmarketplace.alerter.report.PieChartData;

import java.util.List;

/**
 * Created by koskinasm on 25/10/2016.
 */
@RegisterMapper(PieChartMapper.class)
public interface PieChartDAO {

    @SqlQuery("select customer as group_name, count(*) as occurrences from opportunity group by customer")
    List<PieChartData> findOpportunitiesPerCustomer();

    @SqlQuery("select location as group_name, count(*) as occurrences from opportunity group by location")
    List<PieChartData> findOpportunitiesPerLocation();

    @SqlQuery("select r.response as group_name, count(*) as occurrences from opportunity_response or, response r " +
            "where or.response = r.id " +
            "group by r.response;")
    List<PieChartData> responses();
}

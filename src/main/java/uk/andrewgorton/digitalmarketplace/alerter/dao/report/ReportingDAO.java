package uk.andrewgorton.digitalmarketplace.alerter.dao.report;

import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import uk.andrewgorton.digitalmarketplace.alerter.dao.mappers.KeyValueColorMapper;
import uk.andrewgorton.digitalmarketplace.alerter.report.KeyValueColorItem;

import java.util.List;

@RegisterMapper(KeyValueColorMapper.class)
public interface ReportingDAO {

    @SqlQuery("select customer as group_name, count(*) as occurrences from opportunity group by customer")
    List<KeyValueColorItem> findOpportunitiesPerCustomer();

    @SqlQuery("select location as group_name, count(*) as occurrences from opportunity group by location")
    List<KeyValueColorItem> findOpportunitiesPerLocation();

    @SqlQuery("select r.response as group_name, count(*) as occurrences from opportunity_response or, response r " +
            "where or.response = r.id " +
            "group by r.response;")
    List<KeyValueColorItem> responses();

    @SqlQuery("select 'opportunities' as group_name, count(*) as occurrences from opportunity " +
            "union all " +
            "select 'bids' as group_name, count(*) as occurrences from opportunity_response opresp " +
            "where opresp.response = 1;")
    List<KeyValueColorItem> bidsAgainstOpportunities();
}

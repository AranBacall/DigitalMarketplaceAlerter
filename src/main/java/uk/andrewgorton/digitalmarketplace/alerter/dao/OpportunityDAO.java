package uk.andrewgorton.digitalmarketplace.alerter.dao;

import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;
import uk.andrewgorton.digitalmarketplace.alerter.dao.mappers.OpportunityMapper;

import java.util.List;

@RegisterMapper(OpportunityMapper.class)
public interface OpportunityDAO {
    @SqlQuery("select count(*) from opportunity where url = :url")
    long getCountByUrl(@Bind("url") String url);

    @SqlBatch("insert into opportunity(title, type, location, customer, published, " +
            "closing, closed, excerpt, url, firstSeen, lastUpdated, alerted) " +
            "values(:title, :opportunityType, :location, :customer, :published, " +
            ":closing, :closed, :excerpt, :url, :lastUpdated, :lastUpdated, false)")
    @BatchChunkSize(1000)
    void insert(@BindBean List<Opportunity> opportunities);

    @SqlBatch("merge into opportunity(title, type, location, customer, published, " +
            "closing, closed, excerpt, url, lastUpdated) "
            + "key(url) " +
            "values(:title, :opportunityType, :location, :customer, :published, " +
            ":closing, :closed, :excerpt, :url, :lastUpdated)")
    @BatchChunkSize(1000)
    void updateOpen(@BindBean List<Opportunity> opportunities);

    @SqlBatch("merge into opportunity(title, type, location, customer, " +
            "closed, excerpt, url, lastUpdated) "
            + "key(url) " +
            "values(:title, :opportunityType, :location, :customer, " +
            ":closed, :excerpt, :url, :lastUpdated)")
    @BatchChunkSize(1000)
    void updateClosed(@BindBean List<Opportunity> opportunities);

    @SqlQuery("select * from opportunity where closed = false order by published desc")
    List<Opportunity> findAllOpen();

    @SqlQuery("select * from opportunity where closed = false and alerted = false order by published desc")
    List<Opportunity> findAllOpenAndUnalerted();

    @SqlUpdate("update opportunity set alerted = true where id = :id")
    void markAlerted(@Bind("id") long id);

    @SqlUpdate("update opportunity set alerted = false where id = :id")
    void markUnalerted(@Bind("id") long id);

    @SqlQuery("select * from opportunity where id = :id")
    Opportunity findById(@Bind("id") long id);

    @SqlUpdate("update opportunity set duration = :duration where id = :id")
    void setOpportunityDuration(@Bind("duration") int duration, @Bind("id") long id);

    @SqlUpdate("update opportunity set cost = :cost where id = :id")
    void setOpportunityCost(@Bind("cost") int cost, @Bind("id") long id);

    @SqlUpdate("insert into bidmanager_session (opportunity, key) values (:opportunity, :key)")
    void insertKey(@Bind("opportunity") Long opportunityId, @Bind("key") String key);

    @SqlQuery("select count(*) from bidmanager_session where opportunity = :opportunity and key = :key")
    int findKey(@Bind("opportunity") Long opportunityId, @Bind("key") String key);

}

package uk.andrewgorton.digitalmarketplace.alerter.dao.mappers;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import uk.andrewgorton.digitalmarketplace.alerter.Opportunity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OpportunityMapper implements ResultSetMapper<Opportunity> {
    @Override
    public Opportunity map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Opportunity o = new Opportunity();
        o.setId(resultSet.getLong("id"));
        o.setTitle(resultSet.getString("title"));
        o.setOpportunityType(resultSet.getString("type"));
        o.setLocation(resultSet.getString("location"));
        o.setCustomer(resultSet.getString("customer"));
        o.setPublished(resultSet.getTimestamp("published"));
        o.setClosing(resultSet.getTimestamp("closing"));
        o.setClosed(resultSet.getBoolean("closed"));
        o.setExcerpt(resultSet.getString("excerpt"));
        o.setUrl(resultSet.getString("url"));
        o.setFirstSeen(resultSet.getTimestamp("firstSeen"));
        o.setLastUpdated(resultSet.getTimestamp("lastUpdated"));
        o.setAlerted(resultSet.getBoolean("alerted"));
        o.setDuration(resultSet.getInt("duration"));
        o.setCost(resultSet.getInt("cost"));
        return o;
    }
}
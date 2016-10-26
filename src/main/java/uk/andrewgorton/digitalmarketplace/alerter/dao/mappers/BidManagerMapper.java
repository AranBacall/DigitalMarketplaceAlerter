package uk.andrewgorton.digitalmarketplace.alerter.dao.mappers;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import uk.andrewgorton.digitalmarketplace.alerter.model.BidManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BidManagerMapper implements ResultSetMapper<BidManager> {
    @Override
    public BidManager map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new BidManager(resultSet.getLong("id"), resultSet.getString("email"));
    }
}

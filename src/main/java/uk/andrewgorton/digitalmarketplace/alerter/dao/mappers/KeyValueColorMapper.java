package uk.andrewgorton.digitalmarketplace.alerter.dao.mappers;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import uk.andrewgorton.digitalmarketplace.alerter.report.KeyValueColorItem;

import java.sql.ResultSet;
import java.sql.SQLException;

public class KeyValueColorMapper implements ResultSetMapper<KeyValueColorItem> {
    @Override
    public KeyValueColorItem map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new KeyValueColorItem(resultSet.getString("group_name"), resultSet.getLong("occurrences"));
    }
}

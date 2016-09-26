package uk.andrewgorton.digitalmarketplace.alerter.dao.mappers;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import uk.andrewgorton.digitalmarketplace.alerter.Alert;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AlertMapper implements ResultSetMapper<Alert> {
    @Override
    public Alert map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Alert(resultSet.getLong("id"),
                resultSet.getString("email"),
                resultSet.getString("customerRegex"),
                resultSet.getBoolean("enabled"));
    }
}

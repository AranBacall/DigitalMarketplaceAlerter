package uk.andrewgorton.digitalmarketplace.alerter.dao.mappers;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import uk.andrewgorton.digitalmarketplace.alerter.model.Response;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResponseMapper implements ResultSetMapper<Response> {
    @Override
    public Response map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Response(resultSet.getLong("id"), resultSet.getString("reason"), resultSet.getString("reason"));
    }
}

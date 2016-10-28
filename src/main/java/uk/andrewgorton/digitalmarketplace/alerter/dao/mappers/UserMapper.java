package uk.andrewgorton.digitalmarketplace.alerter.dao.mappers;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import uk.andrewgorton.digitalmarketplace.alerter.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements ResultSetMapper<User> {
    @Override
    public User map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new User(resultSet.getLong("id"),
                resultSet.getString("username"),
                resultSet.getString("email"),
                resultSet.getString("salt"),
                resultSet.getString("password"),
                resultSet.getBoolean("disabled"),
                resultSet.getBoolean("admin") );
    }
}

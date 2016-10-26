package uk.andrewgorton.digitalmarketplace.alerter.dao.mappers;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import uk.andrewgorton.digitalmarketplace.alerter.User;
import uk.andrewgorton.digitalmarketplace.alerter.report.PieChartData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by koskinasm on 25/10/2016.
 */
public class PieChartMapper

    implements ResultSetMapper<PieChartData> {
    @Override
    public PieChartData map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new PieChartData(resultSet.getString("group_name"), resultSet.getLong("occurrences"));
    }
}

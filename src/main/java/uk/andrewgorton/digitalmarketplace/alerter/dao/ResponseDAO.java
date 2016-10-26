package uk.andrewgorton.digitalmarketplace.alerter.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import uk.andrewgorton.digitalmarketplace.alerter.dao.mappers.ResponseMapper;
import uk.andrewgorton.digitalmarketplace.alerter.model.Response;

import java.util.List;

@RegisterMapper(ResponseMapper.class)
public interface ResponseDAO {

    @SqlQuery("select * from response")
    List<Response> findAll();

    @SqlUpdate("insert into opportunity_response (opportunity, response) " +
            "values(:opportunity, :response)")
    void link(@Bind("opportunity") Long opportunityId, @Bind("response") Long responseId);
}

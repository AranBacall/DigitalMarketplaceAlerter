package uk.andrewgorton.digitalmarketplace.alerter.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import uk.andrewgorton.digitalmarketplace.alerter.model.BidManager;
import uk.andrewgorton.digitalmarketplace.alerter.dao.mappers.BidManagerMapper;

import java.util.List;

@RegisterMapper(BidManagerMapper.class)
public interface BidManagerDAO {

    @SqlQuery("select * from bidmanager")
    List<BidManager> findAll();

    @SqlUpdate("delete from bidmanager where id = :id")
    void delete(@Bind("id") long id);

    @GetGeneratedKeys
    @SqlUpdate("insert into bidmanager (email) values(:email)")
    long insert(@Bind("email") String email);
}

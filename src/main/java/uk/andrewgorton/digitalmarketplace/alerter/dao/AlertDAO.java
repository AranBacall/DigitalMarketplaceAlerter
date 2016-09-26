package uk.andrewgorton.digitalmarketplace.alerter.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import uk.andrewgorton.digitalmarketplace.alerter.Alert;
import uk.andrewgorton.digitalmarketplace.alerter.dao.mappers.AlertMapper;

import java.util.List;

@RegisterMapper(AlertMapper.class)
public interface AlertDAO {
    @SqlQuery("select * from alert")
    List<Alert> findAll();

    @SqlQuery("select * from alert where enabled = true")
    List<Alert> findAllEnabled();

    @SqlUpdate("update alert set enabled = true where id = :id")
    void enable(@Bind("id") long id);

    @SqlUpdate("update alert set enabled = false where id = :id")
    void disable(@Bind("id") long id);

    @SqlUpdate("delete from alert where id = :id")
    void delete(@Bind("id") long id);

    @SqlUpdate("insert into alert (email, customerRegex, enabled) " +
            "values(:email, :customerMatchRegex, :enabled)")
    @GetGeneratedKeys
    long insert(@Bind("email") String email,
                @Bind("customerMatchRegex") String customerMatchRegex,
                @Bind("enabled") boolean enabled);
}

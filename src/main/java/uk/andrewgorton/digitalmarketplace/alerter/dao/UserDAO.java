package uk.andrewgorton.digitalmarketplace.alerter.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import uk.andrewgorton.digitalmarketplace.alerter.User;
import uk.andrewgorton.digitalmarketplace.alerter.dao.mappers.UserMapper;

import java.util.List;

@RegisterMapper(UserMapper.class)
public interface UserDAO {

    @SqlQuery("select * from user")
    List<User> findAll();

    @SqlQuery("select * from user where username = :username")
    User findByUsername(@Bind("username") String username);

    @SqlQuery("select * from user where id = :id")
    User findById(@Bind("id") Long id);

    @SqlUpdate("update user set salt = :salt, password = :password where username = :username")
    void updateUserCredentials(@Bind("username") String username,
                               @Bind("salt") String salt,
                               @Bind("password") String password);


    @SqlUpdate("insert into user (username,salt,password,disabled,admin) values (:username,:salt,:password, false, false)")
    void createNewUser(@Bind("username") String username,
                               @Bind("salt") String salt,
                               @Bind("password") String password);


    @SqlUpdate("update user set user.admin = true where user.id = :id")
    void promoteUserToAdministrator(@Bind("id") Long id);

    @SqlUpdate("update user set user.disabled = :disabled where user.id = :id")
    void updateUserEnablement(@Bind("id") Long id, @Bind("disabled") boolean disabled);

    @SqlUpdate("delete from user where id = :id")
    void delete(@Bind("id") Long id);

    void close();
}

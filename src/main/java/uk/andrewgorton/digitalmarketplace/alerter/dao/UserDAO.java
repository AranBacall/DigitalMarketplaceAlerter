package uk.andrewgorton.digitalmarketplace.alerter.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import uk.andrewgorton.digitalmarketplace.alerter.User;
import uk.andrewgorton.digitalmarketplace.alerter.dao.mappers.UserMapper;

@RegisterMapper(UserMapper.class)
public interface UserDAO {
    @SqlQuery("select * from user where username = :username")
    User findByUsername(@Bind("username") String username);

    @SqlUpdate("update user set salt = :salt, password = :password where username = :username")
    void updateUserCredentials(@Bind("username") String username,
                               @Bind("salt") String salt,
                               @Bind("password") String password);


    @SqlUpdate("insert into user (username,salt,password,disabled) values (:username,:salt,:password, false)")
    void createNewUser(@Bind("username") String username,
                               @Bind("salt") String salt,
                               @Bind("password") String password);

    void close();
}

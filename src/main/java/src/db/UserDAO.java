package src.db;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import src.models.User;
import src.db.mappers.UserMapper;

@RegisterRowMapper(UserMapper.class)
public interface UserDAO {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS users (" +
            "id SERIAL PRIMARY KEY," +
            "username VARCHAR(64) UNIQUE NOT NULL," +
            "email VARCHAR(64) UNIQUE NOT NULL," +
            "password VARCHAR(64) NOT NULL," +
            "privileges INT DEFAULT 0)")
    void createTable();

    @SqlQuery("SELECT password FROM users WHERE username = :username")
    String getPasswordFromUsername(@Bind("username") String username);

    @SqlUpdate("INSERT INTO users (username, email, password) VALUES (:username, :email, :password)")
    @GetGeneratedKeys("id")
    int createUser(@Bind("username") String username, @Bind("email") String email, @Bind("password") String password);

    @SqlQuery("SELECT * FROM users WHERE username = :username")
    User getUserFromUsername(@Bind("username") String username);

    @SqlQuery("SELECT * FROM users WHERE id = :id")
    User getUserFromId(@Bind("id") int id);

    @SqlUpdate("UPDATE users SET privileges = :privileges WHERE id = :id")
    void changeUserPrivileges(@Bind("privileges") int privileges, @Bind("id") int id);

    @SqlUpdate("UPDATE users SET password = :password WHERE id = :id")
    void updateUserPassword(@Bind("password") String password, @Bind("id") int id);

}

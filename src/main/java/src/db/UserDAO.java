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
            "streetAddress VARCHAR(64) NOT NULL," +
            "postalCode VARCHAR(64) NOT NULL," +
            "province VARCHAR(64) NOT NULL," +
            "privileges INT DEFAULT 0)")
    void createTable();

    @SqlQuery("SELECT password FROM users WHERE username = :username")
    String getPasswordFromUsername(@Bind("username") String username);

    @SqlUpdate("INSERT INTO users (username, email, password, streetAddress, postalCode, province) " +
            "VALUES (:username, :email, :password, :streetAddress, :postalCode, :province)")
    @GetGeneratedKeys("id")
    int createUser(@Bind("username") String username, @Bind("email") String email, @Bind("password")
            String password, @Bind("streetAddress") String streetAddress, @Bind("postalCode") String postalCode, @Bind("province") String province);

    @SqlQuery("SELECT * FROM users WHERE username = :username")
    User getUserFromUsername(@Bind("username") String username);

    @SqlQuery("SELECT * FROM users WHERE id = :id")
    User getUserFromId(@Bind("id") int id);

    @SqlUpdate("UPDATE users SET email = :email," +
            "streetAddress = :streetAddress, postalCode = :postalCode, province = :province WHERE id = :id")
    boolean editProfile(@Bind("id") int id, @Bind("email") String email,
                        @Bind("streetAddress") String streetAddress, @Bind("postalCode") String postalCode,
                        @Bind("province") String province);
}

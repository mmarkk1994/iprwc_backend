package src.db;

import src.models.Order;
import src.db.mappers.OrderMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.ArrayList;

@RegisterRowMapper(OrderMapper.class)
public interface OrderDAO {
    @SqlUpdate("CREATE TABLE IF NOT EXISTS user_orders (" +
            "id SERIAL PRIMARY KEY," +
            "product_id INT," +
            "user_id INT," +
            "order_date TIMESTAMP DEFAULT NOW() NOT NULL," +
            "FOREIGN KEY(product_id) REFERENCES product(id) ON DELETE CASCADE," +
            "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE);")
    void createTable();

    @SqlUpdate("INSERT INTO user_orders(product_id, user_id) VALUES(:product_id, :user_id)")
    boolean createOrder(@Bind("product_id") int product_id, @Bind("user_id") int user_id);

    @SqlQuery("SELECT o.id, u.username, p.album, o.order_date FROM users u " +
            "JOIN user_orders o ON u.id = o.user_id JOIN product p" +
            " ON o.product_id = p.id")
    ArrayList<Order> getOrders();

    @SqlQuery("SELECT o.id, u.username, p.album, o.order_date FROM users u " +
            "JOIN user_orders o ON u.id = o.user_id JOIN product p" +
            " ON o.product_id = p.id WHERE u.id = :id")
    ArrayList<Order> getUserOrders(@Bind("id") int id);
}

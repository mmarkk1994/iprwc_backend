package src.db;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import src.models.Product;
import src.db.mappers.ProductMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterRowMapper(ProductMapper.class)
public interface ProductDAO {
    @SqlUpdate("CREATE TABLE IF NOT EXISTS product(" +
            "id SERIAL PRIMARY KEY,"+
            "album VARCHAR(256) UNIQUE NOT NULL,"+
            "description VARCHAR(256) NOT NULL,"+
            "image TEXT NOT NULL,"+
            "price NUMERIC(11,2) NOT NULL);")
    void createTable();

    @SqlQuery("SELECT * FROM product")
    List<Product> getAllProducts();

    @SqlUpdate("INSERT INTO product(album, description, image, price) VALUES (:album, :description, :image, :price);")
    @GetGeneratedKeys("id")
    int addProduct(@Bind("album") String album,
                   @Bind("description") String description,
                   @Bind("image") String image,
                   @Bind("price") double price);
}

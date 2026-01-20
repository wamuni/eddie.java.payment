package eddie.payment.orders.product;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcProductRepository implements ProductRepository {
	
	private final JdbcTemplate jdbc;

	public JdbcProductRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public Product save(String sku, String name, java.math.BigDecimal priceAmount, String currency, boolean active) {
		var sql = """
			insert into products (sku, name, price_amount, currency, active)
			values (?, ?, ?, ?, ?)
			returning id, sku, name, price_amount, currency, active, create_at
		""";
		return jdbc.queryForObject(sql, productRowMapper(), sku, name, priceAmount, currency, active);
	}

	@Override
	public Optional<Product> findById(long id) {
		var sql = """
			select id, sku, name, price_amount, currency, active, create_at
			from products
			where id = ?
		""";
		return jdbc.query(sql, productRowMapper(), id).stream().findFirst();
	}

	@Override
	public List<Product> findAll(int limit, int offset) {
		var sql = """
			select id, sku, name, price_amount, currency, active, create_at
			from products
			order by id
			limit ?
			offset ?
		""";
		return jdbc.query(sql, productRowMapper(), limit, offset);
	}

	private RowMapper<Product> productRowMapper() {
		return (ResultSet rs, int rowNum) -> new Product(
			rs.getLong("id"),
			rs.getString("sku"),
			rs.getString("name"),
			rs.getBigDecimal("price_amount"),
			rs.getString("currency"),
			rs.getBoolean("active"),
			rs.getObject("create_at", java.time.OffsetDateTime.class)
		);
	}
}

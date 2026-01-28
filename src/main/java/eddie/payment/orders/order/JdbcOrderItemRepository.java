package eddie.payment.orders.order;

import java.sql.ResultSet;
import java.util.List;
import java.math.BigDecimal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcOrderItemRepository implements OrderItemRepository {

	private final JdbcTemplate jdbc;

	public JdbcOrderItemRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public OrderItem insert(long orderId, long productId, int quantity, BigDecimal unitPriceAmount, BigDecimal lineAmount) {
		var sql = """
			insert into order_items (order_id, product_id, quantity, unit_price_amount, line_amount)
			values (?, ?, ?, ?, ?)
			returning id, order_id, product_id, quantity, unit_price_amount, line_amount
		""";
		return jdbc.queryForObject(sql, rowMapper(), orderId, productId, quantity, unitPriceAmount, lineAmount);
	}

	@Override
	public List<OrderItem> findByOrderId(long orderId) {
		var sql = """
			select id, order_id, product_id, quantity, unit_price_amount, line_amount
			from order_items
			where order_id = ?
			order by id
		""";
		return jdbc.query(sql, rowMapper(), orderId);
	}

	private RowMapper<OrderItem> rowMapper() {
		return (ResultSet rs, int rowNum) -> new OrderItem(
			rs.getLong("id"),
			rs.getLong("order_id"),
			rs.getLong("product_id"),
			rs.getInt("quantity"),
			rs.getBigDecimal("unit_price_amount"),
			rs.getBigDecimal("line_amount")
		);
	}

}

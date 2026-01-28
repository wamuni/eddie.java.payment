package eddie.payment.orders.order;

import java.sql.ResultSet;
import java.util.Optional;
import java.math.BigDecimal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcOrderRepository implements OrderRepository {

	private final JdbcTemplate jdbc;

	public JdbcOrderRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public OrderHeader insert(long customerId, OrderStatus status, String currency, BigDecimal totalAmount) {
		var sql = """
			insert into orders (customer_id, status, currency, total_amount)
			values (?, ?, ?, ?)
			returning id, customer_id, status, currency, total_amount, create_at
		""";
		return jdbc.queryForObject(sql, headerMapper(), customerId, status.name(), currency, totalAmount);
	}
	
	@Override
	public Optional<OrderHeader> findHeaderById(long orderId) {
		var sql = """
			select id, customer_id, status, currency, total_amount, create_at
			from orders
			where id = ?
		""";
		return jdbc.query(sql, headerMapper(), orderId).stream().findFirst();
	}

	private RowMapper<OrderHeader> headerMapper() {
		return (ResultSet rs, int rowNum) -> new OrderHeader(
			rs.getLong("id"),
			rs.getLong("customer_id"),
			OrderStatus.valueOf(rs.getString("status")),
			rs.getString("currency"),
			rs.getBigDecimal("total_amount"),
			rs.getObject("create_at", java.time.OffsetDateTime.class)
		);
	}

}

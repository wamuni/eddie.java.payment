package eddie.payment.orders.payment;

import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Optional;
import java.util.List;
import java.sql.ResultSet;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Repository
public class JdbcPaymentRepository implements PaymentRepository {

	private final JdbcTemplate jdbc;

	public JdbcPaymentRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public Optional<Payment> findByOrderAndKey(long orderId, String idempotencyKey) {
		var sql = """
			select id, order_id, idempotency_key, status, amount, currency, method, failure_reason, created_at, updated_at
			from payments
			where order_id = ? and idempotency_key = ?
		""";
		return jdbc.query(sql, mapper(), orderId, idempotencyKey).stream().findFirst();
	}
	
	@Override
	public Payment insertPending(long orderId, String idempotencyKey, BigDecimal amount, String currency, String method) {
		var sql = """
			insert into payments (order_id, idempotency_key, status, amount, currency, method)
			values (?, ?, ?, ?, ?, ?)
			returning id, order_id, idempotency_key, status, amount, currency, method, failure_reason, created_at, updated_at
		""";
		return jdbc.queryForObject(sql, mapper(), orderId, idempotencyKey, PaymentStatus.PENDING.name(), amount, currency, method);
	}
	
	@Override
	public Payment updateResult(long paymentId, PaymentStatus status, String failureReason) {
		var sql = """
			update payments
			set status = ?, failure_reason = ?, updated_at = now()
			where id = ?
			returning id, order_id, idempotency_key, status, amount, currency, method, failure_reason, created_at, updated_at
		""";
		return jdbc.queryForObject(sql, mapper(), status.name(), failureReason, paymentId);
	}
	
	@Override
	public Optional<Payment> findById(long paymentId) {
		var sql = """
			select id, order_id, idempotency_key, status, amount, currency, method, failure_reason, created_at, updated_at
			from payments
			where id = ?
		""";
		return jdbc.query(sql, mapper(), paymentId).stream().findFirst();
	}

	@Override
	public List<Payment> findByOrderId(long orderId, int limit, int offset) {
		var sql = """
			select id, order_id, idempotency_key, status, amount, currency, method, failure_reason, created_at, updated_at
			from payments
			where order_id = ?
			order by id
			limit ? offset ?
		""";
		return jdbc.query(sql, mapper(), orderId, limit, offset);
	}

	private RowMapper<Payment> mapper() {
		return (ResultSet rs, int rowNum) -> new Payment(
			rs.getLong("id"),
			rs.getLong("order_id"),
			rs.getString("idempotency_key"),
			PaymentStatus.valueOf(rs.getString("status")),
			rs.getBigDecimal("amount"),
			rs.getString("currency"),
			rs.getString("method"),
			rs.getString("failure_reason"),
			rs.getObject("created_at", OffsetDateTime.class),
			rs.getObject("updated_at", OffsetDateTime.class)
		);
	}


}

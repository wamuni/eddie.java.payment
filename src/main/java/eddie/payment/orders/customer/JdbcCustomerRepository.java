package eddie.payment.orders.customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcCustomerRepository implements CustomerRepository {
	private final JdbcTemplate jdbc;

	public JdbcCustomerRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public Customer save(String email, String name) {
		var sql = """
			insert into customers (email, name)
			values (?, ?)
			returning id, email, name, create_at
		""";
		return jdbc.queryForObject(sql, customerRowMapper(), email, name);
	}

	@Override
	public Optional<Customer> findById(long id) {
		var sql = """
			select id, email, name, create_at
			from customers
			where id = ?
		""";
		return jdbc.query(sql, customerRowMapper(), id)
			.stream()
			.findFirst();
	}
	
	@Override
	public List<Customer> findAll(int limit, int offset) {
		var sql = """
			select id, email, name, create_at
			from customers
			order by id
			limit ?
			offset ?
		""";
		return jdbc.query(sql, customerRowMapper(), limit, offset);
	}

	@Override
	public Optional<Customer> update(long id, String email, String name) {
		var sql = """
			update customers
			set email = ?, name = ?
			where id = ?
			returning id, email, name, create_at
		""";
		return jdbc.query(sql, customerRowMapper(), email, name, id)
			.stream()
			.findFirst();
	}

	private RowMapper<Customer> customerRowMapper() {
		return (ResultSet rs, int rowNum) -> new Customer(
			rs.getLong("id"),
			rs.getString("email"),
			rs.getString("name"),
			rs.getObject("create_at", java.time.OffsetDateTime.class)
		);
	}
}

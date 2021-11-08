package sk.upjs.paz1c.main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class MysqlIngredientDao implements IngredientDao {

	JdbcTemplate jdbcTemplate;

	public MysqlIngredientDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Ingredient> getAll() {
		String sql = "SELECT id, name, price, amount, amount_availiable FROM ingredient";
		return jdbcTemplate.query(sql, new RowMapper<>() {

			@Override
			public Ingredient mapRow(ResultSet rs, int rowNum) throws SQLException {
				long id = rs.getLong("id");
				String name = rs.getString("name");
				double price = rs.getDouble("price");
				String amount = rs.getString("amount");
				String amountAvailiable = rs.getString("amount_availiable");
				return new Ingredient(id, name, price, amount, amountAvailiable);
			}
		});
	}

	@Override
	public Ingredient save(Ingredient ingredient) {
		if (ingredient.getId() == null) { // insert
			SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
			insert.withTableName("ingredient");
			insert.usingGeneratedKeyColumns("id");
			insert.usingColumns("name", "price", "amount", "amount_availiable");

			Map<String, Object> values = new HashMap<>();
			values.put("name", ingredient.getName());
			values.put("price", ingredient.getPrice());
			values.put("amount", ingredient.getAmount());
			return new Ingredient(insert.executeAndReturnKey(values).longValue(), ingredient.getName(),
					ingredient.getPrice(), ingredient.getAmount(), ingredient.getAmountAvailiable());
		} else { // update
			String sql = "UPDATE ingredient SET name = ?, price = ?, amount = ?, amount_availiable = ? WHERE id = ?";
			int changedCount = jdbcTemplate.update(sql, ingredient.getName(), ingredient.getPrice(),
					ingredient.getAmount(), ingredient.getAmountAvailiable());
			if (changedCount == 1)
				return ingredient;
			else
				throw new EntityNotFoundException("Ingredient with ID: " + ingredient.getId() + " not found in DB!");
		}
	}

	@Override
	public Ingredient delete(long idIgredient) {
		// TODO Auto-generated method stub
		return null;
	}

}

package sk.upjs.paz1c.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class MysqlIngredientDao implements IngredientDao{

	private JdbcTemplate jdbcTemplate;

	public MysqlIngredientDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Ingredient> getAll() {
		String sql = "SELECT id, name, price, amount, amount_availiable FROM ingredient";
		return jdbcTemplate.query(sql, new IngredientRowMapper());
	}

	@Override
	public Ingredient save(Ingredient ingredient) throws EntityNotFoundException {
		if (ingredient.getId() == null) { // insert
			SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
			insert.withTableName("ingredient");
			insert.usingGeneratedKeyColumns("id");
			insert.usingColumns("name", "price", "amount", "amount_availiable");

			Map<String, Object> values = new HashMap<>();
			values.put("name", ingredient.getName());
			values.put("price", ingredient.getPrice());
			values.put("amount", ingredient.getAmount());
			values.put("amount_availiable", ingredient.getAmountAvailiable());
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
	public Ingredient delete(long idIngredient) throws EntityUndeletableException {
		Ingredient ingr = getById(idIngredient);
		try {
			String sql = "DELETE FROM ingredient WHERE id = ?";
			jdbcTemplate.update(sql, idIngredient );
		} catch (DataIntegrityViolationException e) {
			throw new EntityUndeletableException(
					"Ingredient is needed for some food: can not be deleted", e);
		}
		return ingr;
	}

	@Override
	public Ingredient getById(long idIngredient) throws EntityNotFoundException { 
		String sql = "SELECT id, name, price, amount, amount_availiable FROM ingredient WHERE id = ? ";
		try {
			return jdbcTemplate.queryForObject(sql, new IngredientRowMapper(), idIngredient);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException("Ingredient with id " 
					+ idIngredient + " not found in DB!", e);
		}
	}
	
	private class IngredientRowMapper implements RowMapper<Ingredient> {
		@Override
		public Ingredient mapRow(ResultSet rs, int rowNum) throws SQLException {
			long id = rs.getLong("id");
			String name = rs.getString("name");
			Double price = rs.getDouble("price");
			String amount = rs.getString("amount");
			String amountAvailiable = rs.getString("amount_availiable");
			return new Ingredient(id, name, price, amount, amountAvailiable);
		}
	}

}

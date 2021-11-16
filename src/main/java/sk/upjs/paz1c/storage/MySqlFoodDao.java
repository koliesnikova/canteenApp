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

public class MySqlFoodDao implements FoodDao {

	private JdbcTemplate jdbcTemplate;

	public MySqlFoodDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Map<Ingredient, Integer> saveIngredient(Food food, Ingredient ingredient, Integer amount) throws EntityNotFoundException{
		Map<Ingredient, Integer> map = food.getIngredients();
		if (map.containsKey(ingredient)) {
			// UPDATE
			String sql = "UPDATE food_ingredients SET amount_needed = ? WHERE food_id = ? AND ingredient_id = ?";
			int changedCount = jdbcTemplate.update(sql, amount, food.getId(), ingredient.getId());
			if (changedCount == 1) {
				map.put(ingredient, amount);
				food.setIngredients(map);
				return map;
			}
			throw new EntityNotFoundException("Food or ingredient is not in DB: operation failed.");
		} else {
			// INSERT
			String sql = "INSERT INTO food_ingredients (`food_id`,`ingredient_id`,`amount_needed`) VALUES (?, ? ,?)";
			int changedRows = jdbcTemplate.update(sql, food.getId(), ingredient.getId(), amount);
			if (changedRows == 1) {
				map.put(ingredient, amount);
				food.setIngredients(map);
				return map;
			}
			throw new EntityNotFoundException("Food or ingredient is not in DB: operation failed.");
		}

	}
	
	@Override
	public Food deleteIngredient(Food food, Ingredient ingredient) throws EntityNotFoundException{
		String sql = "DELETE FROM food_ingredients WHERE food_id = ? AND ingredient_id = ?";
		int changedRows = jdbcTemplate.update(sql, food.getId(), ingredient.getId());
		if(changedRows==1) {
			food.setIngredients(null);
			return food;
		}
		throw new EntityNotFoundException("Food or ingredient not found: operation failed.");		
	}

	@Override
	public List<Food> getAll() {
		String sql = "SELECT id, name, description, price, image_url, weight FROM food";
		return jdbcTemplate.query(sql, new FoodRowMapper());
	}

	@Override
	public Food save(Food food) throws EntityNotFoundException {
		if (food.getId() == null) { // insert
			SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
			insert.withTableName("food");
			insert.usingGeneratedKeyColumns("id");
			insert.usingColumns("name", "price", "description", "image_url", "weight");

			Map<String, Object> values = new HashMap<>();
			values.put("name", food.getName());
			values.put("price", food.getPrice());
			values.put("description", food.getDescription());
			values.put("image_url", food.getImage_url());
			values.put("weight", food.getWeight());
			return new Food(insert.executeAndReturnKey(values).longValue(), food.getName(), food.getDescription(),
					food.getImage_url(), food.getPrice(), food.getWeight());
		} else { // update
			String sql = "UPDATE food SET name = ?, description = ?, image_url = ?, price = ?,weight = ? WHERE id = ?";
			int changedCount = jdbcTemplate.update(sql, food.getName(), food.getDescription(), food.getImage_url(),
					food.getPrice(), food.getWeight(), food.getId());
			if (changedCount == 1)
				return food;
			else
				throw new EntityNotFoundException("Food with ID: " + food.getId() + " not found in DB!");
		}
	}

	@Override
	public Food delete(long idFood) throws EntityUndeletableException {
		Food food = getById(idFood);
		try {
			String sql = "DELETE FROM food WHERE id = ?";
			jdbcTemplate.update(sql, idFood);
		} catch (DataIntegrityViolationException e) {
			throw new EntityUndeletableException("Food can not be deleted", e);
		}
		return food;
	}

	@Override
	public Food getById(long idFood) throws EntityNotFoundException {
		String sql = "SELECT id, name, description, price, image_url, weight FROM food WHERE id = ? ";
		try {
			return jdbcTemplate.queryForObject(sql, new FoodRowMapper(), idFood);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException("Food with id " + idFood + " not found!", e);
		}
	}

	private class FoodRowMapper implements RowMapper<Food> {
		@Override
		public Food mapRow(ResultSet rs, int rowNum) throws SQLException {
			long id = rs.getLong("id");
			String name = rs.getString("name");
			Double price = rs.getDouble("price");
			String description = rs.getString("description");
			String image_url = rs.getString("image_url");
			Integer weight = rs.getInt("weight");
			return new Food(id, name, description, image_url, price, weight);
		}
	}

}

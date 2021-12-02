package sk.upjs.paz1c.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class MySqlFoodDao implements FoodDao {

	private JdbcTemplate jdbcTemplate;
	private IngredientDao ingredientDao = DaoFactory.INSTANCE.getIngredientDao();

	public MySqlFoodDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Food> getFoodsInOrders() {
		String sql = "select * from food f LEFT JOIN food_ingredients fi on f.id = fi.food_id \r\n"
				+ "where f.id in (select food_id from daily_orders where f.id = food_id)  ORDER BY f.id";
		return jdbcTemplate.query(sql, new ResultSetExtractor<List<Food>>() {
			@Override
			public List<Food> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Food> result = new ArrayList<>();
				Food food = null;
				while (rs.next()) {
					Long id = rs.getLong("id");
					if (food == null || food.getId() != id) {
						String name = rs.getString("name");
						Double price = rs.getDouble("price");
						String description = rs.getString("description");
						String image_url = rs.getString("image_url");
						Integer weight = rs.getInt("weight");
						Long idIngredient = rs.getLong("ingredient_id");
						Integer amount = rs.getInt("amount_needed");
						if (idIngredient == 0 && amount == 0) {
							food = new Food(id, name, description, image_url, price, weight,
									new HashMap<Ingredient, Integer>());
							result.add(food);
						} else {
							Ingredient i = ingredientDao.getById(idIngredient);
							Map<Ingredient, Integer> map = new HashMap<Ingredient, Integer>();
							map.put(i, amount);
							food = new Food(id, name, description, image_url, price, weight, map);
							result.add(food);
						}
					} else {
						result.remove(food);
						Map<Ingredient, Integer> map = food.getIngredients();
						Long idIngredient = rs.getLong("ingredient_id");
						Integer amount = rs.getInt("amount_needed");
						Ingredient i = ingredientDao.getById(idIngredient);
						map.put(i, amount);
						food.setIngredients(map);
						result.add(food);
					}
				}
				return result;
			}
		});
	}

	@Override
	public Food saveIngredientToFood(Food food, Ingredient ingredient, Integer amount) throws EntityNotFoundException {
		Map<Ingredient, Integer> map = food.getIngredients();

		String sql1 = "DELETE FROM food_ingredients WHERE food_id = ? AND ingredient_id = ?";
		jdbcTemplate.update(sql1, food.getId(), ingredient.getId());

		if (amount == 0) {
			Set<Ingredient> ing = food.getIngredients().keySet();
			Ingredient toDelete = null;
			for (Ingredient ingr : ing) {
				if (ingr.getId().equals(ingredient.getId())) {
					toDelete = ingr;
					break;
				}
			}
			food.getIngredients().remove(toDelete);
			return food;
		}
		String sql = "INSERT INTO food_ingredients (`food_id`,`ingredient_id`,`amount_needed`) VALUES (?, ? ,?)";
		try {
			int changedRows = jdbcTemplate.update(sql, food.getId(), ingredient.getId(), amount);

			if (changedRows == 1) {
				Set<Ingredient> keys = map.keySet();
				ArrayList<Ingredient> toReplace = new ArrayList<Ingredient>();
				for (Ingredient ingredient2 : keys) {
					if (ingredient2.getId().equals(ingredient.getId())) {
						toReplace.add(ingredient2);
					}
				}
				for (Ingredient ingredient2 : toReplace) {
					map.remove(ingredient2);
					map.put(ingredient, amount);
				}
				map.put(ingredient, amount);
				food.setIngredients(map);
				return food;
			}
		} catch (DataIntegrityViolationException e) {
			throw new EntityNotFoundException("Food or ingredient is not in DB: operation failed.");
		}

		throw new EntityNotFoundException("Food or ingredient is not in DB: operation failed.");

	}

	@Override
	public Food deleteIngredient(Food food, Ingredient ingredient) throws EntityNotFoundException {
		String sql = "DELETE FROM food_ingredients WHERE food_id = ? AND ingredient_id = ?";
		int changedRows = jdbcTemplate.update(sql, food.getId(), ingredient.getId());
		if (changedRows == 1) {
			Map<Ingredient, Integer> map = food.getIngredients();
			map.remove(ingredient);
			food.setIngredients(map);
			return food;
		}
		throw new EntityNotFoundException("Food or ingredient not found: operation failed.");
	}

	@Override
	public List<Food> getAll() {
		String sql = "SELECT id, name, description, price, image_url, weight, ingredient_id, amount_needed"
				+ " FROM food f LEFT JOIN food_ingredients fi on f.id = fi.food_id ORDER BY f.id";
		return jdbcTemplate.query(sql, new ResultSetExtractor<List<Food>>() {
			@Override
			public List<Food> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Food> result = new ArrayList<>();
				Food food = null;
				while (rs.next()) {
					Long id = rs.getLong("id");
					if (food == null || food.getId() != id) { // new food
						String name = rs.getString("name");
						Double price = rs.getDouble("price");
						String description = rs.getString("description");
						String image_url = rs.getString("image_url");
						Integer weight = rs.getInt("weight");
						Long idIngredient = rs.getLong("ingredient_id");
						Integer amount = rs.getInt("amount_needed");
						if (idIngredient == 0 && amount == 0) {
							food = new Food(id, name, description, image_url, price, weight,
									new HashMap<Ingredient, Integer>());
							result.add(food);
						} else {
							Ingredient i = ingredientDao.getById(idIngredient);
							Map<Ingredient, Integer> map = new HashMap<Ingredient, Integer>();
							map.put(i, amount);
							food = new Food(id, name, description, image_url, price, weight, map);
							result.add(food);
						}
					} else { // same food
						result.remove(food);
						Map<Ingredient, Integer> map = food.getIngredients();
						Long idIngredient = rs.getLong("ingredient_id");
						Integer amount = rs.getInt("amount_needed");
						Ingredient i = ingredientDao.getById(idIngredient);
						map.put(i, amount);
						food.setIngredients(map);
						result.add(food);
					}
				}
				return result;
			}
		});
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

			Food newFood = new Food(insert.executeAndReturnKey(values).longValue(), food.getName(),
					food.getDescription(), food.getImage_url(), food.getPrice(), food.getWeight(),
					food.getIngredients());
			List<Ingredient> setAll = ingredientDao.getAll();
			Set<Ingredient> set = food.getIngredients().keySet();
			for (Ingredient ingredient : setAll) {
				boolean found = false;
				Ingredient fromSet = null;
				for (Ingredient ingredient2 : set) {
					if (ingredient.getId().equals(ingredient2.getId())) {
						found = true;
						fromSet = ingredient2;
						break;
					}
				}
				if (!found) {
					System.out.println("nuuull2 " + food.getIngredients() + ingredient);
					saveIngredientToFood(newFood, ingredientDao.getById(ingredient.getId()), 0);
				} else {
					saveIngredientToFood(newFood, ingredientDao.getById(ingredient.getId()),
							food.getIngredients().get(fromSet));
				}
			}

			return newFood;
		} else { // update
			// TODO when to throw entity not found exception?
			String sql = "UPDATE food SET name = ?, description = ?, image_url = ?, price = ?,weight = ? WHERE id = ?";
			jdbcTemplate.update(sql, food.getName(), food.getDescription(), food.getImage_url(), food.getPrice(),
					food.getWeight(), food.getId());

			List<Ingredient> setAll = ingredientDao.getAll();
			Set<Ingredient> set = food.getIngredients().keySet();

			for (Ingredient ingredient : setAll) {
				boolean found = false;
				Ingredient fromSet = null;
				for (Ingredient ingredient2 : set) {
					if (ingredient.getId().equals(ingredient2.getId())) {
						found = true;
						fromSet = ingredient2;
						break;
					}
				}
				if (!found) {
					System.out.println("nuuull " + food.getIngredients() + ingredient);
					saveIngredientToFood(food, ingredientDao.getById(ingredient.getId()), 0);
				} else {
					saveIngredientToFood(food, ingredientDao.getById(ingredient.getId()),
							food.getIngredients().get(fromSet));
				}
			}
		}
		return food;
	}

	@Override
	public Food delete(long idFood) throws EntityUndeletableException {
		Food food = getById(idFood);
		Set<Ingredient> ingrs = food.getIngredients().keySet();
		ArrayList<Ingredient> toDelete = new ArrayList<Ingredient>();
		for (Ingredient ingredient : ingrs) {
			toDelete.add(ingredient);
		}
		for (Ingredient ingredient : toDelete) {
			deleteIngredient(food, ingredient);
		}

		try {
			String sql = "DELETE FROM food WHERE id = ?";
			jdbcTemplate.update(sql, idFood);
		} catch (DataIntegrityViolationException e) {
			throw new EntityUndeletableException("Food can not be deleted: it is part of some order.", e);
		}
		return food;
	}

	@Override
	public Food getById(long idFood) throws EntityNotFoundException {
		List<Food> allFoods = getAll();
		for (Food f : allFoods) {
			if (f.getId().equals(idFood)) {
				return f;
			}
		}
		throw new EntityNotFoundException("Food with ID: " + idFood + " not found in DB!");

	}

}

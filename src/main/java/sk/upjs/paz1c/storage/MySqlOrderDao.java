package sk.upjs.paz1c.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;


public class MySqlOrderDao implements OrderDao{
	
	private JdbcTemplate jdbcTemplate;
	
	public MySqlOrderDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Order> getAll() {
		String sql = "SELECT id, day, ingredients_prepared, food_id, portions FROM `order` AS o LEFT JOIN daily_orders AS dayo ON  o.id = dayo.order_id";
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<List<Order>>() {

			@Override
			public List<Order> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Order> orders = new ArrayList<>();
				List<Food> foods = DaoFactory.INSTANCE.getFoodDao().getAll();
				Map<Long, Food> mappedFood = new HashMap<>();
				for (Food f : foods) {
					mappedFood.put(f.getId(), f);
				}
				
				Order order = null;
				while (rs.next()) {
					Long id = rs.getLong("id");
					if (order == null || order.getId() !=  id) {
						LocalDateTime day = rs.getTimestamp("day").toLocalDateTime();
						boolean prepared = rs.getBoolean("ingredients_prepared");
						order = new Order(id, day, prepared, new HashMap<>());
						orders.add(order);
					}
					Long foodId = rs.getLong("food_id");
					int portions = rs.getInt("portions");
					if (foodId != null && portions > 0)
						order.getPortions().put(mappedFood.get(foodId), portions);
				}
				return orders;
			}
			
		});
	}
	

	@Override
	public List<Order> getByPrepared(boolean prepared) {
		List<Order> allOrders = getAll();
		List<Order> filtred = new ArrayList<>();
		for (Order o : allOrders) {
			if (o.isPrepared() == prepared) {
				filtred.add(o);
			}
		}
		return filtred;
	}

	@Override
	public Order getById(long idOrder) throws EntityNotFoundException{
		List<Order> orders = getAll();
		for(Order o : orders) {
			if (o.getId().equals(idOrder)) {
				return o;
			}
		}
		throw new EntityNotFoundException("Order with ID: " + idOrder + " not found in DB!");
	}

	@Override
	public List<Order> getByDay(LocalDateTime day) throws EntityNotFoundException{
		List<Order> orders = getAll();
		ArrayList<Order> result = new ArrayList<Order>();
		for(Order o : orders) {
			if (o.getDay().equals(day)) {
				result.add(o);
			}
		}
		if(result.size()==0) {
			throw new EntityNotFoundException("Order for day: " + day + " not found in DB!");
		}
		return result;
		
	}

	@Override
	public Order save(Order order) throws EntityNotFoundException{
		if (order.getId() == null) { // insert
			SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
			insert.withTableName("`order`");
			insert.usingGeneratedKeyColumns("id");
			insert.usingColumns("day", "ingredients_prepared");

			Map<String, Object> values = new HashMap<>();
			values.put("day", order.getDay());
			values.put("ingredients_prepared", order.isPrepared());
			Order newOrder = new Order(insert.executeAndReturnKey(values).longValue(), order.getDay(), order.isPrepared(), order.getPortions());
			insertFoods(newOrder);
			return newOrder;
			
		} else { // update
			String sql = "UPDATE `order` SET day = ?, ingredients_prepared = ? WHERE id = ?";
			int changedCount = jdbcTemplate.update(sql, order.getDay(), order.isPrepared(), order.getId());
			if (changedCount == 1) {
				insertFoods(order);
				return order;
			}
			else
				throw new EntityNotFoundException("Order with ID: " + order.getId() + " not found in DB!");
		}
	}

	@Override
	public Order delete(long idOrder) throws EntityUndeletableException {
		Order order = getById(idOrder);
		try {
			jdbcTemplate.update("DELETE FROM `order` WHERE id = " + idOrder);
		} catch (DataIntegrityViolationException e) {
			throw new EntityUndeletableException("Order can't be deleted, has some portions on it's list!", e);
		}
		return order;
	}

	@Override
	public void insertFoods(Order order) {
		jdbcTemplate.update("delete from daily_orders where order_id = ?", order.getId());
		
		if (order.getPortions() != null && order.getPortions().keySet().size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO daily_orders (order_id, food_id, portions) VALUES ");

			List<Food> foods = new ArrayList<>(order.getPortions().keySet());
			
			for (int i = 0; i < foods.size(); i++) {
				sb.append("(" + order.getId() + ", " + foods.get(i).getId() + ", " + order.getPortions().get(foods.get(i)) + "),");
			}
			
			String insertSql = sb.substring(0, sb.length() - 1);
			jdbcTemplate.update(insertSql);
		}
	}
	
	public Order removeFood(Order order, Food food) {
		String sql = "DELETE FROM daily_orders WHERE order_id = ? AND food_id = ?";
		int changed = jdbcTemplate.update(sql, order.getId(), food.getId());
		if (changed == 1) {
			Map<Food, Integer> portions = order.getPortions();
			portions.remove(food);
			order.setPortions(portions);
			return order;
		}
		throw new EntityNotFoundException("Food: " + food.getName() + " is not on order's list!");
		
		
	}

}

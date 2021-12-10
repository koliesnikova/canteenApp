package sk.upjs.paz1c.biznis;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.Ingredient;
import sk.upjs.paz1c.storage.IngredientDao;
import sk.upjs.paz1c.storage.Order;
import sk.upjs.paz1c.storage.OrderDao;

public class OverviewManagerImpl implements OverviewManager {

	private OrderDao orderDao = DaoFactory.INSTANCE.getOrderDao();
	private IngredientDao ingredientDao = DaoFactory.INSTANCE.getIngredientDao();

	@Override
	public List<OrderFoodOverview> getAll(Long orderId) {
		if (orderId == null) {
			return new ArrayList<>();
		}
		List<OrderFoodOverview> result = new ArrayList<>();
		Order byId = orderDao.getById(orderId);
		for (Entry<Food, Integer> p : byId.getPortions().entrySet()) {
			result.add(new OrderFoodOverview(p.getKey().getId(), p.getKey().getName(), p.getValue(),
					p.getKey().getPrice()));
		}

		return result;
	}

	public Map<Long, Integer> getIngredientsToBuy(LocalDateTime date) {
		Map<Long, Integer> result = new HashMap<Long, Integer>();
		List<Order> ordersForDay = orderDao.getByDay(date);

		for (Order order : ordersForDay) {
			if (!order.isPrepared()) {
				HashMap<Long, Integer> allIngrsAvailable = new HashMap<Long, Integer>();
				Map<Food, Integer> foodsOrdered = order.getPortions();
				for (Food food : foodsOrdered.keySet()) {
					Map<Long, Integer> ingredientsInFoodId = food.getIngredientsById();
					for (Long ingredientId : ingredientsInFoodId.keySet()) {
						int available = 0;
						if (allIngrsAvailable.containsKey(ingredientId)) {
							available = allIngrsAvailable.get(ingredientId);
						} else {
							available = ingredientDao.getById(ingredientId).getAmountAvailiable();
						}
						int needed = ingredientsInFoodId.get(ingredientId) * foodsOrdered.get(food);
						if (available >= needed) {
							allIngrsAvailable.put(ingredientId, available - needed);
							ingredientsInFoodId.put(ingredientId, available - needed);
						} else {
							if (result.containsKey(ingredientId)) {
								result.put(ingredientId, result.get(ingredientId) + Math.abs(available - needed));
							} else {
								result.put(ingredientId, Math.abs(available - needed));
							}
						}
					}
				}
			}
		}
		System.out.println("manager " + result);
		return result;
	}
}

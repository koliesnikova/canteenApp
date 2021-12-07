package sk.upjs.paz1c.biznis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.FoodDao;
import sk.upjs.paz1c.storage.Ingredient;
import sk.upjs.paz1c.storage.IngredientDao;
import sk.upjs.paz1c.storage.Order;
import sk.upjs.paz1c.storage.OrderDao;

public class DefaultCanteenManager implements CanteenManager {

	private FoodDao foodDao = DaoFactory.INSTANCE.getFoodDao();
	private OrderDao orderDao = DaoFactory.INSTANCE.getOrderDao();
	private IngredientDao ingredientDao = DaoFactory.INSTANCE.getIngredientDao();

	@Override
	public List<Food> filterFoodNotInOrder(Long orderId) {
		Order order = orderDao.getById(orderId);
		List<Food> result = foodDao.getAll();

		for (Food f : order.getPortions().keySet()) {
			int idx = -1;
			for (Food foodFromAll : result) {
				if (f.getId().equals(foodFromAll.getId())) {
					idx = result.indexOf(foodFromAll);
					break;
				}
			}
			if (idx != -1)
				result.remove(idx);
		}
		return result;
	}

	public List<ShoppingListItemOverview> updateShoppingList(Order order) {
		ArrayList<ShoppingListItemOverview> result = new ArrayList<ShoppingListItemOverview>();
		Map<Food, Integer> foodsOrdered = order.getPortions();
		for (Food food : foodsOrdered.keySet()) {
			Map<Long, Integer> ingredientsInFoodId = food.getIngredientsById();
			for (Long ingredientId : ingredientsInFoodId.keySet()) {
				int available = ingredientDao.getById(ingredientId).getAmountAvailiable();
				int needed = ingredientsInFoodId.get(ingredientId) * foodsOrdered.get(food);
				if (available >= needed) {
					ingredientsInFoodId.put(ingredientId, available - needed);
				} else {
					result.add(new ShoppingListItemOverview(ingredientDao.getById(ingredientId),
							Math.abs(available - needed)));
				}
			}
		}
		return result;
	}

}

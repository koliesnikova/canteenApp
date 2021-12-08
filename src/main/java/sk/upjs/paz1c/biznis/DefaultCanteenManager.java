package sk.upjs.paz1c.biznis;

import java.time.LocalDateTime;
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

	public List<ShoppingListItemOverview> getItemsForShoppingList(LocalDateTime date) {
		// TODO
		OverviewManagerImpl manager = new OverviewManagerImpl();
		Map<Long, Integer> ingrsToBuy = manager.getIngredientsToBuy(date);

		ArrayList<ShoppingListItemOverview> result = new ArrayList<ShoppingListItemOverview>();

		for (Long ingredientId : ingrsToBuy.keySet()) {
			result.add(new ShoppingListItemOverview(ingredientDao.getById(ingredientId), ingrsToBuy.get(ingredientId)));
		}
		return result;
	}

	public List<ShoppingListItemOverview> getItemsForShoppingList(List<Order> orders) {
		// TODO
		ArrayList<ShoppingListItemOverview> result = new ArrayList<ShoppingListItemOverview>();
		Map<Long, Integer> ingrsToBuy = new HashMap<Long, Integer>();
		OverviewManagerImpl manager = new OverviewManagerImpl();
		List<LocalDateTime> dates = new ArrayList<LocalDateTime>();
		for (Order order : orders) {
			dates.add(order.getDay());
		}
		for (LocalDateTime dateTime : dates) {
			Map<Long, Integer> ingrs = manager.getIngredientsToBuy(dateTime);
			for (Long idIngr : ingrs.keySet()) {
				if (ingrsToBuy.containsKey(idIngr)) {
					ingrsToBuy.put(idIngr, ingrsToBuy.get(idIngr) + ingrs.get(idIngr));
				} else
					ingrsToBuy.put(idIngr, ingrs.get(idIngr));
			}
		}
		for (Long idIngr : ingrsToBuy.keySet()) {
			result.add(new ShoppingListItemOverview(ingredientDao.getById(idIngr), ingrsToBuy.get(idIngr)));
		}

		return result;
	}

	public int getNumberOfToBuy() {
		// returns correct size, the map ingrsToBuy does not contain true values to buy
		OverviewManagerImpl manager = new OverviewManagerImpl();
		Map<Long, Integer> ingrsToBuy = new HashMap<Long, Integer>();
		List<Order> allOrders = orderDao.getAll();
		for (Order order : allOrders) {
			ingrsToBuy.putAll(manager.getIngredientsToBuy(order.getDay()));
		}
		return ingrsToBuy.size();
	}

}

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
		Map<Long, Integer> allIngrs = new HashMap<Long, Integer>();

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
		System.out.println("default> " + result);
		System.out.println();
		return result;
	}

	public int getNumberOfToBuy() {
		return getAllToBuy().size();
	}

	public List<ShoppingListItemOverview> getAllToBuy() {
//TODO more efficient code

		ArrayList<ShoppingListItemOverview> result = new ArrayList<ShoppingListItemOverview>();
		List<Order> orders = orderDao.getByPrepared(false);
		Map<Long, Integer> usedIngredients = new HashMap<Long, Integer>();

		for (Order order : orders) {
			Map<Food, Integer> portions = order.getPortions();
			for (Food food : portions.keySet()) {
				Map<Long, Integer> foodIngredients = food.getIngredientsById();
				for (Long ingredientID : foodIngredients.keySet()) {
					int available = 0;
					int needed = foodIngredients.get(ingredientID) * portions.get(food);
					if (usedIngredients.containsKey(ingredientID)) {
						int usedAmount = usedIngredients.get(ingredientID);
						available = ingredientDao.getById(ingredientID).getAmountAvailiable() - usedAmount;
						if(available<0) {
							available = 0;
						}
						usedIngredients.put(ingredientID, usedAmount + needed);
					} else {
						available = ingredientDao.getById(ingredientID).getAmountAvailiable();
						usedIngredients.put(ingredientID, needed);
					}
					
					int toBuy = (available - needed >= 0) ? 0 : (Math.abs(available - needed));

					ShoppingListItemOverview newItem = new ShoppingListItemOverview(ingredientDao.getById(ingredientID),
							toBuy);
					for (ShoppingListItemOverview item : result) {
						if (item.getIngredient().getId().equals(ingredientID)) {
							System.out.println("old value: " + item.getToBuy());
							newItem.setToBuy(item.getToBuy() + newItem.getToBuy());
							result.remove(item);
							break;
						}
					}
					System.out.println("new value: " + newItem.getToBuy());
					result.add(newItem);
				}
			}
		}
		System.out.println("result " + result);
		return result;
	}

}

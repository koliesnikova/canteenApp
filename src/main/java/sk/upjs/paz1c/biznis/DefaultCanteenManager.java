package sk.upjs.paz1c.biznis;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		OverviewManagerImpl manager = new OverviewManagerImpl();
		Map<Long, Integer> ingrsToBuy = manager.getIngredientsToBuy(date);

		ArrayList<ShoppingListItemOverview> result = new ArrayList<ShoppingListItemOverview>();

		for (Long ingredientId : ingrsToBuy.keySet()) {
			result.add(new ShoppingListItemOverview(ingredientDao.getById(ingredientId), ingrsToBuy.get(ingredientId)));
		}
		return result;
	}

	public int getNumberOfToBuy() {
		return getAllToBuy().size();
	}

	public List<ShoppingListItemOverview> getAllToBuy() {
		ArrayList<ShoppingListItemOverview> result = new ArrayList<ShoppingListItemOverview>();
		List<Order> orders = orderDao.getByPrepared(false);
		Map<Long, Integer> usedIngredients = new HashMap<Long, Integer>();

		for (Order order : orders) {
			Map<Food, Integer> portions = order.getPortions();

			for (Food food : portions.keySet()) {
				Map<Long, Integer> foodIngredients = food.getIngredientsById();

				for (Long ingredientID : foodIngredients.keySet()) {
					int available = 0;
					int usedAmount = 0;
					int needed = foodIngredients.get(ingredientID) * portions.get(food);

					if (usedIngredients.containsKey(ingredientID)) {
						usedAmount = usedIngredients.get(ingredientID);
						usedIngredients.put(ingredientID, usedAmount + needed);
					}
					usedIngredients.put(ingredientID, needed);
					available = ingredientDao.getById(ingredientID).getAmountAvailiable() - usedAmount;
					if (available < 0) {
						available = 0;
					}
					int toBuy = (available - needed >= 0) ? 0 : (Math.abs(available - needed));
					boolean onlyRemoveItem = false;
					if (toBuy == 0) {
						onlyRemoveItem = true;
					}
					ShoppingListItemOverview newItem = new ShoppingListItemOverview(ingredientDao.getById(ingredientID),
							toBuy);
					for (ShoppingListItemOverview item : result) {
						if (item.getIngredient().getId().equals(ingredientID)) {
							if (!onlyRemoveItem) {
								newItem.setToBuy(item.getToBuy() + newItem.getToBuy());
							}
							result.remove(item);
							break;
						}
					}
					if (!onlyRemoveItem)
						result.add(newItem);
				}
			}
		}
		return result;
	}

	public boolean checkOrderIngredientsAvailable(Order order) {
		// for every food in order check every ingredient, if amount available if
		// sufficient
		for (Food f : order.getPortions().keySet()) {
			int foodCount = order.getPortions().get(f);
			for (Ingredient i : f.getIngredients().keySet()) {
				int ingredientCount = f.getIngredients().get(i);
				Ingredient fromDB = ingredientDao.getById(i.getId());
				if (fromDB.getAmountAvailiable() < foodCount * ingredientCount)
					return false;
			}
		}
		return true;
	}

	public void prepareIngredientsForOrder(Order order) {
		for (Food f : order.getPortions().keySet()) {
			int foodCount = order.getPortions().get(f);
			for (Ingredient i : f.getIngredients().keySet()) {
				int ingredientCount = f.getIngredients().get(i);
				Ingredient fromDB = ingredientDao.getById(i.getId());
				fromDB.setAmountAvailiable(fromDB.getAmountAvailiable() - (foodCount * ingredientCount));
				ingredientDao.save(fromDB);
			}
		}
	}

}

package sk.upjs.paz1c.biznis;

import java.util.ArrayList;
import java.util.List;

import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.FoodDao;
import sk.upjs.paz1c.storage.Order;
import sk.upjs.paz1c.storage.OrderDao;

public class DefaultCanteenManager implements CanteenManager {

	private FoodDao foodDao = DaoFactory.INSTANCE.getFoodDao();
	private OrderDao orderDao = DaoFactory.INSTANCE.getOrderDao();
	
	@Override
	public List<Food> filterFoodNotOnOrder(Long orderId) {
		Order order = orderDao.getById(orderId);
		List<Food> result = new ArrayList<>();
		List<Food> allFoods = foodDao.getAll();
		for (Food f : order.getPortions().keySet()) {
			boolean found = false;
			for (Food foodFromAll : allFoods) {
				if (f.getId().equals(foodFromAll.getId())) {
					found = true;
					break;
				}		
			}
			if (!found)
				result.add(f);
		}
		return result;
	}

}

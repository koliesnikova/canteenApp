package sk.upjs.paz1c.biznis;

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

}

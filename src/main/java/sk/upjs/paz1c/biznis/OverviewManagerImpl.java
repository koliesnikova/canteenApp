package sk.upjs.paz1c.biznis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import sk.upjs.paz1c.storage.DaoFactory;
import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.Order;
import sk.upjs.paz1c.storage.OrderDao;

public class OverviewManagerImpl implements OverviewManager {

	private OrderDao orderDao = DaoFactory.INSTANCE.getOrderDao(); 
	
	@Override
	public List<OrderFoodOverview> getAll(Long orderId) {
		if (orderId == null) {
			return new ArrayList<>();
		}
		List<OrderFoodOverview> result = new ArrayList<>();
		Order byId = orderDao.getById(orderId);
		for (Entry<Food, Integer> p : byId.getPortions().entrySet()) {
			result.add(new OrderFoodOverview(p.getKey().getId(), p.getKey().getName(), p.getValue(), p.getKey().getPrice()));
		}
		
		return result;
	}

}

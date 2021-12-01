package sk.upjs.paz1c.biznis;

import java.util.List;

import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.Order;

public interface CanteenManager {
	
	List<Food> filterFoodNotInOrder(Long orderId);

}

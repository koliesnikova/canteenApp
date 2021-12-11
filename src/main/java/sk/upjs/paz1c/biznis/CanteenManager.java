package sk.upjs.paz1c.biznis;

import java.time.LocalDateTime;
import java.util.List;

import sk.upjs.paz1c.storage.Food;
import sk.upjs.paz1c.storage.Order;

public interface CanteenManager {
	
	List<Food> filterFoodNotInOrder(Long orderId);
	
	List<ShoppingListItemOverview> getAllToBuy();
	
	int getNumberOfToBuy();
	
	 List<ShoppingListItemOverview> getItemsForShoppingList(LocalDateTime date);
	 
	 boolean checkOrderIngredientsAvailable(Order order);
	 
	 void prepareIngredientsForOrder(Order order);
}

package sk.upjs.paz1c.biznis;

import java.time.LocalDateTime;
import java.util.List;

import sk.upjs.paz1c.storage.Food;

public interface CanteenManager {
	
	List<Food> filterFoodNotInOrder(Long orderId);
	
	List<ShoppingListItemOverview> getAllToBuy();
	
	int getNumberOfToBuy();
	
	 List<ShoppingListItemOverview> getItemsForShoppingList(LocalDateTime date);
}

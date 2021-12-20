package sk.upjs.paz1c.storage;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderDao {
	
	List<Order> getAll();
	
	List<Order> getByPrepared(boolean prepared);
	
	Order getById(long idOrder) throws EntityNotFoundException;
	
	List<Order> getByDay(LocalDateTime day);
	
	Order save(Order order) throws EntityNotFoundException;
	
	void insertFoods(Order order);
	
	Order delete(long idOrder) throws EntityUndeletableException;
	
	Order removeFood(Order order, Food food);
	
}

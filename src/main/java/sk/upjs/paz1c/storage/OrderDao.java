package sk.upjs.paz1c.storage;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderDao {
	
	List<Order> getAll();
	
	Order getById(long idOrder) throws EntityNotFoundException;
	
	Order getByDay(LocalDateTime day) throws EntityNotFoundException;
	
	Order save(Order order) throws EntityNotFoundException;
	
	void insertFoods(Order order);
	
	Order delete(long idOrder) throws EntityUndeletableException;
	

}

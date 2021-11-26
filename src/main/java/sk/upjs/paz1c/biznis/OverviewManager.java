package sk.upjs.paz1c.biznis;

import java.util.List;

public interface OverviewManager {
	
	public List<OrderFoodOverview> getAll(Long ordeId);
	
}

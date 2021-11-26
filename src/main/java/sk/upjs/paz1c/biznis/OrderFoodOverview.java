package sk.upjs.paz1c.biznis;

public class OrderFoodOverview {
	
	private long foodId;
	private String name;
	private int count;
	private double totalSum;
	
	public OrderFoodOverview(long foodId, String name, int count, double price) {
		this.foodId = foodId;
		this.name = name;
		this.count = count;
		this.totalSum = this.count * price;
	}

	public long getFoodId() {
		return foodId;
	}
	
	public String getName() {
		return name;
	}

	public int getCount() {
		return count;
	}

	public double getTotalSum() {
		return totalSum;
	}

	@Override
	public String toString() {
		return foodId + " " + name;
	}
	
	
	

}

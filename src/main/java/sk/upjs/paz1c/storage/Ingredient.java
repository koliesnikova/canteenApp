package sk.upjs.paz1c.storage;

public class Ingredient {
	private Long id;
	private String name;
	private Double price;
	private String amount; //extract number + unit
	private Integer amountAvailable;
	
	
	public Ingredient(Long id, String name, double price, String amount, Integer amountAvailiable) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.amount = amount;
		this.amountAvailable = amountAvailiable;
	}
	
	public Ingredient( String name, double price, String amount, Integer amountAvailiable) {
		this.name = name;
		this.price = price;
		this.amount = amount;
		this.amountAvailable = amountAvailiable;
	}
	

	public Ingredient(String name, double price, String amount) {
		this.name = name;
		this.price = price;
		this.amount = amount;
		this.amountAvailable = 0;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) throws NullPointerException {
		if(id==null) {
			throw new NullPointerException("ID can not be set to null");
		}
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) throws NullPointerException{
		if(amount==null) {
			throw new NullPointerException("Amount can not be null");
		}
		this.amount = amount;
	}
	
	public Integer getAmountAvailiable() {
		return amountAvailable;
	}

	public void setAmountAvailiable(Integer amountAvailiable) throws NullPointerException{
		if(amountAvailiable==null) {
			throw new NullPointerException("Amount availiable can not be null");
		}
		this.amountAvailable = amountAvailiable;
	}


	@Override
	public String toString() {
		//TODO potom upravit vypis
		return id + " " + name + " " + amount +" ";
	}


//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
//		result = prime * result + ((amountAvailiable == null) ? 0 : amountAvailiable.hashCode());
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
//		result = prime * result + ((name == null) ? 0 : name.hashCode());
//		result = prime * result + ((price == null) ? 0 : price.hashCode());
//		return result;
//	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ingredient other = (Ingredient) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (amountAvailable == null) {
			if (other.amountAvailable != null)
				return false;
		} else if (!amountAvailable.equals(other.amountAvailable))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		return true;
	}
	
	
	
	
}

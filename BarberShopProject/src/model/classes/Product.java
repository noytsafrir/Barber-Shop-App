package model.classes;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class Product extends RecursiveTreeObject<Product> implements Comparable<Product> {

	private int prodId;
	private String name;
	private int price;
	
	public Product(int id, String name, int price) {
		this.prodId = id;
		this.name = name;
		this.price = price;
	}
	
	public Product(String name, int price) {
		this.name = name;
		this.price = price;
	}

	public int getId() {
		return prodId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Product))
			return false;

		Product temp = (Product) obj;
		return temp.prodId == this.prodId;
	}


	public void setId(int prodId) {
		this.prodId = prodId;
	}

	@Override
	public int hashCode() {
		return getId();
	}

	@Override
	public int compareTo(Product prod) {
		return this.getId() - prod.getId();
	}
}

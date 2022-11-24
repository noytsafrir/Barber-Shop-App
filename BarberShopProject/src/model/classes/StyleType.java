package model.classes;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class StyleType extends RecursiveTreeObject<StyleType> {

	public static enum eStyleTypes {eChild, eMan, eBeard, eEdges, eColor, eShades, eStraight};
	
	private int price;
	private int lenght;
	private eStyleTypes type;
	private String name;
	
	public StyleType(eStyleTypes type) {
		this.type = type;
	}
	
	public StyleType(eStyleTypes type, String name, int lenght, int price) {
		this.type = type;
		this.name = name;
		this.lenght = lenght;
		this.price = price;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getLenght() {
		return lenght;
	}

	public void setLenght(int lenght) {
		this.lenght = lenght;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public eStyleTypes getType() {
		return type;
	}
	
}

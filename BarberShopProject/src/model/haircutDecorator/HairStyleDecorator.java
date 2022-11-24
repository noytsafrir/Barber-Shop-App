package model.haircutDecorator;

import model.classes.StyleType;

public abstract class HairStyleDecorator implements iHaircutStyle{
	protected iHaircutStyle hairStyle;
	protected StyleType theType;
	
	public HairStyleDecorator(iHaircutStyle theStyle) {
		this.hairStyle = theStyle;
	}

	@Override
	public int getLenght() {
		return theType.getLenght();
	}

	@Override
	public int getPrice() {
		return theType.getPrice();
	}

	@Override
	public int getTotalLenght() {
		return getLenght() + (hairStyle != null ? this.hairStyle.getTotalLenght() : 0);
	}

	@Override
	public int getTotalPrice() {
		return getPrice() + (hairStyle != null ? this.hairStyle.getTotalPrice() : 0);
	}

	@Override
	public String getName() {
		return theType.getName();
	}
	
	@Override
	public String getAllStylesStrings() {
		return getName() + (hairStyle != null ? (" + " + this.hairStyle.getAllStylesStrings()) : "");
	}	
}

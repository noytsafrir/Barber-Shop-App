package model.haircutDecorator;

import java.sql.SQLException;

import model.classes.StyleType.eStyleTypes;
import model.singletons.CatalogStylesSingleton;

public class StyleChildHaircut extends HairStyleDecorator{

	public StyleChildHaircut(iHaircutStyle theStyle) throws SQLException {
		super(theStyle);
		this.theType = CatalogStylesSingleton.getInstance().getStyle(eStyleTypes.eChild);  
	}
}

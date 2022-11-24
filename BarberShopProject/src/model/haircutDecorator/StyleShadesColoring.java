package model.haircutDecorator;

import java.sql.SQLException;

import model.classes.StyleType.eStyleTypes;
import model.singletons.CatalogStylesSingleton;

public class StyleShadesColoring extends HairStyleDecorator{

	public StyleShadesColoring(iHaircutStyle theHairStyle) throws SQLException {
		super(theHairStyle);
		this.theType = CatalogStylesSingleton.getInstance().getStyle(eStyleTypes.eShades);  
	}
	
}

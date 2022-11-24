package model.haircutDecorator;

import java.sql.SQLException;

import model.classes.StyleType.eStyleTypes;
import model.singletons.CatalogStylesSingleton;

public class StyleStraighteningHaircut extends HairStyleDecorator{

	public StyleStraighteningHaircut(iHaircutStyle theStyle) throws SQLException {
		super(theStyle);
		this.theType = CatalogStylesSingleton.getInstance().getStyle(eStyleTypes.eStraight);  

	}
	
}

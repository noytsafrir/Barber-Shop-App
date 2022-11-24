package model.haircutDecorator;


import java.sql.SQLException;

import model.classes.StyleType.eStyleTypes;
import model.singletons.CatalogStylesSingleton;

public class HairStyleFactory {

	public static iHaircutStyle getDecoratedHairStyle(String key) throws SQLException {
		if(CatalogStylesSingleton.getInstance().getStyle(eStyleTypes.eMan).getName().equals(key))
			return new StyleManHairCut(null);
		if(CatalogStylesSingleton.getInstance().getStyle(eStyleTypes.eChild).getName().equals(key))
			return new StyleChildHaircut(null);
		if(CatalogStylesSingleton.getInstance().getStyle(eStyleTypes.eColor).getName().equals(key))
			return new StyleColorHaircut(null);
		if(CatalogStylesSingleton.getInstance().getStyle(eStyleTypes.eEdges).getName().equals(key))
			return new StyleEdgesHaircut(null);
		if(CatalogStylesSingleton.getInstance().getStyle(eStyleTypes.eShades).getName().equals(key))
			return new StyleShadesColoring(null);
		if(CatalogStylesSingleton.getInstance().getStyle(eStyleTypes.eStraight).getName().equals(key))
			return new StyleStraighteningHaircut(null);
		if(CatalogStylesSingleton.getInstance().manAndBeard.equals(key))
			return new StyleManHairCut(new StyleBeardDesign(null));
		if(CatalogStylesSingleton.getInstance().edgesAndColor.equals(key))
			return new StyleEdgesHaircut(new StyleColorHaircut(null));
		if(CatalogStylesSingleton.getInstance().edgesAndShades.equals(key))
			return new StyleEdgesHaircut(new StyleShadesColoring(null));
		if(CatalogStylesSingleton.getInstance().edgesAndStraightening.equals(key))
			return new StyleEdgesHaircut(new StyleStraighteningHaircut(null));
		if(CatalogStylesSingleton.getInstance().colorAndStraightening.equals(key))
			return new StyleColorHaircut(new StyleStraighteningHaircut(null));
		if(CatalogStylesSingleton.getInstance().shadesAndStraightening.equals(key))
			return new StyleShadesColoring(new StyleStraighteningHaircut(null));
		else
			return null;
	}
}

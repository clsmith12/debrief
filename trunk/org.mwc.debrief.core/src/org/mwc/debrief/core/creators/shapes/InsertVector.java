package org.mwc.debrief.core.creators.shapes;

import MWC.GUI.Shapes.LineShape;
import MWC.GUI.Shapes.PlainShape;
import MWC.GUI.Shapes.VectorShape;
import MWC.GenericData.WorldLocation;
import MWC.GenericData.WorldVector;

/**
 * @author ian.mayo
 *
 */
public class InsertVector extends CoreInsertShape
{


	/** produce the shape for the user
	 * 
	 * @param centre the current centre of the screen
	 * @return a shape, based on the centre
	 */
	protected PlainShape getShape(WorldLocation centre)
	{
		// generate the shape
		PlainShape res = new VectorShape(centre,
        centre.add(new WorldVector(MWC.Algorithms.Conversions.Degs2Rads(45.0),
            0.05, 0)));
		return res;
	}	
	
	/** return the name of this shape, used give the shape an initial name
	 * 
	 * @return the name of this type of shape, eg: rectangle
	 */
	protected String getShapeName()
	{
		return "vector";
	}	
	
}
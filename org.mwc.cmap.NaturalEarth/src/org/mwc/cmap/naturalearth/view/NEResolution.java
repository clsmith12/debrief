package org.mwc.cmap.naturalearth.view;

import java.util.ArrayList;

public class NEResolution extends ArrayList<NEFeatureStyle>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final private Double _minS;
	final private Double _maxS;

	public NEResolution(Double minS, Double maxS)
	{
		_minS = minS;
		_maxS = maxS;
	}
	
	/** whether this set of styles is suited to plotting this particular scale
	 * 
	 * @param scale
	 * @return
	 */
	public boolean canPlot(double scale)
	{
		boolean valid = true;;
		if(_minS != null)
		{
			valid = scale < _minS;
		}
		if(valid)
		{
			if(_maxS != null)
				valid = scale > _maxS;
		}
		
		return valid;
	}

	
	
}

package org.mwc.asset.core;

import org.eclipse.jface.resource.ImageDescriptor;
import org.mwc.cmap.layer_manager.views.support.ViewLabelProvider.ViewLabelImageHelper;

import ASSET.ParticipantType;
import ASSET.GUI.Workbench.Plotters.*;
import ASSET.Models.*;
import ASSET.Participants.Category.Force;

public class ASSETImageHelper implements ViewLabelImageHelper
{

	public ImageDescriptor getImageFor(Object subject)
	{
		final ImageDescriptor res;
		if ((subject instanceof SensorType) || (subject instanceof SensorsPlottable))
		{
			res = ASSETActivator.getImageDescriptor("icons\\satellite_dish.png");
		}
		else if ((subject instanceof MovementType) || (subject instanceof PerformancePlottable))
		{
			res = ASSETActivator.getImageDescriptor("icons\\gear.png");
		}
		else if ((subject instanceof DecisionType) || (subject instanceof BehavioursPlottable))
		{
			res = ASSETActivator.getImageDescriptor("icons\\user_comment.png");
		}
		else if (subject instanceof ScenarioParticipantWrapper)
		{
			ScenarioParticipantWrapper sw = (ScenarioParticipantWrapper) subject;
			ParticipantType part = sw.getParticipant();
			String force = part.getCategory().getForce();
			if(force == Force.RED)
				res = ASSETActivator.getImageDescriptor("icons\\flag_red.png");
			else if(force == Force.GREEN)
				res = ASSETActivator.getImageDescriptor("icons\\flag_green.png");
			else if(force == Force.BLUE)
				res = ASSETActivator.getImageDescriptor("icons\\flag_blue.png");
			else
				res = null;
		}
		else
		{
			res = null;
		}

		return res;
	}

}

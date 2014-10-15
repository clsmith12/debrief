/*
 *    Debrief - the Open Source Maritime Analysis Application
 *    http://debrief.info
 *
 *    (C) 2000-2014, PlanetMayo Ltd
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the Eclipse Public License v1.0
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.mwc.asset.comms.restlet.data;

import org.restlet.resource.Post;

import ASSET.Participants.DemandedStatus;

/**
 * The resource associated to a contact.
 */
public interface DecisionResource
{

	public static class DecidedEvent extends AssetEvent
	{
		final public String _activity;
		final public DemandedStatus _status;

		public DecidedEvent(final DemandedStatus status, final String activity)
		{
			super(status.getTime());
			_status = status;
			_activity = activity;
		}
	}

	@Post
	public void accept(DecidedEvent event);
}

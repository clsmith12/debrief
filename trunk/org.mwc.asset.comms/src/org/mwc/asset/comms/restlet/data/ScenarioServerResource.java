package org.mwc.asset.comms.restlet.data;

import java.util.List;
import java.util.Vector;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * The server side implementation of the Restlet resource.
 */
public class ScenarioServerResource extends ServerResource implements
		ScenariosResource
{
	

	@Get
	public List<Scenario> retrieve()
	{
		List<Scenario> res = new Vector<Scenario>();
		res.add(new Scenario("aaa", 1));
		res.add(new Scenario("bbb", 12));
		res.add(new Scenario("ccc", 21));
		return res;
	}
}
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
package org.bitbucket.es4gwt.shared.elastic.filter;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.bitbucket.es4gwt.shared.elastic.filter.Filters.*;

/**
 * @author Mikael Couzic
 */
class DateRange implements ElasticFilter {

	private final ElasticFilter after;
	private final ElasticFilter before;

	public DateRange(String early, String late) {
		checkNotNull(early);
		checkNotNull(late);
		this.after = endAfter(early);
		this.before = startBefore(late);
	}

	@Override
	public String toRequestString() {
		return and(after, before).toRequestString();
	}

	@Override
	public String toString() {
		return toRequestString();
	}

}

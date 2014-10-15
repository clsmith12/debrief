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
package org.eclipse.nebula.widgets.formattedtext;

import java.util.Locale;

/**
 * This class provides formatting of <code>Double</code> values in a
 * <code>FormattedText</code>.<p>
 * 
 * NumberFormatter returns different numeric types based on the current
 * value in the Text field. DoubleFormatter is an override of NumberFormatter
 * allowing to guaranty to always return Double values (Number.doubleValue()).
 */
public class DoubleFormatter extends NumberFormatter {
  public DoubleFormatter() {
		super();
	}

	public DoubleFormatter(Locale loc) {
		super(loc);
	}

	public DoubleFormatter(String editPattern, Locale loc) {
		super(editPattern, loc);
	}

	public DoubleFormatter(String editPattern, String displayPattern, Locale loc) {
		super(editPattern, displayPattern, loc);
	}

	public DoubleFormatter(String editPattern, String displayPattern) {
		super(editPattern, displayPattern);
	}

	public DoubleFormatter(String editPattern) {
		super(editPattern);
	}

	/**
   * Returns the current value of the text control if it is a valid <code>Double</code>.
   * If the buffer is flagged as modified, the value is recalculated by parsing
   * with the <code>nfEdit</code> initialized with the edit pattern. If the
   * number is not valid, returns <code>null</code>.
   * 
   * @return current number value if valid, <code>null</code> else
   * @see ITextFormatter#getValue()
   */
	public Object getValue() {
		Object value = super.getValue();
		if ( value instanceof Number ) {
			return new Double(((Number) value).doubleValue());
		}
		return super.getValue();
	}

	/**
   * Returns the type of value this {@link ITextFormatter} handles,
   * i.e. returns in {@link #getValue()}.<br>
   * A DoubleFormatter always returns a Double value.
   * 
   * @return The value type.
   */
	public Class getValueType() {
		return Double.class;
	}
}

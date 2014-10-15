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
package de.kupzog.ktable;

/**
 * Table model that implements columnwidths based on weights, always showing all
 * columns and thus preventing H_SCROLL.
 * <p>
 * When resizing, the space is shifted between the columns.
 * 
 * @author Lorenz Maierhofer <lorenz.maierhofer@logicmindguide.com>
 */
public abstract class KTableNoScrollModel extends KTableSortedModel {
	// the factor used to calculate "promille" values of the current control
	// width that are stored.
	private int FACTOR = 10000;

	private KTable _table;

	public KTableNoScrollModel(KTable table) {
		_table = table;
	}

	public void initialize() {
		super.initialize();
		int weightSum = 1;
		for (int i = 0; i < getColumnCount(); i++) {
			int initialWeight = getInitialColumnWidth(i);
			weightSum += initialWeight;
		}

		// initialize with % values.
		for (int i = 0; i < getColumnCount(); i++) {
			super.setColumnWidth(i, (int) ((getInitialColumnWidth(i) / (double) weightSum) * FACTOR));
		}
		int pts = 0;
		for (int i = 0; i < getColumnCount(); i++)
			pts += super.getColumnWidth(i);
		FACTOR = pts;
	}

	// private int getOptimalWidth(int col) {
	// int optWidth = 1;
	// GC gc = new GC(_table.getDisplay());
	// for (int r=0; r<getFixedRowCount(); r++) {
	// int optW = getCellRenderer(col, r).getOptimalWidth(gc,
	// col,r,getContentAt(col,r), true, this);
	// if (optWidth<optW) optWidth=optW;
	// }
	// for (int r=_table.m_TopRow; r<=_table.m_RowsFullyVisible; r++) {
	// int optW = getCellRenderer(col, r).getOptimalWidth(gc,
	// col,r,getContentAt(col,r), false, this);
	// if (optWidth<optW) optWidth=optW;
	// }
	// gc.dispose();
	// return optWidth;
	// }

	/*
	 * 
	 */
	public void setTable(KTable table) {
		_table = table;
	}

	/*
	 * (Kein Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableModel#getColumnWidth(int)
	 */
	public int getColumnWidth(int col) {
		double percent = super.getColumnWidth(col) / ((double) FACTOR);
		if (_table != null && !_table.isDisposed()) {
			return (int) ((_table.getClientArea().width - 1) * percent);
		} else
			return (int) Math.round(percent * 100);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kupzog.ktable.KTableModel#setColumnWidth(int, int)
	 */
	public void setColumnWidth(int col, int value) {
		int tableWidth = _table.getClientArea().width;
		double percent = (value + 1) / (double) tableWidth;
		if (col == getColumnCount() - 1) {
			int weightsum = super.getColumnWidth(col) + super.getColumnWidth(col - 1);
			super.setColumnWidth(col, (int) (percent * FACTOR));
			super.setColumnWidth(col - 1, weightsum - ((int) (percent * FACTOR)));
		} else {
			int weightsum = super.getColumnWidth(col) + super.getColumnWidth(col + 1);
			super.setColumnWidth(col, (int) (percent * FACTOR));
			super.setColumnWidth(col + 1, weightsum - ((int) (percent * FACTOR)));
		}
	}
}

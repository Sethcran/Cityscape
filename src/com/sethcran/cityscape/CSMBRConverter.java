package com.sethcran.cityscape;

import org.khelekore.prtree.MBRConverter;

public class CSMBRConverter implements MBRConverter<Plot>{

	@Override
	public double getMaxX(Plot plot) {
		return plot.getXmax();
	}

	@Override
	public double getMaxY(Plot plot) {
		return plot.getZmax();
	}

	@Override
	public double getMinX(Plot plot) {
		return plot.getXmin();
	}

	@Override
	public double getMinY(Plot plot) {
		return plot.getZmin();
	}

}

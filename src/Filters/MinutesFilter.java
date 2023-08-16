package Filters;

import java.io.IOException;

import Databases.*;

public class MinutesFilter implements Filter{
	private int min;
	private int max;
	
	public MinutesFilter (int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	@Override
	public boolean satisfies(String id) {
		int minutes = MovieDatabase.getMinutes(id);
		boolean logic1 = (minutes >= min);
		boolean logic2 = (minutes <= max);
		return logic1 && logic2;	
	}
	
}
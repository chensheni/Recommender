package Filters;
import Databases.*;

public class DirectorsFilter implements Filter{
	private String directors;
	
	public DirectorsFilter (String directors) {
		this.directors = directors;
	}
	
	@Override
	public boolean satisfies(String id) {
		String[] directorArray = directors.split(",");
		//System.out.println(directorArray[1]);
		String directorOfMovies = MovieDatabase.getDirector(id);
		String directorOfMoviesContent = directorOfMovies.substring(1, directorOfMovies.length()-1);
		for (String director: directorArray) {
			//System.out.println(director);
			boolean temp = directorOfMoviesContent.contains(director);
			if (temp == true) {
				return true;
			}
		}
		return false;	
	}
	
		
	
}
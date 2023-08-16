package Databases;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import Elements.*;
import Filters.*;

public class MovieDatabase {
	private static HashMap<String, Movie> ourMovies;

	public static void initialize(String moviefile) {
		if (ourMovies == null) {
			ourMovies = new HashMap<String, Movie>();
			loadMovies("data/" + moviefile);
		}
	}

	private static void initialize() {
		if (ourMovies == null) {
			ourMovies = new HashMap<String, Movie>();
			loadMovies("src/data/ratedmoviesfull.csv");
		}
	}

	private static void loadMovies(String filename) {
		ArrayList<Movie> movies = new ArrayList<>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			int i = 0;

			while ((line = br.readLine()) != null) {
				if (i != 0) {
					String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
					Movie aMovie = new Movie(values);
					movies.add(aMovie);
				}
				i = 1;
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		for (Movie m : movies) {
			ourMovies.put(m.getID(), m);
		}
	}

	public static boolean containsID(String id) {
		initialize();
		return ourMovies.containsKey(id);
	}

	public static int getYear(String id) {
		initialize();
		return ourMovies.get(id).getYear();
	}

	public static String getGenres(String id) {
		initialize();
		return ourMovies.get(id).getGenres();
	}

	public static String getTitle(String id) {
		initialize();
		return ourMovies.get(id).getTitle();
	}

	public static Movie getMovie(String id) {
		initialize();
		return ourMovies.get(id);
	}

	public static String getPoster(String id) {
		initialize();
		return ourMovies.get(id).getPoster();
	}

	public static int getMinutes(String id) {
		initialize();
		return ourMovies.get(id).getMinutes();
	}

	public static String getCountry(String id) {
		initialize();
		return ourMovies.get(id).getCountry();
	}

	public static String getDirector(String id) {
		initialize();
		return ourMovies.get(id).getDirector();
	}

	public static int size() {
		initialize();
		return ourMovies.size();
	}

	public static ArrayList<String> filterBy(Filter f){
		initialize();
		ArrayList<String> list = new ArrayList<String>();
		for (String id : ourMovies.keySet()) {
			if (f.satisfies(id)) {
				list.add(id);
			}
		}

		return list;
	}

}

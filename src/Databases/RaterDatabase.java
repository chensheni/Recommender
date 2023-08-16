package Databases;

/**
 * Write a description of RaterDatabase here.
 * 
 * @author ChenShen
 * @version 20230814
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import Elements.*;

public class RaterDatabase {
	private static HashMap<String, Rater> ourRaters;

	private static void initialize() {
		// this method is only called from addRatings
		if (ourRaters == null) {
			ourRaters = new HashMap<String, Rater>();
		}
	}

	public static void initialize(String filename) {
		if (ourRaters == null) {
			ourRaters = new HashMap<String, Rater>();
			try {
				addRatings("src/data/" + filename);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void addRatings(String filename) throws IOException {
		initialize();
		BufferedReader br;
		br = new BufferedReader(new FileReader(filename));

		String line;
		int i = 0;

		while ((line = br.readLine()) != null) {
			if (i != 0) {
				String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

				String id = values[0];
				String item = values[1];
				String rating = values[2];
				addRaterRating(id, item, Double.parseDouble(rating));
			}
			i = 1;
		}

	}

	public static void addRaterRating(String raterID, String movieID, double rating) {
		initialize();
		Rater rater = null;
		if (ourRaters.containsKey(raterID)) {
			rater = ourRaters.get(raterID);
		} else {
			rater = new EfficientRater(raterID);
			ourRaters.put(raterID, rater);
		}
		rater.addRating(movieID, rating);
	}

	public static Rater getRater(String id) {
		initialize();

		return ourRaters.get(id);
	}

	public static ArrayList<Rater> getRaters() {
		initialize();
		ArrayList<Rater> list = new ArrayList<Rater>(ourRaters.values());

		return list;
	}

	public static int size() {
		return ourRaters.size();
	}

}
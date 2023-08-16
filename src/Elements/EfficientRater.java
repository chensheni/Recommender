package Elements;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Write a description of class Rater here.
 * 
 * @author ChenShen
 * @version 20230812
 */

public class EfficientRater implements Rater {
	private String myID;
	/**
	 * String: movie ID Ratng: rating
	 */
	private HashMap<String, Rating> myRatings;

	public EfficientRater(String id) {
		myID = id;
		myRatings = new HashMap<String, Rating>();
	}

	public void addRating(String item, double rating) {
		myRatings.put(item, new Rating(item, rating));
	}

	public boolean hasRating(String item) {
		if (myRatings.containsKey(item)) {
			return true;
		} else {
			return false;
		}
	}

	public String getID() {
		return myID;
	}

	public double getRating(String item) {
        for (String movieID : myRatings.keySet()) {
            if (movieID.equals(item)) {
                return myRatings.get(movieID).getValue();
            }
        }
        
        return -1.0;
	}

	public int numRatings() {
		return myRatings.size();
	}

	public ArrayList<String> getItemsRated() {
		ArrayList<String> list = new ArrayList<String>();
		for (String movieID : myRatings.keySet()) {
			list.add(myRatings.get(movieID).getItem());
		}

		return list;
	}
}

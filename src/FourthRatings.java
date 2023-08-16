import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import Databases.*;
import Elements.*;
import Filters.*;

public class FourthRatings {

	public FourthRatings(String filename) {
		RaterDatabase.initialize(filename);
	}

	public FourthRatings() {
		this("ratings.csv");
	}

	public int getRaterSize() {
		return RaterDatabase.size();
	}

	public double getAverageByID(String movieId, int minimalRaters) {
		double sumRating = 0.0;
		int count = 0;

		for (Rater currentRator : RaterDatabase.getRaters()) {
			if (currentRator.hasRating(movieId)) {
				sumRating += currentRator.getRating(movieId);
				// System.out.println(sumRating);
				count++;
			}
		}
		if (count == 0) {
			return 0.0;
		} else if (count < minimalRaters) {
			return 0.0;
		} else {
			return sumRating / (double) count;
		}
	}

	public void printAverageRatings(int minimalRaters) {
		ArrayList<Rating> ratings = getAverageRatings(minimalRaters);
		Collections.sort(ratings);
		for (int i = 0; i < ratings.size(); i++) {
			Rating aRating = ratings.get(i);
			double avgRating = aRating.getValue();
			String title = MovieDatabase.getTitle(aRating.getItem());
			System.out.println(avgRating + " " + title);
		}
		System.out.println(ratings.size());
	}

	public ArrayList<Rating> getAverageRatings(int minimalRaters) {
		ArrayList<Rating> averageRatings = new ArrayList<Rating>();
		ArrayList<String> movies = MovieDatabase.filterBy(new TrueFilter());

		for (String movieID : movies) {
			double average = getAverageByID(movieID, minimalRaters);
			if (average != 0.0) {
				Rating rating = new Rating(movieID, average);
				averageRatings.add(rating);
			}
		}

		return averageRatings;
	}

	public ArrayList<Rating> getAverageRatingsByFilter(int minimalRaters, Filter filterCriteria) {
		ArrayList<Rating> avgRatings = new ArrayList<Rating>();
		ArrayList<String> ids = MovieDatabase.filterBy(filterCriteria);

		for (String movieID : ids) {
			double average = getAverageByID(movieID, minimalRaters);
			if (average != 0.0) {
				Rating rating = new Rating(movieID, average);
				avgRatings.add(rating);
			}
		}
		return avgRatings;
	}

	public void printAverageRatingsByFilter(int minimalRaters, Filter filterCriteria) {
		ArrayList<Rating> ratings = getAverageRatingsByFilter(minimalRaters, filterCriteria);
		Collections.sort(ratings);
		for (int i = 0; i < ratings.size(); i++) {
			Rating aRating = ratings.get(i);
			double avgRating = aRating.getValue();
			String title = MovieDatabase.getTitle(aRating.getItem());
			System.out.println(Double.toString(avgRating) + " " + MovieDatabase.getMovie(aRating.getItem()));
		}
		System.out.println(ratings.size());
	}

	public void printAverageRatingsByTwoFilters(int minimalRaters, Filter filterCriteria1, Filter filterCriteria2) {
		AllFilters aALlFilter = new AllFilters();
		aALlFilter.addFilter(filterCriteria1);
		aALlFilter.addFilter(filterCriteria2);
		ArrayList<Rating> ratings = getAverageRatingsByFilter(minimalRaters, aALlFilter);
		Collections.sort(ratings);
		for (int i = 0; i < ratings.size(); i++) {
			Rating aRating = ratings.get(i);
			double avgRating = aRating.getValue();
			String title = MovieDatabase.getTitle(aRating.getItem());
			System.out.println(Double.toString(avgRating) + " " + MovieDatabase.getMovie(aRating.getItem()));
		}
		System.out.println(ratings.size());
	}

	/**
	 * 
	 * @param me
	 * @param r
	 * @return a similarity between raters
	 */
	private double dotProduct(Rater me, Rater r) {
		double result = 0.0;
		ArrayList<String> ratedId = me.getItemsRated();

		for (String aId : ratedId) {
			if (r.hasRating(aId)) {
				result += (me.getRating(aId) - 5.0) * (r.getRating(aId) - 5.0);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param id
	 * @return similar raters for one rater
	 */
	private ArrayList<Rating> getSimilarities(String id) {
		ArrayList<Rater> allRaters = RaterDatabase.getRaters();
		Rater theRater = RaterDatabase.getRater(id);
		//System.out.println("getSimilarities: " + theRater.getID());
		//System.out.println("getSimilarities: " + theRater.numRatings());
		ArrayList<Rating> positiveSimRatings = new ArrayList<Rating>();

		for (Rater aRater : allRaters) {
			if (!(aRater.getID().equals(id))) {
				double dotProductResult = dotProduct(theRater, aRater);
				if (dotProductResult > 0) {
					positiveSimRatings.add(new Rating(aRater.getID(), dotProductResult));
				}
			}
		}
		Collections.sort(positiveSimRatings, Collections.reverseOrder());
		return positiveSimRatings;
	}

	public ArrayList<Rating> getSimilarRatings(String id, int numSimilarRaters, int minimalRaters) {
		ArrayList<Rating> similarRaters = getSimilarities(id);
		//System.out.print(similarRaters.get(0));
		ArrayList<String> moviesId = MovieDatabase.filterBy(new TrueFilter());
		ArrayList<Rating> results = new ArrayList<Rating>();

		for (String movieID : moviesId) {
			double sumRating = 0.0;
			int count = 0;
			int iterTimes = Math.min(similarRaters.size(), numSimilarRaters);
			for (int k = 0; k < iterTimes; k++) {
				// get k-th similarest rater and the score
				Rating r = similarRaters.get(k);
				String raterID = r.getItem();
				double weight = r.getValue();

				Rater rater = RaterDatabase.getRater(raterID);

				if (rater.hasRating(movieID)) {
					double rating = rater.getRating(movieID) * weight;
					sumRating += rating;
					count += 1;
				}
			}

			if (count >= minimalRaters) {
				Rating newRating = new Rating(movieID, sumRating / (double) count);
				results.add(newRating);
			}
		}
		Collections.sort(results, Collections.reverseOrder());
		return results;

	}

	/**
	 * 
	 * @param id
	 * @param numSimilarRaters
	 * @param minimalRaters
	 * @return This method should return an ArrayList of type Rating, of movies and
	 *         their weighted average ratings using only the top numSimilarRaters
	 *         with positive ratings and including only those movies that have at
	 *         least minimalRaters ratings from those most similar raters (not just
	 *         minimalRaters ratings overall)
	 * @throws IOException
	 */
	public ArrayList<Rating> getSimilarRatingsByFilter(String id, int numSimilarRaters, int minimalRaters,
			Filter aFilter) {
		ArrayList<Rating> similarRaters = getSimilarities(id);
		ArrayList<String> moviesId = MovieDatabase.filterBy(aFilter);
		ArrayList<Rating> results = new ArrayList<Rating>();

		for (String movieID : moviesId) {
			double sumRating = 0.0;
			int count = 0;

			for (int k = 0; k < numSimilarRaters; k++) {
				// get k-th similarest rater and the score
				Rating r = similarRaters.get(k);
				String raterID = r.getItem();
				double weight = r.getValue();

				Rater rater = RaterDatabase.getRater(raterID);

				if (rater.hasRating(movieID)) {
					double rating = rater.getRating(movieID) * weight;
					sumRating += rating;
					count += 1;
				}
			}

			if (count >= minimalRaters) {
				Rating newRating = new Rating(movieID, sumRating / (double) count);
				results.add(newRating);
			}
		}
		Collections.sort(results, Collections.reverseOrder());
		return results;

	}

	public void printSimilarRatings(String id, int numSimilarRaters, int minimalRaters, Filter aFilter) {
		ArrayList<Rating> results = getSimilarRatingsByFilter(id, numSimilarRaters, minimalRaters, aFilter);
		for (Rating aRating : results) {
			System.out.println(MovieDatabase.getTitle(aRating.getItem()) + " " + Double.toString(aRating.getValue()));
		}
	}

	public ArrayList<Rating> getSimilarRatingsByFilters(String id, int numSimilarRaters, int minimalRaters,
			Filter aFilter1, Filter aFilter2) {
		ArrayList<Rating> similarRaters = getSimilarities(id);
		AllFilters aAllFilters = new AllFilters();

		aAllFilters.addFilter(aFilter1);
		aAllFilters.addFilter(aFilter2);

		ArrayList<String> moviesId = MovieDatabase.filterBy(aAllFilters);
		ArrayList<Rating> results = new ArrayList<Rating>();

		for (String movieID : moviesId) {
			double sumRating = 0.0;
			int count = 0;

			for (int k = 0; k < numSimilarRaters; k++) {
				// get k-th similarest rater and the score
				Rating r = similarRaters.get(k);
				String raterID = r.getItem();
				double weight = r.getValue();

				Rater rater = RaterDatabase.getRater(raterID);

				if (rater.hasRating(movieID)) {
					double rating = rater.getRating(movieID) * weight;
					sumRating += rating;
					count += 1;
				}
			}

			if (count >= minimalRaters) {
				Rating newRating = new Rating(movieID, sumRating / (double) count);
				results.add(newRating);
			}
		}
		Collections.sort(results, Collections.reverseOrder());
		return results;

	}

	public void printSimilarRatingsWithTwoFilters(String id, int numSimilarRaters, int minimalRaters, Filter aFilter1,
			Filter aFilter2) {
		ArrayList<Rating> results = getSimilarRatingsByFilters(id, numSimilarRaters, minimalRaters, aFilter1, aFilter2);
		for (Rating aRating : results) {
			System.out.println(MovieDatabase.getTitle(aRating.getItem()) + " " + Double.toString(aRating.getValue()));
		}
	}
}

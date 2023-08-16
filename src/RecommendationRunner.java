import java.util.*;
import java.util.Scanner;
import Databases.*;
import Filters.*;
import Elements.*;
import java.text.DecimalFormat;

public class RecommendationRunner {

	public ArrayList<String> getItemsToRate() {
		ArrayList<String> itemsToRate = new ArrayList<String>();

		ArrayList<String> movies = MovieDatabase.filterBy(new TrueFilter());

		for (int i = 0; i < 12; i++) {
			Random rand = new Random();
			int randomMovieIndex = 0;
			do {
				randomMovieIndex = rand.nextInt(movies.size());
			} while (itemsToRate.contains(movies.get(randomMovieIndex)));
			itemsToRate.add(movies.get(randomMovieIndex));

		}
		return itemsToRate;
	}
	
	public HashMap<String, Integer> getInputs() {
		ArrayList<String> movieIds = getItemsToRate();
		
		HashMap<String, Integer>  results = new HashMap<String, Integer>  ();
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Please give those 5 movies a rate from 1 to 10:");
		for (int i = 0; i < 12; i++) {
			String currentMovieId = movieIds.get(i);
			System.out.println(MovieDatabase.getMovie(currentMovieId));
			int myint = keyboard.nextInt();
			results.put(currentMovieId, myint);
		}
		return results;
	}

	public void printRecommendationsFor(HashMap<String, Integer> webRating) {
		DecimalFormat df = new DecimalFormat("0.00");
		FourthRatings fourthRatings = new FourthRatings();

		MovieDatabase.initialize("ratedmoviesfull");
		RaterDatabase.initialize("ratings");

		System.out.println("Read data for " + Integer.toString(RaterDatabase.size()) + " raters");
		System.out.println("Read data for " + Integer.toString(MovieDatabase.size()) + " movies\n");
		
		String webRaterID = "-1";
		for (String movieId: webRating.keySet()) {
			RaterDatabase.addRaterRating(webRaterID, movieId, webRating.get(movieId));
		}

		int numSimilarRaters = 50;
		int minNumOfRatings = 5;

		ArrayList<Rating> similarRatings = fourthRatings.getSimilarRatings(webRaterID, numSimilarRaters,
				minNumOfRatings);

		if (similarRatings.size() == 0) {
			System.out.println("No matching movies were found");
		} else {
			System.out.println(String.format("%-40s", "Movie Title") + 
					String.format("%-15s", "Rating Value") +
					String.format("%-40s", "Genres"));
			
			int j = 0;
			
			for (Rating rating : similarRatings) {
				String title = MovieDatabase.getTitle(rating.getItem());
				String rate = df.format(rating.getValue());
				String genres = MovieDatabase.getGenres(rating.getItem());
				
				System.out.println(String.format("%-40s", title) + 
						String.format("%-15s", rate) +
						String.format("%-40s", genres));
				j++;
				if (j == 10) {
					break;
				}
			}
		}
	}
	
	public static void main (String[] args) {
		RecommendationRunner aRecommendationRunner = new RecommendationRunner();
		HashMap<String, Integer>  results = aRecommendationRunner.getInputs();
		aRecommendationRunner.printRecommendationsFor(results);
	
	}

}

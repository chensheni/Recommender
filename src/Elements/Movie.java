package Elements;

//An immutable passive data object (PDO) to represent item data
public class Movie {

	/*
	 * id - a String variable representing the IMDB ID of the movie title - a String
	 * variable for the movie’s title year - an integer representing the year genres
	 * - one String of one or more genres separated by commas director - one String
	 * of one or more directors of the movie separated by commas country - one
	 * String of one or more countries the film was made in, separated by commas
	 * minutes - an integer for the length of the movie poster - a String that is a
	 * link to an image of the movie poster if one exists, or “N/A” if no poster
	 * exists
	 */

	private String id;
	private String title;
	private int year;
	private String genres;
	private String director;
	private String country;
	private String poster;
	private int minutes;

	public Movie(String anID, String aTitle, String aYear, String theGenres) {
		// just in case data file contains extra whitespace
		id = anID.trim();
		title = aTitle.trim();
		year = Integer.parseInt(aYear.trim());
		genres = theGenres;
	}

	public Movie(String anID, String aTitle, String aYear, String theGenres, String aDirector, String aCountry,
			String aPoster, int theMinutes) {
		// just in case data file contains extra whitespace
		id = anID.trim();
		title = aTitle.trim();
		year = Integer.parseInt(aYear.trim());
		genres = theGenres;
		director = aDirector;
		country = aCountry;
		poster = aPoster;
		minutes = theMinutes;
	}

	public Movie(String[] anArray) {
		// just in case data file contains extra whitespace
		this(anArray[0], anArray[1], anArray[2], anArray[4], anArray[5], anArray[3], anArray[7],
				Integer.parseInt(anArray[6]));
	}

	// Returns ID associated with this item
	public String getID() {
		return id;
	}

	// Returns title of this item
	public String getTitle() {
		return title;
	}

	// Returns year in which this item was published
	public int getYear() {
		return year;
	}

	// Returns genres associated with this item
	public String getGenres() {
		return genres;
	}

	public String getCountry() {
		return country;
	}

	public String getDirector() {
		return director;
	}

	public String getPoster() {
		return poster;
	}

	public int getMinutes() {
		return minutes;
	}

	// Returns a string of the item's information
	public String toString() {
		String result = "\nMOVIE TITLE: " + title + 
				", produced at " + + year + " in " + country.substring(1, country.length()-1) + "." + 
	"\nGENRES include " + genres.substring(1, genres.length()-1) +  ". " + 
				"DIRECTORS include " + director.substring(1, director.length()-1) + ".";
		return result;
	}
}

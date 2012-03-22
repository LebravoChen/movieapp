package androidnfc.movieapp;

import java.util.Currency;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidnfc.movieapp.common.ImageLoader;
import androidnfc.movieapp.models.Movie;
import androidnfc.movieapp.parsers.FinnkinoXMLParser;
import androidnfc.movieapp.parsers.MovieHandler;

public class MovieDetailsActivity extends Activity {

	private final String XML_PARSER_DEBUG_TAG = "XMLParserActivity";
	public static final String EXTRAS_KEY_IMDB_ID = "IMDB_ID";
	public static final String EXTRAS_KEY_FINNKINO_ID = "FINNKINO_ID";
	private TextView title;
	private TextView year;
	private TextView director;
	private TextView description;
	private TextView rating;
	private TextView cast;
	private ImageView poster;
	private Bitmap bitmapResult;
	private Movie currentMovie;
	final Handler handler = new Handler();
	final Runnable posterExecutor = new Runnable() {
		public void run() {
			displayMovieDetails();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie);

		title = (TextView) findViewById(R.id.movie_title);
		year = (TextView) findViewById(R.id.movie_year);
		director = (TextView) findViewById(R.id.movie_director);
		rating = (TextView) findViewById(R.id.movie_rating);
		cast = (TextView) findViewById(R.id.movie_cast);
		description = (TextView) findViewById(R.id.movie_description);
		poster = (ImageView) findViewById(R.id.movie_poster);

		// TODO Glue for Top panel. This should be integrated in some
		// TopPanelView-widget or so
		{
			ImageView back = (ImageView) findViewById(R.id.topbar_back);
			back.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					finish();
				}
			});
			ImageView search = (ImageView) findViewById(R.id.topbarSearch);
			search.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					Intent intent = new Intent(MovieDetailsActivity.this,
							SearchActivity.class);
					MovieDetailsActivity.this.startActivity(intent);
				}
			});
		}
	}

	@Override
	protected void onStart() {

		super.onStart();
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		if (extras != null) {
			Object o1 = extras.get(EXTRAS_KEY_IMDB_ID);
			Object o2 = extras.get(EXTRAS_KEY_FINNKINO_ID);
			// Just some glue here..
			if (o1 == null || o2 == null) {
				return;
			}
			final int imdbId = (Integer) o1;
			final int finnkinoId = (Integer) o2;
			try {
				// Load stuff async
				// TODO: Add loading-indicator
				Thread t = new Thread() {

					public void run() {
						List<Movie> movies = new FinnkinoXMLParser()
								.parse(finnkinoId);
						if (movies == null || movies.size() != 1) {
							throw new UnsupportedOperationException();
						}

						currentMovie = movies.get(0);

						// TODO: Add image cache
						bitmapResult = ImageLoader.loadImage(currentMovie
								.getImageURL());
						handler.post(posterExecutor);
					}
				};
				t.start();

			} catch (Exception e) {

				Log.e(XML_PARSER_DEBUG_TAG, "XML Parser Error", e);
			}
		}

	}

	private void displayMovieDetails() {
		if (currentMovie != null) {
			title.setText(currentMovie.getTitle());
			year.setText(String.valueOf(currentMovie.getProductionYear()));
			director.setText("Nndirector");
			cast.setText("Some stars");
			rating.setText(currentMovie.getRating());
			Log.i("MovieDetailsActivity", currentMovie.getSynopsis());
			description.setText(currentMovie.getSynopsis());
		}
		if (bitmapResult != null) {
			Log.i("MovieDetailsActivity", "Displayin");
			poster.setImageBitmap(bitmapResult);

		}
	}
}
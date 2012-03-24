package androidnfc.movieapp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidnfc.movieapp.models.SearchResultMovie;
import androidnfc.movieapp.parsers.ImdbJSONParser;

public class SearchActivity extends Activity {

	private Button nfcTagButton;
	private Button xmlParserButton;
	private Button openBrowserButton;
	private Button openMapButton;
	private Button openVideoButton;
	private LinearLayout resultLayout;
	public static String trailerID = ""; 
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		resultLayout = (LinearLayout) findViewById(R.id.resultLayout);
		// TODO Glue for back-button. This should be integrated in some
		// TopPanelView-widget or so
		{
			ImageView back = (ImageView) findViewById(R.id.topbar_back);
			back.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					finish();
				}
			});
		}

		final EditText searchField = (EditText) findViewById(R.id.searchfield);
		ImageView image = (ImageView) findViewById(R.id.searchButton);
		image.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String text = searchField.getText().toString();
				search(text);
			}
		});

		// List<SearchResultMovie> imaginaryResults = new
		// ArrayList<SearchResultMovie>();
		// for (int i = 0; i < 10; i++) {
		// imaginaryResults.add(createFooMovie(i));
		// }
		// setSearchResults(imaginaryResults);
	}

	private void search(String text) {
		Log.d("search", "Searching for " + text);
		// TODO: Cache stuff
		List<SearchResultMovie> results = ImdbJSONParser.create().search(text);
		if (results.size() > 0) {
			setSearchResults(results);
		} else {
			resultLayout.removeAllViewsInLayout();
			TextView resultText = new TextView(getApplicationContext());

			resultText.setText("No results for " + text);
			resultLayout.addView(resultText);
		}

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// TODO Auto-generated method stub
//		MenuInflater inflater = getMenuInflater(); 
//		inflater.inflate(R.menu.menu, menu);
//		return true;
//	}

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//	    switch (item.getItemId()) {
//	        case R.id.showTrailer:
//	        	showTrailer();
//	            return true;
//	        case R.id.help:
//	        	showHelp();
//	            return true;
//	        default:
//	            return super.onOptionsItemSelected(item);
//	    }
//	}

	private void showHelp() {
		// TODO Auto-generated method stub
		
	}

	private void showTrailer() {
		// TODO Auto-generated method stub
		trailerID = "40472";
		if (trailerID.compareTo("") != 0){
			//TEST if adobe flash has been installed
			PackageManager pm = getPackageManager();
			List<ApplicationInfo> appList = pm.getInstalledApplications(PackageManager.GET_META_DATA);
			if (appList.isEmpty()){
				Toast.makeText(getApplicationContext(), "Error",
					      Toast.LENGTH_SHORT).show();
				return;
			}else{
				ApplicationInfo app = null;
				Boolean installedFlag = false;
				for (Iterator<ApplicationInfo> i = appList.iterator(); i.hasNext(); )
					{
					app = i.next();
				    if (app.dataDir.contains("adobe") && app.dataDir.contains("flash")){
				    	installedFlag = true;
				    	break;
				    	}
				    }
				if (installedFlag == false)
					Toast.makeText(getApplicationContext(), "Please install Adobe Flash Player",
									Toast.LENGTH_LONG).show();
			}
			//start web view
			Intent it = new Intent();
			it.setClass(SearchActivity.this, WebDisplay.class);
			it.putExtra("trailerID", trailerID);
			startActivity(it);
		}else{
			Toast.makeText(getApplicationContext(), "XML parser did not pass the trailer ID",
				      Toast.LENGTH_SHORT).show();
			return;
		}
	}

	private void setSearchResults(List<SearchResultMovie> results) {

		resultLayout.removeAllViewsInLayout();
		TextView resultText = new TextView(getApplicationContext());

		resultText.setText("Results");
		resultLayout.addView(resultText);

		// TODO: Fix this line, doesn't show up for some reason. Also Can't set
		// dynamic styles yet, so create a line with hardcoded content
		View line = new View(getApplicationContext());
		line.setBackgroundColor(0x515151);
		resultLayout.addView(line, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 2));

		ListView list = new ListView(getApplicationContext());
		resultLayout.addView(list);

		final SearchResultMovie[] res = new SearchResultMovie[results.size()];
		ResultArrayAdapter adapter = new ResultArrayAdapter(
				getApplicationContext(), results.toArray(res));
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				SearchResultMovie model = res[position];

				Intent intent = new Intent(SearchActivity.this,
						MovieDetailsActivity.class);
				intent.putExtra(MovieDetailsActivity.EXTRAS_KEY_IMDB_ID,
						model.getImdbId());
				intent.putExtra(MovieDetailsActivity.EXTRAS_KEY_FINNKINO_ID,
						model.getFinnkinoId());
				SearchActivity.this.startActivity(intent);
			}
		});

	}

	public class ResultArrayAdapter extends ArrayAdapter<SearchResultMovie> {
		private final Context context;
		private final SearchResultMovie[] values;

		public ResultArrayAdapter(Context context, SearchResultMovie[] values) {
			super(context, R.layout.searchresult, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.searchresult, parent,
					false);
			TextView name = (TextView) rowView.findViewById(R.id.resultName);
			TextView desc = (TextView) rowView
					.findViewById(R.id.resultDescription);
			name.setText(values[position].getTitle());
			desc.setText(String.valueOf(values[position].getDescription()));
			return rowView;
		}
	}

}

package androidnfc.movieapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;


public class MovieappActivity extends Activity {
	/** Called when the activity is first created. */
	private Button nfcTagButton;
	private Button xmlParserButton;
	private Button openBrowserButton;
	private Button openMapButton;
	private Button openVideoButton;
	private Button inTheatersButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        nfcTagButton = (Button)findViewById(R.id.nfcTagButton);
        
        xmlParserButton = (Button)findViewById(R.id.xmlParserButton);
        xmlParserButton.setOnClickListener(new xmlParserOnClickListener());
        
        openBrowserButton = (Button)findViewById(R.id.openBrowserButton);
        openBrowserButton.setOnClickListener(new openBrowserOnClickListener());
        
        inTheatersButton = (Button)findViewById(R.id.inTheatersButton);
        inTheatersButton.setOnClickListener(new inTheatersOnClickListener());
        
    }
    
    private final class xmlParserOnClickListener implements OnClickListener {
		public void onClick(View v) {
			Intent intent = new Intent(MovieappActivity.this,
									   XMLParserActivity.class);
			MovieappActivity.this.startActivity(intent);
		}
    }
    
    //call web view
    private final class openBrowserOnClickListener implements OnClickListener {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent it = new Intent();
			it.setClass(MovieappActivity.this, WebDisplay.class);
			startActivity(it);
		}
    }
    
    private final class inTheatersOnClickListener implements OnClickListener {
		public void onClick(View v) {
			Intent intent = new Intent(MovieappActivity.this,
									   MovieScheduleActivity.class);
			MovieappActivity.this.startActivity(intent);
		}
    }

}

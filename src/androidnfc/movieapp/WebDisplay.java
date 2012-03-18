/**
 * 
 */
package androidnfc.movieapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.util.Log;

/**
 * @author Ken
 *
 */
public class WebDisplay extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webdisplay);
		//create a web view
		WebView myWebView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = myWebView.getSettings();
		PluginState pluginState = webSettings.getPluginState();
		Log.d("webDisplay", "get plugin state is ---> " + pluginState);
		if (pluginState == PluginState.OFF){
			webSettings.setPluginState(PluginState.ON_DEMAND);
		}
		Log.d("webDisplay", "get plugin state is ---> " + pluginState);
		/**change the following line to the URL where the trailer is e.g.
		 * http://www.traileraddict.com/player.swf?id=31763&e=y
		 * but this require to install Adobe Flash
		 * **/
//		myWebView.loadUrl("http://www.google.com");
		myWebView.loadUrl("http://www.traileraddict.com/player.swf?id=31763&e=y");
	}
	
}

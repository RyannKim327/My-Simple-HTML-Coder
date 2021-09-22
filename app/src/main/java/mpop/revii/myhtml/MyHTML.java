package mpop.revii.myhtml;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.os.Handler;

public class MyHTML extends Activity{
    WebView wv;
    SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pref = getSharedPreferences(MainActivity.PREF_NAME,MODE_PRIVATE);
		setTitle("HTML Viewer");
		wv = new WebView(this);
		
		/* WebView Settings */
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setSupportZoom(true);
		
		/* Defaults */
		wv.setWebChromeClient(new WebChromeClient());
		wv.setWebViewClient(new WebViewClient());
		
		/* load the data from shared preferences */
		wv.loadData(pref.getString(MainActivity.PREF_CODE,""),"text/html","UTF-8");
		
		/* To get the title of your code */
		new Handler().postDelayed(new Runnable(){
				@Override
				public void run() {
					getActionBar().setSubtitle(wv.getTitle().toString());
				}
			}, 1000);
		
		setContentView(wv);
	}
}

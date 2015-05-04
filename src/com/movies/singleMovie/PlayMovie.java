package com.movies.singleMovie;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.kaltura.playersdk.PlayerViewController;
import com.kaltura.playersdk.events.KPlayerEventListener;
import com.kaltura.playersdk.events.KPlayerJsCallbackReadyListener;
import com.kaltura.playersdk.events.OnToggleFullScreenListener;
import com.movies.bongobd.R;
import com.tab.ShareData;

public class PlayMovie extends Activity implements OnToggleFullScreenListener
{
	public WebView webView1;
//	public ImageView btnBack;
	public PlayerViewController mPlayerView;
	
	public static String DEBUG_TAG = PlayMovie.class.getSimpleName();
	 OrientationEventListener mOrientationListener;
	 public PlayMovie instance;
	 public int counts;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		setContentView(R.layout.play_movie);
		instance = this;
		counts = 0;
		
		mPlayerView = (PlayerViewController) findViewById(R.id.webView2);
		mPlayerView.addComponents
		("http://cdnapi.kaltura.com/html5/html5lib/v2.7/mwEmbedFrame.php/p/1868701/uiconf_id/29196262/entry_id/0_7divdd1y?wid=_1868701&entry_id=0_7divdd1y&iframeembed=true", this );
		//https://cdnapisec.kaltura.com/html5/html5lib/v2.21/mwEmbedFrame.php/p/1868701/uiconf_id/28968212/entry_id/0_0duow8rg?wid=_1868701&iframeembed=true&playerId=kaltura_player_1428490836&entry_id=0_0duow8rg
		//http://cdnapi.kaltura.com/p/243342/sp/243342/embedIframeJs/uiconf_id/23389712/partner_id/243342?entry_id=0_uka1msg4&iframeembed=true
		mPlayerView.registerJsCallbackReady(new KPlayerJsCallbackReadyListener() {
            @Override
            public void jsCallbackReady() {

                mPlayerView.play();
            	mPlayerView.addKPlayerEventListener("doPause",
						new KPlayerEventListener() {
							@Override
							public void onKPlayerEvent(Object body) {
//								getWindow()
//										.clearFlags(
//												WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
							}

							@Override
							public String getCallbackName() {
								// TODO Auto-generated method stub
								return "EventListenerDoPause";
							}
            	});
            }
        });
		 mPlayerView.setOnFullScreenListener(this);
        
//		btnBack = (ImageView)findViewById(R.id.btnPress);
//		RelativeLayout.LayoutParams btnBackParams = new RelativeLayout.LayoutParams(ShareData.padding10*5, ShareData.padding10*5);
//		btnBackParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//		btnBackParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
////		btnBackParams.setMargins(ShareData.padding10, ShareData.padding10, 0, 0);
//		btnBack.setLayoutParams(btnBackParams);
////		btnBack.setPadding(0, ShareData.padding10*2, 0, 0);
//		btnBack.setOnClickListener(new View.OnClickListener() 
//		{
//			@Override
//			public void onClick(View arg0) 
//			{
//				// TODO Auto-generated method stub
//				finish();
//				startActivity(new Intent(getBaseContext(), SingleMoviePage.class));
//				overridePendingTransition( R.anim.animation1, R.anim.animation2 );
//			}
//		});
		 
		 onRotate();
	}
	
	public void onRotate()
	{
	    mOrientationListener = new OrientationEventListener(instance,
	            SensorManager.SENSOR_DELAY_NORMAL) {

	            @Override
	            public void onOrientationChanged(int orientation) 
	            {
//	                Log.v(DEBUG_TAG,
//	                   "Orientation changed to " + orientation);
	                int orientations = getResources().getConfiguration().orientation; 
	     	        if (Configuration.ORIENTATION_LANDSCAPE == orientations ) 
	     	        { 
	     	        	Log.d(DEBUG_TAG, "rrrr:"+rotate());
	     	        	if(rotate()==3)
	     	        	{
//	     	        		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
	     	        		 counts++;
							 if(counts==1)
							 {
								 mPlayerView.stop();
								 finish();
								 startActivity(new Intent(getBaseContext(), SingleMoviePage.class));
							 }
	     	        	}
	     	        	
	     	        	
	     	        }  
	     	        else
	     	        { 
	     	           //Do SomeThing;  // landscap
	     	       } 
	            }
	        };

	       if (mOrientationListener.canDetectOrientation() == true) {
	           Log.v(DEBUG_TAG, "Can detect orientation");
	           mOrientationListener.enable();
	       } else {
	           Log.v(DEBUG_TAG, "Cannot detect orientation");
	           mOrientationListener.disable();
	       }
	}
	
	public int rotate()
	{
		int rotation = getWindowManager().getDefaultDisplay().getRotation();
		return rotation;
	}
	@Override
	public void onBackPressed()
	{
	    super.onBackPressed();

//	    if(webView1!=null)
//	    {
//	    	webView1.stopLoading();
////			webView.setWebChromeClient(null);
////			webView.setWebViewClient(null);
//			webView1.destroy();
//			webView1 = null;
//	    }
		finish();
		startActivity(new Intent(getBaseContext(), SingleMoviePage.class));
		overridePendingTransition( R.anim.animation1, R.anim.animation2 );
		
		
		
//		System.runFinalizersOnExit(true);
//		System.exit(0);
//		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
//		if(webView1!=null)
//		{
//		    webView1.stopLoading();
////			webView.setWebChromeClient(null);
////			webView.setWebViewClient(null);
//			webView1.destroy();
//			webView1 = null;
//	    }
	}

	@Override
	public void onToggleFullScreen() {
		// TODO Auto-generated method stub
		 finish();
		 startActivity(new Intent(getBaseContext(), SingleMoviePage.class));
	}
}

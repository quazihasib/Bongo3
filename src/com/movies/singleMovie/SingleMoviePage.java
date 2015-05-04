package com.movies.singleMovie;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kaltura.playersdk.PlayerViewController;
import com.kaltura.playersdk.events.KPlayerEventListener;
import com.kaltura.playersdk.events.KPlayerJsCallbackReadyListener;
import com.kaltura.playersdk.events.OnToggleFullScreenListener;
import com.movies.actorProfile.ActorProfile;
import com.movies.bongobd.R;
import com.movies.browseAll.ListViewAdapter;
import com.movies.browseAll.Movies;
import com.movies.categoryPage.CategoryLanding;
import com.movies.movieSummary.MovieSummary;
import com.movies.startingPage.StartingPage;
import com.tab.AddMenu;
import com.tab.Header;
import com.tab.ShareData;

public class SingleMoviePage extends Activity implements OnToggleFullScreenListener 
{
	public static String movieShortSummary;
	public static String movieCategory;
	public static String moviePostedOn;
	public static String movieAvgRating;
	public static String jsonData;
	public static float movieRating;
	public static float userRateStatus;

	public static String movieReleaseDate;
	public String movieDuration;
	public static String movieGenre;
	public String movieContentDetails;
	public static String movieUrl;
	public String rate_status;
	public String position;
	public String formattedYear, formattedMonth, formattedDay;
	public String movieName;
	public String movieDirector;
	public String movieViews;
	public String movieImage;
	public static String movieSummary;
	public String detailsId, roleName, roleId, artistProfileImage, contentId, artistId, artistName;
	public String id;

//	public WebView webView;
	public ViewGroup.LayoutParams vc, vc1;

	public TextView  tvMovieName, tvDirector, tvViews, tvViewsLabel, tvDirectorLabel,
			tvMovieCategory, tvPostedOn, tvShortSummary;
	public Button btnRate;

	public int height, width, count;
	public static int relatedHeight;
	public int finalHeight, finalWidth;
	public int counter;
	
	public RequestQueue requestQueue;
	public ProgressDialog pDialog;

	public LinearLayout directorLayout, infoLayout;
	public JSONArray cast;
	public JSONObject data, additionalData, userData;
	
	public String[] relatedImgUrls;
	public String[] relatedTitles;
	public String[] relatedViews;
	public String[] relatedIds;
	public String[] relatedContentLength;
	public String[] relatedGenre;

	public static SingleMoviePage singleMovieInstance;
	public static String DEBUG_TAG = SingleMoviePage.class.getSimpleName();

	public static TextView tv;
	public static String userId;
	LinearLayout addTabs;
    int counts=0, counts1=0;
	TabResult tb;
	
	public Header h;
	PlayerViewController mPlayerView;
	public LinearLayout singleMovieMainLayout, singleMovieDetails, layoutMain, relatedLayout;
	OrientationEventListener mOrientationListener;
	 
	public com.tab.Header headerLayout;
	public ScrollView scrollLayout;
	public RelativeLayout relatedWebView;
	public ImageView btnFullScreen;
	public boolean _isFullScreen;
	public String movieID;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		setContentView(R.layout.single_movie_page);

		singleMovieInstance = this;
		h = AddMenu.HeaderFunction(singleMovieInstance);
		jsonData = "";
		AddMenu.pageNumber = 4;
		_isFullScreen = false;
		movieID="";
		
		RelatedContents.addLayout=true;
		RelatedContents.seeMoreCounter=3;
		
		StartingPage.movieSummaryPageReturn = 1;
		addTabs = (LinearLayout)findViewById(R.id.tabs);
		
		headerLayout = (com.tab.Header)findViewById(R.id.headers);
		singleMovieMainLayout = (LinearLayout)findViewById(R.id.singleMovieMainLayout);
		singleMovieDetails = (LinearLayout)findViewById(R.id.singleMovieDetails);
		layoutMain = (LinearLayout)findViewById(R.id.layoutMain);
		relatedLayout = (LinearLayout)findViewById(R.id.relatedLayout);
		relatedWebView = (RelativeLayout)findViewById(R.id.relatedWebView);
		
		scrollLayout = (ScrollView)findViewById(R.id.scroll);
		
		if(CategoryLanding.catagoryName!=null)
		{
			if(CategoryLanding.catagoryName.trim().equals("movies"))
			{
				h.tv1.setBackgroundColor(Color.parseColor("#B40404"));
				h.tv2.setBackgroundColor(Color.parseColor("#FF0000"));
				h.tv3.setBackgroundColor(Color.parseColor("#FF0000"));
				h.tv4.setBackgroundColor(Color.parseColor("#FF0000"));
			}
			else if(CategoryLanding.catagoryName.trim().equals("tv"))
			{
				h.tv1.setBackgroundColor(Color.parseColor("#FF0000"));
				h.tv2.setBackgroundColor(Color.parseColor("#B40404"));
				h.tv3.setBackgroundColor(Color.parseColor("#FF0000"));
				h.tv4.setBackgroundColor(Color.parseColor("#FF0000"));
			}
			else if(CategoryLanding.catagoryName.trim().equals("music"))
			{
				h.tv1.setBackgroundColor(Color.parseColor("#FF0000"));
				h.tv2.setBackgroundColor(Color.parseColor("#FF0000"));
				h.tv3.setBackgroundColor(Color.parseColor("#B40404"));
				h.tv4.setBackgroundColor(Color.parseColor("#FF0000"));
			}
		}
		
		tb = new TabResult(singleMovieInstance);
//		VideoCounter.checkVideoCounter(singleMovieInstance);

		tvDirector = (TextView) findViewById(R.id.tvDirectorName);
		tvDirector.setTextColor(Color.GRAY);
		tvDirector.setTypeface(ShareData.RobotoFont(singleMovieInstance));
		tvDirector.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)singleMovieInstance.getResources().getDimension(R.dimen.text_size2));
		
		tvDirectorLabel = (TextView) findViewById(R.id.tvDirectorLabel);
		tvDirectorLabel.setTextColor(Color.GRAY);
		tvDirectorLabel.setTypeface(ShareData.RobotoFont(singleMovieInstance));
		tvDirectorLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)singleMovieInstance.getResources().getDimension(R.dimen.text_size2));
		
		tvViews = (TextView) findViewById(R.id.tvViews);
		tvViews.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)singleMovieInstance.getResources().getDimension(R.dimen.text_size2));
		tvViews.setTextColor(Color.GRAY);
		tvViews.setTypeface(ShareData.RobotoFont(singleMovieInstance));
		
		tvViewsLabel = (TextView) findViewById(R.id.tvViewsLabel);
		tvViewsLabel.setTextColor(Color.GRAY);
		tvViewsLabel.setTypeface(ShareData.RobotoFont(singleMovieInstance));
		tvViewsLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)singleMovieInstance.getResources().getDimension(R.dimen.text_size2));
		
		tvMovieName = (TextView) findViewById(R.id.tvMovieName);
		tvMovieName.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
//		tvMovieName.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
		tvMovieName.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)singleMovieInstance.getResources().getDimension(R.dimen.text_size1));
		tvMovieName.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				finish();
				Intent i = new Intent(getBaseContext(), MovieSummary.class);
				i.putExtra("movieName", movieName);
				i.putExtra("movieImage", movieImage);
				i.putExtra("movieSummary", movieSummary);
				i.putExtra("movieDuration", movieDuration);
				i.putExtra("movieReleaseDate", movieReleaseDate);
				i.putExtra("movieGenre", movieGenre);

				jsonData = data.toString();
				i.putExtra("json", data.toString());
				// i.putExtra("roleName", roleName);
				// i.putExtra("artistName", artistName);

				// go to Movie Summary page
				startActivity(i);
				overridePendingTransition(R.anim.animation1, R.anim.animation2);			
			}
		});

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		height = displaymetrics.heightPixels;
		width = displaymetrics.widthPixels;

		relatedHeight = height;
		
		mPlayerView = (PlayerViewController) findViewById( R.id.webView1 );
//		mPlayerView.addComponents
//		("http://cdnapi.kaltura.com/html5/html5lib/v2.7/mwEmbedFrame.php/p/1868701/uiconf_id/29196262/entry_id/0_7divdd1y?wid=_1868701&entry_id=0_7divdd1y&iframeembed=true", this );
		vc = mPlayerView.getLayoutParams();
		vc.height = height / 2;
		Log.d("Screen Height:", "vc.height:" + vc.height);
		vc1 = relatedWebView.getLayoutParams();
		vc1.height = height/2;
		relatedWebView.setLayoutParams(vc1);
		mPlayerView.setLayoutParams(vc);
		mPlayerView.setPlayerViewDimensions(vc.width , vc.height);
		
		mPlayerView.registerJsCallbackReady(new KPlayerJsCallbackReadyListener()
		{
            @Override
            public void jsCallbackReady()
            {
                mPlayerView.play();
            	mPlayerView.addKPlayerEventListener("doPause",
						new KPlayerEventListener() 
            			{
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
		mPlayerView.setOnFullScreenListener(singleMovieInstance);
//		webView = (WebView) findViewById(R.id.webView1);
//		webView.getSettings().setJavaScriptEnabled(true);
//		webView.getSettings().setUseWideViewPort(true);
//		webView.getSettings().setLoadWithOverviewMode(true);
//		webView.getSettings().setPluginState(PluginState.ON);
//		vc = webView.getLayoutParams();
//		vc.height = height / 2;
//		Log.d("Screen Height:", "vc.height:" + vc.height);
//		vc1 = relatedWebView.getLayoutParams();
//		vc1.height = height/2;
//		relatedWebView.setLayoutParams(vc1);
//		webView.setLayoutParams(vc);
		
//		btnFullScreen = (ImageView)findViewById(R.id.btnFullScreen);
//		btnFullScreen.setVisibility(View.INVISIBLE);
//		RelativeLayout.LayoutParams btnFullScreenParams = new RelativeLayout.LayoutParams(height/20, height/20);
//		btnFullScreenParams.setMargins(ShareData.padding10*3, 0, 0, 0); 
//		btnFullScreenParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//		btnFullScreenParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//		btnFullScreen.setLayoutParams(btnFullScreenParams);
//		btnFullScreen.setPadding(0,  ShareData.padding10, 0, 0);
//		btnFullScreen.setOnClickListener(new View.OnClickListener() 
//		{
//			@Override
//			public void onClick(View v)
//			{
//				// TODO Auto-generated method stub
//				 Log.d(DEBUG_TAG, "rotate::"+rotate());
//				 if(rotate()==1 || rotate()==3)
//				 {
//					 //potrait
//					 if(singleMovieMainLayout.getChildAt(0) != headerLayout)
//					 {
//						 LinearLayout.LayoutParams headerAParams = new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//						 headerLayout.setLayoutParams(headerAParams);
//						 singleMovieMainLayout.addView(headerLayout,0);
//					 }
//					 if(layoutMain.getChildAt(1)!=singleMovieDetails)
//					 {
//						 layoutMain.addView(singleMovieDetails,1);
//					 }
//					 if(layoutMain.getChildAt(2)!=relatedLayout)
//					 {
//						 layoutMain.addView(relatedLayout,2);
//					 }
//					 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//					 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//					 
////					 relatedWebView.getLayoutParams().height = height/2;
////					 webView.getLayoutParams().height =  height/2;
////				
////					 RelativeLayout.LayoutParams btnFullScreenParams = new RelativeLayout.LayoutParams(height/20, height/20);
////					 	btnFullScreenParams.setMargins(ShareData.padding10*3, 0, 0, 0); 
////						btnFullScreenParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
////						btnFullScreenParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
////						btnFullScreen.setLayoutParams(btnFullScreenParams);
////						btnFullScreen.setPadding(0, ShareData.padding10, 0, 0);
//				 }
//				 else
//				 {
//					 //landscape
//					 singleMovieMainLayout.removeView(headerLayout);
//					 layoutMain.removeView(singleMovieDetails);
//					 layoutMain.removeView(relatedLayout);
//					 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//					
////					 relatedWebView.getLayoutParams().height =  (int) (ShareData.getScreenHeight(singleMovieInstance));
////					 webView.getLayoutParams().height =  (int) (ShareData.getScreenHeight(singleMovieInstance));
////				
////					 
////					 RelativeLayout.LayoutParams btnFullScreenParams = new RelativeLayout.LayoutParams(width/9, width/10);
////						btnFullScreenParams.setMargins(0, 0, 0, 0);
////						btnFullScreenParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
////						btnFullScreenParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
////						btnFullScreen.setLayoutParams(btnFullScreenParams);
////						btnFullScreen.setPadding(0, 0, 0, 0);
//				 }
//			}
//		});
		
//		mPlayerView.setPlayerViewDimensions( vc.width , vc.height);
//		mPlayerView.addComponents
//		("http://cdnapi.kaltura.com/html5/html5lib/v2.7/mwEmbedFrame.php/p/1868701/uiconf_id/29196262/entry_id/0_7divdd1y?wid=_1868701&entry_id=0_7divdd1y&iframeembed=true", this );
//		//https://cdnapisec.kaltura.com/html5/html5lib/v2.21/mwEmbedFrame.php/p/1868701/uiconf_id/28968212/entry_id/0_0duow8rg?wid=_1868701&iframeembed=true&playerId=kaltura_player_1428490836&entry_id=0_0duow8rg
//		//http://cdnapi.kaltura.com/p/243342/sp/243342/embedIframeJs/uiconf_id/23389712/partner_id/243342?entry_id=0_uka1msg4&iframeembed=true
//		mPlayerView.registerJsCallbackReady(new KPlayerJsCallbackReadyListener() {
//            @Override
//            public void jsCallbackReady() {
//
//                mPlayerView.play();
//            	mPlayerView.addKPlayerEventListener("doPause",
//						new KPlayerEventListener() {
//							@Override
//							public void onKPlayerEvent(Object body) {
////								getWindow()
////										.clearFlags(
////												WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//							}
//
//							@Override
//							public String getCallbackName() {
//								// TODO Auto-generated method stub
//								return "EventListenerDoPause";
//							}
//            	});
//            }
//        });
//		 mPlayerView.setOnFullScreenListener(singleMovieInstance);
	   
//		mPlayerView.setHorizontalScrollBarEnabled(true);
		
		counts=0;
		counts1=0;
//		webView.setOnTouchListener(new View.OnTouchListener() 
//		{
//			 @Override
//			 public boolean onTouch(View v, MotionEvent event) 
//			 {
//				 // TODO Auto-generated method stub
//		
//				 if(event.getAction()==MotionEvent.ACTION_DOWN)
//				 {
//					 counts++;
//					 if(counts==1)
//					 {
//						 finish();
//						 startActivity(new Intent(getBaseContext(), PlayMovie.class));
//					 }
//				 }
//				 else if(event.getAction()==MotionEvent.ACTION_UP)
//				 {
//					 counts=0;
//				 }
//				
////				 System.runFinalizersOnExit(true);
////			    	System.exit(0);
////				 android.os.Process.killProcess(android.os.Process.myPid());
//				 return false;
//			 }
//		 });

		// id = ShareData.loadSavedPreferences(singleMovieInstance, "id");

		// addListenerOnRatingBar();
		// addFullScreenButton();
		
		Intent i = getIntent();
		// id = ShareData.getSavedString(singleMovieInstance,
		// "ListViewAdapterMovieId");
		id = i.getStringExtra("movieId");

		Log.d(DEBUG_TAG, "StartingPage.value:"+StartingPage.value);
		
		userId = ShareData.getSavedString(singleMovieInstance, "id");
//		userId="305";
//		ListViewAdapter.singleMovieId = "11";
		if(userId!=null && userId.trim().length()!=0)
		{
			makeJsonObjectRequest("http://bongobd.com/api/content.php?id="+ ListViewAdapter.singleMovieId+"&user_id="+userId);
			Log.d(DEBUG_TAG, "browse user signed in:http://bongobd.com/api/content.php?id="+ ListViewAdapter.singleMovieId+"&user_id="+userId);
		}
		else
		{
			makeJsonObjectRequest("http://bongobd.com/api/content.php?id="+ ListViewAdapter.singleMovieId);
			Log.d(DEBUG_TAG, "browse:http://bongobd.com/api/content.php?id="+ ListViewAdapter.singleMovieId);
		}
	
		
			
		infoLayout = (LinearLayout) findViewById(R.id.infoLayout);
		directorLayout = (LinearLayout) infoLayout
				.findViewById(R.id.directorLayout);
		tb.addTabs();
		tb.addListenerOnRatingBar();
		
		onRotate();
	 }
	
	public void onRotate()
	{
	    mOrientationListener = new OrientationEventListener(singleMovieInstance,
	            SensorManager.SENSOR_DELAY_NORMAL) 
	    { 
	            @Override
	            public void onOrientationChanged(int orientation) 
	            {
//	                Log.d(DEBUG_TAG,"Orientation changed to " + orientation);
	                int orientations = getResources().getConfiguration().orientation; 
	     	        if (Configuration.ORIENTATION_LANDSCAPE == orientations) 
	     	        { 
	     	             //Do SomeThing; // Landscape
	     	        	 Log.d(DEBUG_TAG, "LANDSCAPE");
	     	        	 
	     	        	_isFullScreen = true;
	     	        	 counts1=0;
	     	        	 counts++;
						 if(counts==1)
						 {
							 singleMovieMainLayout.removeView(headerLayout);
							 layoutMain.removeView(singleMovieDetails);
							 layoutMain.removeView(relatedLayout);
							 
							 relatedWebView.getLayoutParams().height =  (int) (ShareData.getScreenHeight(singleMovieInstance)-ShareData.getScreenHeight(singleMovieInstance)/15);
							 mPlayerView.getLayoutParams().height =  (int) (ShareData.getScreenHeight(singleMovieInstance)-ShareData.getScreenHeight(singleMovieInstance)/15);
							 mPlayerView.setPlayerViewDimensions(vc.width,  (int) (ShareData.getScreenHeight(singleMovieInstance)-ShareData.getScreenHeight(singleMovieInstance)/15));
							 
//							 RelativeLayout.LayoutParams btnFullScreenParams = new RelativeLayout.LayoutParams(width/9, width/10);
//								btnFullScreenParams.setMargins(0, 0, 0, 0);
//								btnFullScreenParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//								btnFullScreenParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//								btnFullScreen.setLayoutParams(btnFullScreenParams);
//								btnFullScreen.setPadding(0, 0, 0, 0);
						 }
						 
						 Log.d(DEBUG_TAG, "rotate::"+rotate());
						 if(rotate()==3)
						 {
							 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
						 }
	     	        }  
	     	        else  if(Configuration.ORIENTATION_PORTRAIT== orientations) 
	     	        {
	     	        	counts=0;
	     	        	counts1++;
	     	        	_isFullScreen = false;
						 if(counts1==1)
						 {
							 if(singleMovieMainLayout.getChildAt(0) != headerLayout)
							 {
								 LinearLayout.LayoutParams headerAParams = new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
								 headerLayout.setLayoutParams(headerAParams);
								 singleMovieMainLayout.addView(headerLayout,0);
							 }
							 if(layoutMain.getChildAt(1)!=singleMovieDetails)
							 {
								 layoutMain.addView(singleMovieDetails,1);
							 }
							 if(layoutMain.getChildAt(2)!=relatedLayout)
							 {
								 layoutMain.addView(relatedLayout,2);
							 }
							 
							 
							 relatedWebView.getLayoutParams().height = height/2;
							 mPlayerView.getLayoutParams().height =  height/2;
							 mPlayerView.setPlayerViewDimensions(vc.width , height/2);
//							 
//							 RelativeLayout.LayoutParams btnFullScreenParams = new RelativeLayout.LayoutParams(height/20, height/20);
//							 	btnFullScreenParams.setMargins(ShareData.padding10*3, 0, 0, 0); 
//								btnFullScreenParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//								btnFullScreenParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//								btnFullScreen.setLayoutParams(btnFullScreenParams);
//								btnFullScreen.setPadding(0, ShareData.padding10, 0, 0);
						 }
	     	        	Log.d(DEBUG_TAG, "POTRAIT");
	     	       } 
	            }
	        };

	       if (mOrientationListener.canDetectOrientation() == true)
	       {
	           Log.v(DEBUG_TAG, "Can detect orientation");
	           mOrientationListener.enable();
	       }
	       else 
	       {
	           Log.v(DEBUG_TAG, "Cannot detect orientation");
	           mOrientationListener.disable();
	       }
	}
	
	public int rotate()
	{
		int rotation = getWindowManager().getDefaultDisplay().getRotation();
		return rotation;
	}

	public void makeJsonObjectRequest(String urlJsonObj) 
	{
//		showpDialog();
		counter=0;
		relatedImgUrls=null;
		relatedTitles = null;
		relatedViews = null;
		relatedIds = null;
		relatedContentLength = null;
		relatedGenre = null;
		
		requestQueue = Volley.newRequestQueue(this);
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
				urlJsonObj, null, new Response.Listener<JSONObject>()
				{
					@Override
					public void onResponse(JSONObject response) 
					{
						// Log.d(DEBUG_TAG, response.toString());
						try 
						{
							data = response.getJSONObject("data");
							// Log.d(DEBUG_TAG, "data: "+data);
							if(userId.trim().length()!=0)
							{
								userData = data.getJSONObject("content_user_data");
								Log.d(DEBUG_TAG, "userData:"+userData);
								rate_status = userData.getString("rate_status");
								if(!rate_status.trim().equals("null"))
								{
									userRateStatus = Integer.parseInt(rate_status);
									Log.d(DEBUG_TAG, "userRateStatus:"+userRateStatus);
								}
							}
							else
							{
								Log.d(DEBUG_TAG, "No User loged in");
							}
							additionalData = response.getJSONObject("additionalData");
							// Log.d(DEBUG_TAG, " additionalData:"+additionalData );
							movieUrl = additionalData.getString("iframe2");
							Log.d(DEBUG_TAG, " movieUrl:"+ movieUrl );
							String[] array = movieUrl.split("=");
							int len = array.length;
							movieID = array[len-1].trim();
							Log.d(DEBUG_TAG, "movieeeeeIDDD:"+ movieID );

							movieName = data.getString("content_title");
							// Log.d(DEBUG_TAG, "movieName:"+movieName );
							
							movieGenre = data.getString("genre");
							Log.d(DEBUG_TAG, "movieGenre:"+movieGenre );
							
							movieImage = "http://bongobd.com/wp-content/themes/bongobd/images/posterimage/thumb/"
									+ data.getString("content_thumb");
							// Log.d(DEBUG_TAG, "movieImage:"+movieImage );

							movieDirector = data.getString("by");
							// Log.d(DEBUG_TAG,
							// "movieDirector: "+movieDirector);
							// if there is no director then delete it
							if (movieDirector.length() == 0)
							{
								infoLayout.removeView(directorLayout);
							} 
							else
							{
								tvDirector.setText(movieDirector);
							}

							movieViews = data.getString("total_view");
							// Log.d(DEBUG_TAG, "movieViews: "+movieViews);

							movieCategory = data.getString("category_name");
							// Log.d(DEBUG_TAG,
							// "movieCategory: "+movieCategory);

							moviePostedOn = data.getString("entry_time");
							// Log.d(DEBUG_TAG,
							// "moviePostedOn: "+moviePostedOn);

							movieShortSummary = data.getString("content_short_summary");
							// Log.d(DEBUG_TAG, "movieShortSummary: "+movieShortSummary);

							movieSummary = data.getString("content_summary");
							Log.d(DEBUG_TAG, "movieSummary: "+movieSummary);

							movieReleaseDate = data.getString("release_date");
							movieReleaseDate = ShareData.dateFix(movieReleaseDate);
							Log.d(DEBUG_TAG, "movieReleaseDate11: "+movieReleaseDate);

							movieAvgRating = data.getString("avg_rating");
							//Log.d(DEBUG_TAG, "movieAvgRating2222:"+movieAvgRating.length());
							Log.d(DEBUG_TAG, "movieAvgRating string value:"+movieAvgRating);
							
							if(movieAvgRating.equals("null"))
							{
								Log.d(DEBUG_TAG, "movieAvgRating null:");
								movieRating=0;
							}
							else if(!movieAvgRating.equals("null"))
							{
								Log.d(DEBUG_TAG, " not null movieAvgRating:");
								Log.d(DEBUG_TAG, "movieAvgRating:"+movieAvgRating);
								movieRating = Float.parseFloat(movieAvgRating);
								Log.d(DEBUG_TAG, "movieRating:"+movieRating);
							}
							
							if (data.has("content_details")) 
							{
								movieContentDetails = data.getString("content_details");
								// Log.d(DEBUG_TAG,
								// "movieContentDetails: "+movieContentDetails);

								// Parse Movie Casts
								ArrayList<String> allNames = new ArrayList<String>();
								cast = data.getJSONArray("content_details");
								for (int i = 0; i < cast.length(); i++) 
								{
									JSONObject actor = cast.getJSONObject(i);
									detailsId = actor.getString("details_id");
									contentId = actor.getString("content_id");
									artistId = actor.getString("artist_id");
									artistName = actor.getString("artist_name");
									artistProfileImage = actor.getString("artist_profile_image");
									roleName = actor.getString("role_name");
									roleId = actor.getString("role_id");
								}
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
//							Toast.makeText(getApplicationContext(),
//									"Error: " + e.getMessage(),
//									Toast.LENGTH_LONG).show();
						}
						hidepDialog();
					}
				}, new Response.ErrorListener() 
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						VolleyLog.d(DEBUG_TAG, "Error: " + error.getMessage());
//						Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
						// hide the progress dialog
						hidepDialog();
					}
				});

		// Adding request to request queue
		requestQueue.add(jsonObjReq);
	}
	
	public void hidepDialog() 
	{
		tvViews.setText(movieViews);
		tvMovieName.setText(movieName);
		
		mPlayerView.addComponents
		("https://cdnapisec.kaltura.com/html5/html5lib/v2.21/mwEmbedFrame.php/p/1868701/uiconf_id/29233222/entry_id/"+movieID+"?wid=_1868701&iframeembed=true&playerId=kaltura_player_1428490836&entry_id="+movieID, this );
		Log.d(DEBUG_TAG, "movie url::"+"https://cdnapisec.kaltura.com/html5/html5lib/v2.21/mwEmbedFrame.php/p/1868701/uiconf_id/29233222/entry_id/"+movieID+"?wid=_1868701&iframeembed=true&playerId=kaltura_player_1428490836&entry_id="+movieID);
//		webView.loadUrl(movieUrl);
//		webView.setWebViewClient(new WebViewClient() 
//		{
//			   public void onPageFinished(WebView view, String url)
//			   {
//			        // do your stuff here
//				   runOnUiThread(new Runnable() 
//					{
//						public void run() 
//						{ 
//							btnFullScreen.setVisibility(View.VISIBLE);
//							Log.d(DEBUG_TAG, "Play the video");
//							final long uMillis = SystemClock.uptimeMillis();
//							webView.dispatchTouchEvent(MotionEvent.obtain(uMillis, uMillis, MotionEvent.ACTION_DOWN, 100, 100, 0)); 
//							webView.dispatchTouchEvent(MotionEvent.obtain(uMillis, uMillis, MotionEvent.ACTION_UP, 100, 100, 0));
//						} 
//					});
//			    }
//			});
		
		tb.addListenerOnRatingBar();
		//RelatedContents.seeMoreCounter=61;
		
		RelatedContents.makeJsonObjectRequest("http://bongobd.com/api/related_contents.php?content_id="+ListViewAdapter.singleMovieId+"&pager=3", singleMovieInstance);
		Log.d(DEBUG_TAG, "http://bongobd.com/api/related_contents.php?content_id="+ListViewAdapter.singleMovieId+"&pager=3");
	}
	
//	public void onBackPressed() 
//	{
//		//super.onBackPressed();
		// System.runFinalizersOnExit(true);
		// System.exit(0);
		// android.os.Process.killProcess(android.os.Process.myPid());

		// webView.loadUrl("about:blank");
		// webView.stopLoading();
		// webView.setWebChromeClient(null);
		// webView.setWebViewClient(null);
		// webView.destroy();
		// webView = null;
//	}

	
	
	public void onStop()
	{
		super.onStop();
		overridePendingTransition( R.anim.animation1, R.anim.animation2 );
		Log.d(DEBUG_TAG, "App Stopped");
		TabResult.counter=0;
		movieRating=0;
		userRateStatus=0;
		if (requestQueue != null)
		{
			requestQueue.cancelAll(new RequestQueue.RequestFilter()
			{

				@Override
				public boolean apply(Request<?> request) 
				{
					// TODO Auto-generated method stub
					return true;
				}
			});
		}
		
		//webView.loadUrl("about:blank");
//		webView.stopLoading();
////		webView.setWebChromeClient(null);
////		webView.setWebViewClient(null);
//		webView.destroy();
//		webView = null;
	}

	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		Log.d(DEBUG_TAG, "App Destroyed");
	}
	
	public void onBackPressed() 
	{
		super.onBackPressed();
		SingleMoviePage.this.finish();
		if(StartingPage.singleMoviePageReturn == 1)
		{
			 startActivity(new Intent(getBaseContext(), CategoryLanding.class));
			 overridePendingTransition( R.anim.animation1, R.anim.animation2 );
		} 
		else if(StartingPage.singleMoviePageReturn == 2)
		{
		  	 startActivity(new Intent(getBaseContext(), Movies.class));
		  	 overridePendingTransition( R.anim.animation1, R.anim.animation2 );
		} 
		else if(StartingPage.singleMoviePageReturn == 3)
		{
		  	 startActivity(new Intent(getBaseContext(), ActorProfile.class));
		  	 overridePendingTransition( R.anim.animation1, R.anim.animation2 );
		} 
	}

	@Override
	public void onToggleFullScreen() {
		// TODO Auto-generated method stub
		Log.d(DEBUG_TAG, " screen::"+_isFullScreen);
//		Log.d(DEBUG_TAG, "rotate::"+rotate());
		
		if(_isFullScreen == false)
		{
			Log.d(DEBUG_TAG, "Go Landscape");
			_isFullScreen = true;
			 singleMovieMainLayout.removeView(headerLayout);
			 layoutMain.removeView(singleMovieDetails);
			 layoutMain.removeView(relatedLayout);
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
			
			 relatedWebView.getLayoutParams().height =  (int) (ShareData.getScreenHeight(singleMovieInstance)-ShareData.getScreenHeight(singleMovieInstance)/15);
			 mPlayerView.getLayoutParams().height =  (int) (ShareData.getScreenHeight(singleMovieInstance)-ShareData.getScreenHeight(singleMovieInstance)/15);
			 mPlayerView.setPlayerViewDimensions(vc.width, (int) (ShareData.getScreenHeight(singleMovieInstance)-ShareData.getScreenHeight(singleMovieInstance)/15));
			
//			mPlayerView.getLayoutParams().height = getScreenHeight(instance)-200;
//			mPlayerView.setPlayerViewDimensions( getScreenWidth(instance) , getScreenHeight(instance)-200 );
//			} 
		}
		else if(_isFullScreen == true) 
		{
			 if(rotate()==1 || rotate()==3)
			 {
				 _isFullScreen = false;
				 //potrait
				 if(singleMovieMainLayout.getChildAt(0) != headerLayout)
				 {
					 LinearLayout.LayoutParams headerAParams = new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					 headerLayout.setLayoutParams(headerAParams);
					 singleMovieMainLayout.addView(headerLayout,0);
				 }
				 if(layoutMain.getChildAt(1)!=singleMovieDetails)
				 { 
					 layoutMain.addView(singleMovieDetails,1);
				 }
				 if(layoutMain.getChildAt(2)!=relatedLayout)
				 {
					 layoutMain.addView(relatedLayout,2);
				 }
				 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
				 
				 relatedWebView.getLayoutParams().height = height/2;
				 mPlayerView.getLayoutParams().height =  height/2;
				 mPlayerView.setPlayerViewDimensions(vc.width, height/2);
//			
//				 RelativeLayout.LayoutParams btnFullScreenParams = new RelativeLayout.LayoutParams(height/20, height/20);
//				 	btnFullScreenParams.setMargins(ShareData.padding10*3, 0, 0, 0); 
//					btnFullScreenParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//					btnFullScreenParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//					btnFullScreen.setLayoutParams(btnFullScreenParams);
//					btnFullScreen.setPadding(0, ShareData.padding10, 0, 0);
			 }
			Log.d(DEBUG_TAG, "Go Portrait");
//			mPlayerView.getLayoutParams().height = getScreenHeight(instance)/3;
//			mPlayerView.setPlayerViewDimensions( getScreenWidth(instance) , getScreenHeight(instance)/3 );
//			}
		}
	}

}
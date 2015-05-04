package com.movies.movieSummary;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html.ImageGetter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.android.DialogError;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.movies.actorProfile.ActorProfile;
import com.movies.bongobd.ImageLoader;
import com.movies.bongobd.LeadingMarginSpan2;
import com.movies.bongobd.R;
import com.movies.browseAll.ListViewAdapter;
import com.movies.categoryPage.CategoryLanding;
import com.movies.facebook.FacebookApp;
import com.movies.singleMovie.SingleMoviePage;
import com.movies.startingPage.StartingPage;
import com.tab.AddMenu;
import com.tab.Header;
import com.tab.ShareData;

public class MovieSummary extends Activity implements ImageGetter 
{
	public RequestQueue requestQueue;
	public String movieSummary, movieReleaseDate, movieDuration, movieGenre, movieName, movieImage;
	public String detailsId, roleName , roleId, artistProfileImage, contentId, artistId, artistName;

	public static String singleArtistId, singleArtistName, singleArtistRole;
	
	public ImageView ivMovieImage, ivShareMovie;
	public ImageLoader imageLoader1;
	
	public TextView tvMovieSummary, tvDuration, tvGenre, tvReleaseDate, tvMovieNameSum;
	public TextView show, hide;
//	public TextView tvRoleName, tvArtsitName, tvCastTitle;
	public TextView tvDurationLabelSum, tvGenreLabelSum, tvReleaseDateLabelSum;
	
	public LinearLayout summaryLayout;
	public static LinearLayout castCrewLayout ;
	public static TextView tvCastAndCrew, tvMovieStills;
	
	public int finalHeight, finalWidth;
	public int screenheight, screenWidth;

	public TextView [] tvArtistName;
	public TextView [] tvArtistRole;
	public String [] artistIds;
	
	public static String DEBUG_TAG = MovieSummary.class.getSimpleName();
	public static MovieSummary movieSummaryInstance;
	
	public Header h;
	public JSONObject data;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.movie_summary_page);

		movieSummaryInstance = this;
		h = AddMenu.HeaderFunction(movieSummaryInstance);
		AddMenu.pageNumber = 7;
		data = null;
		
		StartingPage.actorPageReturn = 1;
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		screenheight = displaymetrics.heightPixels;
		screenWidth = displaymetrics.widthPixels;
		
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
		
		castCrewLayout = (LinearLayout) findViewById(R.id.castCrewLayout);
//		LinearLayout.LayoutParams castCrewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		castCrewLayoutParams.setMargins(0, ShareData.padding10, 0, ShareData.padding10);
//		castCrewLayout.setLayoutParams(castCrewLayoutParams);
		
		tvCastAndCrew = (TextView) findViewById(R.id.tvCastAndCrew);
		tvCastAndCrew.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)MovieSummary.movieSummaryInstance.getResources().getDimension(R.dimen.text_size1));
		
		tvMovieStills = (TextView) findViewById(R.id.tvMovieStills);
		tvMovieStills.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)MovieSummary.movieSummaryInstance.getResources().getDimension(R.dimen.text_size1));
//		LinearLayout.LayoutParams tvMovieStillsParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		tvMovieStillsParams.setMargins( ShareData.padding10,  ShareData.padding10, 0, 0);
//		tvMovieStills.setLayoutParams(tvMovieStillsParams);
		
//		LinearLayout castLayoutsLeft = (LinearLayout)findViewById(R.id.castLayoutLeft);
//		LinearLayout castLayoutsRight = (LinearLayout)findViewById(R.id.castLayoutRight);
		
		tvDurationLabelSum = (TextView)findViewById(R.id.tvDurationLabelSum);
		tvDurationLabelSum.setTypeface(ShareData.RobotoFont(MovieSummary.movieSummaryInstance));
		tvDurationLabelSum.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)MovieSummary.movieSummaryInstance.getResources().getDimension(R.dimen.text_size2));
		
		tvGenreLabelSum = (TextView)findViewById(R.id.tvGenreLabelSum);
		tvGenreLabelSum.setTypeface(ShareData.RobotoFont(MovieSummary.movieSummaryInstance));
		tvGenreLabelSum.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)MovieSummary.movieSummaryInstance.getResources().getDimension(R.dimen.text_size2));
		
		tvReleaseDateLabelSum = (TextView)findViewById(R.id.tvReleaseDateLabelSum);
		tvReleaseDateLabelSum.setTypeface(ShareData.RobotoFont(MovieSummary.movieSummaryInstance));
		tvReleaseDateLabelSum.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)MovieSummary.movieSummaryInstance.getResources().getDimension(R.dimen.text_size2));
	
		// load image from url
		imageLoader1 = new ImageLoader(this); 

		singleArtistId = null;
		singleArtistName = null;
		singleArtistRole = null;
		makeJsonObjectRequest("http://bongobd.com/api/content.php?id="+ ListViewAdapter.singleMovieId);

		tvMovieNameSum = (TextView) findViewById(R.id.tvMovieNameSum);
		//tvMovieNameSum.setTypeface(ShareData.RobotoFont(MovieSummary.movieSummaryInstance));
		tvMovieNameSum.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)MovieSummary.movieSummaryInstance.getResources().getDimension(R.dimen.text_size1));
		

		tvMovieSummary = (TextView) findViewById(R.id.message_view);
		tvMovieSummary.setTypeface(ShareData.RobotoFont(MovieSummary.movieSummaryInstance));
		tvMovieSummary.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)MovieSummary.movieSummaryInstance.getResources().getDimension(R.dimen.text_size2));

		ivMovieImage = (ImageView) findViewById(R.id.iconMovie);

		ivMovieImage.getLayoutParams().width = screenWidth / 3-(ShareData.padding10*3);
		ivMovieImage.getLayoutParams().height = screenWidth / 3+(ShareData.padding10*4);

		tvDuration = (TextView) findViewById(R.id.tvDurationSum);
		tvDuration.setTypeface(ShareData.RobotoFont(MovieSummary.movieSummaryInstance));
		tvDuration.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)MovieSummary.movieSummaryInstance.getResources().getDimension(R.dimen.text_size2));

		tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDateSum);
		tvReleaseDate.setTypeface(ShareData.RobotoFont(MovieSummary.movieSummaryInstance));
		tvReleaseDate.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)MovieSummary.movieSummaryInstance.getResources().getDimension(R.dimen.text_size2));
		
		tvGenre = (TextView) findViewById(R.id.tvGenreSum);
		tvGenre.setTypeface(ShareData.RobotoFont(MovieSummary.movieSummaryInstance));
		tvGenre.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)MovieSummary.movieSummaryInstance.getResources().getDimension(R.dimen.text_size2));
		
	
		
//		setSome();
//		else if(tvMovieSummary.getText().toString().trim().length()!=0)
//		{
//			show.setVisibility(View.VISIBLE);
//			hide.setVisibility(View.VISIBLE);
//		}
		
		ivShareMovie = (ImageView)findViewById(R.id.ivShareMovie);
		RelativeLayout.LayoutParams ivShareMovieParams = new RelativeLayout.LayoutParams(ShareData.getScreenWidth(movieSummaryInstance)/8, ShareData.getScreenWidth(movieSummaryInstance)/22);
		ivShareMovieParams.setMargins(ShareData.padding10*2, 0, 0, 0);
		ivShareMovie.setLayoutParams(ivShareMovieParams);
		ivShareMovieParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		ivShareMovieParams.addRule(RelativeLayout.RIGHT_OF, R.id.iconMovie);
		ivShareMovie.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				postToWall();
			}
		});
	}

	@Override
	public Drawable getDrawable(String source) 
	{
		LevelListDrawable d = new LevelListDrawable();
		Drawable empty = getResources().getDrawable(R.drawable.ic);
		d.addLevel(0, 0, empty);
		d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
		new LoadImage().execute(source, d);

		return d;
	}
	
	public void makeJsonObjectRequest(String urlJsonObj) 
	{
		movieName=null;
		movieImage=null;
		movieSummary=null;
		movieReleaseDate=null;
		movieDuration=null;
		movieGenre=null;
		
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
							JSONObject data = response.getJSONObject("data");
							//Log.d(DEBUG_TAG, "data: "+data);
							 
							movieName = data.getString("content_title");
							//Log.d(DEBUG_TAG, "movieName:" + movieName);
							tvMovieNameSum.setText(movieName);
								
							movieImage ="http://bongobd.com/wp-content/themes/bongobd/images/posterimage/cover/size-250/"+data.getString("content_poster_cover");
						    //Log.d(DEBUG_TAG, "movieImage:"+movieImage );
							imageLoader1.DisplayImage(movieImage, ivMovieImage);
								
							ListViewAdapter.singleMovieId = data.getString("id");
							//Log.d(DEBUG_TAG, "ListViewAdapter.singleMovieId : "+ListViewAdapter.singleMovieId );
								
						    movieSummary = data.getString("content_summary");
						    //Log.d(DEBUG_TAG, "movieSummary: "+movieSummary);
							setSome(movieSummary);
							
							movieReleaseDate = data.getString("release_date");
							//Log.d(DEBUG_TAG, "movieReleaseDate: "+movieReleaseDate);
								
							if(movieReleaseDate!=null)
							{
								if(movieReleaseDate.toString().trim().equals("0000-00-00"))
								{
									tvReleaseDateLabelSum.setText("");
									tvReleaseDate.setText("");
								}
								else
								{
									tvReleaseDate.setText(movieReleaseDate);
								}
							}
								
								
							movieDuration = data.getString("content_length");
							Log.d(DEBUG_TAG, "movieDuration:"+movieDuration);
							try 
							{
								if(movieDuration!=null)
								{
									tvDuration.setText(movieDuration.trim()+" ");
								}
							}
							catch (Exception e)
							{
									// TODO Auto-generated catch block
								e.printStackTrace();
							}
								
							movieGenre = data.getString("genre");
							Log.d(DEBUG_TAG, "movieGenre:"+movieGenre);
							try
							{
								if(movieGenre!=null)
								{
									tvGenre.setText(movieGenre.trim());
								}
							}
							catch (Exception e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
//							Toast.makeText(getApplicationContext(),
//									"Error: " + e.getMessage(),
//									Toast.LENGTH_LONG).show();
						}
//						hidepDialog();
					}
				}, new Response.ErrorListener() 
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
//						VolleyLog.d(DEBUG_TAG, "Error: " + error.getMessage());
//						Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
//						// hide the progress dialog
//						hidepDialog();
					}
				});

		// Adding request to request queue
		requestQueue.add(jsonObjReq);
		
		AddCastCrewLayout.makeJsonObjectRequestCastAndCrew("http://bongobd.com/api/content.php?id="+ListViewAdapter.singleMovieId);
		Log.d(DEBUG_TAG, "movie detail cast crew:http://bongobd.com/api/content.php?id="+ListViewAdapter.singleMovieId);
	}

	public void setSome(String msg)
	{
		int width = ivMovieImage.getLayoutParams().width;
		int height = ivMovieImage.getLayoutParams().height;
		
		Log.d(DEBUG_TAG, "msg:" + msg);
		
		Log.d(DEBUG_TAG, "width:" + width);
		Log.d(DEBUG_TAG, "height:" + height);

		int leftMargin = width + ShareData.padding10*2;

		// icon.setImageDrawable(getResources().getDrawable(R.drawable.download));
		float textLineHeight = tvMovieSummary.getPaint().getTextSize();
		int lines = (int) Math.round(height / textLineHeight);
		if(msg!=null)
		{
			msg = msg.replaceAll("(?<!\\\\)\\\\(?!\\\\)", "");
			msg = msg.replaceAll("</i>", "");
			msg = msg.replaceAll("<i>", "");
			
			SpannableString ss = new SpannableString(msg);
			ss.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ss.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			ss.setSpan(new LeadingMarginSpan2(lines, leftMargin), 0, ss.length(), 0);
			tvMovieSummary.setText(ss);
			tvMovieSummary.setMaxLines(4);
		}
		
		show = (TextView) findViewById(R.id.showMovieData);
		show.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)MovieSummary.movieSummaryInstance.getResources().getDimension(R.dimen.text_size2));
		show.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				// System.out.println("Show button");
				show.setVisibility(View.INVISIBLE);
				hide.setVisibility(View.VISIBLE);
				tvMovieSummary.setMaxLines(Integer.MAX_VALUE);

			}
		});
		hide = (TextView) findViewById(R.id.hideMovieData);
		hide.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)MovieSummary.movieSummaryInstance.getResources().getDimension(R.dimen.text_size2));
		hide.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				System.out.println("Hide button");
				hide.setVisibility(View.INVISIBLE);
				show.setVisibility(View.VISIBLE);
				tvMovieSummary.setMaxLines(4);

			}
		});
		if(tvMovieSummary.getText().toString().trim().length()==0 || tvMovieSummary.getLineCount()<=4)
		{
			show.setVisibility(View.INVISIBLE);
			hide.setVisibility(View.INVISIBLE);
		}
	}
	
	class LoadImage extends AsyncTask<Object, Void, Bitmap>
	{

		private LevelListDrawable mDrawable;

		@Override
		protected Bitmap doInBackground(Object... params) 
		{
			String source = (String) params[0];
			mDrawable = (LevelListDrawable) params[1];
			Log.d("", "doInBackground " + source);
			try 
			{
				InputStream is = new URL(source).openStream();
				return BitmapFactory.decodeStream(is);
			}
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap)
		{
			Log.d("", "onPostExecute drawable " + mDrawable);
			Log.d("", "onPostExecute bitmap " + bitmap);
			if (bitmap != null)
			{
				BitmapDrawable d = new BitmapDrawable(bitmap);
				mDrawable.addLevel(1, 1, d);
				mDrawable.setBounds(0, 0, ShareData.padding10*2, ShareData.padding10*5);
				mDrawable.setLevel(1);
				// d.setGravity(Gravity.TOP);
				// i don't know yet a better way to refresh TextView
				// mTv.invalidate() doesn't work as expected
				CharSequence t = tvMovieSummary.getText();
				tvMovieSummary.setText(t);
			}
		}
	}
	
	public void onStop() 
	{
		super.onStop();
		Log.d(DEBUG_TAG, "App Stopped");
	}	
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		Log.d(DEBUG_TAG, "App Destroyed");
	}
	
	@Override
	public void onBackPressed()  
	{
		super.onBackPressed();

		//ListViewAdapter.singleMovieId = "";
		//SingleMoviePage.jsonData = null;
		
		MovieSummary.this.finish();
		if(StartingPage.movieSummaryPageReturn  == 1)
		{
		  	 startActivity(new Intent(getBaseContext(), SingleMoviePage.class));
		  	 overridePendingTransition( R.anim.animation1, R.anim.animation2 );
		} 
	}
	

    //Function to post to facebook wall
  	@SuppressWarnings("deprecation")
  	public void postToWall()
  	{
  		
  		Bundle parameters = new Bundle();
  		parameters.putString("name", "BongoBD app");
  		parameters.putString("caption", "BongoBD caption.");
  		parameters.putString("description", "BongoBD description.");
  		parameters.putString("link", "https://play.google.com/store/apps/details?id=com.ck.batterymonitor");
  		//parameters.putString("picture", "link to the picture");
  		parameters.putString("display", "page");
  		    
 		// post on user's wall.
  		FacebookApp.facebook.dialog(this, "feed", parameters, new DialogListener()
  		{
  			@Override
  			public void onFacebookError(FacebookError e)
  			{
  				
  			}
  			@Override
  			public void onError(DialogError e) 
  			{
  				
  			}
  			@Override
  			public void onComplete(Bundle values)
  			{
  				
  			}
  			@Override
  			public void onCancel() 
  			{
  				
  			}
  		});
  	}
}

package com.movies.browseAll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.movies.bongobd.R;
import com.movies.categoryPage.CategoryLanding;
import com.movies.people.People;
import com.movies.startingPage.StartingPage;
import com.tab.AddMenu;
import com.tab.Header;
import com.tab.ShareData;


public class Movies extends Activity 
{
	public static ListView listview;
	public static String MOVIE_NAME = "movieName";
	public static String MOVIE_DIRECTOR = "movieDirector";
	public static String MOVIE_VIEWS = "movieViews";
	public static String MOVIE_BIO = "movieBio";
	public static String MOVIE_IMAGE = "movieImage";
	public static String MOVIE_CONTENT_LENGTH = "contentLength";
	public static String MOVIE_SHORT_SUMMARY= "movieShortSummary";
	public static String MOVIE_SUMMARY= "movieSummary";
	public static String MOVIE_ID = "movieId";
	public static String[] images ;
	public static Movies moviesInstance;
	public static String DEBUG_TAG = Movies.class.getSimpleName();
    public static RequestQueue requestQueue;
    
	public ListViewAdapter adapter;
	public ArrayList<HashMap<String, String>> arraylist;
    public ProgressDialog pDialog;
    
 	public int counter=0, loopCounter=0, seeMoreCounter=0, scrollPosition=0;
	public int mCurrentX, mCurrentY;
	
	public LinearLayout mainMovieLayout;
	public Header h;
	public String total_match;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.listview_movie);
		
		mainMovieLayout = (LinearLayout)findViewById(R.id.mainMovieLayout);
		
		moviesInstance = this;
		h = AddMenu.HeaderFunction(moviesInstance);
		AddMenu.pageNumber = 5;
		
		StartingPage.singleMoviePageReturn = 2;
		StartingPage.actorPageReturn = 2;
		
		if(requestQueue!=null)
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
			requestQueue=null;
		}
	
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
		else if(CategoryLanding.catagoryName.trim().equals("people"))
		{
			h.tv1.setBackgroundColor(Color.parseColor("#FF0000"));
			h.tv2.setBackgroundColor(Color.parseColor("#FF0000"));
			h.tv3.setBackgroundColor(Color.parseColor("#FF0000"));
			h.tv4.setBackgroundColor(Color.parseColor("#B40404"));
		}
		
		if(ShareData.isNetworkAvailable(moviesInstance)==true)
		{
			seeMoreCounter=3;
			arraylist = new ArrayList<HashMap<String, String>>();
			
			Log.d(DEBUG_TAG, "peoplePageEnable:"+ StartingPage.browseAll);
			//For category page
			if(StartingPage.browseAll == 1)
			{
				makeJsonObjectRequestForCategory("http://bongobd.com/api/category.php?catID="+CategoryLanding.categoryID+"&pager=1");
				Log.d(DEBUG_TAG, "browse all category:"+"http://bongobd.com/api/category.php?catID="+CategoryLanding.categoryID+"&pager=1");
			}
			//For people page
			else if(StartingPage.browseAll == 2)
			{
				makeJsonObjectRequestForPeoplePage("http://stage.bongobd.com/api/people_sliders.php?slider_id="+People.categoryID+"&pager=1");
				Log.d(DEBUG_TAG, "browse all people:"+"http://stage.bongobd.com/api/people_sliders.php?slider_id="+People.categoryID+"&pager=1");
			}
			//For search query
			else if(StartingPage.browseAll == 3)
			{
				makeJsonObjectRequestForSearch("http://bongobd.com/api/search.php?key="+AddMenu.searchQuery+"&pager=1");
				Log.d(DEBUG_TAG, "browse all search:"+"http://bongobd.com/api/search.php?key="+AddMenu.searchQuery);
			}
				
		}
		else
		{
			Toast.makeText(getBaseContext(), "No Internet", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public void makeJsonObjectRequestForCategory(String urlJsonObj) 
	{
		//showpDialog();
		counter = 0;
		loopCounter = 0;
		requestQueue = Volley.newRequestQueue(Movies.moviesInstance);
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
				urlJsonObj, null, new Response.Listener<JSONObject>() 
				{
					@Override
					public void onResponse(JSONObject response) 
					{
						//Log.d(DEBUG_TAG, response.toString());
						try
						{
							JSONObject js = response.getJSONObject("data");
							//Log.d(DEBUG_TAG, "js: "+js); 
							JSONObject js1 = response.getJSONObject("additionalData");
							//Log.d(DEBUG_TAG, "js1: "+js1); 
							
						    String total_record = js1.getString("total_records");
					        //Log.d(DEBUG_TAG, "total_record: "+total_record); 
					      
				            float loopCounter1 = Float.parseFloat(total_record);
				            float loopCounter2 = loopCounter1 /10;
				            
				            if(loopCounter2 % 1 != 0)
				            {
				            	Log.d(DEBUG_TAG, loopCounter2+" does have decimal");
				            	loopCounter1 = loopCounter1+10;
				            	//Log.d(DEBUG_TAG, "loopCounter1: "+loopCounter1); 
				            }
				            else if(loopCounter2 % 1 == 0)
				            {
				            	Log.d(DEBUG_TAG, loopCounter2+" does not have decimal");
				            }
				            
				            loopCounter = (int) (loopCounter1/10);
				            loopCounter = loopCounter+2;
				            Log.d(DEBUG_TAG, "loopCounterss: "+loopCounter); 
				            
						    Iterator<String> iter = js.keys();
						    while(iter.hasNext()) 
						    {
						    	counter++;
						    	//Log.d(DEBUG_TAG, "number of items: "+counter); 
						        String key = iter.next();

						        try 
						        {
						            Object value = js.get(key);
						            //Log.d(DEBUG_TAG, "value:"+value ); 
						            
						            JSONObject eachObject = js.getJSONObject(""+ key);
						            
						            String id = eachObject.getString("id").trim();
						            //Log.d(DEBUG_TAG, "id: "+id); 
						            
									String content_title = eachObject.getString("content_title");
									if(content_title.length()>12)
									{
										content_title = content_title.substring(0, 11)+"..";
									}
									
									//Log.d(DEBUG_TAG, "content_title: "+content_title);
									String entry_time = eachObject.getString("entry_time");
								    //Log.d(DEBUG_TAG, "entry_time: "+entry_time); 
									String content_thumb = eachObject.getString("content_thumb");
									content_thumb= "http://bongobd.com/wp-content/themes/bongobd/" +
											"images/posterimage/thumb/"+content_thumb;
									
									 String avg_rating = eachObject.getString("avg_rating");
										//Log.d(DEBUG_TAG, "avg_rating: "+avg_rating); 

									String total_view = eachObject.getString("total_view");
									//Log.d(DEBUG_TAG, "total_view: "+total_view); 
									
									String content_length = eachObject.getString("content_length");
									if(content_length.trim().equals("00:00:00"))
									{
										content_length = "";
									}
									//Log.d(DEBUG_TAG, "content_length: "+content_length); 
									
									String content_short_summary = eachObject.getString("content_short_summary");
									//Log.d(DEBUG_TAG, "content_short_summary: "+content_short_summary); 
									
									HashMap<String, String> map = new HashMap<String, String>();
									map.put("movieId", id);
									map.put("movieName", content_title);
									map.put("movieViews", total_view);
									//map.put("movieDirector", by);
									map.put("movieImage", content_thumb);
									map.put("contentLength", content_length);
									map.put("movieShortSummary", content_short_summary);
									
									arraylist.add(map);
						        } 
						        catch (JSONException e)
						        {
						            // Something went wrong!
						        }
						    }
						} 
						catch (JSONException e) 
						{
							e.printStackTrace();
							Toast.makeText(getApplicationContext(),
									"Error: " + e.getMessage(),
									Toast.LENGTH_LONG).show();
						}
						hidepDialog();
					}
				}, new Response.ErrorListener()
				{

					@Override
					public void onErrorResponse(VolleyError error)
					{
						VolleyLog.d(DEBUG_TAG, "Error: " + error.getMessage());
						Toast.makeText(getApplicationContext(),
								error.getMessage(), Toast.LENGTH_SHORT).show();
						// hide the progress dialog
						hidepDialog();
					}
				});

		// Adding request to request queue
		requestQueue.add(jsonObjReq);
	}
	
	
	public void makeJsonObjectRequestForPeoplePage(String urlJsonObj) 
	{
		//showpDialog();
		counter = 0;
		loopCounter = 0;
		requestQueue = Volley.newRequestQueue(Movies.moviesInstance);
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
				urlJsonObj, null, new Response.Listener<JSONObject>() 
				{

					@Override
					public void onResponse(JSONObject response) 
					{
						//Log.d(DEBUG_TAG, response.toString());

						try {
							String js1 = response.getString("total_record");
							Log.d(DEBUG_TAG, "js1:" + js1.toString());
							
							float loopCounter1 = Float.parseFloat(js1);
					        float loopCounter2 = loopCounter1 /10;
					        //Log.d(DEBUG_TAG, "loopCounter2: "+loopCounter2); 
					        //Log.d(DEBUG_TAG, "seeMoreCounter: "+seeMoreCounter); 
					            
					        if(loopCounter2 % 1 != 0)
					        {
					          	Log.d(DEBUG_TAG, loopCounter2+" does have decimal");
					           	loopCounter1 = loopCounter1+10;
					         	//Log.d(DEBUG_TAG, "loopCounter1: "+loopCounter1); 
							          
					        }
					        else if(loopCounter2 % 1 == 0)
					        {
					           	Log.d(DEBUG_TAG, loopCounter2+" does not have decimal");
					        }
					            
					        loopCounter = (int) (loopCounter1/10);
					        loopCounter = loopCounter+2;
					        //Log.d(DEBUG_TAG, "loopCounter: "+loopCounter); 
							
							JSONArray jsonArtist = response.getJSONArray("artist");
							for (int i=0; i<jsonArtist.length(); i++)
							{
							    JSONObject js = jsonArtist.getJSONObject(i);
							    String content_thumb = js.getString("slider_thumb_image");
							    content_thumb = "http://bongobd.com/wp-content/themes/bongobd/images/artistimage/thumb/"+content_thumb;
							    //Log.d(DEBUG_TAG, "content_thumb:" + content_thumb);
							    
							    
							    String id = js.getString("id");
							    StartingPage.ACTOR_ID =id;
							    Log.d(DEBUG_TAG, "ACTOR_ID:" + StartingPage.ACTOR_ID);
							    
							    String content_title = js.getString("name");
							    if(content_title.length()>12)
							    {
							    	content_title = content_title.substring(0, 11)+"..";
							    }
							    //Log.d(DEBUG_TAG, "content_title:" + content_title);
							    
							    String bio = js.getString("bio");
							    if(bio.length()>28)
							    {
							    	bio = bio.substring(0, 27)+"..";
							    	bio = bio.replaceAll("(?<!\\\\)\\\\(?!\\\\)", "");
							    }
							    Log.d(DEBUG_TAG, "bio:" + bio);
							    
							    //String total_view = js.getString("total_content_view");
							    //Log.d(DEBUG_TAG, "total_view:" + total_view);
							    
//							    String content_length =js.getString("total_content_view");
//							    Log.d(DEBUG_TAG, "content_length:" + content_length);
							    
							    String content_short_summary ="some data";
							    
							    HashMap<String, String> map = new HashMap<String, String>();
								map.put("movieId", id);
								map.put("movieName", content_title);
								map.put("movieBio", bio);
								//map.put("movieViews", total_view);
								map.put("movieImage", content_thumb);
								//map.put("movieImage", content_thumb);
								//map.put("contentLength", content_length);
								map.put("movieShortSummary", content_short_summary);
								
								arraylist.add(map);
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
//						Toast.makeText(getApplicationContext(),
//								error.getMessage(), Toast.LENGTH_SHORT).show();
						// hide the progress dialog
						hidepDialog();
					}
				});

		// Adding request to request queue
		requestQueue.add(jsonObjReq);
	}
	
	public void makeJsonObjectRequestForSearch(String urlJsonObj) 
	{
		//showpDialog();
		counter = 0;
		loopCounter = 0;
		requestQueue = Volley.newRequestQueue(Movies.moviesInstance);
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
				urlJsonObj, null, new Response.Listener<JSONObject>() 
				{
					@Override
					public void onResponse(JSONObject response) 
					{
						//Log.d(DEBUG_TAG, response.toString());
						try
						{
							JSONObject additionalData = response.getJSONObject("additionalData");
							total_match = additionalData.getString("total_match");
							Log.d(DEBUG_TAG, "total_match:"+total_match);
							
							if(seeMoreCounter<4)
							{
								Toast.makeText(getBaseContext(), ""+total_match+" video found", Toast.LENGTH_LONG).show();
							}
							
							JSONObject js = response.getJSONObject("data");
							Log.d(DEBUG_TAG, "js: "+js); 
				            
							loopCounter=500000000;
						    Iterator<String> iter = js.keys();
						    while(iter.hasNext()) 
						    {
						    	counter++;
						    	//Log.d(DEBUG_TAG, "number of items: "+counter); 
						        String key = iter.next();

						        try 
						        {
						            Object value = js.get(key);
						            //Log.d(DEBUG_TAG, "value:"+value ); 
						            
						            JSONObject eachObject = js.getJSONObject(""+ key);
						            
						            String id = eachObject.getString("id").trim();
						            //Log.d(DEBUG_TAG, "id: "+id); 
						            
									String content_title = eachObject.getString("content_title");
									//Log.d(DEBUG_TAG, "content_title: "+content_title);
									String entry_time = eachObject.getString("entry_time");
								    //Log.d(DEBUG_TAG, "entry_time: "+entry_time); 
									String content_thumb = eachObject.getString("content_thumb");
									content_thumb= "http://bongobd.com/wp-content/themes/bongobd/" +
											"images/posterimage/thumb/"+content_thumb;
									
									 String avg_rating = eachObject.getString("avg_rating");
										//Log.d(DEBUG_TAG, "avg_rating: "+avg_rating); 

									String total_view = eachObject.getString("total_view");
									//Log.d(DEBUG_TAG, "total_view: "+total_view); 
									
									String content_length = eachObject.getString("content_length");
									//Log.d(DEBUG_TAG, "content_length: "+content_length); 
									
									String content_short_summary = eachObject.getString("content_short_summary");
									//Log.d(DEBUG_TAG, "content_short_summary: "+content_short_summary); 
									
									HashMap<String, String> map = new HashMap<String, String>();
									map.put("movieId", id);
									map.put("movieName", content_title);
									map.put("movieViews", total_view);
									//map.put("movieDirector", by);
									map.put("movieImage", content_thumb);
									map.put("contentLength", content_length);
									map.put("movieShortSummary", content_short_summary);
									
									arraylist.add(map);
						        } 
						        catch (JSONException e)
						        {
						            // Something went wrong!
						        }
						    }
						} 
						catch (JSONException e) 
						{
							e.printStackTrace();
							Toast.makeText(getBaseContext(),"0 Video Found", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(getBaseContext(),"0 Video Found", Toast.LENGTH_SHORT).show();
//						Toast.makeText(getApplicationContext(),
//								error.getMessage(), Toast.LENGTH_SHORT).show();
						// hide the progress dialog
						hidepDialog();
					}
				});

		// Adding request to request queue
		requestQueue.add(jsonObjReq);
	}
	
	public void hidepDialog() 
	{
		createListView();
	}
	
	public void createListView() 
	{
		// Locate the listview in listview_main.xml
		listview = (ListView) findViewById(R.id.listviewMovies);
		//Log.d(DEBUG_TAG, "list View items:"+listview.getAdapter().getCount());
		//Log.d(DEBUG_TAG, "Button ADDED");
			
		final LinearLayout ll = new LinearLayout(moviesInstance);
		final Button btnLoadMore = new Button(moviesInstance);
		btnLoadMore.setBackgroundColor(Color.RED);
		btnLoadMore.setText("See More");
		btnLoadMore.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)moviesInstance.getResources().getDimension(R.dimen.text_size2));
		//btnLoadMore.setPadding(25, 0, 25, 0);
		btnLoadMore.setTextColor(Color.WHITE);
		ListView.LayoutParams params = new ListView.LayoutParams
		(ListView.LayoutParams.WRAP_CONTENT,ListView.LayoutParams.WRAP_CONTENT);
		btnLoadMore.setLayoutParams(params);

		ll.setGravity(Gravity.CENTER);
		ll.addView(btnLoadMore);
		
		adapter = new ListViewAdapter(Movies.this, arraylist);
		if(seeMoreCounter<loopCounter)
		{
			listview.addFooterView(ll);
		}
		listview.setAdapter(adapter);
		
		btnLoadMore.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				// Starting a new async task
				listview.removeFooterView(ll);
	            	
				seeMoreCounter++;
				Log.d(DEBUG_TAG, "seeMoreCounter:"+seeMoreCounter);
	            
				Log.d(DEBUG_TAG, "peoplePageEnable:"+StartingPage.browseAll);
				//For category page
				if(StartingPage.browseAll == 1)
				{
					makeJsonObjectRequestForCategory("http://bongobd.com/api/category.php?catID="+CategoryLanding.categoryID+"&pager="+seeMoreCounter);
					Log.d(DEBUG_TAG, "browse all category:http://bongobd.com/api/category.php?catID="+CategoryLanding.categoryID+"&pager="+seeMoreCounter);
				}
				//For people page
				else if(StartingPage.browseAll == 2)
				{
					makeJsonObjectRequestForPeoplePage("http://stage.bongobd.com/api/people_sliders.php?slider_id="+People.categoryID+"&pager="+seeMoreCounter);
					Log.d(DEBUG_TAG, "browse all people:"+"http://stage.bongobd.com/api/people_sliders.php?slider_id="+People.categoryID+"&pager="+seeMoreCounter);
				}
				//For search query
				else if(StartingPage.browseAll == 3)
				{
					makeJsonObjectRequestForSearch("http://bongobd.com/api/search.php?key="+AddMenu.searchQuery+"&pager="+seeMoreCounter);
					Log.d(DEBUG_TAG, "browse all search:"+"http://bongobd.com/api/search.php?key="+AddMenu.searchQuery+"&pager="+seeMoreCounter);
				}
				scrollPosition = listview.getPositionForView(arg0);
				Log.d(DEBUG_TAG, "scrollPosition:"+scrollPosition);
	           }
			});
	
		if(scrollPosition>0)
		{
			listview.setSelection(scrollPosition-5);
		}
		//if there is less than 14 elements, remove see more button
		if(listview.getAdapter().getCount()<14)
		{
			listview.removeFooterView(ll);
			
			listview.setOnScrollListener(new OnScrollListener()
			{
				private int mLastFirstVisibleItem;
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) 
				{

				}
				@Override
	        	public void onScroll(AbsListView view, int firstVisibleItem,
	                int visibleItemCount, int totalItemCount) 
					{
						if(ListViewAdapter.popupWindowMenuDropDown!=null && 
							ListViewAdapter.popupWindowMenuDropDown.isShowing()==true)
						{
							ListViewAdapter.popupWindowMenuDropDown.dismiss();
						}
						if(mLastFirstVisibleItem<firstVisibleItem)
						{
							Log.i("SCROLLING DOWN","TRUE");
						}
						if(mLastFirstVisibleItem>firstVisibleItem)
						{
							Log.i("SCROLLING UP","TRUE");
						}
						mLastFirstVisibleItem=firstVisibleItem;
					}
				});		
			}
	}

	@Override
	public void onStop() 
	{
		super.onStop();
		Log.d(DEBUG_TAG, "App Stopped");
		adapter=null;
		scrollPosition=0;
		//mainMovieLayout.removeView(listview);
		//listview.removeAllViews();
//		listview = null;
//		MOVIE_NAME = null;
//		MOVIE_DIRECTOR = null;
//		MOVIE_VIEWS = null;
//		MOVIE_IMAGE = null;
//		MOVIE_CONTENT_LENGTH = null;
//		MOVIE_SHORT_SUMMARY= null;
//		MOVIE_SUMMARY= null;
//		MOVIE_ID = null;
//		images = null ;
//	    
//		adapter = null;
//		arraylist = null;
//	    //pDialog;
//	    
//	 	counter=0;
//	 	loopCounter=0;
//	 	seeMoreCounter=0;
//		mCurrentX=0;
//		mCurrentY=0;
		
		//CatagoryCarasol
//		if(requestQueue!=null)
//		{
//			requestQueue.cancelAll(new RequestQueue.RequestFilter() 
//			{
//				@Override
//				public boolean apply(Request<?> request)
//				{
//					// TODO Auto-generated method stub
//					return true;
//				}
//		    });
//			requestQueue=null;
//		}
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
		Movies.this.finish();
	    if(StartingPage.browseAllPageReturn == 1)
	    {
			 startActivity(new Intent(getBaseContext(), CategoryLanding.class));
			 overridePendingTransition( R.anim.animation1, R.anim.animation2 );
		} 
		else if(StartingPage.browseAllPageReturn == 2)
		{
		  	 startActivity(new Intent(getBaseContext(), People.class));
		  	 overridePendingTransition( R.anim.animation1, R.anim.animation2 );
		} 
	}
}
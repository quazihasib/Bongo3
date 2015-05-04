package com.movies.movieSummary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.movies.actorProfile.ActorProfile;
import com.movies.bongobd.ImageLoader;
import com.movies.bongobd.R;
import com.movies.browseAll.ListViewAdapter;
import com.movies.startingPage.StartingPage;
import com.tab.ShareData;

public class AddCastCrewLayout 
{
	public static RequestQueue requestQueue;
	public static int actorImageCounter;
	public static String[] popularContentTitles , popularContentImages, popularContentRoles, popularContentIds;
	public static String[] movieStillsImages;
	public static ImageView[] im1, im2;
	public static ImageLoader imageLoader;
	
	public static String DEBUG_TAG = AddCastCrewLayout.class.getSimpleName();
	
	public static void makeJsonObjectRequestCastAndCrew(String urlJsonObj) 
	{
		imageLoader = new ImageLoader(MovieSummary.movieSummaryInstance);
		
		actorImageCounter = 0;
		requestQueue = Volley.newRequestQueue(StartingPage.startInstance);
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
				urlJsonObj, null, new Response.Listener<JSONObject>() 
				{
					@Override
					public void onResponse(JSONObject response) 
					{
						//Log.d(DEBUG_TAG, response.toString());
						try
						{
							JSONObject data = response.getJSONObject("data");
							//Log.d(DEBUG_TAG, "data: " + data);
							
							JSONArray array =data.getJSONArray("content_details");
							
							popularContentImages = new String[array.length()+3];
							popularContentTitles = new String[array.length()+3];
							popularContentRoles = new String[array.length()+3];
							popularContentIds = new String[array.length()+3];
							
							for (int i = 0; i < array.length(); i++) 
							{
							    JSONObject row = array.getJSONObject(i);
							    popularContentTitles[i] = row.getString("artist_name");
							    if(popularContentTitles[i].length()>12)
							    {
							    	popularContentTitles[i] = popularContentTitles[i].substring(0, 11)+"..";
							    }
							    Log.d(DEBUG_TAG, "popularContentTitles:"+popularContentTitles[i]);
							    
							    popularContentRoles[i] = row.getString("role_name");
							    if(popularContentRoles[i].length()>12)
							    {
							    	popularContentRoles[i] = popularContentRoles[i].substring(0, 11)+"..";
							    }
							    //Log.d(DEBUG_TAG, "popularContentRoles:"+popularContentRoles[i]);
							    
							    popularContentIds[i] = row.getString("artist_id");
							    
							    popularContentImages[i]=row.getString("slider_thumb_image");
							    popularContentImages[i] = "http://bongobd.com/wp-content/themes/bongobd/images/artistimage/thumb/"+popularContentImages[i].trim();
							}
							
							//Add Most Popular images
							HorizontalScrollView hs1 = (HorizontalScrollView) MovieSummary.movieSummaryInstance.findViewById(R.id.castCrewHS);
//							LinearLayout.LayoutParams castCrewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//							castCrewLayoutParams.setMargins(ShareData.padding10*2, ShareData.padding10, ShareData.padding10, 0);
//							hs1.setLayoutParams(castCrewLayoutParams);
							
							LinearLayout fl1 = new LinearLayout(MovieSummary.movieSummaryInstance);
							LinearLayout.LayoutParams fl1Params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
							fl1.setOrientation(LinearLayout.HORIZONTAL);
									
							LinearLayout fl2 = new LinearLayout(MovieSummary.movieSummaryInstance);
							LinearLayout.LayoutParams fl2Params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
							fl2.setOrientation(LinearLayout.VERTICAL);
									
							RelativeLayout.LayoutParams rl1Params = 
									new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
									RelativeLayout.LayoutParams.WRAP_CONTENT);

							im1 = new ImageView[array.length()+1];
							
							for(int i = 1; i <= array.length(); i++)
							{
								LinearLayout llPopularContents = new LinearLayout(MovieSummary.movieSummaryInstance);
								LinearLayout.LayoutParams llPopularContentsParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
								llPopularContents.setOrientation(LinearLayout.VERTICAL);
								
							    im1[i] = new ImageView(MovieSummary.movieSummaryInstance);
								im1[i].setScaleType(ScaleType.FIT_XY);
								im1[i].setImageResource(R.drawable.ic);
								RelativeLayout.LayoutParams imParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
								imParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
								imParams1.width = ShareData.getScreenWidth(MovieSummary.movieSummaryInstance)/3;
								imParams1.height = ShareData.getScreenWidth(MovieSummary.movieSummaryInstance)/3-ShareData.getScreenWidth(MovieSummary.movieSummaryInstance)/9;
								im1[i].setTag(i);
								//Log.d(DEBUG_TAG, "artist_profile_image:"+popularContentImages[i]);
								imageLoader.DisplayImage(popularContentImages[i-1], im1[i]);
								
								TextView tv = new TextView(MovieSummary.movieSummaryInstance);
								tv.setTypeface(ShareData.RobotoFont(MovieSummary.movieSummaryInstance));
								tv.setText(""+popularContentTitles[i-1]);
								tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)MovieSummary.movieSummaryInstance.getResources().getDimension(R.dimen.text_size2));
								tv.setTextColor(Color.RED);
								//tv.setBackgroundColor(Color.BLACK);
								LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
									
								TextView tv1 = new TextView(MovieSummary.movieSummaryInstance);
								tv1.setTypeface(ShareData.RobotoFont(MovieSummary.movieSummaryInstance));
								tv1.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)MovieSummary.movieSummaryInstance.getResources().getDimension(R.dimen.text_size2));
								tv1.setText(""+popularContentRoles[i-1]);
								tv1.setTextColor(Color.GRAY);
								//tv1.setBackgroundColor(Color.BLACK);
								LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
							
								llPopularContents.addView(im1[i], imParams1);
								llPopularContents.addView(tv, tvParams);
								llPopularContents.addView(tv1, tv1Params);
								llPopularContents.setTag(popularContentIds[i]);
								llPopularContents.setOnClickListener(new View.OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
//										
//										singleArtistName = ""+tvArtistName[v.getId()].getText();
//										singleArtistId = ""+artistIds[v.getId()];
//										singleArtistRole = ""+tvArtistRole[v.getId()].getText();
//										
//										i.putExtra("artistName", tvArtistName[v.getId()].getText());
//										i.putExtra("artistId", artistIds[v.getId()]);
//										i.putExtra("artistRole", tvArtistRole[v.getId()].getText());

										if(v.getTag()!=null)
										{
											MovieSummary.movieSummaryInstance.finish();
											MovieSummary.singleArtistId  = v.getTag().toString();
											Log.d(DEBUG_TAG, "iDDDD:"+MovieSummary.singleArtistId );
											MovieSummary.movieSummaryInstance.startActivity(new Intent(MovieSummary.movieSummaryInstance.getBaseContext(), ActorProfile.class));
											MovieSummary.movieSummaryInstance.overridePendingTransition( R.anim.animation1, R.anim.animation2 );
										}
									}
								});
								
								View v1 = new View(MovieSummary.movieSummaryInstance);
								v1.setBackgroundColor(Color.WHITE);
								RelativeLayout.LayoutParams vL1= new RelativeLayout.LayoutParams(ShareData.padding10, ViewGroup.LayoutParams.MATCH_PARENT); 
								vL1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
												 
								final RelativeLayout rl1 = new RelativeLayout(MovieSummary.movieSummaryInstance);
								rl1.addView(llPopularContents, llPopularContentsParams);
												
								fl1.addView(rl1,rl1Params);
								fl1.addView(v1, vL1);
							}
							fl2.addView(fl1, fl1Params);
							hs1.addView(fl2, fl2Params);
							
							//makeJsonObjectRequestMovieStills("http://bongobd.com/api/content.php?id="+ListViewAdapter.singleMovieId);
						} 
						catch (JSONException e) 
						{
							e.printStackTrace();
//							Toast.makeText(MovieSummary.movieSummaryInstance.getApplicationContext(),
//									"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
							
							MovieSummary.tvCastAndCrew.setText("");
							MovieSummary.castCrewLayout.removeView(MovieSummary.tvCastAndCrew);
							//makeJsonObjectRequestMovieStills("http://bongobd.com/api/content.php?id="+ListViewAdapter.singleMovieId);
						}
						//hidepDialog();
					}
				}, new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error) 
					{
						VolleyLog.d(DEBUG_TAG, "Error: " + error.getMessage());
						
//						Toast.makeText(StartingPage.startInstance.getApplicationContext(),
//								error.getMessage(), Toast.LENGTH_SHORT).show();
						
						// hide the progress dialog
						//hidepDialog();
						MovieSummary.tvCastAndCrew.setText("");
						MovieSummary.castCrewLayout.removeView(MovieSummary.tvCastAndCrew);
//						makeJsonObjectRequestMovieStills("http://bongobd.com/api/content.php?id="+9);
					}
				});

		// Adding request to request queue
		requestQueue.add(jsonObjReq);
		makeJsonObjectRequestMovieStills("http://bongobd.com/api/content.php?id="+ListViewAdapter.singleMovieId);
		Log.d(DEBUG_TAG,"movie summary movie stills:http://bongobd.com/api/content.php?id="+ListViewAdapter.singleMovieId);
	}
	
	
	
	
	public static void makeJsonObjectRequestMovieStills(String urlJsonObj) 
	{
		imageLoader = new ImageLoader(MovieSummary.movieSummaryInstance);
		
		Log.d(DEBUG_TAG, "makeJsonObjectRequestMovieStills entered ");
		
		actorImageCounter = 0;
		requestQueue = Volley.newRequestQueue(StartingPage.startInstance);
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
				urlJsonObj, null, new Response.Listener<JSONObject>() 
				{
					@Override
					public void onResponse(JSONObject response) 
					{
						//Log.d(DEBUG_TAG, response.toString());
						try
						{
							JSONObject data = response.getJSONObject("data");
							//Log.d(DEBUG_TAG, "data: " + data);
							
							JSONArray array =data.getJSONArray("images");
							Log.d(DEBUG_TAG, "images: " + array);
							
							movieStillsImages = new String[array.length()+3];
							
							for (int i = 0; i < array.length(); i++) 
							{
								String js= array.getString(i);
							    movieStillsImages[i] = "http://bongobd.com/wp-content/themes/bongobd/images/posterimage/"+js;
							    Log.d(DEBUG_TAG, "movieStillsImages:::http://bongobd.com/wp-content/themes/bongobd/images/posterimage/"+js);
							}
							if( array.length()==0)
							{
								MovieSummary.tvMovieStills.setText("");
								MovieSummary.castCrewLayout.removeView(MovieSummary.tvMovieStills);
							}
							
							//Add Most Popular images
							HorizontalScrollView hs1 = (HorizontalScrollView) MovieSummary.movieSummaryInstance.findViewById(R.id.movieStillsHS);
//							LinearLayout.LayoutParams castCrewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//							castCrewLayoutParams.setMargins(ShareData.padding10, ShareData.padding10, ShareData.padding10, 0);
//							hs1.setLayoutParams(castCrewLayoutParams);
							
							LinearLayout fl1 = new LinearLayout(MovieSummary.movieSummaryInstance);
							LinearLayout.LayoutParams fl1Params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
							fl1.setOrientation(LinearLayout.HORIZONTAL);
									
							LinearLayout fl2 = new LinearLayout(MovieSummary.movieSummaryInstance);
							LinearLayout.LayoutParams fl2Params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
							fl2.setOrientation(LinearLayout.VERTICAL);
									
							RelativeLayout.LayoutParams rl1Params = 
									new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
									RelativeLayout.LayoutParams.WRAP_CONTENT);

							im2 = new ImageView[array.length()+1];
							for(int i = 1; i <= array.length(); i++)
							{
								LinearLayout llPopularContents = new LinearLayout(MovieSummary.movieSummaryInstance);
								LinearLayout.LayoutParams llPopularContentsParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
								llPopularContents.setOrientation(LinearLayout.VERTICAL);
								
								im2[i] = new ImageView(MovieSummary.movieSummaryInstance);
								im2[i].setScaleType(ScaleType.FIT_XY);
								im2[i].setImageResource(R.drawable.ic);
								RelativeLayout.LayoutParams imParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
								imParams1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
								imParams1.width = ShareData.getScreenWidth(MovieSummary.movieSummaryInstance)/3;
								imParams1.height = ShareData.getScreenWidth(MovieSummary.movieSummaryInstance)/3-ShareData.getScreenWidth(MovieSummary.movieSummaryInstance)/9;
								im2[i].setTag(i);
								Log.d(DEBUG_TAG, "movie stills:"+movieStillsImages[i]);
								imageLoader.DisplayImage(movieStillsImages[i-1], im2[i]);
								
								llPopularContents.addView(im2[i], imParams1);
//								llPopularContents.addView(tv, tvParams);
//								llPopularContents.addView(tv1, tv1Params);
								
								View v1 = new View(MovieSummary.movieSummaryInstance);
								v1.setBackgroundColor(Color.WHITE);
								RelativeLayout.LayoutParams vL1= new RelativeLayout.LayoutParams(ShareData.padding10,ViewGroup.LayoutParams.MATCH_PARENT); 
								vL1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
												 
								final RelativeLayout rl1 = new RelativeLayout(MovieSummary.movieSummaryInstance);
								rl1.addView(llPopularContents, llPopularContentsParams);
												
								fl1.addView(rl1,rl1Params);
								fl1.addView(v1, vL1);
							}
							fl2.addView(fl1, fl1Params);
							hs1.addView(fl2, fl2Params);
							
						} 
						catch (JSONException e) 
						{
							e.printStackTrace();
//							Toast.makeText(MovieSummary.movieSummaryInstance,
//									"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
							
							MovieSummary.tvMovieStills.setText("");
							MovieSummary.castCrewLayout.removeView(MovieSummary.tvMovieStills);
						}
						
						//hidepDialog();
					}
				}, new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error) 
					{
						VolleyLog.d(DEBUG_TAG, "Error: " + error.getMessage());
						
//						Toast.makeText(MovieSummary.movieSummaryInstance.getApplicationContext(),
//								error.getMessage(), Toast.LENGTH_SHORT).show();
						
						// hide the progress dialog
						//hidepDialog();
						MovieSummary.tvMovieStills.setText("");
						MovieSummary.castCrewLayout.removeView(MovieSummary.tvMovieStills);
					}
				});

		// Adding request to request queue
		requestQueue.add(jsonObjReq);
	}
}

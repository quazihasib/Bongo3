package com.tab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.movies.bongobd.R;
import com.movies.startingPage.StartingPage;

public class Search
{
	public static EditText etSearch;
	public static String search;
	public static PopupWindow mpopup;
	public static RequestQueue requestQueue;
	public static  ArrayAdapter<String> adapter;
	public static String[] searchSuggestions;
	public static String DEBUG_TAG = Search.class.getSimpleName();
	
	public static ArrayAdapter<String> makeJsonObjectRequestForSearchQuery(String urlJsonObj, final Activity instance)
	{
		Log.d(DEBUG_TAG, "urlJsonObj:"+ urlJsonObj);
		requestQueue = Volley.newRequestQueue(instance);
		
		JsonArrayRequest s = new JsonArrayRequest(urlJsonObj, new Response.Listener<JSONArray>() 
		{
			@Override
			public void onResponse(JSONArray arg0)
			{
				// TODO Auto-generated method stub
				searchSuggestions = new String[arg0.length()];
				//Log.d(DEBUG_TAG,"Search Responce:"+ arg0.toString());
				for (int i=0; i<arg0.length(); i++)
				{
					try 
					{
						JSONObject json = arg0.getJSONObject(i);
						String a  = json.toString().trim().replace("srh_txt", "");
						a = a.trim().replaceAll("[{}]","");
						a = a.trim().replace("\"", "");
						a = a.trim().replace(":", "");
						
						searchSuggestions[i] = a;
						Log.d(DEBUG_TAG, "suggestions:" + searchSuggestions[i]);
					} 
					catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				adapter = new ArrayAdapter<String>(instance,
						android.R.layout.simple_dropdown_item_1line, searchSuggestions);
				
				if(Search.mpopup!=null)
				{
					Search.mpopup.dismiss();
				}
				Search.showSortPopup(instance);
			}
		},
        new Response.ErrorListener() 
		{
            @Override
            public void onErrorResponse(VolleyError error)
            {
                 Log.d(DEBUG_TAG, "error:"+error.getMessage());
            }
        });
		// Adding request to request queue
		requestQueue.add(s);
		return adapter;
	}
	
//	public static Dialog dialogs(final Activity instance)
//	{
//		final Dialog dialog = new Dialog(instance);
//		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		dialog.setContentView(R.layout.dialog_search);
//		
//		//dialog.setTitle("Search");
////		dialog.setCancelable(false);
//		dialog.getWindow().setLayout(ShareData.getScreenWidth(instance), ShareData.getScreenWidth(instance));
//		
//		ListView lv= (ListView) dialog.findViewById(R.id.listItems);
//		
//		 ArrayAdapter<String> adapter=new ArrayAdapter<String>(
//		            instance,android.R.layout.simple_list_item_1, searchSuggestions)
//		   {
//
//		        @Override
//		        public View getView(int position, View convertView, ViewGroup parent)
//		        {
//		            View view =super.getView(position, convertView, parent);
//
//		            TextView textView=(TextView) view.findViewById(android.R.id.text1);
//		            textView.setTextColor(Color.BLACK);
//
//		            return view;
//		        }
//		    };
//		    
//		lv.setAdapter(adapter);
//		
//		lv.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//			    // When clicked, show a toast with the TextView text
//			    Toast.makeText(instance,
//				((TextView) view).getText(), Toast.LENGTH_SHORT).show();
//			}
//		});
//		
//		return dialog;
//	}
	
	
	public static void showSortPopup(final Activity instance) 
	{
//		if(mpopup!=null)
//		{
//		 	mpopup.dismiss();
//		}
		Rect r = new Rect();
		View rootview = instance.getWindow().getDecorView(); 
		rootview.getWindowVisibleDisplayFrame(r);
		

        int screenHeight = ShareData.getScreenHeight(instance);
        int heightDifference = screenHeight - (r.bottom - r.top);
        Log.d("Keyboard Size", "Size keyboard: " + heightDifference);
        
		// inflating popup layout
		View popUpView = instance.getLayoutInflater().inflate(R.layout.dialog_search, null);
		// Creation of popup
		mpopup = new PopupWindow(popUpView, LayoutParams.FILL_PARENT,
				heightDifference, false); 
		mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
		// Displaying popup
		mpopup.showAtLocation(popUpView, Gravity.TOP, 0, ShareData.getScreenHeight(instance)/8); 
		    
		final ListView lv= (ListView) popUpView.findViewById(R.id.listItems);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(
				instance,android.R.layout.simple_list_item_1, searchSuggestions)
		{

			 @Override
			 public View getView(int position, View convertView, ViewGroup parent)
			 {
			      View view =super.getView(position, convertView, parent);
			            
			      TextView textView=(TextView) view.findViewById(android.R.id.text1);
			      textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int)instance.getResources().getDimension(R.dimen.text_size2));
			      textView.setTextColor(Color.BLACK);
			      textView.setIncludeFontPadding(false);
//			      ViewGroup.LayoutParams textViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//			      textViewParams.setMargins(ShareData.padding10,  -ShareData.padding10*2, ShareData.padding10,  -ShareData.padding10*2);
//			      textView.setLayoutParams(textViewParams);
			            
			      
			      view.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
				        mpopup.dismiss();
						    
				        AddMenu.et.setText(((TextView) v).getText());
//							StartingPage.browseAll = 3;
//							AddMenu.searchQuery = AddMenu.et.getText().toString().trim();
//							StartingPage.startInstance.finish();
//							StartingPage.startInstance.startActivity(new Intent(StartingPage.startInstance.getBaseContext(), Movies.class));
//							StartingPage.startInstance.overridePendingTransition(R.anim.animation1, R.anim.animation2);
						AddMenu.actionSearch(StartingPage.startInstance);
					}
				});
			      return view;
			 }
		};
			    
		lv.setAdapter(adapter);
	}
}

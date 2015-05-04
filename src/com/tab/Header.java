package com.tab;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movies.bongobd.R;

public class Header extends LinearLayout 
{
	public static final String TAG = Header.class.getSimpleName();

	public TextView tv1, tv2, tv3, tv4, tv5;
	public View v1, v2, v3, v4, v5;
	public ImageView menuIcon, bongoIcon, searchIcon;
	public Activity Instance;
	public LinearLayout headerLayout;
	public HorizontalScrollView hsv;
	public LinearLayout.LayoutParams menuLayout,bongoLayout,searchLayout;
	public ImageView sideLeft, sideRight;
	public static int mainMaxScroll;
	
	public static String DEBUG_TAG = Header.class.getSimpleName();
	 
	public Header(Context context) 
	{
		super(context);
	}

	public Header(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public Header(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs);
	}

	public void initHeader(Activity instance) 
	{
		inflateHeader(instance);
	}

	private void inflateHeader(final Activity instance)
	{
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.header, this);
		
		int width = ShareData.getScreenWidth(instance)/3;
		int height = (int) (width/2.5);
		LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(width, height);
		
		headerLayout = (LinearLayout) findViewById(R.id.headerLayout);
		LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, ShareData.headerHeight );
		headerLayout.setLayoutParams(headerParams);
		
		menuIcon = new ImageView(instance); 
		menuIcon.setImageResource(R.drawable.menu);
		menuLayout = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
		
		bongoIcon = new ImageView(instance);
		bongoIcon.setImageResource(R.drawable.logo);
		bongoLayout = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 5);
	
		searchIcon = new ImageView(instance);
		searchIcon.setImageResource(R.drawable.search);
		searchLayout = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
		
		bongoLayout.setMargins(ShareData.padding10*4, ShareData.padding10*2, ShareData.padding10*4, ShareData.padding10*2);
		menuLayout.setMargins(ShareData.padding10*3, ShareData.padding10*2, ShareData.padding10*3, ShareData.padding10*2);
		searchLayout.setMargins(ShareData.padding10*2, ShareData.padding10*2, ShareData.padding10*2, ShareData.padding10*2);
		
		
		tv1 = (TextView) findViewById(R.id.tv1);
		tv1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int)getResources().getDimension(R.dimen.text_size1));
		tv1.setText("Movies");
		tv1.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
		tv1.setLayoutParams(tv1Params);
		
		tv2 = (TextView) findViewById(R.id.tv2);
		tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int)getResources().getDimension(R.dimen.text_size1));
		tv2.setText("TV");
		tv2.setLayoutParams(tv1Params);
		
		tv3 = (TextView) findViewById(R.id.tv3);
		tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int)getResources().getDimension(R.dimen.text_size1));
		tv3.setText("Music");
		tv3.setLayoutParams(tv1Params);
		
		tv4 = (TextView) findViewById(R.id.tv4);
		tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int)getResources().getDimension(R.dimen.text_size1));
		tv4.setText("People");
		tv4.setLayoutParams(tv1Params);
		
//		tv5 = (TextView) findViewById(R.id.tv5);
//		tv5.setText("SUBSCRIBE");
//		tv5.setLayoutParams(tv1Params);
		
		v1 = (View) findViewById(R.id.v1);
		LinearLayout.LayoutParams v1Params = new LinearLayout.LayoutParams(ShareData.padding10/9, ShareData.headerHeight );
		v1.setLayoutParams(v1Params);
		
		v2 = (View) findViewById(R.id.v2);
		LinearLayout.LayoutParams v2Params = new LinearLayout.LayoutParams(ShareData.padding10/9, ShareData.headerHeight );
		v2.setLayoutParams(v2Params);
		
		v3 = (View) findViewById(R.id.v3);
		LinearLayout.LayoutParams v3Params = new LinearLayout.LayoutParams(ShareData.padding10/9, ShareData.headerHeight );
		v3.setLayoutParams(v3Params);
		
//		v4 = (View) findViewById(R.id.v4);
//		LinearLayout.LayoutParams v4Params = new LinearLayout.LayoutParams(ShareData.padding10/9, ShareData.headerHeight );
//		v4.setLayoutParams(v4Params);
		
		headerLayout.addView(menuIcon, menuLayout);
		headerLayout.addView(bongoIcon, bongoLayout);
		headerLayout.addView(searchIcon, searchLayout);
		
		hsv = (HorizontalScrollView)findViewById(R.id.hsv);
		
		sideLeft = (ImageView)findViewById(R.id.sideLeft);
		sideLeft.getLayoutParams().width = width/3;
		sideLeft.getLayoutParams().height = (int)(height/1.5);
		sideLeft.setPadding(0, height/4, 0, 0);
		sideLeft.setVisibility(View.INVISIBLE);
		
		sideRight = (ImageView)findViewById(R.id.sideRight);
		sideRight.getLayoutParams().width = width/3;
		sideRight.getLayoutParams().height = (int)(height/1.5);
		sideRight.setPadding(0, height/4, 0, 0);
		
		ViewTreeObserver vto = hsv.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() 
		{
		    @Override
		    public void onGlobalLayout() 
		    {
		    	hsv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		        mainMaxScroll = hsv.getChildAt(0).getMeasuredWidth()-
		        		instance.getWindowManager().getDefaultDisplay().getWidth();
		        //Log.d(DEBUG_TAG, "mainMaxScroll:"+mainMaxScroll);

		    }
		});
		
	}
}
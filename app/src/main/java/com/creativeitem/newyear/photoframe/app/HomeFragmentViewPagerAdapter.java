package com.creativeitem.newyear.photoframe.app;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creativeitem.newyear.photoframe.R;
import com.creativeitem.newyear.photoframe.model.Slides;


import java.util.ArrayList;

/**
 * 
 * The ViewPagerAdapter used for the HomeFragment ViewPager
 * 
 */
public class HomeFragmentViewPagerAdapter extends PagerAdapter {

	private LayoutInflater mLayoutInflater;


	private ArrayList<Slides> mItems;
	Context context;


	/**
	 * 
	 * @param context
	 *            the application context
	 * @param itemsParam
	 *            ViewPager PreviewPicture items
	 */
	public HomeFragmentViewPagerAdapter(Context context,
										ArrayList<Slides> itemsParam) {
		mLayoutInflater = LayoutInflater.from(context);

		mItems = itemsParam;
		this.context=context;
	}

	@Override
	public Object instantiateItem(View collection, int position) {
		Slides item = mItems.get(position);

		View convertView = mLayoutInflater.inflate(
				R.layout.fragment_main_view_pager_adapter, null);
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.viewPagerItemImageView);
		TextView textView = (TextView) convertView
				.findViewById(R.id.viewPagerItemTitleTextView);

//		String uri = "drawable://"
//				+ mAppContext.getResources().getIdentifier(
//						item.getPreviewPictureUri(), "drawable",
//						mAppContext.getPackageName());
//
//		mImageLoader.displayImage(uri, imageView, mImageOptions);

		textView.setText(item.slideName);

		Glide.with(context).load(item.slideImage).into(imageView);
				((ViewPager) collection).addView(convertView, 0);

		return convertView;

	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

}

package com.creativeitem.newyear.photoframe.frame;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.creativeitem.newyear.photoframe.R;

public class Adapter_grid extends BaseAdapter {

	Integer[] Frame_id;
	Context context;

	LayoutInflater inflater = null;

	public Adapter_grid(Context context, Integer[] Frame_id) {
		// TODO Auto-generated constructor stub
		this.Frame_id = Frame_id;
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Frame_id.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return Frame_id[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class Holder {

		ImageView img;

	}

	Holder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View rowView = convertView;

		if (rowView == null) {
			holder = new Holder();
			rowView = inflater.inflate(R.layout.activity_grid, null);
			holder.img = (ImageView) rowView.findViewById(R.id.imageView1);
			rowView.setTag(holder);
		} else {
			holder = (Holder) rowView.getTag();
		}

		
		new Async_Image(holder.img, Frame_id[position]).execute();

		Log.i("ok...", "" + Frame_id[position]);
		holder.img.setImageResource(Frame_id[position]);

		return rowView;
	}

	class Async_Image extends AsyncTask<Object, String, String> {
		ImageView img;
		int iId;

		public Async_Image(ImageView img, int image_id) {
			// TODO Auto-generated constructor stub
			this.img = img;
			iId = image_id;

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			img.setImageResource(iId);

		}

		@Override
		protected String doInBackground(Object... params) {
			// TODO Auto-generated method stub
			return null;
		}

	}
}

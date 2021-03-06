package com.example.jagdeep.wear1.adapter;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.jagdeep.sharedlibrary.model.tv.client.Show;
import com.example.jagdeep.wear1.R;

import java.util.ArrayList;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder> {

	private ArrayList<Show> shows;
	ImageLoader imageLoader;

	public ShowAdapter(ArrayList<Show> shows, ImageLoader imageLoader) {
		this.shows = shows;
		this.imageLoader = imageLoader;
	}

	// Provide a reference to the views for each data item
	// Complex data items may need more than one view per item, and
	// you provide access to all the views for a data item in a view holder
	public static class ViewHolder extends RecyclerView.ViewHolder {

		// each data item is just a string in this case
		public View mCardView;
		public TextView mTitle;
		public TextView mTimeStamp;
		public TextView mChannel;
		public ImageView mImageView;

		public ViewHolder(View v) {
			super(v);
			mCardView = v;
			mImageView = (ImageView) v.findViewById(R.id.image);
			mTitle = (TextView) v.findViewById(R.id.title);
			mTimeStamp = (TextView) v.findViewById(R.id.timestamp);
			mChannel = (TextView) v.findViewById(R.id.channel);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View v = LayoutInflater.from(viewGroup.getContext())
				.inflate(R.layout.view_card, viewGroup, false);

		// set the view's size, margins, paddings and layout parameters
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, int i) {
		Show show = shows.get(i);
		viewHolder.mTitle.setText(show.getTitle());

		//send request to service
		imageLoader.get(show.getImage(), new ImageLoader.ImageListener() {
			@Override
			public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
				onImageReceived(response.getBitmap(), viewHolder);
			}

			@Override
			public void onErrorResponse(VolleyError error) {
				onImageReceived(null, viewHolder);
			}
		});

		viewHolder.mImageView.setAdjustViewBounds(true);
		viewHolder.mTimeStamp.setText(show.getTime());
		viewHolder.mChannel.setText(show.getChannel_name());
	}

	void onImageReceived(Bitmap bitmap, ViewHolder viewHolder) {
		if (bitmap != null) {
			viewHolder.mImageView.setImageBitmap(bitmap);
			Palette palette = Palette.generate(bitmap, 100);
			if (viewHolder.mCardView.getVisibility() != View.VISIBLE) {
				viewHolder.mCardView.startAnimation(
						AnimationUtils.loadAnimation(viewHolder.mCardView.getContext(), android.R.anim.fade_in));
				viewHolder.mCardView.setVisibility(View.VISIBLE);
			}
			viewHolder.mTitle.setBackgroundColor(palette.getDarkVibrantColor(android.R.color.black));
			viewHolder.mTitle.setTextColor(palette.getLightMutedColor(android.R.color.white));
			viewHolder.mImageView.setBackgroundColor(palette.getDarkVibrantColor(android.R.color.white));
			viewHolder.mTimeStamp.setTextColor(palette.getDarkVibrantColor(android.R.color.white));
			viewHolder.mChannel.setTextColor(palette.getDarkVibrantColor(android.R.color.white));
		} else {
			viewHolder.mCardView.setVisibility(View.GONE);
		}
	}
	//
	//	Bitmap getBitmap(byte[] data) {
	//		Bitmap bmp;
	//		BitmapFactory.Options options = new BitmapFactory.Options();
	//		options.inMutable = true;
	//		bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);
	//		return bmp;
	//	}


	@Override
	public void onViewDetachedFromWindow(ViewHolder holder) {
		super.onViewDetachedFromWindow(holder);
		holder.mCardView.clearAnimation();
	}

	@Override
	public int getItemCount() {
		return shows.size();
	}

}

package com.grieex.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.grieex.R;
import com.grieex.model.tables.Series;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;


/**
 * Created by Griee on 24.9.2015.
 */
public class SearchSeriesAdapter extends RecyclerView.Adapter<SearchSeriesAdapter.ViewHolder> {
    private final ArrayList<Series> mData;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private OnAddClickListener listener2;

    public interface OnAddClickListener {
        void onAddClick(View itemView, int position);
    }

    public void setOnAddClickListener(OnAddClickListener listener) {
        this.listener2 = listener;
    }

    private final DisplayImageOptions options;

    public class ViewHolder extends RecyclerView.ViewHolder {
        final com.grieex.widget.AspectRatioImageView poster;
        final TextView tvSeriesName;
        final FloatingActionButton btnAdd;

        ViewHolder(View v) {
            super(v);
            poster = v.findViewById(R.id.poster);
            tvSeriesName = v.findViewById(R.id.tvSeriesName);
            btnAdd = v.findViewById(R.id.btnAdd);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener2 != null)
                        listener2.onAddClick(btnAdd, getLayoutPosition());
                }
            });
        }


    }

    public void addAll(ArrayList<Series> items, int positionFrom, int positionTo) {
        mData.addAll(items);

        for (int i = positionFrom; i <= positionTo; i++) {
            notifyItemInserted(i);
        }
    }

    public void addAllEnd(ArrayList<Series> items) {
        int positionFrom = mData.size();
        int positionTo = mData.size()+items.size();

        mData.addAll(items);

        for (int i = positionFrom; i <= positionTo; i++) {
            notifyItemInserted(i);
        }
    }

    public void add(int position, Series item) {
        mData.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Series item) {
        int position = mData.indexOf(item);
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void clear(){
        mData.clear();
    }

    public SearchSeriesAdapter(ArrayList<Series> myDataset) {
        mData = myDataset;
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.transparent_back).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(500)).build();
    }

    @Override
    public SearchSeriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_series, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Series show = mData.get(position);

        holder.tvSeriesName.setText(show.getSeriesName());
        ImageLoader.getInstance().displayImage(show.getPoster(), holder.poster, options);

        if (show.getIsExisting()){
            holder.btnAdd.setVisibility(View.GONE);
        }else{
            holder.btnAdd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public Series getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return mData.get(position).getID();
    }

    public int getPositionFromTraktId(int traktId) {
        if (traktId < 0)
            return -1;

        for (int i = 0; i <= mData.size(); i++) {
            if (mData.get(i).getTraktId() == traktId)
                return i;
        }
        return -1;
    }

}

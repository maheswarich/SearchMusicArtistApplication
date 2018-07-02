package com.searchmusicartistapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class RetrofitInfoAdapter extends BaseAdapter {
    Context context;
    Activity activity;
    List<Artist> artistInfoList;

    public RetrofitInfoAdapter(Activity activity, List<Artist> artistInfoList) {
        this.context = activity;
        this.activity = activity;
        this.artistInfoList = artistInfoList;
    }

    @Override
    public int getCount() {
        return artistInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return artistInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return artistInfoList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            view = inflater.inflate(R.layout.artist_info_adapter, null);
        } else {
            view = convertView;
        }
        ImageView artistIcon = view.findViewById(R.id.imageViewArtistIcon);
        TextView artistUserName = view.findViewById(R.id.textViewArtistName);
        TextView artistUrl = view.findViewById(R.id.textViewArtistUrl);
        TextView artistListerners = view.findViewById(R.id.textViewArtistLisCount);

        final Artist artistInfo = artistInfoList.get(position);
        artistUserName.setText(artistInfo.getName());
        artistUrl.setText(artistInfo.getUrl());
        artistListerners.setText("Listeners :" + artistInfo.getPlaycount());

        Glide.with(context).load(artistInfo.getArtsitImage().get(0).getText())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .into(artistIcon);
        return view;
    }


}

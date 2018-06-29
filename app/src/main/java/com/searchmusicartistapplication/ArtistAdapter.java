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
import java.util.ArrayList;

public class ArtistAdapter extends BaseAdapter {
    Context context;
    Activity activity;
    ArrayList<ArtistInfo> artistInfoList;

    public ArtistAdapter(Activity activity, ArrayList<ArtistInfo> artistInfoList) {
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
        ImageView artistIcon = (ImageView) view.findViewById(R.id.imageViewArtistIcon);
        TextView artistUserName = (TextView) view.findViewById(R.id.textViewArtistName);
        TextView artistUrl = (TextView) view.findViewById(R.id.textViewArtistUrl);
        TextView artistListerners = (TextView) view.findViewById(R.id.textViewArtistLisCount);

        final ArtistInfo artistInfo = artistInfoList.get(position);
        artistUserName.setText(artistInfo.getName());
        artistUrl.setText(artistInfo.getUrl());
        artistListerners.setText("Listeners :" + artistInfo.getPlaycount());

        Glide.with(context).load(artistInfo.getArtsitImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .into(artistIcon);
      return view;
     }
}


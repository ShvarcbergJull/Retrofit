package com.example.retrefit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageList extends BaseAdapter {
    ArrayList<Hit> hits;
    Context ctx;
    Picasso picasso;

    public ImageList(Context ctx, ArrayList<Hit> hits, Picasso picasso) {
        this.hits = hits;
        this.ctx = ctx;
        this.picasso = picasso;
        Log.d("mytag", "welcome");
    }

    @Override
    public int getCount() {
        return hits.size();
    }

    @Override
    public Object getItem(int position) {
        return hits.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Hit hit = hits.get(position);
        Log.d("mytag", String.valueOf(hit.type));
        convertView = LayoutInflater.from(ctx).inflate(R.layout.hit_activity, parent, false);
        ImageView hitUser = convertView.findViewById(R.id.h);

        picasso.load(hit.previewURL).into(hitUser);

        return convertView;
    }
}

package com.example.ashwin.popularmovies20;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ashwin on 8/20/16.
 */
public class GridViewAdapter extends ArrayAdapter {

    private Context mContext;
    private int layoutResourceID;
    private ArrayList<GridItem> movieData= new ArrayList();

    public GridViewAdapter(Context mContext, int layoutResourceID, ArrayList<GridItem> movieData )
    {
        super(mContext, layoutResourceID, movieData);

        this.mContext= mContext;
        this.layoutResourceID= layoutResourceID;
        this.movieData= movieData;
    }

    public void setMovieData(ArrayList<GridItem> moviewData)
    {
        this.movieData= moviewData;
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row= convertView;
        ViewHolder holder= null;

        if(row==null)
        {
            LayoutInflater inflater= ((Activity) mContext).getLayoutInflater();
            row= inflater.inflate(layoutResourceID, parent, false);
            holder= new ViewHolder();
            holder.titleTextView= (TextView) row.findViewById(R.id.gridview_item_title_text);
            holder.posterImageView = (ImageView) row.findViewById(R.id.gridview_item_poster_image);
            row.setTag(holder);
        }
        else
        {
            holder= (ViewHolder) row.getTag();
        }

        GridItem item= movieData.get(position);
        holder.titleTextView.setText(Html.fromHtml(item.getTitle()));

        Picasso.with(mContext).load(item.getImage()).into(holder.posterImageView);
        return row;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView posterImageView;

    }
}

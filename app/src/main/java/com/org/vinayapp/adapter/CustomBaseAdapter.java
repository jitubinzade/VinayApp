package com.org.vinayapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.org.vinayapp.R;
import com.org.vinayapp.model.Complaint;
import com.org.vinayapp.utils.WebConstant;

import java.io.InputStream;
import java.util.List;

/**
 * Created by JITU on 18/04/2020.
 */
public class CustomBaseAdapter extends BaseAdapter {

    Context context;
    List<Complaint> complaints;

    public CustomBaseAdapter(Context context, List<Complaint> items) {
        this.context = context;
        this.complaints = items;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_complaint, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        Complaint complaint = (Complaint) getItem(position);
        holder.txtDesc.setText(complaint.getDesc());
        holder.txtTitle.setText(complaint.getTitle());
        new DownloadImageTask(holder.imageView,complaint.getImageName()).execute(complaint.getImageUrl());

        return convertView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        String imageName;
        public DownloadImageTask(ImageView bmImage,String imageName) {
            this.bmImage = bmImage; this.imageName=imageName;
        }

        protected Bitmap doInBackground(String... urls) {
            String URL= WebConstant.VIEWIMG+"?imageName="+imageName+"";
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(URL).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result)
        {
            bmImage.setImageBitmap(result);
        }
    }


    @Override
    public int getCount() {
        return complaints.size();
    }

    @Override
    public Object getItem(int position) {
        return complaints.get(position);
    }

    @Override
    public long getItemId(int position) {
        return complaints.indexOf(getItem(position));
    }
    
}

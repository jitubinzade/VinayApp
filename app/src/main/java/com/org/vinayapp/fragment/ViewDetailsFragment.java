package com.org.vinayapp.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.org.vinayapp.R;
import com.org.vinayapp.model.Complaint;
import com.org.vinayapp.utils.SessionManager;
import com.org.vinayapp.utils.WebConstant;

import java.io.InputStream;

public class ViewDetailsFragment extends Fragment {

    View view;
    ImageView imageView;
    TextView txtFloor,txtSection,txtClassroom,txtDescription;
    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.details_fragmnet,container,false);
        setHasOptionsMenu(true);
        imageView =(ImageView) view.findViewById(R.id.imageViewDet);
        sessionManager =new SessionManager(getContext());
        txtFloor=(TextView) view.findViewById(R.id.textFloor);
        txtSection=(TextView) view.findViewById(R.id.textSection);
        txtClassroom=(TextView) view.findViewById(R.id.textClassroom);
        txtDescription =(TextView) view.findViewById(R.id.txtDesc);
        Bundle bundle =getArguments();
        if(null!=bundle){

            Complaint complaint= (Complaint)bundle.getSerializable("complaint");
            new DownloadImageTask(complaint.getImageName()).execute();
            txtDescription.setText(complaint.getDesc());
            txtFloor.setText("Floor: "+complaint.getFloorName());
            txtSection.setText("Section: "+complaint.getSectionName());
            if(complaint.getClassroomId()>0){
                 txtClassroom.setText("Classroom: "+complaint.getClassroomName());
            }
        }

        return  view;
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        String imageName;
        public DownloadImageTask(String imageName) {
            this.imageName=imageName;
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
            imageView.setImageBitmap(result);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.add(1,3,1,"Logout");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case 3:
                sessionManager.clearLogin();
                getFragmentManager().beginTransaction().replace(android.R.id.content,new LoginFragment()).commit();
                break;
        }
        return true;
    }

}

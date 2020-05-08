package com.org.vinayapp.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.org.vinayapp.R;
import com.org.vinayapp.adapter.CustomBaseAdapter;
import com.org.vinayapp.model.Complaint;
import com.org.vinayapp.utils.JSonParser;
import com.org.vinayapp.utils.SessionManager;
import com.org.vinayapp.utils.WebConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JITU on 18/04/2020.
 */
public class ComplaintFragment extends Fragment {

    View view;
    ListView listView;
    CustomBaseAdapter customBaseAdapter;
    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.list_complaints,container,false);
        listView =(ListView) view.findViewById(R.id.listComplaints);
        setHasOptionsMenu(true);
        sessionManager =new SessionManager(getActivity());
        new GetJsonComplaint().execute();
        return view;
    }

    private void setListAdapter(List<Complaint> complaints){

        customBaseAdapter =new CustomBaseAdapter(getActivity(),complaints);
        listView.setAdapter(customBaseAdapter);
    }

    private class GetJsonComplaint extends AsyncTask<String ,Void,JSONObject> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog= new ProgressDialog(getContext());
            progressDialog.setMessage("Loding...");
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject jsonObject=null;
            try {
                String URL=null;
                if(sessionManager.getUserid().equals("1")){
                    URL= WebConstant.GETALLCOMPLAINTURL;
                } else {
                    URL= WebConstant.GETCOMPLAINTURL+"?userId="+sessionManager.getUserid()+"";
                }
                jsonObject= JSonParser.getJsonData(URL);
            }catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            try {

                List<Complaint> complaints =new ArrayList<Complaint>();

                if (null!=jsonObject) {

                    JSONArray jsonArray = jsonObject.getJSONArray("complaints");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject obj= jsonArray.getJSONObject(i);
                        String description= obj.getString("description");
                        String imageUrl= obj.getString("image");
                        Complaint complaint= new Complaint();
                        complaint.setDesc(description);
                        complaint.setImageUrl(imageUrl);
                        complaint.setImageName(imageUrl);
                        complaints.add(complaint);
                    }
                    progressDialog.dismiss();
                    setListAdapter(complaints);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

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

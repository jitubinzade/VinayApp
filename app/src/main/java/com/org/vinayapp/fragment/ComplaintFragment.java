package com.org.vinayapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                 Complaint complaint =  (Complaint) parent.getItemAtPosition(position);
                 /*String imageName= complaint.getImageName();
                 String floorName= complaint.getFloorName();
                 String sectionName= complaint.getSectionName();
                 String classroomName =complaint.getClassroomName();*/
                 Bundle bundle =new Bundle();
                 bundle.putSerializable("complaint",complaint);
                 ViewDetailsFragment viewDetailsFragment =new ViewDetailsFragment();
                 viewDetailsFragment.setArguments(bundle);
                 getFragmentManager().beginTransaction().replace(android.R.id.content,viewDetailsFragment).addToBackStack(null).commit();
            }
        });

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
                        JSONObject floorObject = obj.getJSONObject("floor");
                        JSONObject sectionObject = obj.getJSONObject("section");
                        String classroomId =null;
                        String classroomName =null;
                        try {
                            JSONObject classroomObject = obj.getJSONObject("classroom");
                             classroomId =classroomObject.getString("classroomId");
                             classroomName= classroomObject.getString("classroomName");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        String sectionId= sectionObject.getString("sectionId");
                        String sectionName= sectionObject.getString("sectionName");

                        String floorId = floorObject.getString("floorId");
                        String floorName= floorObject.getString("floorName");

                        Complaint complaint= new Complaint();
                        complaint.setDesc(description);
                        complaint.setImageUrl(imageUrl);
                        complaint.setImageName(imageUrl);
                        complaint.setFloorId(Integer.parseInt(floorId));
                        complaint.setFloorName(floorName);
                        complaint.setSectionId(Integer.parseInt(sectionId));
                        complaint.setSectionName(sectionName);
                        if(classroomId!=null){
                            complaint.setClassroomId(Integer.parseInt(classroomId));
                            complaint.setClassroomName(classroomName);
                        }else {
                            complaint.setClassroomId(0);
                        }
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

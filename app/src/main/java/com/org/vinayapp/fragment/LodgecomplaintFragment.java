package com.org.vinayapp.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.org.vinayapp.R;
import com.org.vinayapp.model.Floor;
import com.org.vinayapp.model.Section;
import com.org.vinayapp.utils.JSonParser;
import com.org.vinayapp.utils.SessionManager;
import com.org.vinayapp.utils.WebConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by JITU on 11/04/2020.
 */
public class LodgecomplaintFragment extends Fragment {

    View view;

    Spinner spinnerFloor,spinnerSection,spinnerClassroom;
    ArrayAdapter<Floor> floorArrayAdapter;
    ArrayAdapter<Floor> classRoomAdapter;
    ArrayAdapter<Section> sectionAdapter;
    SessionManager sessionManager;
    Button btnSubmitNext;
    private int floorId,sectionId,classRoomId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.fragment_lodged_complaint,container,false);
        setHasOptionsMenu(true);
        spinnerFloor=(Spinner) view.findViewById(R.id.spinnerFloor);
        spinnerSection=(Spinner) view.findViewById(R.id.spinnerSection);
        sessionManager =new SessionManager(getContext());
        spinnerSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        Section section = (Section) adapterView.getSelectedItem();
                        sectionId =section.getId();
                        if(sectionId==0){
                            Toast.makeText(getActivity(),"Please Select Section",Toast.LENGTH_LONG).show();
                        }else {
                            if(section!=null){
                                if(section.getName().equals("Classroom")){
                                    spinnerClassroom.setVisibility(View.VISIBLE);
                                    new GetClassRoomsServiceTask().execute(String.valueOf(sectionId));
                                }else{
                                    spinnerClassroom.setVisibility(View.INVISIBLE);
                                }
                            }
                        }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerClassroom=(Spinner) view.findViewById(R.id.spinnerClassRoom);
        spinnerClassroom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                       Floor floor =(Floor) adapterView.getSelectedItem();
                       classRoomId =floor.getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnSubmitNext =(Button) view.findViewById(R.id.btnSubmitNext);

        btnSubmitNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                bundle.putInt("floorId",floorId);
                bundle.putInt("sectionId",sectionId);
                bundle.putInt("classRoomId",classRoomId);
                SendImageComplaint sendImageComplaint =new SendImageComplaint();
                sendImageComplaint.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(android.R.id.content,sendImageComplaint).addToBackStack(null).commit();
            }
        });
        //for test
         //testData();
        new GetFloorServiceTask().execute();

        spinnerFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    Floor floor =(Floor) adapterView.getSelectedItem();
                    if(floor!=null){

                            if(floor.getId()==0){
                                Toast.makeText(getActivity(),"Please Select Floor",Toast.LENGTH_LONG).show();
                            } else {
                                String tempfloorId=String.valueOf(floor.getId());
                                floorId =floor.getId();
                                new GetSectionTask().execute(tempfloorId);
                            }
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        return view;
    }

    private void testData(){

        ArrayList<Floor> floors =new ArrayList<Floor>();
        Floor floor=new Floor();
        floor.setId(0);
        floor.setName("Please Select Floor");
        floors.add(floor);
        Floor floor1= new Floor();
        Floor floor2= new Floor();
        floor1.setId(1);
        floor2.setId(2);
        floor1.setName("test1");
        floor2.setName("test2");
        floors.add(floor1);
        floors.add(floor2);
        setFloorList(floors);

    }

    private class GetSectionTask extends AsyncTask<String,Void,JSONObject> {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog =new ProgressDialog(getActivity());
            progressDialog.setMessage("loading...");
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {

             JSONObject jsonObject =null;
             try {
                 String floorId= strings[0];
                 String URL= WebConstant.GETSECTIONURL+"?floorId="+floorId;
                 jsonObject=JSonParser.getJsonData(URL);

             }catch (Exception e){
                 e.printStackTrace();
             }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
             progressDialog.dismiss();
             try {

                 ArrayList<Section> arrayList =new ArrayList<Section>();
                 Section sec=new Section();
                 sec.setId(0);
                 sec.setName("Please Select Section");
                 arrayList.add(sec);
                 if(null!=jsonObject){
                     JSONArray jsonArray =jsonObject.getJSONArray("sectionList");
                     for(int i=0;i<jsonArray.length();i++){

                         JSONObject obj= jsonArray.getJSONObject(i);
                         String sectionName= obj.getString("sectionName");
                         int sectionId =Integer.parseInt(obj.getString("sectionId"));
                         Section section =new Section();
                         section.setId(sectionId);
                         section.setName(sectionName);
                         arrayList.add(section);
                     }
                 }
                 setSectionList(arrayList);

             }catch (Exception r){
                 r.printStackTrace();
             }

        }
    }

    private void setSectionList(ArrayList<Section> sectionList){

        sectionAdapter =new ArrayAdapter<Section>(getActivity(),android.R.layout.simple_spinner_item,sectionList);
        spinnerSection.setAdapter(sectionAdapter);

    }

    private void setFloorList(ArrayList<Floor> floorList) {

            floorArrayAdapter =new ArrayAdapter<Floor>(getActivity(),android.R.layout.simple_spinner_item,floorList);
            spinnerFloor.setAdapter(floorArrayAdapter);

    }
    private void setClassRoomList(ArrayList<Floor> classRoomList) {

        classRoomAdapter =new ArrayAdapter<Floor>(getActivity(),android.R.layout.simple_spinner_item,classRoomList);
        spinnerClassroom.setAdapter(classRoomAdapter);

    }


    private class GetClassRoomsServiceTask extends AsyncTask<String,Void,JSONObject> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog =new ProgressDialog(getActivity());
            progressDialog.setMessage("loading...");
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject jsonObject=null;
            try {
                String URL=WebConstant.GETCLASSROOMURL+"?floorId="+floorId+"";
                jsonObject= JSonParser.getJsonData(URL);

            }catch (Exception e){
                e.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progressDialog.dismiss();
            ArrayList<Floor> floors =new ArrayList<Floor>();
            Floor floor=new Floor();
            floor.setId(0);
            floor.setName("Please Select ClassRoom");
            floors.add(floor);
            try {
                if (null!=jsonObject) {
                    JSONArray jsonArray =jsonObject.getJSONArray("classroomList");
                    for(int i=0;i<jsonArray.length();i++){

                        JSONObject obj= jsonArray.getJSONObject(i);
                        int id=Integer.parseInt(obj.getString("classroomId"));
                        String name= obj.getString("classroomName");
                        floor= new Floor();
                        floor.setId(id);
                        floor.setName(name);
                        floors.add(floor);
                    }
                    setClassRoomList(floors);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class GetFloorServiceTask extends AsyncTask<String,Void,JSONObject> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog =new ProgressDialog(getActivity());
            progressDialog.setMessage("loading...");
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject jsonObject=null;
            try {
                jsonObject= JSonParser.getJsonData(WebConstant.GETFLOORURL);

            }catch (Exception e){
                e.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progressDialog.dismiss();
            ArrayList<Floor> floors =new ArrayList<Floor>();
            Floor floor=new Floor();
            floor.setId(0);
            floor.setName("Please Select Floor");
            floors.add(floor);
            try {
                if (null!=jsonObject) {
                    JSONArray jsonArray =jsonObject.getJSONArray("floorList");
                    for(int i=0;i<jsonArray.length();i++){

                            JSONObject obj= jsonArray.getJSONObject(i);
                            int id=Integer.parseInt(obj.getString("floorId"));
                            String name= obj.getString("floorName");
                            floor= new Floor();
                            floor.setId(id);
                            floor.setName(name);
                            floors.add(floor);
                    }
                    setFloorList(floors);
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


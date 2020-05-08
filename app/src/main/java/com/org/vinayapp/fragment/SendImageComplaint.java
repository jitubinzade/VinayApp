package com.org.vinayapp.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.org.vinayapp.R;
import com.org.vinayapp.model.Complaint;
import com.org.vinayapp.utils.JSonParser;
import com.org.vinayapp.utils.SessionManager;
import com.org.vinayapp.utils.WebConstant;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

/**
 * Created by JITU on 25/04/2020.
 */
public class SendImageComplaint extends Fragment {

    private int PICK_IMAGE_REQUEST = 100;
    private Button btnSelect, btnUpload;
    private ImageView imgView;
    View view;
    EditText edtDesc;
    //Uri to store the image uri
    private Uri filePath;
    private String fileName;
    private Button btnRegisterComplaint;
    private int floorId,sectionId,classRoomId;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.fragment_upload_image,container,false);
        imgView = (ImageView) view.findViewById(R.id.imgView);
        btnSelect = (Button) view.findViewById(R.id.btnSelect);
        btnUpload = (Button) view.findViewById(R.id.btnUpload);
        btnRegisterComplaint = (Button) view.findViewById(R.id.btnRegisterComplaint);
        btnRegisterComplaint.setEnabled(false);
        edtDesc=(EditText) view.findViewById(R.id.edtDesc);
        sessionManager =new SessionManager(getActivity());
        setHasOptionsMenu(true);
        Bundle bundle =getArguments();
        if(bundle!=null){
              floorId = bundle.getInt("floorId");
              sectionId =bundle.getInt("sectionId");
              classRoomId =bundle.getInt("classRoomId");
        }

        btnRegisterComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description= edtDesc.getText().toString();
                Complaint complaint =new Complaint();
                complaint.setFloorId(floorId);
                complaint.setSectionId(sectionId);
                complaint.setClassroomId(classRoomId);
                complaint.setImageName(fileName);
                complaint.setDesc(description);
                new LodgeComplaintService().execute(complaint);

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadMultipart();

            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                2000);
                    }
                    else {
                        showFileChooser();
                    }
            }
        });

        return view;
    }

    private class LodgeComplaintService extends AsyncTask<Complaint,Void,JSONObject> {

        ProgressDialog progressDialog =null;
        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(getContext());
            progressDialog.setMessage("loading..");
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(Complaint... cmp) {
            JSONObject jsonObject =null;
            try {
                Complaint complaint =cmp[0];
                jsonObject = JSonParser.lodgedComplaint(complaint,WebConstant.POSTCOMPLAINTURL,sessionManager.getUserid());

            }catch (Exception e){
                e.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progressDialog.dismiss();
            try {
                if(null!=jsonObject){
                    String statusCode =jsonObject.getString("statusCode");
                    if(statusCode.equals("200")){
                        Toast.makeText(getContext(),"Complaint Lodged Successfully!",Toast.LENGTH_LONG).show();
                    }else  {
                        Toast.makeText(getContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if(requestCode == PICK_IMAGE_REQUEST) {
                    filePath = data.getData();
                    Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    imgView.setImageBitmap(bitmapImage);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    public void uploadMultipart() {
        String path = getPath(filePath);
        //Uploading code
        try {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("loading..");
            progressDialog.show();

            SimpleMultiPartRequest multiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, WebConstant.UPLOAD_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        if(response!=null){
                            JSONObject jsonObject =new JSONObject(response);
                            String statusCode = jsonObject.getString("statusCode");
                            fileName =jsonObject.getString("fileName");
                            if(null!=statusCode && statusCode.equals("200")){
                                btnRegisterComplaint.setEnabled(true);
                                btnRegisterComplaint.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                btnRegisterComplaint.setTextColor(getResources().getColor(R.color.white));
                                progressDialog.setMessage("uploaded..");
                                progressDialog.dismiss();
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    progressDialog.setMessage("not uploaded..");
                    progressDialog.dismiss();
                }
            });
            multiPartRequest.addFile("image",path);
            Volley.newRequestQueue(getContext()).add(multiPartRequest);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
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

package com.org.vinayapp.utils;



import android.graphics.Bitmap;
import android.util.Base64;

import com.org.vinayapp.model.Complaint;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class JSonParser {


    /*public static String convertBitmapToString(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream); //compress to which format you want.
        byte[] byte_arr = stream.toByteArray();
        System.out.println("lenght= "+byte_arr.length);
        String imageStr = Base64.encodeToString(byte_arr, Base64.DEFAULT);
      //  System.out.println("imageStr= "+imageStr);
        return imageStr;
    }*/


    public static JSONObject lodgedComplaint(Complaint complaint,String URL,String userid){

          JSONObject jsonObject=null;
          try {
              DefaultHttpClient httpclient = new DefaultHttpClient();
              HttpPost setdata = new HttpPost(URL);
              List<NameValuePair> pairs = new ArrayList<NameValuePair>();
              pairs.add(new BasicNameValuePair("floorId", String.valueOf(complaint.getFloorId())));
              pairs.add(new BasicNameValuePair("sectionId", String.valueOf(complaint.getSectionId())));
              pairs.add(new BasicNameValuePair("classroomId", String.valueOf(complaint.getClassroomId())));
              pairs.add(new BasicNameValuePair("image", complaint.getImageName()));
              pairs.add(new BasicNameValuePair("description", complaint.getDesc()));
              pairs.add(new BasicNameValuePair("userId", userid));
              setdata.setEntity(new UrlEncodedFormEntity(pairs));
              HttpResponse response = httpclient.execute(setdata);
              HttpEntity httpEntity = response.getEntity();
              String data= EntityUtils.toString(httpEntity);
              jsonObject=new JSONObject(data);

          }catch (Exception e){
              e.printStackTrace();
          }
          return jsonObject;

    }

    public static JSONObject getJsonData(String URL)
    {
        JSONObject json=null;
        try
        {
            DefaultHttpClient client=new DefaultHttpClient();
            HttpGet httpGet=new HttpGet(URL);
            HttpResponse response=client.execute(httpGet);
            HttpEntity entity=response.getEntity();
            String data= EntityUtils.toString(entity);
            json=new JSONObject(data);
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        return json;
    }

    public static JSONObject invokeLoginService(String URl, String userid, String password) {
        JSONObject json = null;

        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost setdata = new HttpPost(URl);
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("loginId", userid));
            pairs.add(new BasicNameValuePair("password", password));
            setdata.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse response = httpclient.execute(setdata);
            HttpEntity httpEntity = response.getEntity();
            String data= EntityUtils.toString(httpEntity);
            json=new JSONObject(data);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }



    public static JSONObject setJsonObject(String URl, String email, String password) {
        JSONObject json = null;

        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost setdata = new HttpPost(URl);
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("email", email));
            pairs.add(new BasicNameValuePair("password", password));
            setdata.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse response = httpclient.execute(setdata);
            HttpEntity httpEntity = response.getEntity();
            String data= EntityUtils.toString(httpEntity);
            json=new JSONObject(data);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }


}

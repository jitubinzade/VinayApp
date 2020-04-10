package com.org.vinayapp.utils;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSonParser {


    public static JSONObject getJsonData(String URL)
    {
        JSONObject json=null;
        try
        {
            DefaultHttpClient client=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(URL);
            HttpResponse response=client.execute(httpPost);
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

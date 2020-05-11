package com.org.vinayapp.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.org.vinayapp.R;
import com.org.vinayapp.utils.JSonParser;
import com.org.vinayapp.utils.SessionManager;
import com.org.vinayapp.utils.WebConstant;
import org.json.JSONObject;

/**
 * Created by JITU on 03/04/2020.
 */
public class LoginFragment extends Fragment {

    View view;
    EditText edtuer,edtpassword;
    Button btnlogin;
    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.fragment_login,container, false);
        edtuer= (EditText) view.findViewById(R.id.edtusername);
        edtpassword= (EditText) view.findViewById(R.id.edtpassword);
        btnlogin= (Button) view.findViewById(R.id.btnlogin);
        sessionManager =new SessionManager(getActivity());
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                     if (isValid()) {

                           String userid= edtuer.getText().toString();
                           String password= edtpassword.getText().toString();
                           //getFragmentManager().beginTransaction().replace(android.R.id.content,new HomeFragment()).commit();
                           new LoginService().execute(userid,password);
                     }
            }
        });

        return view;
    }

    private boolean isValid(){

           if(edtuer.getText().length()<1){
               edtuer.requestFocus();
               edtuer.setError("Userid Required!");
               return false;
           }else if (edtpassword.getText().length()<1) {
               edtpassword.requestFocus();
               edtpassword.setError("Password Required!");
               return false;
           }
           return true;
    }


    private class LoginService extends AsyncTask<String,Void,JSONObject> {

        ProgressDialog progressDialog;
        String userid;

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
                String userid= strings[0];
                this.userid=userid;
                String password= strings[1];
                jsonObject = JSonParser.invokeLoginService(WebConstant.LOGINURL,userid,password);
            }catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progressDialog.dismiss();
            try {
                if(null!=jsonObject){

                    String logonStatus= jsonObject.getString("logonStatus");
                    if(null!=logonStatus){

                        if(logonStatus.equals("Success")){
                               String role= jsonObject.getString("role");
                               String userId= jsonObject.getString("userId");
                               sessionManager.setLoginPreference(userId,role);
                               getFragmentManager().beginTransaction().replace(android.R.id.content,new HomeFragment()).commit();
                        } else {
                            Toast.makeText(getActivity(),"Invalidate Userid/Password",Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

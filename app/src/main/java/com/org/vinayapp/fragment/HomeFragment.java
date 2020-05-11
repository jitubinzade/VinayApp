package com.org.vinayapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.org.vinayapp.R;
import com.org.vinayapp.utils.SessionManager;

/**
 * Created by JITU on 10/04/2020.
 */
public class HomeFragment extends Fragment {

    View view;
    Button btnLodge,btnReview;
    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.fragment_home ,container,false);
        setHasOptionsMenu(true);
        btnLodge =(Button) view.findViewById(R.id.btnLodegeComplaint);
        sessionManager= new SessionManager(getActivity());
        btnLodge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getFragmentManager().beginTransaction().replace(android.R.id.content,new LodgecomplaintFragment()).addToBackStack(null).commit();
            }
        });
        btnReview =(Button)view.findViewById(R.id.btnReviewComplaint);
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getFragmentManager().beginTransaction().replace(android.R.id.content,new ComplaintFragment()).addToBackStack(null).commit();
            }
        });

        return view;
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

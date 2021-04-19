package com.fundmanagement.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fundmanagement.R;

public class ProgressFragment extends Fragment implements LoginFragment.onSomeEventListenerLogin,SignUpFragment.onSomeEventListener {

    RelativeLayout relativeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_progress, container, false);
        relativeLayout = view.findViewById(R.id.progress_bar);
        return view;
    }

    @Override
    public void someEventLogin() {
        relativeLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void someEvent() {
        relativeLayout.setVisibility(View.VISIBLE);
    }
}
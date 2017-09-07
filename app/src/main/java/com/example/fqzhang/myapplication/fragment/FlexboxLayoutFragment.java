package com.example.fqzhang.myapplication.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fqzhang.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlexboxLayoutFragment extends Fragment {


    public FlexboxLayoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_flexbox, container, false);
        return inflate;
    }

}

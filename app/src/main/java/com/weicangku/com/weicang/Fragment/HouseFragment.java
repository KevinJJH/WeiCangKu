package com.weicangku.com.weicang.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weicangku.com.weicang.Activity.FansActivity;
import com.weicangku.com.weicang.Fragment.Base.ParentWithNaviFragment;
import com.weicangku.com.weicang.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HouseFragment extends ParentWithNaviFragment implements View.OnClickListener {
    private TextView Fans;


    public HouseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_house,container, false);
        initNaviView();
        Fans=getView(R.id.Fensi_Daili);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fans.setOnClickListener(this);
    }

    @Override
    protected String title() {
        return "仓库";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Fensi_Daili:
                startActivity(FansActivity.class,null,false);
                break;
        }

    }
}

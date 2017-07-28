package com.weicangku.com.weicang.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weicangku.com.weicang.Fragment.Base.ParentWithNaviFragment;
import com.weicangku.com.weicang.R;
import com.weicangku.com.weicang.Util.ClearEditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends ParentWithNaviFragment {
    private ClearEditText mClearEditText;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_home,container, false);
        initNaviView();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        mClearEditText=getView(R.id.et_msg_search);
    }

    @Override
    protected String title() {
        return "微仓";
    }
}

package com.weicangku.com.weicang.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Bean.LandDivide;
import com.weicangku.com.weicang.DB.LandDivideDb;
import com.weicangku.com.weicang.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddressChooseActivity extends ParentWithNaviActivity {
    private ListView mListView1;
    private ListView mListView2;
    private ListView mListView3;

    private LinearLayout mLinearLayout1;
    private LinearLayout mLinearLayout2;
    private LinearLayout mLinearLayout3;


    private List<String> sheng = new ArrayList<String>();
    private List<String> shi = new ArrayList<String>();
    private List<String> qu = new ArrayList<String>();

    private ArrayAdapter<String> shengAdapter;
    private ArrayAdapter<String> shiAdapter;
    private ArrayAdapter<String> quAdapter;

    private TextView shengTxt2;
    private TextView shengTxt3,shiTxt3,topView3;

    private static String shengStr,shiStr,quStr;
    private LandDivideDb landDivideDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_choose);
        initNaviView();
    }

    @Override
    protected void initView() {
        super.initView();
        mLinearLayout1 = (LinearLayout)this.findViewById(R.id.my_set_adresschoose_1);
        mLinearLayout2 = (LinearLayout)this.findViewById(R.id.my_set_adresschoose_2);
        mLinearLayout3 = (LinearLayout)this.findViewById(R.id.my_set_adresschoose_3);

        mLinearLayout1.setVisibility(View.VISIBLE);
        mLinearLayout2.setVisibility(View.GONE);
        mLinearLayout3.setVisibility(View.GONE);

        shengTxt2 = (TextView)this.findViewById(R.id.my_set_adresschoose_sheng_2);
        shengTxt3 = (TextView)this.findViewById(R.id.my_set_adresschoose_sheng_3);
        shiTxt3 = (TextView)this.findViewById(R.id.my_set_adresschoose_shi_3);
        topView3 = (TextView)this.findViewById(R.id.my_set_adresschoose_textview_3);

        landDivideDb=LandDivideDb.getInstance(getBaseContext());
        List<LandDivide> landDivide=landDivideDb.queryAddress("superior=?", new String[]{"1"});
        Iterator<LandDivide> iterator = null;
        if(landDivide!=null){
            iterator = landDivide.iterator();

            while(iterator.hasNext()){
                LandDivide l = iterator.next();
                sheng.add(l.getName());
            }
        }else{
            return;
        }
        shengAdapter = new ArrayAdapter(this,R.layout.my_set_addresschoose_listview_item,R.id.my_set_adresschoose_textview,sheng);
        shiAdapter = new ArrayAdapter(this,R.layout.my_set_addresschoose_listview_item,R.id.my_set_adresschoose_textview,shi);
        quAdapter = new ArrayAdapter(this,R.layout.my_set_addresschoose_listview_item,R.id.my_set_adresschoose_textview,qu);

        mListView1 = (ListView)this.findViewById(R.id.my_set_adresschoose_listview_1);
        mListView1.setAdapter(shengAdapter);
        mListView2 = (ListView)this.findViewById(R.id.my_set_adresschoose_listview_2);
        mListView2.setAdapter(shiAdapter);
        mListView3 = (ListView)this.findViewById(R.id.my_set_adresschoose_listview_3);
        mListView3.setAdapter(quAdapter);

        shengTxt2.setOnClickListener(click);
        shengTxt3.setOnClickListener(click);
        shiTxt3.setOnClickListener(click);
        mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                mLinearLayout1.setVisibility(View.GONE);
                mLinearLayout2.setVisibility(View.VISIBLE);
                mLinearLayout3.setVisibility(View.GONE);


                shi.clear();
                String name = sheng.get(position);
                String code = null;
                shengStr = name;
                shengTxt2.setText(name);

                List<LandDivide> landDivide=landDivideDb.queryAddress("name=?", new String[]{name});
                Iterator<LandDivide> iterator= landDivide.iterator();
                while(iterator.hasNext()){
                    LandDivide l = iterator.next();
                    code = l.getCode();
                    break;
                }
                List<LandDivide> landDivide2=landDivideDb.queryAddress("superior=?", new String[]{code});
                Iterator<LandDivide> iterator_2= landDivide2.iterator();
                while(iterator_2.hasNext()){
                    LandDivide l = iterator_2.next();
                    shi.add(l.getName());
                }
                shiAdapter.notifyDataSetChanged();
                quAdapter.clear();
                quAdapter.notifyDataSetChanged();
            }
        });
        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                qu.clear();

                String name = shi.get(position);
                String code = null;

                shiStr = name;
                shengTxt3.setText(shengStr);
                shiTxt3.setText(name);

                List<LandDivide> landDivide=landDivideDb.queryAddress("name=?", new String[]{name});

                Iterator<LandDivide> iterator= landDivide.iterator();
                while(iterator.hasNext()){
                    LandDivide l = iterator.next();
                    code = l.getCode();
                    break;
                }
                List<LandDivide> landDivide2=landDivideDb.queryAddress("superior=?", new String[]{code});
                if(landDivide2!=null){
                    Iterator<LandDivide> iterator_2= landDivide2.iterator();
                    while(iterator_2.hasNext()){
                        LandDivide l = iterator_2.next();
                        qu.add(l.getName());
                    }
                }
                quAdapter.notifyDataSetChanged();

                if(qu.size()<1){
                    mLinearLayout1.setVisibility(View.GONE);
                    mLinearLayout2.setVisibility(View.VISIBLE);
                    mLinearLayout3.setVisibility(View.GONE);

                    Intent i = new Intent(AddressChooseActivity.this,BuyAddressActivity.class);
                    i.putExtra("address", shengStr+","+shiStr);
                    startActivity(i);
                    finish();

                }else{

                    mLinearLayout1.setVisibility(View.GONE);
                    mLinearLayout2.setVisibility(View.GONE);
                    mLinearLayout3.setVisibility(View.VISIBLE);

                    mListView3.setVisibility(View.VISIBLE);
                    topView3.setText("请选择  县区/其他...");
                }
            }
        });
        mListView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String name = qu.get(position);

                quStr = name;

                Intent i2 = new Intent(AddressChooseActivity.this,BuyAddressActivity.class);
                i2.putExtra("address", shengStr+" "+shiStr+" "+quStr);
                startActivity(i2);
                finish();
            }
        });
    }
View.OnClickListener click = new View.OnClickListener() {

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.my_set_adresschoose_sheng_2:
                mLinearLayout1.setVisibility(View.VISIBLE);
                mLinearLayout2.setVisibility(View.GONE);
                mLinearLayout3.setVisibility(View.GONE);
                break;
            case R.id.my_set_adresschoose_sheng_3:
                mLinearLayout1.setVisibility(View.VISIBLE);
                mLinearLayout2.setVisibility(View.GONE);
                mLinearLayout3.setVisibility(View.GONE);
                break;
            case R.id.my_set_adresschoose_shi_3:
                mLinearLayout1.setVisibility(View.GONE);
                mLinearLayout2.setVisibility(View.VISIBLE);
                mLinearLayout3.setVisibility(View.GONE);
                break;

        }
    }

};
    @Override
    protected String title() {
        return "添加收货地址";
    }
}

package com.weicangku.com.weicang.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Bean.AddressInfo;
import com.weicangku.com.weicang.DB.AddressDb;
import com.weicangku.com.weicang.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class BuyAddressActivity extends ParentWithNaviActivity {
    private EditText jiequText;
    private EditText nameText;
    private EditText phoneText;

    private String provinces;
    private AddressInfo myAddress;

    private LinearLayout shengLinear;
    private LinearLayout jiequLinear;
    private LinearLayout nameLinear;
    private LinearLayout phoneLinear;

    private TextView shengText;
    private TextView jiequTextView;
    private TextView nameTextView;
    private TextView phoneTextView;

    private Button postBtn;
    private AddressInfo addressinfo;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_address);
        initNaviView();
    }

    @Override
    protected void initView() {
        super.initView();
        myAddress=new AddressInfo();
        Intent i = getIntent();
        provinces = i.getStringExtra("address");
        Bundle b = i.getBundleExtra("address_id");

        shengText =(TextView)findViewById(R.id.my_set_buyaddress_sheng);
        jiequText =(EditText)findViewById(R.id.my_set_buyaddress_jiequ);
        nameText =(EditText) findViewById(R.id.my_set_buyaddress_name);
        phoneText =(EditText)findViewById(R.id.my_set_buyaddress_phone);

        shengLinear =(LinearLayout)findViewById(R.id.my_set_buyaddress_sheng_linear);
        jiequLinear =(LinearLayout)findViewById(R.id.my_set_buyaddress_jiequ_linear);
        nameLinear =(LinearLayout)findViewById(R.id.my_set_buyaddress_name_linear);
        phoneLinear =(LinearLayout)findViewById(R.id.my_set_buyaddress_phone_linear);

        jiequTextView =(TextView)findViewById(R.id.my_set_buyaddress_jiequ_text);
        nameTextView =(TextView)findViewById(R.id.my_set_buyaddress_name_text);
        phoneTextView =(TextView)findViewById(R.id.my_set_buyaddress_phone_text);
        checkBox = (CheckBox)findViewById(R.id.my_set_address_checkbox);

        postBtn =(Button)findViewById(R.id.my_set_buyaddress_address_btn);
        if (provinces == null) {
        } else {
            myAddress.setProvinces(provinces);
            shengText.setText(provinces);
        }

        shengLinear.setOnClickListener(click);
        jiequLinear.setOnClickListener(click);
        nameLinear.setOnClickListener(click);
        phoneLinear.setOnClickListener(click);
        postBtn.setOnClickListener(click);

        jiequText.setOnFocusChangeListener(focusChanger);
        nameText.setOnFocusChangeListener(focusChanger);
        phoneText.setOnFocusChangeListener(focusChanger);

        if (b != null) {
            addressinfo = (AddressInfo) b.get("address");

            phoneText.setVisibility(View.GONE);
            phoneText.setText(addressinfo.getPhone());
            phoneLinear.setVisibility(View.VISIBLE);
            phoneTextView.setText(addressinfo.getPhone());

            shengText.setText(addressinfo.getProvinces());
            jiequText.setText(addressinfo.getStreet());
            nameText.setText(addressinfo.getName());
            phoneText.setText(addressinfo.getPhone());

            jiequText.setVisibility(View.GONE);
            jiequLinear.setVisibility(View.VISIBLE);
            jiequTextView.setText(addressinfo.getStreet());

            nameText.setVisibility(View.GONE);
            nameLinear.setVisibility(View.VISIBLE);
            nameTextView.setText(addressinfo.getName());

            if(addressinfo.isStatus()){
                checkBox.setChecked(true);
            }else{
                checkBox.setChecked(false);
            }

            myAddress.setId(addressinfo.getId());
            myAddress.setProvinces(addressinfo.getProvinces());
            myAddress.setStreet(addressinfo.getStreet());
            myAddress.setName(addressinfo.getName());
            myAddress.setPhone(addressinfo.getPhone());
            myAddress.setStatus(addressinfo.isStatus());
        }
    }
View.OnFocusChangeListener focusChanger = new View.OnFocusChangeListener() {

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub
        myAddress.setStreet(jiequText.getText().toString());
        myAddress.setName(nameText.getText().toString());
        myAddress.setPhone(phoneText.getText().toString());

        switch (v.getId()) {
            case R.id.my_set_buyaddress_jiequ:
                if (!hasFocus && myAddress.getStreet().length() > 0) {
                    jiequText.setVisibility(View.GONE);
                    jiequLinear.setVisibility(View.VISIBLE);

                    jiequTextView.setText(myAddress.getStreet());
                }

                if (hasFocus) {
                    jiequText.setSelectAllOnFocus(true);
                }
                break;
            case R.id.my_set_buyaddress_name:
                if (!hasFocus && myAddress.getName().length() > 0) {
                    nameText.setVisibility(View.GONE);
                    nameLinear.setVisibility(View.VISIBLE);

                    nameTextView.setText(myAddress.getName());
                }

                if (hasFocus) {
                    nameText.setSelectAllOnFocus(true);
                }
                break;
            case R.id.my_set_buyaddress_phone:
                if (!hasFocus && myAddress.getPhone().length() > 0) {
                    phoneText.setVisibility(View.GONE);
                    phoneLinear.setVisibility(View.VISIBLE);

                    phoneTextView.setText(myAddress.getPhone());
                }
                if (hasFocus) {
                    phoneText.setSelectAllOnFocus(true);
                }
                break;

            default:
                break;
        }
    }
};
    View.OnClickListener click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.my_set_buyaddress_sheng_linear:
                    Intent i = new Intent(BuyAddressActivity.this, AddressChooseActivity.class);
                    i.putExtra("Boolean", "aaa");
                    startActivity(i);
                    break;
                case R.id.my_set_buyaddress_jiequ_linear:
                    jiequText.setVisibility(View.VISIBLE);
                    jiequLinear.setVisibility(View.GONE);

                    jiequText.setFocusable(true);
                    jiequText.setFocusableInTouchMode(true);

                    jiequText.requestFocus();
                    break;
                case R.id.my_set_buyaddress_name_linear:
                    nameText.setVisibility(View.VISIBLE);
                    nameLinear.setVisibility(View.GONE);

                    nameText.setFocusable(true);
                    nameText.setFocusableInTouchMode(true);

                    nameText.requestFocus();
                    break;
                case R.id.my_set_buyaddress_phone_linear:
                    phoneText.setVisibility(View.VISIBLE);
                    phoneLinear.setVisibility(View.GONE);

                    phoneText.setFocusable(true);
                    phoneText.setFocusableInTouchMode(true);

                    phoneText.requestFocus();
                    break;
                case R.id.my_set_buyaddress_address_btn:
                    myAddress.setStreet(jiequText.getText().toString());
                    myAddress.setName(nameText.getText().toString());
                    myAddress.setPhone(phoneText.getText().toString());

                    if (myAddress.getPhone().length() > 0) {
                        phoneText.setVisibility(View.GONE);
                        phoneLinear.setVisibility(View.VISIBLE);
                        phoneTextView.setText(myAddress.getPhone());
                    }
                    postBtn.requestFocus();

                    postBtn.setFocusable(true);
                    postBtn.setFocusableInTouchMode(true);

                    if (myAddress.getProvinces().length() < 1 || myAddress.getStreet().length() < 1
                            || myAddress.getName().length() < 1 || myAddress.getPhone().length() < 1) {
                        Toast.makeText(getBaseContext(), "请完整填写收货人资料", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    myAddress.setStatus(checkBox.isChecked());
                    AddressDb addressDB = AddressDb.getInstance(getBaseContext());

                    if(checkBox.isChecked()){
                        List<AddressInfo> list = addressDB.queryAddress();
                        if(list!=null){
                            Iterator<AddressInfo> iterator = list.iterator();
                            while(iterator.hasNext()){
                                AddressInfo a = iterator.next();
                                a.setStatus(false);
                                addressDB.updeteAddress(a);
                            }
                        }

                    }


                    if (addressinfo != null) {
                        if(addressDB.updeteAddress(myAddress)){
                            Toast.makeText(getBaseContext(), "修改收货地址成功", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getBaseContext(), "修改收货地址失败", Toast.LENGTH_LONG).show();
                        }
                    } else {

                        SimpleDateFormat format = new SimpleDateFormat(
                                "yyyyMMddHHmmss");
                        Date date = new Date();
                        String id = format.format(date);
                        myAddress.setId(id);

                        if(addressDB.insertAddress(myAddress)){
                            Toast.makeText(getBaseContext(), "添加收货地址成功", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getBaseContext(), "添加收货地址失败", Toast.LENGTH_LONG).show();
                        }
                    }

                        Intent intent = new Intent(BuyAddressActivity.this, MyDeliveryActivity.class);
                    startActivity(intent);
                    finish();


                    break;


            }
        }
    };

    @Override
    protected String title() {
        return "添加收货地址";
    }
}

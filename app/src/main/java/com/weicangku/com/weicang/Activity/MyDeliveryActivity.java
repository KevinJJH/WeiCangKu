package com.weicangku.com.weicang.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weicangku.com.weicang.Activity.Base.ParentWithNaviActivity;
import com.weicangku.com.weicang.Bean.AddressInfo;
import com.weicangku.com.weicang.DB.AddressDb;
import com.weicangku.com.weicang.R;

import java.util.ArrayList;
import java.util.List;

public class MyDeliveryActivity extends ParentWithNaviActivity implements View.OnClickListener {
    private RelativeLayout dizhi;
    private ListView listView;
    private ImageView add_button;
    private List<AddressInfo> address=new ArrayList<>();
    private AddressDb addressDb;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydelivery);
        initNaviView();
    }

    @Override
    protected void initView() {
        super.initView();
        listView=(ListView) findViewById(R.id.myset_address_listView1);
        dizhi=(RelativeLayout) findViewById(R.id.dizhi);

        add_button=(ImageView) findViewById(R.id.pd_buy);

        addressDb=AddressDb.getInstance(getBaseContext());
        address=addressDb.queryAddress();
        if(address == null){
            dizhi.setVisibility(View.VISIBLE);
        }
        adapter=new MyAdapter(this);
        listView.setAdapter(adapter);

        add_button.setOnClickListener(this);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                // TODO Auto-generated method stub
                final AddressInfo a = address.get(position);
                AlertDialog.Builder dialog=new AlertDialog.Builder(MyDeliveryActivity.this)
                        .setTitle("删除收货地址")
                        .setMessage("确定删除这个收货地址吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                if(addressDb.deleteAddress(a)){
                                    ShowToast("删除成功");
                                }else {
                                    ShowToast("删除失败");
                                }
                                address.remove(position);
                                adapter.notifyDataSetChanged();
                                if(address.size() == 0){
                                    dizhi.setVisibility(View.VISIBLE);
                                }
                            }
                        }).setNegativeButton("取消", null);

                dialog.show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent i=new Intent(MyDeliveryActivity.this, BuyAddressActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("address", address.get(position));
                i.putExtra("address_id", b);
                startActivity(i);
            }
        });



    }

    @Override
    protected String title() {
        return "管理收货地址";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pd_buy:
                Intent intent=new Intent(this, AddressChooseActivity.class);
                startActivity(intent);
                finish();
                break;

            default:
                break;
        }
    }
    class MyAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        public MyAdapter(Context context){
            this.context=context;
            this.inflater=LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return address!=null?address.size():0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return address.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder viewHolder=null;
            if(convertView==null){
                convertView=inflater.inflate(R.layout.myset_adress_list_item, null);
                viewHolder=new ViewHolder();
                viewHolder.name=(TextView)convertView.findViewById(R.id.myset_address_item_name);
                viewHolder.listViewItem=(LinearLayout)convertView.findViewById(R.id.listview_item);
                viewHolder.provinces=(TextView)convertView.findViewById(R.id.myset_address_item_provinces);
                viewHolder.street=(TextView)convertView.findViewById(R.id.myset_address_item_street);
                viewHolder.phone=(TextView)convertView.findViewById(R.id.myset_address_phone);
                viewHolder.moren=(CheckBox)convertView.findViewById(R.id.loading_checkbox);
                convertView.setTag(viewHolder);
            }else{
                viewHolder=(ViewHolder)convertView.getTag();
            }

            viewHolder.moren.setClickable(false);
            viewHolder.name.setText(address.get(position).getName());
            viewHolder.provinces.setText(address.get(position).getProvinces());
            viewHolder.street.setText(address.get(position).getStreet());
            viewHolder.phone.setText(address.get(position).getPhone());
            viewHolder.listViewItem.setTag(address.get(position).getId());

            if(address.get(position).isStatus()){
                viewHolder.moren.setChecked(true);
            }else{
                viewHolder.moren.setChecked(false);
            }


            return convertView;
        }

        class ViewHolder{
            LinearLayout listViewItem;
            TextView name,provinces,street,phone;
            CheckBox moren;
        }
    }

}

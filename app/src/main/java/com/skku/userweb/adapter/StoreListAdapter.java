package com.skku.userweb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skku.userweb.R;
import com.skku.userweb.util.Store;

import java.util.ArrayList;

public class StoreListAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Store> store;

    public StoreListAdapter(Context context, ArrayList<Store> name){
        mContext = context;
        store = name;
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount(){
        return store.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Store getItem(int position) {
        return store.get(position);
    }
    @Override
    public View getView(int position, View converView, ViewGroup parent){
        View view = mLayoutInflater.inflate(R.layout.listview_store,null);

        ImageView imageView = view.findViewById(R.id.storeImage);
        TextView storeName = view.findViewById(R.id.storeName);
        TextView storeAddress = view.findViewById(R.id.storeAddress);
        TextView remainedSeat = view.findViewById(R.id.remainedSeat);

        imageView.setImageResource(store.get(position).getStoreImage());
        storeName.setText(store.get(position).getStoreName());
        storeAddress.setText(store.get(position).getStoreAddress());
        remainedSeat.setText(store.get(position).getRemainedSeat());
        return view;
    }

}

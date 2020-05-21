package com.skku.userweb.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skku.userweb.R;
import com.skku.userweb.util.Store;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.ViewHolder> {
    Context mContext = null;
    ArrayList<Store> items = new ArrayList<Store>();
    OnItemClickListener listener;
    public  static interface  OnItemClickListener{
        public void onItemClick(RecyclerView.ViewHolder holder, View view, int position);
    }

    public StoreListAdapter(Context context){
        mContext = context;
    }
    @Override //어댑터에서 관리하는 아이템의 개수를 반환
    public int getItemCount() {
        return items.size();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.listview_store,  viewGroup, false);
        return new ViewHolder(itemView); //각각의 아이템을 위한 뷰를 담고있는 뷰홀더객체를 반환한다.(각 아이템을 위한 XML 레이아웃을 이용해 뷰 객체를 만든 후 뷰홀더에 담아 반환
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Store item = items.get(position);
        viewHolder.setItem(item);
        viewHolder.setOnItemClickListener(listener);
    }
    //아이템을 한개 추가해주고싶을때
    public  void addItem(Store item){
        items.add(item);
    }

    public void addItems(ArrayList<Store> items){
        this.items = items;
    }
    public  Store getItem(int position){
        return  items.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView storeName;
        TextView storeAddress;
        TextView remainedSeat;
        OnItemClickListener listenr; //클릭이벤트처리관련 변수
        private Bitmap bitmap;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.storeImage);
            storeName = itemView.findViewById(R.id.storeName);
            storeAddress = itemView.findViewById(R.id.storeAddress);
            remainedSeat = itemView.findViewById(R.id.remainedSeat);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listenr != null ){
                        listenr.onItemClick(ViewHolder.this, itemView, position);
                    }
                }
            });
        }

        //setItem 메소드는 SingerItem 객체를 전달받아 뷰홀더 안에 있는 뷰에 데이터를 설정하는 역할을 합니다.
        public void setItem(Store item) {
            Thread mThread = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(item.getStoreImage());
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // 서버로 부터 응답 수신
                        conn.connect();
                        InputStream is = conn.getInputStream(); // InputStream 값 가져오기
                        bitmap = BitmapFactory.decodeStream(is); // Bitmap으로 변환
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            mThread.start(); // Thread 실행
            try {
                mThread.join();
                imageView.setImageBitmap(bitmap);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            storeName.setText(item.getStoreName());
            storeAddress.setText(item.getStoreAddress());
            remainedSeat.setText(item.getRemainedSeat());
        }
        //클릭이벤트처리
        public void setOnItemClickListener(OnItemClickListener listenr){
            this.listenr = listenr;
        }
    }
}






//    @Override
//    public int getCount(){
//        return store.size();
//    }
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public Store getItem(int position) {
//        return store.get(position);
//    }
//    @Override
//    public View getView(int position, View converView, ViewGroup parent){
//        View view = mLayoutInflater.inflate(R.layout.listview_store,null);
//
//        ImageView imageView = view.findViewById(R.id.storeImage);
//        TextView storeName = view.findViewById(R.id.storeName);
//        TextView storeAddress = view.findViewById(R.id.storeAddress);
//        TextView remainedSeat = view.findViewById(R.id.remainedSeat);
//
//        imageView.setImageResource(store.get(position).getStoreImage());
//        storeName.setText(store.get(position).getStoreName());
//        storeAddress.setText(store.get(position).getStoreAddress());
//        remainedSeat.setText(store.get(position).getRemainedSeat());
//        return view;
//    }
//
//}

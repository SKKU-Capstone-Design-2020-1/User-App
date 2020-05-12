package com.skku.userweb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skku.userweb.R;
import com.skku.userweb.util.Store;

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

    //한꺼번에 추가해주고싶을때
    public void addItems(ArrayList<Store> items){
        this.items = items;
    }


    public  Store getItem(int position){
        return  items.get(position);
    }

    //클릭리스너관련
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    //뷰홀더
    //뷰홀더 객체는 뷰를 담아두는 역할을 하면서 동시에 뷰에 표시될 데이터를 설정하는 역할을 맡을 수 있습니다.
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView storeName;
        TextView storeAddress;
        TextView remainedSeat;

        OnItemClickListener listenr; //클릭이벤트처리관련 변수

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.storeImage);
            storeName = itemView.findViewById(R.id.storeName);
            storeAddress = itemView.findViewById(R.id.storeAddress);
            remainedSeat = itemView.findViewById(R.id.remainedSeat);

            //아이템 클릭이벤트처리
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
        imageView.setImageResource(item.getStoreImage());
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

package com.example.android.elderlyapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.elderlyapp.core.Medicine;

import java.util.ArrayList;

/**
 * Created by Darsh_Shah on 23-06-2017.
 */

public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> implements RecyclerView.OnItemTouchListener{


//    public RecyclerViewAdapter(Fragment fragment) {
//        mFragment = fragment;
//    }

    private ClickListener clickListener;
    ArrayList<Medicine> arrayList=new ArrayList<>();
    public RecyclerAdapter(ArrayList arrayList){

        this.arrayList=arrayList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rawitem,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.id.setText(arrayList.get(position).getMedicine());
        holder.name.setText(arrayList.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    //--------------------------------------------------------------------------

    private GestureDetector gestureDetector;

    public RecyclerAdapter(Context context, final RecyclerView recyclerView, final ClickListener clickListener){
        this.clickListener=clickListener;
    gestureDetector=new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }


        @Override
        public void onLongPress(MotionEvent e) {
            View child=recyclerView.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clickListener!=null){
                clickListener.OnLongClick(child,recyclerView.getChildPosition(child));
            }
            //super.onLongPress(e);
        }
    });
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child=rv.findChildViewUnder(e.getX(),e.getY());

        if(child!=null && clickListener!=null && gestureDetector.onTouchEvent(e)){
            clickListener.OnClick(child,rv.getChildPosition(child));
        }
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
    //-----------------------------------------------------------------------------------


    public static interface  ClickListener{
        public void OnClick(View view,int position);
        public  void OnLongClick(View view,int position);
    }














    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView id,name;
        public MyViewHolder(View itemView) {
            super(itemView);
            id= (TextView) itemView.findViewById(R.id.Hotelid);
            name= (TextView) itemView.findViewById(R.id.HotelName);
            //name= (TextView) itemView.findViewById(R.id.Hotelname);

        }
    }
}

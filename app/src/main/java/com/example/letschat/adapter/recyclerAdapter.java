package com.example.letschat.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letschat.Activity.ChatActivity;
import com.example.letschat.R;
import com.example.letschat.model.ProfileModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.ViewHolder> {
    Context context;
    ArrayList<ProfileModel> profile_list;


public recyclerAdapter(){

}
    public recyclerAdapter(Context context,ArrayList<ProfileModel> profile_list){
        this.context=context;
        this.profile_list=profile_list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v= LayoutInflater.from(context).inflate(R.layout.recycler_contetn,parent,false);
     ViewHolder viewHolder=new ViewHolder(v);
     return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProfileModel profileModel=profile_list.get(position);


        holder.userName.setText(profileModel.getName());
        Picasso.get().load(profileModel.getImgUri()).into(holder.User_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("recyclerAdapter", "Item clicked: " + profileModel.getName());
                Intent i=new Intent(context,ChatActivity.class);
                i.putExtra("name",profileModel.getName());
                i.putExtra("image",profileModel.getImgUri());
                i.putExtra("uid",profileModel.getUid());
                i.putExtra("fcm",profileModel.getFcmToken());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return profile_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

     CircleImageView User_image;
     TextView userName;
     LinearLayout linearLayout;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        User_image=itemView.findViewById(R.id.list_profile);
        userName=itemView.findViewById(R.id.list_name);
        linearLayout=itemView.findViewById(R.id.linearLayout);
    }
}
}

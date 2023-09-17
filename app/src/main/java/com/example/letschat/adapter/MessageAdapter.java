package com.example.letschat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letschat.R;
import com.example.letschat.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter
{
   Context context;
   public MessageAdapter(){}

    public MessageAdapter(Context context, ArrayList<Message> msgAdapterArray) {
        this.context = context;
        this.msgAdapterArray = msgAdapterArray;
    }

    ArrayList<Message> msgAdapterArray;
 public final static   int ITEM_SEND=1;
   public final static int ITEM_RECEIVE=2;
    public final static int ITEM_SEND_IMG=3;
    public final static int ITEM_RECEIVE_IMG=4;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SEND){
            View view=LayoutInflater.from(context).inflate(R.layout.send_message,parent,false);
            return new senderViewHolder(view);

        }else if(viewType==ITEM_RECEIVE){
            View view=LayoutInflater.from(context).inflate(R.layout.recieve_msg,parent,false);
             return new receiverViewHolder(view);
        } else if (viewType==ITEM_SEND_IMG) {
            View view=LayoutInflater.from(context).inflate(R.layout.send_img,parent,false);
            return new Sender_view_Image(view);
        }else{
            View view=LayoutInflater.from(context).inflate(R.layout.receive_img,parent,false);
            return new receiver_view_image(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
     Message msgModell=msgAdapterArray.get(position);
     if(holder.getClass()==senderViewHolder.class){
         senderViewHolder viewHolder=(senderViewHolder) holder;
            viewHolder.send_Msg.setText(msgModell.getMessage());

     }else if(holder.getClass()==receiverViewHolder.class){
         receiverViewHolder viewHolder=(receiverViewHolder) holder;
         viewHolder.receive_Msg.setText(msgModell.getMessage());
     } else if (holder.getClass()==Sender_view_Image.class) {
         Sender_view_Image viewHolder=(Sender_view_Image) holder;
         viewHolder.send_image.setVisibility(View.VISIBLE);
         Picasso.get().load(msgModell.getImgURL()).into(viewHolder.send_image);
     } else if (holder.getClass()==receiver_view_image.class) {
         receiver_view_image viewHolder=(receiver_view_image) holder;
         viewHolder.receive_image.setVisibility(View.VISIBLE);
         Picasso.get().load(msgModell.getImgURL()).into(viewHolder.receive_image);
     }
    }

    @Override
    public int getItemCount() {
        return msgAdapterArray.size();
    }
    @Override
    public int getItemViewType(int position) {
        Message msgModel=msgAdapterArray.get(position);
        if(msgModel.getImgURL()!=null && !msgModel.getImgURL().isEmpty()){

            return msgModel.getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())?ITEM_SEND_IMG:ITEM_RECEIVE_IMG;
        }else{
            return msgModel.getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())?ITEM_SEND:ITEM_RECEIVE;
        }

    }



    static class senderViewHolder extends RecyclerView.ViewHolder {
        TextView send_Msg;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            send_Msg=itemView.findViewById(R.id.send__msg);
        }
    }

    static class receiverViewHolder extends RecyclerView.ViewHolder {
        TextView receive_Msg;

        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receive_Msg=itemView.findViewById(R.id.receive__msg);
        }
    }

    static  class Sender_view_Image extends  RecyclerView.ViewHolder {
        ImageView send_image;
        public Sender_view_Image(@NonNull View itemView) {
            super(itemView);
            send_image=itemView.findViewById(R.id.image_send);
        }
    }

    static class receiver_view_image extends RecyclerView.ViewHolder {
        ImageView receive_image;
        public receiver_view_image(@NonNull View itemView) {
            super(itemView);
            receive_image=itemView.findViewById(R.id.image_receive);
        }
    }

}

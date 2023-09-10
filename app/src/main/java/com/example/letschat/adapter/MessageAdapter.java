package com.example.letschat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letschat.R;
import com.example.letschat.model.Message;
import com.google.firebase.auth.FirebaseAuth;

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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SEND){
            View view=LayoutInflater.from(context).inflate(R.layout.send_message,parent,false);
            return new senderViewHolder(view);

        }else{
            View view=LayoutInflater.from(context).inflate(R.layout.recieve_msg,parent,false);
             return new receiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
     Message msgModell=msgAdapterArray.get(position);
     if(holder.getClass()==senderViewHolder.class){
         senderViewHolder viewHolder=(senderViewHolder) holder;
            viewHolder.send_Msg.setText(msgModell.getMessage());
     }else{
         receiverViewHolder viewHolder=(receiverViewHolder) holder;
         viewHolder.receive_Msg.setText(msgModell.getMessage());
     }
    }

    @Override
    public int getItemCount() {
        return msgAdapterArray.size();
    }
    @Override
    public int getItemViewType(int position) {
        Message msgModel=msgAdapterArray.get(position);
        if(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(msgModel.getSenderId())) {
            return ITEM_SEND;
        }else {
            return ITEM_RECEIVE;
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

}

package com.example.shareapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {
    private ArrayList<ShareModel> chatList;
    public ShareAdapter(ArrayList<ShareModel> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewGroup itemView = (ViewGroup) layoutInflater.inflate(R.layout.messagetile,parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch (chatList.get(position).getSender()) {
            case 1:
                holder.getTvText_right.setText(chatList.get(position).getMessage());
                holder.getTvText_right.setVisibility(View.VISIBLE);
                 break;
            case 2:
                holder.tvText_left.setText(chatList.get(position).getMessage());
                holder.tvText_left.setVisibility(View.VISIBLE);


            case 4:
                holder.tileImage_left.setImageURI(chatList.get(position).getImageView());
                holder.tileImage_left.setVisibility(View.VISIBLE);

                break;
        }



    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvText_left, getTvText_right;
        private ImageView tileImage_left;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tvText_left = itemView.findViewById(R.id.messageText_left);
            getTvText_right = itemView.findViewById(R.id.messageText_right);

            tileImage_left = itemView.findViewById(R.id.tileImage_left);
        }
    }
}

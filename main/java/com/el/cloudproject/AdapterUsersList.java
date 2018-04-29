package com.el.cloudproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Omar Sheikh on 4/29/2018.
 */
public class AdapterUsersList extends RecyclerView.Adapter<AdapterUsersList.MyViewHolder> {

        public ArrayList<ClassUser> usersList;
        public Context context;
        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView username;
            ImageView profilePicture;
            ImageButton deleteUser;
            public MyViewHolder(View view) {
                super(view);
                username = view.findViewById(R.id.adapterUserListUsername);
                profilePicture = view.findViewById(R.id.adapterUsersListProfilePic);
                deleteUser = view.findViewById(R.id.adapterUsersListRemoveUser);
            }

        }
        public AdapterUsersList(Context context,ArrayList<ClassUser> usersList) {
            this.context = context;
            this.usersList = usersList;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_userslist, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final ClassUser singleUser = usersList.get(position);
            holder.username.setText(singleUser.getUsername());
            Picasso.with(context).load(singleUser.getImageUrl()).fit().into(holder.profilePicture);
            holder.deleteUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Firebase rootURL = new Firebase(FirebaseConstants.rootURL);
                    rootURL.child(FirebaseConstants.userClassURL).child(singleUser.getUsername()).removeValue();
                    usersList.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return usersList.size();
        }
}

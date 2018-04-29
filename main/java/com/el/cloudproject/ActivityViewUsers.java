package com.el.cloudproject;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Omar Sheikh on 4/29/2018.
 */
public class ActivityViewUsers extends AppCompatActivity{

    ClassUser currentUser;
    ArrayList<ClassUser> usersList;
    RecyclerView recyclerView;
    AdapterUsersList adapterUsersList;

    Firebase rootURL;
    public void CreateUserBar(final Context context, String title){
        currentUser = HelperFunctions.GetCurrentUser(context);

        TextView usernameTV, logoutTV, activityTitle;
        CircleImageView userProfilePic;

        usernameTV = (TextView) findViewById(R.id.userBarUsername);
        logoutTV = (TextView) findViewById(R.id.userBarLogout);
        activityTitle = (TextView) findViewById(R.id.userBarTitle);
        userProfilePic = (CircleImageView) findViewById(R.id.userBarProfilePicture);

        usernameTV.setText(currentUser.getUsername());
        activityTitle.setText(title);
        Picasso.with(context).load(currentUser.getImageUrl()).resize(HelperFunctions.DpToPx(80, context),
                HelperFunctions.DpToPx(80, context)) // resizes the image to these dimensions (in pixel)
                .centerCrop().into(userProfilePic);

        logoutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperFunctions.UpdateCurrentUser(context,new ClassUser("","","",-1));
                Intent intent = new Intent(context,ActivityLoginSignup.class);
                startActivity(intent);
                finish();
            }
        });

    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewusers);
        CreateUserBar(ActivityViewUsers.this,"View Rooms");
        InitializeVariables();
        LoadDataFromFirebase();
    }

    public void InitializeVariables(){
        rootURL = new Firebase(FirebaseConstants.rootURL);
        usersList = new ArrayList<>();
    }
    public void DataLoaded(){
        recyclerView = (RecyclerView) findViewById(R.id.viewUsersRecyclerView);
        adapterUsersList = new AdapterUsersList(ActivityViewUsers.this,usersList);
        recyclerView.setAdapter(adapterUsersList);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(ActivityViewUsers.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
    }
    public void LoadDataFromFirebase(){
        rootURL.child(FirebaseConstants.userClassURL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String username = ds.getKey();
                    String password = ds.child(FirebaseConstants.userPasswordURL).getValue().toString();
                    String pic = ds.child(FirebaseConstants.userPictureURL).getValue().toString();
                    int type = Integer.valueOf(ds.child(FirebaseConstants.userTypeURL).getValue().toString());
                    usersList.add(new ClassUser(username,password,pic,type));
                }
                DataLoaded();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}

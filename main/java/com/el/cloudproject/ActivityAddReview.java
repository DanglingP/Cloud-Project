package com.el.cloudproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Omar Sheikh on 4/29/2018.
 */
public class ActivityAddReview extends AppCompatActivity {
    RatingBar rating;
    EditText review;
    Button addReview;
    String currentRoomNumber;
    Firebase rootURL;
    ClassUser currentUser;
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
    public void LoadDataFromIntent(){
        currentRoomNumber = getIntent().getStringExtra("RoomNumber");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addreview);
        CreateUserBar(ActivityAddReview.this,"Review");
        LoadDataFromIntent();
        InitializeVariables();
        SetListners();
    }

    public void InitializeVariables(){
        rootURL = new Firebase(FirebaseConstants.rootURL);
        addReview = (Button) findViewById(R.id.addReviewAddButton);
        rating = (RatingBar) findViewById(R.id.addReviewRating);
        review = (EditText) findViewById(R.id.addReviewReview);
    }
    public void SetListners(){
        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(review.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(),"Incomplete Review",Toast.LENGTH_SHORT).show();
                else{
                    rootURL.child(FirebaseConstants.reviewClassURL).child(currentRoomNumber).child(currentUser.getUsername()).child(FirebaseConstants.reviewRating).setValue((int)rating.getRating());
                    rootURL.child(FirebaseConstants.reviewClassURL).child(currentRoomNumber).child(currentUser.getUsername()).child(FirebaseConstants.reviewReview).setValue(review.getText().toString());
                    finish();
                }
            }
        });
    }
}

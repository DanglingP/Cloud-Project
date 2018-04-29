package com.el.cloudproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Omar Sheikh on 4/26/2018.
 */
public class ActivityUserHome extends AppCompatActivity {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);
        CreateUserBar(ActivityUserHome.this,"Home");
        Intent intent = new Intent(ActivityUserHome.this,ActivityViewRooms.class);
        startActivity(intent);
    }
}

package com.el.cloudproject;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Omar Sheikh on 4/26/2018.
 */
public class ActivityViewRooms  extends AppCompatActivity {

    private static final int numberOfTabs = 2;
    ViewPager viewPager;
    FragmentPagerAdapterClassRooms viewPagerAdapter;

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
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewrooms);
        CreateUserBar(ActivityViewRooms.this,"View Rooms");
        SetViewPager();
    }
    private void SetViewPager(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(numberOfTabs); // Keep the data of the fragments loaded
        viewPagerAdapter = new FragmentPagerAdapterClassRooms(getSupportFragmentManager(),getApplicationContext(), ActivityViewRooms.this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
        tabsStrip.setIndicatorColor(Color.parseColor("#FFFFFF"));
        tabsStrip.setIndicatorHeight(HelperFunctions.DpToPx(2,getApplicationContext()));
        tabsStrip.setBackgroundColor(Color.parseColor("#4c4d4d"));
        tabsStrip.setTextColor(Color.parseColor("#FFFFFF"));
        tabsStrip.setDividerColor(Color.TRANSPARENT);
    }
}
package com.el.cloudproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Omar Sheikh on 4/29/2018.
 */
public class ActivityUpateProfile extends AppCompatActivity{

    ClassUser currentUser;
    CircleImageView profilePicture;
    EditText password;
    TextView username, type;
    Button updateProfile;

    Bitmap profilePictureBitmap;
    Uri uploadProfilePictureURI, downloadProfilePictureURI;
    final static int PERMISSION_ALL = 1;
    public static final int GalleryRequestCodeID = 1;

    StorageReference mStorage;
    Firebase rootURL;

    ProgressDialog progressDialog;
    public void InitializeProgessDialog(){
        progressDialog = new ProgressDialog(ActivityUpateProfile.this);
        progressDialog.setMessage("Updating");
        progressDialog.setCancelable(false);
    }
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
    //Permissions
    public Boolean CheckPermissions(){
        int writeStorage = getApplicationContext().checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return writeStorage == PackageManager.PERMISSION_GRANTED;
    }
    public void GetPermissions(){
        String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(ActivityUpateProfile.this, PERMISSIONS, PERMISSION_ALL);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);
        CreateUserBar(ActivityUpateProfile.this,"Update Profile");
        InitializeVariables();
        InitializeVariables();
        LoadUser();
        SetListners();
    }
    public void InitializeVariables(){
        rootURL = new Firebase(FirebaseConstants.rootURL);
        mStorage = FirebaseStorage.getInstance().getReference();

        profilePicture= (CircleImageView) findViewById(R.id.updateProfileImageView);
        username = (TextView) findViewById(R.id.updateProfileUsername);
        password = (EditText) findViewById(R.id.updateProfilePassword);
        type = (TextView) findViewById(R.id.updateProfileUserType);
        updateProfile = (Button) findViewById(R.id.updateProfileUpdate);
    }
    public void LoadUser(){
        username.setText(currentUser.getUsername());
        if(currentUser.getType() == 1)
            type.setText("Type: Admin");
        else
            type.setText("Type: User");
        Picasso.with(ActivityUpateProfile.this).load(currentUser.getImageUrl()).resize(HelperFunctions.DpToPx(200, ActivityUpateProfile.this),
                HelperFunctions.DpToPx(200, ActivityUpateProfile.this)) // resizes the image to these dimensions (in pixel)
                .centerCrop().into(profilePicture);
    }
    public void SetListners(){
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if(!password.getText().toString().equals(""))
                    rootURL.child(FirebaseConstants.userClassURL).child(currentUser.getUsername()).child(FirebaseConstants.userPasswordURL).setValue(password.getText().toString());
                UploadImageOnFirebase();
            }
        });
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CheckPermissions())
                    GetPermissions();
                else
                    GetImageFromGallery();
            }
        });
    }
    public void GetImageFromGallery(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, GalleryRequestCodeID);
    }
    public void UploadImageOnFirebase(){
        if(uploadProfilePictureURI == null) {
            finish();
        }
        else {
            StorageReference filePath = mStorage.child(FirebaseConstants.profilePicturePath).child(uploadProfilePictureURI.getLastPathSegment());
            filePath.putFile(uploadProfilePictureURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadProfilePictureURI = taskSnapshot.getDownloadUrl();
                    rootURL.child(FirebaseConstants.userClassURL).child(currentUser.getUsername()).child(FirebaseConstants.userPictureURL).setValue(downloadProfilePictureURI.toString());
                    finish();

                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL: {
                if(CheckPermissions()){
                    GetImageFromGallery();
                }
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryRequestCodeID && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            try {
                uploadProfilePictureURI = data.getData();
                profilePictureBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),uri);
                profilePicture.setImageBitmap(profilePictureBitmap);
                profilePicture.setPadding(0,0,0,0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

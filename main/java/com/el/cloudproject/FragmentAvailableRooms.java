package com.el.cloudproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Omar Sheikh on 4/29/2018.
 */

@SuppressLint("ValidFragment")
public class FragmentAvailableRooms extends Fragment {
    View view;

    RecyclerView recyclerView;
    AdapterRoomsList adapterRoomsList;

    ArrayList<ClassRoom> roomsList;
    Firebase rootURL;
    int pictureLoadedCounter;

    TextView noAvailableRooms;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_availablerooms,null,false);
        InitializeVariables();
        LoadRoomsDataFromFirebase();
        return view;
    }

    public void InitializeVariables(){
        rootURL = new Firebase(FirebaseConstants.rootURL);
        roomsList = new ArrayList<>();
        pictureLoadedCounter = 0;

        noAvailableRooms = view.findViewById(R.id.availableRoomsNoRoomTV);
    }
    public void LoadRoomsDataFromFirebase(){
        rootURL.child(FirebaseConstants.roomClassURL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds != null) {
                            String roomNumber = ds.getKey();
                            String roomPrice = ds.child(FirebaseConstants.roomPrice).getValue().toString();
                            int f1 = Integer.valueOf(ds.child(FirebaseConstants.roomFeature1).getValue().toString());
                            int f2 = Integer.valueOf(ds.child(FirebaseConstants.roomFeature2).getValue().toString());
                            int f3 = Integer.valueOf(ds.child(FirebaseConstants.roomFeature3).getValue().toString());
                            int f4 = Integer.valueOf(ds.child(FirebaseConstants.roomFeature4).getValue().toString());
                            int f5 = Integer.valueOf(ds.child(FirebaseConstants.roomFeature5).getValue().toString());
                            int f6 = Integer.valueOf(ds.child(FirebaseConstants.roomFeature6).getValue().toString());
                            int availability = Integer.valueOf(ds.child(FirebaseConstants.roomAvailability).getValue().toString());
                            if(availability == 1)
                                roomsList.add(new ClassRoom(roomNumber,roomPrice,availability,f1,f2,f3,f4,f5,f6));
                        }
                    }
                }
                LoadRoomImagesFromFirebase();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public void LoadRoomImagesFromFirebase(){
        if(roomsList.size() == 0) {
            noAvailableRooms.setVisibility(View.VISIBLE);
            return;
        }
        Firebase roomsURL = rootURL.child(FirebaseConstants.roomClassURL);
        roomsURL.child(roomsList.get(pictureLoadedCounter).getRoomNumber()).child(FirebaseConstants.roomPicturesURL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds != null) {
                            roomsList.get(pictureLoadedCounter).addRoomPicture(ds.getValue().toString());
                        }
                    }
                }
                if(++pictureLoadedCounter < roomsList.size()){
                    LoadRoomImagesFromFirebase();
                }
                else
                    DataLoaded();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }
    public void DataLoaded(){
        recyclerView = view.findViewById(R.id.availableRoomsRecyclerView);
        adapterRoomsList = new AdapterRoomsList(getContext(),roomsList);
        recyclerView.setAdapter(adapterRoomsList);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

    }


}

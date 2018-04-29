package com.el.cloudproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Omar Sheikh on 4/29/2018.
 */

@SuppressLint("ValidFragment")
public class FragmentBookedRooms extends Fragment{
    View view;

    RecyclerView recyclerView;
    AdapterRoomsList adapterRoomsList;

    ArrayList<ClassRoom> roomsList;
    Firebase rootURL;
    int pictureLoadedCounter;
    ClassUser currentUser;
    TextView noBookedRoomsTV;
    int roomLoadedCounter = 0;

    ArrayList<String> bookedRoomsArrayList;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bookedrooms,null,false);
        InitializeVariables();
        LoadRoomsDataFromFirebase();
        return view;
    }

    public void InitializeVariables(){
        currentUser = HelperFunctions.GetCurrentUser(getContext());
        rootURL = new Firebase(FirebaseConstants.rootURL);
        roomsList = new ArrayList<>();
        pictureLoadedCounter = 0;

        noBookedRoomsTV = (TextView) view.findViewById(R.id.bookedRoomsNoRoomTV);

        bookedRoomsArrayList = new ArrayList<>();
    }
    public void LoadCompleteRoomsFromFirebase(String roomNumber){
        rootURL.child(FirebaseConstants.roomClassURL).child(roomNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                            String roomNumber = dataSnapshot.getKey();
                            String roomPrice = dataSnapshot.child(FirebaseConstants.roomPrice).getValue().toString();
                            int f1 = Integer.valueOf(dataSnapshot.child(FirebaseConstants.roomFeature1).getValue().toString());
                            int f2 = Integer.valueOf(dataSnapshot.child(FirebaseConstants.roomFeature2).getValue().toString());
                            int f3 = Integer.valueOf(dataSnapshot.child(FirebaseConstants.roomFeature3).getValue().toString());
                            int f4 = Integer.valueOf(dataSnapshot.child(FirebaseConstants.roomFeature4).getValue().toString());
                            int f5 = Integer.valueOf(dataSnapshot.child(FirebaseConstants.roomFeature5).getValue().toString());
                            int f6 = Integer.valueOf(dataSnapshot.child(FirebaseConstants.roomFeature6).getValue().toString());
                            int availability = Integer.valueOf(dataSnapshot.child(FirebaseConstants.roomAvailability).getValue().toString());
                        roomsList.add(new ClassRoom(roomNumber,roomPrice,availability,f1,f2,f3,f4,f5,f6));
                        }
                if(++roomLoadedCounter == bookedRoomsArrayList.size())
                    LoadRoomImagesFromFirebase();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public void LoadRoomsDataFromFirebase(){
        rootURL.child(FirebaseConstants.bookedClassURL).child(currentUser.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds != null) {
                            bookedRoomsArrayList.add(ds.getKey());
                        }
                    }
                    for(int i = 0 ; i < bookedRoomsArrayList.size(); i++){
                        LoadCompleteRoomsFromFirebase(bookedRoomsArrayList.get(i));
                    }
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public void LoadRoomImagesFromFirebase(){
        if(roomsList.size() == 0) {
            noBookedRoomsTV.setVisibility(View.VISIBLE);
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
        recyclerView = view.findViewById(R.id.bookedRoomsRecyclerView);
        adapterRoomsList = new AdapterRoomsList(getContext(),roomsList);
        recyclerView.setAdapter(adapterRoomsList);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);


    }


}

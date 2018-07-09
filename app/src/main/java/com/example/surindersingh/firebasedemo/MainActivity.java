package com.example.surindersingh.firebasedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

     Spinner spinnerGenre;
     EditText editTextName;
     Button buttonAdd;

     DatabaseReference databaseArtists;

     ListView listViewArtists;
     List<Artist> artistList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseArtists= FirebaseDatabase.getInstance().getReference("HomeRentals");

        editTextName = findViewById(R.id.editTextName);
        spinnerGenre = findViewById(R.id.spinnerGenre);
        buttonAdd = findViewById(R.id.buttonAddArtist);

        listViewArtists = findViewById(R.id.listViewArtits);
        artistList = new ArrayList<>();


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                addArtist();
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                artistList.clear();
                for(DataSnapshot artistSnapshot: dataSnapshot.getChildren())
                {

                    Artist artist = artistSnapshot.getValue(Artist.class);
                    artistList.add(artist);
                }

                    ArtistList adapter = new ArtistList(MainActivity.this,artistList);
                    listViewArtists.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addArtist()
    {


        String name = editTextName.getText().toString().trim();
        String genre = spinnerGenre.getSelectedItem().toString();

        if(!TextUtils.isEmpty(name)){


           String id = databaseArtists.push().getKey();
           Artist artist = new Artist(id,name,genre);

           databaseArtists.child(id).setValue(artist);

           Toast.makeText(this,"artist Added",Toast.LENGTH_LONG).show();

        }else{

            Toast.makeText(this,"You should enter a name", Toast.LENGTH_LONG).show();
        }

    }
}

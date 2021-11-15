package com.example.myloginapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class MainActivity2 extends AppCompatActivity {
    MediaPlayer mysong;
ImageView imageView;
Button button;
Button button2;

FirebaseStorage firebaseStorage ;
StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mysong=MediaPlayer.create(MainActivity2.this,R.raw.fire);
        imageView=(ImageView) findViewById(R.id.imageView2);
        button=(Button)findViewById(R.id.button);
        button2=(Button)findViewById(R.id.button2);

        ArrayList<Double>listCap=new ArrayList<>();
        List myArrayList = new ArrayList();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("capteur");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listCap.clear();
                for( DataSnapshot snapshot:dataSnapshot.getChildren() ){
                    listCap.add(Double.parseDouble(snapshot.getValue().toString()));

                }

                for(int i=0;i<listCap.size();i++){

                    System.out.println(listCap.get(i).getClass().getName());

                    if (listCap.get(i) > 20) {

                        mysong.start();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activite3();
            }


        });
        button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            downloadViaUrl();
        }
        });
    }

    private void Activite3() {

             mysong.stop();
            /*Intent intent = new Intent(this,MainActivity3.class);
            startActivity(intent);*/


    }

    public void downloadViaUrl() {

            StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("/");
            ArrayList<String> uris = new ArrayList<>();
            imageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            uris.add(item.getName().replace(".png",""));
                        }
                        List<Integer> listInteger = uris.stream().map(Integer::parseInt).collect(Collectors.toList());
                        System.out.println("-----------------------------");
                        System.out.println(uris);

                        Collections.sort(listInteger); //Sort the arraylist

                        System.out.println(listInteger.get(listInteger.size() - 1));
                        int h=listInteger.get(listInteger.size() - 1);
                        StorageReference imageRef1=FirebaseStorage.getInstance().getReference().child(h+".png");
                        imageRef1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(MainActivity2.this).load(uri).error(R.drawable.ic_launcher_background).into(imageView);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Toast.makeText(MainActivity2.this,"LOGIN FAILED !!!",Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(MainActivity2.this,"LOGIN FAILED !!!",Toast.LENGTH_SHORT).show();
                    }
                });

        }

    }




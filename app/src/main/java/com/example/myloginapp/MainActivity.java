package com.example.myloginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mysong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mysong=MediaPlayer.create(MainActivity.this,R.raw.fire);

        TextView username =(TextView) findViewById(R.id.username);
        TextView password =(TextView) findViewById(R.id.password);

        Button loginbtn = (Button) findViewById(R.id.loginbtn);

        ArrayList<Double> listCap=new ArrayList<>();
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


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("sse") && password.getText().toString().equals("sse")){
                    //correct
                    //Toast.makeText(MainActivity.this,"LOGIN SUCCESSFUL",Toast.LENGTH_SHORT).show();
                    app2();
                    mysong.stop();
                }else
                    //incorrect
                    Toast.makeText(MainActivity.this,"LOGIN FAILED !!!",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public  void app2(){
        Intent intent = new Intent(this,MainActivity2.class);
        startActivity(intent);
    }
}
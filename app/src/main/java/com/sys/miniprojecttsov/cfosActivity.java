package com.sys.miniprojecttsov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class cfosActivity extends AppCompatActivity {
Button btn;
EditText et;
TextView tv;
DatabaseReference reff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cfos);
        btn= findViewById(R.id.calc);
        et= findViewById(R.id.editText);
        tv= findViewById(R.id.textView);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(cfosActivity.this);
                 try{
                     String st="";
                     st= String.valueOf(et.getText());
                     reff= FirebaseDatabase.getInstance().getReference().child("Vehicles").child(st);
                     final String finalSt = st;
                     reff.addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                 String cp1=dataSnapshot.child("checkpt1").getValue().toString();
                                 String cp2=dataSnapshot.child("checkpt2").getValue().toString();
                                 int c1=Integer.parseInt(cp1);
                                 int c2=Integer.parseInt(cp2);
                                 int c3=c2-c1;
                                 String str="";
                                 if(c3>1){
                                     str="No overspeeding";
                                 }
                                 else{
                                     str="Crossed Speed Limit";
                                 }
                                 tv.setText("Distance=1m\nSpeed limit=1m/s\nTime =1s\n"+str);


                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError databaseError) {

                         }
                     });

                 }catch (Exception e){

                }
            }
        });
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    }


package com.sys.miniprojecttsov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class objdActivity extends AppCompatActivity {
SurfaceView cameraView;
TextView textView;
CameraSource cameraSource;
Vehicles vehicle;
Button but;
public String mes,tex;
final int  RequestCameraPermissionID=1001;
    DatabaseReference reff;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case RequestCameraPermissionID:
            {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objd);
        cameraView = findViewById(R.id.surface_view);
        textView = findViewById(R.id.text_view);
        Intent intent=getIntent();
        String message=intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView tv=findViewById(R.id.checkpoint);
        mes=message;
        reff= FirebaseDatabase.getInstance().getReference().child("Vehicles");
        vehicle= new Vehicles();
        but= findViewById(R.id.upload);
        tv.setText(message);
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if(!textRecognizer.isOperational()){
            Log.w("objdActivity","Detector Dependencies are not yet available");
        }
        else{
            cameraSource = new CameraSource.Builder(getApplicationContext(),textRecognizer).setFacing(CameraSource.CAMERA_FACING_BACK).setRequestedPreviewSize(1280,1024)
                    .setRequestedFps(2.0f).setAutoFocusEnabled(true).build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    try{
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(objdActivity.this,
                                    new String[]{Manifest.permission.CAMERA},RequestCameraPermissionID);
                            return;

                        } cameraSource.start(cameraView.getHolder());
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });
textRecognizer.setProcessor(new Detector.Processor<TextBlock>(){

    @Override
    public void release() {

    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {

final SparseArray<TextBlock> items = detections.getDetectedItems();
if(items.size()!=0){
    textView.post(new Runnable() {
        @Override
        public void run() {
            StringBuilder stringBuilder =new StringBuilder();
            for(int i=0;i<items.size();++i){
                TextBlock item=items.valueAt(i);
                        stringBuilder.append(item.getValue());
                        stringBuilder.append("\n");
            }
            tex=stringBuilder.toString();
            textView.setText(tex);

        }
    });
}
    }
});
        }
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mes.equals("Checkpoint 1")){
                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();
                String g=tex.substring(0,10);
                vehicle.setName(g);
               vehicle.setCheckpt1(ts);
               vehicle.setCheckpt2("0");
                reff.child(g).setValue(vehicle);
               Toast.makeText(objdActivity.this,"Value inserted successfully"+g+ts+mes,Toast.LENGTH_SHORT).show();
                    }
                else if(mes.equals("Checkpoint 2")){
                    Long tsLong = System.currentTimeMillis()/1000;
                    String ts = tsLong.toString();
                    String g=tex.substring(0,10);
                   reff.child(g).child("checkpt2").setValue(ts);
                   Toast.makeText(objdActivity.this,"Value inserted successfully"+ts+mes,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

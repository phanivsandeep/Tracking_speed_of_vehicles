package com.sys.miniprojecttsov;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "";
    RadioGroup rg;
RadioButton r1;
Button cap,cfos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rg= findViewById(R.id.rg);
       // r1=(RadioButton)findViewById(R.id.cp1);
       // r2=(RadioButton)findViewById(R.id.cp2);
        cap= findViewById(R.id.cap);
        cfos= findViewById(R.id.cfos);
        cap.setOnClickListener(new View.OnClickListener(){

                                   @Override
                                   public void onClick(View view) {
                                       int rid= rg.getCheckedRadioButtonId();
                                       r1=findViewById(rid);
                                       Toast.makeText(MainActivity.this,"Selected checkpoint is"+r1.getText(), LENGTH_SHORT).show();
                                       Intent intent= new Intent(MainActivity.this,objdActivity.class);
                                       String message= r1.getText().toString();
                                       intent.putExtra(EXTRA_MESSAGE,message);
                                       startActivity(intent);

                                   }
                               }
        );
        cfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,cfosActivity.class);
                startActivity(intent);
            }
        });
    }
}

package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    EditText Usuario;

    CheckBox Recordar;

    Button Iniciar;

    String User="User";

    SeekBar Seeker;

    int Puntuacion;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Usuario=findViewById(R.id.ETNombre);
        Recordar=findViewById(R.id.CBRecordar);
        Iniciar=findViewById(R.id.BTEntrar);
        Seeker=findViewById(R.id.seekBar);

        SharedPreferences preferences= getSharedPreferences(User,MODE_PRIVATE);

        Usuario.setText(preferences.getString("Nombre","").toString());
        Puntuacion= preferences.getInt("Puntos",0);
        if(preferences.getBoolean("Guardar",false)){
            Recordar.setChecked(true);
        }


        if(preferences.getBoolean("Musica",false)){
            Seeker.setProgress(Seeker.getMax());
        }


    }


    public void Iniciar(View v) {
        SharedPreferences preferences= getSharedPreferences(User, MODE_PRIVATE);
        if(Recordar.isChecked()){
            SharedPreferences.Editor myEditor=preferences.edit();

            myEditor.putString("Nombre",Usuario.getText().toString());
            myEditor.putBoolean("Guardar",true);
            if(Seeker.getProgress()==Seeker.getMax()){
                myEditor.putBoolean("Musica",true);
            }else{
                myEditor.putBoolean("Musica",false);
            }
            myEditor.commit();
        }else{
            SharedPreferences.Editor myEditor=preferences.edit();

            myEditor.putString("Nombre","");
            myEditor.putBoolean("Guardar",false);
            myEditor.putBoolean("Musica",false);
            myEditor.commit();
        }

        Intent ActividadJuego=new Intent(this,JuegoFacil.class);
        startActivity(ActividadJuego);



    }

}
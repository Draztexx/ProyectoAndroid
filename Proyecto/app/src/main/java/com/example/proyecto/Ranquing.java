package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Ranquing extends AppCompatActivity implements View.OnTouchListener {

    List<ListElement> elements;
    private Button boton;
    private boolean botonPresionado = false;
    private static final long TIEMPO_ESPERA = 5000;
    private CountDownTimer countDownTimer;

    private ValueAnimator colorAnimator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranquing);
        boton = findViewById(R.id.boton2);
        boton.setOnTouchListener(this);
        boton.setBackgroundColor(ContextCompat.getColor(this, R.color.AzulMarino));

        init();


    }

    public void init(){
        elements=new ArrayList<>();
        ConexionSqliteHelper con= new ConexionSqliteHelper(this,"misUsuarios",null,1);

        SQLiteDatabase db=con.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre,puntos FROM usuarios ORDER BY puntos DESC;",null);
        if (cursor.moveToFirst()){

            do {
                @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                @SuppressLint("Range") int puntos = cursor.getInt(cursor.getColumnIndex("puntos"));
                elements.add(new ListElement(nombre,puntos));
            } while (cursor.moveToNext());

        }

        ListAdapter listAdapter=new ListAdapter(elements,this);
        RecyclerView recyclerView=findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);

    }



    private void realizarAccion() {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                botonPresionado = true;
                animacionColor();
                countDownTimer = new CountDownTimer(TIEMPO_ESPERA, TIEMPO_ESPERA) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        realizarAccion();

                    }
                };
                countDownTimer.start();
                break;

            case MotionEvent.ACTION_UP:

                botonPresionado = false;
                boton.setBackgroundColor(ContextCompat.getColor(this, R.color.AzulMarino));
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                if (colorAnimator != null && colorAnimator.isRunning()) {
                    colorAnimator.cancel();
                }
                break;
        }
        return true;
    }

    private void animacionColor() {
        int colorInicial = ContextCompat.getColor(this, R.color.AzulMarino);
        int colorFinal = ContextCompat.getColor(this, R.color.colorAccent);


        colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorInicial, colorFinal);
        colorAnimator.setDuration(TIEMPO_ESPERA);
        colorAnimator.setInterpolator(new LinearInterpolator());
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                boton.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });

        colorAnimator.start();

    }
}
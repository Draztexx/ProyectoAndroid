package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JuegoFacil extends AppCompatActivity implements View.OnClickListener {

    ImageView[] imageViewArray= new ImageView[6];
    private int[] isShowingFront = {0,0,0,0,0,0};
    int[] imageResources;

    int puntos=0;

    int contad=0;

    int contadparejas=0;
    boolean Clicks=true;
    boolean Clicks2=true;
    Bitmap[] imagenes=new Bitmap[6];

    int[] posicionesUno = new int[2];

    int vidas=2;

    MediaPlayer player;

     TextView Vidascont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_facil);

        Vidascont=findViewById(R.id.Vidascont);

        player=MediaPlayer.create(this,R.raw.loveislongroad);

        SharedPreferences preferences= getSharedPreferences("User",MODE_PRIVATE);
        if(preferences.getBoolean("Musica",false)){
            player.start();
        }

        imageResources= new int[]{
                R.drawable.carta1,
                R.drawable.carta2,
                R.drawable.carta3,
                R.drawable.carta1,
                R.drawable.carta2,
                R.drawable.carta3,
        };



        List<Integer> imageList = new ArrayList<>();
        for (int resource : imageResources) {
            imageList.add(resource);
        }


        Collections.shuffle(imageList);


        imageResources = new int[6];
        for (int i = 0; i < imageList.size(); i++) {
            imageResources[i] = imageList.get(i);
        }

        for (int i = 0; i < imageViewArray.length; i++) {

            String imageViewIdName = "myImageView" + (i + 1);
            int imageViewId = getResources().getIdentifier(imageViewIdName, "id", getPackageName());
            imageViewArray[i] = findViewById(imageViewId);


            imageViewArray[i].setImageResource(R.drawable.back);

            imageViewArray[i].setOnClickListener(this);
        }






        int cropSize = 3750;

        for (int i = 0; i < imageViewArray.length; i++) {

            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), imageResources[i]);


            int startX = Math.max(0, (originalBitmap.getWidth() - cropSize) / 2);
            int startY = Math.max(0, (originalBitmap.getHeight() - cropSize) / 2);
            int width = Math.min(cropSize, originalBitmap.getWidth() - startX);
            int height = Math.min(cropSize, originalBitmap.getHeight() - startY);

            Bitmap croppedBitmap = Bitmap.createBitmap(originalBitmap, startX, startY, width, height);

            imagenes[i]=croppedBitmap;
        }





    }


    private void cambiaraimagen(final ImageView imageView) {
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(imageView, View.ROTATION_Y, 0f, 180f);
        animacionCarta(rotateAnimator, R.drawable.back,imageView);
    }

    private void cambiarabitmap(final ImageView imageView) {
        int viewId = imageView.getId();


        int index = obtenerIndiceDesdeId(viewId);
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(imageView, View.ROTATION_Y, 0f, 180f);
        animacionCarta(rotateAnimator, imagenes[index-1],imageView);
        if(contad==1){
            posicionesUno[0]=index-1;
        }else{
            posicionesUno[1]=index-1;
        }
    }

    private void animacionCarta(ObjectAnimator rotateAnimator, final int imageResource, final ImageView imageView) {
        rotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimator.setDuration(500);

        rotateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                int index = obtenerIndiceDesdeId(imageView.getId());
                isShowingFront[index-1] = 0;
                imageView.setImageResource(imageResource);
            }
        });

        rotateAnimator.start();
    }

    private void animacionCarta(ObjectAnimator rotateAnimator, final Bitmap bitmap, final ImageView imageView) {
        rotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimator.setDuration(500);

        rotateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                int index = obtenerIndiceDesdeId(imageView.getId());
                isShowingFront[index-1] = 1;

                imageView.setImageBitmap(bitmap);
            }
        });

        rotateAnimator.start();
    }

    private int obtenerIndiceDesdeId(int viewId) {

        String idString = getResources().getResourceEntryName(viewId);
        return Integer.parseInt(idString.substring(idString.length() - 1));
    }

    private boolean hay1(){
        int countfalse = 0;

        for (int value : isShowingFront) {
            if (value==1) {
                countfalse++;
                if (countfalse == 2) {
                    return true;
                }
            }
        }

        return false;
    }

    private void bloquearClicksTemporalmente(boolean a,long duracionMillis) {
        if(a) {
            Clicks = false;
        }else{
            Clicks2=false;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(a){
                    Clicks = true;
                }else{
                    Clicks2=true;
                }

            }
        }, duracionMillis);
    }



    private boolean mismos() {
        int[] lugar = posicionesUno;


        if(imageResources[lugar[0]]==imageResources[lugar[1]]) {
            isShowingFront[lugar[0]]=2;
            imageViewArray[lugar[0]].setEnabled(false);
            isShowingFront[lugar[1]]=2;
            imageViewArray[lugar[1]].setEnabled(false);
            contad=0;
            return true;
        }

        return false;
    }



    @Override
    public void onClick(View v) {

        if (Clicks2 && Clicks  && vidas>=-1) {
            ImageView imageView = (ImageView) v;

            if (isShowingFront[obtenerIndiceDesdeId(imageView.getId())-1]==0 && Clicks) {

                contad++;
                cambiarabitmap(imageView);
                bloquearClicksTemporalmente(true,1000);
            }

            if(contad==2){
                contad=0;
                bloquearClicksTemporalmente(false,2000);
                if(mismos()){
                    Toast.makeText(getApplicationContext(), "mismos", Toast.LENGTH_SHORT).show();
                    puntos+=10;
                    contadparejas++;
                    if(contadparejas==3){
                        avanzar();
                    }
                }else {
                    vidas--;

                    puntos-=5;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (int pos : posicionesUno) {
                                if(vidas!=-1) {
                                    Vidascont.setText(vidas + "");
                                }else{
                                    Toast.makeText(getApplicationContext(), "Perdiste", Toast.LENGTH_SHORT).show();
                                }
                                cambiaraimagen(imageViewArray[pos]);

                            }
                        }
                    }, 2000);


                    if(vidas==-1){

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                guardar();
                            }
                        }, 2000);

                    }


                }

            }


        }

    }

    public void guardar(){
        SharedPreferences preferences= getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor myEditor=preferences.edit();
        myEditor.putInt("Puntos",puntos);
        myEditor.commit();
        Intent Guardar=new Intent(this,Guardar.class);
        startActivity(Guardar);
    }

    public void avanzar(){
        SharedPreferences preferences= getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor myEditor=preferences.edit();
        myEditor.putInt("Puntos",puntos);
        myEditor.commit();
        Intent Avanzar=new Intent(this,ActividadDificil.class);
        Avanzar.putExtra("Vidas", vidas);
        startActivity(Avanzar);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (player != null && player.isPlaying()) {
            player.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE);
        if (preferences.getBoolean("Musica", false) && player != null) {
            player.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }

    @Override
    public void onBackPressed() {


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


        finish();
    }



}
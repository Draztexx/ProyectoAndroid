package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ImageView;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_facil);

        imageResources= new int[]{
                R.drawable.carta1,
                R.drawable.carta2,
                R.drawable.carta3,
                R.drawable.carta1,
                R.drawable.carta2,
                R.drawable.carta3,
        };


// Convierte el array en una lista para poder usar el método shuffle
        List<Integer> imageList = new ArrayList<>();
        for (int resource : imageResources) {
            imageList.add(resource);
        }

// Desordena la lista
        Collections.shuffle(imageList);

// Convierte la lista desordenada de nuevo a un array
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


    private void flipToReversedImage(final ImageView imageView) {
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(imageView, View.ROTATION_Y, 0f, 180f);
        performFlipAnimation(rotateAnimator, R.drawable.back,imageView);
    }

    private void flipToBitmap(final ImageView imageView) {
        int viewId = imageView.getId();

        // Aquí puedes utilizar el id para determinar el índice o cualquier otra lógica que necesites
        int index = obtenerIndiceDesdeId(viewId);
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(imageView, View.ROTATION_Y, 0f, 180f);
        performFlipAnimation(rotateAnimator, imagenes[index-1],imageView);
        if(contad==1){
            posicionesUno[0]=index-1;
        }else{
            posicionesUno[1]=index-1;
        }
    }

    private void performFlipAnimation(ObjectAnimator rotateAnimator, final int imageResource, final ImageView imageView) {
        rotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimator.setDuration(500);

        rotateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                // Actualizar el valor en el array de booleanos
                int index = obtenerIndiceDesdeId(imageView.getId());
                isShowingFront[index-1] = 0;
                imageView.setImageResource(imageResource);
            }
        });

        rotateAnimator.start();
    }

    private void performFlipAnimation(ObjectAnimator rotateAnimator, final Bitmap bitmap, final ImageView imageView) {
        rotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimator.setDuration(500);

        rotateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Actualizar el valor en el array de booleanos
                int index = obtenerIndiceDesdeId(imageView.getId());
                isShowingFront[index-1] = 1;

                imageView.setImageBitmap(bitmap);
            }
        });

        rotateAnimator.start();
    }

    private int obtenerIndiceDesdeId(int viewId) {
        // Implementa la lógica para obtener el índice asociado al id
        // Por ejemplo, podrías analizar el id para extraer información relevante.
        // Esto depende de cómo has definido los ids en tu aplicación.
        // Aquí hay un ejemplo simplificado:
        String idString = getResources().getResourceEntryName(viewId);
        return Integer.parseInt(idString.substring(idString.length() - 1));
    }

    private boolean hay1(){
        int countfalse = 0;

        for (int value : isShowingFront) {
            if (value==1) {
                countfalse++;
                if (countfalse == 2) {
                    return true; // Si hay dos '1', devuelve true
                }
            }
        }

        return false; // Si no hay dos '1', devuelve false
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
        Toast.makeText(getApplicationContext(), lugar[0]+" y "+ lugar[1], Toast.LENGTH_SHORT).show();

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

        if (Clicks2 && Clicks  && vidas>=0) {
            ImageView imageView = (ImageView) v;

            if (isShowingFront[obtenerIndiceDesdeId(imageView.getId())-1]==0 && Clicks) {
                // Realiza la animación para cambiar a la imagen del Bitmap
                contad++;
                flipToBitmap(imageView);
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
                        Toast.makeText(getApplicationContext(), "Puntos: "+puntos, Toast.LENGTH_SHORT).show();
                        SharedPreferences preferences= getSharedPreferences("User", MODE_PRIVATE);
                        SharedPreferences.Editor myEditor=preferences.edit();
                        myEditor.putInt("Puntos",puntos);
                        myEditor.commit();
                    }
                }else {
                    vidas--;
                    puntos-=5;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (int pos : posicionesUno) {
                                flipToReversedImage(imageViewArray[pos]);
                            }
                        }
                    }, 2000);

                }
            }
        }else{
            Toast.makeText(getApplicationContext(), "Puntos: "+puntos, Toast.LENGTH_SHORT).show();
            SharedPreferences preferences= getSharedPreferences("User", MODE_PRIVATE);
            SharedPreferences.Editor myEditor=preferences.edit();
            myEditor.putInt("Puntos",puntos);
            myEditor.commit();
        }

    }






}
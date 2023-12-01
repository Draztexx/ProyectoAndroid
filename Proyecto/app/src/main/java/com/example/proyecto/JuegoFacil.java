package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JuegoFacil extends AppCompatActivity implements View.OnClickListener {

    ImageView[] imageViewArray= new ImageView[6];

    int[] imageResources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_facil);

        this.imageResources= new int[]{
                R.drawable.carta1,
                R.drawable.carta2,
                R.drawable.carta3,
        };


        for (int i = 0; i < imageViewArray.length; i++) {
            String imageViewIdName = "myImageView" + (i + 1);

            int imageViewId = getResources().getIdentifier(imageViewIdName, "id", getPackageName());

            imageViewArray[i] = findViewById(imageViewId);

            imageViewArray[i].setOnClickListener(this);
        }


        // Duplicar o extender el array de recursos si es necesario
        List<Integer> resourceList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            for (int resource : imageResources) {
                resourceList.add(resource);
            }
        }
        Collections.shuffle(resourceList);

        int cropSize = 3750;

        for (int i = 0; i < imageViewArray.length; i++) {

            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), resourceList.get(i));


            int startX = (originalBitmap.getWidth() - cropSize) / 2;
            int startY = (originalBitmap.getHeight() - cropSize) / 2;


            Bitmap croppedBitmap = Bitmap.createBitmap(originalBitmap, startX, startY, cropSize, cropSize);


            imageViewArray[i].setImageBitmap(croppedBitmap);
        }





    }


    @Override
    public void onClick(View v) {
        if (v instanceof ImageView) {
            cambio((ImageView) v);
        }

    }

    private void cambio(ImageView imageView) {
        // Realizar la animación de rotación
        rotarimagen(imageView, 0f, 180f);
    }

    private void rotarimagen(final ImageView imageView, float startDegree, float endDegree) {
        int centerX = imageView.getWidth() / 2;
        int centerY = imageView.getHeight() / 2;

        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(imageView, View.ROTATION, startDegree, endDegree);
        rotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimator.setDuration(1000); // Duración de la animación en milisegundos
        rotateAnimator.setFloatValues(centerX, centerX);

        rotateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Puedes realizar alguna acción al finalizar la animación de rotación si lo deseas
            }
        });

        rotateAnimator.start();
    }






}
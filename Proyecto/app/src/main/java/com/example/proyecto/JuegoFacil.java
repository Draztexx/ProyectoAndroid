package com.example.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class JuegoFacil extends AppCompatActivity {

    ImageView[] imageViewArray= new ImageView[6];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_facil);

        for (int i = 0; i < imageViewArray.length; i++) {
            String imageViewIdName = "myImageView" + (i + 1);

            int imageViewId = getResources().getIdentifier(imageViewIdName, "id", getPackageName());

            imageViewArray[i] = findViewById(imageViewId);
        }






    }
}
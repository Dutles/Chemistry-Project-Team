package com.example.chemistryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Java objects to interact with layout widgets
    private ImageButton cameraButton, trashButton;
    private Button infoButton,deleteCancel, deleteConfirm,okay;
    private ImageView cameraResult;
    private Spinner spinner;
    private AlertDialog.Builder deleteAlert,infoAlert; // for the pop up in trash
    private AlertDialog dialog,infoDialog;
    private TextView prompt,prompt2;


    //Track the context of this Activity
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        //Set up the UI
        setupUI();
    }

    //Link class objects with corresponding xml objects and add click listeners
    private void setupUI() {
        //Populate the spinner
        spinner = findViewById(R.id.spnSamples);
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.samples_array)));

        //Find the cameraButton
        cameraButton = findViewById(R.id.btnCamera);
        //Add a click listener
        cameraButton.setOnClickListener(v -> {
            //Create an intent to start the image capture activity
            Intent i = new Intent();
            i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

            //Start the image capture activity with request code 1888
            startActivityForResult(i, 1888);
        });

        //Find the imageview to display the captured image
        cameraResult = findViewById(R.id.imgThumbnail);

        //Find the trash button
        trashButton = findViewById(R.id.btnTrash);

        //Trash button currently only removes image
        //TO-DO Trash button removes all stored info on sample
        trashButton.setOnClickListener(v -> {
            createNewDialog1();

           // cameraResult.setImageResource(0);
        });

        infoButton= (Button) findViewById(R.id.qButton);

        infoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                createNewDialog2();
            }
        });
    }

    //Recieve information from the camera capture request
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //If the reqest code matches camera capture request and our result is valid
        if (requestCode == 1888 && resultCode == Activity.RESULT_OK) {
            //Unpack the bitmap
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            //Upscale the bitmap and display on screen
            cameraResult.setImageBitmap(scaleBitmap(photo, cameraResult.getWidth(), cameraResult.getHeight()));
        }
    }

    //Scales up a bitmap image to the specified dimensions
    public static Bitmap scaleBitmap(Bitmap bitmapToScale, float newWidth, float newHeight) {
        if(bitmapToScale == null)
            return null;
        //get the original width and height
        int width = bitmapToScale.getWidth();
        int height = bitmapToScale.getHeight();
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(newWidth / width, newHeight / height);

        // recreate the new Bitmap and set it back
        return Bitmap.createBitmap(bitmapToScale, 0, 0, bitmapToScale.getWidth(), bitmapToScale.getHeight(), matrix, true);
    }

    public void createNewDialog1() {
        deleteAlert = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        deleteConfirm = (Button) contactPopupView.findViewById(R.id.delete);
        deleteCancel = (Button) contactPopupView.findViewById(R.id.cancel);


        deleteAlert.setView(contactPopupView);
        dialog = deleteAlert.create();
        dialog.show();

        deleteConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraResult.setImageResource(0); //delete rgb,concentration
                dialog.dismiss();
            }
        });

        deleteCancel.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        }));

    }

    public void createNewDialog2() {
        infoAlert = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup2, null);
        okay = (Button) contactPopupView.findViewById(R.id.okay);

        infoAlert.setView(contactPopupView);
        infoDialog = infoAlert.create();
        infoDialog.show();

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                infoDialog.dismiss();
            }
        });
    }

}
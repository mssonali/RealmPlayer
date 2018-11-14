package com.sonali.myrealmdatabase;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sonali.myrealmdatabase.model.Player;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

public class AddPlayerActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btnSave, btnCamera;
    private Realm realm;
    private EditText edtName, edtScore, edtYear;
    private static int playerId;
    private int REQUEST_CAMERA = 100, RESULT_CROP = 200;
    CircleImageView img;
    public static InputStream IN_BUSINESS_CARD;
    private File finalFile;
    String imageFilePath;
    private Uri resultUri;
    private byte[] byteArray;
    private RadioButton rMale, rFemale;
    private String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplayer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });*/

        getControls();


    }


    private void getControls() {
        edtName = findViewById(R.id.edtName);
        //edtScore = findViewById(R.id.edtScore);
        edtYear = findViewById(R.id.edtYear);

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        btnCamera = findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(this);
        img = findViewById(R.id.img);
        rMale = findViewById(R.id.rMale);
        rFemale = findViewById(R.id.rFemale);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //on selecting on item in menu
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                if (!isAllEmpty()) {
                    saveIntoDatabase();
                }
                break;
            case R.id.btnCamera:
                setPicture();
                break;
        }
    }

    private boolean isAllEmpty() {
        boolean isEmpty = false;
        if (edtName.getText().toString().equals("")) {
            edtName.setError("Empty field");
            isEmpty = true;
        }
        if (edtYear.getText().toString().equals("")) {
            edtYear.setError("Empty field");
            isEmpty = true;
        }
        if (!rFemale.isChecked() && !rMale.isChecked()) {
            isEmpty = true;
            Snackbar.make(btnSave, "Please select gender", Snackbar.LENGTH_LONG)
                    .setAction("Error", null).show();
        }
        return isEmpty;
    }

    private void saveIntoDatabase() {
        try {

            if (byteArray == null || byteArray.length == 0) {
                Drawable d = getResources().getDrawable(R.drawable.default_player); // the drawable (Captain Obvious, to the rescue!!!)
                Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byteArray = stream.toByteArray();
            }
            if (rFemale.isChecked()) {
                gender = "Female";
            } else if (rMale.isChecked()) {
                gender = "Male";
            }

            // Realm does not support primary key . So generated by unique UUID
            String playerId = getPlayerId();
            //Realm.init(this);
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            Player player = realm.createObject(Player.class);
            player.setpName(edtName.getText().toString().trim());
            player.setppGender(gender);
            player.setpYear(edtYear.getText().toString().trim());
            player.setId(playerId);

            //save into realm
            player.setpImag(byteArray);
            realm.commitTransaction();
            realm.close();

            Snackbar.make(btnSave, "Added", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            finish();
            //setResult(Activity.RESULT_OK);
            //Save Image
        } catch (Exception e) {
            Log.i("Realm Exception", e.toString());
        }
    }

    private String getPlayerId() {
        int id = (int) UUID.randomUUID().getMostSignificantBits();
        String sId = UUID.randomUUID().toString();
        return sId;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkCameraPermission() {
        final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
        //int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 124;
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(AddPlayerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(AddPlayerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddPlayerActivity.this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddPlayerActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            // ActivityCompat.requestPermissions(ScanToLeadActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions(AddPlayerActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void setPicture() {
        if (checkCameraPermission()) {
            //  cameraIntent();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //Store Image URI
                resultUri = result.getUri();
                storeImage();
                btnSave.setVisibility(View.VISIBLE);
                   /* InputStream inputStream = Constants.IN_BUSINESS_CARD;
                    String uuid = UUID.randomUUID().toString();

                    //finalFile = saveImage(uuid + ".jpg", inputStream);

                    String myFilePath = finalFile.getPath();
                    btnSave.setVisibility(View.VISIBLE);
                    // EasySP.init(AddCustomerActivity.this).putString("imagePath", myFilePath);*/


                //From here you can load the image however you need to, I recommend using the Glide library

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else if ((requestCode == 600) && (resultCode == Activity.RESULT_OK)) {
            Toast.makeText(this, "Image cropped successfully...", Toast.LENGTH_SHORT).show();

            InputStream inputStream = IN_BUSINESS_CARD;
            String uuid = UUID.randomUUID().toString();

            finalFile = saveImage(uuid + ".jpg", inputStream);
            //  finalFile = saveImage(uuid.replace('-', '_') + ".jpg", inputStream);

            // imageFilePath = finalFile.getPath();
            //  EasySP.init(AddCustomerActivity.this).putString("imagePath", myFilePath);
            //Toast.makeText(this, "" + myFilePath, Toast.LENGTH_SHORT).show();
            Glide.with(AddPlayerActivity.this).load(imageFilePath).diskCacheStrategy(DiskCacheStrategy.ALL).into(img);
        }
    }

    private File saveImage(String fileName, InputStream inputStream) {
//        File imageDir = new File(this.getFilesDir() + File.separator + "imagesx");
        File imageDir = new File(this.getFilesDir() + File.separator + "images");
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
        Log.d("sdf", "saveImage: " + imageDir + File.separator + fileName);
        File file = new File(imageDir + File.separator + fileName);
        return file;
    }

    private void storeImage() {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
            img.setImageBitmap(bitmap);
            finalFile = getOutputMediaFile();
            if (finalFile == null) {
                // Log.d(TAG, "Error creating media file, check storage permissions: ");// e.getMessage());
                return;
            }
            //FileOutputStream fos = new FileOutputStream(finalFile);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);

            byteArray = stream.toByteArray();
            //

            // fos.close();
        } catch (FileNotFoundException e) {
            // Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            //Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "MRD_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
}

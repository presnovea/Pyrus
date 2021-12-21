package com.pear.pearsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main activity. It use fragments to visualize logic
 */
public class FileChooserActivity extends AppCompatActivity {

    DefaultFragment defFragment;
    FilesFragment listFragment;
    FloatingActionButton addFileButton;
    DataWorker dw;

    private static final int REQUEST_CODE_PERMISSION = 1000;
    private static final int RESULT_CODE_FILECHOOSER = 2000;
    private static final String LOG = "PearSample";

    //---------- Methods --------------------------------------------------------------------------


    /**
     * Ask permission to read external storage
     */
    private void askPermissions()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_PERMISSION);
                return;
            }
        }
        browseFiles();
    }

    /**
     * create and start file chooser
     */
    private void browseFiles()
    {
        Intent chooseFilesIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFilesIntent.setType("*/*");
        // Only return URIs that can be opened with ContentResolver
        chooseFilesIntent.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFilesIntent = Intent.createChooser(chooseFilesIntent, "Choose file");
        startActivityForResult(chooseFilesIntent, RESULT_CODE_FILECHOOSER);
    }

    /**
     * update UI from default fragment to recycler vie fragment
     */
    private void updateUI()
    {
        FragmentManager fm = this.getSupportFragmentManager();
        if(DataWorker.isDirEmpty(getApplicationContext()))
            fm.beginTransaction()
                    .hide(listFragment)
                    .show(defFragment).commit();
        else {
            fm.beginTransaction()
                    .hide(defFragment)
                    .show(listFragment).commit();
        }
    }

    //---------- Overrides ------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chooser);

        Log.i(LOG, "Creating activity...");
        dw = new DataWorker(this);

        //File[] l = DataWorker.getFilesList(this.getApplicationContext());
        FragmentManager fm = this.getSupportFragmentManager();

        this.defFragment = (DefaultFragment) fm.findFragmentById(R.id.file_chooser_layout);
        this.listFragment = (FilesFragment) fm.findFragmentById(R.id.file_chooser_layout);

        if (this.defFragment == null){

            this.defFragment = new DefaultFragment();
            fm.beginTransaction()
                    .add(R.id.file_chooser_layout, defFragment)
                    .commit();
        }

        if(this.listFragment == null) {
            this.listFragment = new FilesFragment(dw);
            fm.beginTransaction()
                    .add(R.id.file_chooser_layout, listFragment)
                    .commit();
        }

        if(DataWorker.isDirEmpty(getApplicationContext()))
            fm.beginTransaction()
                    .hide(listFragment)
                    .show(defFragment).commit();
        else {
            fm.beginTransaction()
                    .hide(defFragment)
                    .show(listFragment).commit();
        }

        this.addFileButton = this.findViewById(R.id.addFileButton);
        this.addFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askPermissions();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Callback with result of permission request.
     * Start
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(LOG, "Permission granted!");

            //if granted, start to browsing file
            this.browseFiles();
        }
        else {
            Log.i(LOG, "Permission denied!");
        }

    }

    /**
     * Result of file browsing.
     * If File browsed, get it`s path and copy to local folder.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

                if(resultCode == Activity.RESULT_OK) {
                    if(data != null) {
                        Uri fileUri = data.getData();
                        String filePath;
                        try {
                            filePath = FileUtils.getPath(this,fileUri);
                            Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show();
                            dw.copyFileToFolder(filePath);
                        }
                        catch (Exception ex){
                            Log.e(LOG, "Error: " + ex.getMessage());
                        }
                    }
                }
        super.onActivityResult(requestCode, resultCode, data);
                updateUI();
    }



}
package com.nmamit.nmamitqp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    ListView listTv;
    List<String> array = new ArrayList<>();
    ArrayAdapter<String> arr;
    String[] strings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listTv = findViewById(R.id.listview1);
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();
        Log.d("tester","test");


        storageRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            array.add(prefix.getName());
                        }

                        for (StorageReference item : listResult.getItems()) {
                            array.add(item.getName());
                        }
                        strings = array.stream().toArray(String[]::new);



                        arr
                                = new ArrayAdapter<String>(
                                MainActivity.this,
                                R.layout.support_simple_spinner_dropdown_item,
                                strings);
                        listTv.setAdapter(arr);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
        listTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = strings[position];
                if (s.endsWith(".pdf")){
                            Intent intent = new Intent(MainActivity.this, viewAct.class);
                            intent.putExtra("branch", "ispdf");
                            intent.putExtra("sub", strings[position]);
                            startActivity(intent);
                }else {

                    Intent intent = new Intent(MainActivity.this, SemAct.class);
                    intent.putExtra("branch", strings[position]);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent i = new Intent(MainActivity.this,login.class);
            startActivity(i);
            finish();
        }
    }
}
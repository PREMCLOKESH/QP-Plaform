package com.nmamit.nmamitqp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class listAct extends AppCompatActivity {
    ListView listTv;
    ArrayAdapter<String> arr;
    String[] strings;
    String[] strings1;
    List<String> array = new ArrayList<>();
    List<String> array1 = new ArrayList<>();
    String value,value2,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listTv = findViewById(R.id.listview);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("branch");
            value2 = extras.getString("sem");
            type = extras.getString("type");

        }
        FirebaseStorage storage = FirebaseStorage.getInstance();



        StorageReference storageRef = storage.getReference(value).child(value2).child(type);

        storageRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                        }

                        for (StorageReference item : listResult.getItems()) {
                            array.add(item.getName().substring(0, item.getName().length() - 4));
                            array1.add(item.getName());

                        }
                        strings = array.stream().toArray(String[]::new);
                        strings1 = array1.stream().toArray(String[]::new);


                        arr
                                = new ArrayAdapter<String>(
                                listAct.this,
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
                Intent intent = new Intent(listAct.this,viewAct.class);
                intent.putExtra("sub",strings1[position]);
                intent.putExtra("type",type);
                intent.putExtra("sem",value2);
                intent.putExtra("branch", value);
                startActivity(intent);

            }
        });
    }
}
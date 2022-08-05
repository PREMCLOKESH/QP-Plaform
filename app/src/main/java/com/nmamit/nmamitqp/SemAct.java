package com.nmamit.nmamitqp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class SemAct extends AppCompatActivity {
    ListView listTv;
    List<String> array = new ArrayList<>();
    ArrayAdapter<String> arr;
    String[] strings;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sem);listTv = findViewById(R.id.listview2);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("branch");
        }
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference(value);


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
                                SemAct.this,
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


                                Intent intent = new Intent(SemAct.this, viewAct.class);
                                intent.putExtra("branch", value);
                                intent.putExtra("sem", "ispdf");
                                intent.putExtra("sub", strings[position]);
                                startActivity(intent);

                }else {
                    Intent intent = new Intent(SemAct.this, TypeAct.class);
                    intent.putExtra("sem", strings[position]);
                    intent.putExtra("branch", value);
                    startActivity(intent);
                }

            }
        });
    }
}
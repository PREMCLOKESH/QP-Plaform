package com.nmamit.nmamitqp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class TypeAct extends AppCompatActivity {
    ListView listTv;
    List<String> array = new ArrayList<>();
    ArrayAdapter<String> arr;
    String[] strings;
    String value,sem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        listTv = findViewById(R.id.listview3);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("branch");
            sem = extras.getString("sem");
        }
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference(value).child(sem);


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
                                TypeAct.this,
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
                    StorageReference storageReference = storage.getReference(value).child(sem).child(s);
                    Task<Uri> uri=storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Intent intent = new Intent(TypeAct.this, viewAct.class);
                            intent.putExtra("sem", sem);
                            intent.putExtra("branch", value);
                            intent.putExtra("type", "ispdf");
                            intent.putExtra("sub", strings[position]);
                            startActivity(intent);
                        }
                    });

                }else {
                    Intent intent = new Intent(TypeAct.this, listAct.class);
                    intent.putExtra("sem", sem);
                    intent.putExtra("branch", value);
                    intent.putExtra("type", strings[position]);
                    startActivity(intent);
                }

            }
        });
    }
}

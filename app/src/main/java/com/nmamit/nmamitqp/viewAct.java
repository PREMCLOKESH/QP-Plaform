package com.nmamit.nmamitqp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class viewAct extends AppCompatActivity {
    FirebaseStorage storage;
    StorageReference storageReference;
    String value,sem,type,sub;
    PDFView pdfView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Bundle extras = getIntent().getExtras();
        pdfView = findViewById(R.id.idPDFView);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        if (extras != null) {
            value = extras.getString("branch");
            sem = extras.getString("sem");
            type = extras.getString("type");
            sub = extras.getString("sub");

        }
        storage = FirebaseStorage.getInstance();
        if (value.endsWith("ispdf")){
            storageReference = storage.getReference(sub);
            Task<Uri> uri=storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    new RetrivePDFfromUrl().execute(uri.toString());

                }
            });
        }else if(sem.endsWith("ispdf")) {
            storageReference = storage.getReference(value).child(sub);
            Task<Uri> uri = storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    new RetrivePDFfromUrl().execute(uri.toString());

                }
            });
        }else if(type.endsWith("ispdf")){
            storageReference = storage.getReference(value).child(sem).child(sub);
            Task<Uri> uri = storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    new RetrivePDFfromUrl().execute(uri.toString());

                }
            });
        }else
         {
            storageReference = storage.getReference(value).child(sem).child(type).child(sub);
            Task<Uri> uri = storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    new RetrivePDFfromUrl().execute(uri.toString());

                }
            });
        }
    }
    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            pdfView.fromStream(inputStream).load();
        }
    }
}


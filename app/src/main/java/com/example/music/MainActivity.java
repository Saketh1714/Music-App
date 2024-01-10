package com.example.music;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listView);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                        Toast.makeText(MainActivity.this, "Runtime opening", Toast.LENGTH_SHORT).show();
                        ArrayList<File> mysongs=fetchsongs(Environment.getExternalStorageDirectory());
                        String[] items=new String[mysongs.size()];
                        for(int i=0;i<mysongs.size();i++)
                        {
                            items[i]=mysongs.get(i).getName().replace(".mp3","");

                        }
                        ArrayAdapter<String> ad=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,items);
                        listView.setAdapter(ad);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent=new Intent(MainActivity.this,PlaySong.class);
                                String currentsong=listView.getItemAtPosition(position).toString();
                                intent.putExtra("songlist",mysongs);
                                intent.putExtra("currentsong",currentsong);
                                intent.putExtra("position",position);
                                startActivity(intent);

                            }
                        });


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                })
                .check();
    }
    ArrayList<File> fetchsongs(File file)
    {
        ArrayList arraylist=new ArrayList();
        File[] songs=file.listFiles();
        if(songs!=null)
        {
            for(File myfile:songs)
            {
                if(!myfile.isHidden() && myfile.isDirectory())
                {
                    arraylist.addAll(fetchsongs(myfile));
                }
                else{
                    if(myfile.getName().endsWith(".mp3") && !myfile.getName().startsWith("."))
                    {
                        arraylist.add(myfile);
                    }
                }
            }
        }

      return arraylist;
    }

}
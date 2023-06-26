package com.example.sendquotes;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.sendquotes.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    View root;


    ArrayList<String> contact_numbers ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        root = binding.getRoot();
        setContentView(root);

        if (!grantedPermission()){
            if(shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) || shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS) ){
                new AlertDialog.Builder(this).setTitle("Request for Permission").setMessage("the app need somme permission !\n do you want to grant theme ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermission.launch(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS});
                    }
                }).setNegativeButton("No", null).show();
            }else{
                requestPermission.launch(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS});
            }
        }else{
            binding.btnSend.setEnabled(true);
            loadContacts();
        }
        binding.spinnerContacts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, contact_numbers.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void loadContacts() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getContacts());
        binding.spinnerContacts.setAdapter(adapter);
    }

    private ArrayList<String> getContacts() {
        ArrayList<String> contact_names = new ArrayList<>();
        contact_numbers = new ArrayList<>();
        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
        ContentResolver cv = getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = cv.query(uri, null, null, null, null);
        Toast.makeText(this, cursor.getCount()+" Contacts", Toast.LENGTH_LONG).show();
        while (cursor.moveToNext()){
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String numberPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contact_names.add(contactName+"     "+numberPhone);
            contact_numbers.add(numberPhone);
        }
        return  contact_names;
    }

    private boolean grantedPermission() {
        return checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED  ;
    }

    private final ActivityResultLauncher<String[]> requestPermission = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
            boolean allGranted = true ;
            for (String key: result.keySet()) {
                if (!result.get(key)) {
                    allGranted = false;
                    break;
                }
            }
            if(allGranted){
                binding.btnSend.setEnabled(true);
                loadContacts();
            }else{
                if(!shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) || !shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)){
                    new AlertDialog.Builder(MainActivity.this).setTitle("Request for Permission").setMessage("The app want work Properly because you don't granted somme permissions !\nYou can always go to Settings to Grant Theme").show();
                }else{
                    new AlertDialog.Builder(MainActivity.this).setTitle("Request for Permission").setMessage("The app want work Properly because you don't granted somme permissions !").show();
                }
            }
        }
    });
}
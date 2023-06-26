package com.example.sendquotes;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.sendquotes.databinding.ActivityMainBinding;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    View root;

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
        }

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
            Toast.makeText(MainActivity.this, "all granted "+allGranted, Toast.LENGTH_SHORT).show();
            if(allGranted){
                binding.btnSend.setEnabled(true);
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
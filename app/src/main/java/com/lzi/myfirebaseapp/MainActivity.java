package com.lzi.myfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editTextName = findViewById(R.id.edt_name);
        EditText editTextEmail = findViewById(R.id.edt_email);
        Button buttonSave = findViewById(R.id.btn_save);
        Button buttonGetNames = findViewById(   R.id.btn_allNames);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        buttonSave.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String key = dbRef.push().getKey();
            if (validateForm(name,email)){
                writeNewUser(key,name,email);
                Toast.makeText(MainActivity.this,"User saved",Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(MainActivity.this,"User not saved",Toast.LENGTH_SHORT).show();
        });

        //Read from  the database
        buttonGetNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //String vName = snapshot.child("name").getValue(String.class);
                        Toast.makeText(MainActivity.this,""+snapshot.getValue(),Toast.LENGTH_LONG).show();
                        Log.d(TAG,"value: "+snapshot.getValue());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w(TAG,"Failed to read value",error.toException());
                    }
                });

            }
        });

    }

    private boolean validateForm(String _param,String __param) {
        if (__param.isEmpty() || _param.isEmpty())
            return false;
        return true;
    }

    private void writeNewUser(String userId, String username, String email){
        User user = new User(username,email);
        dbRef.child("users").child(userId).setValue(user);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this).inflate(R.layout.add,null);
            builder.setView(view);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            return true;

        }else
            return super.onOptionsItemSelected(item);
    }
}
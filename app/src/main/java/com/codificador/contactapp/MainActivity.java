package com.codificador.contactapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,NewContactActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        initViews();
    }

    private void initViews(){
        listView = findViewById(R.id.listView);
        adapter = new ContactAdapter(MainActivity.this,new ArrayList<Contact>());
        listView.setAdapter(adapter);
        adapter.addContact(new Contact("Sathish","123"));
        adapter.addContact(new Contact("Kumar","12345"));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                Contact contact = (Contact) data.getSerializableExtra("contact");
                adapter.addContact(contact);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "New contacts added successfully.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

package com.codificador.contactapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ContactAdapter adapter;
    DatabaseHelper databaseHelper;

    static final int NEW_CONTACT_REQUEST_CODE = 1;
    static final int EDIT_CONTACT_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews(){
        listView = findViewById(R.id.listView);

        databaseHelper = new DatabaseHelper(MainActivity.this);
        adapter = new ContactAdapter(MainActivity.this,databaseHelper.getAllContacts());
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                viewContact((Contact) adapter.getItem(i));
                selectedPos = i;
            }
        });
        registerForContextMenu(listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionAdd:
                Intent intent = new Intent(MainActivity.this,NewContactActivity.class);
                startActivityForResult(intent, NEW_CONTACT_REQUEST_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void viewContact(Contact contact){
        Intent viewContactIntent = new Intent(MainActivity.this,ViewContactActivity.class);
        viewContactIntent.putExtra("id",contact.getId());
        viewContactIntent.putExtra("name",contact.getName());
        viewContactIntent.putExtra("number",contact.getNumber());
        startActivity(viewContactIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NEW_CONTACT_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Contact contact = (Contact) data.getSerializableExtra("contact");
                long insertedId = databaseHelper.insertContact(contact);
                contact.setId(insertedId);
                adapter.addContact(contact);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "New contacts added successfully.", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == EDIT_CONTACT_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Contact contact = (Contact) data.getSerializableExtra("contact");
                int rows = databaseHelper.update(contact);
                if(rows > 0){
                    adapter.updateContact(contact,selectedPos);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Contact updated successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.option_menu,menu);
        menu.setHeaderTitle("Options");
    }

    int selectedPos = -1;
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        selectedPos = menuInfo.position;
        switch (item.getItemId()){
            case R.id.actionCall:
                call((Contact) adapter.getItem(selectedPos));
                break;
            case R.id.actionEdit:
                edit((Contact) adapter.getItem(selectedPos));
                break;
            case R.id.actionDelete:
                delete((Contact) adapter.getItem(selectedPos),selectedPos);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void call(Contact contact){
        if(checkPermission()){
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+contact.getNumber()));
            startActivity(callIntent);
        }
    }

    private void edit(Contact contact){
        Intent intent = new Intent(MainActivity.this,EditContactActivity.class);
        intent.putExtra("contact",contact);
        startActivityForResult(intent, EDIT_CONTACT_REQUEST_CODE);
    }

    private void delete(Contact contact,int position){
        int rows = databaseHelper.delete(contact.getId());
        if(rows > 0){
            adapter.removeContact(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Contact successfully deleted", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission(){
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},123);
            return false;
        }
        return true;
    }
}
package com.codificador.contactapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditContactActivity extends AppCompatActivity {

    EditText editTextName, editTextNumber;
    Button buttonUpdate;
    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent() == null || getIntent().getSerializableExtra("contact") == null){
            finish();
        }
        contact = (Contact) getIntent().getSerializableExtra("contact");
        initViews();
    }

    private void initViews(){
        editTextName = findViewById(R.id.editTextName);
        editTextNumber = findViewById(R.id.editTextNumber);

        editTextName.setText(contact.getName());
        editTextNumber.setText(contact.getNumber());

        buttonUpdate= findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                String number = editTextNumber.getText().toString();
                //don't update contact id, because just we are updating the name & number
                contact.setName(name);
                contact.setNumber(number);
                Intent intent = new Intent();
                intent.putExtra("contact",contact);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
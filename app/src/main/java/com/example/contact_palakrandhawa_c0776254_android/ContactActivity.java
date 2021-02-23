package com.example.contact_palakrandhawa_c0776254_android;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.contact_palakrandhawa_c0776254_android.model.Contact;
import com.example.contact_palakrandhawa_c0776254_android.util.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity
{
    // instance of DatabaseOpenHelper class
    DatabaseHelper sqLiteDatabase;

    List<Contact> contactList;
    ListView contactListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        contactListView = findViewById(R.id.lv_employees);
        contactList = new ArrayList<>();

        sqLiteDatabase = new DatabaseHelper(this);
        loadEmployees();
    }

    private void loadEmployees()
    {
       Cursor cursor = sqLiteDatabase.getAllEmployees();

        if (cursor.moveToFirst())
        {
            do {
                // create an employee instance
                contactList.add(new Contact(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getDouble(5)
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }

        // create an adapter to display the employees
        ContactAdapter contactAdapter = new ContactAdapter(this,
                R.layout.list_layout_contact,
                contactList,
                sqLiteDatabase);
        contactListView.setAdapter(contactAdapter);
    }
}
package com.example.contact_palakrandhawa_c0776254_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.contact_palakrandhawa_c0776254_android.util.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener
{
       // sqLite openHelper instance
        DatabaseHelper sqLiteDatabase;

        EditText etFirstName,etLastName, etEmail, etAddress, etPhoneNumber;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            etFirstName = findViewById(R.id.et_first_name);
            etLastName = findViewById(R.id.et_last_name);
            etEmail = findViewById(R.id.et_email);
            etAddress = findViewById(R.id.et_address);
            etPhoneNumber = findViewById(R.id.et_phone_number);

            findViewById(R.id.btn_add_contact).setOnClickListener(this);
            findViewById(R.id.tv_view_contacts).setOnClickListener(this);

            // initializing the instance of sqLLite openHelper class
            sqLiteDatabase = new DatabaseHelper(this);
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.btn_add_contact:
                    addEmployee();
                    break;

                case R.id.tv_view_contacts:
                    startActivity(new Intent(this, ContactActivity.class));
                    break;
            }
        }

        private void addEmployee()
        {
            String firstname = etFirstName.getText().toString().trim();
            String lastname = etLastName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();

            if (firstname.isEmpty())
            {
                etFirstName.setError("name field cannot be empty");
                etFirstName.requestFocus();
                return;
            }
            if (lastname.isEmpty())
            {
                etLastName.setError("name field cannot be empty");
                etLastName.requestFocus();
                return;
            }

            if (email.isEmpty()) {
                etEmail.setError("salary cannot be empty");
                etEmail.requestFocus();
                return;
            }
            if (address.isEmpty()) {
                etAddress.setError("salary cannot be empty");
                etAddress.requestFocus();
                return;
            }
            if (address.isEmpty()) {
                etPhoneNumber.setError("salary cannot be empty");
                etPhoneNumber.requestFocus();
                return;
            }


            // insert employee into database table with the help of database openHelper class
            if (sqLiteDatabase.addEmployee(firstname, lastname, email, address, Double.valueOf(phoneNumber)))
                Toast.makeText(this, "Employee Added", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Employee NOT Added", Toast.LENGTH_SHORT).show();


        }

        @Override
        protected void onRestart()
        {
            super.onRestart();
            etFirstName.setText("");
            etLastName.setText("");
            etEmail.setText("");
            etAddress.setText("");
            etPhoneNumber.setText("");
            etPhoneNumber.clearFocus();
            etFirstName.requestFocus();
        }
    }
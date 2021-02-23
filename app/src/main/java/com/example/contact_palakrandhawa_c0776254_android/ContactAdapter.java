package com.example.contact_palakrandhawa_c0776254_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.contact_palakrandhawa_c0776254_android.model.Contact;
import com.example.contact_palakrandhawa_c0776254_android.util.DatabaseHelper;


import java.util.List;

public class ContactAdapter extends ArrayAdapter
{
        Context context;
        int layoutRes;
        List<Contact> contactList;
        DatabaseHelper sqLiteDatabase;

        public ContactAdapter(@NonNull Context context, int resource, List<Contact> contactList, DatabaseHelper sqLiteDatabase) {
            super(context, resource, contactList);
            this.contactList = contactList;
            this.sqLiteDatabase = sqLiteDatabase;
            this.context = context;
            this.layoutRes = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(layoutRes, null);
            TextView first_nameTV = v.findViewById(R.id.tv_first_name);
            TextView last_nameTV = v.findViewById(R.id.tv_last_name);
            TextView emailTV = v.findViewById(R.id.tv_email);
            TextView addressTV = v.findViewById(R.id.tv_address);
            TextView phoneNumberTV = v.findViewById(R.id.tv_phone_number);


            final Contact contact = contactList.get(position);
            first_nameTV.setText(contact.getFirstName());
            last_nameTV.setText(contact.getLastName());
            emailTV.setText(contact.getEmail());
            addressTV.setText(contact.getAddress());
            phoneNumberTV.setText(String.valueOf(contact.getPhone_number()));


            v.findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    updateEmployee(contact);
                }

                private void updateEmployee(final Contact contact) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                    View view = layoutInflater.inflate(R.layout.dialog_update_contact, null);
                    builder.setView(view);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    final EditText etFirstName = view.findViewById(R.id.et_first_name);
                    final EditText etLastName = view.findViewById(R.id.et_last_name);
                    final EditText etEmail= view.findViewById(R.id.et_email);
                    final EditText etAddress= view.findViewById(R.id.et_address);
                    final EditText etPhoneNumber= view.findViewById(R.id.et_phone_number);
                    //final Spinner spinnerDept = view.findViewById(R.id.spinner_dept);

                   // String[] deptArray = context.getResources().getStringArray(R.array.departments);
                   // int position = Arrays.asList(deptArray).indexOf(contact.getDepartment());

                    etFirstName.setText(contact.getFirstName());
                    etLastName.setText(contact.getLastName());
                    etEmail.setText(contact.getEmail());
                    etAddress.setText(contact.getAddress());
                    etPhoneNumber.setText(String.valueOf(contact.getPhone_number()));
                   // spinnerDept.setSelection(position);

                    view.findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            String firstname = etFirstName.getText().toString().trim();
                            String lastname = etLastName.getText().toString().trim();
                            String email = etEmail.getText().toString().trim();
                            String address = etAddress.getText().toString().trim();
                            String phone_number = etPhoneNumber.getText().toString().trim();
                            //String department = spinnerDept.getSelectedItem().toString();

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
                            if (phone_number.isEmpty()) {
                                etPhoneNumber.setError("salary cannot be empty");
                                etPhoneNumber.requestFocus();
                                return;
                            }

                            if (sqLiteDatabase.updateEmployee(contact.getId(), firstname, lastname, email, address,  Double.parseDouble(phone_number)))
                                loadEmployees();
                            alertDialog.dismiss();
                        }
                    });
                }
            });

            v.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    deleteEmployee(contact);
                }

                private void deleteEmployee(final Contact contact)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Are you sure?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        /*String sql = "DELETE FROM employee WHERE id = ?";
                        sqLiteDatabase.execSQL(sql, new Integer[]{employee.getId()});*/
                            if (sqLiteDatabase.deleteEmployee(contact.getId()))
                                loadEmployees();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "The employee (" + contact.getFirstName() + contact.getLastName() + ") is not deleted", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
            return v;
        }

        private void loadEmployees()
        {
            Cursor cursor = sqLiteDatabase.getAllEmployees();
            contactList.clear();
            if (cursor.moveToFirst()) {
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
            notifyDataSetChanged();
        }
    }

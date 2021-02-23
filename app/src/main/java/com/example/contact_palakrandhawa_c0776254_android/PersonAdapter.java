package com.example.contact_palakrandhawa_c0776254_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact_palakrandhawa_c0776254_android.R;
import com.example.contact_palakrandhawa_c0776254_android.MainActivity;
import com.example.contact_palakrandhawa_c0776254_android.Person;
import com.example.contact_palakrandhawa_c0776254_android.PersonRoomDB;

import java.util.ArrayList;
import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> implements Filterable {

    private static final String TAG = "PersonAdapter";
    Context context;
    int layoutRes;
    List<Person> personList;
    List<Person> personNewList;

    PersonRoomDB personRoomDB;
    MainActivity mainActivity;


    public PersonAdapter(Context context, int resource, List<Person> personList) {

        this.personList = personList;
        this.layoutRes = resource;
        this.context = context;
        this.personNewList = personList;

        personRoomDB = personRoomDB.getINSTANCE(context);

        personNewList = new ArrayList<>(personList);
    }


    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutRes, null);

//        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person,
//                parent,
//                false);

        PersonViewHolder personViewHolder = new PersonAdapter.PersonViewHolder(view);

        return personViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {

        final Person person = personList.get(position);
        holder.firstNameTV.setText(person.getFirstName());
        holder.lastNameTV.setText(person.getLastName());
        holder.emailTV.setText(person.getEmail());
        holder.contactTV.setText(person.getPhoneNumber());
        holder.addressTV.setText(person.getAddress());

        holder.itemView.findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact(person);
            }

            private void updateContact(final Person person) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater layoutInflater = LayoutInflater.from(context);


                View view = layoutInflater.inflate(R.layout.dialog_update_contact, null);
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                final EditText et_first_name = view.findViewById(R.id.et_first_name);
                final EditText et_last_name = view.findViewById(R.id.et_last_name);
                final EditText et_email = view.findViewById(R.id.et_email);
                final EditText et_contact = view.findViewById(R.id.et_contact);
                final EditText et_address = view.findViewById(R.id.et_address);

                et_first_name.setText(person.getFirstName());
                et_last_name.setText(person.getLastName());
                et_email.setText(person.getEmail());
                et_contact.setText(person.getPhoneNumber());
                et_address.setText(person.getAddress());

                view.findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String firstName = et_first_name.getText().toString().trim();
                        String lastName = et_last_name.getText().toString().trim();
                        String email = et_email.getText().toString().trim();
                        String contact = et_contact.getText().toString().trim();
                        String address = et_address.getText().toString().trim();

                        if (firstName.isEmpty()) {
                            et_first_name.setError("Please enter your First Name");
                            et_first_name.requestFocus();
                            return;
                        }

                        if (lastName.isEmpty()) {
                            et_last_name.setError("Please enter your Last Name");
                            et_last_name.requestFocus();
                            return;
                        }

                        if (email.isEmpty()) {
                            et_email.setError("Please enter your Email ID");
                            et_email.requestFocus();
                            return;
                        }
                        if (contact.isEmpty()) {
                            et_contact.setError("Please enter your contact number");
                            et_contact.requestFocus();
                            return;
                        }

                        if (address.isEmpty()) {
                            et_address.setError("Please enter your complete address");
                            et_address.requestFocus();
                            return;
                        }

                        personRoomDB.personDao().updateContact(person.getId(),
                                firstName,
                                lastName,
                                email,
                                contact,
                                address);
                        loadContacts();
                        alertDialog.dismiss();
                    }
                });
            }
        });

        holder.itemView.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact(person);
            }

            private void deleteContact(final Person person) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure to delete " + person.getFirstName() + "?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        personRoomDB.personDao().deleteContact(person.getId());
                        loadContacts();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "The Contact (" + person.getFirstName() + ") has not been deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

    }

    private void loadContacts() {
        personList = personRoomDB.personDao().getAllContacts();
        notifyDataSetChanged();

        MainActivity.calculateContacts(personList.size());
    }

    @Override
    public int getItemCount() {

        Log.d(TAG, "getItemCount: " + personList.size());
        return personList.size();


    }

    @Override
    public Filter getFilter() {

        return personFilter;
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView firstNameTV;
        TextView lastNameTV;
        TextView emailTV;
        TextView contactTV;
        TextView addressTV;


        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            firstNameTV = itemView.findViewById(R.id.tv_firstName);
            lastNameTV = itemView.findViewById(R.id.tv_lastName);
            emailTV = itemView.findViewById(R.id.tv_email);
            contactTV = itemView.findViewById(R.id.tv_contact);
            addressTV = itemView.findViewById(R.id.tv_address);
        }

    }

    private Filter personFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Person> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(personNewList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Person item : personNewList) {
                    if (item.getFirstName().toLowerCase().contains(filterPattern) || item.getLastName().toLowerCase().contains(filterPattern) || item.getPhoneNumber().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            personList.clear();
            personList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}



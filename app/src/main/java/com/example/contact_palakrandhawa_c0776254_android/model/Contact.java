package com.example.contact_palakrandhawa_c0776254_android.model;

public class Contact
{
    int id;
    String first_name,last_name,  email, address;
    double phone_number;

    public Contact(int id, String first_name, String last_name , String email, String address, double phone_number)
    {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.address = address;
        this.phone_number = phone_number;
    }

    public int getId()
    {
        return id;
    }

    public String getFirstName()
    {
        return first_name;
    }
    public String getLastName()
    {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public double getPhone_number() {
        return phone_number;
    }
}

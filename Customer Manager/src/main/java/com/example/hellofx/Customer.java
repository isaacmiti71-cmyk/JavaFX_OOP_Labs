
package com.example.hellofx;

public class Customer {

    // Store the customer's name and province
    private final String name, province;

    // Constructor used to create a customer object
    public Customer(String name, String province) {
        this.name = name;
        this.province = province;
    }

    // Return the customer's name
    public String getName() {
        return name;
    }

    // Return the customer's province
    public String getProvince() {
        return province;
    }
}


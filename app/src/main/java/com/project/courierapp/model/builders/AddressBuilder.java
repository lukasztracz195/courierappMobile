package com.project.courierapp.model.builders;

public class AddressBuilder {

    private static StringBuilder address;

    private AddressBuilder(){
        address = new StringBuilder();
    }
    public static AddressBuilder builder(){
        return new AddressBuilder();
    }

    public AddressBuilder add(String path){
        address.append(path);
        address.append(" ");
        return this;
    }

    public String build(){
        return address.toString();
    }
}

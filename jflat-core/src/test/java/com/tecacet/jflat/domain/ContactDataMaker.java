package com.tecacet.jflat.domain;

public class ContactDataMaker {

    public static Contact createContact(String firstName, String lastName, String telephone,
                                  String street) {
        Address address = new Address();
        address.setCity("Springfield");
        address.setState(Address.State.NV);
        address.setNumberAndStreet(street);
        address.setZip(12345);
        Contact contact = new Contact();
        contact.setAddress(address);
        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setTelephone(new Telephone(telephone));
        return contact;
    }
}

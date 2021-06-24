package com.tecacet.jflat.impl;

import com.tecacet.jflat.domain.Contact;
import com.tecacet.jflat.domain.ContactDataMaker;
import com.tecacet.jflat.domain.Telephone;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class BeanTokenizerTest {

    @Test
    void apply() {
        String[] properties = {"name", "address", "telephone", "address.zip"};
        BeanTokenizer<Contact> tokenizer = new BeanTokenizer<>(properties);
        tokenizer.registerPropertyGetter("name" ,
                contact -> contact.getLastName() + ", " + contact.getFirstName());
        Function<Telephone, String> telephoneConverter = Telephone::getNumber;
        tokenizer.registerConverter(Telephone.class, telephoneConverter);
        Function<Integer, String> zipConverter = i -> "("+i+")";
        tokenizer.registerConverter("address.zip", zipConverter);
        Contact contact = ContactDataMaker.createContact("Leo", "Tolstoy",
                "3901234900", "Polyana");
        String[] tokens = tokenizer.apply(contact);
        assertEquals("[Tolstoy, Leo, Polyana, Springfield, NV, 12345, 1234900, (12345)]",
                Arrays.toString(tokens));
    }
}
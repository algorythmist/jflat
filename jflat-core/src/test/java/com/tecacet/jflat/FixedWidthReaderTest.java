package com.tecacet.jflat;

import com.tecacet.jflat.domain.Address;
import com.tecacet.jflat.domain.Contact;
import com.tecacet.jflat.domain.Telephone;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FixedWidthReaderTest {

    @Test
    void testDefaultReader() throws IOException {
        FixedWidthReader<String[]> reader = FixedWidthReader.createDefaultReader(new int[]{20, 20, 15, 25, 20, 5, 10})
                .withSkipRows(1);
        List<String[]> contacts = reader.readAll("contacts.txt");
        assertEquals(3, contacts.size());
        assertEquals("Homer", contacts.get(0)[0]);
        assertEquals("Skinner", contacts.get(1)[1]);
        assertEquals("9081672312", contacts.get(2)[2]);
    }

    @Test
    void testWithIndexedMapping() throws IOException {
        FixedWidthReader<Contact> reader = FixedWidthReader.createWithIndexMapping(
                Contact.class,
                new String[]{"name", "address.state", "telephone"},
                new int[]{20, 10, 12})
                .withSkipRows(1)
                .registerConverter(Telephone.class, Telephone::new);
        List<Contact> contacts = reader.readAllWithCallback("directory.txt", (record, contact) -> {
            String[] fullName = record.get(0).trim().split("\\s+");
            contact.setFirstName(fullName[0]);
            contact.setLastName(fullName[1]);
        });
        assertEquals(3, contacts.size());
        Contact contact = contacts.get(1);
        assertEquals("Mary", contact.getFirstName());
        assertEquals("Hartford", contact.getLastName());
        assertEquals("(319) 5194341", contact.getTelephone().toString());
        assertEquals(Address.State.CA, contact.getAddress().getState());
    }

    @Test
    public void testReadAsStream() throws IOException {
        FixedWidthReader<Contact> reader = FixedWidthReader.createWithIndexMapping(Contact.class,
                new String[]{"lastName", "address.state", "telephone"},
                new int[]{20, 10, 12})
                .registerConverter(Telephone.class, Telephone::new);
        InputStream is = ClassLoader.getSystemResourceAsStream("directory.txt");
        try (InputStreamReader r = new InputStreamReader(is)) {
            Contact contact = reader.readAsStream(r)
                    .filter(c -> c.getAddress().getState() == Address.State.CA).findFirst()
                    .orElse(null);
            assertEquals(Address.State.CA, contact.getAddress().getState());
            assertEquals("(319) 5194341", contact.getTelephone().toString());
            assertEquals("Mary Hartford", contact.getLastName());
        }
    }

    @Test
    void readWithCallback() throws IOException {
        FixedWidthReader<Contact> reader = FixedWidthReader.createWithIndexMapping(Contact.class,
                new String[]{"lastName", "address.state", "telephone"},
                new int[]{20, 10, 12})
                .withSkipRows(1)
                .registerConverter("telephone", Telephone::new);
        List<Contact> contacts = new ArrayList<>();
        reader.read("directory.txt", (record, contact) -> {
            String[] tokens = record.get(0).split("\\s+");
            contact.setFirstName(tokens[0].trim());
            contact.setLastName(tokens[1].trim());
            contacts.add(contact);
        });
        assertEquals(3, contacts.size());
        Contact contact = contacts.get(1);
        assertEquals(Address.State.CA, contact.getAddress().getState());
        assertEquals("(319) 5194341", contact.getTelephone().toString());
        assertEquals("Mary", contact.getFirstName());
        assertEquals("Hartford", contact.getLastName());
    }

    @Test
    void testSkipProperties() throws IOException {
        FixedWidthReader<Contact> reader = FixedWidthReader.createWithIndexMapping(
                Contact.class,
                new String[]{"firstName", "lastName", null, null, null, null, "address.zip"},
                new int[]{20, 20, 15, 25, 20, 5, 10})
                .withSkipRows(1);
        List<Contact> contacts = reader.readAll("contacts.txt");
        assertEquals(3, contacts.size());
        Contact contact = contacts.get(1);
        assertEquals("Seymour", contact.getFirstName());
        assertEquals("Skinner", contact.getLastName());
        assertNull(contact.getTelephone());
        assertNotNull(contact.getAddress());
        assertNull(contact.getAddress().getNumberAndStreet());
        assertEquals(12345, contact.getAddress().getZip());
    }

    @Test
    void testWithComments() throws IOException {
        FixedWidthReader<Contact> reader = FixedWidthReader.createWithIndexMapping(
                Contact.class,
                new String[]{"firstName", "lastName", null, "address.numberAndStreet", "address.city", "address.state", "address.zip"},
                new int[]{20, 20, 15, 25, 20, 5, 10})
                .withSkipRows(1)
                .withSkipEmptyLines()
                .withSkipComments("//");
        List<Contact> contacts = reader.readAll("contacts_with_comments.txt");
        assertEquals(4, contacts.size());
    }

}
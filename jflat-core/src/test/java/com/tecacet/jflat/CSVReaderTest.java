package com.tecacet.jflat;

import com.tecacet.jflat.domain.*;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CSVReaderTest {

    @Test
    void testDefaultReader() throws IOException {
        CSVReader<String[]> reader = CSVReader.defaultReader()
                .withFormat(CSVFormat.RFC4180
                        .withFirstRecordAsHeader()
                        .withSkipHeaderRecord(true));
        List<String[]> contacts = reader.readAll("contacts.csv");
        assertEquals(3, contacts.size());
        assertEquals("Homer", contacts.get(0)[0]);
        assertEquals("Skinner", contacts.get(1)[1]);
        assertEquals("9081672312", contacts.get(2)[2]);
    }

    @Test
    void testDefaultReaderWithCustomDelimiter() throws IOException {
        CSVReader<String[]> reader = CSVReader.defaultReader()
                .withSkipHeader()
                .withDelimiter('|');
        List<String[]> contacts = reader.readAll("contacts.custom.txt");
        assertEquals(3, contacts.size());
        assertEquals("Homer", contacts.get(0)[0]);
        assertEquals("Skinner", contacts.get(1)[1]);
        assertEquals("9081672312", contacts.get(2)[2]);
    }

    @Test
    void testWithIndexedMapping() throws IOException {
        FlatFileReader<ClassicQuote> csvReader = CSVReader.readerWithIndexMapping(ClassicQuote.class,
                new String[]{"date", "open", null, null, "close", "volume", null})
                .withFormat(CSVFormat.DEFAULT.withFirstRecordAsHeader().withSkipHeaderRecord());
        List<ClassicQuote> quotes = csvReader.readAll("GLD.csv");
        assertEquals(134, quotes.size());
        ClassicQuote quote = quotes.get(10);
        assertEquals(7036000, quote.getVolume());
        assertEquals(LocalDate.of(2015, 2, 2), quote.getDate());
    }

    @Test
    public void readAsStream() throws IOException {
        FlatFileReader<ImmutableQuote> csvReader = CSVReader.readerWithIndexMapping(ImmutableQuote.class,
                new String[]{"date", "open", null, null, "close", "volume", "adjustedClose"})
                .withFormat(CSVFormat.DEFAULT.withFirstRecordAsHeader().withSkipHeaderRecord());
        LocalDate date = LocalDate.of(2015, 5, 1);
        InputStream is = ClassLoader.getSystemResourceAsStream("GLD.csv");
        ImmutableQuote quote = csvReader.readAsStream(is)
                .filter(q -> q.getDate().equals(date)).findFirst()
                .orElse(null);
        assertEquals(date, quote.getDate());
    }

    @Test
    void readWithCallback() throws IOException {
        FlatFileReader<ImmutableQuote> csvReader = CSVReader.readerWithIndexMapping(ImmutableQuote.class,
                new String[]{"date", "open", null, null, "close", "volume", "adjustedClose"})
                .withFormat(CSVFormat.DEFAULT.withFirstRecordAsHeader().withSkipHeaderRecord());

        List<RowRecord> records = new ArrayList<>();
        csvReader.read("GLD.csv", (row, bean) -> {
            records.add(row);
            return null;
        });
        assertEquals(134, records.size());
    }

    @Test
    void readWithHeaderMapping() throws IOException {
        String[] properties = {"date", "open", "volume"};
        String[] header = {"Date", "Open", "Volume"};

        FlatFileReader<ImmutableQuote> csvReader =
                CSVReader.readerWithHeaderMapping(ImmutableQuote.class, header, properties);
        List<ImmutableQuote> quotes = csvReader.readAll("GLD.csv");
        assertEquals(134, quotes.size());
        ImmutableQuote quote = quotes.get(0);
        assertEquals(LocalDate.of(2015, 12, 1), quote.getDate());
        assertEquals(102.30, quote.getOpen().doubleValue(), 0.001);
        assertEquals(5800200L, quote.getVolume().longValue());
    }

    @Test
    void testWithTDFFormat() throws IOException {
        String[] properties = {"firstName", "lastName", "telephone",
                "address.numberAndStreet", "address.city", "address.state", "address.zip"};
        CSVReader<Contact> csvReader = CSVReader.readerWithIndexMapping(Contact.class, properties)
                .withFormat(CSVFormat.TDF)
                .registerConverter(Telephone.class, Telephone::new);

        List<Contact> contacts = csvReader.readAll("contacts.tdf");
        assertEquals(3, contacts.size());
        Contact contact = contacts.get(1);
        assertEquals("Seymour", contact.getFirstName());
        assertEquals("Skinner", contact.getLastName());
        assertEquals("(290) 8972672", contact.getTelephone().toString());
        Address address = contact.getAddress();
        assertEquals(Address.State.NV, address.getState());
        assertEquals("Springfield", address.getCity());
        assertEquals(12345, address.getZip());
        assertEquals("96 Orchard Ave.", address.getNumberAndStreet());
    }

    @Test
    void testReadWithTypeConverter() throws IOException {
        String[] properties = {"firstName", "lastName", "telephone"};
        String[] header = {"First Name", "Last Name", "Phone"};
        CSVReader<Contact> csvReader = CSVReader
                .readerWithHeaderMapping(Contact.class, header, properties)
                .registerConverter(Telephone.class, Telephone::new);
        List<Contact> contacts = csvReader.readAll("contacts.csv");
        assertEquals(3, contacts.size());
        Contact contact = contacts.get(1);
        assertEquals("Seymour", contact.getFirstName());
        assertEquals("Skinner", contact.getLastName());
        assertEquals("(290) 8972672", contact.getTelephone().toString());
    }

    @Test
    public void testReadWithPropertyConverter() throws IOException {
        String[] properties = {"firstName", "lastName", "telephone"};
        String[] header = {"First Name", "Last Name", "Phone"};
        FlatFileReader<Contact> csvReader = CSVReader
                .readerWithHeaderMapping(Contact.class, header, properties)
                .registerConverter("telephone", Telephone::new);
        List<Contact> contacts = csvReader.readAll("contacts.csv");
        assertEquals(3, contacts.size());
        Contact contact = contacts.get(1);
        assertEquals("Seymour", contact.getFirstName());
        assertEquals("Skinner", contact.getLastName());
        assertEquals("(290) 8972672", contact.getTelephone().toString());
    }

    @Test
    public void testReadWithClashingConverters() throws IOException {
        String[] properties = {"firstName", "lastName", "telephone"};
        String[] header = {"First Name", "Last Name", "Phone"};
        Function<String, Telephone> telephoneConverter = Telephone::new;
        FlatFileReader<Contact> csvReader = CSVReader
                .readerWithHeaderMapping(Contact.class, header, properties)
                .registerConverter(Telephone.class, s -> null)
                .registerConverter("telephone", telephoneConverter);
        List<Contact> contacts = csvReader.readAll("contacts.csv");
        assertEquals(3, contacts.size());
        Contact contact = contacts.get(1);
        assertEquals("Seymour", contact.getFirstName());
        assertEquals("Skinner", contact.getLastName());
        //The property converter wins
        assertEquals("(290) 8972672", contact.getTelephone().toString());
    }

    @Test
    public void testReadWithComments() throws IOException {
        String[] properties = {"firstName", "lastName", "telephone"};
        String[] header = {"First Name", "Last Name", "Phone"};
        CSVReader<Contact> csvReader = CSVReader
                .readerWithHeaderMapping(Contact.class, header, properties)
                .registerConverter(Telephone.class, Telephone::new)
                .withFormat(CSVFormat.DEFAULT.withFirstRecordAsHeader().withCommentMarker('#'));
        List<Contact> contacts = csvReader.readAll("contacts_with_comments.csv");
        assertEquals(3, contacts.size());
    }

    @Test
    void testReaderWithCallback() throws IOException {
        FlatFileReaderCallback<Order> callback = (record, order) -> {
            String[] name = record.get(1).split(",");
            String lastName = name[0];
            String firstName = name[1];
            order.setCustomer(new Customer());
            order.getCustomer().setLastName(lastName);
            order.getCustomer().setFirstName(firstName);
            return order;
        };

        String[] properties = new String[] { "number", "price" };
        String[] header = new String[] { "Number", "Price" };
        CSVReader<Order> csvReader = CSVReader.readerWithHeaderMapping(Order.class, header, properties);
        List<Order> orders = csvReader.readAllWithCallback("orders.csv", callback);
        assertEquals(2, orders.size());

        Order order = orders.get(1);
        Customer customer = order.getCustomer();
        assertEquals("Bob", customer.getFirstName());
        assertEquals("Marley", customer.getLastName());
    }
}
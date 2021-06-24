package com.tecacet.jflat;

import com.tecacet.jflat.domain.Contact;
import com.tecacet.jflat.domain.ImmutableQuote;
import com.tecacet.jflat.domain.Telephone;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static com.tecacet.jflat.domain.ContactDataMaker.createContact;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CSVWriterTest {

    @Test
    void writeToFileWithLineMapper() throws IOException {
        ImmutableQuote quote1 = new ImmutableQuote(LocalDate.of(2017, 2, 6), BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0), BigInteger.valueOf(100), null);
        ImmutableQuote quote2 = new ImmutableQuote(LocalDate.of(2017, 2, 7), BigDecimal.valueOf(10.0), BigDecimal.valueOf(20.0), BigInteger.valueOf(1000), null);
        CSVWriter<ImmutableQuote> csvWriter = new CSVWriter<>();
        csvWriter = csvWriter.withLineMapper(q -> q.getDate().toString());
        csvWriter.writeToFile("test1.csv", Arrays.asList(quote1, quote2));

        File file = new File("test1.csv");
        assertTrue(file.exists());
        List<String> lines = Files.readAllLines(Paths.get(file.toURI()));
        assertEquals(2, lines.size());
        assertEquals("2017-02-06", lines.get(0));
        assertEquals("2017-02-07", lines.get(1));
        file.delete();
    }

    @Test
    void writeToFileWithTokenizer() throws IOException {
        LocalDate date = LocalDate.of(2017, 2, 6);
        ImmutableQuote quote1 = new ImmutableQuote(date, BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0), BigInteger.valueOf(100), null);
        ImmutableQuote quote2 = new ImmutableQuote(date, BigDecimal.valueOf(10.0), BigDecimal.valueOf(20.0), BigInteger.valueOf(1000), null);
        CSVWriter<ImmutableQuote> csvWriter = new CSVWriter();
        csvWriter.withHeader(new String[]{"Date", "Price", "Volume"})
                .withTokenizer(q ->
                        new String[]{q.getDate().toString(), q.getOpen().toString(), q.getVolume().toString()});
        csvWriter.writeToFile("test2.csv", Arrays.asList(quote1, quote2));

        new File("test2.csv").delete();
    }

    @Test
    void writeToFileWithBeanTokenizer() throws IOException {
        ImmutableQuote quote1 = new ImmutableQuote(LocalDate.of(2017, 2, 6),
                BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0), BigInteger.valueOf(100), null);
        ImmutableQuote quote2 = new ImmutableQuote(LocalDate.of(2017, 3, 11),
                BigDecimal.valueOf(10.0), BigDecimal.valueOf(20.0), BigInteger.valueOf(1000), null);
        Function<LocalDate, String> dateConverter = date -> date.format(DateTimeFormatter.ofPattern("dd/MM/yy"));
        Function<BigDecimal, String> closeConverter = n -> n.toBigInteger().toString();
        CSVWriter<ImmutableQuote> csvWriter = CSVWriter
                .<ImmutableQuote>writerForProperties(new String[]{"date", "open", "close", "volume"})
                .withHeader(new String[]{"Date", "Open", "Close", "Volume"})
                .registerConverterForClass(LocalDate.class, dateConverter)
                .registerConverterForProperty("close", closeConverter);

        csvWriter.writeToFile("test3.csv", Arrays.asList(quote1, quote2));

        File file = new File("test3.csv");
        assertTrue(file.exists());
        List<String> lines = Files.readAllLines(Paths.get(file.toURI()));
        assertEquals(3, lines.size());
        file.delete();
    }

    @Test
    public void testNestedProperties() throws IOException {
        List<Contact> contacts = createContacts();
        String[] properties = {"firstName", "lastName", "telephone",
                "address.numberAndStreet", "address.city", "address.state", "address.zip"};
        String[] header = {"First Name", "Last Name", "Phone", "Street", "City", "State", "Zip"};
        CSVWriter<Contact> csvWriter = CSVWriter.writerForProperties(properties);
        csvWriter.withHeader(header)
                .registerConverterForClass(Telephone.class, telephone -> telephone.getNumber() + telephone.getAreaCode());
        csvWriter.writeToFile("contacts.csv", contacts);

        CSVReader<Contact> csvReader = CSVReader.readerWithHeaderMapping(Contact.class, header, properties);
        csvReader.registerConverter(Telephone.class, Telephone::new);
        List<Contact> results = csvReader.readAll("contacts.csv");
        assertEquals(3, results.size());
        assertEquals("(908) 1672312", results.get(0).getTelephone().toString());
        assertEquals(contacts.get(1).getAddress().getCity(), results.get(1).getAddress().getCity());
        assertEquals(contacts.get(1).getAddress().getState(), results.get(1).getAddress().getState());

        boolean deleted = new File("contacts.csv").delete();
        assertTrue(deleted);
    }

    private List<Contact> createContacts() {
        return Arrays.asList(
                createContact("Homer", "Simpson", "312 908 1672", "24 Maple St."),
                createContact("Seymour", "Skinner", "6722908972", "96 Orchard Ave."),
                createContact("Marge", "Simpson", "312908 1672", "24 Maple St.")
        );
    }


}

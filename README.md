# JFlat

JFlat is a Java API that helps developers map flat files to and from Java Objects. 
It includes support for CSV, fixed width format, arbitrarily delimited data, excel, et cetera.

## Getting started with JFlat

```xml
<dependency>
    <groupId>com.tecacet</groupId>
    <artifactId>jflat-core</artifactId>
    <version>${jflat.version}</version>
</dependency>
```

### How to read a  file into an list of String arrays

```java
    CSVReader<String[]> reader = CSVReader.createDefaultReader();
    List<String[]> contacts = reader.readAll("contacts.csv");
```

### How to read a file into a list of beans

To read a file without header, just specify the properties of the bean in the locations
where they appear in the file. Use null for columns that you want to skip.

```java
    FlatFileReader<ClassicQuote> csvReader = CSVReader.createWithIndexMapping(ClassicQuote.class,
                new String[]{"date", "open", null, null, "close", "volume", null});
    List<ClassicQuote> quotes = csvReader.readAll("GLD.csv");
```

To read a file with a header, specify the properties and the corresponding names in 
the header

```java
    String[] properties = {"date", "open", "volume"};
    String[] header = {"Date", "Open", "Volume"};

    FlatFileReader<ImmutableQuote> csvReader =
                CSVReader.createWithHeaderMapping(ImmutableQuote.class, header, properties);
    List<ImmutableQuote> quotes = csvReader.readAll("GLD.csv");
```

For CSV support, JFlat uses [Apache Commons CSV](http://commons.apache.org/proper/commons-csv/)
so all the features provided by this library are available, simply by providing the 
appropriate CSV Format.

The following example specifies a CSV format to read a tab-delimited file:

```java
    String[] properties = {"firstName", "lastName", "telephone",
                "address.numberAndStreet", "address.city", "address.state", "address.zip"};
    CSVReader<Contact> csvReader = CSVReader.createWithIndexMapping(Contact.class, properties)
                .withFormat(CSVFormat.TDF)
                .registerConverter(Telephone.class, Telephone::new);
    List<Contact> contacts = csvReader.readAll("contacts.tdf");
```

### Custom Data Conversion

Custom conversion to the desired data type is accomplished easily by registering a converter with the reader. 
A converter is simple a java.util.Function. 
For example, suppose we want to convert telephone numbers to the custom Telephone class:

```java
public class Telephone {

    private final String areaCode;
    private final String number;

    public Telephone(String phoneNumber) {
        String normalized = phoneNumber.replaceAll("[-||\\s+]", "").trim();
        if (normalized.length() != 10) {
            throw new IllegalArgumentException("Invalid Phone Number");
        }
        this.areaCode = normalized.substring(0,3);
        this.number = normalized.substring(3, 10);
    }
```

We can register a converter that invokes the Telephone constructor like this:

```java
    CSVReader<Contact> csvReader = CSVReader
            .createWithHeaderMapping(Contact.class, header, properties)
            .registerConverter(Telephone.class, Telephone::new)
            .withFormat(CSVFormat.DEFAULT.withFirstRecordAsHeader().withCommentMarker('#'));
```
The above example also illustrates how we can use CSV format to skip comments.

Converters can be registered either by type, as illustrated above, or by property:

```java
String[] properties = {"firstName", "lastName", "telephone"};
String[] header = {"First Name", "Last Name", "Phone"};
Function<String, Telephone> telephoneConverter = Telephone::new;
FlatFileReader<Contact> csvReader = CSVReader
                .createWithHeaderMapping(Contact.class, header, properties)
                .registerConverter("telephone", telephoneConverter);
```

### Reading other file formats

JFlat does not only support CSV but other flat file types, such as fixed width. 
Here is an example of how to read a fixed width file that looks like this:

```csv
NAME                STATE     TELEPHONE   
John Smith          WA        418-311-4111
   Mary Hartford    CA        319-519-4341
Evan Nolan          IL        219-532-4301
```

```java
FixedWidthReader<Contact> reader = FixedWidthReader.createWithIndexMapping(
                Contact.class,
                new String[]{"name", "address.state", "telephone"},
                new int[]{20, 10, 12})
                .withSkipRows(1)
                .registerConverter(Telephone.class, s -> new Telephone(s));
```

## Writing to files

In the following example, we map Contact objects in a CSV file. 
We specify the properties we want to map and the header. 
Furthermore, we register a converter that converts objects of Telephone to String.
If we do not specify a converter, the toString() method is going to be used.

```java
    String[] properties = {"firstName", "lastName", "telephone",
                "address.numberAndStreet", "address.city", "address.state", "address.zip"};
    String[] header = {"First Name", "Last Name", "Phone", "Street", "City", "State", "Zip" };
    CSVWriter csvWriter = CSVWriter
                .createForProperties(properties)
                .withHeader(header)
                .registerConverterForClass(Telephone.class, telephone -> telephone.getNumber()+telephone.getAreaCode());
    csvWriter.writeToFile("contacts.csv", contacts);   
```

In the example below, we write Contact objects in a Fixed-width file:

```java
    String[] properties = {"name", "address", "telephone", "address.zip"};
    FixedWidthWriter<Contact> fixedWidthWriter = new FixedWidthWriter<>(new int[] {20, 40, 10, 7},
                properties);
    List<Contact> contacts = Arrays.asList(contact1, contact2);
    fixedWidthWriter.writeToFile("contacts.txt", contacts);
```


## Advanced Features

### Reading records in a Java 8 Stream

In the following example, we read a fixed-width file with three fields 
of widths 20,10, and 12. We read the file bean by bean into a Stream
and look for a particular record.

```java
    FixedWidthReader<Contact> reader = FixedWidthReader.createWithIndexMapping(Contact.class,
                new String[] {"name", "address.state", "telephone"},
                new int[] {20,10,12})
                .registerConverter(Telephone.class, s -> new Telephone(s));
    InputStream is = ClassLoader.getSystemResourceAsStream("directory.txt");
    InputStreamReader r = new InputStreamReader(is);
    Contact contact = reader.readAsStream(r)
                .filter(c -> c.getAddress().getState() == Address.State.CA).findFirst()
                .orElse(null);
```

### Reading with a callback

The beans do not need to be read directly in a list or a stream. 
It is possible to process each bean as it is being read by using a FlatFileReaderCallback.
This enables highly customizable mappings from files to beans.

In the following example, we read an orders file where the Customers name appears in a single field. 
We use a FlatFileReaderCallback to parse the complete name into first and last name:

```java
    List<Order> orders = 
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
    CSVReader<Order> csvReader = CSVReader.createWithHeaderMapping(Order.class, header, properties);
    csvReader.read("orders.csv", callback);
```

### Writing custom properties

When writing files, the output format does not need to match the source beans exactly. 
Suppose we have this contact class

```java
public class Contact {
    private String firstName;
    private String lastName;
    private Telephone telephone;
    private Address address;
}
```

And we want to export a list of instances in fixed width format where the first 20 
characters is the full name in the form Last, First, the next 40 the entire address, 
the next 10 the phone number without the area code, and the last 9 the zip code in parentheses.

The output file will look like this:
```csv
Tolstoy, Leo        Polyana, Springfield, NV, 12345         1234900   (12345)
Karenina, Anna      Liverpool, Springfield, NV, 12345       7862121   (12345)
```

This can be easily accomplished by registering custom property getters, or property converters 
that can be registered either by type or by property name as illustrated in this example:

```java
String[] properties = {"name", "address", "telephone", "address.zip"};
FixedWidthWriter<Contact> fixedWidthWriter = new FixedWidthWriter<>(new int[] {20, 40, 10, 7}, properties);

//register a custom property getter, which is any Function that takes the target bean and returns a String
fixedWidthWriter.registerPropertyGetter("name", contact -> contact.getLastName() + ", " + contact.getFirstName());

//Register a custom String converter for the type Telephone. In this case, we want the number without the area code
fixedWidthWriter.registerConverterForClass(Telephone.class, Telephone::getNumber);

//Register a custom converter for a property by name. In this case we want to enclose the zip code in parenthesis
Function<Integer, String> zipConverter = i -> "(" + i + ")";
fixedWidthWriter.registerConverterForProperty("address.zip", zipConverter);
```
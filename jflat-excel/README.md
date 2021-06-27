# jflat-excel

jflat-excel is a jflat-core extension for reading excel files

```xml
<dependency>
    <groupId>com.tecacet</groupId>
    <artifactId>jflat-excel</artifactId>
    <version>${jflat.version}</version>
</dependency>
```

Read a file with a header:
```java
 ExcelReader<Quote> reader = ExcelReader.createWithHeaderMapping(Quote.class,
                new String[]{"Date", "Open", "Close", "Volume"},
                new String[]{"date", "open", "close", "volume"});
 List<Quote> rows = reader.readAll(filename);

```

Read a file without a header by specifying the order of the properties:
```java
 ExcelReader<Quote> reader = ExcelReader.createWithIndexMapping(Quote.class,
                new String[]{"date", "open", "close", "volume"}, false);
 List<Quote> rows = reader.readAll(filename);

```

jflat-excel can read both xls vs xlsx. It will automatically use the correct 
parser based on the extension.


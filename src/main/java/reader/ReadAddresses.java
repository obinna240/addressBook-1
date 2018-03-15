package reader;

import model.Gender;
import model.Person;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ReadAddresses {

    private String addressBook;
    private List<Person> peopleAddress;

    public ReadAddresses(String addressBook) {
        this.addressBook = addressBook;
        try {
            readAddressBook();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Person> getPeopleAddress() {
        return peopleAddress;
    }

    private List<Person> readAddressBook() throws URISyntaxException, IOException {
        final List<String> addressBookPeople = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(ClassLoader.getSystemResource(addressBook).toURI()))) {
            stream.forEach(addressBookPeople::add);
        }

        final DateTimeFormatter format = new DateTimeFormatterBuilder()
                .appendPattern("dd/MM/")
                .appendValueReduced(ChronoField.YEAR, 2, 2, 1900)
                .toFormatter();

        List<Person> addressListOfPeople = addressBookPeople.stream()
                .map(s -> s.split(", "))
                .map(array -> new Person(array[0], Gender.valueOf(array[1].toUpperCase()), LocalDate.parse(array[2], format)))
                .collect(toList());

        peopleAddress = addressListOfPeople;
        return addressListOfPeople;
    }

}

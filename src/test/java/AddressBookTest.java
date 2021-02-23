import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for {@link AddressBook}
 * @author Corneilious Eanes
 * @since February 22, 2021
 */
class AddressBookTest {

  private static AddressEntry createEntryA() {
    return new AddressEntry("John", "Smith", new Address("123 Main Street", "San Francisco", "CA", 12345), "555-555-1234", "john.smith@example.com");
  }

  private static AddressEntry createEntryB() {
    return new AddressEntry("Michael", "Doe", new Address("456 Elm Avenue", "Seattle", "WA", 67890), "123-456-7890", "michael.doe@example.com");
  }

  private static AddressEntry createEntryC() {
    return new AddressEntry("Jane", "Doe", new Address("789 First Street", "Washington D.C.", "ML", 76543), "987-654-4321", "jdoe@website.org");
  }

  private static AddressEntry createEntryD() {
    return new AddressEntry("Marylyn", "Roe", new Address("654 Second Avenue", "Philadelphia", "PA", 37892), "654-123-8976", "marlyn123@test.co");
  }

  private static AddressEntry createEntryE() {
    return new AddressEntry("Lynne", "Grewe", new Address("123 3rd Ave", "Hayward", "Ca", 28666), "lynne.grewe@csueastbay.edu", "510-885-4167");
  }

  private static AddressEntry createEntryF() {
    return new AddressEntry("Butch", "Grewe", new Address("Bay Avenue", "Capitoal", "Ca", 99999), "bg@gmail.com", "415-389-1239");
  }

  private static AddressBook createBook() {
    AddressBook ab = new AddressBook();
    ab.add(createEntryA());
    ab.add(createEntryB());
    ab.add(createEntryC());
    return ab;
  }

  @Test
  void testRemove() {
    AddressBook ab = createBook();
    assertTrue(ab.remove("Smith"));
    // smith has already been removed
    assertFalse(ab.remove("Smith"));
    assertTrue(ab.remove("Doe"));
    assertFalse(ab.remove("Johnson"));
  }

  @Test
  void testAdd() {
    AddressBook ab = createBook();
    AddressEntry entry1 = createEntryD();
    ab.add(entry1);
    List<AddressEntry> queryResult = ab.find("Roe");
    assertTrue(queryResult.contains(entry1));
    AddressEntry entry2 = createEntryE();
    ab.add(entry2);
    queryResult = ab.find("Grewe");
    assertTrue(queryResult.contains(entry2));
    AddressEntry entry3 = createEntryF();
    ab.add(entry3);
    queryResult = ab.find("Grewe");
    assertTrue(queryResult.contains(entry2));
    assertTrue(queryResult.contains(entry3));
  }

  @Test
  void testFeadFromFile() {
    AddressBook ab = createBook();
    assertDoesNotThrow(() -> ab.readFromFile("src/test/resources/AddressInputDataFile.txt"));
    // first check that pre-existing entries are preserved
    List<AddressEntry> queryResult = ab.find("Doe");
    assertTrue(queryResult.contains(createEntryB()));
    assertTrue(queryResult.contains(createEntryC()));
    queryResult = ab.find("Smith");
    assertTrue(queryResult.contains(createEntryA()));
    // then check if file-specific entries were added
    AddressEntry entry1 = createEntryE();
    AddressEntry entry2 = createEntryF();
    queryResult = ab.find(entry1.getLastName());
    assertTrue(queryResult.contains(entry1));
    queryResult = ab.find(entry2.getLastName());
    assertTrue(queryResult.contains(entry2));
  }

  @Test
  void testFind() {
    AddressBook ab = createBook();
    AddressEntry e1 = createEntryA();
    AddressEntry e2 = createEntryB();
    AddressEntry e3 = createEntryC();

    List<AddressEntry> queryResult = ab.find("Smi");
    assertTrue(queryResult.contains(e1));
    assertFalse(queryResult.contains(e2));
    assertFalse(queryResult.contains(e3));

    queryResult = ab.find("smi");
    assertTrue(queryResult.contains(e1));
    assertFalse(queryResult.contains(e2));
    assertFalse(queryResult.contains(e3));

    queryResult = ab.find("Smith");
    assertTrue(queryResult.contains(e1));
    assertFalse(queryResult.contains(e2));
    assertFalse(queryResult.contains(e3));

    queryResult = ab.find("Doe");
    assertFalse(queryResult.contains(e1));
    assertTrue(queryResult.contains(e2));
    assertTrue(queryResult.contains(e3));

    queryResult = ab.find("do");
    assertFalse(queryResult.contains(e1));
    assertTrue(queryResult.contains(e2));
    assertTrue(queryResult.contains(e3));

    queryResult = ab.find("ajksdbjkasd");
    assertFalse(queryResult.contains(e1));
    assertFalse(queryResult.contains(e2));
    assertFalse(queryResult.contains(e3));
  }

}
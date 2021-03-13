import address.data.Address;
import address.AddressBook;
import address.data.AddressEntry;
import address.data.Name;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for {@link AddressBook}
 * @author Corneilious Eanes
 * @since March 11, 2021
 */
class AddressBookTest {

  private static final UUID
    ID_A = UUID.fromString("53cdeb31-d68d-4a2b-8be6-cdacff90e315"),
    ID_B = UUID.fromString("15e9c516-fab6-45d1-97c3-78a172dabb49"),
    ID_C = UUID.fromString("e1052539-c5e0-457a-bf10-cbe591c2c53f"),
    ID_D = UUID.fromString("b8ac866d-6ba6-49c5-8bfc-c7bf2f2f4f48"),
    ID_E = UUID.fromString("a90b4a26-51e9-4246-a36a-637bbe2e9b5d");


  private static AddressEntry createEntryA() {
    return new AddressEntry(ID_A, new Name("John", "Smith"),
      new Address("123 Main Street", "San Francisco", "CA", 12345), "555-555-1234", "john.smith@example.com");
  }

  private static AddressEntry createEntryB() {
    return new AddressEntry(ID_B, new Name("Michael", "Doe"),
      new Address("456 Elm Avenue", "Seattle", "WA", 67890), "123-456-7890", "michael.doe@example.com");
  }

  private static AddressEntry createEntryC() {
    return new AddressEntry(ID_C, new Name("Jane", "Doe"),
      new Address("789 First Street", "Washington D.C.", "ML", 76543), "987-654-4321", "jdoe@website.org");
  }

  private static AddressEntry createEntryD() {
    return new AddressEntry(ID_D, new Name("Marylyn", "Roe"),
      new Address("654 Second Avenue", "Philadelphia", "PA", 37892), "654-123-8976", "marlyn123@test.co");
  }

  private static AddressEntry createEntryE() {
    return new AddressEntry(ID_E, new Name("Alexander", "Hamilton"),
      new Address("321 Third Place", "New York City", "NY", 43219), "555-555-1111", "aham@mytestwebsite.org");
  }

  private static AddressBook createBook() {
    AddressBook ab = new AddressBook();
    ab.add(createEntryA());
    ab.add(createEntryB());
    ab.add(createEntryC());
    return ab;
  }

  @Test
  void count() {
    AddressBook ab = createBook();
    assertEquals(ab.count(), 3);
    ab.add(createEntryD());
    ab.add(createEntryE());
    assertEquals(ab.count(), 5);
    ab.remove(ID_E);
    assertEquals(ab.count(), 4);
  }

  @Test
  void get() {
    AddressBook ab = createBook();
    assertEquals(ab.get(ID_A), createEntryA());
    assertEquals(ab.get(ID_B), createEntryB());
    assertEquals(ab.get(ID_C), createEntryC());
    assertNull(ab.get(ID_D));
    assertNull(ab.get(ID_E));
  }

  @Test
  void remove_lastName() {
    AddressBook ab = createBook();
    assertTrue(ab.remove("Smith"));
    assertFalse(ab.remove("Smith"));
    assertTrue(ab.remove("Doe"));
    assertFalse(ab.remove("Doe"));
    assertFalse(ab.remove("Johnson"));
    assertFalse(ab.remove("Roe"));
    assertEquals(ab.count(), 0);
  }

  @Test
  void remove_id() {
    AddressBook ab = createBook();

    assertTrue(ab.remove(ID_A));
    assertFalse(ab.remove(ID_A));
    assertTrue(ab.remove(ID_B));
    assertFalse(ab.remove(ID_B));
    assertTrue(ab.remove(ID_C));
    assertFalse(ab.remove(ID_C));
    assertFalse(ab.remove(ID_D));
    assertEquals(ab.count(), 0);
  }

  @Test
  void remove_ref() {
    AddressBook ab = createBook();
    AddressEntry a = createEntryA();
    AddressEntry b = createEntryB();
    AddressEntry c = createEntryC();
    AddressEntry d = createEntryD();

    assertTrue(ab.remove(a));
    assertFalse(ab.remove(a));
    assertTrue(ab.remove(b));
    assertFalse(ab.remove(b));
    assertTrue(ab.remove(c));
    assertFalse(ab.remove(c));
    assertFalse(ab.remove(d));
    assertEquals(ab.count(), 0);
  }

  @Test
  void add() {
    AddressBook ab = createBook();
    AddressEntry entry1 = createEntryD();
    ab.add(entry1);
    List<AddressEntry> queryResult = ab.find("Roe");
    assertTrue(queryResult.contains(entry1));
    AddressEntry entry2 = createEntryE();
    ab.add(entry2);
    queryResult = ab.find("Hamilton");
    assertTrue(queryResult.contains(entry2));
    assertTrue(queryResult.contains(entry2));
  }

  @Test
  void find() {
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

  @Test
  void clear() {
    AddressBook ab = createBook();
    ab.clear();
    assertEquals(ab.count(), 0);
    ab.add(createEntryA());
    ab.add(createEntryB());
    ab.clear();
    assertEquals(ab.count(), 0);
  }

}
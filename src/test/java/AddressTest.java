import address.data.Address;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test cases for {@link Address}
 * @author Corneilious Eanes
 * @since March 11, 2021
 */
class AddressTest {

  private static Address createA() {
    return new Address("123 Main Street", "San Francisco", "CA", 12345);
  }

  private static Address createB() {
    return new Address("456 Elm Avenue", "Seattle", "WA", 67890);
  }

  @Test
  void _toString() {
    assertEquals(createA().toString(), "123 Main Street\nSan Francisco, CA 12345");
    assertEquals(createB().toString(), "456 Elm Avenue\nSeattle, WA 67890");
  }

  @Test
  void _equals() {
    Address addressA = createA();
    assertEquals(addressA, addressA);
    assertEquals(createA(), addressA);
    Address addressB = createB();
    assertEquals(addressB, addressB);
    assertEquals(createB(), addressB);
    assertNotEquals(addressB, addressA);
    assertNotEquals(addressA, addressB);
    assertNotEquals(addressA, null);
    assertNotEquals(addressB, null);
  }

  @Test
  void getStreet() {
    assertEquals(createA().getStreet(), "123 Main Street");
    assertEquals(createB().getStreet(), "456 Elm Avenue");
  }

  @Test
  void setStreet() {
    Address address = createA();
    final String newStreet1 = "789 Woodward Place";
    address.setStreet(newStreet1);
    assertEquals(address.getStreet(), newStreet1);
    final String newStreet2 = "8912 Yale Court";
    address.setStreet(newStreet2);
    assertEquals(address.getStreet(), newStreet2);
  }

  @Test
  void getCity() {
    assertEquals(createA().getCity(), "San Francisco");
    assertEquals(createB().getCity(), "Seattle");
  }

  @Test
  void setCity() {
    Address address = createA();
    final String newCity1 = "Los Angeles";
    address.setCity(newCity1);
    assertEquals(address.getCity(), newCity1);
    final String newCity2 = "Alamo";
    address.setCity(newCity2);
    assertEquals(address.getCity(), newCity2);
  }

  @Test
  void getState() {
    assertEquals(createA().getState(), "CA");
    assertEquals(createB().getState(), "WA");
  }

  @Test
  void setState() {
    Address address = createA();
    final String newState1 = "WA";
    address.setState(newState1);
    assertEquals(address.getState(), newState1);
    final String newState2 = "PA";
    address.setState(newState2);
    assertEquals(address.getState(), newState2);
  }

  @Test
  void getZip() {
    assertEquals(createA().getZip(), 12345);
    assertEquals(createB().getZip(), 67890);
  }

  @Test
  void setZip() {
    Address address = createA();
    final int newZip1 = 67890;
    address.setZip(newZip1);
    assertEquals(address.getZip(), newZip1);
    final int newZip2 = 89732;
    address.setZip(newZip2);
    assertEquals(address.getZip(), newZip2);
  }

}
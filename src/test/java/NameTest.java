import address.data.Name;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test cases for {@link Name}
 * @author Corneilious Eanes
 * @since March 11, 2021
 */
class NameTest {
  
  private static Name createA() {
    return new Name("John", "Smith");
  }

  private static Name createB() {
    return new Name("Jane", "Doe");
  }

  @Test
  void _toString() {
    assertEquals(createA().toString(), "Smith, John");
    assertEquals(createB().toString(), "Doe, Jane");
  }

  @Test
  void _equals() {
    Name a0 = createA();
    Name a1 = createA();
    Name b0 = createB();
    Name b1 = createB();
    assertEquals(a0, a1);
    assertEquals(a1, a0);
    assertEquals(b0, b1);
    assertEquals(b1, b0);
    assertNotEquals(a0, b0);
    assertNotEquals(a0, b1);
    assertNotEquals(a1, b0);
    assertNotEquals(a1, b1);
  }

  @Test
  void compareTo() {
    Name a = createA();
    Name b = createB();
    assertEquals(a.compareTo(createA()), 0);
    assertEquals(b.compareTo(createB()), 0);
    Name[] names = new Name[] {a, b};
    Arrays.sort(names);
    assertEquals(names[0], b);
    assertEquals(names[1], a);
  }

  @Test
  void getFirstName() {
    assertEquals(createA().getFirstName(), "John");
    assertEquals(createB().getFirstName(), "Jane");
  }

  @Test
  void setFirstName() {
    Name a = createA();
    String n0 = "Tristan";
    a.setFirstName(n0);
    assertEquals(n0, a.getFirstName());
    String n1 = "James";
    a.setFirstName(n1);
    assertEquals(n1, a.getFirstName());
  }

  @Test
  void getLastName() {
    assertEquals(createA().getLastName(), "Smith");
    assertEquals(createB().getLastName(), "Doe");
  }

  @Test
  void setLastName() {
    Name a = createA();
    String n0 = "Woodward";
    a.setLastName(n0);
    assertEquals(n0, a.getLastName());
    String n1 = "Quinton";
    a.setLastName(n1);
    assertEquals(n1, a.getLastName());
  }
  
}
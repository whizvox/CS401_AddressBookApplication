package address;

import address.data.Address;
import address.data.AddressEntry;
import address.data.Name;
import address.gui.MainPanel;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * Both stores the main method for the application and acts as the main container for all objects in the
 * application.
 * @author Corneilious Eanes
 * @since March 15, 2021
 */
public class AddressBookApplication {

  /**
   * The main entry point into the address book application.
   * @param args This is ignored, please don't use it
   */
  public static void main(String[] args) {
    try {
      Utils.info("Checking SQL driver...");
      Class.forName("oracle.jdbc.OracleDriver");
      Utils.info("Starting address book application...");
      new AddressBookApplication();
      Utils.info("Setup complete");
    } catch (Exception e) {
      Utils.error(e, "FATAL ERROR OCCURRED, IMMEDIATELY EXITING APPLICATION");
    }
  }

  /**
   * The frame used to contain an instance of {@link MainPanel}
   */
  private JFrame frame;
  /**
   * The application's address book used for querying information
   */
  private AddressBook book;
  /**
   * The connection object used to connect to the database that stores instances of {@link AddressEntry}
   */
  private Connection conn;

  /**
   * The default constructor for the application. Will automatically create an instance of {@link JFrame} that contains
   * the application. Will also automatically connect to a remote database given the username and password specified by
   * <code>credentials.txt</code>.
   * @throws RuntimeException If some fatal error occurred during startup. This could be triggered by the
   *                          <code>credentials.txt</code> not being present, the username/password being incorrect,
   *                          the database being inaccessible, or a number of unforeseen events.
   */
  public AddressBookApplication() throws RuntimeException {
    if (instance != null) {
      throw new RuntimeException("Cannot have multiple instances of AddressBookApplication");
    }
    instance = this;
    book = new AddressBook();

    // reads a file called "credentials.txt" from the project root directory. this stores the username and password
    // needed to connect to the remote database.
    if (!Files.exists(Paths.get("credentials.txt"))) {
      JOptionPane.showMessageDialog(null, "Could not open credentials.txt", "Cannot open file", JOptionPane.ERROR_MESSAGE);
      throw new RuntimeException("Could not connect to database as credentials.txt is missing");
    }
    String username;
    String password;
    try (FileReader reader = new FileReader("credentials.txt")) {
      Scanner scanner = new Scanner(reader);
      username = scanner.nextLine();
      password = scanner.nextLine();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, "Could not read credentials.txt", "Cannot read file", JOptionPane.ERROR_MESSAGE);

      throw new RuntimeException("Could not read credentials", e);
    }

    try {
      Utils.info("Establishing connection to database server...");
      conn = DriverManager.getConnection("jdbc:oracle:thin:@adcsdb01.csueastbay.edu:1521:mcspdb.ad.csueastbay.edu",
        username, password);
      Utils.info("Connection successful");
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, "Invalid username/password.  Logon denied", "Invalid logon", JOptionPane.ERROR_MESSAGE);

      throw new RuntimeException("Could not establish connection to database server", e);
    }
    Utils.info("Initializing contacts in address book...");
    refreshContactsList();
    Utils.info("Read %d contacts", book.count());

    Utils.info("Starting Swing application...");
    frame = new JFrame("Address Book Application");
    frame.setContentPane(new MainPanel());
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();

    // Closes the connection to the remote database when the window is closing. While this isn't necessarily needed,
    // it's still good practice.
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent event) {
        try {
          conn.close();
          Utils.info("Database connection successfully closed");
        } catch (SQLException e) {
          Utils.warn(e, "Could not close database connection");
        }
      }
    });

    frame.setVisible(true);
  }

  /**
   * Get the application's address book.
   * @return The application's address book
   */
  public AddressBook getBook() {
    return book;
  }

  /**
   * Get the application's connection to the remote database.
   * @return The application's connection object
   */
  public Connection getConnection() {
    return conn;
  }

  /**
   * Get the application's GUI frame
   * @return The application's frame
   */
  public JFrame getFrame() {
    return frame;
  }

  /**
   * Will refresh the contents of {@link #getBook()}, querying the remote database in the process.
   */
  public void refreshContactsList() {
    try {
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM ADDRESSENTRYTABLE");
      while (rs.next()) {
        AddressEntry contact = new AddressEntry(
          UUID.fromString(rs.getString("ID")),
          new Name(
            rs.getString("FIRSTNAME"),
            rs.getString("LASTNAME")
          ),
          new Address(
            rs.getString("STREET"),
            rs.getString("CITY"),
            rs.getString("STATE"),
            rs.getInt("ZIP")
          ),
          rs.getString("PHONE"),
          rs.getString("EMAIL")
        );
        book.add(contact);
        Utils.info("Contact added: %s (%s)", contact.getId(), contact.getName());
      }
    } catch (SQLException e) {
      throw new RuntimeException("Could not initialize contacts list", e);
    }
  }

  /**
   * Inserts a new contact to the remote database.
   * @param contact The contact to add to the database
   * @return The randomly-generated UUID associated with the specified contact information
   */
  public UUID addContact(AddressEntry contact) {
    UUID id = UUID.randomUUID();
    try {
      PreparedStatement stmt = conn.prepareStatement("INSERT INTO ADDRESSENTRYTABLE VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
      stmt.setString(1, id.toString());
      stmt.setString(2, contact.getName().getFirstName());
      stmt.setString(3, contact.getName().getLastName());
      stmt.setString(4, contact.getAddress().getStreet());
      stmt.setString(5, contact.getAddress().getCity());
      stmt.setString(6, contact.getAddress().getState());
      stmt.setInt(7, contact.getAddress().getZip());
      stmt.setString(8, contact.getPhone());
      stmt.setString(9, contact.getEmail());
      stmt.execute();
      contact.setId(id);
      if (!book.add(contact)) {
        Utils.warn("Cache mismatch: Could not add contact to internal cache: %1$s (%2$s)", contact.getId(), contact.getName());
      }
      Utils.info("Added new contact to database: %1$s (%2$s)", id, contact.getName());
    } catch (SQLException e) {
      throw new RuntimeException("Could not add contact", e);
    }
    return id;
  }

  /**
   * Removes a contact from the remote database.
   * @param id The ID of the contact to remove
   */
  public void removeContact(UUID id) {
    try {
      PreparedStatement stmt = conn.prepareStatement("DELETE FROM ADDRESSENTRYTABLE WHERE id=?");
      stmt.setString(1, id.toString());
      stmt.execute();
      if (!book.remove(id)) {
        Utils.warn("Cache mismatch: attempted to remove entry from internal cache: %s", id);
      }
      Utils.info("Removed contact from database: %s", id);
    } catch (SQLException e) {
      throw new RuntimeException("Could not remove contact", e);
    }
  }

  /**
   * Queries the remote database to fetch all contact IDs that match the specified query string.
   * @param lastNameQuery The query string. Last names that start with this string will match
   * @return A list of IDs that matched the query
   */
  public List<UUID> findContacts(String lastNameQuery) {
    try {
      ArrayList<UUID> ids = new ArrayList<>();
      PreparedStatement stmt = conn.prepareStatement("SELECT (id) FROM ADDRESSENTRYTABLE WHERE LASTNAME LIKE ?% ORDER BY LASTNAME, FIRSTNAME");
      stmt.setString(1, lastNameQuery);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String idStr = rs.getString("id");
        ids.add(UUID.fromString(idStr));
      }
      return ids;
    } catch (SQLException | IllegalArgumentException e) {
      throw new RuntimeException("Could not find contact", e);
    }
  }

  /**
   * Updates a specific contact from the remote database.
   * @param contact The entry to update. Will use {@link AddressEntry#getId()} for selecting the exact contact entry
   * @throws SQLException If one of the contact's fields conflict with the remote database's constraints. Most likely,
   *                      this will be thrown if a string-based field is too long.
   */
  public void updateContact(AddressEntry contact) throws SQLException {
    PreparedStatement stmt = conn.prepareStatement("UPDATE ADDRESSENTRYTABLE SET FIRSTNAME=?, LASTNAME=?, STREET=?, CITY=?, STATE=?, ZIP=?, PHONE=?, EMAIL=? WHERE ID=?");
    stmt.setString(1, contact.getName().getFirstName());
    stmt.setString(2, contact.getName().getLastName());
    stmt.setString(3, contact.getAddress().getStreet());
    stmt.setString(4, contact.getAddress().getCity());
    stmt.setString(5, contact.getAddress().getState());
    stmt.setInt(6, contact.getAddress().getZip());
    stmt.setString(7, contact.getPhone());
    stmt.setString(8, contact.getEmail());
    stmt.setString(9, contact.getId().toString());
    stmt.execute();
    Utils.info("Contact has been updated: %s (%s)", contact.getId(), contact.getName());
  }

  /**
   * Gracefully closes the application
   */
  public void closeWindow() {
    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
  }

  /**
   * A singleton instance of the application
   */
  private static AddressBookApplication instance;

  /**
   * Gets the singleton instance of this application
   * @return The singleton instance of this application
   */
  public static AddressBookApplication getInstance() {
    return instance;
  }

}

package address.gui;

import address.AddressBook;
import address.AddressBookApplication;
import address.Utils;
import address.data.Address;
import address.data.AddressEntry;
import address.data.Name;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

/**
 * Dialog window allowing the user to update pre-existing entries in the remote database. Any successful updates will
 * be updated in the parent window.
 * @author Corneilious Eanes
 * @since March 15, 2021
 * @see CreateContactDialog
 */
public class UpdateContactDialog extends JDialog {
  // This window looks and functions almost exactly like CreateContactDialog, so any presumably confusing code will
  // probably be explained there.

  private static final int GAP_SIZE = 10;

  private MainPanel parent;
  private AddressEntry origEntry;
  private JTextField firstNameField;
  private JTextField lastNameField;
  private JTextField streetField;
  private JTextField cityField;
  private JTextField stateField;
  private JTextField zipField;
  private JTextField emailField;
  private JTextField phoneField;

  /**
   * Constructor for this dialog. Is automatically visible when a new instance is created.
   * @param parent The parent panel
   * @param entry The entry the user wants to update
   */
  public UpdateContactDialog(MainPanel parent, AddressEntry entry) {
    this.parent = parent;
    this.origEntry = entry;

    JLabel firstNameLabel = new JLabel("First name");
    firstNameField = new JTextField(origEntry.getName().getFirstName());
    JLabel lastNameLabel = new JLabel("Last name");
    lastNameField = new JTextField(origEntry.getName().getLastName());
    JLabel streetLabel = new JLabel("Street address");
    streetField = new JTextField(origEntry.getAddress().getStreet());
    JLabel cityLabel = new JLabel("City name");
    cityField = new JTextField(origEntry.getAddress().getCity());
    JLabel stateLabel = new JLabel("State code");
    stateField = new JTextField(origEntry.getAddress().getState());
    JLabel zipLabel = new JLabel("Zip code");
    zipField = new JTextField(Integer.toString(origEntry.getAddress().getZip()));
    JLabel emailLabel = new JLabel("Email address");
    emailField = new JTextField(origEntry.getEmail());
    JLabel phoneLabel = new JLabel("Phone number");
    phoneField = new JTextField(origEntry.getPhone());
    JButton updateButton = new JButton("Update");
    JButton cancelButton = new JButton("Cancel");

    JPanel main = new JPanel();
    GroupLayout layout = new GroupLayout(main);
    layout.setAutoCreateContainerGaps(true);
    layout.setHorizontalGroup(layout.createParallelGroup()
      .addComponent(firstNameLabel)
      .addComponent(firstNameField)
      .addComponent(lastNameLabel)
      .addComponent(lastNameField)
      .addComponent(streetLabel)
      .addComponent(streetField)
      .addComponent(cityLabel)
      .addComponent(cityField)
      .addComponent(stateLabel)
      .addComponent(stateField)
      .addComponent(zipLabel)
      .addComponent(zipField)
      .addComponent(phoneLabel)
      .addComponent(phoneField)
      .addComponent(emailLabel)
      .addComponent(emailField)
      .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addGap(GAP_SIZE * 4)
        .addComponent(cancelButton)
        .addGap(GAP_SIZE)
        .addComponent(updateButton)
      )
    );
    layout.setVerticalGroup(layout.createSequentialGroup()
      .addComponent(firstNameLabel)
      .addComponent(firstNameField)
      .addGap(GAP_SIZE)
      .addComponent(lastNameLabel)
      .addComponent(lastNameField)
      .addGap(GAP_SIZE)
      .addComponent(streetLabel)
      .addComponent(streetField)
      .addGap(GAP_SIZE)
      .addComponent(cityLabel)
      .addComponent(cityField)
      .addGap(GAP_SIZE)
      .addComponent(stateLabel)
      .addComponent(stateField)
      .addGap(GAP_SIZE)
      .addComponent(zipLabel)
      .addComponent(zipField)
      .addGap(GAP_SIZE)
      .addComponent(phoneLabel)
      .addComponent(phoneField)
      .addGap(GAP_SIZE)
      .addComponent(emailLabel)
      .addComponent(emailField)
      .addGap(GAP_SIZE)
      .addGroup(layout.createParallelGroup()
        .addComponent(cancelButton)
        .addComponent(updateButton)
      )
    );

    updateButton.addActionListener(e -> updateContactInformation());
    cancelButton.addActionListener(e -> closeDialog());

    main.setLayout(layout);
    setContentPane(main);
    setTitle("Update contact");
    pack();
    setVisible(true);
  }

  /**
   * Gracefully closes this dialog
   */
  private void closeDialog() {
    dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
  }

  /**
   * Attempts to update the specified contact given the information provided by the user via the text fields. The
   * contact information (both local and remote) will not be updated if one of the following conditions are reached:
   * <ul>
   *   <li>The user did not provide information sufficient for a valid contact</li>
   *   <li>No changes are detected between the original contact and the user-provided information</li>
   *   <li>Attempting to update the remote database results in an exception being thrown</li>
   * </ul>
   * If any of these conditions are reached, the user is notified about it via a message dialog, and more details about
   * the condition are printed to the console. Otherwise, both remote and local databases are successfully updated and
   * then dialog automatically closes.
   * @see #createContact()
   */
  private void updateContactInformation() {
    AddressEntry updatedEntry;
    try {
      updatedEntry = createContact();
    } catch (IllegalArgumentException e) {
      JOptionPane.showMessageDialog(this, e.getMessage(), "Could not update contact", JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (updatedEntry.equals(origEntry)) {
      JOptionPane.showMessageDialog(this, "No changes detected. Database has not been updated.", "No changes detected", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    try {
      AddressBookApplication.getInstance().updateContact(updatedEntry);
    } catch (SQLException e) {
      Utils.error(e, "Could not update contact information for %s (%s)", updatedEntry.getName(), updatedEntry.getId());
      JOptionPane.showMessageDialog(this, "Could not update contact in remote database! Check the console for more details.", "Could not update contact", JOptionPane.ERROR_MESSAGE);
      return;
    }
    // there is no update method in AddressBook, so just delete and add the contact instead
    AddressBook book = AddressBookApplication.getInstance().getBook();
    book.remove(updatedEntry.getId());
    book.add(updatedEntry);
    // update contacts list in parent
    parent.displayContacts();
    closeDialog();
  }

  /**
   * Creates a new address entry instance based on the information the user provided via the text fields.
   * @return A contact based on the user-provided information
   * @throws IllegalArgumentException If the user-provided information is deemed invalid
   * @see Utils#validateAddressEntry(AddressEntry)
   */
  private AddressEntry createContact() throws IllegalArgumentException {
    int zip;
    try {
      zip = Integer.parseInt(zipField.getText());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Zip code must be 5 numerical digits");
    }
    AddressEntry entry = new AddressEntry(origEntry.getId(),
      new Name(
        firstNameField.getText(),
        lastNameField.getText()
      ),
      new Address(
        streetField.getText(),
        cityField.getText(),
        stateField.getText(),
        zip
      ),
      phoneField.getText(),
      emailField.getText()
    );
    Utils.validateAddressEntry(entry);
    return entry;
  }

}

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
 */
public class UpdateContactDialog extends JDialog {

  private static final int GAP_SIZE = 10;

  private MainPanel parent;
  private JPanel main;
  private AddressEntry origEntry;
  private JTextField firstNameField;
  private JTextField lastNameField;
  private JTextField streetField;
  private JTextField cityField;
  private JTextField stateField;
  private JTextField zipField;
  private JTextField emailField;
  private JTextField phoneField;
  private JButton updateButton;
  private JButton cancelButton;

  public UpdateContactDialog(MainPanel parent, AddressEntry entry) {
    this.parent = parent;
    this.origEntry = entry;
    main = new JPanel();
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

  private void closeDialog() {
    dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
  }

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
    AddressBook book = AddressBookApplication.getInstance().getBook();
    book.remove(updatedEntry.getId());
    book.add(updatedEntry);
    // update contacts list in parent
    parent.displayContacts();
    closeDialog();
  }

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

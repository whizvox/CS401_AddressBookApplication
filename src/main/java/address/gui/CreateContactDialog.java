package address.gui;

import address.AddressBookApplication;
import address.Utils;
import address.data.Address;
import address.data.AddressEntry;
import address.data.Name;

import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * @author Corneilious Eanes
 * @since March 15, 2021
 */
public class CreateContactDialog extends JDialog {

  private static final int GAP_SIZE = 10;

  private MainPanel parent;
  private JPanel main;
  private JTextField firstNameField;
  private JTextField lastNameField;
  private JTextField streetField;
  private JTextField cityField;
  private JTextField stateField;
  private JTextField zipField;
  private JTextField emailField;
  private JTextField phoneField;

  public CreateContactDialog(MainPanel parent) {
    this.parent = parent;
    main = new JPanel();

    JLabel firstNameLabel = new JLabel("First name");
    firstNameField = new JTextField();
    JLabel lastNameLabel = new JLabel("Last name");
    lastNameField = new JTextField();
    JLabel streetLabel = new JLabel("Street address");
    streetField = new JTextField();
    JLabel cityLabel = new JLabel("City name");
    cityField = new JTextField();
    JLabel stateLabel = new JLabel("State code");
    stateField = new JTextField();
    JLabel zipLabel = new JLabel("Zip code");
    zipField = new JTextField();
    JLabel emailLabel = new JLabel("Email address");
    emailField = new JTextField();
    JLabel phoneLabel = new JLabel("Phone number");
    phoneField = new JTextField();
    JButton addButton = new JButton("Add");
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
        .addComponent(addButton)
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
        .addComponent(addButton)
      )
    );

    addButton.addActionListener(e -> addContact());
    cancelButton.addActionListener(e -> closeDialog());

    main.setLayout(layout);
    setContentPane(main);

    setTitle("Create contact");
    pack();
    setVisible(true);
  }

  private void closeDialog() {
    dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
  }

  private void addContact() {
    try {
      AddressEntry contact = createContact();
      AddressBookApplication.getInstance().addContact(contact);
      parent.displayContacts();
      closeDialog();
    } catch (IllegalArgumentException e) {
      JOptionPane.showMessageDialog(this, e.getMessage(), "Could not add contact!", JOptionPane.ERROR_MESSAGE);
    }
  }

  private AddressEntry createContact() throws IllegalArgumentException {
    int zip;
    try {
      zip = Integer.parseInt(zipField.getText());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Zip code must be a 5 digit number");
    }
    AddressEntry entry = new AddressEntry(
      null,
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

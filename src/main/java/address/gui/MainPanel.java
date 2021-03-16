package address.gui;

import address.AddressBookApplication;
import address.data.AddressEntry;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

/**
 * The landing page of sorts for the application. Gives access to all other parts of the application.
 * @author Corneilious Eanes
 * @author Mike Langdon
 * @since March 15, 2021
 */
public class MainPanel extends JPanel {

  private JTextArea contactInfoArea;
  private JList<String> displayList;
  private ArrayList<UUID> entryIds;

  /**
   * The constructor for this panel. Needs to be attached to a window of some sort, preferably a {@link JFrame}.
   */
  public MainPanel() {
    JButton displayButton = new JButton("Display");
    JButton newButton = new JButton("New");
    JButton updateButton = new JButton("Update");
    JButton findButton = new JButton("Find");
    JButton removeButton = new JButton("Remove");
    JButton exitButton = new JButton("Exit");
    contactInfoArea = new JTextArea();
    contactInfoArea.setEditable(false);
    JScrollPane contactInfoPane = new JScrollPane(contactInfoArea);

    displayList = new JList<>();
    displayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane displayPane = new JScrollPane(displayList);

    entryIds = new ArrayList<>();

    // add action listeners
    displayButton.addActionListener(e -> displayContacts());
    newButton.addActionListener(e -> new CreateContactDialog(this));
    updateButton.addActionListener(e -> updateContact());
    findButton.addActionListener(e -> new FindContactDialog());
    removeButton.addActionListener(e -> removeContact());
    exitButton.addActionListener(e -> AddressBookApplication.getInstance().closeWindow());
    displayList.addListSelectionListener(e -> updateContactInfoArea());

    GroupLayout layout = new GroupLayout(this);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup()
      .addGroup(layout.createSequentialGroup()
        .addComponent(displayPane, 75, 100, 300)
        .addComponent(contactInfoPane, 100, 150, 500)
      )
      .addGroup(layout.createSequentialGroup()
        .addComponent(displayButton)
        .addComponent(newButton)
        .addComponent(updateButton)
        .addComponent(findButton)
        .addComponent(removeButton)
        .addComponent(exitButton)
      )
    );
    layout.setVerticalGroup(layout.createSequentialGroup()
      .addGroup(layout.createParallelGroup()
        .addComponent(displayPane)
        .addComponent(contactInfoPane)
      )
      .addGroup(layout.createParallelGroup()
        .addComponent(displayButton)
        .addComponent(newButton)
        .addComponent(updateButton)
        .addComponent(findButton)
        .addComponent(removeButton)
        .addComponent(exitButton)
      )
    );
    setPreferredSize(new Dimension(500, 300));

    displayContacts();
  }

  /**
   * Queries the local database and refreshes the entries in the GUI's list of contacts.
   */
  public void displayContacts() {
    Vector<String> entryNames = new Vector<>();
    entryIds.clear();
    AddressBookApplication.getInstance().getBook().find("").forEach(entry -> {
      entryNames.add(entry.getName().toString());
      entryIds.add(entry.getId());
    });
    displayList.setListData(entryNames);
  }

  /**
   * Create a {@link AddressEntry} based on what the user selected in the list.
   * @return An address entry corresponding to what the user selected, or <code>null</code> if either the corresponding
   *         ID does not exist or if nothing is selected.
   */
  private AddressEntry getSelectedEntry() {
    int selected = displayList.getSelectedIndex();
    if (selected != -1) {
      UUID selectedId = entryIds.get(selected);
      return AddressBookApplication.getInstance().getBook().get(selectedId);
    }
    return null;
  }

  /**
   * Will update the contact info text area whenever the user selects an entry in the list.
   */
  private void updateContactInfoArea() {
    AddressEntry entry = getSelectedEntry();
    if (entry != null) {
      contactInfoArea.setText(entry.toString().replace("\t", ""));
    }
  }

  /**
   * Creates an instance of {@link UpdateContactDialog}, allowing the user to update a selected entry.
   */
  private void updateContact() {
    AddressEntry entry = getSelectedEntry();
    if (entry != null) {
      new UpdateContactDialog(this, entry);
    }
  }

  /**
   * Allows the user to remove a selected contact, first prompting them with a confirmation message box.
   */
  private void removeContact() {
    AddressEntry entry = getSelectedEntry();
    if (entry != null) {
      int result = JOptionPane.showConfirmDialog(this, "Are you sure you wish to delete the contact information for " +
        entry.getName().toString() + "?", "Confirm deletion", JOptionPane.YES_NO_OPTION);
      if (result == 0) {
        AddressBookApplication.getInstance().removeContact(entry.getId());
        JOptionPane.showMessageDialog(this, "Contact deleted", "Contact deleted", JOptionPane.INFORMATION_MESSAGE);
        displayContacts();
      }
    }
  }

}

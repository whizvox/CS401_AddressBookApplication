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

    displayButton.addActionListener(e -> displayContacts());
    newButton.addActionListener(e -> new CreateContactDialog(this));
    updateButton.addActionListener(e -> updateContact());
    findButton.addActionListener(e -> new FindContactDialog(this));
    removeButton.addActionListener(e -> removeContact());
    exitButton.addActionListener(e -> exitApplication());
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

  public void displayContacts() {
    Vector<String> entryNames = new Vector<>();
    entryIds.clear();
    AddressBookApplication.getInstance().getBook().find("").forEach(entry -> {
      entryNames.add(entry.getName().toString());
      entryIds.add(entry.getId());
    });
    displayList.setListData(entryNames);
  }

  private AddressEntry getSelectedEntry() {
    int selected = displayList.getSelectedIndex();
    if (selected != -1) {
      UUID selectedId = entryIds.get(selected);
      return AddressBookApplication.getInstance().getBook().get(selectedId);
    }
    return null;
  }

  private void updateContactInfoArea() {
    AddressEntry entry = getSelectedEntry();
    if (entry != null) {
      contactInfoArea.setText(entry.toString().replace("\t", ""));
    }
  }

  private void updateContact() {
    AddressEntry entry = getSelectedEntry();
    if (entry != null) {
      new UpdateContactDialog(this, entry);
    }
  }

  private void removeContact() {
    AddressEntry entry = getSelectedEntry();
    if (entry != null) {
      int result = JOptionPane.showConfirmDialog(this, "Are you sure you wish to delete the contact information for " + entry.getName().toString() + "?", "Confirm deletion", JOptionPane.YES_NO_OPTION);
      if (result == 0) {
        AddressBookApplication.getInstance().removeContact(entry.getId());
        JOptionPane.showMessageDialog(this, "Contact deleted", "Contact deleted", JOptionPane.INFORMATION_MESSAGE);
        displayContacts();
      }
    }
  }

  private void exitApplication() {
    // Gracefully closes the window
    AddressBookApplication.getInstance().closeWindow();
  }

}

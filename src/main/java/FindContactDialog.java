import javax.swing.*;
import java.util.InputMismatchException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.UUID;

public class FindContactDialog extends JDialog {
    private MainPanel parent;
    private JPanel main;
    private JTextField findField;
    private JButton searchButton;
    private JButton cancelButton;

    public FindContactDialog(MainPanel parent) {
        this.parent = parent;
        main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.add(new JLabel("Lookup Last Name: "));
        main.add(findField = new JTextField());
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getInputEntry();
            }
        });
        cancelButton.addActionListener(e -> closeDialog());
    }

    public String getInputEntry() {
        // check to see if input last name matches any of the entries
         String inputLastName = findField.getText();
        if(findField.getText().length() > 0){
            AddressBookApplication.getInstance().getBook().get(UUID.fromString(inputLastName));

        }
        return ;
    }

    private void closeDialog() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

}

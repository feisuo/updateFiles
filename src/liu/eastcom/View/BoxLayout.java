package liu.eastcom.View;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.GridLayout;

public class BoxLayout extends JPanel {
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private JButton btnNewButton_2;

	/**
	 * Create the panel.
	 */
	public BoxLayout() {
		setLayout(new GridLayout(2, 2, 0, 0));
		
		btnNewButton = new JButton("New button");
		add(btnNewButton);
		
		btnNewButton_2 = new JButton("New button");
		add(btnNewButton_2);
		
		btnNewButton_1 = new JButton("New button");
		add(btnNewButton_1);

	}
}

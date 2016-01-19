package assign3;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
public class mn extends JFrame{
	public mn() {
		super("Sudoku Solver");
		setLayout(new BorderLayout(4, 4));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addNorthButtons();
		pack();
		setVisible(true);
	}
	public static void main(String[] args) {
		mn m=new mn();
		
	}


/**
 * This class is responsible for detecting when the buttons are clicked or
 * interactors are used, so you will have to add code to respond to these
 * actions.
 */
public void actionPerformed(ActionEvent e) {
	
	if (e.getSource() == add) {
		System.out.println("adsaf");
		
	} else if ((e.getSource() == delete||e.getSource()==text) && !text.getText().equals("")) {
		
	} else if ((e.getSource() == lookup ||e.getSource()==text)&& !text.getText().equals("")) {
		
	} else if ((e.getSource() == changeStatus||e.getSource()==status)&&!status.getText().equals("")) {
		
	} else if ((e.getSource() == changePicture||e.getSource()==picture)&& !picture.getText().equals("")) {
		
	} else if ((e.getSource() == addFriend ||e.getSource()==friend)&& !friend.getText().equals("")) {
		
	} else if ((e.getSource() == addAge||e.getSource()==age) && !age.getText().equals("")) {
		
	} else if (e.getSource() == male) {
		
	} else if (e.getSource() == female ) {
		
	}
	else if((e.getSource()==remove||e.getSource()==text)&&text.getText()!=""){		
	}

}


private void addNorthButtons(){
	add = new JButton("Add");
	delete = new JButton("Delete");
	lookup = new JButton("Lookup");
	remove = new JButton("Remove Friend");
	add.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {
				System.out.println("adsaf");
		}
	});
	add(add,BorderLayout.NORTH);
	
}




private JButton add, delete, lookup, changeStatus, changePicture,
		addFriend, addAge, male, female, remove;
private JTextField text, status, picture, friend, age;


}


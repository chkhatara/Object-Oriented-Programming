package assign3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


 @SuppressWarnings("serial")
public class SudokuFrame extends JFrame {
	 JTextArea puzzleText;
	 JTextArea solutionText;
	 JCheckBox autoCheck;
	public SudokuFrame() {
		super("Sudoku Solver");
		setLayout(new BorderLayout(4, 4));
		addTexts();
		addBox();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void addTexts(){
		puzzleText = new JTextArea(15, 30);
		puzzleText.setBorder(new TitledBorder("Puzzle"));		
		puzzleText.getDocument().addDocumentListener(new PuzzleTextListener());	
		solutionText = new JTextArea(15, 30);
		solutionText.setBorder(new TitledBorder("Solution"));
		solutionText.setEditable(false);
		add(puzzleText, BorderLayout.CENTER);
		add(solutionText, BorderLayout.EAST);;	
	}
	
	private void addBox(){
		Box controlBox = Box.createHorizontalBox();
		JButton check = new JButton("Check");
		check.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				solveSudoku();
			}
		});
		controlBox.add(check);
		autoCheck = new JCheckBox("Auto Check");
		autoCheck.setSelected(true);
		controlBox.add(check);
		controlBox.add(autoCheck);	
		add(controlBox,BorderLayout.SOUTH);
	}
	
	class PuzzleTextListener implements DocumentListener {		 
	    public void insertUpdate(DocumentEvent e) {
	    	if (autoCheck.isSelected())
	    		solveSudoku();
	    }
	    public void removeUpdate(DocumentEvent e) {
	    	if (autoCheck.isSelected())
	    		solveSudoku();
	    }
	    public void changedUpdate(DocumentEvent e) {
	    	if (autoCheck.isSelected())
	    		solveSudoku();
	    }	    
	}
	
	private void solveSudoku(){
		try {
			int [][] textGrid = Sudoku.textToGrid(puzzleText.getText());
			Sudoku sudoku=new Sudoku(textGrid);
			int solve=sudoku.solve();
			if(solve>0){
				solutionText.setText(sudoku.getSolutionText());
				solutionText.append("solutions:" + solve + "\n");
				solutionText.append("elapsed:" + sudoku.getElapsed() + "ms\n");
			}
		} catch (Exception ParseError) {
			solutionText.setText("Parsing problem");
		}		
	}
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		@SuppressWarnings("unused")
		SudokuFrame frame = new SudokuFrame();
	}

}

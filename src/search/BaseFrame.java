package search;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;


public abstract class BaseFrame extends JFrame {

	public BaseFrame() {
		returnValue = 1;
		centerPane = new JPanel();
	}

	protected void initComponents() {
		setContentPane(centerPane);
		centerPane.setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));
		centerPane.setLayout(new BorderLayout(0, 4));
		applyClosingAction();
		applyEscapeAction();
	}

	public JPanel getCenterPane() {
		return centerPane;
	}

	public void setCenterPane(JPanel centerPane) {
		this.centerPane = centerPane;
	}

	public JButton getOKButton() {
		return okButton;
	}

	protected JPanel createControlButtonPane() {
		JPanel controlPane = new JPanel();
		controlPane.setLayout(new BorderLayout());
		JPanel buttonsPane = new JPanel();
		controlPane.add(buttonsPane, "East");
		buttonsPane.setLayout(new GridLayout(1, 2, 4, 0));
		okButton = new JButton("确定");
		okButton.setMnemonic('O');
		buttonsPane.add(okButton);
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				doOK();
			}

		});
		JButton cancelButton = new JButton("取消");
		cancelButton.setMnemonic('C');
		buttonsPane.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				doCancel();
			}

		});
		getRootPane().setDefaultButton(okButton);
		return controlPane;
	}

	protected void doOK() {
		try {
			checkValid();
		} catch (Exception exp) {
			JOptionPane.showMessageDialog(this, exp.getMessage());
			return;
		}
		setReturnValue(0);
		dialogExit();
	}

	protected void doCancel() {
		setReturnValue(1);
		dialogExit();
	}

	protected void doHelp() {
	}

	public void checkValid() throws Exception {
	}

	public void dialogExit() {
		setVisible(false);
		dispose();
	}

	protected void applyClosingAction() {
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				doCancel();
			}

		});
	}

	protected void applyEscapeAction() {
		InputMap inputMapAncestor = centerPane.getInputMap(1);
		ActionMap actionMap = centerPane.getActionMap();
		inputMapAncestor.put(KeyStroke.getKeyStroke(27, 0), "dialogExit");
		actionMap.put("dialogExit", new AbstractAction() {

			public void actionPerformed(ActionEvent evt) {
				doCancel();
			}

		});
	}

	public int getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(int returnValue) {
		this.returnValue = returnValue;
	}

	private int returnValue;

	private JPanel centerPane;

	private JButton okButton;
}


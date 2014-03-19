package gui;

import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

/**
 * A container of buttons for player options in Blackjack: Hit, Stand, Double, 
 * Split, Surrender.
 * 
 * @author Vance Zuo
 */
public class ChoicePanel extends JPanel {

	private JButton hit = new JButton("Hit");
	private JButton stand = new JButton("Stand");
	private JButton dbl = new JButton("Double");	
	private JButton split = new JButton("Split");
	private JButton surrender = new JButton("Surrender");

	/**
	 * Makes a choice panel with the above buttons
	 */
	public ChoicePanel() {
		super();	
		this.setOpaque(false);
		Color gold = new Color(197,179,88);
		hit.setBackground(gold);
		stand.setBackground(gold);
		dbl.setBackground(gold);
		split.setBackground(gold);
		surrender.setBackground(gold);
		add(hit);
		add(stand);
		add(dbl);
//		add(split); // Unimplemented
		add(surrender);
	}

	/**
	 * Enables hit button.
	 */
	public void enableHit() {
		hit.setEnabled(true);
	}

	/**
	 * Enables stand button.
	 */
	public void enableStand() {
		stand.setEnabled(true);
	}

	/**
	 * Enables double button.
	 */
	public void enableDouble() {
		dbl.setEnabled(true);
	}

	/**
	 * Enables split button.
	 */
	public void enableSplit() {
		split.setEnabled(true);
	}

	/**
	 * Enables surrender button.
	 */
	public void enableSurrender() {
		surrender.setEnabled(true);
	}

	/**
	 * Disables hit button.
	 */
	public void disableHit() {
		hit.setEnabled(false);
	}

	/**
	 * Disables stand button.
	 */
	public void disableStand() {
		stand.setEnabled(false);
	}

	/**
	 * Disables double button.
	 */
	public void disableDouble() {
		dbl.setEnabled(false);
	}

	/**
	 * Disables split button.
	 */
	public void disableSplit() {
		split.setEnabled(false);
	}

	/**
	 * Disables surrender button.
	 */
	public void disableSurrender() {
		surrender.setEnabled(false);
	}

	/**
	 * Adds a listener for these buttons. The listener should contain responses
	 * for each of these button commands.
	 * @param a listener of these buttons
	 */
	public void addListener(ActionListener a) {
		hit.addActionListener(a);
		stand.addActionListener(a);
		dbl.addActionListener(a);
		split.addActionListener(a);
		surrender.addActionListener(a);
	}
}

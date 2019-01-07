package com.hit.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CacheUnitView {

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	JFrame frmMmuProject = new JFrame();
	private JFileChooser OpenFileChooser = new JFileChooser();;
	public String filePath;
	JTextPane msgToDisplay = new JTextPane();
	private JLabel fileFild;
	private JProgressBar progressBar = new JProgressBar();

	public CacheUnitView() {
		initialize();
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}

	public void start() {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				initialize();
			}
		});

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		progressBar.setVisible(false);
		fileFild = new JLabel();
		fileFild.setFont(new Font("Dialog", Font.BOLD, 17));
		String defaoultDir = new String(System.getProperty("user.dir"));
		frmMmuProject.getContentPane().setBackground(Color.LIGHT_GRAY);
		frmMmuProject.setForeground(new Color(0, 0, 102));
		frmMmuProject.getContentPane().setForeground(Color.GRAY);
		frmMmuProject.setBounds(100, 100, 841, 698);
		frmMmuProject.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		msgToDisplay.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		msgToDisplay.setForeground(Color.BLACK);
		msgToDisplay.setBackground(UIManager.getColor("Button.background"));

		frmMmuProject.setTitle("MMU Project Ilya & Ronald");
		JButton OpenFileButton = new JButton("Load File");
		OpenFileButton.setIcon(new ImageIcon(defaoultDir + "\\src\\main\\resources\\send-file.png"));
		OpenFileButton.setFont(new Font("Tw Cen MT", Font.BOLD | Font.ITALIC, 22));
		OpenFileButton.setForeground(new Color(0, 0, 153));
		OpenFileChooser.setCurrentDirectory(new File(defaoultDir + "\\src\\main\\resources"));
		OpenFileChooser.setFileFilter(new FileNameExtensionFilter("json", "json"));
		filePath = new String();

		OpenFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnValue = OpenFileChooser.showOpenDialog(OpenFileChooser);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					filePath = OpenFileChooser.getSelectedFile().getAbsolutePath();
					progressBar.setVisible(true);
					progressBar.setValue(100);
					fileFild.setVisible(true);
					fileFild.setText("file opened successfully");
					pcs.firePropertyChange(filePath, null, " ");

					System.out.println(
							"You chose to open this directory: " + OpenFileChooser.getSelectedFile().getAbsolutePath());
				} else {
					progressBar.setVisible(true);
					progressBar.setValue(0);
					fileFild.setVisible(true);
					fileFild.setText("Failed to open file");
					msgToDisplay.setText("no file chosen");
				}
			}
		});

		JButton btnShowStatistics = new JButton("Show Statistics");
		btnShowStatistics.setIcon(new ImageIcon(defaoultDir + "\\src\\main\\resources\\statistics.png"));
		btnShowStatistics.setFont(new Font("Tw Cen MT", Font.BOLD | Font.ITALIC, 20));
		btnShowStatistics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				progressBar.setVisible(false);
				fileFild.setText("Statistics polled successfully");
				pcs.firePropertyChange("s", null, " ");
			}
		});

		btnShowStatistics.setForeground(Color.BLUE);
		progressBar.setStringPainted(true);
		GroupLayout groupLayout = new GroupLayout(frmMmuProject.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(msgToDisplay, GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(OpenFileButton, GroupLayout.PREFERRED_SIZE, 473, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(btnShowStatistics, GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(fileFild, GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(13)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(OpenFileButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGap(25))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnShowStatistics)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addComponent(msgToDisplay, GroupLayout.PREFERRED_SIZE, 394, GroupLayout.PREFERRED_SIZE)
					.addGap(33)
					.addComponent(fileFild)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(141, Short.MAX_VALUE))
		);
		frmMmuProject.getContentPane().setLayout(groupLayout);
		frmMmuProject.setVisible(true);
	}

	public <T> void updateUIData(T t) {
		msgToDisplay.setText((String) t);

	}
}
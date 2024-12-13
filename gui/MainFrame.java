package gui;

import java.awt.Checkbox;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import scrambler.Scrambler222;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener, ItemListener {

    private JTextField text;
    private Checkbox[] subGroups;
    private Checkbox[] olls;
    private JButton scramble;
    private Scrambler222 scrambler = new Scrambler222();

    public MainFrame() {
        initComponents();
        setupLayout();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    private void initComponents() {
        // TODO Auto-generated method stub
        subGroups = new Checkbox[3];
        subGroups[0] = new Checkbox("EG0(CLL)", true);
        subGroups[1] = new Checkbox("EG1", true);
        subGroups[2] = new Checkbox("EG2", true);

        olls = new Checkbox[8];
        olls[0] = new Checkbox("Pi", true);
        olls[1] = new Checkbox("H", true);
        olls[2] = new Checkbox("U", true);
        olls[3] = new Checkbox("T", true);
        olls[4] = new Checkbox("L", true);
        olls[5] = new Checkbox("Sune", true);
        olls[6] = new Checkbox("Anti-Sune", true);
        olls[7] = new Checkbox("None", false);
        for (int i = 0; i < olls.length; i++) {
            olls[i].addItemListener(this);
        }

        text = new JTextField();
        text.setFont(new Font("Times New Roman", Font.BOLD, 24));
        text.setEditable(false);

        scramble = new JButton("´òÂÒ");
        scramble.setActionCommand("scramble");
        scramble.addActionListener(this);
    }

    private void setupLayout() {
        this.setResizable(false);
        this.setBounds(300, 300, 400, 200);
        this.setTitle("EGÑµÁ·Æ÷");

        Box box = Box.createVerticalBox();
        JPanel subGroupPanel = new JPanel(new FlowLayout());
        JPanel ollsPanel = new JPanel(new FlowLayout());

        for (int i = 0; i < subGroups.length; i++) {
            subGroupPanel.add(subGroups[i]);
        }
        for (int i = 0; i < olls.length; i++) {
            ollsPanel.add(olls[i]);
        }

        box.add(subGroupPanel);
        box.add(ollsPanel);
        box.add(text);
        box.add(scramble);

        this.add(box);
    }

    private String scramble() {
        int subGroupsChoosed = 0;
        String ollsChoosed = "";
        for (int i = 0; i < subGroups.length; i++) {
            if (subGroups[i].getState() == true) {
                subGroupsChoosed |= (int) (Math.pow(2, i) + 0.00001);
            }
        }
        if (olls[olls.length - 1].getState() == true) {
            ollsChoosed = "N";
        } else {
            for (int i = 0; i < olls.length - 1; i++) {
                if (olls[i].getState() == true) {
                    ollsChoosed += "PHUTLSAN".charAt(i);
                }
            }
        }
        if (ollsChoosed == "") {
            ollsChoosed = "X";
        }
        // System.out.println(subGroupsChoosed);
        // System.out.println(ollsChoosed);
        return scrambler.scrambleEG(subGroupsChoosed, ollsChoosed);
    }

    public static void main(String args[]) {
        new MainFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getActionCommand().equals("scramble")) {
            text.setText(scramble());
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        // TODO Auto-generated method stub
        Checkbox src = (Checkbox) e.getSource();
        if (src.getLabel() == olls[olls.length - 1].getLabel()) {
            if (src.getState() == true) {
                for (int i = 0; i < olls.length - 1; i++) {
                    olls[i].setState(false);
                }
            }
        } else {
            if (src.getState() == true) {
                olls[olls.length - 1].setState(false);
            }
        }
    }
}

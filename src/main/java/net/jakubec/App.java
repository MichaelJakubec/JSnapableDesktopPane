package net.jakubec;

import net.jakubec.snap.JSnapableDesktopPane;

import javax.swing.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ){
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setSize(800, 600);
        JDesktopPane pane = new JSnapableDesktopPane();

        JInternalFrame a = new JInternalFrame("A", true, true, true, true);
        pane.add(a);
        a.setSize(100,200);
        a.setVisible(true);
        JInternalFrame b = new JInternalFrame("B", true, true, true, true);
        b.setSize(200,100);
        pane.add(b);
        b.setVisible(true);

        f.setContentPane(pane);
        f.setVisible(true);
    }
}

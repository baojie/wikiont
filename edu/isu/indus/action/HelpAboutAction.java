package edu.isu.indus.action;

import java.awt.event.*;
import javax.swing.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Jie Bao</p>
 * @author Jie Bao
 * @version 1.0
 */

public class HelpAboutAction
    extends AbstractAction {
    public HelpAboutAction() {
        putValue(Action.NAME, "About...");
    }

    public void actionPerformed(ActionEvent e) { //{{{
        JOptionPane.showMessageDialog(null,
                                      "XML document editor\n" +
                                      "Version 0.0.6\n\n" +

                                      "Jie Bao\n" +
                                      "2003-10-04\n" +
                                      "Iowa State University\n" +
                                      "baojie@iastate.edu\n\n" +

            "This software is based on the following open source software:\n" +
            " - jsXe of Ian Lewis"
            );
    } //}}}

}
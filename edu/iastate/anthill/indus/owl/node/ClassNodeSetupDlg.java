package edu.iastate.anthill.indus.owl.node;

import java.awt.Frame;

public class ClassNodeSetupDlg
    extends DefaultOntologyNodeSetupDlg
{

    public ClassNodeSetupDlg(Frame frame, String title, String nameStr,
                             String packageStr, String commentStr)
    {
        super(frame, title, nameStr, packageStr, commentStr);
        pack();
    }

}

/**
 *
 * <p>Title: XEditor: a XML document viewer and editor</p>
 * <p>Description: XEditor</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Iowa State University</p>
 * @author Jie Bao
 * @version 1.0
 */

package edu.isu.indus.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import edu.isu.indus.gui.view.ChildFrame;

public class ViewRefreshAction
    extends AbstractAction {

    public void actionPerformed(ActionEvent e) {
//        Debug.trace("ViewRefreshAction::actionPerformed" + (view != null));
        view.refreshAction(e);
//        Debug.trace("Not implemented !");
    }

    public ViewRefreshAction(ChildFrame parent) { //{{{
        putValue(Action.NAME, "Refresh Tree");
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl R"));
        view = parent;
    } //}}}

//{{{ Private members
    private ChildFrame view;

}

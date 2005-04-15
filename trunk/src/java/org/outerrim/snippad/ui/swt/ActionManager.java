/**
 * 
 */
package org.outerrim.snippad.ui.swt;

import org.eclipse.jface.action.IAction;
import org.outerrim.snippad.ui.swt.actions.AboutAction;
import org.outerrim.snippad.ui.swt.actions.DeleteWikiWordAction;
import org.outerrim.snippad.ui.swt.actions.ExitAction;
import org.outerrim.snippad.ui.swt.actions.MoveWordBackAction;
import org.outerrim.snippad.ui.swt.actions.MoveWordDownAction;
import org.outerrim.snippad.ui.swt.actions.MoveWordForwardAction;
import org.outerrim.snippad.ui.swt.actions.MoveWordUpAction;
import org.outerrim.snippad.ui.swt.actions.NewWikiAction;
import org.outerrim.snippad.ui.swt.actions.NewWikiWordAction;
import org.outerrim.snippad.ui.swt.actions.OpenAction;
import org.outerrim.snippad.ui.swt.actions.PreferencesAction;
import org.outerrim.snippad.ui.swt.actions.PrintAction;
import org.outerrim.snippad.ui.swt.actions.RenameWordAction;
import org.outerrim.snippad.ui.swt.actions.SaveAsWikiAction;
import org.outerrim.snippad.ui.swt.actions.SaveWikiAction;
import org.outerrim.snippad.ui.swt.actions.ShowEditorAction;

/**
 * @author <a href="mailto:sithlord@berlios.de">Michael Osterlie</a>
 *
 */
public class ActionManager {
    static private NewWikiAction actionNew;
    static private OpenAction actionOpen;
    static private SaveWikiAction actionSaveWiki;
    static private SaveAsWikiAction actionSaveAsWiki;
    static private PrintAction actionPrint;
    static private ExitAction actionExit;
    static private ShowEditorAction actionEdit;
    static private NewWikiWordAction actionNewWikiWord;
    static private DeleteWikiWordAction actionDeleteWikiWord;
    static private RenameWordAction actionRenameWord;
    static private MoveWordUpAction actionMoveUp;
    static private MoveWordDownAction actionMoveDown;
    static private MoveWordBackAction actionMoveBack;
    static private MoveWordForwardAction actionMoveForward;
    static private PreferencesAction actionPreferences;
    static private AboutAction actionAbout;

    static {
        actionNew = new NewWikiAction();
        actionOpen = new OpenAction();
        actionSaveAsWiki = new SaveAsWikiAction();
        actionSaveWiki = new SaveWikiAction( actionSaveAsWiki );
        actionPrint = new PrintAction();
        actionExit = new ExitAction();
        actionEdit = new ShowEditorAction();
        actionNewWikiWord = new NewWikiWordAction();
        actionDeleteWikiWord = new DeleteWikiWordAction();
        actionRenameWord = new RenameWordAction();
        actionMoveUp = new MoveWordUpAction();
        actionMoveDown = new MoveWordDownAction();
        actionMoveBack = new MoveWordBackAction();
        actionMoveForward = new MoveWordForwardAction();
        actionPreferences = new PreferencesAction();
        actionAbout = new AboutAction();
        
        actionSaveWiki.setEnabled( false );
        actionSaveAsWiki.setEnabled( false );
        actionPrint.setEnabled( false );
        actionNewWikiWord.setEnabled( false );
        actionDeleteWikiWord.setEnabled( false );
        actionRenameWord.setEnabled( false );
        actionMoveUp.setEnabled( false );
        actionMoveDown.setEnabled( false );
        actionMoveBack.setEnabled( false );
        actionMoveForward.setEnabled( false );        
    }
    
    static public IAction getNewWikiAction() { return actionNew; }
    static public IAction getOpenAction() { return actionOpen; }
    static public IAction getSaveAsWikiAction() { return actionSaveAsWiki; }
    static public IAction getSaveWikiAction() { return actionSaveWiki; }
    static public IAction getPrintAction() { return actionPrint; }
    static public IAction getExitAction() { return actionExit; }
    static public IAction getEditAction() { return actionEdit; }
    static public IAction getNewWikiWordAction() { return actionNewWikiWord; }
    static public IAction getDeleteWikiWordAction() { return actionDeleteWikiWord; }
    static public IAction getRenameWordAction() { return actionRenameWord; }
    static public IAction getMoveUpAction() { return actionMoveUp; }
    static public IAction getMoveDownAction() { return actionMoveDown; }
    static public IAction getMoveBackAction() { return actionMoveBack; }
    static public IAction getMoveForwardAction() { return actionMoveForward; }
    static public IAction getPreferencesAction() { return actionPreferences; }
    static public IAction getAboutAction() { return actionAbout; }
    
    static public void registerSnipPad( SnipPad sp ) {
        actionOpen.setSnipPad( sp );
        actionExit.setSnipPad( sp );
        actionSaveWiki.setSnipPad( sp );
        actionSaveAsWiki.setSnipPad( sp );
        actionNewWikiWord.setSnipPad( sp );
        actionDeleteWikiWord.setSnipPad( sp );
        actionMoveBack.setSnipPad( sp );
        actionMoveDown.setSnipPad( sp );
        actionMoveForward.setSnipPad( sp );
        actionMoveUp.setSnipPad( sp );
        actionNew.setSnipPad( sp );
        actionRenameWord.setSnipPad( sp );
    }
}

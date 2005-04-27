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
public final class ActionManager {
    private static NewWikiAction actionNew;
    private static OpenAction actionOpen;
    private static SaveWikiAction actionSaveWiki;
    private static SaveAsWikiAction actionSaveAsWiki;
    private static PrintAction actionPrint;
    private static ExitAction actionExit;
    private static ShowEditorAction actionEdit;
    private static NewWikiWordAction actionNewWikiWord;
    private static DeleteWikiWordAction actionDeleteWikiWord;
    private static RenameWordAction actionRenameWord;
    private static MoveWordUpAction actionMoveUp;
    private static MoveWordDownAction actionMoveDown;
    private static MoveWordBackAction actionMoveBack;
    private static MoveWordForwardAction actionMoveForward;
    private static PreferencesAction actionPreferences;
    private static AboutAction actionAbout;

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

    public static IAction getNewWikiAction() { return actionNew; }
    public static IAction getOpenAction() { return actionOpen; }
    public static IAction getSaveAsWikiAction() { return actionSaveAsWiki; }
    public static IAction getSaveWikiAction() { return actionSaveWiki; }
    public static IAction getPrintAction() { return actionPrint; }
    public static IAction getExitAction() { return actionExit; }
    public static IAction getEditAction() { return actionEdit; }
    public static IAction getNewWikiWordAction() { return actionNewWikiWord; }
    public static IAction getDeleteWikiWordAction() {
        return actionDeleteWikiWord;
    }
    public static IAction getRenameWordAction() { return actionRenameWord; }
    public static IAction getMoveUpAction() { return actionMoveUp; }
    public static IAction getMoveDownAction() { return actionMoveDown; }
    public static IAction getMoveBackAction() { return actionMoveBack; }
    public static IAction getMoveForwardAction() { return actionMoveForward; }
    public static IAction getPreferencesAction() { return actionPreferences; }
    public static IAction getAboutAction() { return actionAbout; }

    /**
     * @param sp SnipPad instance
     */
    public static void registerSnipPad( final SnipPad sp ) {
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

    private ActionManager() { }
}

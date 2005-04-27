package org.outerrim.snippad.ui.swt.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.outerrim.snippad.data.serialize.SerializeException;
import org.outerrim.snippad.data.serialize.XmlSerializer;
import org.outerrim.snippad.ui.swt.SnipPad;

/**
 * @author <a href="mailto:sithlord@berlios.de">Michael Osterlie</a>
 */
public class RecentDocumentAction extends SnipPadBaseAction {
    private String displayFile;
    private String filename;

    private static final Log LOG = LogFactory.getLog(
            RecentDocumentAction.class );

    /**
     * @param f filename
     */
    public RecentDocumentAction( final String f ) {
        filename = f;

        int index = filename.lastIndexOf( File.separator );
        displayFile = filename.substring( index + 1, filename.length() );

        setText( displayFile );
        setToolTipText( "Open " + filename );
    }

    public String getId() {
        return filename;
    }

    /**
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run() {
        try {
            snippad.openWiki(
                    (new XmlSerializer()).load(
                            new FileInputStream( filename ) ),
                    filename );
        } catch( IOException e ) {
            SnipPad.logError( e.getMessage(), e );
        } catch( SerializeException e ) {
            SnipPad.logError( e.getMessage(), e );
        }
    }
}

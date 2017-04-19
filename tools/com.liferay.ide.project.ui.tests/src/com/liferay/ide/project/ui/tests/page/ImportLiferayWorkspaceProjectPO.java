
package com.liferay.ide.project.ui.tests.page;

import org.eclipse.swtbot.swt.finder.SWTBot;

import com.liferay.ide.project.ui.tests.ImportLiferayWorkspaceProject;
import com.liferay.ide.ui.tests.swtbot.page.CheckBoxPO;
import com.liferay.ide.ui.tests.swtbot.page.TextPO;

import com.liferay.ide.ui.tests.swtbot.page.WizardPO;

public class ImportLiferayWorkspaceProjectPO extends WizardPO implements ImportLiferayWorkspaceProject
{

    private TextPO _workspaceLocation;
    private TextPO _buildTypeText;
    private TextPO _serverName;
    private TextPO _bundleUrl;
    private CheckBoxPO _downloadLiferaybundle;

    public ImportLiferayWorkspaceProjectPO( SWTBot bot, String title, int validationMessageIndex )
    {

        super( bot, title, BUTTON_CANCEL, BUTTON_FINISH, BUTTON_BACK, BUTTON_NEXT, validationMessageIndex );
        _buildTypeText = new TextPO( bot, LABEL_BUILD_TYPE );
        _workspaceLocation = new TextPO( bot, TEXT_WORKSPACE_LOCATION );
        _downloadLiferaybundle = new CheckBoxPO( bot, CHECKBOX_DOWNLOAD_LIFERAY_BUNDLE );
        _serverName = new TextPO( bot, TEXT_SERVER_NAME );
        _bundleUrl = new TextPO( bot, TEXT_BUNDLE_URL );
    }

    public CheckBoxPO getDownloadLiferaybundle()
    {
        return _downloadLiferaybundle;
    }

    public void setDownloadLiferaybundle( CheckBoxPO downloadLiferaybundle )
    {
        this._downloadLiferaybundle = downloadLiferaybundle;
    }

    public TextPO getWorkspaceLocation()
    {
        return _workspaceLocation;
    }

    public void setWorkspaceLocation( String workspaceLocation )
    {
        this._workspaceLocation.setText( workspaceLocation );;
    }

    public TextPO getBuildTypeText()
    {
        return _buildTypeText;
    }

    public void setBuildTypeText( String buildTypeText )
    {
        this._buildTypeText.setText( buildTypeText );
    }

    public TextPO getServerName()
    {
        return _serverName;
    }

    public void setServerName( String serverName )
    {
        this._serverName.setText( serverName );
    }

    public TextPO getBundleUrl()
    {
        return _bundleUrl;
    }

    public void setBundleUrl( String bundleUrl )
    {
        this._bundleUrl.setText( bundleUrl );
    }

}

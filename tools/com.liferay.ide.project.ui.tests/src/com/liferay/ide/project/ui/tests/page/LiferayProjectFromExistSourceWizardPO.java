
package com.liferay.ide.project.ui.tests.page;

import org.eclipse.swtbot.swt.finder.SWTBot;

import com.liferay.ide.project.ui.tests.LiferayProjectFromExistSourceWizard;
import com.liferay.ide.ui.tests.swtbot.page.TextPO;
import com.liferay.ide.ui.tests.swtbot.page.ToolbarButtonPO;
import com.liferay.ide.ui.tests.swtbot.page.WizardPO;

public class LiferayProjectFromExistSourceWizardPO extends WizardPO implements LiferayProjectFromExistSourceWizard
{

    private ToolbarButtonPO _browseProjectDirectory;

    private TextPO _pluginTypeText;

    private TextPO _projectDirectoryText;

    private TextPO _sdkVersionText;

    public LiferayProjectFromExistSourceWizardPO( SWTBot bot )
    {
        this( bot, TITLE_NEW_LIFERAY_PROJECT_EXIS_SOURCE, BUTTON_CANCEL, BUTTON_FINISH, BUTTON_BACK, BUTTON_NEXT, INDEX_DEFAULT_VALIDATION_MESSAGE );
    }

    public LiferayProjectFromExistSourceWizardPO(
        SWTBot bot, String title, String cancelButtonText, String finishButtonText, String backButtonText,
        String nextButtonText, int validationMessageIndex )
    {
        super( bot, title, cancelButtonText, finishButtonText, backButtonText, nextButtonText, validationMessageIndex );
        _pluginTypeText = new TextPO( bot, LABLE_PLUGIN_TYPE );
        _projectDirectoryText = new TextPO( bot, LABLE_PROJECT_DIRECTORY );
        _sdkVersionText = new TextPO( bot, LABLE_SDK_VERSION );
        _browseProjectDirectory = new ToolbarButtonPO( bot, LABLE_BROWSE_PROJECT );
    }

    public ToolbarButtonPO getBrowseProjectDirectory()
    {
        return _browseProjectDirectory;
    }

    public TextPO getPluginTypeText()
    {
        return _pluginTypeText;
    }

    public TextPO getProjectDirectoryText()
    {
        return _projectDirectoryText;
    }

    public TextPO getSdkVersionText()
    {
        return _sdkVersionText;
    }

    public void importProject( String path )
    {
        getProjectDirectoryText().setText( path );

        finish();
        waitForPageToClose();
    }
}

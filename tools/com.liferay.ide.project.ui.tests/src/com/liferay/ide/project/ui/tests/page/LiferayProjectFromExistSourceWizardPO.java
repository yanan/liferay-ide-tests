/*******************************************************************************
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 *******************************************************************************/

package com.liferay.ide.project.ui.tests.page;

import org.eclipse.swtbot.swt.finder.SWTBot;

import com.liferay.ide.project.ui.tests.LiferayProjectFromExistSourceWizard;
import com.liferay.ide.ui.tests.swtbot.page.TextPO;
import com.liferay.ide.ui.tests.swtbot.page.ToolbarButtonWithTooltipPO;
import com.liferay.ide.ui.tests.swtbot.page.WizardPO;

/**
 * @author Li Lu
 */
public class LiferayProjectFromExistSourceWizardPO extends WizardPO implements LiferayProjectFromExistSourceWizard
{

    private ToolbarButtonWithTooltipPO _browseProjectDirectory;

    private TextPO _pluginTypeText;

    private TextPO _projectDirectoryText;

    private TextPO _sdkVersionText;

    public LiferayProjectFromExistSourceWizardPO( SWTBot bot )
    {
        this( bot, TITLE_NEW_LIFERAY_PROJECT_EXIS_SOURCE, BUTTON_CANCEL, BUTTON_FINISH, BUTTON_BACK, BUTTON_NEXT, INDEX_DEFAULT_WIZARD_VALIDATION_MESSAGE );
    }

    public LiferayProjectFromExistSourceWizardPO(
        SWTBot bot, String title, String cancelButtonText, String finishButtonText, String backButtonText,
        String nextButtonText, int validationMessageIndex )
    {
        super( bot, title, cancelButtonText, finishButtonText, backButtonText, nextButtonText, validationMessageIndex );
        _pluginTypeText = new TextPO( bot, LABLE_PLUGIN_TYPE );
        _projectDirectoryText = new TextPO( bot, LABLE_PROJECT_DIRECTORY );
        _sdkVersionText = new TextPO( bot, LABLE_SDK_VERSION );
        _browseProjectDirectory = new ToolbarButtonWithTooltipPO( bot, LABLE_BROWSE_PROJECT );
    }

    public ToolbarButtonWithTooltipPO getBrowseProjectDirectory()
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

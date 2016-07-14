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

package com.liferay.ide.gradle.ui.tests.page;

import com.liferay.ide.ui.tests.swtbot.page.CheckBoxPO;
import com.liferay.ide.ui.tests.swtbot.page.TextPO;
import com.liferay.ide.ui.tests.swtbot.page.WizardPO;
import com.liferay.ide.gradle.tests.LiferayWorkspaceProjectWizard;

import org.eclipse.swtbot.swt.finder.SWTBot;

/**
 * @author Vicky Wang
 */
public class CreateWorkspaceProjectWizardPO extends WizardPO implements LiferayWorkspaceProjectWizard
{

    private TextPO _workspaceNameText;
    private TextPO _serverNameText;
    private CheckBoxPO _downloadLiferayBundleCheckBox;

    public CreateWorkspaceProjectWizardPO( SWTBot bot )
    {
        this( bot, INDEX_DEFAULT_VALIDATION_MESSAGE );
    }

    public CreateWorkspaceProjectWizardPO( SWTBot bot, int index )
    {
        this( bot, TEXT_BLANK, index );
    }

    public CreateWorkspaceProjectWizardPO( SWTBot bot, String title )
    {
        this( bot, title, INDEX_DEFAULT_VALIDATION_MESSAGE );
    }

    public CreateWorkspaceProjectWizardPO( SWTBot bot, String title, int validationMessageIndex )
    {
        super( bot, title, BUTTON_CANCEL, BUTTON_FINISH, BUTTON_BACK, BUTTON_NEXT, validationMessageIndex );

        _workspaceNameText = new TextPO( bot, LABEL_WORKSPACE_NAME );
        _serverNameText = new TextPO( bot, LABEL_SERVER_NAME );
        _downloadLiferayBundleCheckBox = new CheckBoxPO( bot, LABEL_DOWNLOAD_LIFERAY_BUNDLE );
    }

    public String getWorkspaceNameText()
    {
        return _workspaceNameText.getText();
    }

    public void setWorkspaceName( String workspaceName )
    {
        this._workspaceNameText.setText( workspaceName );
    }

    public void setServerName( String serverName )
    {
        this._serverNameText.setText( serverName );
    }

    public CheckBoxPO get_downloadLiferayBundleCheckbox()
    {
        return _downloadLiferayBundleCheckBox;
    }

    public void setDownloadLiferayBundleCheckBox( CheckBoxPO _downloadLiferayBundleCheckBox )
    {
        this._downloadLiferayBundleCheckBox = _downloadLiferayBundleCheckBox;
    }

    public boolean isDownloadLiferayBundleChecked()
    {
        return _downloadLiferayBundleCheckBox.isChecked();
    }

}

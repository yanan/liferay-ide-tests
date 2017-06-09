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

package com.liferay.ide.module.fragment.ui.tests.page;

import org.eclipse.swtbot.swt.finder.SWTBot;

import com.liferay.ide.module.fragment.tests.ModuleFragmentProjectWizard;
import com.liferay.ide.ui.tests.swtbot.page.TablePO;
import com.liferay.ide.ui.tests.swtbot.page.TextPO;
import com.liferay.ide.ui.tests.swtbot.page.ToolbarButtonWithTooltipPO;
import com.liferay.ide.ui.tests.swtbot.page.WizardPO;

/**
 * @author Demetria Ding
 */
public class CreateModuleFragmentFilesWizardPO extends WizardPO implements ModuleFragmentProjectWizard
{

    private TextPO _projectNameText;
    private TextPO _liferayRuntimeText;
    private TextPO _hostOsgiBundleText;
    private ToolbarButtonWithTooltipPO _addOverriddenFilesButton;
    private ToolbarButtonWithTooltipPO _addOverrideFilePathButton;
    private ToolbarButtonWithTooltipPO _addOverrideFileDeletButton;
    private ToolbarButtonWithTooltipPO _addOverrideFileUpButton;
    private ToolbarButtonWithTooltipPO _addOverrideFileDownButton;
    private ToolbarButtonWithTooltipPO _newLiferayRuntime;
    private TablePO _overriddenFilesTable;

    public CreateModuleFragmentFilesWizardPO( SWTBot bot, int index )
    {
        this( bot, TEXT_OSGI_BUNDLE_BLANK, index );
    }

    public CreateModuleFragmentFilesWizardPO( SWTBot bot, String title, int validationMessageIndex )
    {
        super( bot, title, BUTTON_CANCEL, BUTTON_FINISH, BUTTON_BACK, BUTTON_NEXT, validationMessageIndex );

        _projectNameText = new TextPO( bot, LABEL_PROJECT_NAME );
        _liferayRuntimeText = new TextPO( bot, LABEL_RUNTIME_NAME );
        _hostOsgiBundleText = new TextPO( bot, LABEL_OSGI_BUNDLE );
        _addOverriddenFilesButton = new ToolbarButtonWithTooltipPO( bot, BUTTON_ADD_FILES_TO_OVERRIDE );
        _addOverrideFilePathButton = new ToolbarButtonWithTooltipPO( bot, BUTTON_ADD_FILE_PATH );
        _addOverrideFileDeletButton = new ToolbarButtonWithTooltipPO( bot, TOOLBARBOTTON_DELETE );
        _addOverrideFileUpButton = new ToolbarButtonWithTooltipPO( bot, TOOLBARBOTTON_MOVE_UP );
        _addOverrideFileDownButton = new ToolbarButtonWithTooltipPO( bot, TOOLBARBOTTON_MOVE_DOWN );
        _newLiferayRuntime = new ToolbarButtonWithTooltipPO( bot, TOOLBARBOTTON_LIFERAY_RUNTIME );
        _overriddenFilesTable = new TablePO( bot, LABLE_OVERRIDDEN_FILES );
    }

    public String getProjectNameText()
    {
        return _projectNameText.getText();
    }

    public boolean isLiferayRuntimeTextEnabled()
    {
        return _liferayRuntimeText.isEnabled();
    }

    public String getHostOsgiBundleText()
    {
        return _hostOsgiBundleText.getText();
    }

    public ToolbarButtonWithTooltipPO getAddOverriddenFilesButton()
    {
        return _addOverriddenFilesButton;
    }

    public ToolbarButtonWithTooltipPO getAddOverrideFilePathButton()
    {
        return _addOverrideFilePathButton;
    }

    public ToolbarButtonWithTooltipPO getDeletOverrideFileButton()
    {
        return _addOverrideFileDeletButton;
    }

    public ToolbarButtonWithTooltipPO getUpOverrideFileButton()
    {
        return _addOverrideFileUpButton;
    }

    public ToolbarButtonWithTooltipPO getDownOverrideFileButton()
    {
        return _addOverrideFileDownButton;
    }

    public ToolbarButtonWithTooltipPO getNewLiferayRuntimeutton()
    {
        return _newLiferayRuntime;
    }

    public TablePO getOverriddenFiles()
    {
        return _overriddenFilesTable;
    }

    public void setProjectName( String projectName )
    {
        this._projectNameText.setText( projectName );
    }
}

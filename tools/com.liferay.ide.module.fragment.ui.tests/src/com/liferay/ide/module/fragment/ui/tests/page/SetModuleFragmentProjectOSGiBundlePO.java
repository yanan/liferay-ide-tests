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
import com.liferay.ide.ui.tests.swtbot.page.ToolbarButtonPO;
import com.liferay.ide.ui.tests.swtbot.page.WizardPO;

/**
 * @author Vicky Wang
 */
public class SetModuleFragmentProjectOSGiBundlePO extends WizardPO implements ModuleFragmentProjectWizard
{

    private ToolbarButtonPO _addOverriddenFilesButton;
    private ToolbarButtonPO _addOverrideFilePathButton;
    private TablePO _overriddenFilesTable;
    private ToolbarButtonPO _selectOSGiBundleButton;

    public SetModuleFragmentProjectOSGiBundlePO( SWTBot bot, int index )
    {
        this( bot, TEXT_OSGI_BUNDLE_BLANK, index );
    }

    public SetModuleFragmentProjectOSGiBundlePO( SWTBot bot, String title, int validationMessageIndex )
    {
        super( bot, title, BUTTON_CANCEL, BUTTON_FINISH, BUTTON_BACK, BUTTON_NEXT, validationMessageIndex );

        _selectOSGiBundleButton = new ToolbarButtonPO( bot, BUTTON_OSGI_BUNDLE_BROWSE );
        _addOverriddenFilesButton = new ToolbarButtonPO( bot, BUTTON_ADD_FILES_TO_OVERRIDE );
        _addOverrideFilePathButton = new ToolbarButtonPO( bot, BUTTON_ADD_FILE_PATH );
        _overriddenFilesTable = new TablePO( bot, LABLE_OVERRIDDEN_FILES );
    }

    public ToolbarButtonPO getAddOverriddenFilesButton()
    {
        return _addOverriddenFilesButton;
    }

    public ToolbarButtonPO getAddOverrideFilePathButton()
    {
        return _addOverrideFilePathButton;
    }

    public TablePO getOverriddenFiles()
    {
        return _overriddenFilesTable;
    }

    public ToolbarButtonPO getSelectOSGiBundleButton()
    {
        return _selectOSGiBundleButton;
    }

}
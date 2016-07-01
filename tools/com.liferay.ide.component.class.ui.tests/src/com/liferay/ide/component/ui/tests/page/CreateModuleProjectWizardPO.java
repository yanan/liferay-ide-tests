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

package com.liferay.ide.component.ui.tests.page;

import org.eclipse.swtbot.swt.finder.SWTBot;

import com.liferay.ide.component.ui.tests.ModuleProjectWizard;
import com.liferay.ide.ui.tests.swtbot.page.ComboBoxPO;
import com.liferay.ide.ui.tests.swtbot.page.TextPO;
import com.liferay.ide.ui.tests.swtbot.page.WizardPO;

/**
 * @author Ying Xu
 */
public class CreateModuleProjectWizardPO extends WizardPO implements ModuleProjectWizard
{

    private ComboBoxPO _projectTemplateNameComboBox;
    private TextPO _projectNameText;

    public CreateModuleProjectWizardPO( SWTBot bot )
    {
        this( bot, TEXT_BLANK );
    }

    public CreateModuleProjectWizardPO( SWTBot bot, String title )
    {
        super( bot, title, BUTTON_CANCEL, BUTTON_FINISH, BUTTON_BACK, BUTTON_NEXT );
        _projectTemplateNameComboBox = new ComboBoxPO( bot, LABEL_PROJECT_TEMPLATE_NAME );
        _projectNameText = new TextPO( bot, LABEL_MODULE_PROJECT_NAME );
    }

    public void createModuleProject( String projectName, String projectTemplate )
    {
        _projectTemplateNameComboBox.setSelection( projectTemplate );
        _projectNameText.setText( projectName );
    }

    public ComboBoxPO getProjectTemplateNameComboBox()
    {
        return _projectTemplateNameComboBox;
    }

    public TextPO getProjectNameText()
    {
        return _projectNameText;
    }

}

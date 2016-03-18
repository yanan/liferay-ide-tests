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

package com.liferay.ide.hook.ui.tests.page;

import org.eclipse.swtbot.swt.finder.SWTBot;

import com.liferay.ide.hook.ui.tests.HookConfigurationWizard;
import com.liferay.ide.project.ui.tests.ProjectWizard;
import com.liferay.ide.ui.tests.swtbot.page.CheckBoxPO;
import com.liferay.ide.ui.tests.swtbot.page.WizardPO;

/**
 * @author Vicky Wang
 */
public class HookTypesToCreatePO extends WizardPO implements HookConfigurationWizard, ProjectWizard
{

    private CheckBoxPO _customJSPsCheckBox;
    private CheckBoxPO _languagePropertiesCheckBox;
    private CheckBoxPO _portalPropertiesCheckBox;
    private CheckBoxPO _servicesCheckBox;

    public HookTypesToCreatePO( SWTBot bot, String title )
    {
        super( bot, title, BUTTON_CANCEL, BUTTON_FINISH, BUTTON_BACK, BUTTON_NEXT );

        _customJSPsCheckBox = new CheckBoxPO( bot, LABLE_CUSTOM_JSPS );
        _portalPropertiesCheckBox = new CheckBoxPO( bot, LABLE_PORTAL_PROPERTIES );
        _servicesCheckBox = new CheckBoxPO( bot, LABLE_SERVICES );
        _languagePropertiesCheckBox = new CheckBoxPO( bot, LABLE_LANGUAGE_PROPERTIES );
    }

    public CheckBoxPO getCustomJSPs()
    {
        return _customJSPsCheckBox;
    }

    public CheckBoxPO getLanguageProperties()
    {
        return _languagePropertiesCheckBox;
    }

    public CheckBoxPO getPortalProperties()
    {
        return _portalPropertiesCheckBox;
    }

    public CheckBoxPO getServices()
    {
        return _servicesCheckBox;
    }

}
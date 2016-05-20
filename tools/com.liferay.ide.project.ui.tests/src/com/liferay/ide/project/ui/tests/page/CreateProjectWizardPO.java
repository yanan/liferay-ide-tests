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

import com.liferay.ide.project.ui.tests.ProjectWizard;
import com.liferay.ide.ui.tests.swtbot.page.CheckBoxPO;
import com.liferay.ide.ui.tests.swtbot.page.ComboBoxPO;
import com.liferay.ide.ui.tests.swtbot.page.TextPO;
import com.liferay.ide.ui.tests.swtbot.page.WizardPO;

import org.eclipse.swtbot.swt.finder.SWTBot;

/**
 * @author Terry Jia
 * @author Ashley Yuan
 */
public class CreateProjectWizardPO extends WizardPO implements ProjectWizard
{

    private ComboBoxPO _buildTypeComboBox;
    private TextPO _displayNameText;
    private CheckBoxPO _includeSimpleCodeCheckBox;
    private CheckBoxPO _launchNewPortletWizardCheck;
    private ComboBoxPO _pluginTypeComboBox;
    private TextPO _projectNameText;

    public CreateProjectWizardPO( SWTBot bot )
    {
        this( bot, INDEX_DEFAULT_VALIDATION_MESSAGE );
    }

    public CreateProjectWizardPO( SWTBot bot, int index )
    {
        this( bot, TEXT_BLANK, index );
    }

    public CreateProjectWizardPO( SWTBot bot, String title )
    {
        this( bot, title, INDEX_DEFAULT_VALIDATION_MESSAGE );
    }

    public CreateProjectWizardPO( SWTBot bot, String title, int validationMessageIndex )
    {
        super( bot, title, BUTTON_CANCEL, BUTTON_FINISH, BUTTON_BACK, BUTTON_NEXT, validationMessageIndex );

        _projectNameText = new TextPO( bot, LABEL_PROJECT_NAME );
        _displayNameText = new TextPO( bot, LABEL_DISPLAY_NAME );
        _buildTypeComboBox = new ComboBoxPO( bot, LABEL_BUILD_TYPE );
        _pluginTypeComboBox = new ComboBoxPO( bot, LABEL_PLUGIN_TYPE );
        _includeSimpleCodeCheckBox = new CheckBoxPO( bot, CHECKBOX_INCLUDE_SAMPLE_CODE );
        _launchNewPortletWizardCheck = new CheckBoxPO( bot, LABEL_LAUNCH_NEW_PORTLET_WIZARD_AFTER_PROJECT );
    }

    public void createSDKPortletProject( String projectName )
    {
        createSDKProject( projectName, MENU_PORTLET );
    }

    public void createSDKProject( String projectName, String pluginType )
    {
        createSDKProject( projectName, TEXT_BLANK, pluginType );
    }

    public void createSDKProject( String projectName, String pluginType, boolean includeSimpleCode )
    {
        createSDKProject( projectName, pluginType, includeSimpleCode, false );
    }

    public void createSDKProject(
        String projectName, String pluginType, boolean includeSimpleCode, boolean launchNewPortletWizard )
    {
        createSDKProject( projectName, TEXT_BLANK, pluginType, includeSimpleCode, launchNewPortletWizard );
    }

    public void createSDKProject( String projectName, String displayName, String pluginType )
    {
        createSDKProject( projectName, displayName, pluginType, false, false );
    }

    public void createSDKProject(
        String projectName, String displayName, String pluginType, boolean includeSimpleCode,
        boolean launchNewPortletWizard )
    {
        _projectNameText.setText( projectName );

        if( displayName != null && !displayName.equals( TEXT_BLANK ) )
        {
            _displayNameText.setText( displayName );
        }

        _buildTypeComboBox.setSelection( MENU_BUILD_TYPE_ANT );

        _pluginTypeComboBox.setSelection( pluginType );

        if( pluginType.equals( MENU_PORTLET ) || pluginType.equals( MENU_SERVICE_BUILDER_PORTLET ) )
        {
            if( includeSimpleCode )
            {
                _includeSimpleCodeCheckBox.select();
            }
            else
            {
                _includeSimpleCodeCheckBox.deselect();
            }
        }

        if( pluginType.equals( MENU_PORTLET ) )
        {
            if( launchNewPortletWizard )
            {
                _launchNewPortletWizardCheck.select();
            }
            else
            {
                _launchNewPortletWizardCheck.deselect();
            }
        }
    }

    public String getBuildTypeComboBox()
    {
        return _buildTypeComboBox.getText();
    }

    public String getPluginTypeComboBox()
    {
        return _pluginTypeComboBox.getText();
    }

    public String getProjectNameText()
    {
        return _projectNameText.getText();
    }

    public boolean IsIncludeSimpleCodeCheckBoxChecked()
    {
        return _includeSimpleCodeCheckBox.isChecked();
    }

    public boolean IsLaunchNewPortletWizardCheckBoxChecked()
    {
        return _launchNewPortletWizardCheck.isChecked();
    }

    public void setBuildTypeComboBox( ComboBoxPO buildTypeComboBox )
    {
        this._buildTypeComboBox = buildTypeComboBox;
    }

    public void setPluginTypeComboBox( ComboBoxPO pluginTypeComboBox )
    {
        this._pluginTypeComboBox = pluginTypeComboBox;
    }

    public void setProjectName( TextPO projectName )
    {
        _projectNameText = projectName;
    }

}

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

import com.liferay.ide.component.ui.tests.NewLiferayComponentWizard;
import com.liferay.ide.ui.tests.swtbot.page.ComboBoxPO;
import com.liferay.ide.ui.tests.swtbot.page.TextPO;
import com.liferay.ide.ui.tests.swtbot.page.ToolbarButtonPO;
import com.liferay.ide.ui.tests.swtbot.page.WizardPO;

/**
 * @author Ying Xu
 */
public class NewLiferayComponentWizardPO extends WizardPO implements NewLiferayComponentWizard
{

    private TextPO _packageName;
    private TextPO _componentClassName;

    private ToolbarButtonPO _browseButton;

    private ComboBoxPO _projectName;
    private ComboBoxPO _componentClassTemplate;

    private int _validationMessageIndex = INDEX_DEFAULT_VALIDATION_MESSAGE;

    public NewLiferayComponentWizardPO( SWTBot bot, String title )
    {
        this( bot, title, INDEX_VALIDATION_MESSAGE );
    }

    public NewLiferayComponentWizardPO( SWTBot bot, String title, int validationMessageIndex )
    {
        super( bot, title, BUTTON_CANCEL, BUTTON_FINISH, BUTTON_BACK, BUTTON_NEXT, validationMessageIndex );
        this._validationMessageIndex = validationMessageIndex;
        _packageName = new TextPO( bot, LABEL_PACKAGE_NAME );
        _componentClassName = new TextPO( bot, LABEL_COMPONENT_CLASS_NAME );
        _projectName = new ComboBoxPO( bot, COMBOBOX_PROJECT_NAME );
        _componentClassTemplate = new ComboBoxPO( bot, COMBOBOX_COMPONENT_CLASS_TEMPLATE );
        _browseButton = new ToolbarButtonPO( bot, BUTTON_BROWSE );
    }

    public void selectTemplate( String projectName, String componentClassTemplate )
    {
        _projectName.setSelection( projectName );
        _componentClassTemplate.setSelection( componentClassTemplate );
    }

    public TextPO getPackageName()
    {
        return _packageName;
    }

    public void setPackageName( TextPO _packageName )
    {
        this._packageName = _packageName;
    }

    public TextPO getComponentClassName()
    {
        return _componentClassName;
    }

    public void setComponentClassName( TextPO _componentClassName )
    {
        this._componentClassName = _componentClassName;
    }

    public ToolbarButtonPO getBrowseButton()
    {
        return _browseButton;
    }

    public ComboBoxPO getProjectName()
    {
        return _projectName;
    }

    public ComboBoxPO getComponentClassTemplate()
    {
        return _componentClassTemplate;
    }

    public String getValidationMessage()
    {
        if( _validationMessageIndex < 0 )
        {
            log.error( "Validation Message Index error" );

            return null;
        }

        return bot.text( _validationMessageIndex ).getText();
    }

}

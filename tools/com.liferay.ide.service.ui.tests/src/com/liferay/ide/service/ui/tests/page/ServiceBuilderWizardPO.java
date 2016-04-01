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

package com.liferay.ide.service.ui.tests.page;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;

import com.liferay.ide.service.ui.tests.ServiceBuilderWizard;
import com.liferay.ide.ui.tests.swtbot.page.CheckBoxPO;
import com.liferay.ide.ui.tests.swtbot.page.ComboBoxPO;
import com.liferay.ide.ui.tests.swtbot.page.TextPO;
import com.liferay.ide.ui.tests.swtbot.page.WizardPO;

/**
 * @author Ying Xu
 */
public class ServiceBuilderWizardPO extends WizardPO implements ServiceBuilderWizard
{

    TextPO author;
    TextPO serviceFile;
    TextPO namespace;
    TextPO packagePath;

    ComboBoxPO pluginProjectComboBox;
    CheckBoxPO includeSampleEntityCheckBox;

    private final String browseButtonText;
    private int validationMessageIndex = -1;

    public ServiceBuilderWizardPO( SWTBot bot )
    {
        this( bot, TEXT_BLANK, BUTTON_CANCEL, BUTTON_FINISH, BUTTON_BROWSE, INDEX_VALIDATION_MESSAGE );
    }

    public ServiceBuilderWizardPO( SWTBot bot, String title )
    {
        this( bot, title, BUTTON_CANCEL, BUTTON_FINISH, BUTTON_BROWSE, INDEX_VALIDATION_MESSAGE );
    }

    public ServiceBuilderWizardPO(
        SWTBot bot, String title, String cancelButtonText, String finishButtonText, String browseButtonText )
    {
        super( bot, title, cancelButtonText, finishButtonText, browseButtonText, TEXT_BLANK );
        this.browseButtonText = browseButtonText;
        packagePath = new TextPO( bot, LABEL_PACKAGE_PATH );
        namespace = new TextPO( bot, LABEL_NAMESPACE );
        author = new TextPO( bot, LABEL_AUTHOR );
        serviceFile = new TextPO( bot, LABEL_SERVICE_FILE );
        includeSampleEntityCheckBox = new CheckBoxPO( bot, CHECKBOX_INCLUDE_SAMPLE_ENTITY );
        pluginProjectComboBox = new ComboBoxPO( bot, COMBOBOX_PLUGIN_PROJECT );

    }

    public ServiceBuilderWizardPO(
        SWTBot bot, String title, String cancelButtonText, String finishButtonText, String browseButtonText,
        int validationMessageIndex )
    {
        this( bot, title, cancelButtonText, finishButtonText, browseButtonText );

        this.validationMessageIndex = validationMessageIndex;
    }

    public void browse()
    {
        clickClosingButton( browseButton() );
    }

    protected SWTBotButton browseButton()
    {
        return bot.button( browseButtonText );
    }

    public TextPO getAuthorText()
    {
        return author;
    }

    public CheckBoxPO getIncludeSampleEntityCheckBox()
    {
        return includeSampleEntityCheckBox;
    }

    public TextPO getNamespaceText()
    {
        return namespace;
    }

    public TextPO getPackagePathText()
    {
        return packagePath;
    }

    public ComboBoxPO getPluginProjectComboBox()
    {
        return pluginProjectComboBox;
    }

    public TextPO getServiceFileText()
    {
        return serviceFile;
    }

    public String getValidationMessage()
    {
        if( validationMessageIndex < 0 )
        {
            log.error( "Validation Message Index error" );

            return null;
        }

        return bot.text( validationMessageIndex ).getText();
    }

    public void NewServiceBuilder( String packagePathText, String namespaceText )
    {
        NewServiceBuilder( packagePathText, namespaceText, true );
    }

    public void NewServiceBuilder( String packagePathText, String namespaceText, boolean includeSampleEntity )
    {
        packagePath.setText( packagePathText );
        namespace.setText( namespaceText );

        if( includeSampleEntity )
        {
            includeSampleEntityCheckBox.select();
        }
        else
        {
            includeSampleEntityCheckBox.deselect();
        }
    }

}

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

package com.liferay.ide.ui.tests.swtbot.page;

import org.eclipse.swtbot.swt.finder.SWTBot;

import com.liferay.ide.ui.tests.UIBase;

/**
 * @author Ying Xu
 */
public class NewJavaPackagePO extends WizardPO implements UIBase
{

    TextPO name;
    TextPO sourceFolder;

    public NewJavaPackagePO( SWTBot bot )
    {
        this( bot, TEXT_BLANK, BUTTON_CANCEL, BUTTON_FINISH );
    }

    public NewJavaPackagePO( SWTBot bot, String title, String cancelButtonText, String finishButtonText )
    {
        super( bot, title, cancelButtonText, finishButtonText, TEXT_BLANK, TEXT_BLANK );
        sourceFolder = new TextPO( bot, LABEL_SOURCE_FOLDER );
        name = new TextPO( bot, LABEL_NAME );

    }

    public TextPO getSourceFolderText()
    {
        return sourceFolder;
    }

    public void setName( String packageName )
    {
        name.setText( packageName );
    }

}

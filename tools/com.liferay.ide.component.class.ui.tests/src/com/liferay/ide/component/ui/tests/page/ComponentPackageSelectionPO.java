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
import com.liferay.ide.ui.tests.swtbot.page.ButtonPO;
import com.liferay.ide.ui.tests.swtbot.page.DialogPO;
import com.liferay.ide.ui.tests.swtbot.page.TextPO;

/**
 * @author Ying Xu
 */
public class ComponentPackageSelectionPO extends DialogPO implements NewLiferayComponentWizard
{

    private TextPO _packageSelectionText;

    private ButtonPO _okButton;

    public ComponentPackageSelectionPO( SWTBot bot )
    {
        this( bot, TEXT_BLANK );
    }

    public ComponentPackageSelectionPO( SWTBot bot, String title )
    {
        super( bot, title, BUTTON_CANCEL, BUTTON_OK );
        _packageSelectionText = new TextPO( bot, LABEL_CHOOSE_PACKAGE );

    }

    public void getPackageSelectionText( String packageText )
    {
        _packageSelectionText.getText();
    }

    public void setPackageSelectionText( String packageText )
    {
        _packageSelectionText.setText( packageText );
    }

    public ButtonPO getOkButton()
    {
        return _okButton;
    }

}

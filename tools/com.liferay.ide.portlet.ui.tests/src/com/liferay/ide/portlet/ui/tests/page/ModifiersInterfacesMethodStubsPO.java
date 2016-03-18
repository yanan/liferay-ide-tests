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

package com.liferay.ide.portlet.ui.tests.page;

import org.eclipse.swtbot.swt.finder.SWTBot;

import com.liferay.ide.portlet.ui.tests.LiferayPortletWizard;
import com.liferay.ide.ui.tests.ProjectWizard;
import com.liferay.ide.ui.tests.swtbot.page.CheckBoxPO;
import com.liferay.ide.ui.tests.swtbot.page.TablePO;
import com.liferay.ide.ui.tests.swtbot.page.WizardPO;

/**
 * @author Ashley Yuan
 */
public class ModifiersInterfacesMethodStubsPO extends WizardPO implements LiferayPortletWizard, ProjectWizard
{

    private CheckBoxPO _publicCheckbox;
    private CheckBoxPO _abstractCheckbox;
    private CheckBoxPO _finalCheckbox;

    private TablePO _interfacesTable;

    private CheckBoxPO _createEntryClassCheckbox;
    private CheckBoxPO _constrcutFromSuperClassCheckbox;
    private CheckBoxPO _inheritedAbstractMethodsCheckbox;
    private CheckBoxPO _initCheckbox;
    private CheckBoxPO _destoryCheckbox;
    private CheckBoxPO _doViewCheckbox;
    private CheckBoxPO _doEditCheckbox;
    private CheckBoxPO _doHelpCheckbox;
    private CheckBoxPO _doAboutCheckbox;
    private CheckBoxPO _doConfigCheckbox;
    private CheckBoxPO _doEditDefaultsCheckbox;
    private CheckBoxPO _doEditGuestCheckbox;
    private CheckBoxPO _doPreviewCheckbox;
    private CheckBoxPO _doPrintCheckbox;
    private CheckBoxPO _processActionCheckbox;
    private CheckBoxPO _serveResourceCheckbox;

    public ModifiersInterfacesMethodStubsPO( SWTBot bot, int validationMessageIndex )
    {
        this( bot, TEXT_BLANK, validationMessageIndex );
    }

    public ModifiersInterfacesMethodStubsPO( SWTBot bot, String title, int validationMessageIndex )
    {
        super( bot, title, BUTTON_BACK, BUTTON_NEXT, BUTTON_FINISH, BUTTON_CANCEL, validationMessageIndex );

        _publicCheckbox = new CheckBoxPO( bot, LABEL_PUBLIC );
        _abstractCheckbox = new CheckBoxPO( bot, LABEL_ABSTRACT );
        _finalCheckbox = new CheckBoxPO( bot, LABEL_FINAL );
        _interfacesTable = new TablePO( bot, LABEL_INTERFACES );
        _constrcutFromSuperClassCheckbox = new CheckBoxPO( bot, LABEL_CONSTRUCTORS_FROM_SUPERCLASS );
        _inheritedAbstractMethodsCheckbox = new CheckBoxPO( bot, LABEL_INHERITED_ABSTRACT_METHODS );
        _initCheckbox = new CheckBoxPO( bot, LABEL_INIT );
        _destoryCheckbox = new CheckBoxPO( bot, LABEL_DESTROY );
        _doViewCheckbox = new CheckBoxPO( bot, LABEL_DOVIEW );
        _doEditCheckbox = new CheckBoxPO( bot, LABEL_DOEDIT );
        _doHelpCheckbox = new CheckBoxPO( bot, LABEL_DOHELP );
        _doAboutCheckbox = new CheckBoxPO( bot, LABEL_DOABOUT );
        _doConfigCheckbox = new CheckBoxPO( bot, LAEBL_DOCONFIG );
        _doEditDefaultsCheckbox = new CheckBoxPO( bot, LABEL_DOEDITDEFAULTS );
        _doEditGuestCheckbox = new CheckBoxPO( bot, LABEL_DOEDITGUEST );
        _doPreviewCheckbox = new CheckBoxPO( bot, LABEL_DOPREVIEW );
        _doPrintCheckbox = new CheckBoxPO( bot, LABEL_DOPRINT );
        _processActionCheckbox = new CheckBoxPO( bot, LABEL_PROCESSACTION );
        _serveResourceCheckbox = new CheckBoxPO( bot, LABEL_SERVERESOURCE );
    }

    public boolean isAbstractChecked()
    {
        return _abstractCheckbox.isChecked();
    }

    public boolean isAbstractEnabled()
    {
        return _abstractCheckbox.isEnabled();
    }

    public boolean isConstrcutFromSuperClassChecked()
    {
        return _constrcutFromSuperClassCheckbox.isChecked();
    }

    public boolean isConstrcutFromSuperClassEnabled()
    {
        return _constrcutFromSuperClassCheckbox.isEnabled();
    }

    public boolean isCreateEntryClassChecked()
    {
        return _createEntryClassCheckbox.isChecked();
    }

    public boolean isCreateEntryClassEnabled()
    {
        return _createEntryClassCheckbox.isEnabled();
    }

    public boolean isDestoryChecked()
    {
        return _destoryCheckbox.isChecked();
    }

    public boolean isDestoryEnabled()
    {
        return _destoryCheckbox.isEnabled();
    }

    public boolean isDoAboutChecked()
    {
        return _doAboutCheckbox.isChecked();
    }

    public boolean isDoAboutEnabled()
    {
        return _doAboutCheckbox.isEnabled();
    }

    public boolean isDoConfigChecked()
    {
        return _doConfigCheckbox.isChecked();
    }

    public boolean isDoConfigEnabled()
    {
        return _doConfigCheckbox.isEnabled();
    }

    public boolean isDoEditChecked()
    {
        return _doEditCheckbox.isChecked();
    }

    public boolean isDoEditDefaultsChecked()
    {
        return _doEditDefaultsCheckbox.isChecked();
    }

    public boolean isDoEditDefaultsEnabled()
    {
        return _doEditDefaultsCheckbox.isEnabled();
    }

    public boolean isDoEditEnabled()
    {
        return _doEditCheckbox.isEnabled();
    }

    public boolean isDoEditGuestChecked()
    {
        return _doEditGuestCheckbox.isChecked();
    }

    public boolean isDoEditGuestEnabled()
    {
        return _doEditGuestCheckbox.isEnabled();
    }

    public boolean isDoHelpChecked()
    {
        return _doHelpCheckbox.isChecked();
    }

    public boolean isDoHelpEnabled()
    {
        return _doHelpCheckbox.isEnabled();
    }

    public boolean isDoPreviewChecked()
    {
        return _doPreviewCheckbox.isChecked();
    }

    public boolean isDoPreviewEnabled()
    {
        return _doPreviewCheckbox.isEnabled();
    }

    public boolean isDoPrintChecked()
    {
        return _doPrintCheckbox.isChecked();
    }

    public boolean isDoPrintEnabled()
    {
        return _doPrintCheckbox.isEnabled();
    }

    public boolean isDoViewChecked()
    {
        return _doViewCheckbox.isChecked();
    }

    public boolean isDoViewEnabled()
    {
        return _doViewCheckbox.isEnabled();
    }

    public boolean isFinalChecked()
    {
        return _finalCheckbox.isChecked();
    }

    public boolean isFinalEnabled()
    {
        return _finalCheckbox.isEnabled();
    }

    public boolean isInheritedAbstractMethodsChecked()
    {
        return _inheritedAbstractMethodsCheckbox.isChecked();
    }

    public boolean isInheritedAbstractMethodsEnabled()
    {
        return _inheritedAbstractMethodsCheckbox.isEnabled();
    }

    public boolean isInitChecked()
    {
        return _initCheckbox.isChecked();
    }

    public boolean isInitEnabled()
    {
        return _initCheckbox.isEnabled();
    }

    public boolean isProcessActionChecked()
    {
        return _processActionCheckbox.isChecked();
    }

    public boolean isProcessActionEnabled()
    {
        return _processActionCheckbox.isEnabled();
    }

    public boolean isPublicChecked()
    {
        return _publicCheckbox.isChecked();
    }

    public boolean isPublicEnabled()
    {
        return _publicCheckbox.isEnabled();
    }

    public boolean isServeResourceChecked()
    {
        return _serveResourceCheckbox.isChecked();
    }

    public boolean isServeResourceEnabled()
    {
        return _serveResourceCheckbox.isEnabled();
    }

    public void selectInterface( int interfaceIndex )
    {
        _interfacesTable.click( interfaceIndex, 0 );
    }

}

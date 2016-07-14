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

package com.liferay.ide.project.ui.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.liferay.ide.project.ui.tests.page.CreateModuleProjectWizardPO;
import com.liferay.ide.project.ui.tests.page.ModuleProjectWizardSecondPagePO;
import com.liferay.ide.ui.tests.SWTBotBase;

/**
 * @author Ying Xu
 */
public class ModuleProjectWizardTests extends SWTBotBase implements ModuleProjectWizard
{

    @Before
    public void openWizard()
    {
        eclipse.getLiferayWorkspacePerspective().activate();
        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();
        sleep( 15000 );
    }

    @After
    public void waitForCreate()
    {
        sleep();
    }

    @Test
    public void createMvcportletModuleProject()
    {
        String projectName = "testMvcportletProject";

        CreateModuleProjectWizardPO createModuleProjectWizard =
            new CreateModuleProjectWizardPO( bot, INDEX_NEW_LIFERAY_MODULE_PROJECT_VALIDATION_MESSAGE );

        createModuleProjectWizard.createModuleProject( projectName );

        assertEquals( MENU_MODULE_MVCPORTLET, createModuleProjectWizard.getProjectTemplateNameComboBox().getText() );
        createModuleProjectWizard.getProjectTemplateNameComboBox().getComboBoxItemCounts( 9 );

        String[] expectedModuleProjectTemplateItems = { MENU_MODULE_MVCPORTLET, MENU_MODULE_ACTIVATOR,
            MENU_MODULE_CONTENTTARGETINGRULE, MENU_MODULE_CONTENTTARGETINGTRACKINGACTION, MENU_MODULE_CONTROLMENUENTRY,
            MENU_MODULE_PORTLET, MENU_MODULE_SERVICE, MENU_MODULE_SERVICEBUILDER, MENU_MODULE_SERVICEWRAPPER };

        for( String expectedModuleProjectTemplateItem : expectedModuleProjectTemplateItems )
        {
            assertTrue( matchItemInItems( expectedModuleProjectTemplateItems, expectedModuleProjectTemplateItem ) );
        }

        createModuleProjectWizard.next();

        ModuleProjectWizardSecondPagePO createModuleProjectSecondPageWizard =
            new ModuleProjectWizardSecondPagePO( bot, INDEX_CONFIGURE_COMPONENT_CLASS_VALIDATION_MESSAGE );

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );

        createModuleProjectSecondPageWizard.finish();
    }

    // @Test
    public void validationProjectName()
    {
        CreateModuleProjectWizardPO createModuleProjectWizard =
            new CreateModuleProjectWizardPO( bot, INDEX_NEW_LIFERAY_MODULE_PROJECT_VALIDATION_MESSAGE );

        assertEquals( TEXT_ENTER_MODULE_PROJECT_NAME_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertFalse( createModuleProjectWizard.finishButton().isEnabled() );

        createModuleProjectWizard.createModuleProject( "." );
        sleep( 1000 );
        assertEquals( " '.'" + TEXT_INVALID_NAME_ON_PLATFORM, createModuleProjectWizard.getValidationMessage() );
        assertFalse( createModuleProjectWizard.finishButton().isEnabled() );
        createModuleProjectWizard.createModuleProject( "/" );
        sleep( 1000 );
        assertEquals( " /" + TEXT_INVALID_CHARACTER_IN_RESOURCE_NAME + "'/'.",
            createModuleProjectWizard.getValidationMessage() );
        assertFalse( createModuleProjectWizard.finishButton().isEnabled() );
        createModuleProjectWizard.createModuleProject( "$" );
        sleep( 1000 );
        assertEquals( TEXT_INVALID_NAME_FOR_GRADLE_PROJECT, createModuleProjectWizard.getValidationMessage() );
        assertFalse( createModuleProjectWizard.finishButton().isEnabled() );
        createModuleProjectWizard.createModuleProject( "" );
        sleep( 1000 );
        assertEquals( TEXT_MUST_SPECIFIED_PROJECT_NAME, createModuleProjectWizard.getValidationMessage() );
        assertFalse( createModuleProjectWizard.finishButton().isEnabled() );

        createModuleProjectWizard.createModuleProject( "a" );
        sleep( 1000 );
        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertTrue( createModuleProjectWizard.finishButton().isEnabled() );

        createModuleProjectWizard.cancel();
    }

    @AfterClass
    public static void cleanAll()
    {
        // eclipse.closeShell( LABEL_NEW_LIFERAY_MODULE_PROJECT );
        // eclipse.getPackageExporerView().deleteResoucesByNames( new String[] {}, true );
    }
}

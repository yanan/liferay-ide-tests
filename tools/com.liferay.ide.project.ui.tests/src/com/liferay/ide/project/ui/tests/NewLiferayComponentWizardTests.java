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

import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.liferay.ide.project.ui.tests.page.ComponentPackageSelectionPO;
import com.liferay.ide.project.ui.tests.page.CreateModuleProjectWizardPO;
import com.liferay.ide.project.ui.tests.page.NewLiferayComponentWizardPO;
import com.liferay.ide.ui.tests.SWTBotBase;
import com.liferay.ide.ui.tests.swtbot.page.DialogPO;
import com.liferay.ide.ui.tests.swtbot.page.TreePO;

/**
 * @author Ying Xu
 */
public class NewLiferayComponentWizardTests extends SWTBotBase implements NewLiferayComponentWizard, ModuleProjectWizard
{

    @AfterClass
    public static void cleanAll()
    {
        eclipse.closeShell( LABEL_NEW_LIFERAY_COMPONENT );
    }

    @Before
    public void shouldRunTests()
    {
        Assume.assumeTrue( runTest() || runAllTests() );
    }

    @Test
    public void newLiferayComponentClassWithoutAvailableModuleProject()
    {
        eclipse.getCreateLiferayProjectToolbar().getNewLiferayComponentClass().click();

        NewLiferayComponentWizardPO newLiferayComponentWizard =
            new NewLiferayComponentWizardPO( bot, LABEL_NEW_LIFERAY_COMPONENT, INDEX_VALIDATION_MESSAGE );

        newLiferayComponentWizard.waitForPageToOpen();

        // check default initial state
        assertEquals( TEXT_DEFAULT_PROJECT_NAME_VALUE, newLiferayComponentWizard.getProjectName().getText() );
        assertEquals( TEXT_DEFAULT_PACKAGE_NAME_VALUE, newLiferayComponentWizard.getPackageName().getText() );
        assertEquals(
            TEXT_DEFAULT_COMPONENT_CLASS_NAME_VALUE, newLiferayComponentWizard.getComponentClassName().getText() );
        assertEquals(
            TEXT_DEFAULT_COMPONENT_CLASS_TEMPLATE_VALUE,
            newLiferayComponentWizard.getComponentClassTemplate().getText() );
        assertEquals( TEXT_ENTER_PROJECT_NAME_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );

        String[] expectedComponentTemplateItems = { MENU_TEMPLATE_AUTH_FAILURES, MENU_TEMPLATE_AUTH_MAX_FAILURE,
            MENU_TEMPLATE_AUTHENTICATOR, MENU_TEMPLATE_FRIENDLY_URL_MAPPER, MENU_TEMPLATE_GOGO_COMMAND,
            MENU_TEMPLATE_INDEXER_POST_PROCESSOR, MENU_TEMPLATE_LOGIN_PRE_ACTION, MENU_TEMPLATE_MODEL_LISTENER,
            MENU_TEMPLATE_POLLER_PROCESSOR, MENU_TEMPLATE_PORTLET, MENU_TEMPLATE_PORTLET_ACTION_COMMAND,
            MENU_TEMPLATE_PORTLET_FILTER, MENU_TEMPLATE_REST, MENU_TEMPLATE_SERVICE_WRAPPER,
            MENU_TEMPLATE_STRUTS_IN_ACTION, MENU_TEMPLATE_STRUTS_PORTLET_ACTION };

        String[] componentTemplateItems =
            newLiferayComponentWizard.getComponentClassTemplate().getAvailableComboValues();

        assertTrue( componentTemplateItems.length >= 1 );

        assertEquals( expectedComponentTemplateItems.length, componentTemplateItems.length );

        for( int i = 0; i < componentTemplateItems.length; i++ )
        {
            assertTrue( componentTemplateItems[i].equals( expectedComponentTemplateItems[i] ) );
        }

        newLiferayComponentWizard.cancel();

        // create Liferay Fragment project (to do in the future)

        // open component wizard again then check state to make sure it couldn't support new component class

    }

    @Test
    public void newLiferayComponentClass()
    {
        if( !hasAddedProject )
        {
            eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();

            CreateModuleProjectWizardPO createModuleProjectWizard = new CreateModuleProjectWizardPO( bot );

            createModuleProjectWizard.createModuleProject( "testComponent" );
            sleep( 90000 );
            createModuleProjectWizard.finish();
            sleep( 15000 );

            DialogPO dialogPage = new DialogPO( bot, "Open Associated Perspective", BUTTON_NO, BUTTON_YES );

            dialogPage.confirm();
            sleep( 5000 );
        }

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayComponentClass().click();

        NewLiferayComponentWizardPO newLiferayComponentWizard =
            new NewLiferayComponentWizardPO( bot, LABEL_NEW_LIFERAY_COMPONENT, INDEX_VALIDATION_MESSAGE );

        newLiferayComponentWizard.waitForPageToOpen();

        newLiferayComponentWizard.selectProject( "testComponent" );

        assertEquals( "testComponent", newLiferayComponentWizard.getProjectName().getText() );
        assertEquals( TEXT_DEFAULT_PACKAGE_NAME_VALUE, newLiferayComponentWizard.getPackageName().getText() );
        assertEquals(
            TEXT_DEFAULT_COMPONENT_CLASS_NAME_VALUE, newLiferayComponentWizard.getComponentClassName().getText() );
        assertEquals(
            TEXT_DEFAULT_COMPONENT_CLASS_TEMPLATE_VALUE,
            newLiferayComponentWizard.getComponentClassTemplate().getText() );
        assertEquals( TEXT_CREATE_COMPONENT_CLASS_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertTrue( newLiferayComponentWizard.finishButton().isEnabled() );

        // validation test for Package name
        newLiferayComponentWizard.getPackageName().setText( "1" );
        sleep( 1000 );
        assertEquals( TEXT_ENTER_PROJECT_NAME_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.getPackageName().setText( "-" );
        sleep( 1000 );
        assertEquals( " \"-\"" + TEXT_VALIDATION_PACKAGE_NAME_MESSAGE,
            newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.getPackageName().setText( "." );
        sleep( 1000 );
        assertEquals( " \".\"" + TEXT_VALIDATION_PACKAGE_NAME_MESSAGE,
            newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.getPackageName().setText( "/" );
        sleep( 1000 );
        assertEquals( " \"/\"" + TEXT_VALIDATION_PACKAGE_NAME_MESSAGE,
            newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.getPackageName().setText( "a" );
        sleep( 1000 );
        assertEquals( TEXT_CREATE_COMPONENT_CLASS_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertTrue( newLiferayComponentWizard.finishButton().isEnabled() );

        newLiferayComponentWizard.getBrowseButton().click();

        ComponentPackageSelectionPO selectPackageName = new ComponentPackageSelectionPO( bot, BUTTON_OK );

        selectPackageName.setPackageSelectionText( "content" );
        selectPackageName.confirm();

        assertEquals( TEXT_CREATE_COMPONENT_CLASS_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertTrue( newLiferayComponentWizard.finishButton().isEnabled() );

        // validation test for Component Class Name
        newLiferayComponentWizard.getComponentClassName().setText( "1" );
        sleep( 1000 );
        assertEquals( TEXT_VALIDATION_COMPONENT_CLASS_TEMPLATE_MESSAGE,
            newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.getComponentClassName().setText( "-" );
        sleep( 1000 );
        assertEquals( TEXT_VALIDATION_COMPONENT_CLASS_TEMPLATE_MESSAGE,
            newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.getComponentClassName().setText( "." );
        sleep( 1000 );
        assertEquals( TEXT_VALIDATION_COMPONENT_CLASS_TEMPLATE_MESSAGE,
            newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.getComponentClassName().setText( "/" );
        sleep( 1000 );
        assertEquals( TEXT_VALIDATION_COMPONENT_CLASS_TEMPLATE_MESSAGE,
            newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.getComponentClassName().setText( "a" );
        sleep( 1000 );
        assertEquals( TEXT_CREATE_COMPONENT_CLASS_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertTrue( newLiferayComponentWizard.finishButton().isEnabled() );

        newLiferayComponentWizard.getComponentClassName().setText( "" );
        newLiferayComponentWizard.finish();
        sleep();

        TreePO projectTree = eclipse.getPackageExporerView().getProjectTree();

        assertTrue(
            projectTree.expandNode(
                "testComponent", "src/main/java", "content", "TestcomponentPortlet.java" ).isVisible() );

        eclipse.getPackageExporerView().deleteResouceByName( "testComponent", true );

    }

}

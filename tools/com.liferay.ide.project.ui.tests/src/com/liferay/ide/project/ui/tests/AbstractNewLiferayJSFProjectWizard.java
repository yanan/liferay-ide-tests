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

import org.junit.Assume;
import org.junit.Before;

import com.liferay.ide.project.ui.tests.page.NewLiferayJSFProjectWizardPO;
import com.liferay.ide.ui.tests.swtbot.page.TreePO;

/**
 * @author Ying Xu
 */
public abstract class AbstractNewLiferayJSFProjectWizard extends AbstractNewLiferayModuleProjectWizard
    implements NewLiferayJSFProjectWizard
{

    NewLiferayJSFProjectWizardPO newJSFProject =
        new NewLiferayJSFProjectWizardPO( bot, INDEX_NEW_JSF_PROJECT_VALIDATION_MESSAGE );

    TreePO projectTree = eclipse.getPackageExporerView().getProjectTree();

    @Before
    public void openWizard()
    {
        Assume.assumeTrue( runTest() || runAllTests() );

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayJSFProject().click();
        sleep();
    }

    public void newLiferayJSFProject(
        String projectName, String buildFramework, String componentSuite, String defaultLocation )
    {
        assertEquals( TEXT_ENTER_PROJECT_NAME, newJSFProject.getValidationMessage() );
        assertEquals( "", newJSFProject.getProjectName().getText() );
        assertEquals( MENU_JSF_STANDARD, newJSFProject.getComponentSuite().getText() );
        assertTrue( newJSFProject.getUseDefaultLocation().isChecked() );

        newJSFProject.createJSFProject( projectName, buildFramework, componentSuite );
        sleep( 2000 );

        assertEquals( buildFramework, newJSFProject.getBuildFramework().getText() );

        String[] JSFProjectcomponentSuiteItems = newJSFProject.getComponentSuite().getAvailableComboValues();

        assertTrue( JSFProjectcomponentSuiteItems.length >= 1 );
        assertEquals( expectedJSFProjectcomponentSuiteItems.length, JSFProjectcomponentSuiteItems.length );

        for( int i = 0; i < JSFProjectcomponentSuiteItems.length; i++ )
        {
            assertTrue( JSFProjectcomponentSuiteItems[i].equals( expectedJSFProjectcomponentSuiteItems[i] ) );
        }

        assertTrue( newJSFProject.finishButton().isEnabled() );
        newJSFProject.finish();
        sleep( 9000 );

    }

    public boolean checkProjectAndFileExist( String projectName, String... nodes )
    {
        String[] expandNodes = new String[nodes.length - 1];

        for( int i = 0; i < nodes.length - 1; i++ )
        {
            expandNodes[i] = nodes[i];
        }

        projectTree.setFocus();

        return projectTree.expandNode( expandNodes ).isVisible();
    }

    public void validationProjectName()
    {
        assertEquals( TEXT_ENTER_PROJECT_NAME, newJSFProject.getValidationMessage() );
        assertFalse( newJSFProject.finishButton().isEnabled() );

        newJSFProject.createJSFProject( "." );
        sleep();
        assertEquals( " '.'" + TEXT_INVALID_NAME_ON_THIS_PLATFORM, newJSFProject.getValidationMessage() );
        assertFalse( newJSFProject.finishButton().isEnabled() );

        newJSFProject.createJSFProject( "/" );
        sleep();
        assertEquals(
            " /" + TEXT_IS_INVALID_CHARACTER_IN_RESOURCE_NAME + "'/'.", newJSFProject.getValidationMessage() );
        assertFalse( newJSFProject.finishButton().isEnabled() );

        newJSFProject.createJSFProject( "$" );
        sleep();
        assertEquals( TEXT_PROJECT_NAME_INVALID, newJSFProject.getValidationMessage() );
        assertFalse( newJSFProject.finishButton().isEnabled() );

        newJSFProject.createJSFProject( "" );
        sleep();
        assertEquals( TEXT_PROJECT_NAME_MUST_SPECIFIED, newJSFProject.getValidationMessage() );
        assertFalse( newJSFProject.finishButton().isEnabled() );

        newJSFProject.createJSFProject( "a" );
        sleep();
        assertEquals( TEXT_CHOOSE_TEMPLATE_FOR_NEW_JSF_PROJECT, newJSFProject.getValidationMessage() );
        assertTrue( newJSFProject.finishButton().isEnabled() );

        newJSFProject.cancel();
    }

}

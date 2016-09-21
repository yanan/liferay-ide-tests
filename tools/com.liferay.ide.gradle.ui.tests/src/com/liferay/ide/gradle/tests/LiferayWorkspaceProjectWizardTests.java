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

package com.liferay.ide.gradle.tests;

import static org.junit.Assert.assertEquals;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.liferay.ide.ui.tests.SWTBotBase;
import com.liferay.ide.gradle.ui.tests.page.CreateWorkspaceProjectWizardPO;

/**
 * @author Vicky Wang
 */
public class LiferayWorkspaceProjectWizardTests extends SWTBotBase implements LiferayWorkspaceProjectWizard
{
    String projectName = "workspace-project";
    String serverName = "Liferay 7.0 CE Server";

    @AfterClass
    public static void cleanAll()
    {
        eclipse.closeShell( WINDOW_NEW_LIFERAY_WORKSPACE );
    }

    @Test
    public void liferayWorksapceProjectWizard()
    {
        CreateWorkspaceProjectWizardPO newWorkspaceProjectPage =
            new CreateWorkspaceProjectWizardPO( bot, INDEX_VALIDATION_PAGE_MESSAGE3 );

        newWorkspaceProjectPage.setWorkspaceName( ".." );
        sleep();

        assertEquals( " '..'" + TEXT_INVALID_NAME_ON_PLATFORM, newWorkspaceProjectPage.getValidationMessage() );

        newWorkspaceProjectPage.setWorkspaceName( "##" );
        sleep();
        assertEquals( " " + TEXT_INVALID_NAME_PROJECT, newWorkspaceProjectPage.getValidationMessage() );

        newWorkspaceProjectPage.setWorkspaceName( "*" );
        sleep();
        assertEquals( " *" + TEXT_INVALID_CHARACTER_IN_WORKSPACE_NAME + "'*'.", newWorkspaceProjectPage.getValidationMessage() );

        newWorkspaceProjectPage.setWorkspaceName( TEXT_BLANK );
        sleep();
        assertEquals(  TEXT_ENTER_PROJECT_NAME, newWorkspaceProjectPage.getValidationMessage() );

        newWorkspaceProjectPage.setWorkspaceName( projectName );
        sleep();

        assertEquals( false, newWorkspaceProjectPage.isDownloadLiferayBundleChecked() );

        newWorkspaceProjectPage.get_downloadLiferayBundleCheckbox().select();
        newWorkspaceProjectPage.setServerName( serverName );
        newWorkspaceProjectPage.finish();

        sleep( 20000 );

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayWorkspaceProject().click();
        assertEquals( " A Liferay Workspace project already exists in this Eclipse instance.", 
            newWorkspaceProjectPage.getValidationMessage() );

    }

    @Before
    public void openWizard()
    {
        eclipse.getLiferayWorkspacePerspective().activate();
        eclipse.getCreateLiferayProjectToolbar().getNewLiferayWorkspaceProject().click();
    }

}

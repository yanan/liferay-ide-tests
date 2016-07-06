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

package com.liferay.ide.layouttpl.ui.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.liferay.ide.layouttpl.ui.tests.pages.ChooseInitialTemplatePO;
import com.liferay.ide.layouttpl.ui.tests.pages.CreateLayoutTemplateWizardPO;
import com.liferay.ide.layouttpl.ui.tests.pages.TempalteSelectionDialogPO;
import com.liferay.ide.project.ui.tests.ProjectWizard;
import com.liferay.ide.project.ui.tests.page.CreateProjectWizardPO;
import com.liferay.ide.project.ui.tests.page.SetSDKLocationPO;
import com.liferay.ide.ui.tests.SWTBotBase;

/**
 * @author Li Lu
 */
public class NewLayoutTemplateWizardTests extends SWTBotBase implements CreateLayouttplWizard, ProjectWizard
{

    static String projectName = "test-layouttpl";

    @BeforeClass
    public static void createProject()
    {
        eclipse.getCreateLiferayProjectToolbar().getNewLiferayPluginProject().click();

        CreateProjectWizardPO page1 = new CreateProjectWizardPO( bot );
        page1.createSDKProject( projectName, MENU_LAYOUT_TEMPLATE );

        if( page1.finishButton().isEnabled() )
        {
            page1.finish();
        }
        else
        {
            page1.next();
            SetSDKLocationPO page2 = new SetSDKLocationPO( bot, "" );
            page2.setSdkLocation( getLiferayPluginsSdkDir().toString() );
            page2.finish();
        }
    }

    CreateLayoutTemplateWizardPO page = new CreateLayoutTemplateWizardPO( bot );

    ChooseInitialTemplatePO page2 = new ChooseInitialTemplatePO( bot );

    @After
    public void closeWizard()
    {
        eclipse.closeShell( TITLE_NEW_LAYOUT_TEMPLATE );
    }

    @Before
    public void openWizard()
    {
        eclipse.getCreateLiferayProjectToolbar().getNewLiferayLayoutTemplate().click();
    }

    @Test
    public void testContentDefaultValues() throws Exception
    {
        // check default values page 1
        assertEquals( projectName, page.getLayoutPluginProjectText() );
        assertEquals( "New Template", page.getNameText() );
        assertEquals( "newtemplate", page.getIdText() );
        assertEquals( "/newtemplate.tpl", page.getTemplateFileText() );
        assertEquals( "/newtemplate.wap.tpl", page.getWapTemplateFileText() );
        assertEquals( "/newtemplate.png", page.getThumbnailFileText() );
        // page2
        page.next();
        assertEquals( true, page2.isRadioSelected( 0 ) );
        assertEquals( false, page2.isRadioSelected( 1 ) );
        assertEquals( false, page2.isRadioSelected( 2 ) );
        assertEquals( false, page2.isRadioSelected( 3 ) );
        assertEquals( false, page2.isRadioSelected( 4 ) );
        assertEquals( false, page2.isRadioSelected( 5 ) );
        assertEquals( false, page2.isRadioSelected( 6 ) );
        assertEquals( false, page2.isRadioSelected( 7 ) );
        assertEquals( false, page2.isRadioSelected( 8 ) );
    }

    @Test
    public void testCreateLayoutTemplate()
    {
        page.setNameText( "Test Template" );
        page.setIdText( "testtesttemplate" );
        page.setTemplateFileText( "testtemplate.tpl" );
        page.setWapTemplateFileText( "testtemplate.wap.tpl" );
        page.setThumbnailFileText( "testtemplate.png" );

        page.next();
        ChooseInitialTemplatePO page2 = new ChooseInitialTemplatePO( bot );
        page2.selectRadio( 1 );
        page.finish();

        sleep( 2000 );
        assertEquals( true, eclipse.getProjectTree().hasTreeItem( projectName, "docroot", "testtemplate.tpl" ) );
        assertEquals( true, eclipse.getProjectTree().hasTreeItem( projectName, "docroot", "testtemplate.wap.tpl" ) );
        assertEquals( true, eclipse.getProjectTree().hasTreeItem( projectName, "docroot", "testtemplate.png" ) );

    }

    @Test
    public void testID()
    {
        page.setIdText( "" );

        assertEquals( "New Template", page.getNameText() );
        assertEquals( "/.tpl", page.getTemplateFileText() );
        assertEquals( "/.wap.tpl", page.getWapTemplateFileText() );
        assertEquals( "/.png", page.getThumbnailFileText() );
        assertEquals( TEXT_ID_CANNT_BE_EMPTY, page.getValidationMessage() );
        assertEquals( false, page.finishButton().isEnabled() );

        page.setIdText( "layout test" );
        assertEquals( "New Template", page.getNameText() );
        assertEquals( "/layout test.tpl", page.getTemplateFileText() );
        assertEquals( "/layout test.wap.tpl", page.getWapTemplateFileText() );
        assertEquals( "/layout test.png", page.getThumbnailFileText() );
        assertEquals( TEXT_ID_INVALID, page.getValidationMessage() );
        assertEquals( false, page.finishButton().isEnabled() );

        page.setIdText( "newtemplate" );
        assertEquals( TEXT_DEFAULT_MESSAGE, page.getValidationMessage() );
    }

    @Test
    public void testName()
    {
        page.setNameText( "" );

        assertEquals( "", page.getIdText() );
        assertEquals( "/.tpl", page.getTemplateFileText() );
        assertEquals( "/.wap.tpl", page.getWapTemplateFileText() );
        assertEquals( "/.png", page.getThumbnailFileText() );
        assertEquals( TEXT_ID_CANNT_BE_EMPTY, page.getValidationMessage() );
        assertEquals( false, page.finishButton().isEnabled() );

        page.setNameText( "New_ Template" );
        assertEquals( "newtemplate", page.getIdText() );
        assertEquals( TEXT_DEFAULT_MESSAGE, page.getValidationMessage() );

        page.setNameText( "" );
        page.setIdText( "newtemplate" );
        assertEquals( TEXT_DEFAULT_MESSAGE, page.getValidationMessage() );
    }

    @Test
    public void testTemplateFile()
    {
        page.clickBrowseButton( 0 );

        TempalteSelectionDialogPO templateFileSelection = new TempalteSelectionDialogPO( bot );

        assertTrue( templateFileSelection.containsItem( "test.tpl" ) );
        assertTrue( templateFileSelection.containsItem( "test.png" ) );
        assertTrue( templateFileSelection.containsItem( "blank_columns.wap.tpl" ) );

        templateFileSelection.select( "WEB-INF" );
        assertEquals( TEXT_CHOOSE_VALID_PROJECT_FILE, templateFileSelection.getValidationMessage() );
        assertEquals( false, templateFileSelection.canFinish() );

        templateFileSelection.select( "test.tpl" );
        templateFileSelection.confirm();

        assertEquals( TEXT_TEMPLATE_FILE_EXIST, page.getValidationMessage() );
        assertEquals( true, page.finishButton().isEnabled() );

        page.setTemplateFileText( "" );
        assertEquals( TEXT_TEMPLATE_FILE_INVALID, page.getValidationMessage() );
        assertEquals( false, page.finishButton().isEnabled() );

        page.setTemplateFileText( "aa.tpl" );
        assertEquals( true, page.finishButton().isEnabled() );
    }

    @Test
    public void testThumbnailFile()
    {
        page.clickBrowseButton( 2 );

        TempalteSelectionDialogPO thumbnailFileSelection = new TempalteSelectionDialogPO( bot );

        assertTrue( thumbnailFileSelection.containsItem( "test.tpl" ) );
        assertTrue( thumbnailFileSelection.containsItem( "test.png" ) );
        assertTrue( thumbnailFileSelection.containsItem( "blank_columns.wap.tpl" ) );

        thumbnailFileSelection.select( "WEB-INF" );

        assertEquals( TEXT_CHOOSE_VALID_PROJECT_FILE, thumbnailFileSelection.getValidationMessage() );
        assertEquals( false, thumbnailFileSelection.canFinish() );

        thumbnailFileSelection.select( "blank_columns.wap.tpl" );
        thumbnailFileSelection.confirm();

        assertEquals( TEXT_THUMBNAIL_FILE_EXIST, page.getValidationMessage() );
        assertEquals( true, page.finishButton().isEnabled() );

        page.setThumbnailFileText( "" );
        assertEquals( TEXT_THUMBNAIL_FILE_INVALID, page.getValidationMessage() );
        assertEquals( false, page.finishButton().isEnabled() );

        page.setThumbnailFileText( "/aa.wap.tpl" );
        assertEquals( true, page.finishButton().isEnabled() );
    }

    @Test
    public void testWapTemplateFile()
    {
        page.clickBrowseButton( 1 );

        TempalteSelectionDialogPO templateFileSelection = new TempalteSelectionDialogPO( bot );

        assertTrue( templateFileSelection.containsItem( "test.tpl" ) );
        assertTrue( templateFileSelection.containsItem( "test.png" ) );
        assertTrue( templateFileSelection.containsItem( "blank_columns.wap.tpl" ) );

        templateFileSelection.select( "WEB-INF" );
        assertEquals( TEXT_CHOOSE_VALID_PROJECT_FILE, templateFileSelection.getValidationMessage() );
        assertEquals( false, templateFileSelection.canFinish() );

        templateFileSelection.select( "blank_columns.wap.tpl" );
        templateFileSelection.confirm();

        assertEquals( TEXT_WAP_TEMPLATE_FILE_EXIST, page.getValidationMessage() );
        assertEquals( true, page.finishButton().isEnabled() );

        page.setWapTemplateFileText( "" );
        assertEquals( TEXT_WAP_TEMPLATE_FILE_INVALID, page.getValidationMessage() );
        assertEquals( false, page.finishButton().isEnabled() );

        page.setWapTemplateFileText( "/aa.wap.tpl" );
        assertEquals( true, page.finishButton().isEnabled() );
    }
}

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

import static org.eclipse.swtbot.swt.finder.SWTBotAssert.assertContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.liferay.ide.project.ui.tests.page.CreateProjectWizardPO;
import com.liferay.ide.project.ui.tests.page.SelectPortletFrameworkPO;
import com.liferay.ide.project.ui.tests.page.SetSDKLocationPO;
import com.liferay.ide.project.ui.tests.page.ThemeWizardPO;
import com.liferay.ide.ui.tests.SWTBotBase;
import com.liferay.ide.ui.tests.swtbot.page.ShellPO;
import com.liferay.ide.ui.tests.swtbot.page.TextEditorPO;
import com.liferay.ide.ui.tests.swtbot.page.TreePO;

/**
 * @author Terry Jia
 * @author Ashley Yuan
 * @author Vicky Wang
 * @author Ying Xu
 */
public class ProjectWizardTests extends SWTBotBase implements ProjectWizard
{

    @After
    public void waitForCreate()
    {
        // sleep( 5000 );
    }

    @AfterClass
    public static void cleanAll()
    {
    	eclipse.closeShell( TITLE_NEW_LIFERAY_PROJECT );
    	eclipse.closeShell( TITLE_NEW_LIFERAY_PORTLET );
        eclipse.getPackageExporerView().deleteProjectExcludeNames( new String[] { getLiferayPluginsSdkName() }, true );
    }

    @Ignore
    public void createExtProject()
    {
        CreateProjectWizardPO page1 = new CreateProjectWizardPO( bot );
        page1.createSDKProject( "text", MENU_EXT );

        if( hasAddedProject )
        {
            assertEquals( MESAGE_SDK_NOT_SUPPORT, page1.getValidationMessage() );
            page1.cancel();
        }
        else
        {
            page1.next();

            SetSDKLocationPO page2 = new SetSDKLocationPO( bot );
            page2.setSdkLocation( getLiferayPluginsSdkDir().toString() );
            assertEquals( MESAGE_SDK_NOT_SUPPORT, page2.getValidationMessage() );
            page2.cancel();
        }
    }

    @Test
    public void createHookProject()
    {
        CreateProjectWizardPO page1 = new CreateProjectWizardPO( bot );
        page1.createSDKProject( "text", MENU_HOOK );

        if( hasAddedProject )
        {
            page1.finish();
        }
        else
        {
            page1.next();

            SetSDKLocationPO page2 = new SetSDKLocationPO( bot );
            page2.setSdkLocation( getLiferayPluginsSdkDir().toString() );
            page2.finish();
        }
    }

    @Test
    public void createServiceBuilderPortletProject()
    {
        CreateProjectWizardPO page1 = new CreateProjectWizardPO( bot );
        page1.createSDKProject( "textsb", MENU_SERVICE_BUILDER_PORTLET, true );

        if( hasAddedProject )
        {
            page1.finish();

            page1.sleep();
        }
        else
        {
            page1.next();

            SetSDKLocationPO page2 = new SetSDKLocationPO( bot );
            page2.setSdkLocation( getLiferayPluginsSdkDir().toString() );
            page2.finish();
        }
    }

    @Test
    public void createServiceBuilderPortletProjectWithoutSampleCode()
    {
        CreateProjectWizardPO page1 = new CreateProjectWizardPO( bot );

        page1.createSDKProject( "textsbwithoutcode", MENU_SERVICE_BUILDER_PORTLET, false );

        if( hasAddedProject )
        {
            page1.finish();
        }
        else
        {
            page1.next();

            SetSDKLocationPO page2 = new SetSDKLocationPO( bot );
            page2.setSdkLocation( getLiferayPluginsSdkDir().toString() );
            page2.finish();
        }
    }

    @Test
    public void createLayoutProject()
    {
        CreateProjectWizardPO page1 = new CreateProjectWizardPO( bot );

        page1.createSDKProject( "text", MENU_LAYOUT_TEMPLATE );

        if( hasAddedProject )
        {
            page1.finish();
        }
        else
        {
            page1.next();

            SetSDKLocationPO page2 = new SetSDKLocationPO( bot );
            page2.setSdkLocation( getLiferayPluginsSdkDir().toString() );
            page2.finish();
        }
    }

    @Test
    public void createPortletProject()
    {
        String projectName = "testPortlet";

        CreateProjectWizardPO page1 = new CreateProjectWizardPO( bot, TOOLTIP_MENU_ITEM_NEW_LIFERAY_PROJECT );

        page1.createSDKProject( projectName, MENU_PORTLET, true, false );

        assertTrue( page1.IsIncludeSimpleCodeCheckBoxChecked() );
        assertFalse( page1.IsLaunchNewPortletWizardCheckBoxChecked() );

        page1.next();

        SelectPortletFrameworkPO page2 = new SelectPortletFrameworkPO( bot );

        assertTrue( page2.IsLiferayMVCRadioSelected() );

        if( !hasAddedProject )
        {
            page1.next();

            SetSDKLocationPO page3 = new SetSDKLocationPO( bot );

            page3.setSdkLocation( getLiferayPluginsSdkDir().toString() );
        }

        page1.finish();

        // check in console and package explorer
        // assertTrue( UITestsUtils.checkConsoleMessage( "BUILD SUCCESSFUL", "Java" ) );

        TreePO projectTree = eclipse.showPackageExporerView().getProjectTree();

        String fileName = "liferay-display.xml";

        projectTree.expandNode( projectName + "-portlet", "docroot", "WEB-INF" ).doubleClick( fileName );

        TextEditorPO editor = eclipse.getTextEditor( fileName );

        assertTrue( editor.isActive() );

        assertContains( "sample", editor.getText() );

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayPluginProject().click();

        CreateProjectWizardPO page4 = new CreateProjectWizardPO( bot, INDEX_NEW_LIFERAY_PLUGIN_PROJECT_VALIDATION_MESSAGE );

        page4.createSDKPortletProject( projectName );
        assertEquals( TEXT_PROJECT_ALREADY_EXISTS, page4.getValidationMessage() );

        page4.createSDKPortletProject( projectName + "-portlet" );
        assertEquals( TEXT_PROJECT_ALREADY_EXISTS, page4.getValidationMessage() );

        page4.cancel();

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayPluginProject().click();

        CreateProjectWizardPO page5 = new CreateProjectWizardPO( bot, INDEX_NEW_LIFERAY_PLUGIN_PROJECT_VALIDATION_MESSAGE );

        page5.createSDKPortletProject( projectName );
        assertContains( TEXT_PROJECT_ALREADY_EXISTS, page5.getValidationMessage() );

        page5.createSDKPortletProject( projectName + "-portlet" );
        assertContains( TEXT_PROJECT_ALREADY_EXISTS, page5.getValidationMessage() );

        page5.cancel();

        deleteProjectInSdk( projectName + "-portlet" );
    }

    @Test
    public void createPortletProjectWithoutSampleAndLaunchNewPortletWizard()
    {
        CreateProjectWizardPO page1 = new CreateProjectWizardPO( bot );

        page1.createSDKProject( "NoSampleTest", MENU_PORTLET, false, true );

        assertFalse( page1.IsIncludeSimpleCodeCheckBoxChecked() );
        assertTrue( page1.IsLaunchNewPortletWizardCheckBoxChecked() );

        page1.next();

        SelectPortletFrameworkPO page2 = new SelectPortletFrameworkPO( bot );

        assertTrue( page2.IsLiferayMVCRadioSelected() );

        if( !hasAddedProject )
        {
            page1.next();

            SetSDKLocationPO page3 = new SetSDKLocationPO( bot );

            page3.setSdkLocation( getLiferayPluginsSdkDir().toString() );
        }

        page1.finish();

        ShellPO newPortletPage = new ShellPO( bot, "New Liferay Portlet" ){};

        newPortletPage.waitForPageToOpen();

        assertEquals( TOOLTIP_NEW_LIFERAY_PORTLET, newPortletPage.getTitle() );

        newPortletPage.closeIfOpen();
    }

    @Test
    public void createThemeProject()
    {
        CreateProjectWizardPO page1 = new CreateProjectWizardPO( bot );
        ThemeWizardPO page2 = new ThemeWizardPO( bot, INDEX_NEW_LIFERAY_PLUGIN_PROJECT_VALIDATION_MESSAGE );

        String defaultMessage = "Select options for creating new theme project.";
        String warningMessage = " For advanced theme developers only.";

        String projectThemeName = "test";

        page1.createSDKProject( projectThemeName, MENU_THEME );

        page1.next();

        // assertEquals( defaultMessage, page2.getValidationMessage() );

        page2.setParentFramework( MENU_THEME_PARENT_UNSTYLED, MENU_THEME_FRAMEWORK_JSP );
        // assertEquals( warningMessage, page2.getValidationMessage() );

        page2.setParentFramework( MENU_THEME_PARENT_CLASSIC, MENU_THEME_FRAMEWORK_VELOCITY );
        // assertEquals( defaultMessage, page2.getValidationMessage() );

        page2.setParentFramework( MENU_THEME_PARENT_STYLED, MENU_THEME_FRAMEWORK_FREEMARKER );

        if( hasAddedProject )
        {
            page2.finish();
        }
        else
        {
            page2.next();

            SetSDKLocationPO page3 = getSetSDKLoactionPage();

            page3.finish();
        }

        // sleep( 5000 );

        deleteProjectInSdk( projectThemeName + "-theme" );
    }

    @Test
    public void createWebProject()
    {
        CreateProjectWizardPO page1 = new CreateProjectWizardPO( bot, INDEX_NEW_LIFERAY_PLUGIN_PROJECT_VALIDATION_MESSAGE );

        page1.createSDKProject( "text", MENU_WEB );

        if( hasAddedProject )
        {
            sleep();
            //assertEquals( TEXT_WEB_SDK_62_ERRORR_MESSAGE, page1.getValidationMessage() );
            page1.cancel();
        }
        else
        {
            page1.next();
            SetSDKLocationPO page2 = new SetSDKLocationPO( bot, INDEX_VALIDATION_MESSAGE2 );

            page2.setSdkLocation( getLiferayPluginsSdkDir().toString() );

            sleep();
            //assertEquals( TEXT_WEB_SDK_62_ERRORR_MESSAGE, page2.getValidationMessage() );
            page2.cancel();
        }
    }

    @Test
    public void validationProjectName()
    {
        String invalidNameDoubleDash = "--";

        CreateProjectWizardPO page1 = new CreateProjectWizardPO( bot, INDEX_NEW_LIFERAY_PLUGIN_PROJECT_VALIDATION_MESSAGE );

        page1.createSDKPortletProject( invalidNameDoubleDash );

        sleep();
        assertEquals( " The project name is invalid.", page1.getValidationMessage() );

        page1.cancel();
    }

    public static void deleteProjectInSdk( String projectName )
    {
        eclipse.getPackageExporerView().deleteResouceByName( projectName, true );
    }

    private SetSDKLocationPO getSetSDKLoactionPage()
    {
        SetSDKLocationPO page = new SetSDKLocationPO( bot );
        page.setSdkLocation( getLiferayPluginsSdkDir().toString() );

        return page;
    }

    @Before
    public void openWizard()
    {
        hasAddedProject = addedProjects();

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayPluginProject().click();
    }

}

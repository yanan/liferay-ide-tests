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

import org.eclipse.swtbot.swt.finder.keyboard.Keyboard;
import org.eclipse.swtbot.swt.finder.keyboard.KeyboardFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.liferay.ide.project.ui.tests.page.CreateModuleProjectWizardPO;
import com.liferay.ide.project.ui.tests.page.ModuleProjectWizardSecondPagePO;
import com.liferay.ide.project.ui.tests.page.NewLiferayWorkspaceProjectWizardPO;
import com.liferay.ide.project.ui.tests.page.SelectModuleServiceNamePO;
import com.liferay.ide.ui.tests.SWTBotBase;
import com.liferay.ide.ui.tests.swtbot.eclipse.page.DeleteResourcesContinueDialogPO;
import com.liferay.ide.ui.tests.swtbot.eclipse.page.DeleteResourcesDialogPO;
import com.liferay.ide.ui.tests.swtbot.page.TextEditorPO;
import com.liferay.ide.ui.tests.swtbot.page.TreePO;

/**
 * @author Ying Xu
 */
public class ModuleProjectWizardTests extends SWTBotBase implements ModuleProjectWizard
{

    public static boolean finishButtonState = false;

    String liferayWorkspaceName = "liferayWorkspace";

    Keyboard keyPress = KeyboardFactory.getAWTKeyboard();

    NewLiferayWorkspaceProjectWizardPO newLiferayWorkspace = new NewLiferayWorkspaceProjectWizardPO( bot );

    TreePO projectTree = eclipse.getPackageExporerView().getProjectTree();

    DeleteResourcesDialogPO deleteResources = new DeleteResourcesDialogPO( bot );

    DeleteResourcesContinueDialogPO continueDeleteResources =
        new DeleteResourcesContinueDialogPO( bot, "Delete Resources" );

    CreateModuleProjectWizardPO createModuleProjectWizard = new CreateModuleProjectWizardPO( bot );

    @AfterClass
    public static void cleanAll()
    {
        eclipse.closeShell( LABEL_NEW_LIFERAY_MODULE_PROJECT );
        eclipse.getPackageExporerView().deleteProjectExcludeNames( new String[] { getLiferayPluginsSdkName() }, true );
    }

    public boolean ifAddedLiferayWorksapce()
    {
        newLiferayWorkspace.setWorkspaceNameText( liferayWorkspaceName );
        sleep( 2000 );

        return newLiferayWorkspace.finishButton().isEnabled();
    }

    @BeforeClass
    public static void waitForOpenModuleProjectWizard()
    {
        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();

        CreateModuleProjectWizardPO createModuleProjectWizard =
            new CreateModuleProjectWizardPO( bot, INDEX_NEW_LIFERAY_MODULE_PROJECT_VALIDATION_MESSAGE );

        assertEquals( TEXT_ENTER_MODULE_PROJECT_NAME_MESSAGE, createModuleProjectWizard.getValidationMessage() );

        createModuleProjectWizard.createModuleProject( "test" );
        sleep( 1000 );
        assertEquals( TEXT_DOWNLOADING_TEMPLATE_MESSAGE, createModuleProjectWizard.getValidationMessage() );

        sleep( 45000 );
        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );

        createModuleProjectWizard.cancel();
    }

    @Test
    public void createMvcportletModuleProject()
    {
        String projectName = "testMvcportletProject";

        if( !finishButtonState )
        {
            newLiferayWorkspace.cancel();
            sleep();

            projectTree.getTreeItem( liferayWorkspaceName ).doAction( BUTTON_DELETE );
            sleep( 2000 );

            deleteResources.confirmDeleteFromDisk();
            deleteResources.confirm();

            try
            {
                continueDeleteResources.clickContinueButton();
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }

            sleep( 5000 );
        }
        else
        {
            newLiferayWorkspace.cancel();
        }

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();

        createModuleProjectWizard.createModuleProject( projectName );

        assertEquals( MENU_MODULE_MVCPORTLET, createModuleProjectWizard.getProjectTemplateNameComboBox().getText() );

        String[] expectedModuleProjectTemplateItems =
            { MENU_MODULE_ACTIVATOR, MENU_MODULE_API, MENU_MODULE_CONTENTTARGETINGGREPORT,
                MENU_MODULE_CONTENTTARGETINGRULE, MENU_MODULE_CONTENTTARGETINGTRACKINGACTION,
                MENU_MODULE_CONTROLMENUENTRY, MENU_MODULE_MVCPORTLET, MENU_MODULE_PANELAPP, MENU_MODULE_PORTLET,
                MENU_MODULE_PORTLETCONFIGURATIONICON, MENU_MODULE_PORTLETPROVIDER,
                MENU_MODULE_PORTLETTOOLBARCONTRIBUTOR, MENU_MODULE_SERVICE, MENU_MODULE_SERVICEBUILDER,
                MENU_MODULE_SERVICEWRAPPER, MENU_MODULE_SIMULATIONPANELENTRY, MENU_MODULE_TEMPLATECONTEXTCONTRIBUTOR };

        String[] moduleProjectTemplateItems =
            createModuleProjectWizard.getProjectTemplateNameComboBox().getAvailableComboValues();

        assertTrue( moduleProjectTemplateItems.length >= 1 );

        assertEquals( expectedModuleProjectTemplateItems.length, moduleProjectTemplateItems.length );

        for( int i = 0; i < moduleProjectTemplateItems.length; i++ )
        {
            assertTrue( moduleProjectTemplateItems[i].equals( expectedModuleProjectTemplateItems[i] ) );
        }

        createModuleProjectWizard.next();

        ModuleProjectWizardSecondPagePO createModuleProjectSecondPageWizard =
            new ModuleProjectWizardSecondPagePO( bot, INDEX_CONFIGURE_COMPONENT_CLASS_VALIDATION_MESSAGE );

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );

        // add properties then check toolbarButton state
        assertTrue( createModuleProjectSecondPageWizard.getAddPropertyKeyButton().isEnabled() );
        assertFalse( createModuleProjectSecondPageWizard.getDeleteButton().isEnabled() );
        assertFalse( createModuleProjectSecondPageWizard.getMoveUpButton().isEnabled() );
        assertFalse( createModuleProjectSecondPageWizard.getMoveDownButton().isEnabled() );

        createModuleProjectSecondPageWizard.getAddPropertyKeyButton().click();
        sleep();
        createModuleProjectSecondPageWizard.setPropertiesText( 2, "a" );
        sleep();
        createModuleProjectSecondPageWizard.getProperties().doubleClick( 0, 1 );
        sleep();
        createModuleProjectSecondPageWizard.setPropertiesText( 2, "b" );
        sleep();

        keyPress.pressShortcut( enter );
        sleep();

        assertTrue( createModuleProjectSecondPageWizard.getDeleteButton().isEnabled() );
        assertFalse( createModuleProjectSecondPageWizard.getMoveUpButton().isEnabled() );
        assertFalse( createModuleProjectSecondPageWizard.getMoveDownButton().isEnabled() );

        createModuleProjectSecondPageWizard.getAddPropertyKeyButton().click();
        sleep();
        createModuleProjectSecondPageWizard.setPropertiesText( 2, "c" );
        sleep();
        createModuleProjectSecondPageWizard.getProperties().doubleClick( 1, 1 );
        sleep();
        createModuleProjectSecondPageWizard.setPropertiesText( 2, "d" );
        sleep();

        keyPress.pressShortcut( enter );
        sleep();

        assertTrue( createModuleProjectSecondPageWizard.getMoveUpButton().isEnabled() );
        assertFalse( createModuleProjectSecondPageWizard.getMoveDownButton().isEnabled() );
        createModuleProjectSecondPageWizard.getMoveUpButton().click();
        assertFalse( createModuleProjectSecondPageWizard.getMoveUpButton().isEnabled() );
        assertTrue( createModuleProjectSecondPageWizard.getMoveDownButton().isEnabled() );
        createModuleProjectSecondPageWizard.getMoveDownButton().click();

        createModuleProjectSecondPageWizard.getDeleteButton().click();
        createModuleProjectSecondPageWizard.getDeleteButton().click();

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        String javaFileName = "TestmvcportletprojectPortlet.java";

        projectTree.expandNode( projectName, "src/main/java", "testmvcportletproject.portlet" ).doubleClick(
            javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "extends MVCPortlet", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String buildGradleFileName = "build.gradle";

        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createMvcportletModuleProjectInLiferayWorkspace()
    {
        if( finishButtonState )
        {
            newLiferayWorkspace.finish();
            sleep( 20000 );
        }
        else
        {
            newLiferayWorkspace.cancel();
        }

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();

        String projectName = "testMvcportletInLS";

        createModuleProjectWizard.createModuleProject( projectName );

        assertEquals( MENU_MODULE_MVCPORTLET, createModuleProjectWizard.getProjectTemplateNameComboBox().getText() );

        createModuleProjectWizard.next();

        ModuleProjectWizardSecondPagePO createModuleProjectSecondPageWizard =
            new ModuleProjectWizardSecondPagePO( bot, INDEX_CONFIGURE_COMPONENT_CLASS_VALIDATION_MESSAGE );

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        String javaFileName = "TestmvcportletinlsPortlet.java";

        projectTree.expandNode(
            liferayWorkspaceName, "modules", projectName, "src/main/java", "testmvcportletinls.portlet" ).doubleClick(
                javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "extends MVCPortlet", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String buildGradleFileName = "build.gradle";

        projectTree.expandNode( liferayWorkspaceName, "modules", projectName ).doubleClick( buildGradleFileName );

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        assertContains( "dependencies", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createServiceModuleProject()
    {
        String projectName = "testServiceProject";

        if( !finishButtonState )
        {
            newLiferayWorkspace.cancel();
            sleep();

            projectTree.getTreeItem( liferayWorkspaceName ).doAction( BUTTON_DELETE );
            sleep( 2000 );

            deleteResources.confirmDeleteFromDisk();
            deleteResources.confirm();
            sleep();

            try
            {
                continueDeleteResources.clickContinueButton();
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }

            sleep( 5000 );
        }
        else
        {
            newLiferayWorkspace.cancel();
        }

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_SERVICE );
        createModuleProjectWizard.next();

        ModuleProjectWizardSecondPagePO createModuleProjectSecondPageWizard =
            new ModuleProjectWizardSecondPagePO( bot, INDEX_MUST_SPECIFY_SERVICE_NAME_VALIDATIOIN_MESSAGE );

        assertEquals( TEXT_SERVICE_NAME_MUST_BE_SPECIFIED, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getServiceName().getText() );
        assertFalse( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.getBrowseButton().click();
        sleep( 5000 );

        SelectModuleServiceNamePO selectOneServiceName = new SelectModuleServiceNamePO( bot );

        selectOneServiceName.selectServiceName( "gg" );
        sleep( 2000 );
        assertFalse( selectOneServiceName.confirmButton().isEnabled() );
        selectOneServiceName.selectServiceName( "*lifecycleAction" );
        sleep();
        assertTrue( selectOneServiceName.confirmButton().isEnabled() );
        selectOneServiceName.confirm();

        assertEquals(
            "com.liferay.portal.kernel.events.LifecycleAction",
            createModuleProjectSecondPageWizard.getServiceName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.getAddPropertyKeyButton().click();
        sleep();
        createModuleProjectSecondPageWizard.setPropertiesText( 3, "key" );
        sleep();
        createModuleProjectSecondPageWizard.getProperties().doubleClick( 0, 1 );
        sleep();
        createModuleProjectSecondPageWizard.setPropertiesText( 3, "login.events.pre" );
        sleep();

        keyPress.pressShortcut( enter );
        sleep();

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        String javaFileName = "Testserviceproject.java";

        projectTree.expandNode( projectName, "src/main/java", "testserviceproject" ).doubleClick( javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "implements LifecycleAction", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String buildGradleFileName = "build.gradle";

        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createServiceModuleProjectInLiferayWorkspace()
    {
        String projectName = "testServiceProjectInLS";

        if( finishButtonState )
        {
            newLiferayWorkspace.finish();
            sleep( 20000 );
        }
        else
        {
            newLiferayWorkspace.cancel();
        }

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_SERVICE );
        createModuleProjectWizard.next();

        ModuleProjectWizardSecondPagePO createModuleProjectSecondPageWizard =
            new ModuleProjectWizardSecondPagePO( bot, INDEX_MUST_SPECIFY_SERVICE_NAME_VALIDATIOIN_MESSAGE );

        assertEquals( TEXT_SERVICE_NAME_MUST_BE_SPECIFIED, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getServiceName().getText() );
        assertFalse( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.getBrowseButton().click();
        sleep( 5000 );

        SelectModuleServiceNamePO selectOneServiceName = new SelectModuleServiceNamePO( bot );

        selectOneServiceName.selectServiceName( "*lifecycleAction" );
        sleep();
        assertTrue( selectOneServiceName.confirmButton().isEnabled() );
        selectOneServiceName.confirm();

        assertEquals(
            "com.liferay.portal.kernel.events.LifecycleAction",
            createModuleProjectSecondPageWizard.getServiceName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.getAddPropertyKeyButton().click();
        sleep();
        createModuleProjectSecondPageWizard.setPropertiesText( 3, "key" );
        sleep();
        createModuleProjectSecondPageWizard.getProperties().doubleClick( 0, 1 );
        sleep();
        createModuleProjectSecondPageWizard.setPropertiesText( 3, "login.events.pre" );
        sleep();

        keyPress.pressShortcut( enter );
        sleep();

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        String javaFileName = "Testserviceprojectinls.java";

        projectTree.expandNode(
            liferayWorkspaceName, "modules", projectName, "src/main/java", "testserviceprojectinls" ).doubleClick(
                javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "implements LifecycleAction", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String buildGradleFileName = "build.gradle";

        projectTree.expandNode( liferayWorkspaceName, "modules", projectName ).doubleClick( buildGradleFileName );

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        assertContains( "dependencies", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createServiceBuilderModuleProject()
    {
        String projectName = "testServiceBuilderProject";

        if( !finishButtonState )
        {
            newLiferayWorkspace.cancel();
            sleep();

            projectTree.getTreeItem( liferayWorkspaceName ).doAction( BUTTON_DELETE );
            sleep( 2000 );

            deleteResources.confirmDeleteFromDisk();
            deleteResources.confirm();

            try
            {
                continueDeleteResources.clickContinueButton();
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }

            sleep( 5000 );
        }
        else
        {
            newLiferayWorkspace.cancel();
        }

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_SERVICEBUILDER );
        createModuleProjectWizard.next();

        ModuleProjectWizardSecondPagePO createModuleProjectSecondPageWizard = new ModuleProjectWizardSecondPagePO(
            bot, INDEX_SERVICEBUILDER_CONFIGURE_COMPONENT_CLASS_VALIDATION_MESSAGE );

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );

        try
        {
            assertTrue( projectTree.getTreeItem( projectName + "-api" ).isVisible() );
        }
        catch( Exception e )
        {
            assertTrue( projectTree.expandNode( projectName, projectName + "-api" ).isVisible() );
        }

        try
        {
            assertTrue( projectTree.getTreeItem( projectName + "-service" ).isVisible() );
        }
        catch( Exception e )
        {
            assertTrue( projectTree.expandNode( projectName, projectName + "-service" ).isVisible() );
        }

        try
        {
            assertTrue( projectTree.expandNode( projectName + "-service", "service.xml" ).isVisible() );
        }
        catch( Exception e )
        {
            assertTrue( projectTree.expandNode( projectName, projectName + "-service", "service.xml" ).isVisible() );
        }

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        try
        {
            projectTree.expandNode( projectName + "-api" ).doubleClick( buildGradleFileName );
        }
        catch( Exception e )
        {
            projectTree.expandNode( projectName, projectName + "-api" ).doubleClick( buildGradleFileName );
        }

        assertContains( "dependencies", buildGradleFile.getText() );
        buildGradleFile.close();

        projectTree.setFocus();

        try
        {
            projectTree.expandNode( projectName + "-service" ).doubleClick( buildGradleFileName );
        }
        catch( Exception e )
        {
            projectTree.expandNode( projectName, projectName + "-service" ).doubleClick( buildGradleFileName );
        }

        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "buildService", buildGradleFile.getText() );
        buildGradleFile.close();

        projectTree.getTreeItem( projectName ).doAction( "Liferay", "build-service" );
        sleep( 10000 );

        try
        {
            projectTree.getTreeItem( projectName ).doAction( "Gradle", "Refresh Gradle Project" );
        }
        catch( Exception e )
        {
            projectTree.getTreeItem( projectName ).doAction( "Gradle", "Refresh Gradle Project" );
        }

        sleep( 10000 );

        try
        {
            assertTrue( projectTree.expandNode(
                projectName + "-api", "src/main/java", "testservicebuilderproject.service" ).isVisible() );
        }
        catch( Exception e )
        {
            assertTrue(
                projectTree.expandNode(
                    projectName, projectName + "-api", "src/main/java",
                    "testservicebuilderproject.service" ).isVisible() );
        }

        try
        {
            assertTrue(
                projectTree.expandNode(
                    projectName + "-service", "src/main/java", "testservicebuilderproject.model.impl" ).isVisible() );
        }
        catch( Exception e )
        {
            assertTrue(
                projectTree.expandNode(
                    projectName, projectName + "-service", "src/main/java",
                    "testservicebuilderproject.model.impl" ).isVisible() );
        }

        eclipse.getPackageExporerView().deleteResouceByName( projectName, true );
    }

    @Test
    public void createServiceBuilderModuleProjectInLiferayWorkspace()
    {
        String projectName = "testServiceBuilderProjectInLS";

        if( finishButtonState )
        {
            newLiferayWorkspace.finish();
            sleep( 20000 );
        }
        else
        {
            newLiferayWorkspace.cancel();
        }

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_SERVICEBUILDER );
        createModuleProjectWizard.next();

        ModuleProjectWizardSecondPagePO createModuleProjectSecondPageWizard = new ModuleProjectWizardSecondPagePO(
            bot, INDEX_SERVICEBUILDER_CONFIGURE_COMPONENT_CLASS_VALIDATION_MESSAGE );

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        assertTrue( projectTree.expandNode( liferayWorkspaceName, "modules", projectName ).isVisible() );
        assertTrue(
            projectTree.expandNode( liferayWorkspaceName, "modules", projectName, projectName + "-api" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                liferayWorkspaceName, "modules", projectName, projectName + "-service" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                liferayWorkspaceName, "modules", projectName, projectName + "-service", "service.xml" ).isVisible() );

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( liferayWorkspaceName, "modules", projectName, projectName + "-api" ).doubleClick(
            buildGradleFileName );

        assertContains( "dependencies", buildGradleFile.getText() );
        buildGradleFile.close();

        projectTree.setFocus();

        projectTree.expandNode( liferayWorkspaceName, "modules", projectName, projectName + "-service" ).doubleClick(
            buildGradleFileName );

        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "buildService", buildGradleFile.getText() );
        buildGradleFile.close();

        projectTree.expandNode( liferayWorkspaceName, "modules", projectName, projectName + "-service" ).doAction(
            "Liferay", "build-service" );
        sleep( 10000 );

        try
        {
            projectTree.expandNode( liferayWorkspaceName, "modules", projectName ).doAction( "Gradle",
                "Refresh Gradle Project" );
        }
        catch( Exception e )
        {
            projectTree.expandNode( liferayWorkspaceName, "modules", projectName ).doAction(
                "Gradle", "Refresh Gradle Project" );
        }

        sleep( 10000 );

        assertTrue( projectTree.expandNode(
            liferayWorkspaceName, "modules", projectName, projectName + "-api", "src/main/java",
            "testservicebuilderprojectinls.service" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                liferayWorkspaceName, "modules", projectName, projectName + "-service", "src/main/java",
                "testservicebuilderprojectinls.model.impl" ).isVisible() );
    }

    @Test
    public void validationProjectName()
    {
        newLiferayWorkspace.cancel();

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();

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

    @Before
    public void openLiferayWorkspaceWizard()
    {
        Assume.assumeTrue( runTest() || runAllTests() );

        eclipse.getLiferayWorkspacePerspective().activate();

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayWorkspaceProject().click();
        sleep( 2000 );

        finishButtonState = ifAddedLiferayWorksapce();
    }

    @After
    public void waitForCreate()
    {
        sleep();
    }
}

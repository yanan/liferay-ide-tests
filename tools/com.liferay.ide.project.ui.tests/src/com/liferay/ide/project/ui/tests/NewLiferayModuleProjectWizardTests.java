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

import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.liferay.ide.project.ui.tests.page.NewLiferayModuleProjectWizardPO;
import com.liferay.ide.project.ui.tests.page.NewLiferayModuleProjectWizardSecondPagePO;
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
public class NewLiferayModuleProjectWizardTests extends SWTBotBase implements NewLiferayModuleProjectWizard
{

    TreePO projectTree = eclipse.getPackageExporerView().getProjectTree();

    NewLiferayModuleProjectWizardPO createModuleProjectWizard = new NewLiferayModuleProjectWizardPO( bot );

    @AfterClass
    public static void cleanAll()
    {
        eclipse.closeShell( LABEL_NEW_LIFERAY_MODULE_PROJECT );
        eclipse.getPackageExporerView().deleteProjectExcludeNames( new String[] { getLiferayPluginsSdkName() }, true );
    }

    @BeforeClass
    public static void switchToLiferayWorkspacePerspective()
    {
        eclipse.getLiferayWorkspacePerspective().activate();
    }

    @Test
    public void createMvcportletModuleProject()
    {
        NewLiferayModuleProjectWizardPO createModuleProjectWizard =
            new NewLiferayModuleProjectWizardPO( bot, INDEX_NEW_LIFERAY_MODULE_PROJECT_VALIDATION_MESSAGE );

        sleep( 3000 );
        assertEquals( TEXT_ENTER_MODULE_PROJECT_NAME_MESSAGE, createModuleProjectWizard.getValidationMessage() );

        String projectName = "testMvcportletProject";

        createModuleProjectWizard.createModuleProject( projectName );

        assertEquals( MENU_MODULE_MVC_PORTLET, createModuleProjectWizard.getProjectTemplateNameComboBox().getText() );

        String[] expectedModuleProjectTemplateItems =
            { MENU_MODULE_ACTIVATOR, MENU_MODULE_API, MENU_MODULE_CONTENT_TARGETING_REPORT,
                MENU_MODULE_CONTENT_TARGETING_RULE, MENU_MODULE_CONTENT_TARGETING_TRACKING_ACTION,
                MENU_MODULE_CONTROL_MENU_ENTRY, MENU_MODULE_FORM_FIELD, MENU_MODULE_MVC_PORTLET, MENU_MODULE_PANEL_APP,
                MENU_MODULE_PORTLET, MENU_MODULE_PORTLET_CONFIGURATION_ICON, MENU_MODULE_PORTLET_PROVIDER,
                MENU_MODULE_PORTLET_TOOLBAR_CONTRIBUTOR, MENU_MODULE_REST, MENU_MODULE_SERVICE,
                MENU_MODULE_SERVICE_BUILDER, MENU_MODULE_SERVICE_WRAPPER, MENU_MODULE_SIMULATION_PANEL_ENTRY,
                MENU_MODULE_TEMPLATE_CONTEXT_CONTRIBUTOR, MENU_MODULE_THEME, MENU_MODULE_THEME_CONTRIBUTOR };

        String[] moduleProjectTemplateItems =
            createModuleProjectWizard.getProjectTemplateNameComboBox().getAvailableComboValues();

        assertTrue( moduleProjectTemplateItems.length >= 1 );

        assertEquals( expectedModuleProjectTemplateItems.length, moduleProjectTemplateItems.length );

        for( int i = 0; i < moduleProjectTemplateItems.length; i++ )
        {
            assertTrue( moduleProjectTemplateItems[i].equals( expectedModuleProjectTemplateItems[i] ) );
        }

        createModuleProjectWizard.next();

        NewLiferayModuleProjectWizardSecondPagePO createModuleProjectSecondPageWizard =
            new NewLiferayModuleProjectWizardSecondPagePO( bot, INDEX_CONFIGURE_COMPONENT_CLASS_VALIDATION_MESSAGE );

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
        sleep( 3000 );
        createModuleProjectSecondPageWizard.getProperties().doubleClick( 0, 1 );
        sleep();
        createModuleProjectSecondPageWizard.setPropertiesText( 2, "b" );
        sleep( 3000 );

        createModuleProjectSecondPageWizard.getProperties().setFocus();
        sleep();

        assertTrue( createModuleProjectSecondPageWizard.getDeleteButton().isEnabled() );
        assertFalse( createModuleProjectSecondPageWizard.getMoveUpButton().isEnabled() );
        assertFalse( createModuleProjectSecondPageWizard.getMoveDownButton().isEnabled() );

        createModuleProjectSecondPageWizard.getAddPropertyKeyButton().click();
        sleep();
        createModuleProjectSecondPageWizard.setPropertiesText( 2, "c" );
        sleep( 3000 );
        createModuleProjectSecondPageWizard.getProperties().doubleClick( 1, 1 );
        sleep();
        createModuleProjectSecondPageWizard.setPropertiesText( 2, "d" );
        sleep( 3000 );

        createModuleProjectSecondPageWizard.getProperties().setFocus();
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

        String javaFileName = "TestMvcportletProjectPortlet.java";

        projectTree.expandNode( projectName, "src/main/java", "testMvcportletProject.portlet" ).doubleClick(
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
        createModuleProjectWizard.cancel();

        String liferayWorkspaceName = "liferayWorkspace";

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayWorkspaceProject().click();
        sleep( 2000 );

        NewLiferayWorkspaceProjectWizardPO newLiferayWorkspace = new NewLiferayWorkspaceProjectWizardPO( bot );

        newLiferayWorkspace.setWorkspaceNameText( liferayWorkspaceName );
        newLiferayWorkspace.finish();
        sleep( 20000 );

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();
        sleep( 3000 );

        String projectName = "testMvcportletInLS";

        createModuleProjectWizard.createModuleProject( projectName );

        assertEquals( MENU_MODULE_MVC_PORTLET, createModuleProjectWizard.getProjectTemplateNameComboBox().getText() );

        createModuleProjectWizard.next();

        NewLiferayModuleProjectWizardSecondPagePO createModuleProjectSecondPageWizard =
            new NewLiferayModuleProjectWizardSecondPagePO( bot, INDEX_CONFIGURE_COMPONENT_CLASS_VALIDATION_MESSAGE );

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        String javaFileName = "TestMvcportletInLSPortlet.java";

        projectTree.expandNode(
            liferayWorkspaceName, "modules", projectName, "src/main/java", "testMvcportletInLS.portlet" ).doubleClick(
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

        DeleteResourcesDialogPO deleteResources = new DeleteResourcesDialogPO( bot );

        DeleteResourcesContinueDialogPO continueDeleteResources =
            new DeleteResourcesContinueDialogPO( bot, "Delete Resources" );

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
    }

    @Test
    public void createServiceModuleProject()
    {
        String projectName = "testServiceProject";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_SERVICE );
        createModuleProjectWizard.next();

        NewLiferayModuleProjectWizardSecondPagePO createModuleProjectSecondPageWizard =
            new NewLiferayModuleProjectWizardSecondPagePO( bot, INDEX_MUST_SPECIFY_SERVICE_NAME_VALIDATIOIN_MESSAGE );

        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getServiceName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

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
        sleep( 3000 );
        createModuleProjectSecondPageWizard.getProperties().doubleClick( 0, 1 );
        sleep();
        createModuleProjectSecondPageWizard.setPropertiesText( 3, "login.events.pre" );
        sleep( 3000 );

        createModuleProjectSecondPageWizard.getProperties().setFocus();
        sleep();

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        String javaFileName = "TestServiceProject.java";

        projectTree.expandNode( projectName, "src/main/java", "testServiceProject" ).doubleClick( javaFileName );

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
    public void createServiceBuilderModuleProject()
    {
        String projectName = "testServiceBuilderProject";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_SERVICE_BUILDER );
        createModuleProjectWizard.next();

        NewLiferayModuleProjectWizardSecondPagePO createModuleProjectSecondPageWizard =
            new NewLiferayModuleProjectWizardSecondPagePO(
                bot, INDEX_SERVICEBUILDER_CONFIGURE_COMPONENT_CLASS_VALIDATION_MESSAGE );

        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue( projectTree.expandNode( projectName, projectName + "-api" ).isVisible() );
        assertTrue( projectTree.expandNode( projectName, projectName + "-service" ).isVisible() );
        assertTrue( projectTree.expandNode( projectName, projectName + "-service", "service.xml" ).isVisible() );

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( projectName, projectName + "-api" ).doubleClick( buildGradleFileName );

        assertContains( "dependencies", buildGradleFile.getText() );
        buildGradleFile.close();

        projectTree.setFocus();

        projectTree.expandNode( projectName, projectName + "-service" ).doubleClick( buildGradleFileName );

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

        assertTrue(
            projectTree.expandNode(
                projectName, projectName + "-api", "src/main/java", "testServiceBuilderProject.service" ).isVisible() );

        assertTrue(
            projectTree.expandNode(
                projectName, projectName + "-service", "src/main/java",
                "testServiceBuilderProject.model.impl" ).isVisible() );

        eclipse.getPackageExporerView().deleteResouceByName( projectName, true );
    }

    @Test
    public void validationProjectName()
    {
        NewLiferayModuleProjectWizardPO createModuleProjectWizard =
            new NewLiferayModuleProjectWizardPO( bot, INDEX_NEW_LIFERAY_MODULE_PROJECT_VALIDATION_MESSAGE );

        assertEquals( TEXT_ENTER_MODULE_PROJECT_NAME_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertFalse( createModuleProjectWizard.finishButton().isEnabled() );

        createModuleProjectWizard.createModuleProject( "." );
        sleep( 1000 );
        assertEquals( " '.'" + TEXT_INVALID_NAME_ON_PLATFORM, createModuleProjectWizard.getValidationMessage() );
        assertFalse( createModuleProjectWizard.finishButton().isEnabled() );
        createModuleProjectWizard.createModuleProject( "/" );
        sleep( 1000 );
        assertEquals(
            " /" + TEXT_INVALID_CHARACTER_IN_RESOURCE_NAME + "'/'.", createModuleProjectWizard.getValidationMessage() );
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
    public void openWizard()
    {
        Assume.assumeTrue( runTest() || runAllTests() );

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();
        sleep( 2000 );
    }

}

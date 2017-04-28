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

import java.io.IOException;

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

    NewLiferayModuleProjectWizardPO createModuleProjectWizard =
        new NewLiferayModuleProjectWizardPO( bot, INDEX_NEW_LIFERAY_MODULE_PROJECT_VALIDATION_MESSAGE );

    NewLiferayModuleProjectWizardSecondPagePO createModuleProjectSecondPageWizard =
        new NewLiferayModuleProjectWizardSecondPagePO( bot, INDEX_CONFIGURE_COMPONENT_CLASS_VALIDATION_MESSAGE );

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
        assertEquals( TEXT_PLEASE_ENTER_A_PROJECT_NAME, createModuleProjectWizard.getValidationMessage() );

        String projectName = "testMvcportletProject";

        createModuleProjectWizard.createModuleProject( projectName );
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );
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

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
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
    public void createMvcportletModuleProjectInLiferayWorkspace() throws IOException
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

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        String javaFileName = "TestMvcportletInLSPortlet.java";

        assertTrue( projectTree.expandNode( liferayWorkspaceName, "modules", projectName ).isVisible() );
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
        sleep( 2000 );

        killGradleProcess();

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
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );

        createModuleProjectWizard.next();

        NewLiferayModuleProjectWizardSecondPagePO createModuleProjectSecondPageWizard =
            new NewLiferayModuleProjectWizardSecondPagePO(
                bot, INDEX_SERVICE_CONFIGURE_COMPONENT_CLASS_VALIDATION_MESSAGE );

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
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

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
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
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );

        createModuleProjectWizard.next();

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
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
    public void createActivatorModuleProject()
    {
        String projectName = "testActivatorProject";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_ACTIVATOR );
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );

        createModuleProjectWizard.next();

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", projectName, "TestActivatorProjectActivator.java" ).isVisible() );

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createApiModuleProject()
    {
        String projectName = "testApiProject";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_API );
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );

        createModuleProjectWizard.next();

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testApiProject.api", "TestApiProject.java" ).isVisible() );

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createContentTargetingReportModuleProject()
    {
        String projectName = "testContentTargetingReportProject";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_CONTENT_TARGETING_REPORT );
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );

        createModuleProjectWizard.next();

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testContentTargetingReportProject.content.targeting.report",
                "TestContentTargetingReportProjectReport.java" ).isVisible() );

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createContentTargetingRuleModuleProject()
    {
        String projectName = "testContentTargetingRuleProject";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_CONTENT_TARGETING_RULE );
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );

        createModuleProjectWizard.next();

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testContentTargetingRuleProject.content.targeting.rule",
                "TestContentTargetingRuleProjectRule.java" ).isVisible() );

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createContentTargetingTrackingActionModuleProject()
    {
        String projectName = "testContentTargetingTrackingActionProject";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_CONTENT_TARGETING_TRACKING_ACTION );
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );

        createModuleProjectWizard.next();

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java",
                "testContentTargetingTrackingActionProject.content.targeting.tracking.action",
                "TestContentTargetingTrackingActionProjectTrackingAction.java" ).isVisible() );

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createControlMenuEntryModuleProject()
    {
        String projectName = "testControlMenuEntryProject";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_CONTROL_MENU_ENTRY );
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );

        createModuleProjectWizard.next();

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testControlMenuEntryProject.control.menu",
                "TestControlMenuEntryProjectProductNavigationControlMenuEntry.java" ).isVisible() );

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createFormFieldModuleProject()
    {
        String projectName = "testFormFieldProject";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_FORM_FIELD );
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );

        createModuleProjectWizard.next();

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testFormFieldProject.form.field",
                "TestFormFieldProjectDDMFormFieldRenderer.java" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testFormFieldProject.form.field",
                "TestFormFieldProjectDDMFormFieldType.java" ).isVisible() );

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );
        assertContains( "task wrapSoyTemplates", buildGradleFile.getText() );
        assertContains( "classes", buildGradleFile.getText() );
        assertContains( "transpileJS", buildGradleFile.getText() );
        assertContains( "wrapSoyTemplates", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createPanelAppModuleProject()
    {
        String projectName = "testPanelAppProject";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_PANEL_APP );
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );

        createModuleProjectWizard.next();

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testPanelAppProject.application.list" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testPanelAppProject.application.list",
                "TestPanelAppProjectPanelApp.java" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testPanelAppProject.application.list",
                "TestPanelAppProjectPanelCategory.java" ).isVisible() );

        assertTrue(
            projectTree.expandNode( projectName, "src/main/java", "testPanelAppProject.constants" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testPanelAppProject.constants",
                "TestPanelAppProjectPanelCategoryKeys.java" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testPanelAppProject.constants",
                "TestPanelAppProjectPortletKeys.java" ).isVisible() );

        assertTrue( projectTree.expandNode( projectName, "src/main/java", "testPanelAppProject.portlet" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testPanelAppProject.portlet",
                "TestPanelAppProjectPortlet.java" ).isVisible() );

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createPortletModuleProject()
    {
        String projectName = "testPortletProject";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_PORTLET );
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );

        createModuleProjectWizard.next();

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.getComponentClassName().setText( "testComponentClass" );
        createModuleProjectSecondPageWizard.getPackageName().setText( "testPackage" );

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue( projectTree.expandNode( projectName, "src/main/java", "testPackage.portlet" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testPackage.portlet", "testComponentClassPortlet.java" ).isVisible() );

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createPortletConfigurationIconModuleProject()
    {
        String projectName = "testPortletConfigurationIconProject";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_PORTLET_CONFIGURATION_ICON );
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );

        createModuleProjectWizard.next();

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testPortletConfigurationIconProject.portlet.configuration.icon",
                "TestPortletConfigurationIconProjectPortletConfigurationIcon.java" ).isVisible() );

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createPortletProviderModuleProject()
    {
        String projectName = "testPortletProviderProject";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_PORTLET_PROVIDER );
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );

        createModuleProjectWizard.next();

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testPortletProviderProject.constants" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testPortletProviderProject.constants",
                "TestPortletProviderProjectPortletKeys.java" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testPortletProviderProject.constants",
                "TestPortletProviderProjectWebKeys.java" ).isVisible() );

        assertTrue(
            projectTree.expandNode( projectName, "src/main/java", "testPortletProviderProject.portlet" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testPortletProviderProject.portlet",
                "TestPortletProviderProjectAddPortletProvider.java" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testPortletProviderProject.portlet",
                "TestPortletProviderProjectPortlet.java" ).isVisible() );

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createPortletToolbarContributorModuleProject()
    {
        String projectName = "testPortletToolbarContributorProject";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_PORTLET_TOOLBAR_CONTRIBUTOR );
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );

        createModuleProjectWizard.next();

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testPortletToolbarContributorProject.portlet.toolbar.contributor",
                "TestPortletToolbarContributorProjectPortletToolbarContributor.java" ).isVisible() );

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createRestModuleProject()
    {
        String projectName = "testRestProject";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_REST );
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );

        createModuleProjectWizard.next();

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testRestProject.application",
                "TestRestProjectApplication.java" ).isVisible() );

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createServiceWrapperModuleProject()
    {
        String projectName = "testServiceWrapperProject";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_SERVICE_WRAPPER );
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );

        createModuleProjectWizard.next();

        NewLiferayModuleProjectWizardSecondPagePO createModuleProjectSecondPageWizard =
            new NewLiferayModuleProjectWizardSecondPagePO(
                bot, INDEX_SERVICE_WRAPPER_CONFIGURE_COMPONENT_CLASS_VALIDATION_MESSAGE );

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.getBrowseButton().click();
        sleep( 5000 );

        SelectModuleServiceNamePO selectOneServiceName = new SelectModuleServiceNamePO( bot );

        selectOneServiceName.selectServiceName( "gg" );
        sleep( 2000 );
        assertFalse( selectOneServiceName.confirmButton().isEnabled() );
        selectOneServiceName.selectServiceName( "*BookmarksEntryLocalServiceWrapper" );
        sleep();
        assertTrue( selectOneServiceName.confirmButton().isEnabled() );
        selectOneServiceName.confirm();

        assertEquals(
            "com.liferay.bookmarks.service.BookmarksEntryLocalServiceWrapper",
            createModuleProjectSecondPageWizard.getServiceName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        String javaFileName = "TestServiceWrapperProject.java";

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", projectName, "TestServiceWrapperProject.java" ).isVisible() );

        projectTree.expandNode( projectName, "src/main/java", projectName ).doubleClick( javaFileName );
        sleep( 2000 );

        assertContains( "extends BookmarksEntryLocalServiceWrapper", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createSimulationPanelEntryModuleProject()
    {
        String projectName = "testSimulationPanelEntryProject";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_SIMULATION_PANEL_ENTRY );
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );

        createModuleProjectWizard.next();

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testSimulationPanelEntryProject.application.list",
                "TestSimulationPanelEntryProjectSimulationPanelApp.java" ).isVisible() );

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createTemplateContextContributorModuleProject()
    {
        String projectName = "testTemplateContextContributorProject";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_TEMPLATE_CONTEXT_CONTRIBUTOR );
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );

        createModuleProjectWizard.next();

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/java", "testTemplateContextContributorProject.context.contributor",
                "TestTemplateContextContributorProjectTemplateContextContributor.java" ).isVisible() );

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createThemeModuleProject()
    {
        String projectName = "testThemeProject";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_THEME );
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );
        assertFalse( createModuleProjectWizard.nextButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue( projectTree.expandNode( projectName, "src", "main", "webapp", "css", "_custom.scss" ).isVisible() );

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void createThemeContributorModuleProject()
    {
        String projectName = "testThemeContributorProject";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_THEME_CONTRIBUTOR );
        sleep();

        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );
        assertEquals( TEXT_BUILD_TYPE, createModuleProjectWizard.getBuildType().getText() );

        createModuleProjectWizard.next();

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/resources", "META-INF", "resources", "css", projectName,
                "_body.scss" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/resources", "META-INF", "resources", "css", projectName,
                "_control_menu.scss" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/resources", "META-INF", "resources", "css", projectName,
                "_product_menu.scss" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/resources", "META-INF", "resources", "css", projectName,
                "_simulation_panel.scss" ).isVisible() );

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );

        buildGradleFile.close();
    }

    @Test
    public void validationProjectName()
    {
        NewLiferayModuleProjectWizardPO createModuleProjectWizard =
            new NewLiferayModuleProjectWizardPO( bot, INDEX_NEW_LIFERAY_MODULE_PROJECT_VALIDATION_MESSAGE );

        assertEquals( TEXT_PLEASE_ENTER_A_PROJECT_NAME, createModuleProjectWizard.getValidationMessage() );
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
        assertEquals( TEXT_THE_PROJECT_NAME_INVALID, createModuleProjectWizard.getValidationMessage() );
        assertFalse( createModuleProjectWizard.finishButton().isEnabled() );
        createModuleProjectWizard.createModuleProject( "" );
        sleep( 1000 );
        assertEquals( TEXT_PROJECT_NAME_MUST_BE_SPECIFIED, createModuleProjectWizard.getValidationMessage() );
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
        createModuleProjectWizard.setBuildType( TEXT_BUILD_TYPE );
    }

}

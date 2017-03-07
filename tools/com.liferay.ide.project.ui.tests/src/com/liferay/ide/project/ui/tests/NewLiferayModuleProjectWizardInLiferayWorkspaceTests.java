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
public class NewLiferayModuleProjectWizardInLiferayWorkspaceTests extends SWTBotBase
    implements NewLiferayModuleProjectWizard
{

    static String liferayWorkspaceName = "liferayWorkspace";

    static NewLiferayWorkspaceProjectWizardPO newLiferayWorkspace = new NewLiferayWorkspaceProjectWizardPO( bot );

    static TreePO projectTree = eclipse.getPackageExporerView().getProjectTree();

    NewLiferayModuleProjectWizardPO createModuleProjectWizard = new NewLiferayModuleProjectWizardPO( bot );

    @AfterClass
    public static void cleanAll()
    {
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

        eclipse.closeShell( LABEL_NEW_LIFERAY_MODULE_PROJECT );
        eclipse.getPackageExporerView().deleteProjectExcludeNames( new String[] { getLiferayPluginsSdkName() }, true );
    }

    @BeforeClass
    public static void createLiferayWorkspace()
    {
        eclipse.getLiferayWorkspacePerspective().activate();

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayWorkspaceProject().click();
        sleep( 2000 );

        newLiferayWorkspace.setWorkspaceNameText( liferayWorkspaceName );
        newLiferayWorkspace.finish();
        sleep( 20000 );
    }

    @Test
    public void createMvcportletModuleProject()
    {
        String projectName = "testMvcportletProject";

        createModuleProjectWizard.createModuleProject( projectName );

        assertEquals( MENU_MODULE_MVC_PORTLET, createModuleProjectWizard.getProjectTemplateNameComboBox().getText() );

        createModuleProjectWizard.deSelectDefaultLocation();
        createModuleProjectWizard.setLocation( getLiferayBundlesPath().toString() );

        createModuleProjectWizard.next();

        NewLiferayModuleProjectWizardSecondPagePO createModuleProjectSecondPageWizard =
            new NewLiferayModuleProjectWizardSecondPagePO( bot, INDEX_CONFIGURE_COMPONENT_CLASS_VALIDATION_MESSAGE );

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );

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
        String projectName = "testMvcportletInLS";

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
    }

    @Test
    public void createServiceModuleProjectInLiferayWorkspace()
    {
        String projectName = "testServiceProjectInLS";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_SERVICE );
        createModuleProjectWizard.next();

        NewLiferayModuleProjectWizardSecondPagePO createModuleProjectSecondPageWizard =
            new NewLiferayModuleProjectWizardSecondPagePO(
                bot, INDEX_SERVICE_CONFIGURE_COMPONENT_CLASS_VALIDATION_MESSAGE );

        assertEquals( "", createModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertEquals( "", createModuleProjectSecondPageWizard.getServiceName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

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
        sleep( 3000 );
        createModuleProjectSecondPageWizard.getProperties().doubleClick( 0, 1 );
        sleep();
        createModuleProjectSecondPageWizard.setPropertiesText( 3, "login.events.pre" );
        sleep( 3000 );

        createModuleProjectSecondPageWizard.getProperties().setFocus();
        sleep();

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        String javaFileName = "TestServiceProjectInLS.java";

        projectTree.expandNode(
            liferayWorkspaceName, "modules", projectName, "src/main/java", "testServiceProjectInLS" ).doubleClick(
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
    public void createServiceBuilderModuleProjectInLiferayWorkspace()
    {
        String projectName = "testServiceBuilderProjectInLS";

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_SERVICE_BUILDER );
        createModuleProjectWizard.next();

        NewLiferayModuleProjectWizardSecondPagePO createModuleProjectSecondPageWizard =
            new NewLiferayModuleProjectWizardSecondPagePO( bot, INDEX_CONFIGURE_COMPONENT_CLASS_VALIDATION_MESSAGE );

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
            projectTree.expandNode( liferayWorkspaceName, "modules", projectName ).doAction(
                "Gradle", "Refresh Gradle Project" );
        }
        catch( Exception e )
        {
            projectTree.expandNode( liferayWorkspaceName, "modules", projectName ).doAction(
                "Gradle", "Refresh Gradle Project" );
        }

        sleep( 10000 );

        assertTrue(
            projectTree.expandNode(
                liferayWorkspaceName, "modules", projectName, projectName + "-api", "src/main/java",
                "testServiceBuilderProjectInLS.service" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                liferayWorkspaceName, "modules", projectName, projectName + "-service", "src/main/java",
                "testServiceBuilderProjectInLS.model.impl" ).isVisible() );
    }

    @Before
    public void openWizard()
    {
        Assume.assumeTrue( runTest() || runAllTests() );

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();
    }
}

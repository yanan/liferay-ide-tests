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
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.liferay.ide.project.ui.tests.page.NewLiferayModuleProjectWizardPO;
import com.liferay.ide.project.ui.tests.page.NewLiferayModuleProjectWizardSecondPagePO;

import com.liferay.ide.project.ui.tests.page.SelectModuleServiceNamePO;
import com.liferay.ide.ui.tests.SWTBotBase;

import com.liferay.ide.ui.tests.swtbot.page.CTabItemPO;
import com.liferay.ide.ui.tests.swtbot.page.TextEditorPO;
import com.liferay.ide.ui.tests.swtbot.page.TreePO;

/**
 * @author Sunny Shi
 */
public class NewLiferayMavenModuleProjectWizardTests extends SWTBotBase implements NewLiferayModuleProjectWizard
{

    TreePO projectTree = eclipse.getPackageExporerView().getProjectTree();

    NewLiferayModuleProjectWizardPO createMavenModuleProjectWizard =
        new NewLiferayModuleProjectWizardPO( bot, INDEX_NEW_LIFERAY_MODULE_PROJECT_VALIDATION_MESSAGE );

    NewLiferayModuleProjectWizardSecondPagePO createMavenModuleProjectSecondPageWizard =
        new NewLiferayModuleProjectWizardSecondPagePO( bot );

    @After
    public void cleanAll()
    {
        eclipse.closeShell( LABEL_NEW_LIFERAY_MODULE_PROJECT );

        if( addedProjects() )
        {
            eclipse.getPackageExporerView().deleteProjectExcludeNames(
                new String[] { getLiferayPluginsSdkName() }, true );
        }
    }

    @BeforeClass
    public static void switchToLiferayWorkspacePerspective()
    {
        eclipse.getLiferayWorkspacePerspective().activate();
    }

    @Before
    public void openWizard()
    {
        Assume.assumeTrue( runTest() || runAllTests() );

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();
        sleep( 3000 );
        assertEquals( TEXT_PLEASE_ENTER_A_PROJECT_NAME, createMavenModuleProjectWizard.getValidationMessage() );
    }

    @Test
    public void validationProjectName()
    {
        assertEquals( TEXT_PLEASE_ENTER_A_PROJECT_NAME, createMavenModuleProjectWizard.getValidationMessage() );
        assertFalse( createMavenModuleProjectWizard.finishButton().isEnabled() );

        createMavenModuleProjectWizard.createModuleProject( "." );
        sleep( 1000 );
        assertEquals( " '.'" + TEXT_INVALID_NAME_ON_PLATFORM, createMavenModuleProjectWizard.getValidationMessage() );
        assertFalse( createMavenModuleProjectWizard.finishButton().isEnabled() );

        createMavenModuleProjectWizard.createModuleProject( "/" );
        sleep( 1000 );
        assertEquals(
            " /" + TEXT_INVALID_CHARACTER_IN_RESOURCE_NAME + "'/'.",
            createMavenModuleProjectWizard.getValidationMessage() );
        assertFalse( createMavenModuleProjectWizard.finishButton().isEnabled() );

        createMavenModuleProjectWizard.createModuleProject( "$" );
        sleep( 1000 );
        assertEquals( TEXT_THE_PROJECT_NAME_INVALID, createMavenModuleProjectWizard.getValidationMessage() );
        assertFalse( createMavenModuleProjectWizard.finishButton().isEnabled() );

        createMavenModuleProjectWizard.createModuleProject( "" );
        sleep( 1000 );
        assertEquals( TEXT_PROJECT_NAME_MUST_BE_SPECIFIED, createMavenModuleProjectWizard.getValidationMessage() );
        assertFalse( createMavenModuleProjectWizard.finishButton().isEnabled() );

        createMavenModuleProjectWizard.createModuleProject( "a" );
        sleep( 1000 );
        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createMavenModuleProjectWizard.getValidationMessage() );
        assertTrue( createMavenModuleProjectWizard.finishButton().isEnabled() );

        createMavenModuleProjectWizard.cancel();
    }

    @Test
    public void createMvcportletModuleProject()
    {
        String projectName = "testMvcportletProject";

        assertEquals( TEXT_BUILD_TYPE_MAVEN, createMavenModuleProjectWizard.getBuildType().getText() );

        String[] expectedModuleBuildTypeItems = { TEXT_BUILD_TYPE, TEXT_BUILD_TYPE_MAVEN };
        String[] moduleBuildTypeItems = createMavenModuleProjectWizard.getBuildType().getAvailableComboValues();
        assertTrue( moduleBuildTypeItems.length >= 1 );
        assertEquals( expectedModuleBuildTypeItems.length, moduleBuildTypeItems.length );

        for( int j = 0; j < moduleBuildTypeItems.length; j++ )
        {
            assertTrue( moduleBuildTypeItems[j].equals( expectedModuleBuildTypeItems[j] ) );
        }

        assertEquals(
            MENU_MODULE_MVC_PORTLET, createMavenModuleProjectWizard.getProjectTemplateNameComboBox().getText() );

        String[] expectedModuleProjectTemplateItems =
            { MENU_MODULE_ACTIVATOR, MENU_MODULE_API, MENU_MODULE_CONTENT_TARGETING_REPORT,
                MENU_MODULE_CONTENT_TARGETING_RULE, MENU_MODULE_CONTENT_TARGETING_TRACKING_ACTION,
                MENU_MODULE_CONTROL_MENU_ENTRY, MENU_MODULE_FORM_FIELD, MENU_MODULE_MVC_PORTLET, MENU_MODULE_PANEL_APP,
                MENU_MODULE_PORTLET, MENU_MODULE_PORTLET_CONFIGURATION_ICON, MENU_MODULE_PORTLET_PROVIDER,
                MENU_MODULE_PORTLET_TOOLBAR_CONTRIBUTOR, MENU_MODULE_REST, MENU_MODULE_SERVICE,
                MENU_MODULE_SERVICE_BUILDER, MENU_MODULE_SERVICE_WRAPPER, MENU_MODULE_SIMULATION_PANEL_ENTRY,
                MENU_MODULE_TEMPLATE_CONTEXT_CONTRIBUTOR, MENU_MODULE_THEME, MENU_MODULE_THEME_CONTRIBUTOR };

        String[] moduleProjectTemplateItems =
            createMavenModuleProjectWizard.getProjectTemplateNameComboBox().getAvailableComboValues();
        assertTrue( moduleProjectTemplateItems.length >= 1 );
        assertEquals( expectedModuleProjectTemplateItems.length, moduleProjectTemplateItems.length );

        for( int i = 0; i < moduleProjectTemplateItems.length; i++ )
        {
            assertTrue( moduleProjectTemplateItems[i].equals( expectedModuleProjectTemplateItems[i] ) );
        }

        createMavenModuleProjectWizard.createModuleProject(
            projectName, MENU_MODULE_MVC_PORTLET, TEXT_BUILD_TYPE_MAVEN );
        sleep( 3000 );
        createMavenModuleProjectWizard.next();

        NewLiferayModuleProjectWizardSecondPagePO createMavenModuleProjectSecondPageWizard =
            new NewLiferayModuleProjectWizardSecondPagePO( bot, INDEX_CONFIGURE_COMPONENT_CLASS_VALIDATION_MESSAGE );

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createMavenModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createMavenModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createMavenModuleProjectSecondPageWizard.getPackageName().getText() );

        createMavenModuleProjectSecondPageWizard.getComponentClassName().setText( "@@" );
        sleep( 2000 );
        assertEquals( TEXT_INVALID_CLASS_NAME, createMavenModuleProjectSecondPageWizard.getValidationMessage() );
        createMavenModuleProjectSecondPageWizard.getComponentClassName().setText( "testClassName" );
        sleep( 2000 );

        createMavenModuleProjectSecondPageWizard.getPackageName().setText( "!!" );
        sleep( 2000 );
        assertEquals( TEXT_INVALID_PACKAGE_NAME, createMavenModuleProjectSecondPageWizard.getValidationMessage() );
        createMavenModuleProjectSecondPageWizard.getPackageName().setText( "testPackageName" );
        sleep( 2000 );

        // add properties and check toolbarButton state
        assertTrue( createMavenModuleProjectSecondPageWizard.getAddPropertyKeyButton().isEnabled() );
        assertFalse( createMavenModuleProjectSecondPageWizard.getDeleteButton().isEnabled() );
        assertFalse( createMavenModuleProjectSecondPageWizard.getMoveUpButton().isEnabled() );
        assertFalse( createMavenModuleProjectSecondPageWizard.getMoveDownButton().isEnabled() );

        createMavenModuleProjectSecondPageWizard.getAddPropertyKeyButton().click();
        createMavenModuleProjectSecondPageWizard.getProperties().setFocus();
        assertEquals( TEXT_NAME_MUST_BE_SPECIFIED, createMavenModuleProjectSecondPageWizard.getValidationMessage() );
        assertTrue( createMavenModuleProjectSecondPageWizard.getDeleteButton().isEnabled() );
        createMavenModuleProjectSecondPageWizard.getDeleteButton().click();

        createMavenModuleProjectSecondPageWizard.getAddPropertyKeyButton().click();
        sleep();
        createMavenModuleProjectSecondPageWizard.setPropertiesText( 2, "a" );
        createMavenModuleProjectSecondPageWizard.getProperties().setFocus();
        sleep( 5000 );
        assertEquals( TEXT_VALUE_MUST_BE_SPECIFIED, createMavenModuleProjectSecondPageWizard.getValidationMessage() );
        sleep( 2000 );
        createMavenModuleProjectSecondPageWizard.getProperties().doubleClick( 0, 1 );
        sleep();
        createMavenModuleProjectSecondPageWizard.setPropertiesText( 2, "b" );
        sleep( 3000 );

        createMavenModuleProjectSecondPageWizard.getProperties().setFocus();
        sleep();

        assertTrue( createMavenModuleProjectSecondPageWizard.getDeleteButton().isEnabled() );
        assertFalse( createMavenModuleProjectSecondPageWizard.getMoveUpButton().isEnabled() );
        assertFalse( createMavenModuleProjectSecondPageWizard.getMoveDownButton().isEnabled() );

        createMavenModuleProjectSecondPageWizard.getAddPropertyKeyButton().click();
        sleep();
        createMavenModuleProjectSecondPageWizard.setPropertiesText( 2, "c" );
        sleep( 3000 );
        createMavenModuleProjectSecondPageWizard.getProperties().doubleClick( 1, 1 );
        sleep();
        createMavenModuleProjectSecondPageWizard.setPropertiesText( 2, "d" );
        sleep( 3000 );

        createMavenModuleProjectSecondPageWizard.getProperties().setFocus();
        sleep();

        assertTrue( createMavenModuleProjectSecondPageWizard.getMoveUpButton().isEnabled() );
        assertFalse( createMavenModuleProjectSecondPageWizard.getMoveDownButton().isEnabled() );

        createMavenModuleProjectSecondPageWizard.getMoveUpButton().click();
        assertFalse( createMavenModuleProjectSecondPageWizard.getMoveUpButton().isEnabled() );
        assertTrue( createMavenModuleProjectSecondPageWizard.getMoveDownButton().isEnabled() );
        createMavenModuleProjectSecondPageWizard.getMoveDownButton().click();

        createMavenModuleProjectSecondPageWizard.getDeleteButton().click();

        createMavenModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        String javaFileName = "testClassNamePortlet.java";

        projectTree.expandNode( projectName, "src/main/java", "testPackageName.portlet" ).doubleClick( javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "extends MVCPortlet", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( projectName, pomXmlFileName ).doubleClick();
        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testMvcportletProject/pom.xml" );

        assertContains( "com.liferay.portal", pomXmlFile.getText() );
        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "dependency", pomXmlFile.getText() );
        assertContains( "executions", pomXmlFile.getText() );

        pomXmlFile.close();
    }

    @Test
    public void createServiceModuleProject()
    {
        String projectName = "testServiceProject";

        createMavenModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_SERVICE, TEXT_BUILD_TYPE_MAVEN );
        createMavenModuleProjectWizard.next();

        assertEquals( "", createMavenModuleProjectSecondPageWizard.getComponentClassName().getText() );
        assertEquals( "", createMavenModuleProjectSecondPageWizard.getPackageName().getText() );
        assertEquals( "", createMavenModuleProjectSecondPageWizard.getServiceName().getText() );
        assertTrue( createMavenModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createMavenModuleProjectSecondPageWizard.getBrowseButton().click();
        sleep( 5000 );

        SelectModuleServiceNamePO selectOneServiceName = new SelectModuleServiceNamePO( bot );
        selectOneServiceName.cancel();
        createMavenModuleProjectSecondPageWizard.getBrowseButton().click();
        sleep( 5000 );

        selectOneServiceName.selectServiceName( "gg" );
        sleep( 2000 );
        assertFalse( selectOneServiceName.confirmButton().isEnabled() );
        selectOneServiceName.selectServiceName( "*lifecycleAction" );
        sleep();
        assertTrue( selectOneServiceName.confirmButton().isEnabled() );
        selectOneServiceName.confirm();

        assertEquals(
            "com.liferay.portal.kernel.events.LifecycleAction",
            createMavenModuleProjectSecondPageWizard.getServiceName().getText() );
        assertTrue( createMavenModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createMavenModuleProjectSecondPageWizard.getAddPropertyKeyButton().click();
        sleep();
        createMavenModuleProjectSecondPageWizard.setPropertiesText( 3, "key" );
        sleep( 3000 );
        createMavenModuleProjectSecondPageWizard.getProperties().doubleClick( 0, 1 );
        sleep();
        createMavenModuleProjectSecondPageWizard.setPropertiesText( 3, "login.events.pre" );
        sleep( 3000 );

        createMavenModuleProjectSecondPageWizard.getProperties().setFocus();
        sleep();

        createMavenModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        String javaFileName = "TestServiceProject.java";

        projectTree.expandNode( projectName, "src/main/java", "testServiceProject" ).doubleClick( javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "implements LifecycleAction", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( projectName ).doubleClick( pomXmlFileName );

        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();
        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testServiceProject/pom.xml" );

        assertContains( "com.liferay.portal", pomXmlFile.getText() );
        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "dependency", pomXmlFile.getText() );
        assertContains( "executions", pomXmlFile.getText() );

        pomXmlFile.close();
    }

    @Test
    public void createServiceBuilderModuleProject()
    {
        String projectName = "testServiceBuilderProject";

        createMavenModuleProjectWizard.createModuleProject(
            projectName, MENU_MODULE_SERVICE_BUILDER, TEXT_BUILD_TYPE_MAVEN );
        createMavenModuleProjectWizard.next();

        assertEquals( "", createMavenModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createMavenModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createMavenModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue( projectTree.expandNode( projectName, projectName + "-api" ).isVisible() );
        assertTrue( projectTree.expandNode( projectName, projectName + "-service" ).isVisible() );
        assertTrue( projectTree.expandNode( projectName, projectName + "-service", "service.xml" ).isVisible() );

        projectTree.setFocus();
        sleep( 3000 );
        String pomXmlFileName = "pom.xml";
        projectTree.expandNode( projectName + "-api" ).doubleClick( pomXmlFileName );

        CTabItemPO switchCTabItem1 = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem1.click();
        sleep( 3000 );
        TextEditorPO apiPomXmlFile = eclipse.getTextEditor( "testServiceBuilderProject-api/pom.xml" );

        assertContains( "groupId", apiPomXmlFile.getText() );
        assertContains( "artifactId", apiPomXmlFile.getText() );
        apiPomXmlFile.close();

        projectTree.setFocus();
        sleep( 5000 );
        projectTree.expandNode( projectName + "-service" ).doubleClick( pomXmlFileName );
        CTabItemPO switchCTabItem2 = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem2.click();
        TextEditorPO servicePomXmlFile = eclipse.getTextEditor( "testServiceBuilderProject-service/pom.xml" );

        assertContains( "com.liferay.portal", servicePomXmlFile.getText() );
        assertContains( "artifactId", servicePomXmlFile.getText() );
        assertContains( "dependency", servicePomXmlFile.getText() );
        servicePomXmlFile.close();

        projectTree.getTreeItem( projectName + "-service" ).doAction( "Liferay", "liferay:build-service" );
        sleep( 10000 );

        assertTrue(
            projectTree.expandNode(
                projectName + "-api", "src/main/java", "testServiceBuilderProject.service" ).isVisible() );

        assertTrue(
            projectTree.expandNode(
                projectName + "-service", "src/main/java", "testServiceBuilderProject.model.impl" ).isVisible() );

        eclipse.getPackageExporerView().deleteResouceByName( projectName, true );
    }

    @Test
    public void createActivatorModuleProject()
    {
        String projectName = "testActivatorProject";

        createMavenModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_ACTIVATOR, TEXT_BUILD_TYPE_MAVEN );
        createMavenModuleProjectWizard.next();

        sleep();

        assertTrue( createMavenModuleProjectSecondPageWizard.finishButton().isEnabled() );
        createMavenModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        projectTree.setFocus();

        String javaFileName = "TestActivatorProjectActivator.java";

        projectTree.expandNode( projectName, "src/main/java", "testActivatorProject" ).doubleClick( javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "implements BundleActivator", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( projectName, pomXmlFileName ).doubleClick();
        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testActivatorProject/pom.xml" );

        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "dependency", pomXmlFile.getText() );
        assertContains( "executions", pomXmlFile.getText() );

        pomXmlFile.close();
    }

    @Test
    public void createApiModuleProject()
    {
        String projectName = "testApiProject";

        createMavenModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_API, TEXT_BUILD_TYPE_MAVEN );
        createMavenModuleProjectWizard.next();

        sleep();
        assertTrue( createMavenModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createMavenModuleProjectSecondPageWizard.finish();
        sleep( 10000 );
        projectTree.setFocus();

        String javaFileName = "TestApiProject.java";

        projectTree.expandNode( projectName, "src/main/java", "testApiProject.api" ).doubleClick( javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "public interface TestApiProject", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( projectName, pomXmlFileName ).doubleClick();
        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testApiProject/pom.xml" );

        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "dependency", pomXmlFile.getText() );
        assertContains( "executions", pomXmlFile.getText() );

        pomXmlFile.close();
    }

    @Test
    public void createContentTargetingReportModuleProject()
    {
        String projectName = "testContentTargetingReportProject";

        createMavenModuleProjectWizard.createModuleProject(
            projectName, MENU_MODULE_CONTENT_TARGETING_REPORT, TEXT_BUILD_TYPE_MAVEN );
        createMavenModuleProjectWizard.next();

        sleep();
        assertTrue( createMavenModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createMavenModuleProjectSecondPageWizard.finish();

        sleep( 10000 );
        projectTree.setFocus();

        String javaFileName = "TestContentTargetingReportProjectReport.java";

        projectTree.expandNode(
            projectName, "src/main/java", "testContentTargetingReportProject.content.targeting.report" ).doubleClick(
                javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "extends BaseJSPReport", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( projectName, pomXmlFileName ).doubleClick();
        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testContentTargetingReportProject/pom.xml" );

        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "dependency", pomXmlFile.getText() );
        assertContains( "executions", pomXmlFile.getText() );

        pomXmlFile.close();

    }

    @Test
    public void createContentTargetingRuleModuleProject()
    {
        String projectName = "testContentTargetingRuleProject";

        createMavenModuleProjectWizard.createModuleProject(
            projectName, MENU_MODULE_CONTENT_TARGETING_RULE, TEXT_BUILD_TYPE_MAVEN );
        createMavenModuleProjectWizard.next();

        sleep();
        assertTrue( createMavenModuleProjectSecondPageWizard.finishButton().isEnabled() );
        createMavenModuleProjectSecondPageWizard.finish();

        sleep( 10000 );
        projectTree.setFocus();

        String javaFileName = "TestContentTargetingRuleProjectRule.java";

        projectTree.expandNode(
            projectName, "src/main/java", "testContentTargetingRuleProject.content.targeting.rule" ).doubleClick(
                javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "extends BaseJSPRule", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( projectName, pomXmlFileName ).doubleClick();
        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testContentTargetingRuleProject/pom.xml" );

        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "dependency", pomXmlFile.getText() );
        assertContains( "executions", pomXmlFile.getText() );

        pomXmlFile.close();

    }

    @Test
    public void createContentTargetingTrackingActionModuleProject()
    {
        String projectName = "testContentTargetingTrackingActionProject";

        createMavenModuleProjectWizard.createModuleProject(
            projectName, MENU_MODULE_CONTENT_TARGETING_TRACKING_ACTION, TEXT_BUILD_TYPE_MAVEN );
        createMavenModuleProjectWizard.next();

        sleep();
        assertTrue( createMavenModuleProjectSecondPageWizard.finishButton().isEnabled() );
        createMavenModuleProjectSecondPageWizard.finish();

        sleep( 10000 );
        projectTree.setFocus();

        String javaFileName = "TestContentTargetingTrackingActionProjectTrackingAction.java";

        projectTree.expandNode(
            projectName, "src/main/java",
            "testContentTargetingTrackingActionProject.content.targeting.tracking.action" ).doubleClick( javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "extends BaseJSPTrackingAction", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( projectName, pomXmlFileName ).doubleClick();
        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testContentTargetingTrackingActionProject/pom.xml" );

        assertContains( "com.liferay.content-targeting", pomXmlFile.getText() );
        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "dependency", pomXmlFile.getText() );
        assertContains( "executions", pomXmlFile.getText() );

        pomXmlFile.close();

    }

    @Test
    public void createControlMenuEntryModuleProject()
    {
        String projectName = "testControlMenuEntryProject";

        createMavenModuleProjectWizard.createModuleProject(
            projectName, MENU_MODULE_CONTROL_MENU_ENTRY, TEXT_BUILD_TYPE_MAVEN );
        createMavenModuleProjectWizard.next();

        sleep();
        assertTrue( createMavenModuleProjectSecondPageWizard.finishButton().isEnabled() );
        createMavenModuleProjectSecondPageWizard.finish();

        sleep( 10000 );
        projectTree.setFocus();

        String javaFileName = "TestControlMenuEntryProjectProductNavigationControlMenuEntry.java";

        projectTree.expandNode( projectName, "src/main/java", "testControlMenuEntryProject.control.menu" ).doubleClick(
            javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "link", checkJavaFile.getText() );
        assertContains( "custom-message", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( projectName, pomXmlFileName ).doubleClick();
        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testControlMenuEntryProject/pom.xml" );

        assertContains( "com.liferay.product.navigation.control.menu.api", pomXmlFile.getText() );
        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "dependency", pomXmlFile.getText() );
        assertContains( "executions", pomXmlFile.getText() );

        pomXmlFile.close();
    }

    @Test
    public void createFormFieldModuleProject()
    {
        String projectName = "testFormFieldProject";

        createMavenModuleProjectWizard.createModuleProject(
            projectName, MENU_MODULE_FORM_FIELD, TEXT_BUILD_TYPE_MAVEN );
        createMavenModuleProjectWizard.next();

        sleep();
        assertTrue( createMavenModuleProjectSecondPageWizard.finishButton().isEnabled() );
        createMavenModuleProjectSecondPageWizard.finish();

        sleep( 10000 );
        projectTree.setFocus();

        String javaFileName1 = "TestFormFieldProjectDDMFormFieldRenderer.java";
        String javaFileName2 = "TestFormFieldProjectDDMFormFieldType.java";
        projectTree.expandNode( projectName, "src/main/java", "testFormFieldProject.form.field" ).doubleClick(
            javaFileName1 );
        TextEditorPO checkJavaFile1 = eclipse.getTextEditor( javaFileName1 );
        assertContains( "extends BaseDDMFormFieldRenderer", checkJavaFile1.getText() );
        assertContains( "ddm.TestFormFieldProject", checkJavaFile1.getText() );
        checkJavaFile1.close();

        projectTree.setFocus();
        projectTree.expandNode( projectName, "src/main/java", "testFormFieldProject.form.field" ).doubleClick(
            javaFileName2 );
        TextEditorPO checkJavaFile2 = eclipse.getTextEditor( javaFileName2 );
        assertContains( "extends BaseDDMFormFieldType", checkJavaFile2.getText() );
        assertContains( "DDMFormFieldType.class", checkJavaFile2.getText() );
        checkJavaFile2.close();

        projectTree.setFocus();
        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( projectName, pomXmlFileName ).doubleClick();
        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testFormFieldProject/pom.xml" );

        assertContains( "com.liferay.dynamic.data.mapping.form.field.type", pomXmlFile.getText() );
        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "dependency", pomXmlFile.getText() );
        assertContains( "executions", pomXmlFile.getText() );

        pomXmlFile.close();

    }

    @Test
    public void createPanelAppModuleProject()
    {
        String projectName = "testPanelAppProject";

        createMavenModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_PANEL_APP, TEXT_BUILD_TYPE_MAVEN );
        createMavenModuleProjectWizard.next();

        sleep();
        assertTrue( createMavenModuleProjectSecondPageWizard.finishButton().isEnabled() );
        createMavenModuleProjectSecondPageWizard.finish();

        sleep( 10000 );
        projectTree.setFocus();
        projectTree.expandNode( projectName, "src/main/java" );
        sleep( 3000 );
        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue(
            projectTree.expandNode( projectName, "src/main/java", projectName + ".application.list" ).isVisible() );
        assertTrue( projectTree.expandNode( projectName, "src/main/java", projectName + ".constants" ).isVisible() );
        assertTrue( projectTree.expandNode( projectName, "src/main/java", projectName + ".portlet" ).isVisible() );

        String javaFileName = "TestPanelAppProjectPortlet.java";

        projectTree.expandNode( projectName, "src/main/java", "testPanelAppProject.portlet" ).doubleClick(
            javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "extends MVCPortlet ", checkJavaFile.getText() );
        assertContains( "TestPanelAppProjectPortletKeys.TestPanelAppProject", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( projectName, pomXmlFileName ).doubleClick();
        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testPanelAppProject/pom.xml" );

        assertContains( "testPanelAppProject", pomXmlFile.getText() );
        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "dependency", pomXmlFile.getText() );
        assertContains( "executions", pomXmlFile.getText() );
        pomXmlFile.close();
    }

    @Test
    public void createPortletModuleProject()
    {
        String projectName = "testPortletProject";

        createMavenModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_PORTLET, TEXT_BUILD_TYPE_MAVEN );
        createMavenModuleProjectWizard.next();

        sleep();
        assertTrue( createMavenModuleProjectSecondPageWizard.finishButton().isEnabled() );
        createMavenModuleProjectSecondPageWizard.finish();

        sleep( 10000 );
        projectTree.setFocus();

        String javaFileName = "TestPortletProjectPortlet.java";

        projectTree.expandNode( projectName, "src/main/java", "testPortletProject.portlet" ).doubleClick(
            javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "extends GenericPortlet", checkJavaFile.getText() );
        assertContains( "testPortletProject Portlet - Hello World!", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( projectName, pomXmlFileName ).doubleClick();
        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testPortletProject/pom.xml" );

        assertContains( "testPortletProject", pomXmlFile.getText() );
        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "dependency", pomXmlFile.getText() );
        assertContains( "executions", pomXmlFile.getText() );

        pomXmlFile.close();

    }

    @Test
    public void createPortletConfigurationIconModuleProject()
    {
        String projectName = "testPortletConfigurationIconProject";

        createMavenModuleProjectWizard.createModuleProject(
            projectName, MENU_MODULE_PORTLET_CONFIGURATION_ICON, TEXT_BUILD_TYPE_MAVEN );
        createMavenModuleProjectWizard.next();

        sleep();
        assertTrue( createMavenModuleProjectSecondPageWizard.finishButton().isEnabled() );
        createMavenModuleProjectSecondPageWizard.finish();

        sleep( 10000 );
        projectTree.setFocus();

        String javaFileName = "TestPortletConfigurationIconProjectPortletConfigurationIcon.java";

        projectTree.expandNode(
            projectName, "src/main/java",
            "testPortletConfigurationIconProject.portlet.configuration.icon" ).doubleClick( javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "sample-link", checkJavaFile.getText() );
        assertContains( "extends BasePortletConfigurationIcon", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( projectName, pomXmlFileName ).doubleClick();
        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testPortletConfigurationIconProject/pom.xml" );

        assertContains( "testPortletConfigurationIconProject", pomXmlFile.getText() );
        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "dependency", pomXmlFile.getText() );
        assertContains( "executions", pomXmlFile.getText() );

        pomXmlFile.close();
    }

    @Test
    public void createPortletProviderModuleProject()
    {
        String projectName = "testPortletProviderProject";

        createMavenModuleProjectWizard.createModuleProject(
            projectName, MENU_MODULE_PORTLET_PROVIDER, TEXT_BUILD_TYPE_MAVEN );
        createMavenModuleProjectWizard.next();

        sleep();
        assertTrue( createMavenModuleProjectSecondPageWizard.finishButton().isEnabled() );
        createMavenModuleProjectSecondPageWizard.finish();

        sleep( 10000 );
        projectTree.setFocus();

        assertTrue( projectTree.expandNode( projectName, "src/main/java", projectName + ".constants" ).isVisible() );
        assertTrue( projectTree.expandNode( projectName, "src/main/java", projectName + ".portlet" ).isVisible() );

        String javaFileName1 = "TestPortletProviderProjectAddPortletProvider.java";

        projectTree.expandNode( projectName, "src/main/java", "testPortletProviderProject.portlet" ).doubleClick(
            javaFileName1 );
        TextEditorPO checkJavaFile1 = eclipse.getTextEditor( javaFileName1 );
        assertContains( "service.ranking:Integer=", checkJavaFile1.getText() );
        assertContains( "TestPortletProviderProjectPortletKeys.TestPortletProviderProject", checkJavaFile1.getText() );
        checkJavaFile1.close();

        sleep( 3000 );

        projectTree.setFocus();
        String javaFileName2 = "TestPortletProviderProjectPortlet.java";

        projectTree.expandNode( projectName, "src/main/java", "testPortletProviderProject.portlet" ).doubleClick(
            javaFileName2 );
        TextEditorPO checkJavaFile2 = eclipse.getTextEditor( javaFileName2 );
        assertContains( "testPortletProviderProject Portlet", checkJavaFile2.getText() );
        assertContains( "power-user,user", checkJavaFile2.getText() );
        checkJavaFile2.close();

        projectTree.setFocus();

        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( projectName, pomXmlFileName ).doubleClick();
        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testPortletProviderProject/pom.xml" );

        assertContains( "testPortletProviderProject", pomXmlFile.getText() );
        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "dependency", pomXmlFile.getText() );
        assertContains( "executions", pomXmlFile.getText() );

        pomXmlFile.close();
    }

    @Test
    public void createPortletToolBarContributorModuleProject()
    {
        String projectName = "testPortletToolBarContributorProject";

        createMavenModuleProjectWizard.createModuleProject(
            projectName, MENU_MODULE_PORTLET_TOOLBAR_CONTRIBUTOR, TEXT_BUILD_TYPE_MAVEN );
        createMavenModuleProjectWizard.next();

        sleep();
        assertTrue( createMavenModuleProjectSecondPageWizard.finishButton().isEnabled() );
        createMavenModuleProjectSecondPageWizard.finish();

        sleep( 10000 );
        projectTree.setFocus();

        String javaFileName = "TestPortletToolBarContributorProjectPortletToolbarContributor.java";

        projectTree.expandNode(
            projectName, "src/main/java",
            "testPortletToolBarContributorProject.portlet.toolbar.contributor" ).doubleClick( javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "implements PortletToolbarContributor", checkJavaFile.getText() );
        assertContains( "list-of-links", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( projectName, pomXmlFileName ).doubleClick();
        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testPortletToolBarContributorProject/pom.xml" );

        assertContains( "testPortletToolBarContributorProject", pomXmlFile.getText() );
        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "dependency", pomXmlFile.getText() );
        assertContains( "executions", pomXmlFile.getText() );

        pomXmlFile.close();
    }

    @Test
    public void createRestModuleProject()

    {
        String projectName = "testRestProject";

        createMavenModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_REST, TEXT_BUILD_TYPE_MAVEN );
        createMavenModuleProjectWizard.next();

        sleep();
        assertTrue( createMavenModuleProjectSecondPageWizard.finishButton().isEnabled() );
        createMavenModuleProjectSecondPageWizard.finish();

        sleep( 10000 );
        projectTree.setFocus();

        String javaFileName = "TestRestProjectApplication.java";

        projectTree.expandNode( projectName, "src/main/java", "testRestProject.application" ).doubleClick(
            javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "extends Application", checkJavaFile.getText() );
        assertContains( "Good morning!", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( projectName, pomXmlFileName ).doubleClick();
        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testRestProject/pom.xml" );

        assertContains( "testRestProject", pomXmlFile.getText() );
        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "dependency", pomXmlFile.getText() );
        assertContains( "executions", pomXmlFile.getText() );

        pomXmlFile.close();
    }

    @Test
    public void createServiceWrapperModuleProject()

    {
        String projectName = "testServiceWrapperProject";

        createMavenModuleProjectWizard.createModuleProject(
            projectName, MENU_MODULE_SERVICE_WRAPPER, TEXT_BUILD_TYPE_MAVEN );
        createMavenModuleProjectWizard.next();

        sleep();
        assertTrue( createMavenModuleProjectSecondPageWizard.finishButton().isEnabled() );

        SelectModuleServiceNamePO selectOneServiceName = new SelectModuleServiceNamePO( bot );
        createMavenModuleProjectSecondPageWizard.getBrowseButton().click();
        sleep( 5000 );
        selectOneServiceName.selectServiceName( "*BookmarksEntryService" );
        selectOneServiceName.confirm();
        sleep();
        createMavenModuleProjectSecondPageWizard.finish();

        sleep( 10000 );
        projectTree.setFocus();

        String javaFileName = "TestServiceWrapperProject.java";

        projectTree.expandNode( projectName, "src/main/java", "testServiceWrapperProject" ).doubleClick( javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "extends BookmarksEntryServiceWrapper", checkJavaFile.getText() );
        assertContains( "service = ServiceWrapper.class", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( projectName, pomXmlFileName ).doubleClick();
        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testServiceWrapperProject/pom.xml" );

        assertContains( "testServiceWrapperProject", pomXmlFile.getText() );
        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "dependency", pomXmlFile.getText() );
        assertContains( "executions", pomXmlFile.getText() );

        pomXmlFile.close();
    }

    @Test
    public void createSimulationPanelEntryModuleProject()

    {
        String projectName = "testSimulationPanelEntryProject";

        createMavenModuleProjectWizard.createModuleProject(
            projectName, MENU_MODULE_SIMULATION_PANEL_ENTRY, TEXT_BUILD_TYPE_MAVEN );
        createMavenModuleProjectWizard.next();

        sleep();
        assertTrue( createMavenModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createMavenModuleProjectSecondPageWizard.finish();

        sleep( 10000 );
        projectTree.setFocus();

        String javaFileName = "TestSimulationPanelEntryProjectSimulationPanelApp.java";

        projectTree.expandNode(
            projectName, "src/main/java", "testSimulationPanelEntryProject.application.list" ).doubleClick(
                javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "simulation-sample", checkJavaFile.getText() );
        assertContains( "SimulationPanelCategory.SIMULATION", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( projectName, pomXmlFileName ).doubleClick();
        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testSimulationPanelEntryProject/pom.xml" );

        assertContains( "testSimulationPanelEntryProject", pomXmlFile.getText() );
        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "dependency", pomXmlFile.getText() );
        assertContains( "executions", pomXmlFile.getText() );

        pomXmlFile.close();
    }

    @Test
    public void createTemplateContextContributorModuleProject()

    {
        String projectName = "testTemplateContextContributorProject";

        createMavenModuleProjectWizard.createModuleProject(
            projectName, MENU_MODULE_TEMPLATE_CONTEXT_CONTRIBUTOR, TEXT_BUILD_TYPE_MAVEN );
        createMavenModuleProjectWizard.next();

        sleep();
        assertTrue( createMavenModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createMavenModuleProjectSecondPageWizard.finish();

        sleep( 10000 );
        projectTree.setFocus();

        String javaFileName = "TestTemplateContextContributorProjectTemplateContextContributor.java";

        projectTree.expandNode(
            projectName, "src/main/java", "testTemplateContextContributorProject.theme.contributor" ).doubleClick(
                javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "implements TemplateContextContributor", checkJavaFile.getText() );
        assertContains( "TemplateContextContributor.class", checkJavaFile.getText() );
        checkJavaFile.close();

        projectTree.setFocus();

        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( projectName, pomXmlFileName ).doubleClick();
        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testTemplateContextContributorProject/pom.xml" );

        assertContains( "testTemplateContextContributorProject", pomXmlFile.getText() );
        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "dependency", pomXmlFile.getText() );
        assertContains( "executions", pomXmlFile.getText() );

        pomXmlFile.close();
    }

    @Test
    public void createThemeModuleProject()
    {
        String projectName = "testThemeProject";

        createMavenModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_THEME, TEXT_BUILD_TYPE_MAVEN );
        assertFalse( createMavenModuleProjectWizard.nextButton().isEnabled() );;
        assertTrue( createMavenModuleProjectWizard.finishButton().isEnabled() );

        createMavenModuleProjectWizard.finish();

        sleep( 10000 );
        projectTree.setFocus();

        String scssFileName = "_custom.scss";

        projectTree.expandNode( projectName, "src", "main", "webapp", "css" ).doubleClick( scssFileName );

        TextEditorPO checkScssFile = eclipse.getTextEditor( scssFileName );

        assertContains( "inject:imports", checkScssFile.getText() );
        assertContains( "endinject", checkScssFile.getText() );
        checkScssFile.close();

        projectTree.setFocus();

        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( projectName, pomXmlFileName ).doubleClick();
        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testThemeProject/pom.xml" );

        assertContains( "testThemeProject", pomXmlFile.getText() );
        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "dependency", pomXmlFile.getText() );
        assertContains( "executions", pomXmlFile.getText() );

        pomXmlFile.close();

        eclipse.getPackageExporerView().deleteResouceByName( projectName, true );
    }

    @Test
    public void createThemeContributorModuleProject()

    {
        String projectName = "testThemeContributorProject";

        createMavenModuleProjectWizard.createModuleProject(
            projectName, MENU_MODULE_THEME_CONTRIBUTOR, TEXT_BUILD_TYPE_MAVEN );
        createMavenModuleProjectWizard.next();

        sleep();
        assertTrue( createMavenModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createMavenModuleProjectSecondPageWizard.finish();

        sleep( 10000 );
        projectTree.setFocus();

        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/resources", "META-INF", "resources", "css",
                projectName + "_rtl.css" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/resources", "META-INF", "resources", "css", projectName + ".css" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName, "src/main/resources", "META-INF", "resources", "css",
                projectName + ".scss" ).isVisible() );

        projectTree.setFocus();
        String scssFileName = "_body.scss";

        projectTree.expandNode(
            projectName, "src/main/resources", "META-INF", "resources", "css", projectName ).doubleClick(
                scssFileName );

        TextEditorPO checkScssFile = eclipse.getTextEditor( scssFileName );

        assertContains( "background-color:", checkScssFile.getText() );
        checkScssFile.close();

        projectTree.setFocus();

        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( projectName, pomXmlFileName ).doubleClick();
        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testThemeContributorProject/pom.xml" );

        assertContains( "testThemeContributorProject", pomXmlFile.getText() );
        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "dependency", pomXmlFile.getText() );
        assertContains( "executions", pomXmlFile.getText() );

        pomXmlFile.close();
    }
}

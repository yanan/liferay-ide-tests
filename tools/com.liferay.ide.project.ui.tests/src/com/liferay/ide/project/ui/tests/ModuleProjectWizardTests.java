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
import org.junit.Test;

import com.liferay.ide.project.ui.tests.page.CreateModuleProjectWizardPO;
import com.liferay.ide.project.ui.tests.page.ModuleProjectWizardSecondPagePO;
import com.liferay.ide.project.ui.tests.page.SelectModuleServiceNamePO;
import com.liferay.ide.ui.tests.SWTBotBase;
import com.liferay.ide.ui.tests.swtbot.page.TextEditorPO;
import com.liferay.ide.ui.tests.swtbot.page.TreePO;

/**
 * @author Ying Xu
 */
public class ModuleProjectWizardTests extends SWTBotBase implements ModuleProjectWizard
{

    @AfterClass
    public static void cleanAll()
    {
        eclipse.closeShell( LABEL_NEW_LIFERAY_MODULE_PROJECT );
        eclipse.getPackageExporerView().deleteProjectExcludeNames( new String[] { getLiferayPluginsSdkName() }, true );
    }

    @Test
    public void createMvcportletModuleProject()
    {
        String projectName = "testMvcportletProject";

        CreateModuleProjectWizardPO createModuleProjectWizard =
            new CreateModuleProjectWizardPO( bot, INDEX_NEW_LIFERAY_MODULE_PROJECT_VALIDATION_MESSAGE );

        createModuleProjectWizard.createModuleProject( projectName );

        assertEquals( MENU_MODULE_MVCPORTLET, createModuleProjectWizard.getProjectTemplateNameComboBox().getText() );

        String[] expectedModuleProjectTemplateItems = { MENU_MODULE_ACTIVATOR, MENU_MODULE_CONTENTTARGETINGGREPORT,
            MENU_MODULE_CONTENTTARGETINGRULE, MENU_MODULE_CONTENTTARGETINGTRACKINGACTION, MENU_MODULE_CONTROLMENUENTRY,
            MENU_MODULE_MVCPORTLET, MENU_MODULE_PANELAPP, MENU_MODULE_PORTLET, MENU_MODULE_PORTLETPROVIDER,
            MENU_MODULE_SERVICE, MENU_MODULE_SERVICEBUILDER, MENU_MODULE_SERVICEWRAPPER };

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

        Keyboard keyPress = KeyboardFactory.getAWTKeyboard();
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

        TreePO projectTree = eclipse.getPackageExporerView().getProjectTree();

        projectTree.setFocus();
        String javaFileName = "TestmvcportletprojectmvcportletPortlet.java";
        projectTree.expandNode( projectName, "src/main/java", "com.example.portlet" ).doubleClick( javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "extends MVCPortlet", checkJavaFile.getText() );

        projectTree.setFocus();

        String buildGradleFileName = "build.gradle";
        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );
    }

    @Test
    public void createServiceModuleProject()
    {
        String projectName = "testServiceProject";

        CreateModuleProjectWizardPO createModuleProjectWizard = new CreateModuleProjectWizardPO( bot );

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

        Keyboard keyPress = KeyboardFactory.getAWTKeyboard();
        keyPress.pressShortcut( enter );
        sleep();

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        TreePO projectTree = eclipse.getPackageExporerView().getProjectTree();

        String javaFileName = "Testserviceprojectservice.java";
        projectTree.expandNode( projectName, "src/main/java", "com.example" ).doubleClick( javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "implements LifecycleAction", checkJavaFile.getText() );

        projectTree.setFocus();

        String buildGradleFileName = "build.gradle";
        projectTree.expandNode( projectName ).doubleClick( buildGradleFileName );

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        assertContains( "buildscript", buildGradleFile.getText() );
        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "repositories", buildGradleFile.getText() );
    }

    @Before
    public void openWizard()
    {
        Assume.assumeTrue( runTest() || runAllTests() );

        eclipse.getLiferayWorkspacePerspective().activate();

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();
        sleep( 15000 );
    }

    @Test
    public void createServiceBuilderModuleProject()
    {
        String projectName = "testServiceBuilderProject";

        CreateModuleProjectWizardPO createModuleProjectWizard = new CreateModuleProjectWizardPO( bot );

        createModuleProjectWizard.createModuleProject( projectName, MENU_MODULE_SERVICEBUILDER );
        createModuleProjectWizard.next();

        ModuleProjectWizardSecondPagePO createModuleProjectSecondPageWizard = new ModuleProjectWizardSecondPagePO(
            bot, INDEX_SERVICEBUILDER_CONFIGURE_COMPONENT_CLASS_VALIDATION_MESSAGE );

        assertEquals( TEXT_CONFIGURE_COMPONENT_CLASS, createModuleProjectSecondPageWizard.getValidationMessage() );
        assertEquals( "", createModuleProjectSecondPageWizard.getPackageName().getText() );
        assertTrue( createModuleProjectSecondPageWizard.finishButton().isEnabled() );

        createModuleProjectSecondPageWizard.finish();
        sleep( 10000 );

        TreePO projectTree = eclipse.getPackageExporerView().getProjectTree();

        assertTrue( projectTree.getTreeItem( projectName ).isVisible() );
        assertTrue( projectTree.getTreeItem( projectName + "-api" ).isVisible() );
        assertTrue( projectTree.getTreeItem( projectName + "-service" ).isVisible() );
        assertTrue( projectTree.expandNode( projectName + "-service", "service.xml" ).isVisible() );

        String buildGradleFileName = "build.gradle";

        TextEditorPO buildGradleFile = eclipse.getTextEditor( buildGradleFileName );

        projectTree.expandNode( projectName + "-api" ).doubleClick( buildGradleFileName );
        assertContains( "dependencies", buildGradleFile.getText() );
        buildGradleFile.close();

        projectTree.setFocus();
        projectTree.expandNode( projectName + "-service" ).doubleClick( buildGradleFileName );

        assertContains( "apply plugin", buildGradleFile.getText() );
        assertContains( "dependencies", buildGradleFile.getText() );
        assertContains( "buildService", buildGradleFile.getText() );
        buildGradleFile.close();

        projectTree.getTreeItem( projectName + "-service" ).doAction( "Liferay", "Gradle", "build-service" );
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
            projectTree.expandNode( projectName + "-api", "src/main/java", "com.example.service" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                projectName + "-service", "src/main/java", "com.example.service.impl" ).isVisible() );

        eclipse.getPackageExporerView().deleteResouceByName( projectName, true );
    }

    @Test
    public void validationProjectName()
    {
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

    @After
    public void waitForCreate()
    {
        sleep();
    }
}

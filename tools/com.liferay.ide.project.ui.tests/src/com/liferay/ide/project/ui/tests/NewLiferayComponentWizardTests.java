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

import com.liferay.ide.project.ui.tests.page.AddOverriddenFilePO;
import com.liferay.ide.project.ui.tests.page.ComponentModelClassSelectionPO;
import com.liferay.ide.project.ui.tests.page.ComponentPackageSelectionPO;
import com.liferay.ide.project.ui.tests.page.CreateModuleProjectWizardPO;
import com.liferay.ide.project.ui.tests.page.FragmentHostOSGIBundlePO;
import com.liferay.ide.project.ui.tests.page.NewLiferayComponentWizardPO;
import com.liferay.ide.project.ui.tests.page.NewLiferayServerPO;
import com.liferay.ide.project.ui.tests.page.NewLiferayServerRuntimePO;
import com.liferay.ide.project.ui.tests.page.NewModuleFragmentWizardPO;
import com.liferay.ide.project.ui.tests.page.NewModuleFragmentWizardSecondPagePO;
import com.liferay.ide.project.ui.tests.page.SelectModuleServiceNamePO;
import com.liferay.ide.ui.tests.SWTBotBase;
import com.liferay.ide.ui.tests.swtbot.page.TextEditorPO;
import com.liferay.ide.ui.tests.swtbot.page.TreePO;

/**
 * @author Ying Xu
 */
public class NewLiferayComponentWizardTests extends SWTBotBase
    implements NewLiferayComponentWizard, ModuleProjectWizard, NewServerRuntimeWizard
{

    static String projectName = "testComponent";

    CreateModuleProjectWizardPO createModuleProjectWizard = new CreateModuleProjectWizardPO( bot );

    @AfterClass
    public static void cleanAll()
    {
        eclipse.closeShell( LABEL_NEW_LIFERAY_COMPONENT );
        eclipse.getPackageExporerView().deleteResouceByName( projectName, true );
    }

    @Test
    public void newLiferayDefaultPortletComponentClass()
    {
        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();

        if( !hasAddedProject )
        {
            createModuleProjectWizard.createModuleProject( projectName );
            createModuleProjectWizard.finish();
            sleep( 15000 );
        }
        else
        {
            createModuleProjectWizard.cancel();
        }

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayComponentClass().click();

        NewLiferayComponentWizardPO newLiferayComponentWizard =
            new NewLiferayComponentWizardPO( bot, LABEL_NEW_LIFERAY_COMPONENT, INDEX_VALIDATION_MESSAGE );

        newLiferayComponentWizard.waitForPageToOpen();

        newLiferayComponentWizard.selectProject( projectName );

        assertEquals( projectName, newLiferayComponentWizard.getProjectName().getText() );
        assertEquals( TEXT_DEFAULT_PACKAGE_NAME_VALUE, newLiferayComponentWizard.getPackageName().getText() );
        assertEquals(
            TEXT_DEFAULT_COMPONENT_CLASS_NAME_VALUE, newLiferayComponentWizard.getComponentClassName().getText() );
        assertEquals(
            TEXT_DEFAULT_COMPONENT_CLASS_TEMPLATE_VALUE,
            newLiferayComponentWizard.getComponentClassTemplate().getText() );
        assertEquals( TEXT_CREATE_COMPONENT_CLASS_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertTrue( newLiferayComponentWizard.finishButton().isEnabled() );

        // validation test for Package name
        newLiferayComponentWizard.getPackageName().setText( "1" );
        sleep( 1000 );
        assertEquals( TEXT_ENTER_PROJECT_NAME_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.getPackageName().setText( "-" );
        sleep( 1000 );
        assertEquals(
            " \"-\"" + TEXT_VALIDATION_PACKAGE_NAME_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.getPackageName().setText( "." );
        sleep( 1000 );
        assertEquals(
            " \".\"" + TEXT_VALIDATION_PACKAGE_NAME_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.getPackageName().setText( "/" );
        sleep( 1000 );
        assertEquals(
            " \"/\"" + TEXT_VALIDATION_PACKAGE_NAME_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.getPackageName().setText( "a" );
        sleep( 1000 );
        assertEquals( TEXT_CREATE_COMPONENT_CLASS_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertTrue( newLiferayComponentWizard.finishButton().isEnabled() );

        newLiferayComponentWizard.getPackageBrowseButton().click();

        ComponentPackageSelectionPO selectPackageName = new ComponentPackageSelectionPO( bot, BUTTON_OK );

        selectPackageName.setPackageSelectionText( "content" );
        selectPackageName.confirm();

        assertEquals( TEXT_CREATE_COMPONENT_CLASS_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertTrue( newLiferayComponentWizard.finishButton().isEnabled() );

        // validation test for Component Class Name
        newLiferayComponentWizard.getComponentClassName().setText( "1" );
        sleep( 1000 );
        assertEquals(
            TEXT_VALIDATION_COMPONENT_CLASS_TEMPLATE_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.getComponentClassName().setText( "-" );
        sleep( 1000 );
        assertEquals(
            TEXT_VALIDATION_COMPONENT_CLASS_TEMPLATE_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.getComponentClassName().setText( "." );
        sleep( 1000 );
        assertEquals(
            TEXT_VALIDATION_COMPONENT_CLASS_TEMPLATE_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.getComponentClassName().setText( "/" );
        sleep( 1000 );
        assertEquals(
            TEXT_VALIDATION_COMPONENT_CLASS_TEMPLATE_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.getComponentClassName().setText( "a" );
        sleep( 1000 );
        assertEquals( TEXT_CREATE_COMPONENT_CLASS_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertTrue( newLiferayComponentWizard.finishButton().isEnabled() );

        newLiferayComponentWizard.getComponentClassName().setText( "" );
        newLiferayComponentWizard.finish();
        sleep( 5000 );

        TreePO projectTree = eclipse.getPackageExporerView().getProjectTree();

        String javaFileName = "TestcomponentPortlet.java";

        assertTrue( projectTree.expandNode( projectName, "src/main/java", "content", javaFileName ).isVisible() );

        projectTree.expandNode( projectName, "src/main/java", "content" ).doubleClick( javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "TestcomponentPortlet extends GenericPortlet", checkJavaFile.getText() );

        checkJavaFile.close();

    }

    @Test
    public void newLiferayModelListenerComponentClass()
    {
        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();

        if( !hasAddedProject )
        {
            createModuleProjectWizard.createModuleProject( projectName );
            createModuleProjectWizard.finish();
            sleep( 15000 );
        }
        else
        {
            createModuleProjectWizard.cancel();
        }

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayComponentClass().click();

        NewLiferayComponentWizardPO newLiferayComponentWizard = new NewLiferayComponentWizardPO(
            bot, LABEL_NEW_LIFERAY_COMPONENT, INDEX_MODEL_LISTENER_VALIDATION_MESSAGE );

        newLiferayComponentWizard.waitForPageToOpen();

        newLiferayComponentWizard.selectProject( projectName );
        newLiferayComponentWizard.selectTemplate( MENU_TEMPLATE_MODEL_LISTENER );

        assertEquals( projectName, newLiferayComponentWizard.getProjectName().getText() );
        assertEquals( TEXT_DEFAULT_PACKAGE_NAME_VALUE, newLiferayComponentWizard.getPackageName().getText() );
        assertEquals(
            TEXT_DEFAULT_COMPONENT_CLASS_NAME_VALUE, newLiferayComponentWizard.getComponentClassName().getText() );
        assertEquals( MENU_TEMPLATE_MODEL_LISTENER, newLiferayComponentWizard.getComponentClassTemplate().getText() );

        newLiferayComponentWizard.getPackageBrowseButton().click();

        ComponentPackageSelectionPO selectPackageName = new ComponentPackageSelectionPO( bot, BUTTON_OK );

        selectPackageName.setPackageSelectionText( "content" );
        selectPackageName.confirm();

        // validation test for model class
        assertEquals( "", newLiferayComponentWizard.getModelClaa().getText() );
        newLiferayComponentWizard.setModelClassName( "tt" );
        sleep();
        assertEquals(
            " \"tt\"" + TEXT_NOT_AMONG_POSSIBLE_VALUES_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.setModelClassName( "1" );
        sleep();
        assertEquals(
            " \"1\"" + TEXT_NOT_AMONG_POSSIBLE_VALUES_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.setModelClassName( "-" );
        sleep();
        assertEquals(
            " \"-\"" + TEXT_NOT_AMONG_POSSIBLE_VALUES_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.setModelClassName( "." );
        sleep();
        assertEquals(
            " \".\"" + TEXT_NOT_AMONG_POSSIBLE_VALUES_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.setModelClassName( "" );
        sleep();
        assertEquals( TEXT_VALIDATION_MODEL_LISTENER_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );

        newLiferayComponentWizard.getBrowseButton().click();
        sleep( 5000 );

        ComponentModelClassSelectionPO selectModelClass = new ComponentModelClassSelectionPO( bot, BUTTON_OK );

        selectModelClass.setModelClassSelectionText( "tt" );
        sleep();
        assertFalse( selectModelClass.confirmButton().isEnabled() );
        selectModelClass.setModelClassSelectionText( "*com.liferay.blogs.kernel.model.BlogsEntry" );
        sleep();
        selectModelClass.confirm();
        sleep( 2000 );

        assertEquals( "com.liferay.blogs.kernel.model.BlogsEntry", newLiferayComponentWizard.getModelClaa().getText() );
        newLiferayComponentWizard.finish();
        sleep( 5000 );

        TreePO projectTree = eclipse.getPackageExporerView().getProjectTree();

        String javaFileName = "TestcomponentModelListener.java";

        assertTrue( projectTree.expandNode( projectName, "src/main/java", "content", javaFileName ).isVisible() );

        projectTree.expandNode( projectName, "src/main/java", "content" ).doubleClick( javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "TestcomponentModelListener extends BaseModelListener<BlogsEntry>", checkJavaFile.getText() );

        checkJavaFile.close();

    }

    @Test
    public void newLiferayServiceWrapperComponentClass()
    {
        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();

        if( !hasAddedProject )
        {
            createModuleProjectWizard.createModuleProject( projectName );
            createModuleProjectWizard.finish();
            sleep( 15000 );
        }
        else
        {
            createModuleProjectWizard.cancel();
        }

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayComponentClass().click();

        NewLiferayComponentWizardPO newLiferayComponentWizard = new NewLiferayComponentWizardPO(
            bot, LABEL_NEW_LIFERAY_COMPONENT, INDEX_SERVICE_WRAPPER_VALIDATION_MESSAGE );

        newLiferayComponentWizard.waitForPageToOpen();

        newLiferayComponentWizard.selectProject( projectName );
        newLiferayComponentWizard.selectTemplate( MENU_TEMPLATE_SERVICE_WRAPPER );

        assertEquals( projectName, newLiferayComponentWizard.getProjectName().getText() );
        assertEquals( TEXT_DEFAULT_PACKAGE_NAME_VALUE, newLiferayComponentWizard.getPackageName().getText() );
        assertEquals(
            TEXT_DEFAULT_COMPONENT_CLASS_NAME_VALUE, newLiferayComponentWizard.getComponentClassName().getText() );
        assertEquals( MENU_TEMPLATE_SERVICE_WRAPPER, newLiferayComponentWizard.getComponentClassTemplate().getText() );

        assertEquals( "", newLiferayComponentWizard.getServiceName().getText() );
        newLiferayComponentWizard.setServiceName( "tt" );
        sleep();
        assertEquals( TEXT_CREATE_COMPONENT_CLASS_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertTrue( newLiferayComponentWizard.finishButton().isEnabled() );
        newLiferayComponentWizard.setServiceName( "" );
        sleep();
        assertEquals( TEXT_VALIDATION_SERVICE_WRAPPER_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );

        newLiferayComponentWizard.getPackageBrowseButton().click();

        ComponentPackageSelectionPO selectPackageName = new ComponentPackageSelectionPO( bot, BUTTON_OK );

        selectPackageName.setPackageSelectionText( "content" );
        selectPackageName.confirm();

        newLiferayComponentWizard.getBrowseButton().click();
        sleep( 5000 );

        SelectModuleServiceNamePO selectServiceName = new SelectModuleServiceNamePO( bot );

        selectServiceName.selectServiceName( "gg" );
        sleep();
        assertFalse( selectServiceName.confirmButton().isEnabled() );
        selectServiceName.selectServiceName( "*bookmarksEntryLocal" );
        sleep();
        assertTrue( selectServiceName.confirmButton().isEnabled() );
        selectServiceName.confirm();
        sleep( 2000 );

        assertEquals(
            "com.liferay.bookmarks.service.BookmarksEntryLocalServiceWrapper",
            newLiferayComponentWizard.getServiceName().getText() );
        newLiferayComponentWizard.finish();
        sleep( 5000 );

        TreePO projectTree = eclipse.getPackageExporerView().getProjectTree();

        String javaFileName = "TestcomponentServiceHook.java";

        assertTrue( projectTree.expandNode( projectName, "src/main/java", "content", javaFileName ).isVisible() );

        projectTree.expandNode( projectName, "src/main/java", "content" ).doubleClick( javaFileName );

        TextEditorPO checkJavaFile = eclipse.getTextEditor( javaFileName );

        assertContains( "TestcomponentServiceHook extends BookmarksEntryLocalServiceWrapper", checkJavaFile.getText() );

        checkJavaFile.close();

    }

    @Test
    public void newLiferayComponentClassWithoutAvailableModuleProject()
    {
        if( hasAddedProject )
        {
            eclipse.getPackageExporerView().deleteResouceByName( projectName, true );
        }

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayComponentClass().click();

        NewLiferayComponentWizardPO newLiferayComponentWizard =
            new NewLiferayComponentWizardPO( bot, LABEL_NEW_LIFERAY_COMPONENT, INDEX_VALIDATION_MESSAGE );

        newLiferayComponentWizard.waitForPageToOpen();

        // check default initial state
        assertEquals( TEXT_DEFAULT_PROJECT_NAME_VALUE, newLiferayComponentWizard.getProjectName().getText() );
        assertEquals( TEXT_DEFAULT_PACKAGE_NAME_VALUE, newLiferayComponentWizard.getPackageName().getText() );
        assertEquals(
            TEXT_DEFAULT_COMPONENT_CLASS_NAME_VALUE, newLiferayComponentWizard.getComponentClassName().getText() );
        assertEquals(
            TEXT_DEFAULT_COMPONENT_CLASS_TEMPLATE_VALUE,
            newLiferayComponentWizard.getComponentClassTemplate().getText() );
        assertEquals( TEXT_ENTER_PROJECT_NAME_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );

        String[] expectedComponentTemplateItems = { MENU_TEMPLATE_AUTH_FAILURES, MENU_TEMPLATE_AUTH_MAX_FAILURE,
            MENU_TEMPLATE_AUTHENTICATOR, MENU_TEMPLATE_FRIENDLY_URL_MAPPER, MENU_TEMPLATE_GOGO_COMMAND,
            MENU_TEMPLATE_INDEXER_POST_PROCESSOR, MENU_TEMPLATE_LOGIN_PRE_ACTION, MENU_TEMPLATE_MVC_PORTLET,
            MENU_TEMPLATE_MODEL_LISTENER, MENU_TEMPLATE_POLLER_PROCESSOR, MENU_TEMPLATE_PORTLET,
            MENU_TEMPLATE_PORTLET_ACTION_COMMAND, MENU_TEMPLATE_PORTLET_FILTER, MENU_TEMPLATE_REST,
            MENU_TEMPLATE_SERVICE_WRAPPER, MENU_TEMPLATE_STRUTS_IN_ACTION, MENU_TEMPLATE_STRUTS_PORTLET_ACTION };

        String[] componentTemplateItems =
            newLiferayComponentWizard.getComponentClassTemplate().getAvailableComboValues();

        assertTrue( componentTemplateItems.length >= 1 );

        assertEquals( expectedComponentTemplateItems.length, componentTemplateItems.length );

        for( int i = 0; i < componentTemplateItems.length; i++ )
        {
            assertTrue( componentTemplateItems[i].equals( expectedComponentTemplateItems[i] ) );
        }

        newLiferayComponentWizard.cancel();

        // create Liferay Fragment project
        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleFragmentProject().click();

        NewModuleFragmentWizardPO newModuleFragmentProject = new NewModuleFragmentWizardPO( bot );

        newModuleFragmentProject.setProjectName( "fragmentTest" );
        newModuleFragmentProject.next();

        // select OSGi Bundle and Overridden files
        NewModuleFragmentWizardSecondPagePO setModuleFragmentOSGiBundle =
            new NewModuleFragmentWizardSecondPagePO( bot );

        setModuleFragmentOSGiBundle.getOSGiBundleButton().click();

        FragmentHostOSGIBundlePO selectOSGiBundle = new FragmentHostOSGIBundlePO( bot, BUTTON_OK );

        AddOverriddenFilePO addJSPFiles = new AddOverriddenFilePO( bot );

        selectOSGiBundle.setOSGiBundle( "*bookmarks" );
        selectOSGiBundle.confirm();

        setModuleFragmentOSGiBundle.getAddOverridFilesButton().click();
        addJSPFiles.select( "META-INF/resources/bookmarks/view.jsp" );
        addJSPFiles.confirm();

        setModuleFragmentOSGiBundle.finish();
        sleep( 15000 );

        // open component wizard again then check state to make sure it couldn't support new component class
        eclipse.getCreateLiferayProjectToolbar().getNewLiferayComponentClass().click();

        newLiferayComponentWizard.waitForPageToOpen();

        // check default state again
        assertEquals( TEXT_DEFAULT_PROJECT_NAME_VALUE, newLiferayComponentWizard.getProjectName().getText() );
        assertEquals( TEXT_DEFAULT_PACKAGE_NAME_VALUE, newLiferayComponentWizard.getPackageName().getText() );
        assertEquals(
            TEXT_DEFAULT_COMPONENT_CLASS_NAME_VALUE, newLiferayComponentWizard.getComponentClassName().getText() );
        assertEquals(
            TEXT_DEFAULT_COMPONENT_CLASS_TEMPLATE_VALUE,
            newLiferayComponentWizard.getComponentClassTemplate().getText() );
        assertEquals( TEXT_ENTER_PROJECT_NAME_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );

        newLiferayComponentWizard.cancel();

        eclipse.getPackageExporerView().deleteResouceByName( "fragmentTest", true );

        // create module theme project
        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();
        sleep( 10000 );

        createModuleProjectWizard.createModuleProject( "themeTest", MENU_MODULE_THEME );
        createModuleProjectWizard.finish();
        sleep( 15000 );

        // open component wizard again then check state to make sure it couldn't support new component class
        eclipse.getCreateLiferayProjectToolbar().getNewLiferayComponentClass().click();

        newLiferayComponentWizard.waitForPageToOpen();

        // check default state again
        assertEquals( TEXT_DEFAULT_PROJECT_NAME_VALUE, newLiferayComponentWizard.getProjectName().getText() );
        assertEquals( TEXT_DEFAULT_PACKAGE_NAME_VALUE, newLiferayComponentWizard.getPackageName().getText() );
        assertEquals(
            TEXT_DEFAULT_COMPONENT_CLASS_NAME_VALUE, newLiferayComponentWizard.getComponentClassName().getText() );
        assertEquals(
            TEXT_DEFAULT_COMPONENT_CLASS_TEMPLATE_VALUE,
            newLiferayComponentWizard.getComponentClassTemplate().getText() );
        assertEquals( TEXT_ENTER_PROJECT_NAME_MESSAGE, newLiferayComponentWizard.getValidationMessage() );
        assertFalse( newLiferayComponentWizard.finishButton().isEnabled() );

        newLiferayComponentWizard.cancel();

        eclipse.getPackageExporerView().deleteResouceByName( "themeTest", true );

    }

    @Before
    public void shouldRunTests()
    {
        Assume.assumeTrue( runTest() || runAllTests() );

        hasAddedProject = addedProjects();
    }

    @BeforeClass
    public static void initWizard() throws IOException
    {
        eclipse.getLiferayWorkspacePerspective().activate();

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();

        CreateModuleProjectWizardPO createModuleProjectWizard =
            new CreateModuleProjectWizardPO( bot, INDEX_NEW_LIFERAY_MODULE_PROJECT_VALIDATION_MESSAGE );

        createModuleProjectWizard.createModuleProject( "test" );
        sleep( 120000 );
        assertEquals( TEXT_NEW_LIFERAY_MODULE_MESSAGE, createModuleProjectWizard.getValidationMessage() );

        createModuleProjectWizard.cancel();

        unzipServer();

        NewLiferayServerPO newServer = new NewLiferayServerPO( bot );

        NewLiferayServerRuntimePO setRuntime = new NewLiferayServerRuntimePO( bot );

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayServer().click();

        newServer.getServerTypeTree().selectTreeItem( NODE_LIFERAY_INC, NODE_LIFERAY_7X );
        newServer.next();

        setRuntime.getServerLocation().setText( getLiferayServerDir().toOSString() );

        setRuntime.finish();
    }

}

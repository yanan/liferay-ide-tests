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

package com.liferay.ide.portlet.ui.tests;

import static org.eclipse.swtbot.swt.finder.SWTBotAssert.assertContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swtbot.swt.finder.keyboard.Keyboard;
import org.eclipse.swtbot.swt.finder.keyboard.KeyboardFactory;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.liferay.ide.portlet.ui.tests.page.CreateLiferayPortletWizardPO;
import com.liferay.ide.portlet.ui.tests.page.InterfaceSelectionPO;
import com.liferay.ide.portlet.ui.tests.page.LiferayPortletDeploymentDescriptorPO;
import com.liferay.ide.portlet.ui.tests.page.ModifiersInterfacesMethodStubsPO;
import com.liferay.ide.portlet.ui.tests.page.NewSourceFolderPO;
import com.liferay.ide.portlet.ui.tests.page.PackageSelectionPO;
import com.liferay.ide.portlet.ui.tests.page.PortletDeploymentDescriptorPO;
import com.liferay.ide.portlet.ui.tests.page.SelectTypePO;
import com.liferay.ide.portlet.ui.tests.page.SuperclassSelectionPO;
import com.liferay.ide.project.ui.tests.ProjectWizardTests;
import com.liferay.ide.project.ui.tests.page.CreateProjectWizardPO;
import com.liferay.ide.project.ui.tests.page.NewProjectPO;
import com.liferay.ide.project.ui.tests.page.ProjectTreePO;
import com.liferay.ide.project.ui.tests.page.SetSDKLocationPO;
import com.liferay.ide.ui.tests.ProjectWizard;
import com.liferay.ide.ui.tests.SWTBotBase;
import com.liferay.ide.ui.tests.swtbot.page.DialogPO;
import com.liferay.ide.ui.tests.swtbot.page.EditorPO;
import com.liferay.ide.ui.tests.swtbot.page.SelectionDialogPO;
import com.liferay.ide.ui.tests.swtbot.page.TextEditorPO;
import com.liferay.ide.ui.tests.swtbot.page.TreeItemPO;
import com.liferay.ide.ui.tests.swtbot.page.TreePO;

/**
 * @author Ashley Yuan
 */
public class LiferayPortletWizardTests extends SWTBotBase implements LiferayPortletWizard, ProjectWizard
{

    protected static IPath getCustomLocationBase()
    {
        final IPath customLocationBase =
            org.eclipse.core.internal.utils.FileUtil.canonicalPath( new Path( System.getProperty( "java.io.tmpdir" ) ) ).append(
                "custom-project-location-tests" );

        return customLocationBase;
    }

    @BeforeClass
    public static void openPluginsSDKProject() throws Exception
    {
        String projectName = "SdkProject";

        // toolbarBot.menuClick( TOOLTIP_CREATE_LIFERAY_PROJECT );
        eclipse.getCreateLiferayProjectToolbar().click();

        CreateProjectWizardPO projectPage = new CreateProjectWizardPO( bot );

        projectPage.createSDKProject( projectName, MENU_HOOK );
        projectPage.next();

        SetSDKLocationPO sdkPage = new SetSDKLocationPO( bot );

        sdkPage.setSdkLocation( getLiferayPluginsSdkDir().toString() );
        sdkPage.finish();

        ProjectTreePO deleteProject = new ProjectTreePO( bot, "SdkProject-hook" );

        deleteProject.deleteProject();
    }

    @After
    public void cleanAll()
    {
        eclipse.getPackageExporerView().deleteProjectExcludeNames( new String[] { getLiferayPluginsSdkName() } );
    }

    @Test
    public void createPortletClassPageTest()
    {
        // new liferay plugin project
        eclipse.getNewToolbar().menuClick( TOOLTIP_MENU_ITEM_LIFERAY_PROJECT );

        CreateProjectWizardPO newLiferayProjectPage =
            new CreateProjectWizardPO( bot, TOOLTIP_MENU_ITEM_NEW_LIFERAY_PROJECT, INDEX_VALIDATION_MESSAGE3 );
        assertEquals( "Portlet", newLiferayProjectPage.getPluginTypeComboBox() );

        // create portlet project with launch new MVC default portlet
        newLiferayProjectPage.createSDKProject( "test", "Portlet", false, true );
        newLiferayProjectPage.finish();

        CreateLiferayPortletWizardPO newPortletPage =
            new CreateLiferayPortletWizardPO( bot, TOOLTIP_NEW_LIFERAY_PORTLET, INDEX_VALIDATION_MESSAGE4 );

        newPortletPage.waitForPageToOpen();

        // check initial state
        assertEquals( "test-portlet", newPortletPage.getPortletPluginProject() );
        assertEquals( "/test-portlet/docroot/WEB-INF/src", newPortletPage.getSourceFolderText() );
        assertEquals( "NewPortlet", newPortletPage.getPortletClassText() );
        assertEquals( "com.test", newPortletPage.getJavaPackageText() );
        assertEquals( "com.liferay.util.bridges.mvc.MVCPortlet", newPortletPage.getSuperClassCombobox() );
        assertTrue( newPortletPage.isRadioSelected( RADIO_CREATE_NEW_PORTLET ) );
        assertFalse( newPortletPage.isRadioSelected( RADIO_USE_DEFAULT_PORTLET ) );
        assertTrue( Arrays.equals( newPortletPage.getAvailableSuperclasses(), availableSuperclasses ) );

        newPortletPage.createLiferayPortlet( true );

        assertFalse( newPortletPage.isPortletClassTextEnabled() );
        assertFalse( newPortletPage.isJavaPackageTextEnabled() );
        assertFalse( newPortletPage.isSuperClassComboboxEnabled() );

        newPortletPage.finish();

        String fileName = "view.jsp";

        TextEditorPO editor = eclipse.getTextEditor( fileName );
        assertTrue( editor.isActive() );

        TreePO projectTree = eclipse.showPackageExporerView().getProjectTree();

        fileName = "portlet.xml";

        projectTree.expandNode( new String[] { "test-portlet", "docroot", "WEB-INF" } ).doubleClick( "" );

        editor = eclipse.getTextEditor( fileName );

        assertContains( "<portlet-name>new</portlet-name>", editor.getText() );
        assertContains( "com.liferay.util.bridges.mvc.MVCPortlet", editor.getText() );

        // Ctrl+N shortcut to new liferay project with launch portlet wizard
        Keyboard keyPress = KeyboardFactory.getAWTKeyboard();
        // treeItemPage.doubleClick();
        keyPress.pressShortcut( ctrl, N );

        SelectTypePO newSelectLiferayPage = new SelectTypePO( bot, INDEX_VALIDATION_MESSAGE2 );

        newSelectLiferayPage.createProject( "liferay", "Liferay", TOOLTIP_MENU_ITEM_LIFERAY_PROJECT );
        newSelectLiferayPage.next();

        newLiferayProjectPage.createSDKProject( "test-second", "Portlet", false, true );
        assertEquals( TEXT_CREATE_NEW_PROJECT_AS_LIFERAY_PLUGIN, newLiferayProjectPage.getValidationMessage() );

        newLiferayProjectPage.finish();
        newPortletPage.waitForPageToOpen();

        assertEquals( TEXT_CREATE_A_PORTLET_CLASS, newPortletPage.getValidationMessage() );
        assertEquals( "test-second-portlet", newPortletPage.getPortletPluginProject() );
        assertTrue( newPortletPage.isRadioSelected( RADIO_CREATE_NEW_PORTLET ) );
        assertTrue( newPortletPage.isButtonEnabled( BUTTON_FINISH ) );

        newPortletPage.finish();

        eclipse.showPackageExporerView().getProjectTree().getTreeItem( "Liferay Portlet" ).doAction( "New" );

        assertEquals( "test-portlet", newPortletPage.getPortletPluginProject() );

        assertTrue( newPortletPage.nextButton().isEnabled() );
        assertTrue( newPortletPage.finishButton().isEnabled() );

        newPortletPage.createLiferayPortlet( "test-portlet", true );

        assertTrue( newPortletPage.nextButton().isEnabled() );
        assertTrue( newPortletPage.finishButton().isEnabled() );

        newPortletPage.cancel();

        portletXmlPage.close();

        // new source folder
        treeItemPage.doAction( "Open" );
        assertTrue( portletXmlPage.isActive() );
        treeItemPage.doAction( "New", "Other..." );

        SelectTypePO newTypePage = new SelectTypePO( bot, INDEX_VALIDATION_MESSAGE2 );

        newTypePage.createProject( "Source Folder", "Java", "Source Folder" );
        assertEquals( "Create a Java source folder", newTypePage.getValidationMessage() );
        newTypePage.next();

        NewSourceFolderPO newSourceFolderPage =
            new NewSourceFolderPO( bot, "New Source Folder", INDEX_VALIDATION_MESSAGE3 );

        assertEquals( "test-portlet", newSourceFolderPage.getProjectNameText() );
        assertEquals( TEXT_CREATE_A_NEW_SOURCE_FOLDER, newSourceFolderPage.getValidationMessage() );

        newSourceFolderPage.newSourceFolder( "mysrc" );
        newSourceFolderPage.finish();

        // source folder tests
        eclipse.getCreateLiferayProjectToolbar().getNewLiferayPortlet().click();

        newPortletPage.setSourceFolderText( TEXT_BLANK );
        assertEquals( TEXT_SOURCE_FOLDER_CANNOT_BE_EMPTY, newPortletPage.getValidationMessage() );

        newPortletPage.setSourceFolderText( "123" );
        assertEquals( TEXT_SOUCCE_FOLDER_MUST_BE_ABSOLUTE_PATH, newPortletPage.getValidationMessage() );

        newPortletPage.getBrowseButton().click();

        SelectionDialogPO browseSourceFolderPage = new SelectionDialogPO( bot, "Container Selection", 0 );

        assertEquals( "Choose a Container:", browseSourceFolderPage.getDialogLabel() );
        assertFalse( browseSourceFolderPage.isButtonEnabled( BUTTON_OK ) );
        assertTrue( browseSourceFolderPage.isButtonEnabled( BUTTON_CANCEL ) );

        browseSourceFolderPage.getSelcetFileTree().selectTreeItem( "test-portlet", "docroot" );
        assertTrue( browseSourceFolderPage.isButtonEnabled( BUTTON_OK ) );
        assertTrue( browseSourceFolderPage.isButtonEnabled( BUTTON_CANCEL ) );

        buttonBot.click( BUTTON_OK );
        assertEquals( TEXT_NOT_A_JAVA_SOURCE_FOLDER, newPortletPage.getValidationMessage() );

        newPortletPage.getBrowseButton().click();
        browseSourceFolderPage.getSelcetFileTree().selectTreeItem( "test-portlet", "mysrc" );
        buttonBot.click( BUTTON_OK );
        assertEquals( TEXT_CREATE_A_PORTLET_CLASS, newPortletPage.getValidationMessage() );

        // portlet class tests
        newPortletPage.setPortletClassText( TEXT_BLANK );
        assertEquals( TEXT_CLASS_NAME_CANNOT_BE_EMPTY, newPortletPage.getValidationMessage() );

        newPortletPage.setPortletClassText( "123" );
        assertEquals(
            TEXT_INVALID_JAVA_CLASS_NAME + "'123'" + TEXT_NOT_A_VALID_IDENTIFIER, newPortletPage.getValidationMessage() );

        newPortletPage.setPortletClassText( "aaa" );
        assertEquals( TEXT_JAVA_TYPE_START_WITH_AN_UPPERCASE_LETTER, newPortletPage.getValidationMessage() );

        newPortletPage.setPortletClassText( "MyPortlet" );
        assertEquals( TEXT_CREATE_A_PORTLET_CLASS, newPortletPage.getValidationMessage() );

        // java package tests
        newPortletPage.setJavaPackageText( "123" );
        assertEquals(
            TEXT_INVALID_JAVA_PACKAGE_NAME + "'123'" + TEXT_NOT_A_VALID_JAVA_IDENTIFIER,
            newPortletPage.getValidationMessage() );

        newPortletPage.setJavaPackageText( ".." );
        assertEquals(
            TEXT_INVALID_JAVA_PACKAGE_NAME + TEXT_PACKAGE_NAME_CANNOT_END_WITH_DOT,
            newPortletPage.getValidationMessage() );

        newPortletPage.setJavaPackageText( "MyPackage" );
        assertEquals( TEXT_JAVA_PACKAGE_START_WITH_AN_UPPERCASE_LETTER, newPortletPage.getValidationMessage() );

        newPortletPage.browse( 1 );

        PackageSelectionPO selectPackagePage = new PackageSelectionPO( bot, "Package Selection", 0 );

        assertEquals( "Choose a package:", browseSourceFolderPage.getDialogLabel() );
        selectPackagePage.clickPackage( 0 );

        buttonBot.click( BUTTON_OK );

        assertEquals( TEXT_BLANK, newPortletPage.getJavaPackageText() );
        assertEquals( TEXT_CREATE_A_PORTLET_CLASS, newPortletPage.getValidationMessage() );

        newPortletPage.setJavaPackageText( "myPackage" );
        assertEquals( TEXT_CREATE_A_PORTLET_CLASS, newPortletPage.getValidationMessage() );

        // superclass tests
        newPortletPage.setSuperClassCombobox( TEXT_BLANK );
        assertEquals( TEXT_MUST_SPECIFY_A_PORTLET_SUPERCLASS, newPortletPage.getValidationMessage() );

        newPortletPage.setSuperClassCombobox( "MyClass123" );
        assertEquals( TEXT_SUPERCLASS_MUST_BE_VALID, newPortletPage.getValidationMessage() );

        newPortletPage.setSuperClassCombobox( "com.test.NewPortlet" );
        assertEquals( TEXT_SUPERCLASS_MUST_BE_VALID, newPortletPage.getValidationMessage() );

        newPortletPage.setSuperClassCombobox( "com.liferay.portal.kernel.portlet.LiferayPortlet" );
        assertEquals( TEXT_CREATE_A_PORTLET_CLASS, newPortletPage.getValidationMessage() );

        newPortletPage.browse( 2 );

        SuperclassSelectionPO selectSuperclassPage = new SuperclassSelectionPO( bot, "Superclass Selection", 0 );

        assertEquals( "Choose a superclass:", selectSuperclassPage.getDialogLabel() );
        selectPackagePage.clickPackage( 0 );

        buttonBot.click( BUTTON_OK );
        assertEquals( "com.liferay.util.bridges.alloy.AlloyPortlet", newPortletPage.getSuperClassCombobox() );
        assertEquals( TEXT_CREATE_A_PORTLET_CLASS, newPortletPage.getValidationMessage() );

        newPortletPage.setSuperClassCombobox( "javax.portlet.GenericPortlet" );
        assertEquals( TEXT_CREATE_A_PORTLET_CLASS, newPortletPage.getValidationMessage() );

        newPortletPage.finish();

        TextEditorPO myPortletJavaPage = new TextEditorPO( bot, "MyPortlet.java" );

        assertContains( "package myPackage;", myPortletJavaPage.getText() );
        assertContains( "public class MyPortlet extends GenericPortlet", myPortletJavaPage.getText() );

        TreeItemPO mysrcTreePage = new TreeItemPO( bot, "test-portlet", "mysrc", "myPackage" );
        mysrcTreePage.expand();

        assertTrue( mysrcTreePage.isVisible() );
        assertTrue( myPortletJavaPage.isActive() );

        keyPress.pressShortcut( ctrl, N );

        newSelectLiferayPage.createProject( "liferay", "Liferay", TOOLTIP_MENU_ITEM_LIFERAY_PORTLET );
        newSelectLiferayPage.next();

        newPortletPage.createLiferayPortlet(
            "test-second-portlet", "MySecondPortlet", TEXT_BLANK, "com.test.NewPortlet" );
        newPortletPage.finish();

        TextEditorPO mySecondPortletJavaPage = new TextEditorPO( bot, "MySecondPortlet.java" );

        assertContains( "import com.test.NewPortlet", mySecondPortletJavaPage.getText() );
        assertContains( "public class MySecondPortlet extends NewPortlet", mySecondPortletJavaPage.getText() );
        assertFalse( mySecondPortletJavaPage.getText().contains( "package" ) );

        eclipse.getNewToolbar().menuClick( TOOLTIP_MENU_ITEM_LIFERAY_PORTLET );

        newPortletPage.createLiferayPortlet( "test-second-portlet" );

        assertEquals( " Type 'com.test.NewPortlet'" + TEXT_ALREADY_EXISTS, newPortletPage.getValidationMessage() );

        newPortletPage.cancel();
    }

    public void createProject(
        String filterText, String projectTypeTree, String projectTypeNode, String projectName, String validateMessage1,
        String validateMessage2 )
    {

        // toolbarBot.menuClick( MENU_FILE, MENU_NEW, MENU_PROJECT );

        SelectTypePO newProjectPage = new SelectTypePO( bot, INDEX_VALIDATION_MESSAGE2 );

        newProjectPage.createProject( filterText, projectTypeTree, projectTypeNode );
        assertEquals( validateMessage1, newProjectPage.getValidationMessage() );
        newProjectPage.next();

        NewProjectPO newJavaProjectPage = new NewProjectPO( bot, INDEX_VALIDATION_MESSAGE3 );

        newJavaProjectPage.createJavaProject( projectName );
        assertEquals( validateMessage2, newJavaProjectPage.getValidationMessage() );

        newJavaProjectPage.finish();

        if( !projectTypeNode.equals( "Project" ) )
        {
            DialogPO dialogPage = new DialogPO( bot, "Open Associated Perspective", BUTTON_YES, BUTTON_NO );

            dialogPage.confirm();
        }
    }

    public boolean isInAvailableLists( String[] avaiable, String excepted )
    {
        for( String temp : avaiable )
        {
            if( temp.equals( excepted ) )
            {
                return true;
            }
        }
        return false;
    }

    @Test
    public void liferayPortletDeploymentDescriptor()
    {

        // create portlet project with sample code and check
        // toolbarBot.menuClick( TOOLTIP_CREATE_LIFERAY_PROJECT );
        eclipse.getCreateLiferayProjectToolbar().click();

        CreateProjectWizardPO newLiferayProjectPage =
            new CreateProjectWizardPO( bot, TOOLTIP_MENU_ITEM_NEW_LIFERAY_PROJECT );

        newLiferayProjectPage.createSDKProject( "test", "Portlet", true, false );
        newLiferayProjectPage.finish();

        TreePO porjectTree = eclipse.showPackageExporerView().getProjectTree();

        String fileName = "liferay-portlet.xml";
        porjectTree.expandNode( "test-portlet", "docroot", "WEB-INF" );

        porjectTree.getTreeItem( fileName ).doubleClick();

        TextEditorPO liferayPortletEditor = new TextEditorPO( bot, "liferay-portlet.xml" );

        assertContains(
            "<portlet-name>test</portlet-name>\n\t\t<icon>/icon.png</icon>\n\t\t<header-portlet-css>/css/main.css</header-portlet-css>\n\t\t<footer-portlet-javascript>/js/main.js</footer-portlet-javascript>\n\t\t<css-class-wrapper>test-portlet</css-class-wrapper>",
            liferayPortletEditor.getText() );

        // new liferay portlet and go to speficy liferay portlet deployment descriptor page

        eclipse.getCreateLiferayProjectToolbar().menuClick( TOOLTIP_MENU_ITEM_NEW_LIFERAY_PORTLET );

        CreateLiferayPortletWizardPO newPortletPage = new CreateLiferayPortletWizardPO( bot );
        newPortletPage.next();

        PortletDeploymentDescriptorPO specifyPortletDeploymentDescriptorPage =
            new PortletDeploymentDescriptorPO( bot, INDEX_VALIDATION_MESSAGE6 );

        newPortletPage.next();

        LiferayPortletDeploymentDescriptorPO specifyLiferayPortletDeploymentDescriptorPage =
            new LiferayPortletDeploymentDescriptorPO( bot, INDEX_VALIDATION_MESSAGE7 );

        // check initial state
        assertEquals(
            TEXT_SPECIFY_LIFERAY_PORTLET_DEPLOYMENT_DESCRIPTOR_DETAILS,
            specifyLiferayPortletDeploymentDescriptorPage.getValidationMessage() );

        assertEquals( "/icon.png", specifyLiferayPortletDeploymentDescriptorPage.getIconText() );
        assertFalse( specifyLiferayPortletDeploymentDescriptorPage.isAddToControlPanelChecked() );
        assertEquals( "/css/main.css", specifyLiferayPortletDeploymentDescriptorPage.getCssText() );
        assertEquals( "/js/main.js", specifyLiferayPortletDeploymentDescriptorPage.getJavaScriptText() );
        assertEquals( "new-portlet", specifyLiferayPortletDeploymentDescriptorPage.getCssClassWrapperText() );

        assertEquals( "Sample", specifyLiferayPortletDeploymentDescriptorPage.getDisplayCategoryCombobox() );
        assertTrue( Arrays.equals(
            specifyLiferayPortletDeploymentDescriptorPage.getDisplayCategoryAvailableComboValues(),
            availableDisplayCategories ) );

        assertFalse( specifyLiferayPortletDeploymentDescriptorPage.isAddToControlPanelChecked() );
        assertFalse( specifyLiferayPortletDeploymentDescriptorPage.isEntryCategoryEnabled() );
        assertFalse( specifyLiferayPortletDeploymentDescriptorPage.isEntryWeightEnabled() );
        assertFalse( specifyLiferayPortletDeploymentDescriptorPage.isCreateEntryClassEnabled() );
        assertFalse( specifyLiferayPortletDeploymentDescriptorPage.isEntryClassEnabled() );

        assertEquals(
            "My Account Administration", specifyLiferayPortletDeploymentDescriptorPage.getEntryCategoryCombobox() );
        assertEquals( "1.5", specifyLiferayPortletDeploymentDescriptorPage.getEntryWeightText() );
        assertFalse( specifyLiferayPortletDeploymentDescriptorPage.isCreateEntryClassChecked() );
        assertEquals( "NewPortletControlPanelEntry", specifyLiferayPortletDeploymentDescriptorPage.getEntryClassText() );

        newPortletPage.finish();

        // check codes generate in liferay-portlet.xml
        assertContains(
            "<portlet-name>new</portlet-name>\n\t\t<icon>/icon.png</icon>\n\t\t<header-portlet-css>/css/main.css</header-portlet-css>\n\t\t<footer-portlet-javascript>\n\t\t\t/js/main.js\n\t\t</footer-portlet-javascript>\n\t\t<css-class-wrapper>new-portlet</css-class-wrapper>\n\t</portlet>\n\t<role-mapper>",
            liferayPortletEditor.getText() );

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayPortlet().click();

        newPortletPage.createLiferayPortlet( TEXT_BLANK, "NewPortletOne", null, null );
        newPortletPage.next();
        sleep( 500 );
        newPortletPage.next();

        assertEquals( "new-portlet-one-portlet", specifyLiferayPortletDeploymentDescriptorPage.getCssClassWrapperText() );

        specifyLiferayPortletDeploymentDescriptorPage.specifyLiferayPortletInfo(
            TEXT_BLANK, true, TEXT_BLANK, TEXT_BLANK, TEXT_BLANK );
        assertEquals(
            TEXT_SPECIFY_LIFERAY_PORTLET_DEPLOYMENT_DESCRIPTOR_DETAILS,
            specifyLiferayPortletDeploymentDescriptorPage.getValidationMessage() );

        // display category tests
        specifyLiferayPortletDeploymentDescriptorPage.setDisplayCategoryCombobox( TEXT_BLANK );
        assertEquals( TEXT_CATEGORY_NAME_IS_EMPTY, specifyLiferayPortletDeploymentDescriptorPage.getValidationMessage() );

        specifyLiferayPortletDeploymentDescriptorPage.setDisplayCategoryCombobox( "my1category" );
        assertEquals(
            TEXT_SPECIFY_LIFERAY_PORTLET_DEPLOYMENT_DESCRIPTOR_DETAILS,
            specifyLiferayPortletDeploymentDescriptorPage.getValidationMessage() );

        specifyLiferayPortletDeploymentDescriptorPage.specifyLiferayDisplay( null, true, null, null, true, null );

        assertTrue( Arrays.equals(
            availableEntryCategories,
            specifyLiferayPortletDeploymentDescriptorPage.getEntryCategoryAvailableComboValues() ) );
        assertEquals( "1.5", specifyLiferayPortletDeploymentDescriptorPage.getEntryWeightText() );
        assertEquals(
            "NewPortletOneControlPanelEntry", specifyLiferayPortletDeploymentDescriptorPage.getEntryClassText() );
        assertTrue( specifyLiferayPortletDeploymentDescriptorPage.isEntryCategoryEnabled() );
        assertTrue( specifyLiferayPortletDeploymentDescriptorPage.isEntryWeightEnabled() );
        assertTrue( specifyLiferayPortletDeploymentDescriptorPage.isEntryClassEnabled() );

        newPortletPage.finish();

        assertContains(
            "<portlet-name>new-portlet-one</portlet-name>\n\t\t<icon></icon>\n\t\t<control-panel-entry-category>my</control-panel-entry-category>\n\t\t<control-panel-entry-weight>1.5</control-panel-entry-weight>\n\t\t<control-panel-entry-class>\n\t\t\tcom.test.NewPortletOneControlPanelEntry\n\t\t</control-panel-entry-class>\n\t\t<instanceable>true</instanceable>\n\t\t<header-portlet-css></header-portlet-css>\n\t\t<footer-portlet-javascript></footer-portlet-javascript>\n\t\t<css-class-wrapper></css-class-wrapper>",
            liferayPortletEditor.getText() );

        toolbarBot.menuClick( TOOLTIP_CREATE_LIFERAY_PROJECT, TOOLTIP_MENU_ITEM_NEW_LIFERAY_PORTLET );

        newPortletPage.createLiferayPortlet( TEXT_BLANK, "NewPortletSecond", null, null );
        newPortletPage.next();

        PortletDeploymentDescriptorPO portletDeploymentDescriptorPage = new PortletDeploymentDescriptorPO( bot );

        portletDeploymentDescriptorPage.speficyPortletInfo( "new-portlet-second2", null, null );
        newPortletPage.next();

        assertEquals(
            "new-portlet-second2-portlet", specifyLiferayPortletDeploymentDescriptorPage.getCssClassWrapperText() );

        specifyLiferayPortletDeploymentDescriptorPage.specifyLiferayPortletInfo(
            "unexistentIcon", false, "unexistentCss", "unexistentJavaScript", null );

        assertTrue( isInAvailableLists(
            specifyLiferayPortletDeploymentDescriptorPage.getDisplayCategoryAvailableComboValues(), "my1category" ) );

        // entry tests after checked add to control panel
        specifyLiferayPortletDeploymentDescriptorPage.specifyLiferayDisplay( null, true, null, null, true, null );
        assertEquals(
            TEXT_SPECIFY_LIFERAY_PORTLET_DEPLOYMENT_DESCRIPTOR_DETAILS,
            specifyLiferayPortletDeploymentDescriptorPage.getValidationMessage() );

        specifyLiferayPortletDeploymentDescriptorPage.setEntryCategoryCombobox( TEXT_BLANK );
        assertEquals(
            TEXT_SPECIFY_LIFERAY_PORTLET_DEPLOYMENT_DESCRIPTOR_DETAILS,
            specifyLiferayPortletDeploymentDescriptorPage.getValidationMessage() );

        specifyLiferayPortletDeploymentDescriptorPage.setEntryWeightText( TEXT_BLANK );
        assertEquals(
            TEXT_MUST_SPECIFY_VALID_ENTRY_WEIGHT, specifyLiferayPortletDeploymentDescriptorPage.getValidationMessage() );

        specifyLiferayPortletDeploymentDescriptorPage.setEntryWeightText( "**" );
        assertEquals(
            TEXT_MUST_SPECIFY_VALID_ENTRY_WEIGHT, specifyLiferayPortletDeploymentDescriptorPage.getValidationMessage() );

        specifyLiferayPortletDeploymentDescriptorPage.setEntryWeightText( ".1" );
        assertEquals(
            TEXT_SPECIFY_LIFERAY_PORTLET_DEPLOYMENT_DESCRIPTOR_DETAILS,
            specifyLiferayPortletDeploymentDescriptorPage.getValidationMessage() );

        specifyLiferayPortletDeploymentDescriptorPage.setEntryClassText( TEXT_BLANK );
        assertEquals(
            TEXT_CLASS_NAME_CANNOT_BE_EMPTY, specifyLiferayPortletDeploymentDescriptorPage.getValidationMessage() );

        specifyLiferayPortletDeploymentDescriptorPage.setEntryClassText( "." );
        assertEquals(
            TEXT_DONOT_USE_QUALIDIED_CLASS_NAME, specifyLiferayPortletDeploymentDescriptorPage.getValidationMessage() );

        specifyLiferayPortletDeploymentDescriptorPage.setEntryClassText( "**" );
        assertEquals(
            TEXT_INVALID_JAVA_CLASS_NAME + "'**'" + TEXT_NOT_A_VALID_IDENTIFIER,
            specifyLiferayPortletDeploymentDescriptorPage.getValidationMessage() );

        specifyLiferayPortletDeploymentDescriptorPage.setEntryClassText( "aA" );
        assertEquals(
            TEXT_JAVA_TYPE_START_WITH_AN_UPPERCASE_LETTER,
            specifyLiferayPortletDeploymentDescriptorPage.getValidationMessage() );

        specifyLiferayPortletDeploymentDescriptorPage.setEntryClassText( "MyEntryClass" );
        assertEquals(
            TEXT_SPECIFY_LIFERAY_PORTLET_DEPLOYMENT_DESCRIPTOR_DETAILS,
            specifyLiferayPortletDeploymentDescriptorPage.getValidationMessage() );

        specifyLiferayPortletDeploymentDescriptorPage.specifyLiferayDisplay( null, false, null, null, true, null );

        newPortletPage.finish();

        assertContains(
            "<portlet-name>new-portlet-second2</portlet-name>\n\t\t<icon>unexistentIcon</icon>\n\t\t<header-portlet-css>unexistentCss</header-portlet-css>\n\t\t<footer-portlet-javascript>\n\t\t\tunexistentJavaScript\n\t\t</footer-portlet-javascript>\n\t\t<css-class-wrapper>\n\t\t\tnew-portlet-second2-portlet\n\t\t</css-class-wrapper>",
            liferayPortletEditor.getText() );

        // add to control panel and create entry class tests
        toolbarBot.menuClick( TOOLTIP_CREATE_LIFERAY_PROJECT, TOOLTIP_MENU_ITEM_NEW_LIFERAY_PORTLET );

        newPortletPage.createLiferayPortlet( TEXT_BLANK, "NewPortletThird", TEXT_BLANK, "javax.portlet.GenericPortlet" );
        newPortletPage.next();
        sleep( 500 );
        newPortletPage.next();

        specifyLiferayPortletDeploymentDescriptorPage.specifyLiferayDisplay(
            "Tools", true, "MyEntryCategory", "1", true, null );

        newPortletPage.finish();

        assertContains(
            "<portlet-name>new-portlet-third</portlet-name>\n\t\t<icon>/icon.png</icon>\n\t\t<control-panel-entry-category>\n\t\t\tMyEntryCategory\n\t\t</control-panel-entry-category>\n\t\t<control-panel-entry-weight>1</control-panel-entry-weight>\n\t\t<control-panel-entry-class>\n\t\t\tNewPortletThirdControlPanelEntry\n\t\t</control-panel-entry-class>\n\t\t<header-portlet-css>/css/main.css</header-portlet-css>\n\t\t<footer-portlet-javascript>\n\t\t\t/js/main.js\n\t\t</footer-portlet-javascript>\n\t\t<css-class-wrapper>new-portlet-third-portlet</css-class-wrapper>",
            liferayPortletEditor.getText() );

        TreeItemPO liferayDisplayXmlPage =
            new TreeItemPO( bot, "test-portlet", "docroot", "WEB-INF", "liferay-display.xml" );
        liferayDisplayXmlPage.expand();
        liferayDisplayXmlPage.doubleClick();

        TextEditorPO liferayDisplayEditor = new TextEditorPO( bot, "liferay-display.xml" );

        assertContains(
            "<display>\n\t<category name=\"category.sample\">\n\t\t<portlet id=\"test\" />\n\t\t<portlet id=\"new\"></portlet>\n\t\t<portlet id=\"new-portlet-second2\"></portlet>\n\t</category>\n\t<category name=\"my1category\">\n\t\t<portlet id=\"new-portlet-one\"></portlet>\n\t</category>\n\t<category name=\"category.tools\">\n\t\t<portlet id=\"new-portlet-third\"></portlet>\n\t</category>\n</display>",
            liferayDisplayEditor.getText() );

        TreeItemPO NewPortletThirdControlPanelEntryJavaFile =
            new TreeItemPO(
                bot, "test-portlet", "docroot/WEB-INF/src", "(default package)",
                "NewPortletThirdControlPanelEntry.java" );
        NewPortletThirdControlPanelEntryJavaFile.expand();
        assertTrue( NewPortletThirdControlPanelEntryJavaFile.isVisible() );

        TreeItemPO NewPortletOneControlPanelEntryJavaFile =
            new TreeItemPO(
                bot, "test-portlet", "docroot/WEB-INF/src", "com.test", "NewPortletOneControlPanelEntry.java" );
        NewPortletOneControlPanelEntryJavaFile.expand();
        assertTrue( NewPortletOneControlPanelEntryJavaFile.isVisible() );

        // browse icon tests
        toolbarBot.menuClick( TOOLTIP_CREATE_LIFERAY_PROJECT, TOOLTIP_MENU_ITEM_NEW_LIFERAY_PORTLET );

        newPortletPage.createLiferayPortlet(
            TEXT_BLANK, "NewPortletPortletPortlet", "anotherJavaPackage",
            "com.liferay.portal.kernel.portlet.LiferayPortlet" );
        newPortletPage.next();

        assertEquals( "new-portlet-portlet", specifyPortletDeploymentDescriptorPage.getPortletName() );
        assertEquals( "New Portlet Portlet", specifyPortletDeploymentDescriptorPage.getDisplayName() );
        assertEquals( "New Portlet Portlet", specifyPortletDeploymentDescriptorPage.getPortletTitle() );
        assertEquals( "/html/newportletportlet", specifyPortletDeploymentDescriptorPage.getJspFolder() );

        newPortletPage.next();
        assertEquals(
            "new-portlet-portlet-portlet", specifyLiferayPortletDeploymentDescriptorPage.getCssClassWrapperText() );

        specifyLiferayPortletDeploymentDescriptorPage.browse( 0 );

        IconSelectionPO iconSelectPage = new IconSelectionPO( bot, "Icon Selection", INDEX_VALIDATION_MESSAGE1 );

        assertEquals( "Choose an icon file:", iconSelectPage.getDialogLabel() );
        assertFalse( iconSelectPage.isButtonEnabled( ProjectWizard.BUTTON_OK ) );
        assertTrue( iconSelectPage.isButtonEnabled( ProjectWizard.BUTTON_CANCEL ) );

        iconSelectPage.getSelcetFileTree().selectTreeItem( "WEB-INF", "lib" );
        assertFalse( iconSelectPage.isButtonEnabled( ProjectWizard.BUTTON_OK ) );
        assertTrue( iconSelectPage.isButtonEnabled( ProjectWizard.BUTTON_CANCEL ) );

        iconSelectPage.getSelcetFileTree().selectTreeItem( "js", "main.js" );
        assertTrue( iconSelectPage.isButtonEnabled( ProjectWizard.BUTTON_CANCEL ) );
        buttonBot.click( BUTTON_OK );

        assertEquals( "/js/main.js", specifyLiferayPortletDeploymentDescriptorPage.getIconText() );

        // browse css tests
        specifyLiferayPortletDeploymentDescriptorPage.browse( 1 );

        SelectionDialogPO cssSelectPage = new SelectionDialogPO( bot, "CSS Selection", INDEX_VALIDATION_MESSAGE1 );

        assertEquals( "Choose a css file:", cssSelectPage.getDialogLabel() );
        assertFalse( cssSelectPage.isButtonEnabled( ProjectWizard.BUTTON_OK ) );
        assertTrue( cssSelectPage.isButtonEnabled( ProjectWizard.BUTTON_CANCEL ) );

        iconSelectPage.getSelcetFileTree().selectTreeItem( "WEB-INF", "lib" );
        assertFalse( cssSelectPage.isButtonEnabled( ProjectWizard.BUTTON_OK ) );
        assertTrue( cssSelectPage.isButtonEnabled( ProjectWizard.BUTTON_CANCEL ) );

        cssSelectPage.getSelcetFileTree().selectTreeItem( "view.jsp" );
        assertTrue( cssSelectPage.isButtonEnabled( ProjectWizard.BUTTON_CANCEL ) );
        buttonBot.click( BUTTON_OK );

        assertEquals( "/view.jsp", specifyLiferayPortletDeploymentDescriptorPage.getCssText() );

        // browse javaScript tests
        specifyLiferayPortletDeploymentDescriptorPage.browse( 2 );

        JavaScriptSelectionPO javaScriptSelectPage =
            new JavaScriptSelectionPO( bot, "JavaScript Selection", INDEX_VALIDATION_MESSAGE1 );

        assertEquals( "Choose a javascript file:", javaScriptSelectPage.getDialogLabel() );
        assertFalse( javaScriptSelectPage.isButtonEnabled( ProjectWizard.BUTTON_OK ) );
        assertTrue( javaScriptSelectPage.isButtonEnabled( ProjectWizard.BUTTON_CANCEL ) );

        javaScriptSelectPage.getSelcetFileTree().selectTreeItem( "WEB-INF", "lib" );
        assertFalse( javaScriptSelectPage.isButtonEnabled( ProjectWizard.BUTTON_OK ) );
        assertTrue( javaScriptSelectPage.isButtonEnabled( ProjectWizard.BUTTON_CANCEL ) );

        javaScriptSelectPage.getSelcetFileTree().selectTreeItem( "html", "new", "view.jsp" );
        assertTrue( javaScriptSelectPage.isButtonEnabled( ProjectWizard.BUTTON_CANCEL ) );
        buttonBot.click( BUTTON_OK );

        assertEquals( "/html/new/view.jsp", specifyLiferayPortletDeploymentDescriptorPage.getJavaScriptText() );

        newPortletPage.finish();

        assertContains(
            "<portlet-name>new-portlet-portlet</portlet-name>\n\t\t<icon>/js/main.js</icon>\n\t\t<header-portlet-css>/view.jsp</header-portlet-css>\n\t\t<footer-portlet-javascript>\n\t\t\t/html/new/view.jsp\n\t\t</footer-portlet-javascript>\n\t\t<css-class-wrapper>\n\t\t\tnew-portlet-portlet-portlet\n\t\t</css-class-wrapper>",
            liferayPortletEditor.getText() );

    }

    @Test
    public void modifiersInterfacesMethodStubs()
    {

        // new liferay portlet project without sample code and launch portlet wizard
        eclipse.getCreateLiferayProjectToolbar().click();

        CreateProjectWizardPO newLiferayProjectPage =
            new CreateProjectWizardPO( bot, TOOLTIP_MENU_ITEM_NEW_LIFERAY_PROJECT );

        newLiferayProjectPage.createSDKProject( "test", "Portlet", false, false );
        newLiferayProjectPage.finish();

        // check specfy modifier, interface and method stubs using GenericPortlet superclass
        eclipse.getCreateLiferayProjectToolbar().getNewLiferayPortlet().click();;

        CreateLiferayPortletWizardPO newPortletPage = new CreateLiferayPortletWizardPO( bot );

        newPortletPage.createLiferayPortlet( TEXT_BLANK, null, null, "javax.portlet.GenericPortlet" );

        newPortletPage.next();
        sleep( 500 );
        newPortletPage.next();

        LiferayPortletDeploymentDescriptorPO specifyLiferayPortletDeploymentDescriptorPage =
            new LiferayPortletDeploymentDescriptorPO( bot, INDEX_VALIDATION_MESSAGE7 );

        specifyLiferayPortletDeploymentDescriptorPage.specifyLiferayDisplay( null, true, null, null, true, null );

        newPortletPage.next();

        // check initial state
        ModifiersInterfacesMethodStubsPO modifiersInterfacesMethodStubsPage =
            new ModifiersInterfacesMethodStubsPO( bot, INDEX_VALIDATION_MESSAGE1 );
        assertEquals(
            TEXT_SPECIFY_STUBS_TO_GENERATE_IN_PORTLET_CLASS, modifiersInterfacesMethodStubsPage.getValidationMessage() );

        assertTrue( modifiersInterfacesMethodStubsPage.isPublicChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isPublicEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isAbstractChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isAbstractEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isFinalChecked() );
        assertTrue( modifiersInterfacesMethodStubsPage.isFinalEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isConstrcutFromSuperClassChecked() );
        assertTrue( modifiersInterfacesMethodStubsPage.isConstrcutFromSuperClassEnabled() );

        assertTrue( modifiersInterfacesMethodStubsPage.isInheritedAbstractMethodsChecked() );
        assertTrue( modifiersInterfacesMethodStubsPage.isInheritedAbstractMethodsEnabled() );

        assertTrue( modifiersInterfacesMethodStubsPage.isInitChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isInitEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isDestoryChecked() );
        assertTrue( modifiersInterfacesMethodStubsPage.isDestoryEnabled() );

        assertTrue( modifiersInterfacesMethodStubsPage.isDoViewChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isDoViewEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isDoEditChecked() );
        assertTrue( modifiersInterfacesMethodStubsPage.isDoEditEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isDoHelpChecked() );
        assertTrue( modifiersInterfacesMethodStubsPage.isDoHelpEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isDoAboutChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isDoAboutEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isDoConfigChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isDoConfigEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isDoEditDefaultsChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isDoEditDefaultsEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isDoEditGuestChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isDoEditGuestEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isDoPreviewChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isDoPreviewEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isDoPrintChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isDoPrintEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isProcessActionChecked() );
        assertTrue( modifiersInterfacesMethodStubsPage.isProcessActionEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isServeResourceChecked() );
        assertTrue( modifiersInterfacesMethodStubsPage.isServeResourceEnabled() );

        assertTrue( modifiersInterfacesMethodStubsPage.isButtonEnabled( BUTTON_ADD ) );
        assertFalse( modifiersInterfacesMethodStubsPage.isButtonEnabled( BUTTON_REMOVE ) );

        modifiersInterfacesMethodStubsPage.selectCheckbox( LABEL_FINAL );
        modifiersInterfacesMethodStubsPage.selectCheckbox( LABEL_SERVERESOURCE );

        modifiersInterfacesMethodStubsPage.clickButton( BUTTON_ADD );

        // click Add button to add interface and tests
        InterfaceSelectionPO selectInterfacePage =
            new InterfaceSelectionPO( bot, "Interface Selection", INDEX_VALIDATION_MESSAGE1 );

        selectInterfacePage.setItemToOpen( "acceptor" );
        selectInterfacePage.clickMatchItem( 0 );
        selectInterfacePage.confirm();

        modifiersInterfacesMethodStubsPage.selectInterface( 0 );
        modifiersInterfacesMethodStubsPage.clickButton( BUTTON_REMOVE );

        assertFalse( modifiersInterfacesMethodStubsPage.isButtonEnabled( BUTTON_REMOVE ) );

        modifiersInterfacesMethodStubsPage.clickButton( BUTTON_ADD );

        assertTrue( selectInterfacePage.isButtonEnabled( BUTTON_OK ) );
        selectInterfacePage.confirm();

        newPortletPage.finish();

        // check generate codes
        TextEditorPO newPortletJavaEditor = new TextEditorPO( bot, "NewPortlet.java" );

        assertContains(
            "public final class NewPortlet extends GenericPortlet implements Acceptor", newPortletJavaEditor.getText() );
        assertContains( "public void init()", newPortletJavaEditor.getText() );
        assertContains( "public void serveResource", newPortletJavaEditor.getText() );
        assertContains( "public void doView", newPortletJavaEditor.getText() );

        // new liferay portlet project using superclass MVCPortlet
        toolbarBot.menuClick( TOOLTIP_CREATE_LIFERAY_PROJECT, TOOLTIP_MENU_ITEM_NEW_LIFERAY_PORTLET );

        newPortletPage.createLiferayPortlet( null, "NewSecondPortlet", null, null );

        newPortletPage.next();
        newPortletPage.next();
        newPortletPage.next();

        // go to page and check state
        assertEquals(
            TEXT_SPECIFY_STUBS_TO_GENERATE_IN_PORTLET_CLASS, modifiersInterfacesMethodStubsPage.getValidationMessage() );

        assertTrue( modifiersInterfacesMethodStubsPage.isPublicChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isPublicEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isAbstractChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isAbstractEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isFinalChecked() );
        assertTrue( modifiersInterfacesMethodStubsPage.isFinalEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isConstrcutFromSuperClassChecked() );
        assertTrue( modifiersInterfacesMethodStubsPage.isConstrcutFromSuperClassEnabled() );

        assertTrue( modifiersInterfacesMethodStubsPage.isInheritedAbstractMethodsChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isInheritedAbstractMethodsEnabled() );

        assertTrue( modifiersInterfacesMethodStubsPage.isInitChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isInitEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isDestoryChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isDestoryEnabled() );

        assertTrue( modifiersInterfacesMethodStubsPage.isDoViewChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isDoViewEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isDoEditChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isDoEditEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isDoHelpChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isDoHelpEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isDoAboutChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isDoAboutEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isDoConfigChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isDoConfigEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isDoEditDefaultsChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isDoEditDefaultsEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isDoEditGuestChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isDoEditGuestEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isDoPreviewChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isDoPreviewEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isDoPrintChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isDoPrintEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isProcessActionChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isProcessActionEnabled() );

        assertFalse( modifiersInterfacesMethodStubsPage.isServeResourceChecked() );
        assertFalse( modifiersInterfacesMethodStubsPage.isServeResourceEnabled() );

        modifiersInterfacesMethodStubsPage.selectCheckbox( LABEL_CONSTRUCTORS_FROM_SUPERCLASS );

        newPortletPage.finish();

        // check generate codes in java file
        TextEditorPO newSecondPortletJavaEditor = new TextEditorPO( bot, "NewSecondPortlet.java" );

        assertContains( "public NewSecondPortlet()", newSecondPortletJavaEditor.getText() );

    }

    @Test
    public void noLiferayProjectsTest()
    {
        // click new liferay portlet wizard without projects
        toolbarBot.menuClick( TOOLTIP_CREATE_LIFERAY_PROJECT, TOOLTIP_MENU_ITEM_NEW_LIFERAY_PORTLET );

        DialogPO dialogPage1 = new DialogPO( bot, "New Liferay Portlet", BUTTON_NO, BUTTON_YES );

        dialogPage1.confirm();

        CreateProjectWizardPO newLiferayProjectPage1 =
            new CreateProjectWizardPO( bot, TOOLTIP_MENU_ITEM_NEW_LIFERAY_PROJECT, INDEX_VALIDATION_MESSAGE3 );

        assertEquals( TEXT_ENTER_PROJECT_NAME, newLiferayProjectPage1.getValidationMessage() );
        assertFalse( buttonBot.isEnabled( ProjectWizard.BUTTON_NEXT ) );

        newLiferayProjectPage1.createSDKPortletProject( "test" );
        assertTrue( buttonBot.isEnabled( ProjectWizard.BUTTON_NEXT ) );

        newLiferayProjectPage1.cancel();

        DialogPO dialogPage2 = new DialogPO( bot, "New Liferay Portlet", BUTTON_YES, BUTTON_NO );

        dialogPage2.confirm();

        CreateLiferayPortletWizardPO newPortletPage1 =
            new CreateLiferayPortletWizardPO( bot, TOOLTIP_NEW_LIFERAY_PORTLET, INDEX_VALIDATION_MESSAGE4 );

        newPortletPage1.waitForPageToOpen();
        assertEquals( TEXT_ENTER_A_PROJECT_NAME, newPortletPage1.getValidationMessage() );

        newPortletPage1.cancel();

        // new Java project
        toolbarBot.menuClick( MENU_FILE, MENU_NEW, MENU_PROJECT );
        createProject(
            "Java", "Java", LABEL_JAVA_PROJECT, "JavaExample", TEXT_CREATE_A_JAVA_PROJECT,
            TEXT_CREATE_A_JAVA_PROJECT_IN_WORKSPACE );

        // new general project
        toolbarBot.menuClick( MENU_FILE, MENU_NEW, MENU_PROJECT );
        createProject(
            "project", "General", "Project", "GeneralExample", TEXT_CREATE_A_NEW_PROJECT_RESOURCE,
            TEXT_CREATE_A_NEW_PROJECT_RESOURCE + '.' );

        toolbarBot.menuClick( TOOLTIP_NEW, TOOLTIP_MENU_ITEM_LIFERAY_PORTLET );

        DialogPO dialogPage3 = new DialogPO( bot, "New Liferay Portlet", BUTTON_YES, BUTTON_NO );

        dialogPage3.confirm();

        CreateLiferayPortletWizardPO newPortletPage2 =
            new CreateLiferayPortletWizardPO( bot, TOOLTIP_NEW_LIFERAY_PORTLET, INDEX_VALIDATION_MESSAGE4 );

        newPortletPage2.waitForPageToOpen();
        newPortletPage2.cancel();

        toolbarBot.menuClick( MENU_FILE, TOOLTIP_NEW, TOOLTIP_MENU_ITEM_LIFERAY_PORTLET );

        DialogPO dialogPage4 = new DialogPO( bot, "New Liferay Portlet", BUTTON_NO, BUTTON_YES );

        dialogPage4.confirm();

        CreateProjectWizardPO newLiferayProjectPage2 =
            new CreateProjectWizardPO( bot, TOOLTIP_MENU_ITEM_NEW_LIFERAY_PROJECT );

        newLiferayProjectPage2.createSDKPortletProject( "test" );
        newLiferayProjectPage2.finish();
        sleep();
        // assertTrue( UITestsUtils.checkConsoleMessage( "BUILD SUCCESSFUL", "Java" ) );

        CreateLiferayPortletWizardPO newPortletPage3 =
            new CreateLiferayPortletWizardPO( bot, TOOLTIP_NEW_LIFERAY_PORTLET, INDEX_VALIDATION_MESSAGE4 );

        newPortletPage3.waitForPageToOpen();
        assertEquals( TEXT_CREATE_A_PORTLET_CLASS, newPortletPage3.getValidationMessage() );
        assertEquals( "test-portlet", newPortletPage3.getPortletPluginProject() );
        assertTrue( newPortletPage3.isRadioSelected( RADIO_CREATE_NEW_PORTLET ) );
        assertEquals( "com.liferay.util.bridges.mvc.MVCPortlet", newPortletPage3.getSuperClassCombobox() );

        newPortletPage3.finish();

        ProjectWizardTests.deleteProjectInSdk( "test-portlet" );
    }

    @Test
    public void portletDeploymentDescriptorTest()
    {
        eclipse.getCreateLiferayProjectToolbar().getNewLiferayPluginProject().click();

        CreateProjectWizardPO newLiferayProjectPage =
            new CreateProjectWizardPO( bot, TOOLTIP_MENU_ITEM_NEW_LIFERAY_PROJECT );

        // create portlet project without sample and launch portlet wizard
        newLiferayProjectPage.createSDKProject( "test", "Portlet", false, false );
        newLiferayProjectPage.finish();

        // relate ticket IDE-2156, regression for IDE-119
        TreePO projectTree = eclipse.showPackageExporerView().getProjectTree();

        projectTree.expandNode( "test-portlet", "docroot", "WEB-INF" );

        eclipse.showPackageExporerView().deleteResouceByName( "liferay-display.xml" );

        // new liferay portlet wizard
        eclipse.getCreateLiferayProjectToolbar().menuClick( TOOLTIP_MENU_ITEM_NEW_LIFERAY_PROJECT );

        CreateLiferayPortletWizardPO newPortletPage = new CreateLiferayPortletWizardPO( bot );
        newPortletPage.next();

        PortletDeploymentDescriptorPO specifyPortletDeploymentDescriptorPage =
            new PortletDeploymentDescriptorPO( bot, INDEX_VALIDATION_MESSAGE6 );

        // initial state check
        assertEquals(
            TEXT_SPECIFY_PORTLET_DEPLOYMENT_DESCRIPTOR_DETAILS,
            specifyPortletDeploymentDescriptorPage.getValidationMessage() );
        assertEquals( "new", specifyPortletDeploymentDescriptorPage.getPortletName() );
        assertEquals( "New", specifyPortletDeploymentDescriptorPage.getDisplayName() );
        assertEquals( "New", specifyPortletDeploymentDescriptorPage.getPortletTitle() );
        assertTrue( specifyPortletDeploymentDescriptorPage.isViewPortletModeChecked() );
        assertTrue( specifyPortletDeploymentDescriptorPage.isCreateJspFilesChecked() );
        assertEquals( "/html/new", specifyPortletDeploymentDescriptorPage.getJspFolder() );
        assertFalse( specifyPortletDeploymentDescriptorPage.isCreateResourceBundleFileChecked() );
        assertFalse( specifyPortletDeploymentDescriptorPage.isBundleFilePathEnabled() );
        assertEquals( "content/Language.properties", specifyPortletDeploymentDescriptorPage.getResourceBundleFilePath() );

        newPortletPage.finish();

        // IDE-2156 treeItemPage.isVisible();

        // check generate codes and files
        EditorPO newPortletJavaPage = new EditorPO( bot, "NewPortlet.java" );
        assertTrue( newPortletJavaPage.isActive() );
        projectTree.expandNode( "test-portlet", "docroot", "html", "new" );

        String fileName = "view.jsp";
        assertTrue( projectTree.getTreeItem( fileName ).isVisible() );

        projectTree.expandNode( "test-portlet", "docroot", "WEB-INF" );
        
        fileName = "portlet.xml";

        projectTree.getTreeItem( fileName ).doubleClick();

        TextEditorPO portletXmlPage = eclipse.getTextEditor( "portlet.xml" );

        assertContains( "<portlet-name>new</portlet-name>", portletXmlPage.getText() );
        assertContains( "<display-name>New</display-name>", portletXmlPage.getText() );
        assertContains( "<title>New</title>", portletXmlPage.getText() );

        // new liferay portlet wizard with default MVCPortlet
        eclipse.getCreateLiferayProjectToolbar().getNewLiferayPortlet().click();

        newPortletPage.createLiferayPortlet( true );
        newPortletPage.next();

        assertFalse( specifyPortletDeploymentDescriptorPage.isButtonEnabled( BUTTON_FINISH ) );
        assertEquals( TEXT_PORTLET_NAME_ALREADY_EXISTS, specifyPortletDeploymentDescriptorPage.getValidationMessage() );

        specifyPortletDeploymentDescriptorPage.setPortletName( "New" );
        assertEquals(
            TEXT_VIEW_JSP_EXSITS_AND_OVERWRITTEN, specifyPortletDeploymentDescriptorPage.getValidationMessage() );
        assertTrue( specifyPortletDeploymentDescriptorPage.isButtonEnabled( BUTTON_FINISH ) );

        specifyPortletDeploymentDescriptorPage.specifyResources( false, null, true, null );
        assertEquals(
            TEXT_SPECIFY_PORTLET_DEPLOYMENT_DESCRIPTOR_DETAILS,
            specifyPortletDeploymentDescriptorPage.getValidationMessage() );
        assertTrue( specifyPortletDeploymentDescriptorPage.isBundleFilePathEnabled() );

        newPortletPage.finish();

        assertContains( "<portlet-name>New</portlet-name>", portletXmlPage.getText() );
        assertContains( "<resource-bundle>content.Language</resource-bundle>", portletXmlPage.getText() );

        fileName = "Language.properties";
        projectTree.expandNode( "test-portlet", "docroot/WEB-INF/src", "content" );
        assertTrue(projectTree.getTreeItem( "Language.properties" ).isVisible());

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayPortlet().click();

        // new portlet with more than two uppercase portlet class name
        newPortletPage.createLiferayPortlet( TEXT_BLANK, "MyNewPortlet", null, null );
        newPortletPage.next();

        assertEquals( "my-new", specifyPortletDeploymentDescriptorPage.getPortletName() );
        assertEquals( "My New", specifyPortletDeploymentDescriptorPage.getDisplayName() );
        assertEquals( "My New", specifyPortletDeploymentDescriptorPage.getPortletTitle() );
        assertEquals( "/html/mynew", specifyPortletDeploymentDescriptorPage.getJspFolder() );

        specifyPortletDeploymentDescriptorPage.setPortletName( "mynew" );
        assertEquals( "Mynew", specifyPortletDeploymentDescriptorPage.getDisplayName() );
        assertEquals( "Mynew", specifyPortletDeploymentDescriptorPage.getPortletTitle() );
        assertEquals( "/html/mynew", specifyPortletDeploymentDescriptorPage.getJspFolder() );

        newPortletPage.back();

        // check and validate portlet class, dispaly name, title and jsp folder in wizard
        newPortletPage.createLiferayPortlet( TEXT_BLANK, "MyTestPortlet", null, null );
        newPortletPage.next();

        assertEquals( "mynew", specifyPortletDeploymentDescriptorPage.getPortletName() );
        assertEquals( "Mynew", specifyPortletDeploymentDescriptorPage.getDisplayName() );
        assertEquals( "Mynew", specifyPortletDeploymentDescriptorPage.getPortletTitle() );
        assertEquals( "/html/mytest", specifyPortletDeploymentDescriptorPage.getJspFolder() );

        specifyPortletDeploymentDescriptorPage.setDisplayName( "Mynew1" );
        assertEquals( "Mynew", specifyPortletDeploymentDescriptorPage.getPortletTitle() );

        specifyPortletDeploymentDescriptorPage.setPortletName( TEXT_BLANK );

        assertEquals( TEXT_PORTLET_NAME_IS_EMPTY, specifyPortletDeploymentDescriptorPage.getValidationMessage() );
        assertEquals( "Mynew1", specifyPortletDeploymentDescriptorPage.getDisplayName() );
        assertEquals( TEXT_BLANK, specifyPortletDeploymentDescriptorPage.getPortletTitle() );

        specifyPortletDeploymentDescriptorPage.speficyPortletInfo( "my-new", "Mynew1", TEXT_BLANK );

        specifyPortletDeploymentDescriptorPage.setJspFolder( TEXT_BLANK );
        assertEquals( TEXT_JSP_FOLDER_CANNOT_EMPTY, specifyPortletDeploymentDescriptorPage.getValidationMessage() );
        specifyPortletDeploymentDescriptorPage.setJspFolder( "test." );
        assertEquals( TEXT_FOLDER_VALUE_IS_INVALID, specifyPortletDeploymentDescriptorPage.getValidationMessage() );

        // relate ticket IDE-2158
        // specifyPortletDeploymentDescriptorPage.setJspFolder( "." );
        // assertEquals( TEXT_VIEW_JSP_EXSITS_AND_OVERWRITTEN,
        // specifyPortletDeploymentDescriptorPage.getValidationMessage() );
        // specifyPortletDeploymentDescriptorPage.setJspFolder( ".." );
        // specifyPortletDeploymentDescriptorPage.back

        specifyPortletDeploymentDescriptorPage.specifyResources( true, "/myhtml/myjspfolder", true, TEXT_BLANK );
        assertEquals(
            TEXT_RESOURCE_BUNDLE_FILE_MUST_VALID_PATH, specifyPortletDeploymentDescriptorPage.getValidationMessage() );

        specifyPortletDeploymentDescriptorPage.setResourceBundleFilePath( "content/Language.properties1" );
        assertEquals(
            TEXT_RESOURCE_BUNDLE_FILE_END_WITH_PROPERTIES,
            specifyPortletDeploymentDescriptorPage.getValidationMessage() );

        // relate ticket IDE-2159
        // specifyPortletDeploymentDescriptorPage.setResourceBundleFilePath( ".properties" );

        specifyPortletDeploymentDescriptorPage.setResourceBundleFilePath( "mycontent/Lang.properties" );
        assertEquals(
            TEXT_SPECIFY_PORTLET_DEPLOYMENT_DESCRIPTOR_DETAILS,
            specifyPortletDeploymentDescriptorPage.getValidationMessage() );

        specifyPortletDeploymentDescriptorPage.speficyPortletModes( true, true );
        specifyPortletDeploymentDescriptorPage.speficyLiferayPortletModes( true, true, true, true, true, true );

        newPortletPage.finish();

        // check codes generate in portlet.xml file
        assertContains( "<portlet-name>my-new</portlet-name>", portletXmlPage.getText() );
        assertContains( "<display-name>Mynew1</display-name>", portletXmlPage.getText() );
        assertContains( "<value>/myhtml/myjspfolder/view.jsp</value>", portletXmlPage.getText() );
        assertContains( "<value>/myhtml/myjspfolder/edit.jsp</value>", portletXmlPage.getText() );
        assertContains( "<value>/myhtml/myjspfolder/help.jsp</value>", portletXmlPage.getText() );
        assertContains( "<value>/myhtml/myjspfolder/about.jsp</value>", portletXmlPage.getText() );
        assertContains( "<value>/myhtml/myjspfolder/config.jsp</value>", portletXmlPage.getText() );
        assertContains( "<value>/myhtml/myjspfolder/edit-defaults.jsp</value>", portletXmlPage.getText() );
        assertContains( "<value>/myhtml/myjspfolder/edit-guest.jsp</value>", portletXmlPage.getText() );
        assertContains( "<value>/myhtml/myjspfolder/preview.jsp</value>", portletXmlPage.getText() );
        assertContains( "<value>/myhtml/myjspfolder/print.jsp</value>", portletXmlPage.getText() );

        assertContains( "<portlet-mode>view</portlet-mode>", portletXmlPage.getText() );
        assertContains( "<portlet-mode>edit</portlet-mode>", portletXmlPage.getText() );
        assertContains( "<portlet-mode>help</portlet-mode>", portletXmlPage.getText() );
        assertContains( "<portlet-mode>about</portlet-mode>", portletXmlPage.getText() );
        assertContains( "<portlet-mode>config</portlet-mode>", portletXmlPage.getText() );
        assertContains( "<portlet-mode>edit_defaults</portlet-mode>", portletXmlPage.getText() );
        assertContains( "<portlet-mode>edit_guest</portlet-mode>", portletXmlPage.getText() );
        assertContains( "<portlet-mode>preview</portlet-mode>", portletXmlPage.getText() );
        assertContains( "<portlet-mode>print</portlet-mode>", portletXmlPage.getText() );

        assertContains( "<resource-bundle>mycontent.Lang</resource-bundle>", portletXmlPage.getText() );
        assertContains( "<title></title>", portletXmlPage.getText() );

        // check language file

        fileName = "Lang.properties";
        projectTree.expandNode( "test-portlet", "docroot/WEB-INF/src", "mycontent" );
        assertTrue(projectTree.getTreeItem( fileName ).isVisible());

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayPortlet().click();

        newPortletPage.createLiferayPortlet( TEXT_BLANK, "MyPortletPortlet", null, "javax.portlet.GenericPortlet" );
        newPortletPage.next();

        assertEquals( "my-portlet", specifyPortletDeploymentDescriptorPage.getPortletName() );
        assertEquals( "My Portlet", specifyPortletDeploymentDescriptorPage.getDisplayName() );
        assertEquals( "My Portlet", specifyPortletDeploymentDescriptorPage.getPortletTitle() );

        assertFalse( specifyPortletDeploymentDescriptorPage.getAboutCheckBox().isEnabled() );
        assertFalse( specifyPortletDeploymentDescriptorPage.getConfigCheckBox().isEnabled() );
        assertFalse( specifyPortletDeploymentDescriptorPage.getEditDefaultsCheckBox().isEnabled() );
        assertFalse( specifyPortletDeploymentDescriptorPage.getEditGuestCheckBox().isEnabled() );
        assertFalse( specifyPortletDeploymentDescriptorPage.getPreviewCheckBox().isEnabled() );
        assertFalse( specifyPortletDeploymentDescriptorPage.getPrintCheckBox().isEnabled() );

        newPortletPage.finish();

        assertContains(
            "<init-param>\n\t\t\t<name>view-template</name>\n\t\t\t<value>/html/myportlet/view.jsp</value>\n\t\t</init-param>\n\t\t<expiration-cache>0</expiration-cache>\n\t\t<supports>\n\t\t\t<mime-type>text/html</mime-type>\n\t\t\t<portlet-mode>view</portlet-mode>\n\t\t</supports>\n\t\t<portlet-info>\n\t\t\t<title>My Portlet</title>\n\t\t\t<short-title>My Portlet</short-title>\n\t\t\t<keywords></keywords>\n\t\t</portlet-info>",
            portletXmlPage.getText() );
    }

    @After
    public void waitForCreate()
    {
        sleep( 5000 );
    }

}

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

package com.liferay.ide.hook.ui.tests;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.liferay.ide.hook.ui.tests.page.AddEventActionPO;
import com.liferay.ide.hook.ui.tests.page.AddJSPFilePathPO;
import com.liferay.ide.hook.ui.tests.page.AddLanguagePropertyPO;
import com.liferay.ide.hook.ui.tests.page.AddPortalPropertiesOverridePO;
import com.liferay.ide.hook.ui.tests.page.AddServiceWarningPO;
import com.liferay.ide.hook.ui.tests.page.AddServiceWrapperPO;
import com.liferay.ide.hook.ui.tests.page.ContainerSelectionPO;
import com.liferay.ide.hook.ui.tests.page.CreateCustomJSPsPO;
import com.liferay.ide.hook.ui.tests.page.EventSelectionPO;
import com.liferay.ide.hook.ui.tests.page.HookTypesToCreatePO;
import com.liferay.ide.hook.ui.tests.page.LanguagePropertiesPO;
import com.liferay.ide.hook.ui.tests.page.LiferayCustomJSPPO;
import com.liferay.ide.hook.ui.tests.page.NewClassPO;
import com.liferay.ide.hook.ui.tests.page.NewImplClassPO;
import com.liferay.ide.hook.ui.tests.page.PortalPropertiesFilePO;
import com.liferay.ide.hook.ui.tests.page.PortalPropertiesPO;
import com.liferay.ide.hook.ui.tests.page.PropertySelectionPO;
import com.liferay.ide.hook.ui.tests.page.ServicesPO;
import com.liferay.ide.hook.ui.tests.page.SuperclassSelectionPO;
import com.liferay.ide.project.ui.tests.page.CreateProjectWizardPO;
import com.liferay.ide.project.ui.tests.page.SetSDKLocationPO;
import com.liferay.ide.ui.tests.SWTBotBase;

/**
 * @author Vicky Wang
 */
public class HookConfigurationWizardTests extends SWTBotBase implements HookConfigurationWizard
{

    HookTypesToCreatePO newHookTypesPage = new HookTypesToCreatePO( bot, "New Liferay Hook Configuration" );

    String projectHookName = "hook-configuration-wizard";

    @AfterClass
    public static void cleanAll()
    {
        eclipse.getPackageExporerView().deleteProjectExcludeNames( new String[] { getLiferayPluginsSdkName() } );
    }

    private SetSDKLocationPO getSetSDKLoactionPage()
    {
        SetSDKLocationPO page = new SetSDKLocationPO( bot );
        page.setSdkLocation( getLiferayPluginsSdkDir().toString() );

        return page;
    }

    @Test
    public void hookConfigurationAllHookTypes()
    {
        CreateCustomJSPsPO customJSPpage = new CreateCustomJSPsPO( bot, INDEX_CUSTOM_JSPS_VALIDATION_MESSAGE );

        eclipse.getCreateLiferayProjectToolbar().menuClick( TOOLTIP_MENU_ITEM_NEW_LIFERAY_HOOK_CONFIGURATION );

        newHookTypesPage.getCustomJSPs().select();
        newHookTypesPage.getPortalProperties().select();
        newHookTypesPage.getServices().select();
        newHookTypesPage.getLanguageProperties().select();

        newHookTypesPage.next();
        //sleep( 1000 );

        // Custom JSPs
        AddJSPFilePathPO jspFile = new AddJSPFilePathPO( bot );

        customJSPpage.getAddButton().click();
        jspFile.setJSPFilePathText( "CustomJsps.jsp" );
        jspFile.confirm();
        customJSPpage.next();
        //sleep( 500 );

        // Portal Properties
        PortalPropertiesPO portalPropertiesPage =
            new PortalPropertiesPO( bot, INDEX_PORTAL_PROPERTIES_VALIDATION_MESSAGE );

        AddEventActionPO eventActionPage = new AddEventActionPO( bot );

        portalPropertiesPage.getEventAddButton().click();
        eventActionPage.setEvent( "portalProperties" );
        eventActionPage.setEventActionclass( "portalPropertiesClass" );
        eventActionPage.confirm();

        portalPropertiesPage.next();
        //sleep( 500 );

        // Service
        ServicesPO servicesPage = new ServicesPO( bot, "", INDEX_SERVICES_MESSAGE );

        AddServiceWrapperPO serviceWrapperPage = new AddServiceWrapperPO( bot );

        servicesPage.getAddButton().click();
        serviceWrapperPage.setServiceTypeText( "com.liferay.portal.service.AddressService" );
        serviceWrapperPage.setImplClassText( "com.liferay.portal.service.AddressServiceWrapper" );
        serviceWrapperPage.confirm();

        servicesPage.next();
        //sleep( 500 );

        // Language Properties
        LanguagePropertiesPO languagePropertiesPage =
            new LanguagePropertiesPO( bot, INDEX_LANGUAGE_PROPERTIES_VALIDATION_MESSAGE );

        AddLanguagePropertyPO languageProperty = new AddLanguagePropertyPO( bot );

        languagePropertiesPage.getAddButton().click();
        languageProperty.setLanguagePropertyFileText( "languageTest.properties" );
        languageProperty.confirm();
        languagePropertiesPage.finish();
        //sleep( 1000 );

        // check files
        // treeBot.doubleClick( "CustomJsps.jsp", projectHookName + "-hook", "docroot", "META-INF", "custom_jsps" );
        // treeBot.doubleClick( "portal.properties", projectHookName + "-hook", "docroot/WEB-INF/src" );
        // TextEditorPO textEditorPage = new TextEditorPO( bot, "portal.properties" );
        // assertContains( "portalProperties=portalPropertiesClass", textEditorPage.getText() );
        // treeBot.doubleClick( "languageTest.properties", projectHookName + "-hook", "docroot/WEB-INF/src", "content"
        // );
    }

    @Test
    public void hookConfigurationCustomJSPs()
    {
        String defaultMessage = "Create customs JSP folder and select JSPs to override.";
        String errorMessage = " Custom JSPs folder not configured.";

        CreateCustomJSPsPO customJSPpage = new CreateCustomJSPsPO( bot, INDEX_CUSTOM_JSPS_VALIDATION_MESSAGE );

        eclipse.getCreateLiferayProjectToolbar().menuClick( TOOLTIP_MENU_ITEM_NEW_LIFERAY_HOOK_CONFIGURATION );

        newHookTypesPage.getCustomJSPs().select();
        newHookTypesPage.next();

        // Custom JSPs page
        assertEquals( defaultMessage, customJSPpage.getValidationMessage() );
        assertEquals( "hook-configuration-wizard-hook", customJSPpage.getSelectedProject().getText() );
        assertEquals( "docroot", customJSPpage.getWebRootFolder().getText() );
        assertEquals( "/META-INF/custom_jsps", customJSPpage.getCustomJSPfolder().getText() );

        customJSPpage.setCustomJSPfolder( "" );

        assertEquals( errorMessage, customJSPpage.getValidationMessage() );

        customJSPpage.getBrowseButton().click();

        ContainerSelectionPO chooseFolder = new ContainerSelectionPO( bot );

        chooseFolder.select( "hook-configuration-wizard-hook", "docroot", "META-INF" );
        chooseFolder.confirm();

        customJSPpage.setCustomJSPfolder( "/META-INF/custom_jsps" );

        // JSP files to override
        LiferayCustomJSPPO chooseCustomJSP = new LiferayCustomJSPPO( bot );

        customJSPpage.getAddFromLiferayButton().click();
        chooseCustomJSP.select( "html", "common", "themes", "body_bottom.jsp" );
        chooseCustomJSP.confirm();

        AddJSPFilePathPO jspFile = new AddJSPFilePathPO( bot );
        customJSPpage.getAddButton().click();
        jspFile.setJSPFilePathText( "test.jsp" );
        jspFile.confirm();

        customJSPpage.getJspFilesToOverride().click( 1, 0 );
        customJSPpage.getEditButton().click();

        jspFile.setJSPFilePathText( "hooktest.jsp" );
        jspFile.confirm();

        customJSPpage.getJspFilesToOverride().click( 1, 0 );
        customJSPpage.getRemoveButton().click();

        customJSPpage.finish();
        ////sleep( 1000 );

        // treeBot.doubleClick(
        // "body_bottom.jsp", projectHookName + "-hook", "docroot", "META-INF", "custom_jsps", "html", "common",
        // "themes" );
    }

    @Test
    public void hookConfigurationLanguageProperties()
    {
        String defaultMessage = "Create new Language properties files.";
        String errorMessage = " Content folder not configured.";

        eclipse.getCreateLiferayProjectToolbar().menuClick( TOOLTIP_MENU_ITEM_NEW_LIFERAY_HOOK_CONFIGURATION );

        newHookTypesPage.getLanguageProperties().select();

        newHookTypesPage.next();

        //sleep( 1000 );

        LanguagePropertiesPO languagePropertiesPage =
            new LanguagePropertiesPO(
                bot, "New Liferay Hook Configuration", INDEX_LANGUAGE_PROPERTIES_VALIDATION_MESSAGE );

        assertEquals( defaultMessage, languagePropertiesPage.getValidationMessage() );
        assertEquals(
            "/hook-configuration-wizard-hook/docroot/WEB-INF/src/content",
            languagePropertiesPage.getContentFolderText() );

        languagePropertiesPage.setContentFolderText( "" );
        //sleep( 500 );

        assertEquals( errorMessage, languagePropertiesPage.getValidationMessage() );

        languagePropertiesPage.getBrowseButton().click();
        //sleep( 500 );
        ContainerSelectionPO chooseFolder = new ContainerSelectionPO( bot );

        chooseFolder.select( "hook-configuration-wizard-hook", "docroot", "WEB-INF", "src" );

        chooseFolder.confirm();

        //sleep( 500 );

        // Language property files
        languagePropertiesPage.getAddButton().click();

        AddLanguagePropertyPO languageProperty = new AddLanguagePropertyPO( bot );
        languageProperty.setLanguagePropertyFileText( "test.properties" );
        languageProperty.confirm();

        languagePropertiesPage.getAddButton().click();

        languageProperty.setLanguagePropertyFileText( "test-hook" );
        languageProperty.confirm();

        languagePropertiesPage.setFocus();
        languagePropertiesPage.clickLanguagePropertyFiles( 1 );

        languagePropertiesPage.getEditButton().click();

        languageProperty.setLanguagePropertyFileText( "hook" );
        languageProperty.confirm();

        languagePropertiesPage.setFocus();
        languagePropertiesPage.clickLanguagePropertyFiles( 1 );

        languagePropertiesPage.getRemoveButton().click();

        languagePropertiesPage.finish();

        // check language properties file exist in the project
        // treeBot.doubleClick( "test.properties", projectHookName + "-hook", "docroot/WEB-INF/src", "content" );
        //sleep( 1000 );
    }

    @Test
    public void hookConfigurationPortalProperties()
    {
        String defaultMessage = "Specify which portal properties to override.";
        String errorMessage = " portal.properties file not configured.";

        PortalPropertiesPO portalPropertiesPage =
            new PortalPropertiesPO( bot, INDEX_PORTAL_PROPERTIES_VALIDATION_MESSAGE );

        eclipse.getCreateLiferayProjectToolbar().menuClick( TOOLTIP_MENU_ITEM_NEW_LIFERAY_HOOK_CONFIGURATION );

        newHookTypesPage.getPortalProperties().select();
        newHookTypesPage.next();
        //sleep( 1000 );

        assertEquals( defaultMessage, portalPropertiesPage.getValidationMessage() );
        assertEquals(
            "/hook-configuration-wizard-hook/docroot/WEB-INF/src/portal.properties",
            portalPropertiesPage.getPortalPropertiesFile().getText() );
        portalPropertiesPage.setPortalPropertiesFile( "" );
        //sleep( 500 );

        assertEquals( errorMessage, portalPropertiesPage.getValidationMessage() );
        portalPropertiesPage.getBrowseButton().click();
        //sleep( 500 );

        PortalPropertiesFilePO propertiesPage = new PortalPropertiesFilePO( bot );

        propertiesPage.select( "hook-configuration-wizard-hook", "docroot", "WEB-INF", "src" );
        propertiesPage.confirm();

        // Define actions to be executed on portal events
        AddEventActionPO eventActionPage = new AddEventActionPO( bot );

        portalPropertiesPage.getEventAddButton().click();
        eventActionPage.getNewButton().click();
        //sleep( 500 );

        NewClassPO newClassPage = new NewClassPO( bot );

        newClassPage.setClassName( "test" );
        newClassPage.setJavaPackage( "hook" );
        newClassPage.getCreateButton().click();
        //sleep( 500 );

        PropertySelectionPO propertySelectionPage = new PropertySelectionPO( bot );

        eventActionPage.getSelectEventButton().click();
        propertySelectionPage.select( "application.startup.events" );
        propertySelectionPage.confirm();
        //sleep( 500 );

        portalPropertiesPage.getAddEventAction().setFocus();
        eventActionPage.confirm();
        //sleep( 500 );

        portalPropertiesPage.getEventAddButton().click();
        eventActionPage.getSelectEventButton().click();
        propertySelectionPage.select( "application.startup.events" );
        propertySelectionPage.confirm();
        //sleep( 500 );
        portalPropertiesPage.getAddEventAction().setFocus();

        EventSelectionPO eventSelectionPage = new EventSelectionPO( bot );
        eventActionPage.getSelectClass().click();
        //sleep( 500 );

        eventSelectionPage.setEventAction( "ObjectAction" );
        sleep( 5000 );
        eventSelectionPage.confirm();

        eventActionPage.confirm();
        //sleep( 5000 );

        portalPropertiesPage.getDefineActionsOnPortalEvents().click( 1, 1 );
        portalPropertiesPage.getEventRemoveButton().click();

        portalPropertiesPage.getDefineActionsOnPortalEvents().click( 0, 1 );
        portalPropertiesPage.getEventEditButton().click();

        eventActionPage.setEventActionclass( "test_hook" );
        eventActionPage.confirm();

        // Specify properties to override
        AddPortalPropertiesOverridePO propertyOverridePage = new AddPortalPropertiesOverridePO( bot );

        portalPropertiesPage.getPropertyAddButton().click();
        //sleep( 500 );
        propertyOverridePage.getSelectProperty().click();
        propertySelectionPage.select( "admin.default.group.names" );

        propertySelectionPage.confirm();
        //sleep( 1000 );
        portalPropertiesPage.getAddPropertyOverride().setFocus();
        propertyOverridePage.setValue( "1" );
        propertyOverridePage.confirm();

        portalPropertiesPage.getPropertyAddButton().click();
        propertyOverridePage.setProperty( "test" );
        propertyOverridePage.setValue( "2" );
        propertyOverridePage.confirm();
        //sleep( 500 );

        portalPropertiesPage.getNewLiferayHookConfiguration().setFocus();
        portalPropertiesPage.getSpecifyPropertiesToOverride().click( 1, 1 );
        portalPropertiesPage.getPropertyEditButton().click();

        propertyOverridePage.setProperty( "test_hook" );
        propertyOverridePage.setValue( "3" );
        propertyOverridePage.confirm();

        portalPropertiesPage.getNewLiferayHookConfiguration().setFocus();
        portalPropertiesPage.getSpecifyPropertiesToOverride().click( 1, 1 );

        portalPropertiesPage.getPropertyRemoveButton().click();
        portalPropertiesPage.finish();
        //sleep( 1000 );

        // check files exist in the project
        // treeBot.doubleClick( "portal.properties", projectHookName + "-hook", "docroot/WEB-INF/src" );
        // EditorPO editorPage = new EditorPO( bot, "portal.properties" );
        // assertTrue( editorPage.isActive() );
        // TextEditorPO textEditorPage = new TextEditorPO( bot, "portal.properties" );
        // assertContains( "application.startup.events=test_hook", textEditorPage.getText() );
        // assertContains( "admin.default.group.names=1", textEditorPage.getText() );
        // //sleep( 500 );
        //
        // // treeBot.doubleClick( "test.java", projectHookName + "-hook", "docroot/WEB-INF/src", "hook" );
        // EditorPO editorPagejava = new EditorPO( bot, "test.java" );
        // assertTrue( editorPagejava.isActive() );
        //
        // TextEditorPO textEditorPagejava = new TextEditorPO( bot, "test.java" );
        // assertContains( "SimpleAction", textEditorPagejava.getText() );
    }

    @Test
    public void hookConfigurationServices()
    {
        String defaultMessage = "Specify which Liferay services to extend.";
        String errorMessage = " Need to specify at least one Service to override.";

        ServicesPO servicesPage = new ServicesPO( bot, INDEX_SERVICES_MESSAGE );

        eclipse.getCreateLiferayProjectToolbar().menuClick( TOOLTIP_MENU_ITEM_NEW_LIFERAY_HOOK_CONFIGURATION );

        newHookTypesPage.getServices().select();

        newHookTypesPage.next();

        //sleep( 1000 );

        assertEquals( defaultMessage, servicesPage.getValidationMessage() );

        servicesPage.getAddButton().click();
        //sleep( 500 );

        AddServiceWrapperPO serviceWrapperPage = new AddServiceWrapperPO( bot );

        AddServiceWarningPO serviceWarningPage = new AddServiceWarningPO( bot );

        serviceWrapperPage.getNewButton().click();
        servicesPage.getAddService().setFocus();
        serviceWarningPage.getOkButton().click();
        //sleep( 500 );

        servicesPage.getAddServiceWrapper().setFocus();
        serviceWrapperPage.getSelectImplClass( 1 ).click();
        serviceWarningPage.getOkButton().click();

        servicesPage.getAddServiceWrapper().setFocus();
        serviceWrapperPage.setServiceTypeText( "test" );
        serviceWrapperPage.setImplClassText( "test" );

        servicesPage.getAddServiceWrapper().setFocus();
        serviceWrapperPage.confirm();
        servicesPage.getDefinePortalServices().click( 0, 1 );
        servicesPage.getRemoveButton().click();
        //sleep( 500 );
        assertEquals( errorMessage, servicesPage.getValidationMessage() );

        servicesPage.getAddButton().click();
        serviceWrapperPage.getSelectServiceType().click();
        //sleep( 500 );

        SuperclassSelectionPO superclassPage = new SuperclassSelectionPO( bot );

        NewImplClassPO newImplClassPage = new NewImplClassPO( bot );

        superclassPage.setSuperclass( "AccountService" );
        superclassPage.confirm();
        servicesPage.getAddServiceWrapper().setFocus();
        serviceWrapperPage.getNewButton().click();
        newImplClassPage.getJavaPackage().setText( "hookservice" );
        newImplClassPage.getCreateButton().click();

        servicesPage.getAddServiceWrapper().setFocus();
        serviceWrapperPage.confirm();
        servicesPage.getDefinePortalServices().click( 0, 1 );
        servicesPage.getEditButton().click();

        serviceWrapperPage.getImplClass().setText( "hookservice.ExtAccountService" );
        serviceWrapperPage.confirm();
        servicesPage.finish();
        //sleep( 1000 );

        // check file exist in the project
        // treeBot.doubleClick( "ExtAccountService.java", projectHookName + "-hook", "docroot/WEB-INF/src",
        // "hookservice" );
        // EditorPO editorPagejava = new EditorPO( bot, "ExtAccountService.java" );
        // assertTrue( editorPagejava.isActive() );
        // TextEditorPO textEditorPagejava = new TextEditorPO( bot, "ExtAccountService.java" );
        // assertContains( "AccountServiceWrapper", textEditorPagejava.getText() );
    }

    @Before
    public void openWizardCreateProject()
    {
        hasAddedProject = addedProjects();

        if( hasAddedProject )
        {
            return;
        }

        eclipse.getCreateLiferayProjectToolbar().menuClick( TOOLTIP_MENU_ITEM_NEW_LIFERAY_PROJECT );

        CreateProjectWizardPO page1 = new CreateProjectWizardPO( bot );

        String projectHookName = "hook-configuration-wizard";

        page1.createSDKProject( projectHookName, MENU_HOOK );

        if( hasAddedProject )
        {
            page1.finish();
        }
        else
        {
            page1.next();

            SetSDKLocationPO page2 = getSetSDKLoactionPage();

            page2.finish();
        }

        //sleep( 10000 );
    }

    @After
    public void waitForCreate()
    {
        ////sleep( 5000 );
    }

}

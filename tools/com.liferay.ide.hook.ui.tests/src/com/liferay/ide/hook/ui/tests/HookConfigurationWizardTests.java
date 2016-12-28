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

import com.liferay.ide.hook.ui.tests.page.AddEventActionPO;
import com.liferay.ide.hook.ui.tests.page.AddJSPFilePathPO;
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
import com.liferay.ide.ui.tests.swtbot.page.TextEditorPO;
import com.liferay.ide.ui.tests.swtbot.page.TreePO;

/**
 * @author Vicky Wang
 * @author Ying Xu
 */
public class HookConfigurationWizardTests extends SWTBotBase implements HookConfigurationWizard
{

    HookTypesToCreatePO newHookTypesPage = new HookTypesToCreatePO( bot, "New Liferay Hook Configuration" );

    String projectHookName = "hook-configuration-wizard";

    @AfterClass
    public static void cleanAll()
    {
        try
        {
            eclipse.closeShell( WINDOW_NEW_LIFERAY_HOOK_CONFIGURATION );
            eclipse.getPackageExporerView().deleteProjectExcludeNames(
                new String[] { getLiferayPluginsSdkName() }, true );
        }
        catch( Exception e )
        {
        }
    }

    @BeforeClass
    public static void unzipServerAndSdk() throws IOException
    {
        unzipPluginsSDK();
        unzipServer();
    }

    @Test
    public void hookConfigurationAllHookTypes()
    {
        CreateCustomJSPsPO customJSPpage = new CreateCustomJSPsPO( bot, INDEX_CUSTOM_JSPS_VALIDATION_MESSAGE );

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayHookConfigration().click();

        newHookTypesPage.getCustomJSPs().select();
        newHookTypesPage.getPortalProperties().select();
        newHookTypesPage.getServices().select();
        newHookTypesPage.getLanguageProperties().select();

        LanguagePropertiesPO languagePropertiesPage = new LanguagePropertiesPO(
            bot, WINDOW_NEW_LIFERAY_HOOK_CONFIGURATION, INDEX_LANGUAGE_PROPERTIES_VALIDATION_MESSAGE );

        assertEquals(
            " Modifying Language properties in Plugins SDK 7.0 is not supported, use Liferay module instead.",
            languagePropertiesPage.getValidationMessage() );
        assertFalse( languagePropertiesPage.nextButton().isEnabled() );

        newHookTypesPage.getLanguageProperties().deselect();
        newHookTypesPage.next();

        // Custom JSPs
        AddJSPFilePathPO jspFile = new AddJSPFilePathPO( bot );

        customJSPpage.getAddButton().click();
        jspFile.setJSPFilePathText( "CustomJsps.jsp" );
        jspFile.confirm();
        customJSPpage.next();

        // Portal Properties
        PortalPropertiesPO portalPropertiesPage =
            new PortalPropertiesPO( bot, INDEX_PORTAL_PROPERTIES_VALIDATION_MESSAGE );

        AddEventActionPO eventActionPage = new AddEventActionPO( bot );

        portalPropertiesPage.getEventAddButton().click();
        eventActionPage.setEvent( "portalProperties" );
        eventActionPage.setEventActionclass( "portalPropertiesClass" );
        eventActionPage.confirm();

        sleep( 1000 );
        portalPropertiesPage.next();

        // Service
        ServicesPO servicesPage = new ServicesPO( bot, INDEX_SERVICES_MESSAGE );

        AddServiceWrapperPO serviceWrapperPage = new AddServiceWrapperPO( bot );

        servicesPage.getAddButton().click();

        serviceWrapperPage.setServiceTypeText( "com.liferay.portal.service.AddressService" );
        serviceWrapperPage.setImplClassText( "com.liferay.portal.service.AddressServiceWrapper" );
        serviceWrapperPage.confirm();

        servicesPage.finish();

        TreePO projectTree = eclipse.showPackageExporerView().getProjectTree();

        String fileName = "CustomJsps.jsp";

        projectTree.expandNode(
            new String[] { projectHookName + "-hook", "docroot", "META-INF", "custom_jsps" } ).doubleClick( fileName );

        fileName = "portal.properties";

        projectTree.expandNode( new String[] { projectHookName + "-hook", "docroot/WEB-INF/src" } ).doubleClick(
            fileName );

        TextEditorPO textEditor = eclipse.getTextEditor( fileName );

        assertTrue( textEditor.isActive() );

        assertContains( "portalProperties=portalPropertiesClass", textEditor.getText() );

    }

    @Test
    public void hookConfigurationCustomJSPs()
    {
        String defaultMessage = "Create customs JSP folder and select JSPs to override.";
        String errorMessage = " Custom JSPs folder not configured.";

        CreateCustomJSPsPO customJSPpage = new CreateCustomJSPsPO( bot, INDEX_CUSTOM_JSPS_VALIDATION_MESSAGE );

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayHookConfigration().click();

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
        chooseCustomJSP.select( "html", "common", "themes", "bottom.jsp" );
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

        TreePO projectTree = eclipse.showPackageExporerView().getProjectTree();

        projectTree.expandNode(
            new String[] { projectHookName + "-hook", "docroot", "META-INF", "custom_jsps", "html", "common",
                "themes" } ).doubleClick( "bottom.jsp" );

    }

    @Test
    public void hookConfigurationLanguageProperties()
    {

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayHookConfigration().click();

        newHookTypesPage.getLanguageProperties().select();

        LanguagePropertiesPO languagePropertiesPage = new LanguagePropertiesPO(
            bot, WINDOW_NEW_LIFERAY_HOOK_CONFIGURATION, INDEX_LANGUAGE_PROPERTIES_VALIDATION_MESSAGE );

        assertEquals(
            " Modifying Language properties in Plugins SDK 7.0 is not supported, use Liferay module instead.",
            languagePropertiesPage.getValidationMessage() );
        assertFalse( languagePropertiesPage.nextButton().isEnabled() );

        newHookTypesPage.cancel();

    }

    @Test
    public void hookConfigurationPortalProperties()
    {
        String defaultMessage = "Specify which portal properties to override.";
        String errorMessage = " portal.properties file not configured.";

        PortalPropertiesPO portalPropertiesPage =
            new PortalPropertiesPO( bot, INDEX_PORTAL_PROPERTIES_VALIDATION_MESSAGE );

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayHookConfigration().click();

        newHookTypesPage.getPortalProperties().select();
        newHookTypesPage.next();
        // sleep( 1000 );

        assertEquals( defaultMessage, portalPropertiesPage.getValidationMessage() );
        assertEquals(
            "/hook-configuration-wizard-hook/docroot/WEB-INF/src/portal.properties",
            portalPropertiesPage.getPortalPropertiesFile().getText() );
        portalPropertiesPage.setPortalPropertiesFile( "" );

        assertEquals( errorMessage, portalPropertiesPage.getValidationMessage() );
        portalPropertiesPage.getBrowseButton().click();

        PortalPropertiesFilePO propertiesPage = new PortalPropertiesFilePO( bot );

        propertiesPage.select( "hook-configuration-wizard-hook", "docroot", "WEB-INF", "src" );
        propertiesPage.confirm();

        // Define actions to be executed on portal events
        AddEventActionPO eventActionPage = new AddEventActionPO( bot );

        portalPropertiesPage.getEventAddButton().click();
        eventActionPage.getNewButton().click();

        NewClassPO newClassPage = new NewClassPO( bot );

        newClassPage.setClassName( "test" );
        newClassPage.setJavaPackage( "hook" );
        newClassPage.getCreateButton().click();

        PropertySelectionPO propertySelectionPage = new PropertySelectionPO( bot );

        eventActionPage.getSelectEventButton().click();
        propertySelectionPage.select( "application.startup.events" );
        propertySelectionPage.confirm();

        portalPropertiesPage.getAddEventAction().setFocus();
        eventActionPage.confirm();

        portalPropertiesPage.getEventAddButton().click();
        eventActionPage.getSelectEventButton().click();
        propertySelectionPage.select( "application.startup.events" );
        propertySelectionPage.confirm();
        portalPropertiesPage.getAddEventAction().setFocus();

        EventSelectionPO eventSelectionPage = new EventSelectionPO( bot );
        eventActionPage.getSelectClass().click();

        sleep( 30000 );
        eventSelectionPage.setEventAction( "ObjectAction" );

        eventSelectionPage.confirm();

        eventActionPage.confirm();

        portalPropertiesPage.getDefineActionsOnPortalEvents().click( 1, 1 );
        portalPropertiesPage.getEventRemoveButton().click();

        portalPropertiesPage.getDefineActionsOnPortalEvents().click( 0, 1 );
        portalPropertiesPage.getEventEditButton().click();

        eventActionPage.setEventActionclass( "test_hook" );
        eventActionPage.confirm();

        // Specify properties to override
        AddPortalPropertiesOverridePO propertyOverridePage = new AddPortalPropertiesOverridePO( bot );

        portalPropertiesPage.getPropertyAddButton().click();
        propertyOverridePage.getSelectProperty().click();
        propertySelectionPage.select( "admin.default.group.names" );

        propertySelectionPage.confirm();
        portalPropertiesPage.getAddPropertyOverride().setFocus();
        propertyOverridePage.setValue( "1" );
        propertyOverridePage.confirm();

        portalPropertiesPage.getPropertyAddButton().click();
        propertyOverridePage.setProperty( "test" );
        propertyOverridePage.setValue( "2" );
        propertyOverridePage.confirm();

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

        TreePO projectTree = eclipse.getPackageExporerView().getProjectTree();

        String fileName = "portal.properties";

        projectTree.expandNode( new String[] { projectHookName + "-hook", "docroot/WEB-INF/src" } ).doubleClick(
            fileName );

        TextEditorPO textEditor = eclipse.getTextEditor( fileName );

        assertTrue( textEditor.isActive() );

        assertContains( "application.startup.events=test_hook", textEditor.getText() );
        assertContains( "admin.default.group.names=1", textEditor.getText() );

        projectTree = eclipse.showPackageExporerView().getProjectTree();

        fileName = "test.java";

        projectTree.expandNode( new String[] { projectHookName + "-hook", "docroot/WEB-INF/src", "hook" } ).doubleClick(
            fileName );

        textEditor = eclipse.getTextEditor( fileName );

        assertTrue( textEditor.isActive() );

        assertContains( "SimpleAction", textEditor.getText() );
    }

    @Test
    public void hookConfigurationServices()
    {
        String defaultMessage = "Specify which Liferay services to extend.";
        String errorMessage = " Need to specify at least one Service to override.";

        ServicesPO servicesPage = new ServicesPO( bot, INDEX_SERVICES_MESSAGE );

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayHookConfigration().click();

        newHookTypesPage.getServices().select();

        newHookTypesPage.next();

        assertEquals( defaultMessage, servicesPage.getValidationMessage() );

        servicesPage.getAddButton().click();

        AddServiceWrapperPO serviceWrapperPage = new AddServiceWrapperPO( bot );

        AddServiceWarningPO serviceWarningPage = new AddServiceWarningPO( bot );

        serviceWrapperPage.getNewButton().click();
        servicesPage.getAddService().setFocus();
        serviceWarningPage.getOkButton().click();

        servicesPage.getAddServiceWrapper().setFocus();
        serviceWrapperPage.getSelectImplClass( 1 ).click();
        sleep( 10000 );
        serviceWarningPage.getOkButton().click();

        servicesPage.getAddServiceWrapper().setFocus();
        serviceWrapperPage.setServiceTypeText( "test" );
        serviceWrapperPage.setImplClassText( "test" );

        servicesPage.getAddServiceWrapper().setFocus();
        serviceWrapperPage.confirm();
        servicesPage.getDefinePortalServices().click( 0, 1 );
        servicesPage.getRemoveButton().click();

        assertEquals( errorMessage, servicesPage.getValidationMessage() );

        servicesPage.getAddButton().click();
        serviceWrapperPage.getSelectServiceType().click();
        sleep( 15000 );

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

        TreePO projectTree = eclipse.getPackageExporerView().getProjectTree();

        String fileName = "ExtAccountService.java";
        projectTree.expandNode(
            new String[] { projectHookName + "-hook", "docroot/WEB-INF/src", "hookservice" } ).doubleClick( fileName );

        TextEditorPO textEditor = eclipse.getTextEditor( fileName );

        assertTrue( textEditor.isActive() );

        assertContains( "AccountServiceWrapper", textEditor.getText() );
    }

    @Before
    public void openWizardCreateProject()
    {
        Assume.assumeTrue( runTest() || runAllTests() );

        hasAddedProject = addedProjects();

        if( hasAddedProject )
        {
            return;
        }

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayPluginProject().click();

        CreateProjectWizardPO createProjectWizard = new CreateProjectWizardPO( bot );

        String projectHookName = "hook-configuration-wizard";

        createProjectWizard.createSDKProject( projectHookName, MENU_HOOK );

        if( hasAddedProject )
        {
            createProjectWizard.finish();
        }
        else
        {
            createProjectWizard.next();

            SetSDKLocationPO setSDKLocation = new SetSDKLocationPO( bot );

            setSDKLocation.setSdkLocation( getLiferayPluginsSdkDir().toString() );

            setSDKLocation.finish();
        }

        sleep( 60000 );
    }
}

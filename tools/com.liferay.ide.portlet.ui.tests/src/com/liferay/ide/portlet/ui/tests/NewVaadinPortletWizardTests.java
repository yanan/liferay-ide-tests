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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.liferay.ide.portlet.ui.tests.page.CreateVaadinPortletWizardPO;
import com.liferay.ide.portlet.ui.tests.page.LiferayPortletDeploymentDescriptorPO;
import com.liferay.ide.portlet.ui.tests.page.PortletDeploymentDescriptorPO;
import com.liferay.ide.project.ui.tests.ProjectWizard;
import com.liferay.ide.project.ui.tests.page.ChoosePortletFrameworkPO;
import com.liferay.ide.project.ui.tests.page.CreateProjectWizardPO;
import com.liferay.ide.project.ui.tests.page.SetSDKLocationPO;
import com.liferay.ide.ui.tests.SWTBotBase;

/**
 * @author Li Lu
 */
public class NewVaadinPortletWizardTests extends SWTBotBase
    implements VaadinPortletWizard, LiferayPortletWizard, ProjectWizard
{

    static String projectName = "vaadin-test";

    @BeforeClass
    public static void createJSFPortletProject() throws Exception
    {
        unzipPluginsSDK();
        unzipServer();

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayPluginProject().click();

        CreateProjectWizardPO page1 = new CreateProjectWizardPO( bot );
        page1.createSDKPortletProject( projectName );
        page1.next();

        ChoosePortletFrameworkPO page2 = new ChoosePortletFrameworkPO( bot );

        page2.selectPortletFramework( LABEL_VAADIN_FRAMEWORK );

        if( page1.finishButton().isEnabled() )
        {
            page1.finish();
        }

        else
        {
            page1.next();
            SetSDKLocationPO page3 = new SetSDKLocationPO( bot, "" );
            page3.setSdkLocation( getLiferayPluginsSdkDir().toString() );
            page3.finish();
        }
    }

    @AfterClass
    public static void deleteProject()
    {
        eclipse.getPackageExporerView().deleteProjectExcludeNames( new String[] { getLiferayPluginsSdkName() }, true );
    }

    CreateVaadinPortletWizardPO page = new CreateVaadinPortletWizardPO( bot, INDEX__VAADIN_VALIDATION_MESSAGE1 );

    PortletDeploymentDescriptorPO page2 = new PortletDeploymentDescriptorPO( bot );
    LiferayPortletDeploymentDescriptorPO page3 = new LiferayPortletDeploymentDescriptorPO( bot );

    @After
    public void closeWizard()
    {
        eclipse.closeShell( LABEL_NEW_LIFERAY_VAADIN_PORTLET );
    }

    @Before
    public void openWizard()
    {
        Assume.assumeTrue( runTest() || runAllTests() );

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayVaadinPortlet().click();
    }

    @Test
    public void testApplicationClass() throws Exception
    {
        page.setApplicationClassText( "" );

        String message = page.getValidationMessage();
        assertEquals( TEXT_CLASS_NAME_CANNOT_BE_EMPTY, message );
        assertEquals( false, page.finishButton().isEnabled() );
    }

    @Test
    public void testContentDefaultValues() throws Exception
    {
        // check default values page 1
        assertEquals( projectName + "-portlet", page.getPortletPluginProject() );
        assertTrue( page.getSourceFolderText().contains( "docroot/WEB-INF/src" ) );
        assertEquals( "NewVaadinPortletApplication", page.getApplicationClassText() );
        assertEquals( "com.test", page.getJavaPackageText() );
        assertEquals( "com.vaadin.Application", page.getSuperClassCombobox() );
        assertEquals( "com.vaadin.terminal.gwt.server.ApplicationPortlet2", page.getVaadinPortletClassText() );
        // page2
        page.next();
        assertEquals( "newvaadinportlet", page2.getPortletName() );
        assertEquals( "NewVaadinPortlet", page2.getDisplayName() );
        assertEquals( "NewVaadinPortlet", page2.getPortletTitle() );
        assertFalse( page2.get_createResourceBundleFileCheckbox().isChecked() );
        assertEquals( "content/Language.properties", page2.getResourceBundleFilePath() );
        // page3
        page.next();
        assertFalse( page3.isEntryCategoryEnabled() );
        assertFalse( page3.isEntryWeightEnabled() );
        assertFalse( page3.isCreateEntryClassEnabled() );
        assertFalse( page3.isEntryClassEnabled() );

        assertEquals( "/icon.png", page3.getIconText() );
        assertEquals( false, page3.isAllowMultipleInstancesChecked() );
        assertEquals( "/css/main.css", page3.getCssText() );
        assertEquals( "/js/main.js", page3.getJavaScriptText() );
        assertEquals( "newvaadinportlet-portlet", page3.getCssClassWrapperText() );
        assertEquals( "Sample", page3.getDisplayCategoryCombobox() );
        assertEquals( false, page3.isAddToControlPanelChecked() );
        assertEquals( "My Account Administration", page3.getEntryCategoryCombobox() );
        assertEquals( "1.5", page3.getEntryWeightText() );
        assertEquals( false, page3.isCreateEntryClassChecked() );
        assertEquals( "NewVaadinPortletApplicationControlPanelEntry", page3.getEntryClassText() );
    }

    @Test
    public void testPortletClass() throws Exception
    {

        page.setPortletClassText( "" );

        String message = page.getValidationMessage();
        assertEquals( TEXT_MUST_SPECIFY_VAADIN_PORTLET_CLASS, message );
        assertEquals( false, page.finishButton().isEnabled() );
    }

}

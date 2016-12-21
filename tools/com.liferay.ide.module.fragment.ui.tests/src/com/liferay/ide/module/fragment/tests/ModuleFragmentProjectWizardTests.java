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

package com.liferay.ide.module.fragment.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.liferay.ide.module.fragment.ui.tests.page.AddFilesToOverridePO;
import com.liferay.ide.module.fragment.ui.tests.page.CreateModuleFragmentProjectWizardPO;
import com.liferay.ide.module.fragment.ui.tests.page.HostOSGiBundlePO;
import com.liferay.ide.module.fragment.ui.tests.page.SetModuleFragmentProjectOSGiBundlePO;
import com.liferay.ide.server.ui.tests.page.NewServerPO;
import com.liferay.ide.server.ui.tests.page.NewServerRuntimeEnvPO;
import com.liferay.ide.ui.tests.SWTBotBase;
import com.liferay.ide.ui.tests.swtbot.page.DialogPO;
import com.liferay.ide.ui.tests.swtbot.page.TreePO;

/**
 * @author Vicky Wang
 */
public class ModuleFragmentProjectWizardTests extends SWTBotBase implements ModuleFragmentProjectWizard
{

    String projectName = "module-fragment-project";

    @AfterClass
    public static void cleanAll()
    {
        eclipse.closeShell( WINDOW_NEW_LIFERAY_MODULE_FRAGMENT );
    }

    @BeforeClass
    public static void prepareServer() throws IOException
    {
        unzipServer();
    }

    @Test
    public void moduleFragmentProjectWizard()
    {
        CreateModuleFragmentProjectWizardPO newModuleFragmentPage =
            new CreateModuleFragmentProjectWizardPO( bot, INDEX_VALIDATION_PAGE_MESSAGE3 );

        newModuleFragmentPage.setProjectName( ".." );
        sleep();

        assertEquals( " '..'" + TEXT_INVALID_NAME_ON_PLATFORM, newModuleFragmentPage.getValidationMessage() );

        newModuleFragmentPage.setProjectName( "##" );
        sleep();
        assertEquals( " " + TEXT_INVALID_GRADLE_PROJECT, newModuleFragmentPage.getValidationMessage() );

        newModuleFragmentPage.setProjectName( "*" );
        sleep();
        assertEquals( " *" + TEXT_INVALID_CHARACTER_IN_RESOURCE_NAME + "'*'.",
            newModuleFragmentPage.getValidationMessage() );

        newModuleFragmentPage.setProjectName( TEXT_BLANK );
        sleep();
        assertEquals( TEXT_ENTER_PROJECT_NAME, newModuleFragmentPage.getValidationMessage() );

        newModuleFragmentPage.setProjectName( projectName );
        sleep();

        assertTrue( newModuleFragmentPage.isLiferayRuntimeTextEnabled() );
        newModuleFragmentPage.next();

        // select OSGi Bundle and Overridden files
        SetModuleFragmentProjectOSGiBundlePO moduleFragmentOSGiBundlePage =
            new SetModuleFragmentProjectOSGiBundlePO( bot, INDEX_VALIDATION_MESSAGE2 );

        assertEquals( TEXT_OSGI_BUNDLE_BLANK, moduleFragmentOSGiBundlePage.getValidationMessage() );

        moduleFragmentOSGiBundlePage.getSelectOSGiBundleButton().click();

        HostOSGiBundlePO selectOSGiBundlePage = new HostOSGiBundlePO( bot );
        AddFilesToOverridePO addJSPFilesPage = new AddFilesToOverridePO( bot );

        selectOSGiBundlePage.setOSGiBundle( "com.liferay.blogs.web" );
        selectOSGiBundlePage.confirm();

        moduleFragmentOSGiBundlePage.getOverriddenFiles().containsItem( null );

        String[] files = new String[] { "META-INF/resources/blogs_admin/configuration.jsp",
            "META-INF/resources/blogs_aggregator/init.jsp", "META-INF/resources/blogs/asset/abstract.jsp",
            "META-INF/resources/blogs/edit_entry.jsp", "portlet.properties" };

        for( String file : files )
        {
            moduleFragmentOSGiBundlePage.getAddOverriddenFilesButton().click();
            addJSPFilesPage.select( file );
            addJSPFilesPage.confirm();
        }

        moduleFragmentOSGiBundlePage.finish();

        sleep();

        DialogPO dialogPage = new DialogPO( bot, "Open Associated Perspective", BUTTON_YES, BUTTON_NO );

        dialogPage.confirm();

        TreePO projectTree = eclipse.showPackageExporerView().getProjectTree();

        String[] pathTree = new String[] { projectName, "src/main/java" };

        projectTree.expandNode( pathTree ).doubleClick( "portlet-ext.properties" );

        pathTree = new String[] { projectName, "src/main/resources", "META-INF", "resources", "blogs" };

        projectTree.expandNode( pathTree ).doubleClick( "edit_entry.jsp" );

        pathTree = new String[] { projectName, "src/main/resources", "META-INF", "resources", "blogs", "asset" };

        projectTree.expandNode( pathTree ).doubleClick( "abstract.jsp" );

        pathTree = new String[] { projectName, "src/main/resources", "META-INF", "resources", "blogs_admin" };

        projectTree.expandNode( pathTree ).doubleClick( "configuration.jsp" );

        pathTree = new String[] { projectName, "src/main/resources", "META-INF", "resources", "blogs_aggregator" };

        projectTree.expandNode( pathTree ).doubleClick( "init.jsp" );
    }

    @Before
    public void openWizard()
    {
        Assume.assumeTrue( runTest() || runAllTests() );

        NewServerPO newServerPage = new NewServerPO( bot );
        NewServerRuntimeEnvPO setRuntimePage = new NewServerRuntimeEnvPO( bot );

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayServer().click();

        newServerPage.getServerTypeTree().selectTreeItem( NODE_LIFERAY_INC, NODE_LIFERAY_7X );
        newServerPage.next();

        setRuntimePage.getServerLocation().setText( getLiferayServerDir().toOSString() );

        assertEquals( "Tomcat", setRuntimePage.getPortalBundleType().getText() );

        setRuntimePage.finish();

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleFragmentProject().click();
    }

}

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

import static org.eclipse.swtbot.swt.finder.SWTBotAssert.assertContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.liferay.ide.module.fragment.ui.tests.page.AddFilesToOverridePO;
import com.liferay.ide.module.fragment.ui.tests.page.CreateModuleFragmentFilesWizardPO;
import com.liferay.ide.module.fragment.ui.tests.page.CreateModuleFragmentProjectWizardPO;
import com.liferay.ide.module.fragment.ui.tests.page.HostOSGiBundlePO;
import com.liferay.ide.module.fragment.ui.tests.page.SetModuleFragmentProjectOSGiBundlePO;
import com.liferay.ide.project.ui.tests.page.NewLiferayModuleProjectWizardPO;
import com.liferay.ide.server.ui.tests.page.DeleteRuntimeConfirmPO;
import com.liferay.ide.server.ui.tests.page.DeleteRuntimePO;
import com.liferay.ide.server.ui.tests.page.NewServerPO;
import com.liferay.ide.server.ui.tests.page.NewServerRuntimeEnvPO;
import com.liferay.ide.ui.tests.SWTBotBase;
import com.liferay.ide.ui.tests.swtbot.page.CTabItemPO;
import com.liferay.ide.ui.tests.swtbot.page.TablePO;
import com.liferay.ide.ui.tests.swtbot.page.TextEditorPO;
import com.liferay.ide.ui.tests.swtbot.page.TreePO;

/**
 * @author Demetria Ding
 */
@RunWith( SWTBotJunit4ClassRunner.class )
public class NewLiferayModuleFragmentFilesTests extends SWTBotBase implements ModuleFragmentProjectWizard
{

    NewServerPO newServerPage = new NewServerPO( bot );

    NewServerRuntimeEnvPO setRuntimePage = new NewServerRuntimeEnvPO( bot );

    CreateModuleFragmentFilesWizardPO newModuleFragmentFilePage =
        new CreateModuleFragmentFilesWizardPO( bot, INDEX_VALIDATION_PAGE_MESSAGE3 );

    CreateModuleFragmentProjectWizardPO newModuleFragmentProjectPage = new CreateModuleFragmentProjectWizardPO( bot );

    NewLiferayModuleProjectWizardPO createModuleProjectPage = new NewLiferayModuleProjectWizardPO( bot );

    SetModuleFragmentProjectOSGiBundlePO moduleFragmentProjectOSGiBundlePage =
        new SetModuleFragmentProjectOSGiBundlePO( bot, INDEX_VALIDATION_MESSAGE2 );

    HostOSGiBundlePO selectOSGiBundlePage = new HostOSGiBundlePO( bot );

    AddFilesToOverridePO addJSPFilesPage = new AddFilesToOverridePO( bot );

    TreePO projectTree = eclipse.showPackageExporerView().getProjectTree();

    TreePO serverTree = new TreePO( bot, 1 );

    TablePO tableItem = new TablePO( bot, "Override File Path" );

    @BeforeClass
    public static void prepareServer() throws IOException
    {
        unzipServer();

        eclipse.getLiferayWorkspacePerspective().activate();

    }

    @Test
    public void moduleFragmentFilesWizardWithGradleModuleProject()
    {
        String GradleFragmentProjectName = "testGradleFragment";

        if( serverTree.hasItems() )
        {
            deleteRuntimeFromPreferences();
        }

        addLiferayServerAndOpenWizard();

        newModuleFragmentProjectPage.setProjectName( GradleFragmentProjectName, TEXT_BUILD_TYPE_GRADLE );
        newModuleFragmentProjectPage.next();

        moduleFragmentProjectOSGiBundlePage.getSelectOSGiBundleButton().click();

        selectOSGiBundlePage.setOSGiBundle( "com.liferay.blogs.web-1.0.15" );
        selectOSGiBundlePage.confirm();

        moduleFragmentProjectOSGiBundlePage.getOverriddenFiles().containsItem( null );

        moduleFragmentProjectOSGiBundlePage.finish();
        sleep( 8000 );

        projectTree.setFocus();
        projectTree.getTreeItem( GradleFragmentProjectName ).select();

        eclipse.getFileMenu().clickMenu( TOOLBAR_NEW, LABLE_LIFERAY_MODULE_FRAGMENT_FILES );

        sleep();

        assertEquals( GradleFragmentProjectName, newModuleFragmentFilePage.getProjectNameText() );
        assertEquals( TEXT_BLANK, newModuleFragmentFilePage.getHostOsgiBundleText() );

        assertTrue( newModuleFragmentFilePage.getAddOverriddenFilesButton().isEnabled() );
        assertTrue( newModuleFragmentFilePage.getAddOverrideFilePathButton().isEnabled() );
        assertFalse( newModuleFragmentFilePage.getDeletOverrideFileButton().isEnabled() );
        assertFalse( newModuleFragmentFilePage.getUpOverrideFileButton().isEnabled() );
        assertFalse( newModuleFragmentFilePage.getDownOverrideFileButton().isEnabled() );

        newModuleFragmentFilePage.getOverriddenFiles().containsItem( null );

        String[] files = new String[] { "META-INF/resources/blogs_admin/init.jsp",
            "META-INF/resources/blogs_aggregator/configuration.jsp", "META-INF/resources/blogs/asset/full_content.jsp",
            "META-INF/resources/blogs/view_entry.jsp", "portlet.properties" };

        for( String file : files )
        {
            newModuleFragmentFilePage.getAddOverriddenFilesButton().click();
            addJSPFilesPage.select( file );
            addJSPFilesPage.confirm();
        }

        assertTrue( newModuleFragmentFilePage.getAddOverriddenFilesButton().isEnabled() );
        assertTrue( newModuleFragmentFilePage.getAddOverrideFilePathButton().isEnabled() );
        assertFalse( newModuleFragmentFilePage.getDeletOverrideFileButton().isEnabled() );
        assertFalse( newModuleFragmentFilePage.getUpOverrideFileButton().isEnabled() );
        assertFalse( newModuleFragmentFilePage.getDownOverrideFileButton().isEnabled() );

        newModuleFragmentFilePage.getOverriddenFiles().click( 1 );

        assertTrue( newModuleFragmentFilePage.getAddOverriddenFilesButton().isEnabled() );
        assertTrue( newModuleFragmentFilePage.getAddOverrideFilePathButton().isEnabled() );
        assertTrue( newModuleFragmentFilePage.getDeletOverrideFileButton().isEnabled() );
        assertTrue( newModuleFragmentFilePage.getUpOverrideFileButton().isEnabled() );
        assertTrue( newModuleFragmentFilePage.getDownOverrideFileButton().isEnabled() );

        newModuleFragmentFilePage.getDownOverrideFileButton().click();
        newModuleFragmentFilePage.getUpOverrideFileButton().click();
        newModuleFragmentFilePage.getDeletOverrideFileButton().click();
        newModuleFragmentFilePage.getDeletOverrideFileButton().click();
        newModuleFragmentFilePage.getDeletOverrideFileButton().click();
        newModuleFragmentFilePage.getDeletOverrideFileButton().click();

        newModuleFragmentFilePage.finish();

        sleep( 10000 );

    }

    @Test
    public void moduleFragmentFilesWizardWithMavenModuleProject()
    {
        String MavenFragmentProjectName = "testMavenFragment";

        if( serverTree.hasItems() )
        {
            deleteRuntimeFromPreferences();
        }
        else
        {
            addLiferayServerAndOpenWizard();
        }

        newModuleFragmentProjectPage.setProjectName( MavenFragmentProjectName, TEXT_BUILD_TYPE_MAVEN );
        newModuleFragmentProjectPage.next();

        moduleFragmentProjectOSGiBundlePage.getSelectOSGiBundleButton().click();

        selectOSGiBundlePage.setOSGiBundle( "com.liferay.blogs.web-1.0.15" );
        selectOSGiBundlePage.confirm();

        moduleFragmentProjectOSGiBundlePage.getOverriddenFiles().containsItem( null );

        moduleFragmentProjectOSGiBundlePage.finish();
        
        sleep( 10000 );

        projectTree.setFocus();
        projectTree.getTreeItem( MavenFragmentProjectName ).doAction(
            TOOLBAR_NEW, LABLE_LIFERAY_MODULE_FRAGMENT_FILES );

        sleep();

        assertEquals( MavenFragmentProjectName, newModuleFragmentFilePage.getProjectNameText() );
        assertEquals( TEXT_BLANK, newModuleFragmentFilePage.getHostOsgiBundleText() );

        newModuleFragmentFilePage.getOverriddenFiles().containsItem( null );

        String[] files = new String[] { "META-INF/resources/blogs/configuration.jsp",
            "META-INF/resources/blogs_aggregator/init-ext.jsp", "META-INF/resources/blogs/asset/full_content.jsp",
            "META-INF/resources/blogs_admin/entry_action.jsp", "portlet.properties" };

        for( String file : files )
        {
            newModuleFragmentFilePage.getAddOverriddenFilesButton().click();
            addJSPFilesPage.select( file );
            addJSPFilesPage.confirm();
        }

        newModuleFragmentFilePage.finish();
        
        sleep( 10000 );

        projectTree.setFocus();
        projectTree.expandNode( MavenFragmentProjectName ).doubleClick();

        assertTrue(
            projectTree.expandNode(
                MavenFragmentProjectName, "src/main/resources", "META-INF", "resources", "blogs",
                "configuration.jsp" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                MavenFragmentProjectName, "src/main/resources", "META-INF", "resources", "blogs_aggregator",
                "init-ext.jsp" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                MavenFragmentProjectName, "src/main/resources", "META-INF", "resources", "blogs", "asset",
                "full_content.jsp" ).isVisible() );
        assertTrue(
            projectTree.expandNode(
                MavenFragmentProjectName, "src/main/resources", "META-INF", "resources", "blogs_admin",
                "entry_action.jsp" ).isVisible() );
        assertTrue(
            projectTree.expandNode( MavenFragmentProjectName, "src/main/java", "portlet-ext.properties" ).isVisible() );

        String pomXmlFileName = "pom.xml";

        projectTree.expandNode( MavenFragmentProjectName, pomXmlFileName ).doubleClick();

        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( MavenFragmentProjectName + "/pom.xml" );

        assertContains( "<artifactId>testMavenFragment</artifactId>", pomXmlFile.getText() );

        pomXmlFile.close();
    }

    @Ignore
    @Test
    public void moduleFragmentFilesWizardWithoutServer()
    {
        if( serverTree.hasItems() )
        {
            deleteRuntimeFromPreferences();
        }

        eclipse.getFileMenu().clickMenu( TOOLBAR_NEW, LABLE_LIFERAY_MODULE_FRAGMENT_FILES );

        assertEquals( TEXT_PROJECT_NAME_MUST_BE_SPECIFIED, newModuleFragmentFilePage.getValidationMessage() );
        assertEquals( TEXT_BLANK, newModuleFragmentFilePage.getProjectNameText() );
        assertEquals( TEXT_BLANK, newModuleFragmentFilePage.getHostOsgiBundleText() );

        assertTrue( newModuleFragmentFilePage.getNewLiferayRuntimeutton().isEnabled() );
        assertFalse( newModuleFragmentFilePage.getAddOverriddenFilesButton().isEnabled() );
        assertTrue( newModuleFragmentFilePage.getAddOverrideFilePathButton().isEnabled() );
        assertFalse( newModuleFragmentFilePage.getDeletOverrideFileButton().isEnabled() );
        assertFalse( newModuleFragmentFilePage.getUpOverrideFileButton().isEnabled() );
        assertFalse( newModuleFragmentFilePage.getDownOverrideFileButton().isEnabled() );

        newModuleFragmentFilePage.getNewLiferayRuntimeutton().click();
        newServerPage.getServerTypeTree().selectTreeItem( NODE_LIFERAY_INC, NODE_LIFERAY_7X );
        newServerPage.next();
        
        sleep();

        if( !newServerPage.finishButton().isEnabled() )
        {
            setRuntimePage.getServerLocation().setText( getLiferayServerDir().toOSString() );

            assertEquals( "Tomcat", setRuntimePage.getPortalBundleType().getText() );
            setRuntimePage.finish();
        }
        else
        {
            newServerPage.finish();
        }

        newModuleFragmentFilePage.cancel();

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleProject().click();

        createModuleProjectPage.createModuleProject( "ModuleProject" );
        createModuleProjectPage.finish();

        projectTree.setFocus();
        projectTree.getTreeItem( "ModuleProject" ).select();
        eclipse.getFileMenu().clickMenu( TOOLBAR_NEW, LABLE_LIFERAY_MODULE_FRAGMENT_FILES );
        assertEquals( TEXT_PROJECT_NAME_MUST_BE_SPECIFIED, newModuleFragmentFilePage.getValidationMessage() );

        assertFalse( newModuleFragmentFilePage.backButton().isEnabled() );
        assertFalse( newModuleFragmentFilePage.nextButton().isEnabled() );
        assertFalse( newModuleFragmentFilePage.finishButton().isEnabled() );
        assertTrue( newModuleFragmentFilePage.cancelButton().isEnabled() );
        assertTrue( newModuleFragmentFilePage.cancelButton().isEnabled() );
        assertTrue( newModuleFragmentFilePage.cancelButton().isEnabled() );

        assertTrue( newModuleFragmentFilePage.getNewLiferayRuntimeutton().isEnabled() );
        assertFalse( newModuleFragmentFilePage.getAddOverriddenFilesButton().isEnabled() );
        assertTrue( newModuleFragmentFilePage.getAddOverrideFilePathButton().isEnabled() );
        assertFalse( newModuleFragmentFilePage.getDeletOverrideFileButton().isEnabled() );
        assertFalse( newModuleFragmentFilePage.getUpOverrideFileButton().isEnabled() );
        assertFalse( newModuleFragmentFilePage.getDownOverrideFileButton().isEnabled() );
        createModuleProjectPage.cancel();

    }

    @Before
    public void openWizard()
    {
        Assume.assumeTrue( runTest() || runAllTests() );
    }

    public void addLiferayServerAndOpenWizard()
    {
        eclipse.getCreateLiferayProjectToolbar().getNewLiferayServer().click();

        newServerPage.getServerTypeTree().selectTreeItem( NODE_LIFERAY_INC, NODE_LIFERAY_7X );

        sleep( 3000 );

        if( !newServerPage.finishButton().isEnabled() )
        {
            newServerPage.next();

            sleep();

            setRuntimePage.getServerLocation().setText( getLiferayServerDir().toOSString() );

            assertEquals( "Tomcat", setRuntimePage.getPortalBundleType().getText() );
            setRuntimePage.finish();
        }
        else
        {
            newServerPage.finish();
        }
        eclipse.getCreateLiferayProjectToolbar().getNewLiferayModuleFragmentProject().click();
    }

    public void deleteRuntimeFromPreferences()
    {
        eclipse.getPreferencesMenu().click();

        TreePO preferencesTree = new TreePO( bot );

        preferencesTree.expandNode( "Server", "Runtime Environments" ).select();
        sleep();

        DeleteRuntimePO deleteRuntime = new DeleteRuntimePO( bot );

        if( deleteRuntime.getServerRuntimeEnvironments().containsItem( NODE_LIFERAY_7X ) )
        {
            deleteRuntime.getServerRuntimeEnvironments().click( NODE_LIFERAY_7X );
            deleteRuntime.getRemove().click();

            DeleteRuntimeConfirmPO confirmDelete = new DeleteRuntimeConfirmPO( bot );

            confirmDelete.confirm();
        }

        deleteRuntime.confirm();

        sleep();
    }

    @After
    public void deleteProject()
    {
        eclipse.closeShell( LABEL_NEW_LIFERAY_MODULE_FRAGMENT_FILES );

        if( addedProjects() )
        {
            eclipse.getPackageExporerView().deleteProjectExcludeNames(
                new String[] { getLiferayPluginsSdkName() }, true );
        }
    }
}

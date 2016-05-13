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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.liferay.ide.project.ui.tests.page.LiferayProjectFromExistSourceWizardPO;
import com.liferay.ide.ui.tests.SWTBotBase;
import com.liferay.ide.ui.tests.swtbot.page.DialogPO;
import com.liferay.ide.ui.tests.swtbot.page.TreeItemPO;
import com.liferay.ide.ui.tests.util.FileUtil;
import com.liferay.ide.ui.tests.util.ZipUtil;

/**
 * @author Li Lu
 */
@RunWith( SWTBotJunit4ClassRunner.class )
public class SDKProjectImportWizardTests extends SWTBotBase implements LiferayProjectFromExistSourceWizard
{

    private static final String BUNDLE_ID = "com.liferay.ide.project.ui.tests";

    @AfterClass
    public static void deleteSDK()
    {
        eclipse.getPackageExporerView().deleteResouceByName( getLiferayPluginsSdkName(), true );
    }

    public void openWizard()
    {
        eclipse.getCreateLiferayProjectToolbar().menuClick( MENU_NEW_LIFERAY_PROJECT_EXIS_SOURCE );
    }

    private LiferayProjectFromExistSourceWizardPO _wizard = new LiferayProjectFromExistSourceWizardPO( bot );

    @After
    public void cleanUp()
    {
        DialogPO shell = new DialogPO( bot, TITLE_NEW_LIFERAY_PROJECT_EXIS_SOURCE );
        shell.closeIfOpen();

        eclipse.getPackageExporerView().deleteProjectExcludeNames(
            ( new String[] { getLiferayPluginsSdkName() } ), true );
    }

    public void importSDKProject( String path, String projectName ) throws Exception
    {

        path = getLiferayPluginsSdkDir().append( path ).toOSString();

        final File projectZipFile = getProjectZip( BUNDLE_ID, projectName );

        ZipUtil.unzip( projectZipFile, new File( path ) );

        openWizard();

        _wizard.getProjectDirectoryText().setText( path + "/" + projectName );

        String FILE_SEPARATOR = System.getProperty( "file.separator" );
        String[] sdkPath = path.split( "\\" + FILE_SEPARATOR );

        String projectFolder = sdkPath[sdkPath.length - 1];

        String pluginType = projectFolder.endsWith( "s" )
            ? projectFolder.substring( 0, projectFolder.lastIndexOf( 's' ) ) : projectFolder;

        sleep( 1000 );
        assertEquals( pluginType, _wizard.getPluginTypeText().getText() );

        assertEquals( "7.0.0", _wizard.getSdkVersionText().getText() );

        assertTrue( _wizard.finishButton().isEnabled() );
        bot.button( "Finish" ).click();

        TreeItemPO sdkTreeItem =
            eclipse.getPackageExporerView().getProjectTree().getTreeItem( getLiferayPluginsSdkName() );
        assertTrue( sdkTreeItem.isVisible() );
    }

    @Test
    public void testDefaults()
    {
        openWizard();

        assertEquals( MESSAGE_DEFAULT, _wizard.getValidationMessage() );

        assertTrue( _wizard.getProjectDirectoryText().isEnabled() );
        assertTrue( _wizard.getBrowseProjectDirectory().isEnabled() );
        assertTrue( _wizard.getPluginTypeText().isEnabled() );
        assertTrue( _wizard.getSdkVersionText().isEnabled() );

        assertTrue( _wizard.getProjectDirectoryText().isActive() );
        assertFalse( _wizard.getBrowseProjectDirectory().isActive() );
        assertFalse( _wizard.getPluginTypeText().isActive() );
        assertFalse( _wizard.getSdkVersionText().isActive() );

        _wizard.cancel();
    }

    @Test
    public void testPluginType() throws Exception
    {
        importSDKProject( "hooks", "Import-223-hook" );
        importSDKProject( "themes", "Import-223-theme" );
        importSDKProject( "ext", "Import-223-ext" );
        importSDKProject( "layouttpl", "Import-223-layouttpl" );
    }

    @Test
    public void testProjectDirValidation() throws Exception
    {
        openWizard();
        _wizard.getProjectDirectoryText().setText( "AAA" );
        sleep( 1000 );
        assertEquals( " \"AAA\" is not an absolute path.", _wizard.getValidationMessage() );
        assertFalse( _wizard.finishButton().isEnabled() );

        _wizard.getProjectDirectoryText().setText( "C:/" );
        sleep( 1000 );
        assertEquals( MESSAGE_INVALID_PROJECT_LOCATION, _wizard.getValidationMessage() );
        assertFalse( _wizard.finishButton().isEnabled() );
        _wizard.cancel();

        // import project outside of SDK
        importSDKProject( "portlets", "Import-223-portlet" );
        IPath projectDir = getLiferayPluginsSdkDir().append( "/portlets/Import-223-portlet" ).makeAbsolute();
        IPath projectCopyDir =
            getLiferayPluginsSdkDir().removeLastSegments( 2 ).append( "/Import-223-portlet" ).makeAbsolute();

        FileUtil.copyDirectiory( projectDir.toOSString(), projectCopyDir.toOSString() );

        eclipse.getPackageExporerView().deleteResouceByName( "Import-223-portlet", false );

        openWizard();
        _wizard.getProjectDirectoryText().setText( projectCopyDir.toOSString() );

        sleep( 1000 );
        assertTrue( _wizard.finishButton().isEnabled() );

        // import project from another SDK
        IPath sdk2Dir = getLiferayPluginsSdkDir().removeLastSegments( 1 ).append( "sdk2" );

        FileUtil.copyDirectiory( getLiferayPluginsSdkDir().toOSString(), sdk2Dir.toOSString() );

        FileUtil.copyDirectiory(
            projectCopyDir.toOSString(), sdk2Dir.append( "/portlets/Import-223-portlet" ).toOSString() );

        _wizard.getProjectDirectoryText().setText( sdk2Dir.append( "/portlets/Import-223-portlet" ).toString() );

        sleep( 1000 );
        assertEquals( MESSAGE_PROJECT_HAS_DIFF_SDK, _wizard.getValidationMessage() );

        assertFalse( _wizard.finishButton().isEnabled() );

        _wizard.cancel();

        FileUtil.deleteDir( sdk2Dir.toFile(), true );
        FileUtil.deleteDir( projectCopyDir.toFile(), true );
    }

    @Test
    public void testValidationNoSDK() throws Exception
    {
        // test import project when no sdk in wrokspace
        importSDKProject( "portlet", "Import-223-portlet" );

        TreeItemPO sdkTreeItem =
            eclipse.getPackageExporerView().getProjectTree().getTreeItem( getLiferayPluginsSdkName() );
        assertTrue( sdkTreeItem.isEnabled() );

        String projectPath = getLiferayPluginsSdkDir().append( "portlet/Import-223-portlet" ).toOSString();

        openWizard();
        _wizard.getProjectDirectoryText().setText( projectPath );

        sleep( 1000 );
        assertEquals( MESSAGE_PROJECT_NAME_EXSIT, _wizard.getValidationMessage() );
        assertFalse( _wizard.finishButton().isEnabled() );

        _wizard.cancel();
    }
}

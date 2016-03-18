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

package com.liferay.ide.ui.tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.swt.SWT;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.liferay.ide.ui.tests.swtbot.page.EclipsePO;
import com.liferay.ide.ui.tests.util.CoreUtil;
import com.liferay.ide.ui.tests.util.FileUtil;
import com.liferay.ide.ui.tests.util.ZipUtil;

/**
 * @author Terry Jia
 * @author Ashley Yuan
 */
@RunWith( SWTBotJunit4ClassRunner.class )
public class SWTBotBase implements UIBase
{

    private final static String liferayBundlesDir = System.getProperty( "liferay.bundles.dir" );
    private final static String tempDir = System.getProperty( "temp.dir" );

    public static boolean hasAddedProject = false;

    private final long DEFAULT_SLEEP_MILLIS = 1000;
    private static IPath liferayBundlesPath;

    protected KeyStroke ctrl = KeyStroke.getInstance( SWT.CTRL, 0 );
    protected KeyStroke N = KeyStroke.getInstance( 'N' );

    public static SWTWorkbenchBot bot;
    public static EclipsePO eclipse;

    @BeforeClass
    public static void beforeClass() throws Exception
    {
        bot = new SWTWorkbenchBot();

        eclipse = new EclipsePO( bot );

        try
        {
            eclipse.getWelcomeView().close();

            eclipse.getLiferayPerspective().activate();

            eclipse.showProgressView();

            eclipse.showErrorLogView().clearLogViewer();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        SWTBotPreferences.TIMEOUT = 30000;

        SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";

        unzipPluginsSDK();
    }

    protected static IPath getIvyCacheZip()
    {
        return getLiferayBundlesPath().append( "ivy-cache.zip" );
    }

    protected static IPath getLiferayBundlesPath()
    {
        if( liferayBundlesPath == null )
        {
            liferayBundlesPath = new Path( liferayBundlesDir );
        }

        return liferayBundlesPath;
    }

    protected static IPath getLiferayPluginsSdkDir()
    {
        return new Path(tempDir).append( "liferay-plugins-sdk-6.2" );
    }

    protected static String getLiferayPluginsSdkName()
    {
        return "liferay-plugins-sdk-6.2";
    }

    protected static IPath getLiferayPluginsSDKZip()
    {
        return getLiferayBundlesPath().append( "liferay-plugins-sdk-6.2.zip" );
    }

    protected static String getLiferayPluginsSdkZipFolder()
    {
        return "liferay-plugins-sdk-6.2/";
    }

    protected static void unzipPluginsSDK() throws IOException
    {
        FileUtil.deleteDir( getLiferayPluginsSdkDir().toFile(), true );

        final File liferayPluginsSdkZipFile = getLiferayPluginsSDKZip().toFile();

        assertEquals(
            "Expected file to exist: " + liferayPluginsSdkZipFile.getAbsolutePath(), true,
            liferayPluginsSdkZipFile.exists() );

        final File liferayPluginsSdkDirFile = getLiferayPluginsSdkDir().toFile();

        liferayPluginsSdkDirFile.mkdirs();

        final String liferayPluginsSdkZipFolder = getLiferayPluginsSdkZipFolder();

        if( CoreUtil.isNullOrEmpty( liferayPluginsSdkZipFolder ) )
        {
            ZipUtil.unzip( liferayPluginsSdkZipFile, liferayPluginsSdkDirFile );
        }
        else
        {
            ZipUtil.unzip(
                liferayPluginsSdkZipFile, liferayPluginsSdkZipFolder, liferayPluginsSdkDirFile,
                new NullProgressMonitor() );
        }

        assertEquals( true, liferayPluginsSdkDirFile.exists() );

        final File ivyCacheDir = new File( liferayPluginsSdkDirFile, ".ivy" );

        if( !ivyCacheDir.exists() )
        {
            final File ivyCacheZipFile = getIvyCacheZip().toFile();

            assertEquals(
                "Expected ivy-cache.zip to be here: " + ivyCacheZipFile.getAbsolutePath(), true,
                ivyCacheZipFile.exists() );

            ZipUtil.unzip( ivyCacheZipFile, liferayPluginsSdkDirFile );
        }

        assertEquals( "Expected .ivy folder to be here: " + ivyCacheDir.getAbsolutePath(), true, ivyCacheDir.exists() );
    }

    protected void sleep()
    {
        sleep( DEFAULT_SLEEP_MILLIS );
    }

    protected void sleep( long millis )
    {
        bot.sleep( millis );
    }


    public boolean addedProjects()
    {
        eclipse.showPackageExporerView();

        return eclipse.hasProjects();
    }

}
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

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.keyboard.Keyboard;
import org.eclipse.swtbot.swt.finder.keyboard.KeyboardFactory;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.TextConsole;
import org.eclipse.ui.ide.IDE;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.liferay.ide.ui.tests.swtbot.page.EclipsePO;
import com.liferay.ide.ui.tests.util.CoreUtil;
import com.liferay.ide.ui.tests.util.FileUtil;
import com.liferay.ide.ui.tests.util.ZipUtil;

/**
 * @author Terry Jia
 * @author Ashley Yuan
 * @author Vicky Wang
 * @author Ying Xu
 */
@RunWith( SWTBotJunit4ClassRunner.class )
public class SWTBotBase implements UIBase
{

    public final static String BUNDLE_ZIP = "liferay-ce-portal-tomcat-7.0-ga3-20160804222206210.zip";
    public final static String BUNDLE_DIR = "liferay-ce-portal-7.0-ga3";
    public final static String TOMCAT_NAME = "tomcat-8.0.32";
    public final static String IVY_CACHE_ZIP = "ivy-cache-7.0.zip";
    public final static String PLUGINS_SDK_ZIP = "com.liferay.portal.plugins.sdk-7.0-ga3-20160804222206210.zip";
    public final static String PLUGINS_SDK_DIR = "com.liferay.portal.plugins.sdk-7.0";

    private final static String liferayBundlesDir = System.getProperty( "liferay.bundles.dir" );
    private final static String runTest = System.getProperty( "runTest" );

    public static boolean hasAddedProject = false;

    private final long DEFAULT_SLEEP_MILLIS = 1000;
    private static IPath liferayBundlesPath;

    protected Keyboard keyPress = KeyboardFactory.getAWTKeyboard();

    protected KeyStroke ctrl = KeyStroke.getInstance( SWT.CTRL, 0 );
    protected KeyStroke N = KeyStroke.getInstance( 'N' );
    protected KeyStroke M = KeyStroke.getInstance( 'M' );
    protected KeyStroke alt = KeyStroke.getInstance( SWT.ALT, 0 );
    protected KeyStroke enter = KeyStroke.getInstance( KeyEvent.VK_ENTER );
    protected KeyStroke up = KeyStroke.getInstance( KeyEvent.VK_UP );
    protected KeyStroke S = KeyStroke.getInstance( 'S' );
    protected KeyStroke slash = KeyStroke.getInstance( '/' );

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
        }
        catch( Exception e )
        {
            // e.printStackTrace();
        }

        eclipse.getLiferayPerspective().activate();

        try
        {
            eclipse.showProgressView();
            eclipse.showErrorLogView().clearLogViewer();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        SWTBotPreferences.TIMEOUT = 30000;

        SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
    }

    protected static void copyFileToStartServer()
    {

        String filename = "com.liferay.ip.geocoder.internal.IPGeocoderConfiguration.cfg";

        File source = new File( liferayBundlesDir + "/" + filename );
        File dest = new File( getLiferayServerDir().toString() + "osgi/configs/" + filename );

        try
        {
            FileUtil.copyFile( source, dest );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }

    protected static boolean runAllTests()
    {
        return( "".equals( runTest ) || runTest == null );
    }

    protected boolean runTest()
    {
        String fullClassName = this.getClass().getName();

        String className = fullClassName.substring( fullClassName.lastIndexOf( '.' ) ).substring( 1 );

        return( className.equals( runTest ) );
    }

    protected static IPath getIvyCacheZip()
    {
        return getLiferayBundlesPath().append( IVY_CACHE_ZIP );
    }

    protected static IPath getLiferayBundlesPath()
    {
        if( liferayBundlesPath == null )
        {
            liferayBundlesPath = new Path( liferayBundlesDir );
        }

        return liferayBundlesPath;
    }

    protected static String getLiferayPluginServerName()
    {
        return TOMCAT_NAME;
    }

    protected static IPath getLiferayPluginsSdkDir()
    {
        return new Path( liferayBundlesDir + "/bundles/" ).append( PLUGINS_SDK_DIR );
    }

    protected static String getLiferayPluginsSdkName()
    {
        return PLUGINS_SDK_DIR;
    }

    protected static IPath getLiferayPluginsSDKZip()
    {
        return getLiferayBundlesPath().append( PLUGINS_SDK_ZIP );
    }

    protected static String getLiferayPluginsSdkZipFolder()
    {
        return PLUGINS_SDK_DIR + "/";
    }

    protected static IPath getLiferayServerDir()
    {
        return new Path( liferayBundlesDir + "/bundles/" ).append( BUNDLE_DIR + "/" );
    }

    protected static IPath getLiferayServerZip()
    {
        return getLiferayBundlesPath().append( BUNDLE_ZIP );
    }

    protected static String getLiferayServerZipFolder()
    {
        return BUNDLE_DIR + "/";
    }

    protected File getProjectZip( String bundleId, String projectName ) throws IOException
    {
        final URL projectZipUrl = Platform.getBundle( bundleId ).getEntry( "projects/" + projectName + ".zip" );

        final File projectZipFile = new File( FileLocator.toFileURL( projectZipUrl ).getFile() );
        return projectZipFile;
    }

    protected static void unzipPluginsSDK() throws IOException
    {
        FileUtil.deleteDir( getLiferayPluginsSdkDir().toFile(), true );

        assertEquals(
            "Expected file to be not exist:" + getLiferayPluginsSdkDir().toPortableString(), false,
            getLiferayPluginsSdkDir().toFile().exists() );

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

        final File ivyCacheZipFile = getIvyCacheZip().toFile();

        assertEquals(
            "Expected ivy-cache.zip to be here: " + ivyCacheZipFile.getAbsolutePath(), true, ivyCacheZipFile.exists() );

        ZipUtil.unzip( ivyCacheZipFile, liferayPluginsSdkDirFile );

        assertEquals( "Expected .ivy folder to be here: " + ivyCacheDir.getAbsolutePath(), true, ivyCacheDir.exists() );

        Map<String, String> map = System.getenv();
        String username = map.get( "USERNAME" );
        File userBuildFile = new File( liferayPluginsSdkDirFile, "build." + username + ".properties" );

        if( !userBuildFile.exists() )
        {
            userBuildFile.createNewFile();
            String appServerParentDir =
                "app.server.parent.dir=" + getLiferayServerDir().toFile().getPath().replace( "\\", "/" );
            try
            {
                FileWriter writer = new FileWriter( userBuildFile.getPath(), true );
                writer.write( appServerParentDir );
                writer.close();
            }
            catch( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

    protected static void unzipServer() throws IOException
    {
        FileUtil.deleteDir( getLiferayServerDir().toFile(), true );

        assertEquals(
            "Expected file to be not exist:" + getLiferayServerDir().toPortableString(), false,
            getLiferayServerDir().toFile().exists() );

        final File liferayServerZipFile = getLiferayServerZip().toFile();

        assertEquals(
            "Expected file to exist: " + liferayServerZipFile.getAbsolutePath(), true, liferayServerZipFile.exists() );

        final File liferayServerDirFile = getLiferayServerDir().toFile();

        liferayServerDirFile.mkdirs();

        final String liferayServerZipFolder = getLiferayServerZipFolder();

        if( CoreUtil.isNullOrEmpty( liferayServerZipFolder ) )
        {
            ZipUtil.unzip( liferayServerZipFile, liferayServerDirFile );
        }
        else
        {
            ZipUtil.unzip(
                liferayServerZipFile, liferayServerZipFolder, liferayServerDirFile, new NullProgressMonitor() );
        }

    }

    public boolean checkServerConsoleMessage( String expectedMessage, String consoleName, int timeout ) throws Exception
    {
        TextConsole console = (TextConsole) getConsole( consoleName ); // get server console

        long timeoutExpiredMs = System.currentTimeMillis() + timeout;

        while( true )
        {
            Thread.sleep( 500 );

            IDocument content = console.getDocument();

            if( content.get().contains( expectedMessage ) )
            {
                return true;
            }

            if( System.currentTimeMillis() >= timeoutExpiredMs )
            {
                return false;
            }
        }
    }

    protected IConsole getConsole( String name )
    {
        ConsolePlugin plugin = ConsolePlugin.getDefault();

        IConsoleManager conMan = plugin.getConsoleManager();

        IConsole[] existing = conMan.getConsoles();

        for( int i = 0; i < existing.length; i++ )
            if( ( existing[i].getName() ).contains( name ) )
                return existing[i];

        return null;
    }

    public void openFile( final String path ) throws Exception
    {
        Display.getDefault().syncExec( new Runnable()
        {

            public void run()
            {
                try
                {
                    File fileToOpen = new File( path );

                    if( fileToOpen.exists() && fileToOpen.isFile() )
                    {
                        IFileStore fileStore = EFS.getLocalFileSystem().getStore( fileToOpen.toURI() );
                        IWorkbenchPage page = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getPages()[0];
                        IDE.openInternalEditorOnFileStore( page, fileStore );
                    }
                }
                catch( Exception e )
                {
                    e.printStackTrace();
                }
            }
        } );

    }

    protected void sleep()
    {
        sleep( DEFAULT_SLEEP_MILLIS );
    }

    protected static void sleep( long millis )
    {
        bot.sleep( millis );
    }

    public boolean addedProjects()
    {
        eclipse.showPackageExporerView();

        return eclipse.hasProjects();
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

}

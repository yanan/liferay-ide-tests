/******************************************************************************
 * Copyright (c) 2010 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Original file was copied from
 * org.eclipse.wst.common.project.facet.core.util.internal.FileUtil
 *
 ******************************************************************************/

package com.liferay.ide.ui.tests.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.osgi.util.NLS;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

/**
 * @author Greg Amerson
 * @author Cindy Li
 * @author Simon Jiang
 */
public class FileUtil
{

    public static void copyDirectiory( String sourceDir, String targetDir ) throws IOException
    {
        ( new File( targetDir ) ).mkdirs();

        File[] file = ( new File( sourceDir ) ).listFiles();

        for( int i = 0; i < file.length; i++ )
        {
            if( file[i].isFile() )
            {
                File sourceFile = file[i];
                File targetFile =
                    new File( new File( targetDir ).getAbsolutePath() + File.separator + file[i].getName() );

                copyFile( sourceFile, targetFile );

            }

            if( file[i].isDirectory() )
            {
                String dir1 = sourceDir + "/" + file[i].getName();
                String dir2 = targetDir + "/" + file[i].getName();

                copyDirectiory( dir1, dir2 );
            }
        }
    }

    public static void copyFile( File src, File dest )
    {
        if( src == null || ( !src.exists() ) || dest == null || dest.isDirectory() )
        {
            return;
        }

        byte[] buf = new byte[4096];

        OutputStream out = null;
        FileInputStream in = null;

        try
        {
            out = new FileOutputStream( dest );
            in = new FileInputStream( src );

            int avail = in.read( buf );
            while( avail > 0 )
            {
                out.write( buf, 0, avail );
                avail = in.read( buf );
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if( in != null )
                    in.close();
            }
            catch( Exception ex )
            {
                // ignore
            }
            try
            {
                if( out != null )
                    out.close();
            }
            catch( Exception ex )
            {
                // ignore
            }
        }
    }

    public static void deleteDir( File directory, boolean removeAll )
    {
        if( directory == null || !directory.isDirectory() )
        {
            return;
        }

        for( File file : directory.listFiles() )
        {
            if( file.isDirectory() && removeAll )
            {
                deleteDir( file, removeAll );
            }
            else
            {
                file.delete();
            }
        }

        directory.delete();
    }

    public static void deleteDirContents( final File directory )
    {
        if( directory == null || !directory.isDirectory() )
        {
            return;
        }

        for( File file : directory.listFiles() )
        {
            if( file.isDirectory() )
            {
                deleteDir( file, true );
            }
            else
            {
                file.delete();
            }
        }

    }

    public static File[] getDirectories( File directory )
    {
        return directory.listFiles( new FileFilter()
        {

            public boolean accept( File file )
            {
                return file.isDirectory();
            }
        } );
    }

    public static IContainer getWorkspaceContainer( final File f )
    {
        final IWorkspace ws = ResourcesPlugin.getWorkspace();
        final IWorkspaceRoot wsroot = ws.getRoot();
        final IPath path = new Path( f.getAbsolutePath() );

        final IContainer[] wsContainers = wsroot.findContainersForLocationURI( path.toFile().toURI() );

        if( wsContainers.length > 0 )
        {
            return wsContainers[0];
        }

        return null;
    }

    public static IFile getWorkspaceFile( final File f, String expectedProjectName )
    {
        final IWorkspace ws = ResourcesPlugin.getWorkspace();
        final IWorkspaceRoot wsroot = ws.getRoot();
        final IPath path = new Path( f.getAbsolutePath() );

        final IFile[] wsFiles = wsroot.findFilesForLocationURI( path.toFile().toURI() );

        if( wsFiles.length > 0 )
        {
            for( IFile wsFile : wsFiles )
            {
                if( wsFile.getProject().getName().equals( expectedProjectName ) )
                {
                    return wsFile;
                }
            }
        }

        return null;
    }

    public static void mkdirs( final File f ) throws CoreException
    {
        if( f.exists() )
        {
            if( f.isFile() )
            {
                final String msg = NLS.bind( Msgs.locationIsFile, f.getAbsolutePath() );

            }
        }
        else
        {
            mkdirs( f.getParentFile() );

            final IContainer wsContainer = getWorkspaceContainer( f );

            if( wsContainer != null )
            {
                // Should be a folder...

                final IFolder iFolder = (IFolder) wsContainer;
                iFolder.create( true, true, null );
            }
            else
            {
                final boolean isSuccessful = f.mkdir();

                if( !isSuccessful )
                {
                    final String msg = NLS.bind( Msgs.failedToCreateDirectory, f.getAbsolutePath() );

                }
            }
        }
    }

    public static String readContents( File file )
    {
        return readContents( file, false );
    }

    public static String readContents( File file, boolean includeNewlines )
    {
        if( file == null )
        {
            return null;
        }

        if( !file.exists() )
        {
            return null;
        }

        StringBuffer contents = new StringBuffer();
        BufferedReader bufferedReader = null;

        try
        {
            FileReader fileReader = new FileReader( file );

            bufferedReader = new BufferedReader( fileReader );

            String line;

            while( ( line = bufferedReader.readLine() ) != null )
            {
                contents.append( line );

                if( includeNewlines )
                {
                    contents.append( System.getProperty( "line.separator" ) );
                }
            }
        }
        catch( Exception e )
        {
        }
        finally
        {
            if( bufferedReader != null )
            {
                try
                {
                    bufferedReader.close();
                }
                catch( IOException e )
                {
                    // best effort no need to log
                }
            }
        }

        return contents.toString();
    }

    public static String[] readLinesFromFile( File file )
    {
        if( file == null )
        {
            return null;
        }

        if( !file.exists() )
        {
            return null;
        }

        List<String> lines = new ArrayList<String>();
        BufferedReader bufferedReader = null;

        try
        {
            FileReader fileReader = new FileReader( file );

            bufferedReader = new BufferedReader( fileReader );

            String line;

            while( ( line = bufferedReader.readLine() ) != null )
            {
                lines.add( line );
            }
        }
        catch( Exception e )
        {
        }
        finally
        {
            if( bufferedReader != null )
            {
                try
                {
                    bufferedReader.close();
                }
                catch( Exception e )
                {
                    // no need to log, best effort
                }
            }
        }

        return lines.toArray( new String[lines.size()] );
    }

    public static Document readXML( InputStream inputStream, EntityResolver resolver, ErrorHandler error )
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;

        try
        {
            db = dbf.newDocumentBuilder();

            if( resolver != null )
            {
                db.setEntityResolver( resolver );
            }

            if( error != null )
            {
                db.setErrorHandler( error );
            }

            return db.parse( inputStream );
        }
        catch( Throwable SWTBot )
        {
            return null;
        }
    }

    public static Document readXML( String content )
    {
        return readXML( new ByteArrayInputStream( content.getBytes() ), null, null );
    }

    public static Document readXMLFile( File file )
    {
        return readXMLFile( file, null );
    }

    public static Document readXMLFile( File file, EntityResolver resolver )
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;

        try
        {
            db = dbf.newDocumentBuilder();

            if( resolver != null )
            {
                db.setEntityResolver( resolver );
            }

            return db.parse( file );
        }
        catch( Throwable SWTBot )
        {
            return null;
        }
    }

    public static boolean searchAndReplace( File file, String search, String replace ) throws FileNotFoundException,
        IOException
    {
        boolean replaced = false;

        if( file.exists() )
        {
            final String searchContents = CoreUtil.readStreamToString( new FileInputStream( file ) );

            final String replaceContents = searchContents.replaceAll( search, replace );

            replaced = !searchContents.equals( replaceContents );

            CoreUtil.writeStreamFromString( replaceContents, new FileOutputStream( file ) );
        }

        return replaced;
    }

    public static void validateEdit( final IFile... files ) throws CoreException
    {
        final IWorkspace ws = ResourcesPlugin.getWorkspace();
        final IStatus st = ws.validateEdit( files, IWorkspace.VALIDATE_PROMPT );

        if( st.getSeverity() == IStatus.ERROR )
        {
            throw new CoreException( st );
        }
    }

    public static String validateNewFolder( IFolder folder, String folderValue )
    {
        if( folder == null || folderValue == null )
        {
            return null;
        }

        if( CoreUtil.isNullOrEmpty( folderValue ) )
        {
            return Msgs.folderValueNotEmpty;
        }

        if( !Path.ROOT.isValidPath( folderValue ) )
        {
            return Msgs.folderValueInvalid;
        }

        IWorkspace workspace = ResourcesPlugin.getWorkspace();

        IStatus result =
            workspace.validatePath( folder.getFolder( folderValue ).getFullPath().toString(), IResource.FOLDER );

        if( !result.isOK() )
        {
            return result.getMessage();
        }

        if( folder.getFolder( new Path( folderValue ) ).exists() )
        {
            return Msgs.folderAlreadyExists;
        }

        return null;
    }

    public static int writeFileFromStream( File tempFile, InputStream in ) throws IOException
    {
        byte[] buffer = new byte[1024];

        BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream( tempFile ) );
        BufferedInputStream bin = new BufferedInputStream( in );

        int bytesRead = 0;
        int bytesTotal = 0;

        // Keep reading from the file while there is any content
        // when the end of the stream has been reached, -1 is returned
        while( ( bytesRead = bin.read( buffer ) ) != -1 )
        {
            out.write( buffer, 0, bytesRead );
            bytesTotal += bytesRead;
        }

        if( bin != null )
        {
            bin.close();
        }

        if( out != null )
        {
            out.flush();
            out.close();
        }

        return bytesTotal;
    }

    private static class Msgs extends NLS
    {

        public static String failedToCreateDirectory;
        public static String folderAlreadyExists;
        public static String folderValueInvalid;
        public static String folderValueNotEmpty;
        public static String locationIsFile;

        static
        {
            initializeMessages( FileUtil.class.getName(), Msgs.class );
        }
    }

}

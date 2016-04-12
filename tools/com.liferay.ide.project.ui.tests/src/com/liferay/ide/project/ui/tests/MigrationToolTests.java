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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.liferay.ide.project.ui.tests.page.LiferayProjectFromExistSourceWizardPO;
import com.liferay.ide.ui.tests.SWTBotBase;
import com.liferay.ide.ui.tests.swtbot.condition.ViewVisibleCondition;
import com.liferay.ide.ui.tests.swtbot.page.DialogPO;
import com.liferay.ide.ui.tests.swtbot.page.TreeItemPO;
import com.liferay.ide.ui.tests.util.CoreUtil;
import com.liferay.ide.ui.tests.util.ZipUtil;

/**
 * @author Li Lu
 */
@RunWith( SWTBotJunit4ClassRunner.class )
@Ignore
public class MigrationToolTests extends SWTBotBase implements MigrateProjectWizard
{

	String MARKER_TYPE = "com.liferay.ide.project.core.MigrationProblemMarker";
    private static final String BUNDLE_ID = "com.liferay.ide.project.ui.tests";
    private static IProject project;

    @After
    public void cleanup() throws CoreException
    {
        eclipse.getPackageExporerView().deleteResouceByName( project.getName() , true );
    }

    public void deleteMigrationMarkers( IResource resource ) throws CoreException
    {
        IMarker[] markers = resource.findMarkers( MARKER_TYPE, false, IResource.DEPTH_ZERO );

        for( IMarker marker : markers )
        {
            marker.delete();
        }
    }

    public IMarker findMigrationMarker( IResource resource, String markerMessage, boolean fullMatch )
        throws CoreException
    {
        IMarker[] markers = resource.findMarkers( MARKER_TYPE, false, IResource.DEPTH_ZERO );

        for( IMarker marker : markers )
        {
            if( fullMatch && marker.getAttribute( IMarker.MESSAGE ).toString().equals( markerMessage ) )
            {
                return marker;
            }
            else if( !fullMatch && marker.getAttribute( IMarker.MESSAGE ).toString().matches( markerMessage ) )
            {
                return marker;
            }
        }
        return null;
    }

    @Before
    public void importProject() throws IOException
    {
        File projectZipFile = getProjectZip( BUNDLE_ID, "knowledge-base-portlet" );
        IPath copyDir = getLiferayPluginsSdkDir().append( "/portlets" );

        ZipUtil.unzip( projectZipFile, copyDir.toFile() );

        eclipse.getCreateLiferayProjectToolbar().menuClick( MENU_NEW_LIFERAY_PROJECT_EXIS_SOURCE );
        LiferayProjectFromExistSourceWizardPO _wizard = new LiferayProjectFromExistSourceWizardPO( bot );
        _wizard.importProject( copyDir.append( "knowledge-base-portlet" ).toOSString() );
    }

    @Test
    public void testMigrateProjectHandlerCancelOnMenu() throws Exception
    {

       project = CoreUtil.getProject( "knowledge-base-portlet" );

        IMarker marker = findMigrationMarker( project, ".*", false );
        assertNull( marker );

        TreeItemPO projectTreeItem = eclipse.getProjectTree().getTreeItem( "knowledge-base-portlet" );

        projectTreeItem.doAction( MENU_MIGREATE_PROJECT_TO_70 );

        sleep( 500 );
        DialogPO migrateDialog = new DialogPO( bot, BUTTON_CANCEL, TITLE_FIND_MIGREATE_PROBLEMS );
        assertTrue( migrateDialog.isOpen() );

        migrateDialog.cancel();

        sleep( 1000 );
        ViewVisibleCondition showMigrationView = new ViewVisibleCondition( TITLE_PLUGIN_MIGRATION, true, false );
        assertTrue( showMigrationView.test() );

        marker = findMigrationMarker( project, ".*", false );
        assertNotNull( marker );
    }
}

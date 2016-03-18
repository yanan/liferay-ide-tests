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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Terry Jia
 */
public class LiferayUITestsPlugin extends AbstractUIPlugin implements IStartup
{

    // The plugin ID
    public static final String PLUGIN_ID = "com.liferay.ide.ui.tests";

    // The shared instance
    private static LiferayUITestsPlugin plugin;

    public static IStatus createErrorStatus( String string )
    {
        return new Status( IStatus.ERROR, PLUGIN_ID, string );
    }

    public static void logError( String msg, Exception e )
    {
        getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, msg, e));
    }

    public static IWorkbenchPage getActivePage()
    {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    }

    public static LiferayUITestsPlugin getDefault()
    {
        return plugin;
    }

    public static void logError(Exception e)
    {
        logError( e.getMessage(), e );
    }

    public LiferayUITestsPlugin()
    {
    }

    public void earlyStartup()
    {
    }

    public void start( BundleContext context ) throws Exception
    {
        super.start( context );

        plugin = this;
    }

    public void stop( BundleContext context ) throws Exception
    {
        plugin = null;

        super.stop( context );
    }

}

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

package com.liferay.ide.ui.tests.swtbot.eclipse.page;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

import com.liferay.ide.ui.tests.UIBase;
import com.liferay.ide.ui.tests.swtbot.page.DialogPO;
import com.liferay.ide.ui.tests.swtbot.page.TreePO;
import com.liferay.ide.ui.tests.swtbot.page.ViewPO;

/**
 * @author Terry Jia
 */
public class PackageExplorerViewPO extends ViewPO implements UIBase
{

    private TreePO _projectsTree;
    private DeleteResourcesDialogPO _deleteResourcesDialog;
    private DialogPO _continueDeleteResourcesDialog;

    public PackageExplorerViewPO( SWTWorkbenchBot bot )
    {
        super( bot, LABEL_PACKAGE_EXPLORER );

        _projectsTree = new TreePO( bot );
        _deleteResourcesDialog = new DeleteResourcesDialogPO( bot );
        _continueDeleteResourcesDialog = new DialogPO( bot, BUTTON_CANCEL, BUTTON_CONTINUE );
    }

    public void deleteProjectExcludeNames( String[] names )
    {
        String[] allItemNames = _projectsTree.getAllItems();

        for( String itemName : allItemNames )
        {
            for( String name : names )
            {
                if( name.equals( itemName ) )
                {
                    continue;
                }
            }

            deleteResouceByName( itemName );
        }
    }

    public void deleteResoucesByNames( String[] names )
    {
        for( String name : names )
        {
            deleteResouceByName( name );
        }
    }

    public void deleteResouceByName( String name )
    {
        _projectsTree.getTreeItem( name ).doAction( BUTTON_DELETE );

        _deleteResourcesDialog.confirmDeleteFromDisk();

        _deleteResourcesDialog.confirm();

        try
        {
            _continueDeleteResourcesDialog.confirm();
        }
        catch( Exception e )
        {
        }
    }

    public boolean hasProjects()
    {
        return _projectsTree.hasItems();
    }

}

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

package com.liferay.ide.ui.tests.swtbot.page;

import com.liferay.ide.ui.tests.UIBase;
import com.liferay.ide.ui.tests.swtbot.eclipse.page.CreateLifeayProjectToolbarDropDownButtonPO;
import com.liferay.ide.ui.tests.swtbot.eclipse.page.ErrorLogViewPO;
import com.liferay.ide.ui.tests.swtbot.eclipse.page.NewToolbarDropDownButtonPO;
import com.liferay.ide.ui.tests.swtbot.eclipse.page.PackageExplorerViewPO;
import com.liferay.ide.ui.tests.swtbot.eclipse.page.ProgressViewPO;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

/**
 * @author Terry Jia
 * @author Ying Xu
 */
public class EclipsePO extends AbstractPO implements UIBase
{

    private CreateLifeayProjectToolbarDropDownButtonPO _createLiferayProjectToolbar;
    private NewToolbarDropDownButtonPO _newToolbar;
    private PerspectivePO _liferayPerspective;
    private PackageExplorerViewPO _packageExporerView;
    private TreePO _projectTree;
    private ViewPO _welcomeView;
    private ProgressViewPO _progressView;
    private MenuPO _otherMenu;
    private ShowViewDialogPO _showViewDialog;
    private ErrorLogViewPO _errorLogView;
    private MenuPO _fileMenu;

    public EclipsePO( SWTWorkbenchBot bot )
    {
        super( bot );

        _createLiferayProjectToolbar = new CreateLifeayProjectToolbarDropDownButtonPO( bot );
        _packageExporerView = new PackageExplorerViewPO( bot );
        _welcomeView = new ViewPO( bot, LABEL_WELCOME );
        _progressView = new ProgressViewPO( bot );
        _liferayPerspective = new PerspectivePO( bot, LABEL_LIFERAY );
        _projectTree = new TreePO( bot );
        _fileMenu = new MenuPO( bot, MENU_FILE );

        String[] otherLabel = { LABEL_WINDOW, LABEL_SHOW_VIEW, LABEL_OTHER };

        _otherMenu = new MenuPO( bot, otherLabel );
        _showViewDialog = new ShowViewDialogPO( bot );
        _errorLogView = new ErrorLogViewPO( bot );
        _newToolbar = new NewToolbarDropDownButtonPO( bot );
    }
    
    public void closeShell( String title )
    {
        DialogPO shell = new DialogPO( bot, title );

        shell.closeIfOpen();
    }

    public NewToolbarDropDownButtonPO getNewToolbar()
    {
        return _newToolbar;
    }

    public MenuPO getFileMenu()
    {
        return _fileMenu;
    }

    public CreateLifeayProjectToolbarDropDownButtonPO getCreateLiferayProjectToolbar()
    {
        return _createLiferayProjectToolbar;
    }

    public PerspectivePO getLiferayPerspective()
    {
        return _liferayPerspective;
    }

    public PackageExplorerViewPO getPackageExporerView()
    {
        return _packageExporerView;
    }
    
    public TreePO getProjectTree()
    {
        return _projectTree;
    }

    public ViewPO getWelcomeView()
    {
        return _welcomeView;
    }

    public PackageExplorerViewPO showPackageExporerView()
    {
        _packageExporerView.show();

        return _packageExporerView;
    }

    public ProgressViewPO showProgressView()
    {
        try
        {
            _progressView.show();
        }
        catch( Exception e )
        {
            _otherMenu.click();

            _showViewDialog.setSearchText( LABEL_PROGRESS );

            sleep( 500 );

            _showViewDialog.confirm();

            _progressView.show();
        }

        return _progressView;
    }

    public ErrorLogViewPO showErrorLogView()
    {
        try
        {
            _errorLogView.show();
        }
        catch( Exception e )
        {
            _otherMenu.click();

            _showViewDialog.setSearchText( LABEL_ERROR_LOG );

            sleep( 500 );

            _showViewDialog.confirm();

            _errorLogView.show();
        }

        return _errorLogView;
    }

    public boolean hasProjects()
    {
        _packageExporerView.show();

        try
        {
            return _projectTree.getWidget().hasItems();
        }
        catch( Exception e )
        {
        }

        return false;
    }

    public TextEditorPO getTextEditor( String fileName )
    {
        return new TextEditorPO( bot, fileName );
    }

}

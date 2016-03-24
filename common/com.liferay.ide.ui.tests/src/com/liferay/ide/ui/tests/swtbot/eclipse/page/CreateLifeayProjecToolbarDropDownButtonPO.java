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

import org.eclipse.swtbot.swt.finder.SWTBot;

import com.liferay.ide.ui.tests.UIBase;
import com.liferay.ide.ui.tests.swtbot.page.MenuItemPO;
import com.liferay.ide.ui.tests.swtbot.page.ToolbarDropDownButtonPO;

/**
 * @author Terry Jia
 * @author Vicky Wang
 */
public class CreateLifeayProjecToolbarDropDownButtonPO extends ToolbarDropDownButtonPO implements UIBase
{

    private MenuItemPO _newLiferayPluginProject;
    private MenuItemPO _newLiferayPortlet;
    private MenuItemPO _newLiferayHookConfigration;
    private MenuItemPO _newLiferayServer;

    public CreateLifeayProjecToolbarDropDownButtonPO( SWTBot bot )
    {
        super( bot, TOOLBAR_CREATE_LIFERAY_PROJECT );

        _newLiferayPluginProject = new MenuItemPO( bot, this, LABEL_NEW_LIFERAY_PLUGIN_PROJECT );
        _newLiferayPortlet = new MenuItemPO( bot, this, LABEL_NEW_LIFERAY_PORTLET );
        _newLiferayHookConfigration = new MenuItemPO( bot, this, LABEL_NEW_LIFERAY_HOOK_CONFIGURATION );
        _newLiferayServer = new MenuItemPO( bot, this, LABEL_NEW_LIFERAY_SERVER );
    }

    public MenuItemPO getNewLiferayPluginProject()
    {
        return _newLiferayPluginProject;
    }

    public MenuItemPO getNewLiferayPortlet()
    {
        return _newLiferayPortlet;
    }

    public MenuItemPO getNewLiferayHookConfigration()
    {
        return _newLiferayHookConfigration;
    }

    public MenuItemPO getNewLiferayServer()
    {
        return _newLiferayServer;
    }

}

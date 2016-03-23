
package com.liferay.ide.ui.tests.swtbot.eclipse.page;

import org.eclipse.swtbot.swt.finder.SWTBot;

import com.liferay.ide.ui.tests.UIBase;
import com.liferay.ide.ui.tests.swtbot.page.MenuItemPO;
import com.liferay.ide.ui.tests.swtbot.page.ToolbarDropDownButtonPO;

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

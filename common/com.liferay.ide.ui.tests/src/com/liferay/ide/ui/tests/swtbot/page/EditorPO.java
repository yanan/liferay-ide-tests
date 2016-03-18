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

import com.liferay.ide.ui.tests.swtbot.condition.EditorActiveCondition;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;

/**
 * @author Terry Jia
 * @author Ashley Yuan
 */
public class EditorPO extends AbstractPO
{

    private final String name;

    public EditorPO( SWTWorkbenchBot bot, String name )
    {
        super( bot );

        this.name = name;
    }

    public void close()
    {
        getEditor().close();

        bot.waitUntil( new EditorActiveCondition( name, false ) );
    }

    protected SWTBotEditor getEditor()
    {
        return ( (SWTWorkbenchBot) bot ).editorByTitle( name );
    }

    public boolean isActive()
    {
        return getEditor().isActive();
    }

    public void save()
    {
        getEditor().save();
    }

    public void waitForPageToOpen()
    {
        bot.waitUntil( new EditorActiveCondition( name, true ) );
    }
}

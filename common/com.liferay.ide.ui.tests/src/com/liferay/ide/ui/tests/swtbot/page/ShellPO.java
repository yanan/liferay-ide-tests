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

import com.liferay.ide.ui.tests.swtbot.condition.ShellCondition;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;

/**
 * @author Terry Jia
 * @author Ashley Yuan
 * @author Ying Xu
 */
public abstract class ShellPO extends AbstractPO
{

    protected String title;
    int index = -1;

    public ShellPO( SWTBot bot, String title )
    {
        this( bot, title, 0 );
    }

    public ShellPO( SWTBot bot, String title, int index )
    {
        super( bot );

        this.title = title;
        this.index = index;
    }

    public void activate()
    {
        getShell().isActive();
    }

    protected SWTBotShell getShell()
    {
        return bot.shell( title, index );
    }

    public void waitForPageToOpen()
    {
        bot.waitUntil( new ShellCondition( title, true ) );
    }

    protected void waitForPageToClose()
    {
        bot.waitUntil( new ShellCondition( title, false ) );
    }

    public void closeIfOpen()
    {
        long oldTimeOut = SWTBotPreferences.TIMEOUT;

        SWTBotPreferences.TIMEOUT = 1000;

        try
        {
            SWTBotShell[] shells = bot.shells();

            for( SWTBotShell shell : shells )
            {
                if( shell.getText().equals( title ) )
                {
                    log.warn( "force closing of still open shell\"" + shell.getText() + "\"" );

                    shell.close();

                    bot.waitUntil( new ShellCondition( title, false ) );

                    break;
                }
            }
        }
        catch( WidgetNotFoundException e )
        {
        }
        catch( TimeoutException e )
        {
        }
        finally
        {
            SWTBotPreferences.TIMEOUT = oldTimeOut;
        }
    }

    public String getTitle()
    {
        return title;
    }

    public void setFocus()
    {
        bot.shell( title ).setFocus();
    }
}

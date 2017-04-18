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

package com.liferay.ide.server.ui.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.liferay.ide.server.ui.tests.page.NewServerPO;
import com.liferay.ide.server.ui.tests.page.NewServerRuntimeEnvPO;
import com.liferay.ide.ui.tests.SWTBotBase;

/**
 * @author Vicky Wang
 * @author Ashley Yuan
 */
public class NewServerRuntimeWizardTests extends SWTBotBase implements ServerRuntimeWizard
{

    NewServerPO newServerPage = new NewServerPO( bot );

    @BeforeClass
    public static void prepareServer() throws IOException
    {
        unzipServer();

        copyFileToStartServer();
    }

    NewServerRuntimeEnvPO setRuntimePage = new NewServerRuntimeEnvPO( bot );

    @After
    public void closeWizard()
    {
        eclipse.closeShell( TITLE_NEW_SERVER );
    }

    @Before
    public void openWizard()
    {
        Assume.assumeTrue( runTest() || runAllTests() );

        eclipse.getCreateLiferayProjectToolbar().getNewLiferayServer().click();
    }

    @Test
    public void serverRuntimeTest() throws Exception
    {

        newServerPage.getServerTypeTree().selectTreeItem( NODE_LIFERAY_INC, NODE_LIFERAY_7X );
        newServerPage.next();

        setRuntimePage.getServerLocation().setText(
            getLiferayServerDir().toString() + "/" + getLiferayPluginServerName() );

        assertEquals( "Tomcat", setRuntimePage.getPortalBundleType().getText() );

        setRuntimePage.getServerLocation().setText( getLiferayServerDir().toOSString() );

        assertEquals( "Tomcat", setRuntimePage.getPortalBundleType().getText() );

        setRuntimePage.finish();

        String serversStopped = "Liferay 7.x at localhost  [Stopped]";
        String serverStartButton = "Start the server (Ctrl+Alt+R)";

        bot.tree( 1 ).getTreeItem( serversStopped ).select();
        bot.toolbarButtonWithTooltip( serverStartButton ).click();

        sleep( 120000 );
        assertTrue( checkServerConsoleMessage( SERVER_STARTUP_MESSAGE, "Liferay", 600000 ) );
        // Future: need to add jboss test
    }
}

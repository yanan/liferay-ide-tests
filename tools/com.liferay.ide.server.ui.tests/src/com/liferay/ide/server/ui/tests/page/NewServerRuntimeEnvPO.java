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

package com.liferay.ide.server.ui.tests.page;

import org.eclipse.swtbot.swt.finder.SWTBot;

import com.liferay.ide.server.ui.tests.ServerRuntimeWizard;
import com.liferay.ide.ui.tests.swtbot.page.TextPO;
import com.liferay.ide.ui.tests.swtbot.page.WizardPO;

/**
 * @author Vicky Wang
 */
public class NewServerRuntimeEnvPO extends WizardPO implements ServerRuntimeWizard
{

    private TextPO _portalBundleType;
    private TextPO _serverLocation;

    public NewServerRuntimeEnvPO( SWTBot bot )
    {
        this( bot, TITLE_NEW_SERVER_RUNTIME_ENVIRONMENT );
    }

    public NewServerRuntimeEnvPO( SWTBot bot, String title )
    {
        super( bot, title, BUTTON_CANCEL, BUTTON_FINISH, BUTTON_BACK, BUTTON_NEXT );

        _serverLocation = new TextPO( bot, LABEL_SERVER_LOCATION );
        _portalBundleType = new TextPO( bot, LABEL_BUNDLE_TYPE );
    }

    public TextPO getPortalBundleType()
    {
        return _portalBundleType;
    }

    public TextPO getServerLocation()
    {
        return _serverLocation;
    }

}

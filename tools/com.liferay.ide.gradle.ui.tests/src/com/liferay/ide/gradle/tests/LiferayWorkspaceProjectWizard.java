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
 
package com.liferay.ide.gradle.tests;

import com.liferay.ide.ui.tests.UIBase;

/**
 * @author Vicky Wang
 */
public interface LiferayWorkspaceProjectWizard extends UIBase
{
    public final int INDEX_VALIDATION_PAGE_MESSAGE3 = 2;

    public final String LABEL_DOWNLOAD_LIFERAY_BUNDLE = "Download liferay bundle";
    public final String LABEL_SERVER_NAME = "Server name:";
    public final String LABEL_WORKSPACE_NAME = "Workspace name:";

    public final String NODE_LIFERAY_7X = "Liferay 7.x";

    public final String TEXT_ENTER_PROJECT_NAME = " Liferay Workspace name could not be null";
    public final String TEXT_INVALID_CHARACTER_IN_WORKSPACE_NAME = " is an invalid character in resource name ";
    public final String TEXT_INVALID_NAME_ON_PLATFORM = " is an invalid name on this platform.";
    public final String TEXT_INVALID_NAME_PROJECT = "The name is invalid for a project";

    public final String WINDOW_NEW_LIFERAY_WORKSPACE = "New Liferay Workspace Project";

}

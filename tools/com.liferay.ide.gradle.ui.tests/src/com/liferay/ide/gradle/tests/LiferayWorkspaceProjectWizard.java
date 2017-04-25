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
 * @author Ying Xu
 */
public interface LiferayWorkspaceProjectWizard extends UIBase
{

    public final int INDEX_VALIDATION_WORKSPACE_NAME_MESSAGE = 2;

    public final String LABEL_DOWNLOAD_LIFERAY_BUNDLE = "Download Liferay bundle";
    public final String LABEL_SERVER_NAME = "Server name:";

    public final String LABEL_USE_DEFAULT_LOCATION = "Use default location";
    public final String LABEL_LOCATIOIN = "Location:";
    public final String LABEL_BUNDLE_URL = "Bundle url:";

    public final String TEXT_WORKSPACE_NAME_COULD_NOT_EMPTY = " Liferay Workspace name could not be empty.";
    public final String TEXT_INVALID_CHARACTER_IN_WORKSPACE_NAME = " is an invalid character in resource name ";
    public final String TEXT_PLEASE_ENTER_THE_WORKSPACE_NAME = "Please enter the workspace name.";
    public final String TEXT_PLEASE_ENTER_A_PROJECT_NAME = "Please enter a project name.";
    public final String TEXT_INVALID_NAME_PROJECT = "The name is invalid for a project.";
    public final String TEXT_INVALID_BUNDLE_URL = " The bundle URL should be a vaild URL.";
    public final String TEXT_CREATE_LIFERAY_WORKSPACE = "Create a new liferay workspace";
    public final String TEXT_WORKSPACE_ALREADY_EXISTS =
        " A Liferay Workspace project already exists in this Eclipse instance.";

    public final String WINDOW_NEW_LIFERAY_WORKSPACE = "New Liferay Workspace Project";

}

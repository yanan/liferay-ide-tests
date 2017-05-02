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

    public final String TEXT_ENTER_PROJECT_NAME = " Liferay Workspace name could not be empty.";
    public final String TEXT_PLEASE_ENTER_THE_WORKSPACE_NAME = "Please enter the workspace name.";

    public final String TEXT_INVALID_NAME_PROJECT = "The name is invalid for a project.";

}

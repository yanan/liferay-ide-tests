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
 
package com.liferay.ide.module.fragment.tests;

import com.liferay.ide.ui.tests.UIBase;

/**
 * @author Vicky Wang
 */
public interface ModuleFragmentProjectWizard extends UIBase
{

    public final String BUTTON_ADD_FILES_TO_OVERRIDE = "Add Files From OSGi Bundle to Override...";
    public final String BUTTON_ADD_FILE_PATH = "Add Override File Path";
    public final String BUTTON_OSGI_BUNDLE_BROWSE = "Browse";

    public final int INDEX_VALIDATION_MESSAGE2 = 1;
    public final int INDEX_VALIDATION_PAGE_MESSAGE3 = 2;

    public final String LABEL_OSGI_BUNDLE = "Host OSGi Bundle:";
    public final String LABEL_PROJECT_NAME = "Project name:";
    public final String LABEL_RUNTIME_NAME = "Liferay runtime name:";
    public final String LABLE_SELECT_OSGI_BUNDLE = "Select Host OSGi Bundle:";
    public final String LABLE_OVERRIDDEN_FILES = "Overridden files:";
    public final String LABLE_SELECT_OVERRIDE_FILE_PATH = "Select Override File Path:";

    public final String NODE_LIFERAY_7X = "Liferay 7.x";
    public final String NODE_LIFERAY_INC = "Liferay, Inc.";

    public final String TEXT_ENTER_PROJECT_NAME = " Project name must be specified";
    public final String TEXT_INVALID_CHARACTER_IN_RESOURCE_NAME = " is an invalid character in resource name ";
    public final String TEXT_INVALID_GRADLE_PROJECT = "The project name is invalid for a gradle project";
    public final String TEXT_INVALID_NAME_ON_PLATFORM = " is an invalid name on this platform.";
    public final String TEXT_OSGI_BUNDLE_BLANK = " Host OSGi Bundle must be specified";

    public final String WINDOW_NEW_LIFERAY_MODULE_FRAGMENT = "New Liferay Module Fragment Project";

}

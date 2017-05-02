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

package com.liferay.ide.project.ui.tests;

import com.liferay.ide.ui.tests.UIBase;

/**
 * @author Ying Xu
 */
public interface NewLiferayJSFProjectWizard extends UIBase
{

    public final String LABEL_JSF_PROJECT_NAME = "Project name:";
    public final String LABEL_JSF_LOCATION = "Location";
    public final String LABEL_BUILD_FRAMEWORK = "Build Framework:";
    public final String LABEL_COMPONENT_SUITE = "Component Suite:";

    public final String CHECKBOX_USE_DEFAULT_LOCATION = "Use default location";

    public final String MENU_JSF_STANDARD = "JSF Standard";
    public final String MENU_LIFERAY_FACES_ALLOY = "Liferay Faces Alloy";
    public final String MENU_ICEFACES = "ICEFaces";
    public final String MENU_PRIMEFACES = "PrimeFaces";
    public final String MENU_RICHFFACES = "RichFaces";

    public final String TEXT_BUILD_FRAMEWORK_GRADLE = "Gradle";
    public final String TEXT_BUILD_FRAMEWORK_MAVEN = "Maven";

    public final String TEXT_ENTER_PROJECT_NAME = "Please enter a project name.";
    public final String TEXT_IS_INVALID_CHARACTER_IN_RESOURCE_NAME = " is an invalid character in resource name ";
    public final String TEXT_PROJECT_NAME_INVALID = " The project name is invalid.";
    public final String TEXT_PROJECT_NAME_MUST_SPECIFIED = " Project name must be specified";
    public final String TEXT_INVALID_NAME_ON_THIS_PLATFORM = " is an invalid name on this platform.";
    public final String TEXT_CHOOSE_TEMPLATE_FOR_NEW_JSF_PROJECT =
        "Enter a name and choose a template to use for a new Liferay JSF project.";

    public final int INDEX_NEW_JSF_PROJECT_VALIDATION_MESSAGE = 2;

    String expectedJSFProjectcomponentSuiteItems[] =
        { MENU_LIFERAY_FACES_ALLOY, MENU_ICEFACES, MENU_JSF_STANDARD, MENU_PRIMEFACES, MENU_RICHFFACES };

}

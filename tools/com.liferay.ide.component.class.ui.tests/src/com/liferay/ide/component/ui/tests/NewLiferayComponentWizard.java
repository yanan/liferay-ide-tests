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

package com.liferay.ide.component.ui.tests;

import com.liferay.ide.ui.tests.UIBase;

/**
 * @author Ying Xu
 */
public interface NewLiferayComponentWizard extends UIBase
{

    public final String LABEL_PACKAGE_NAME = "Package name:";
    public final String LABEL_COMPONENT_CLASS_NAME = "Component Class Name:";
    public final String LABEL_CHOOSE_PACKAGE = "Choose a package:";

    public final String COMBOBOX_PROJECT_NAME = "Project name:";
    public final String COMBOBOX_COMPONENT_CLASS_TEMPLATE = "Component Class Template:";

    public final String MENU_TEMPLATE_AUTH_FAILURES = "Auth Failures";
    public final String MENU_TEMPLATE_AUTH_MAX_FAILURE = "Auth Max Failure";
    public final String MENU_TEMPLATE_AUTHENTICATOR = "Authenticator";
    public final String MENU_TEMPLATE_FRIENDLY_URL_MAPPER = "Friendly URL Mapper";
    public final String MENU_TEMPLATE_GOGO_COMMAND = "GOGO Command";
    public final String MENU_TEMPLATE_INDEXER_POST_PROCESSOR = "Indexer Post Processor";
    public final String MENU_TEMPLATE_LOGIN_PRE_ACTION = "Login Pre Action";
    public final String MENU_TEMPLATE_MODEL_LISTENER = "Model Listener";
    public final String MENU_TEMPLATE_POLLER_PROCESSOR = "Poller Processor";
    public final String MENU_TEMPLATE_PORTLET = "Portlet";
    public final String MENU_TEMPLATE_PORTLET_ACTION_COMMAND = "Portlet Action Command";
    public final String MENU_TEMPLATE_PORTLET_FILTER = "Portlet Filter";
    public final String MENU_TEMPLATE_REST = "Rest";
    public final String MENU_TEMPLATE_SERVICE_WRAPPER = "Service Wrapper";
    public final String MENU_TEMPLATE_STRUTS_IN_ACTION = "Struct In Action";
    public final String MENU_TEMPLATE_STRUTS_PORTLET_ACTION = "Struts Portlet Action";

    public final String TEXT_DEFAULT_PROJECT_NAME_VALUE = "";
    public final String TEXT_DEFAULT_PACKAGE_NAME_VALUE = "";
    public final String TEXT_DEFAULT_COMPONENT_CLASS_NAME_VALUE = "";
    public final String TEXT_DEFAULT_COMPONENT_CLASS_TEMPLATE_VALUE = "Portlet";
    public final String TEXT_ENTER_PROJECT_NAME_MESSAGE = "Please enter a project name.";
    public final String TEXT_CREATE_COMPONENT_CLASS_MESSAGE = "Create a new Liferay Component Class";

    public final String TEXT_VALIDATION_PACKAGE_NAME_MESSAGE = " is not a valid Java package name";
    public final String TEXT_VALIDATION_COMPONENT_CLASS_TEMPLATE_MESSAGE = " Invalid class name";

    public final String LABEL_NEW_LIFERAY_COMPONENT = "New Liferay Component";
    public final int INDEX_VALIDATION_MESSAGE = 2;
}

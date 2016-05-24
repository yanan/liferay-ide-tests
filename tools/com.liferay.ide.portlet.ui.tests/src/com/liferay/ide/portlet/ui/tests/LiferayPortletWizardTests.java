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

package com.liferay.ide.portlet.ui.tests;

import static org.eclipse.swtbot.swt.finder.SWTBotAssert.assertContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swtbot.swt.finder.keyboard.Keyboard;
import org.eclipse.swtbot.swt.finder.keyboard.KeyboardFactory;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.liferay.ide.portlet.ui.tests.page.CreateLiferayPortletWizardPO;
import com.liferay.ide.portlet.ui.tests.page.InterfaceSelectionPO;
import com.liferay.ide.portlet.ui.tests.page.LiferayPortletDeploymentDescriptorPO;
import com.liferay.ide.portlet.ui.tests.page.ModifiersInterfacesMethodStubsPO;
import com.liferay.ide.portlet.ui.tests.page.NewSourceFolderPO;
import com.liferay.ide.portlet.ui.tests.page.PackageSelectionPO;
import com.liferay.ide.portlet.ui.tests.page.PortletDeploymentDescriptorPO;
import com.liferay.ide.portlet.ui.tests.page.SelectTypePO;
import com.liferay.ide.portlet.ui.tests.page.SuperclassSelectionPO;
import com.liferay.ide.project.ui.tests.ProjectWizard;
import com.liferay.ide.project.ui.tests.ProjectWizardTests;
import com.liferay.ide.project.ui.tests.page.CreateProjectWizardPO;
import com.liferay.ide.project.ui.tests.page.NewProjectPO;
import com.liferay.ide.project.ui.tests.page.ProjectTreePO;
import com.liferay.ide.project.ui.tests.page.SetSDKLocationPO;
import com.liferay.ide.ui.tests.SWTBotBase;
import com.liferay.ide.ui.tests.swtbot.page.DialogPO;
import com.liferay.ide.ui.tests.swtbot.page.EditorPO;
import com.liferay.ide.ui.tests.swtbot.page.SelectionDialogPO;
import com.liferay.ide.ui.tests.swtbot.page.TextEditorPO;
import com.liferay.ide.ui.tests.swtbot.page.TreeItemPO;
import com.liferay.ide.ui.tests.swtbot.page.TreePO;

/**
 * @author Ashley Yuan
 */
public class LiferayPortletWizardTests extends SWTBotBase implements LiferayPortletWizard, ProjectWizard
{

   

}

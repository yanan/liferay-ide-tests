
package com.liferay.ide.project.ui.tests;

import com.liferay.ide.ui.tests.UIBase;

public interface LiferayProjectFromExistSourceWizard extends UIBase
{

    public final String TITLE_NEW_LIFERAY_PROJECT_EXIS_SOURCE = "New Liferay Project from existing source";
    public final String MENU_LIFERAY_PROJECT_EXIS_SOURCE = "Liferay Project from Existing Source";

    public final int INDEX_DEFAULT_VALIDATION_MESSAGE = 3;

    public final String LABLE_PROJECT_DIRECTORY = "Project Directory:";
    public final String LABLE_PLUGIN_TYPE = "Plugin Type:";
    public final String LABLE_SDK_VERSION = "SDK Version:";
    public final String LABLE_BROWSE_PROJECT = "Browse";

    public final String MESSAGE_PROJECT_NAME_EXSIT = " Project name already exists.";
    public final String MESSAGE_PROJECT_HAS_DIFF_SDK = " This project has different sdk than current workspace sdk";
    public final String MESSAGE_COULD_NOT_DETER_SDK = " Could not determine SDK from project location";
    public final String MESSAGE_INVALID_PROJECT_LOCATION = " Invalid project location";
    public final String MESSAGE_DEFAULT = "Please select an existing project";

}

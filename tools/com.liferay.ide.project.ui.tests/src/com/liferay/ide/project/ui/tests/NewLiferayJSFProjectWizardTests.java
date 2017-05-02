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

import org.junit.Test;

/**
 * @author Ying Xu
 */
public class NewLiferayJSFProjectWizardTests extends AbstractNewLiferayJSFProjectWizard
    implements NewLiferayJSFProjectWizard
{

    @Test
    public void createJSFStandardProject()
    {
        String projectName = "testJSFStandardProject";

        newLiferayJSFProject( projectName, TEXT_BUILD_FRAMEWORK_GRADLE, MENU_JSF_STANDARD, TEXT_BLANK );

        String i18nPropertiesFileName = " i18n.properties";
        String facesConfigXmlFileName = "faces-config.xml";
        String buildGradleFileName = "build.gradle";
        String gradleContent = "providedCompile group: 'javax.faces', name: 'javax.faces-api', version:'2.2'";

        checkProjectAndFileExist( projectName, projectName, "src/main/resources", i18nPropertiesFileName );
        checkProjectAndFileExist(
            projectName, projectName, "src", "main", "webapp", "WEB-INF", facesConfigXmlFileName );
        checkProjectAndFileExist( projectName, projectName, buildGradleFileName );

        openEditorAndCheck( gradleContent, projectName, projectName, buildGradleFileName );
    }

    @Test
    public void createLiferayFacesAlloyProject()
    {
        String projectName = "testLiferayFacesAlloyProject";

        newLiferayJSFProject( projectName, TEXT_BUILD_FRAMEWORK_GRADLE, MENU_LIFERAY_FACES_ALLOY, TEXT_BLANK );

        String i18nPropertiesFileName = " i18n.properties";
        String facesConfigXmlFileName = "faces-config.xml";
        String buildGradleFileName = "build.gradle";
        String gradleContent = "compile group: 'com.liferay.faces', name: 'com.liferay.faces.alloy', version:'3.0.0'";

        checkProjectAndFileExist( projectName, projectName, "src/main/resources", i18nPropertiesFileName );
        checkProjectAndFileExist(
            projectName, projectName, "src", "main", "webapp", "WEB-INF", facesConfigXmlFileName );
        checkProjectAndFileExist( projectName, projectName, buildGradleFileName );

        openEditorAndCheck( gradleContent, projectName, projectName, buildGradleFileName );
    }

    @Test
    public void createICEFacesProject()
    {
        String projectName = "testICEFacesProject";

        newLiferayJSFProject( projectName, TEXT_BUILD_FRAMEWORK_GRADLE, MENU_ICEFACES, TEXT_BLANK );

        String i18nPropertiesFileName = " i18n.properties";
        String facesConfigXmlFileName = "faces-config.xml";
        String buildGradleFileName = "build.gradle";
        String gradleContent = "compile group: 'org.icefaces', name: 'icefaces-ace', version:'4.1.1'";

        checkProjectAndFileExist( projectName, projectName, "src/main/resources", i18nPropertiesFileName );
        checkProjectAndFileExist(
            projectName, projectName, "src", "main", "webapp", "WEB-INF", facesConfigXmlFileName );
        checkProjectAndFileExist( projectName, projectName, buildGradleFileName );

        openEditorAndCheck( gradleContent, projectName, projectName, buildGradleFileName );
    }

    @Test
    public void createPrimeFacesProject()
    {
        String projectName = "testPrimeFacesProject";

        newLiferayJSFProject( projectName, TEXT_BUILD_FRAMEWORK_GRADLE, MENU_PRIMEFACES, TEXT_BLANK );

        String i18nPropertiesFileName = " i18n.properties";
        String facesConfigXmlFileName = "faces-config.xml";
        String buildGradleFileName = "build.gradle";
        String gradleContent = "compile group: 'org.primefaces', name: 'primefaces', version:'6.0'";

        checkProjectAndFileExist( projectName, projectName, "src/main/resources", i18nPropertiesFileName );
        checkProjectAndFileExist(
            projectName, projectName, "src", "main", "webapp", "WEB-INF", facesConfigXmlFileName );
        checkProjectAndFileExist( projectName, projectName, buildGradleFileName );

        openEditorAndCheck( gradleContent, projectName, projectName, buildGradleFileName );
    }

    @Test
    public void createRichFacesProject()
    {
        String projectName = "testRichFacesProject";

        newLiferayJSFProject( projectName, TEXT_BUILD_FRAMEWORK_GRADLE, MENU_RICHFFACES, TEXT_BLANK );

        String i18nPropertiesFileName = " i18n.properties";
        String facesConfigXmlFileName = "faces-config.xml";
        String buildGradleFileName = "build.gradle";
        String gradleContent = "compile group: 'org.richfaces', name: 'richfaces', version:'4.5.17.Final'";

        checkProjectAndFileExist( projectName, projectName, "src/main/resources", i18nPropertiesFileName );
        checkProjectAndFileExist(
            projectName, projectName, "src", "main", "webapp", "WEB-INF", facesConfigXmlFileName );
        checkProjectAndFileExist( projectName, projectName, buildGradleFileName );

        openEditorAndCheck( gradleContent, projectName, projectName, buildGradleFileName );
    }

    @Test
    public void validateProjectName()
    {
        validationProjectName();
    }
}

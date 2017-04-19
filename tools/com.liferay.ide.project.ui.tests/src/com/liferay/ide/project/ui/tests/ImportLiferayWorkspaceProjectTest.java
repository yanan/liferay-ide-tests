
package com.liferay.ide.project.ui.tests;

import static org.eclipse.swtbot.swt.finder.SWTBotAssert.assertContains;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.liferay.ide.project.ui.tests.page.ImportLiferayWorkspaceProjectPO;
import com.liferay.ide.project.ui.tests.page.SelectTypePO;
import com.liferay.ide.ui.tests.SWTBotBase;
import com.liferay.ide.ui.tests.swtbot.eclipse.page.DeleteResourcesContinueDialogPO;
import com.liferay.ide.ui.tests.swtbot.page.CTabItemPO;
import com.liferay.ide.ui.tests.swtbot.page.TextEditorPO;
import com.liferay.ide.ui.tests.swtbot.page.TreePO;

public class ImportLiferayWorkspaceProjectTest extends SWTBotBase implements ImportLiferayWorkspaceProject
{

    private String liferayWorkspaceRootPath = System.getProperty( "user.dir" );

    TreePO projectTree = eclipse.showPackageExporerView().getProjectTree();

    ImportLiferayWorkspaceProjectPO importLiferayWorkspaceProject = new ImportLiferayWorkspaceProjectPO(
        bot, TITLE_IMPORT_LIFERAY_WORKSPACE, INDEX_PLEASE_SELECT_THE_WORKSPACE_LOCATION );

    SelectTypePO selectImportPage =
        new SelectTypePO( bot, INDEX_SELECT_IMPORT_LIFERAY_WORKSPACE_PROJECT_VALIDATION_MESSAGE );

    DeleteResourcesContinueDialogPO continueDeleteResources =
        new DeleteResourcesContinueDialogPO( bot, "Delete Resources" );

    private String invalidLocation01 = "..";
    private String invalidLocation02 = "1.*";

    @Before
    public void importWorkspaceProject()
    {
        Assume.assumeTrue( runTest() || runAllTests() );

        eclipse.getLiferayWorkspacePerspective().activate();

        importLiferayWorkspaceProject();
    }

    @After
    public void clean()
    {
        eclipse.closeShell( TITLE_IMPORT_LIFERAY_WORKSPACE );
        if( addedProjects() )
        {
            importLiferayWorkspaceProject();

            importLiferayWorkspaceProject.setWorkspaceLocation(
                liferayWorkspaceRootPath + "/projects/testGradleWorkspace" );

            sleep( 3000 );

            assertEquals(
                TEXT_A_LIFERAY_WORKSPACE_PROJECT_ALREADY_EXISTS_IN_THIS_ECLIPSE_INSTANCE,
                importLiferayWorkspaceProject.getValidationMessage() );
            importLiferayWorkspaceProject.cancel();

            eclipse.getPackageExporerView().deleteProjectExcludeNames(
                new String[] { getLiferayPluginsSdkName() }, false );
        }
    }

    public void importLiferayWorkspaceProject()
    {
        eclipse.getFileMenu().clickMenu( LABEL_IMPORT );

        selectImportPage.selectItem( "liferay", "Liferay", LABEL_IMPORT_LIFERAY_WORKSPACE_PROJECT );
        assertEquals(
            TEXT_SELECT_IMPORT_LIFERAY_WORKSPACE_PROJECT_VALIDATION_MESSAGE, selectImportPage.getValidationMessage() );
        selectImportPage.next();
    }

    @Test
    public void importGradleLiferayWorkspaceProject()
    {
        importLiferayWorkspaceProject.setWorkspaceLocation(
            liferayWorkspaceRootPath + "/projects/testGradleWorkspace" );
        sleep( 5000 );

        assertEquals( TEXT_GRADLE_LIFERAY_WORKSPACE, importLiferayWorkspaceProject.getBuildTypeText().getText() );
        assertEquals( "", importLiferayWorkspaceProject.getServerName().getText() );
        importLiferayWorkspaceProject.finish();
        sleep( 8000 );

        projectTree.setFocus();
        projectTree.expandNode( "testGradleWorksapce", "bundles" );
        projectTree.getTreeItem( "testGradleWorkspace" ).getTreeItem( "bundles" ).doAction( BUTTON_DELETE );
        continueDeleteResources.confirm();
        sleep( 3000 );

        if( addedProjects() )
        {
            eclipse.getPackageExporerView().deleteProjectExcludeNames(
                new String[] { getLiferayPluginsSdkName() }, false );
        }

        importLiferayWorkspaceProject();

        importLiferayWorkspaceProject.setWorkspaceLocation(
            liferayWorkspaceRootPath + "/projects/testGradleWorkspace" );
        sleep( 5000 );

        assertEquals(
            TEXT_SELECT_LOCATION_OF_LIFERAY_WORKSPACE_PARENT_DIRECTORY,
            importLiferayWorkspaceProject.getValidationMessage() );
        sleep( 3000 );

        assertEquals( TEXT_GRADLE_LIFERAY_WORKSPACE, importLiferayWorkspaceProject.getBuildTypeText().getText() );
        sleep();

        importLiferayWorkspaceProject.getDownloadLiferaybundle().select();
        sleep();

        assertEquals( "", importLiferayWorkspaceProject.getServerName().getText() );
        assertEquals( "", importLiferayWorkspaceProject.getBundleUrl().getText() );
        sleep();

        importLiferayWorkspaceProject.getServerName().setText( "test-lrws" );

        importLiferayWorkspaceProject.finish();
        importLiferayWorkspaceProject.waitForPageToClose();
        sleep( 8000 );

        projectTree.setFocus();
        projectTree.expandNode( "testGradleWorksapce", "bundles" );
        projectTree.expandNode( "testGradleWorksapce", "bundles" ).doAction( "delete" );
        continueDeleteResources.confirm();

        sleep( 3000 );
        projectTree.getTreeItem( "testGradleWorksapce" ).doAction( "Liferay", "Initialize Server Bundle" );

        sleep( 8000 );
        assertTrue( projectTree.expandNode( "testGradleWorksapce", "bundles" ).isVisible() );
        assertTrue( projectTree.expandNode( "testGradleWorksapce", "configs" ).isVisible() );
        assertTrue( projectTree.expandNode( "testGradleWorksapce", "gradle" ).isVisible() );

        String gradlePropertyFileName = "gradle.properties";
        String settingGradleFileName = "settings.gradle";

        projectTree.expandNode( "testGradleWorksapce", gradlePropertyFileName ).doubleClick();
        TextEditorPO gradlePropertiesFile = eclipse.getTextEditor( gradlePropertyFileName );

        assertContains( "liferay.workspace.modules.dir", gradlePropertiesFile.getText() );
        assertContains( "liferay.workspace.home.dir", gradlePropertiesFile.getText() );
        gradlePropertiesFile.close();
        sleep( 3000 );
        projectTree.expandNode( "testGradleWorksapce", settingGradleFileName ).doubleClick();
        TextEditorPO settingGradleFile = eclipse.getTextEditor( settingGradleFileName );

        assertContains( "buildscript", settingGradleFile.getText() );
        assertContains( "repositories", settingGradleFile.getText() );
        assertContains( "apply plugin:", settingGradleFile.getText() );

        settingGradleFile.close();

        sleep( 3000 );
    }

    @Test
    public void importMavenLiferayWorkspaceProject()
    {
        importLiferayWorkspaceProject.setWorkspaceLocation( invalidLocation01 );
        sleep( 2000 );
        assertEquals( TEXT_PLEASE_SELECT_THE_WORKSPACE_LOCATION, importLiferayWorkspaceProject.getValidationMessage() );

        importLiferayWorkspaceProject.setWorkspaceLocation( invalidLocation02 );
        sleep( 2000 );
        assertEquals(
            " " + "\"" + invalidLocation02 + "\"" + TEXT_IS_NOT_A_VALID_PATH,
            importLiferayWorkspaceProject.getValidationMessage() );

        importLiferayWorkspaceProject.setWorkspaceLocation( " " );
        sleep( 2000 );
        assertEquals( TEXT_WORKSPACE_LOCATION_MUST_BE_SPECIFIED, importLiferayWorkspaceProject.getValidationMessage() );

        importLiferayWorkspaceProject.setWorkspaceLocation( "F://hh" );
        sleep( 2000 );
        assertEquals( TEXT_DIRECTORY_DOESNT_EXIST, importLiferayWorkspaceProject.getValidationMessage() );

        importLiferayWorkspaceProject.setWorkspaceLocation( "D:/github" );
        sleep( 2000 );
        assertEquals( TEXT_INVALID_LIFERAY_WORKSPACE, importLiferayWorkspaceProject.getValidationMessage() );

        importLiferayWorkspaceProject.setWorkspaceLocation( liferayWorkspaceRootPath + "/projects/testMavenWorkspace" );
        sleep( 2000 );
        assertEquals(
            TEXT_SELECT_LOCATION_OF_LIFERAY_WORKSPACE_PARENT_DIRECTORY,
            importLiferayWorkspaceProject.getValidationMessage() );
        sleep( 3000 );
        assertEquals( TEXT_MAVEN_LIFERAY_WORKSPACE, importLiferayWorkspaceProject.getBuildTypeText().getText() );

        assertFalse( importLiferayWorkspaceProject.getDownloadLiferaybundle().isChecked() );

        importLiferayWorkspaceProject.finish();
        importLiferayWorkspaceProject.waitForPageToClose();

        sleep( 5000 );

        projectTree.setFocus();
        projectTree.expandNode( "testMavenWorkspace" ).doubleClick();
        assertTrue( projectTree.getTreeItem( "testMavenWorkspace-modules (in modules)" ).isVisible() );
        assertTrue( projectTree.getTreeItem( "testMavenWorkspace-themes (in themes)" ).isVisible() );
        assertTrue( projectTree.getTreeItem( "testMavenWorkspace-wars (in wars)" ).isVisible() );

        projectTree.expandNode(
            "testMavenWorkspace", "testMavenWorkspace-modules (in modules)", "pom.xml" ).doubleClick();

        CTabItemPO switchCTabItem = new CTabItemPO( bot, "pom.xml" );
        sleep();
        switchCTabItem.click();

        TextEditorPO pomXmlFileModules = eclipse.getTextEditor( "testMavenWorkspace-modules/pom.xml" );

        assertContains( "testMavenWorkspace-modules", pomXmlFileModules.getText() );
        assertContains( "artifactId", pomXmlFileModules.getText() );
        assertContains( "relativePath", pomXmlFileModules.getText() );
        assertContains( "packaging", pomXmlFileModules.getText() );

        pomXmlFileModules.close();
        sleep( 3000 );

        projectTree.expandNode( "testMavenWorkspace", "pom.xml" ).doubleClick();
        sleep();
        switchCTabItem.click();

        TextEditorPO pomXmlFile = eclipse.getTextEditor( "testMavenWorkspace/pom.xml" );

        assertContains( "testMavenWorkspace", pomXmlFile.getText() );
        assertContains( "artifactId", pomXmlFile.getText() );
        assertContains( "com.liferay", pomXmlFile.getText() );
        assertContains( "packaging", pomXmlFile.getText() );

        pomXmlFile.close();
        sleep( 3000 );

    }

}

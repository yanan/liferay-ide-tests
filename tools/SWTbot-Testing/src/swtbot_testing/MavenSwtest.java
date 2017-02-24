
package swtbot_testing;

import static org.junit.Assert.*;

import java.awt.List;
import java.util.ArrayList;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class MavenSwtest
{

    private static SWTWorkbenchBot bot;

    @BeforeClass
    public static void beforeClass()
    {
        bot = new SWTWorkbenchBot();

        bot.viewByTitle( "Welcome" ).close();
        bot.toolbarButtonWithTooltip( "Open Perspective" ).click();
        bot.table().select( "Liferay Plugins" );
        bot.button( "OK" ).click();

        bot.toolbarDropDownButtonWithTooltip( "Create a new Liferay Plugin Project" ).menuItem(
            "New Liferay Plugin Project" ).click();
    }

    public boolean matchItemInItems( String[] actualItems, String expectedItems )
    {

        for( String item : actualItems )
        {
            if( item.equals( expectedItems ) )
                return true;
        }
        return false;
    }

    @Test
    public void newMavenPortletProjectWithCreateMavenProfile()
    {

        /* 查看页面的初始状态 */

        String validationMessage = bot.text( 2 ).getText();
        assertEquals( "Please enter a project name.", validationMessage );// 校验提示
        assertEquals( "", bot.textWithLabel( "Project name:" ).getText() );
        assertEquals( "", bot.textWithLabel( "Display name:" ).getText() );

        /* 如果Build type是 Ant (liferay-plugins-sdk) */

        if( ( bot.comboBoxWithLabel( "Build type:" ).getText() ).equals( "Ant (liferay-plugins-sdk)" ) )
        {

            assertEquals( "Ant (liferay-plugins-sdk)", bot.comboBoxWithLabel( "Build type:" ).getText() );
            assertEquals( "Portlet", bot.comboBoxWithLabel( "Plugin type:" ).getText() );

            String expected_buildtype[] = { "Ant (liferay-plugins-sdk)", "Maven (liferay-maven-plugin)" };
            String expected_plugintype[] =
                { "Portlet", "Service Builder Portlet", "Hook", "Layout Template", "Theme", "Ext", "Web" };
            String actual_buildtype[] = bot.comboBoxWithLabel( "Build type:" ).items();
            String actual_plugintype[] = bot.comboBoxWithLabel( "Plugin type:" ).items();

            assertEquals( expected_plugintype.length, actual_plugintype.length );
            for( String expected_b : expected_buildtype )
            {
                assertTrue( matchItemInItems( actual_buildtype, expected_b ) );
            }
            assertEquals( expected_buildtype.length, actual_buildtype.length );
            for( String expected_p : expected_plugintype )
            {
                assertTrue( matchItemInItems( actual_plugintype, expected_p ) );
            }

            assertTrue( bot.checkBox( "Include sample code" ).isChecked() );
            assertFalse( bot.checkBox( "Launch New Portlet Wizard after project is created" ).isChecked() );
            assertFalse( bot.checkBox( "Add project to working set" ).isChecked() );

            assertFalse( bot.button( "< Back" ).isEnabled() );
            assertFalse( bot.button( "Next >" ).isEnabled() );
            assertFalse( bot.button( "Finish" ).isEnabled() );
            assertTrue( bot.button( "Cancel" ).isEnabled() );

            // 输入项目名称之后
            bot.textWithLabel( "Project name:" ).setText( "CreateMavenProjectTesting" );// 输入项目名称
            bot.sleep( 2000 );
            String validationMessage1 = bot.text( 2 ).getText();
            assertEquals( "Create a new project configured as a Liferay plugin", validationMessage1 );
            // next/Cancel button is enabled

            assertFalse( bot.button( "< Back" ).isEnabled() );
            assertTrue( bot.button( "Next >" ).isEnabled() );
            assertFalse( bot.button( "Finish" ).isEnabled() );
            assertTrue( bot.button( "Cancel" ).isEnabled() );

            /* 选择maven项目之后，finish button availible */

            bot.comboBoxWithLabel( "Build type:" ).setSelection( "Maven (liferay-maven-plugin)" );// 选择Maven项目
            assertFalse( bot.button( "< Back" ).isEnabled() );
            assertTrue( bot.button( "Next >" ).isEnabled() );
            bot.sleep( 2000 );
            assertTrue( bot.button( "Finish" ).isEnabled() );
            assertTrue( bot.button( "Cancel" ).isEnabled() );

            assertEquals( "", bot.textWithLabel( "Artifact version:" ).getText() );
            assertEquals( "", bot.textWithLabel( "Group id:" ).getText() );
            assertEquals( "", bot.textWithLabel( "Active profiles:" ).getText() );

            /* create new maven profile */

            bot.toolbarButtonWithTooltip( "Create New Maven Profile Based on Liferay Runtime..." ).click();// =

            /* 校验初始值 */

            bot.comboBoxWithLabel( "Liferay runtime:" ).setSelection( "<None>" );
            assertEquals( "", bot.textWithLabel( "New profile id:" ).getText() );
            assertEquals( "", bot.comboBoxWithLabel( "Liferay version:" ).getText() );
            assertTrue( bot.radio( "Project pom.xml" ).isSelected() );
            assertFalse( bot.radio( "User settings at ${user.home}/.m2/settings.xml" ).isSelected() );

            bot.toolbarButtonWithTooltip( "New Liferay Runtime..." ).click();
            bot.tree().getTreeItem( "Liferay, Inc." ).getNode( "Liferay v6.2 CE (Tomcat 7)" ).select();
            bot.button( "Next >" ).click();

            bot.textWithLabel( "Liferay Tomcat directory" ).setText(
                "D:\\6.2\\liferay-portal-tomcat-6.2-ce-ga6-20160112152609836\\liferay-portal-6.2-ce-ga6" );

            bot.button( "Next >" ).click();
            bot.button( "Finish" ).click();
            assertEquals( "", bot.textWithLabel( "New profile id:" ).getText() );
            bot.comboBoxWithLabel( "Liferay version:" ).setSelection( "6.2.5" );

            bot.button( "OK" ).click();
            bot.textWithLabel( "Active profiles:" ).setText( "Liferay-v6.2-CE-(Tomcat-7)" );

            bot.toolbarButtonWithTooltip( "Select Active Profiles..." ).click();
            bot.button( "OK" ).click();
            bot.button( "Next >" ).click();
            /* 查看next page */
            bot.radio( "Liferay MVC" ).isSelected();
            assertTrue( bot.radio( "Liferay MVC" ).isSelected() );
            assertFalse( bot.radio( "JSF 2.x" ).isSelected() );
            assertFalse( bot.radio( "Spring MVC" ).isSelected() );
            assertFalse( bot.radio( "Vaadin" ).isSelected() );

            assertEquals( "", bot.textWithLabel( "Portlet name:" ).getText() );
            assertEquals( "", bot.textWithLabel( "Display name:" ).getText() );
            assertEquals( "", bot.textWithLabel( "Archetype:" ).getText() );

            assertTrue( bot.button( "< Back" ).isEnabled() );
            assertFalse( bot.button( "Next >" ).isEnabled() );
            assertTrue( bot.button( "Finish" ).isEnabled() );
            assertTrue( bot.button( "Cancel" ).isEnabled() );
            bot.button( "Finish" ).click();
            bot.sleep( 2000 );
            // finish之后check生成的java文件
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).expand();
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "pom.xml" ).select();
            assertTrue( bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "pom.xml" ).isVisible() );
            bot.sleep( 2000 );
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).expand();
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "src" ).expand();
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "src" ).getNode( "main" ).expand();
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "src" ).getNode( "main" ).getNode(
                "webapp" ).expand();
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "src" ).getNode( "main" ).getNode(
                "webapp" ).getNode( "view.jsp" ).select();
            assertTrue(
                bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "src" ).getNode( "main" ).getNode(
                    "webapp" ).getNode( "view.jsp" ).isVisible() );
        }
        else
        {
            assertEquals( "Maven (liferay-maven-plugin)", bot.comboBoxWithLabel( "Build type:" ).getText() );
            assertEquals( "", bot.textWithLabel( "Artifact version:" ).getText() );
            assertEquals( "", bot.textWithLabel( "Group id:" ).getText() );
            assertEquals( "", bot.textWithLabel( "Active profiles:" ).getText() );
            assertTrue( bot.checkBox( "Use default location" ).isChecked() );

            assertEquals( "Portlet", bot.comboBoxWithLabel( "Plugin type:" ).getText() );

            assertTrue( bot.checkBox( "Include sample code" ).isChecked() );
            assertFalse( bot.checkBox( "Launch New Portlet Wizard after project is created" ).isChecked() );
            assertFalse( bot.checkBox( "Add project to working set" ).isChecked() );

            assertFalse( bot.button( "< Back" ).isEnabled() );
            assertFalse( bot.button( "Next >" ).isEnabled() );
            assertFalse( bot.button( "Finish" ).isEnabled() );
            assertTrue( bot.button( "Cancel" ).isEnabled() );

            // 输入项目名称之后
            bot.textWithLabel( "Project name:" ).setText( "CreateMavenProjectTesting" );// 输入项目名称
            bot.sleep( 2000 );
            String validationMessage1 = bot.text( 2 ).getText();
            assertEquals( "Create a new project configured as a Liferay plugin", validationMessage1 );
            // next/finish button is enabled
            assertFalse( bot.button( "< Back" ).isEnabled() );
            assertTrue( bot.button( "Next >" ).isEnabled() );
            assertTrue( bot.button( "Finish" ).isEnabled() );
            assertTrue( bot.button( "Cancel" ).isEnabled() );
            // bot.button("Next >").click();

            bot.toolbarButtonWithTooltip( "Create New Maven Profile Based on Liferay Runtime..." ).click();//

            // Create new Maven Profile 初始界面

            assertEquals( "<None>", bot.comboBoxWithLabel( "Liferay runtime:" ).getText() );
            assertEquals( "", bot.textWithLabel( "New profile id:" ).getText() );
            assertEquals( "", bot.comboBoxWithLabel( "Liferay version:" ).getText() );
            assertTrue( bot.radio( "Project pom.xml" ).isSelected() );
            assertFalse( bot.radio( "User settings at ${user.home}/.m2/settings.xml" ).isSelected() );

            // 选择Liferay Runtime

            bot.toolbarButtonWithTooltip( "New Liferay Runtime..." ).click();
            bot.tree().getTreeItem( "Liferay, Inc." ).getNode( "Liferay v6.2 CE (Tomcat 7)" ).select();
            bot.button( "Next >" ).click();

            bot.textWithLabel( "Liferay Tomcat directory" ).setText(
                "D:\\6.2\\liferay-portal-tomcat-6.2-ce-ga6-20160112152609836\\liferay-portal-6.2-ce-ga6" );

            bot.button( "Next >" ).click();
            bot.button( "Finish" ).click();
            assertEquals( "", bot.textWithLabel( "New profile id:" ).getText() );
            assertEquals( "6.2.5", bot.comboBoxWithLabel( "Liferay version:" ).getText() );
            bot.button( "OK" ).click();

            // bot.textWithLabel("Active profiles:").setText("Liferay-v6.2-CE-(Tomcat-7)");
            assertEquals( "Liferay-v6.2-CE-(Tomcat-7)", bot.textWithLabel( "Active profiles:" ).getText() );
            bot.toolbarButtonWithTooltip( "Select Active Profiles..." ).click();
            bot.button( "OK" ).click();
            bot.button( "Next >" ).click();
            /* 查看next page */
            bot.radio( "Liferay MVC" ).isSelected();
            assertTrue( bot.radio( "Liferay MVC" ).isSelected() );
            assertFalse( bot.radio( "JSF 2.x" ).isSelected() );
            assertFalse( bot.radio( "Spring MVC" ).isSelected() );
            assertFalse( bot.radio( "Vaadin" ).isSelected() );

            assertEquals( "", bot.textWithLabel( "Portlet name:" ).getText() );
            assertEquals( "", bot.textWithLabel( "Display name:" ).getText() );
            assertEquals( "", bot.textWithLabel( "Archetype:" ).getText() );

            assertTrue( bot.button( "< Back" ).isEnabled() );
            assertFalse( bot.button( "Next >" ).isEnabled() );
            assertTrue( bot.button( "Finish" ).isEnabled() );
            assertTrue( bot.button( "Cancel" ).isEnabled() );
            bot.button( "Finish" ).click();

            // finish之后check生成的java文件
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).expand();
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "pom.xml" ).select();
            assertTrue( bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "pom.xml" ).isVisible() );
            bot.sleep( 2000 );
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).expand();
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "src" ).expand();
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "src" ).getNode( "main" ).expand();
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "src" ).getNode( "main" ).getNode(
                "webapp" ).expand();
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "src" ).getNode( "main" ).getNode(
                "webapp" ).getNode( "view.jsp" ).select();
            assertTrue(
                bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "src" ).getNode( "main" ).getNode(
                    "webapp" ).getNode( "view.jsp" ).isVisible() );
        }
    }

    @Ignore
    @Test
    public void newMavenProjectWithLaunchAndWithoutCreateMavenProfile()
    {

        /* 校验初始页面 */
        String validationMessage = bot.text( 2 ).getText();
        assertEquals( "Please enter a project name.", validationMessage );// 校验提示
        assertEquals( "", bot.textWithLabel( "Project name:" ).getText() );
        assertEquals( "", bot.textWithLabel( "Display name:" ).getText() );

        /* 如果Build type是 Ant (liferay-plugins-sdk) */

        if( ( bot.comboBoxWithLabel( "Build type:" ).getText() ).equals( "Ant (liferay-plugins-sdk)" ) )
        {

            assertEquals( "Ant (liferay-plugins-sdk)", bot.comboBoxWithLabel( "Build type:" ).getText() );
            assertEquals( "Portlet", bot.comboBoxWithLabel( "Plugin type:" ).getText() );

            assertTrue( bot.checkBox( "Include sample code" ).isChecked() );
            assertFalse( bot.checkBox( "Launch New Portlet Wizard after project is created" ).isChecked() );
            assertFalse( bot.checkBox( "Add project to working set" ).isChecked() );

            assertFalse( bot.button( "< Back" ).isEnabled() );
            assertFalse( bot.button( "Next >" ).isEnabled() );
            assertFalse( bot.button( "Finish" ).isEnabled() );
            assertTrue( bot.button( "Cancel" ).isEnabled() );

            /* 输入项目名 */

            bot.textWithLabel( "Project name:" ).setText( "CreateMavenProjectTesting" );// 输入项目名称
            bot.sleep( 2000 );
            String validationMessage1 = bot.text( 2 ).getText();
            assertEquals( "Create a new project configured as a Liferay plugin", validationMessage1 );
            bot.checkBox( "Launch New Portlet Wizard after project is created" ).click();
            assertFalse( bot.button( "< Back" ).isEnabled() );
            assertTrue( bot.button( "Next >" ).isEnabled() );
            assertFalse( bot.button( "Finish" ).isEnabled() );
            assertTrue( bot.button( "Cancel" ).isEnabled() );

            /* 选择maven */

            bot.comboBoxWithLabel( "Build type:" ).setSelection( "Maven (liferay-maven-plugin)" );// 选择Maven项目
            assertFalse( bot.button( "< Back" ).isEnabled() );
            assertTrue( bot.button( "Next >" ).isEnabled() );
            bot.sleep( 2000 );
            assertTrue( bot.button( "Finish" ).isEnabled() );
            assertTrue( bot.button( "Cancel" ).isEnabled() );

            assertEquals( "", bot.textWithLabel( "Artifact version:" ).getText() );
            assertEquals( "", bot.textWithLabel( "Group id:" ).getText() );
            assertEquals( "", bot.textWithLabel( "Active profiles:" ).getText() );

            /* do not create new maven profiles on */

            bot.toolbarButtonWithTooltip( "Select Active Profiles..." ).click();
            bot.button( "OK" ).click();
            bot.button( "Next >" ).click();

            /* check nextpage */

            bot.radio( "Liferay MVC" ).isSelected();
            assertTrue( bot.radio( "Liferay MVC" ).isSelected() );
            assertFalse( bot.radio( "JSF 2.x" ).isSelected() );
            assertFalse( bot.radio( "Spring MVC" ).isSelected() );
            assertFalse( bot.radio( "Vaadin" ).isSelected() );

            assertEquals( "", bot.textWithLabel( "Portlet name:" ).getText() );
            assertEquals( "", bot.textWithLabel( "Display name:" ).getText() );
            assertEquals( "", bot.textWithLabel( "Archetype:" ).getText() );

            assertTrue( bot.button( "< Back" ).isEnabled() );
            assertFalse( bot.button( "Next >" ).isEnabled() );
            assertTrue( bot.button( "Finish" ).isEnabled() );
            assertTrue( bot.button( "Cancel" ).isEnabled() );
            bot.button( "Finish" ).click();
            bot.sleep( 2000 );

            bot.comboBoxWithLabel( "Superclass:" ).setSelection( "javax.portlet.GenericPortlet" );
            bot.button( "Finish" ).click();
            // finish之后check生成的java文件
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).expand();
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "pom.xml" ).select();
            assertTrue( bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "pom.xml" ).isVisible() );
            bot.sleep( 2000 );

            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).expand();
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "src" ).expand();
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "src" ).getNode( "main" ).expand();
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "src" ).getNode( "main" ).getNode(
                "webapp" ).expand();
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "src" ).getNode( "main" ).getNode(
                "webapp" ).getNode( "view.jsp" ).select();
            assertTrue(
                bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "src" ).getNode( "main" ).getNode(
                    "webapp" ).getNode( "view.jsp" ).isVisible() );
        }

        else
        {
            assertEquals( "Maven (liferay-maven-plugin)", bot.comboBoxWithLabel( "Build type:" ).getText() );

            assertEquals( "", bot.textWithLabel( "Artifact version:" ).getText() );
            assertEquals( "", bot.textWithLabel( "Group id:" ).getText() );
            assertEquals( "", bot.textWithLabel( "Active profiles:" ).getText() );
            assertTrue( bot.checkBox( "Use default location" ).isChecked() );

            assertEquals( "Portlet", bot.comboBoxWithLabel( "Plugin type:" ).getText() );

            assertTrue( bot.checkBox( "Include sample code" ).isChecked() );
            assertFalse( bot.checkBox( "Launch New Portlet Wizard after project is created" ).isChecked() );
            assertFalse( bot.checkBox( "Add project to working set" ).isChecked() );

            assertFalse( bot.button( "< Back" ).isEnabled() );
            assertFalse( bot.button( "Next >" ).isEnabled() );
            assertFalse( bot.button( "Finish" ).isEnabled() );
            assertTrue( bot.button( "Cancel" ).isEnabled() );

            // 输入项目名称之后
            bot.textWithLabel( "Project name:" ).setText( "CreateMavenProjectTesting" );// 输入项目名称
            String validationMessage1 = bot.text( 2 ).getText();
            assertEquals( "Create a new project configured as a Liferay plugin", validationMessage1 );
            // next/finish button is enabled
            assertFalse( bot.button( "< Back" ).isEnabled() );
            assertTrue( bot.button( "Next >" ).isEnabled() );
            assertTrue( bot.button( "Finish" ).isEnabled() );
            assertTrue( bot.button( "Cancel" ).isEnabled() );
            // bot.button("Next >").click();

            bot.checkBox( "Launch New Portlet Wizard after project is created" ).click();

            bot.button( "Next >" ).click();
            /* 查看next page */
            bot.radio( "Liferay MVC" ).isSelected();
            assertTrue( bot.radio( "Liferay MVC" ).isSelected() );
            assertFalse( bot.radio( "JSF 2.x" ).isSelected() );
            assertFalse( bot.radio( "Spring MVC" ).isSelected() );
            assertFalse( bot.radio( "Vaadin" ).isSelected() );

            assertEquals( "", bot.textWithLabel( "Portlet name:" ).getText() );
            assertEquals( "", bot.textWithLabel( "Display name:" ).getText() );
            assertEquals( "", bot.textWithLabel( "Archetype:" ).getText() );

            assertTrue( bot.button( "< Back" ).isEnabled() );
            assertFalse( bot.button( "Next >" ).isEnabled() );
            assertTrue( bot.button( "Finish" ).isEnabled() );
            assertTrue( bot.button( "Cancel" ).isEnabled() );
            bot.button( "Finish" ).click();
            bot.sleep( 2000 );
            bot.comboBoxWithLabel( "Superclass:" ).setSelection( "javax.portlet.GenericPortlet" );
            bot.button( "Finish" ).click();
            // finish之后check生成的java文件
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).expand();
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "pom.xml" ).select();
            assertTrue( bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "pom.xml" ).isVisible() );

            bot.sleep( 2000 );
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).expand();
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "src" ).expand();
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "src" ).getNode( "main" ).expand();
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "src" ).getNode( "main" ).getNode(
                "webapp" ).expand();
            bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "src" ).getNode( "main" ).getNode(
                "webapp" ).getNode( "view.jsp" ).select();
            assertTrue(
                bot.tree().getTreeItem( "CreateMavenProjectTesting" ).getNode( "src" ).getNode( "main" ).getNode(
                    "webapp" ).getNode( "view.jsp" ).isVisible() );

        }
    }

    @AfterClass
    public static void afterClass()
    {
        bot.sleep( 5000 );
    }

}

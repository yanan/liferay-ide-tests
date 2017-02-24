
package swtbot_testing;

import static org.junit.Assert.*;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MavenSwtest implements MavenInterface
{

    public static SWTWorkbenchBot bot = new SWTWorkbenchBot();

    public static boolean matchItemInItems( String[] actualItems, String expectedItems )
    {
        for( String item : actualItems )
        {
            if( item.equals( expectedItems ) )
                return true;
        }
        return false;
    }

    @BeforeClass
    public static void beforeClass()
    {

        bot.viewByTitle( PAGE_WELCOME ).close();
        bot.toolbarButtonWithTooltip( TOOLBAR_OPRN_PER ).click();
        bot.table().select( "Liferay Plugins" );
        bot.button( BUTTON_OK ).click();
        bot.toolbarDropDownButtonWithTooltip( TOOLTIP_CREATE_PLUGIN_PROJECT ).menuItem(
            "New Liferay Plugin Project" ).click();

        /* check the initial page */

        String validationMessage = bot.text( 2 ).getText();
        assertEquals( VALIDATE_PLS_ENTER_PROJECT_NAME, validationMessage );
        assertEquals( "", bot.textWithLabel( LABEL_PROJECT_NAME ).getText() );
        assertEquals( "", bot.textWithLabel( LABEL_DISPLAY_NAME ).getText() );

        String expected_buildtype[] = { COMBO_ANT, COMBO_MAVEN };
        String expected_plugintype[] =
            { COMBO_PORTLET, "Service Builder Portlet", "Hook", "Layout Template", "Theme", "Ext", "Web" };
        String actual_buildtype[] = bot.comboBoxWithLabel( LABEL_BUILD_TYPE ).items();
        String actual_plugintype[] = bot.comboBoxWithLabel( LABEL_PLUGIN_TYPE ).items();

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
    }

    @Test
    public void newMavenPortletProjectWithCreateMavenProfile()
    {

        /* check if the Build type is Ant (liferay-plugins-sdk) or Maven (liferay-maven-plugin) */

        if( ( bot.comboBoxWithLabel( LABEL_BUILD_TYPE ).getText() ).equals( COMBO_ANT ) )
        {
            assertEquals( COMBO_ANT, bot.comboBoxWithLabel( LABEL_BUILD_TYPE ).getText() );
            assertEquals( COMBO_PORTLET, bot.comboBoxWithLabel( LABEL_PLUGIN_TYPE ).getText() );
        }

        else
        {
            assertEquals( COMBO_MAVEN, bot.comboBoxWithLabel( LABEL_BUILD_TYPE ).getText() );
            assertEquals( "", bot.textWithLabel( LABEL_ARTIFACT_VERSION ).getText() );
            assertEquals( "", bot.textWithLabel( LABEL_GROUP_ID ).getText() );
            assertEquals( "", bot.textWithLabel( LABEL_ACTIVE_PROFILES ).getText() );
            assertTrue( bot.checkBox( CHECKBOX_USE_DEFAULT_LOCATION ).isChecked() );
        }

        assertTrue( bot.checkBox( "Include sample code" ).isChecked() );
        assertFalse( bot.checkBox( "Launch New Portlet Wizard after project is created" ).isChecked() );
        assertFalse( bot.checkBox( "Add project to working set" ).isChecked() );

        assertFalse( bot.button( BUTTON_BACK ).isEnabled() );
        assertFalse( bot.button( BUTTON_NEXT ).isEnabled() );
        assertFalse( bot.button( BUTTON_FINISH ).isEnabled() );
        assertTrue( bot.button( BUTTON_CANCEL ).isEnabled() );

        /* Input the project name */

        bot.textWithLabel( LABEL_PROJECT_NAME ).setText( TEXT_PROJECT_NAME );
        bot.sleep( 2000 );
        String validationMessage1 = bot.text( 2 ).getText();
        assertEquals( "Create a new project configured as a Liferay plugin", validationMessage1 );

        /* check if the next/Cancel button is enabled */

        assertFalse( bot.button( BUTTON_BACK ).isEnabled() );
        assertTrue( bot.button( BUTTON_NEXT ).isEnabled() );
        assertFalse( bot.button( BUTTON_FINISH ).isEnabled() );
        assertTrue( bot.button( BUTTON_CANCEL ).isEnabled() );

        /* After select the maven project ,finish button is enabled */

        bot.comboBoxWithLabel( LABEL_BUILD_TYPE ).setSelection( COMBO_MAVEN );
        assertFalse( bot.button( BUTTON_BACK ).isEnabled() );
        assertTrue( bot.button( BUTTON_NEXT ).isEnabled() );
        bot.sleep( 2000 );
        assertTrue( bot.button( BUTTON_FINISH ).isEnabled() );
        assertTrue( bot.button( BUTTON_CANCEL ).isEnabled() );

        assertEquals( "", bot.textWithLabel( LABEL_ARTIFACT_VERSION ).getText() );
        assertEquals( "", bot.textWithLabel( LABEL_GROUP_ID ).getText() );
        assertEquals( "", bot.textWithLabel( LABEL_ACTIVE_PROFILES ).getText() );

        assertTrue( bot.checkBox( CHECKBOX_USE_DEFAULT_LOCATION ).isChecked() );

        /* create new maven profile */

        bot.toolbarButtonWithTooltip( "Create New Maven Profile Based on Liferay Runtime..." ).click();

        /* select the Liferay runtime */

        bot.comboBoxWithLabel( LABEL_LIFERAY_RUNTIME ).setSelection( "<None>" );
        assertEquals( "", bot.textWithLabel( LABEL_NEW_PROFILE_ID ).getText() );
        assertEquals( "", bot.comboBoxWithLabel( LABEL_LIFERAY_VERSION ).getText() );
        assertTrue( bot.radio( "Project pom.xml" ).isSelected() );
        assertFalse( bot.radio( "User settings at ${user.home}/.m2/settings.xml" ).isSelected() );

        bot.toolbarButtonWithTooltip( "New Liferay Runtime..." ).click();
        bot.tree().getTreeItem( "Liferay, Inc." ).getNode( "Liferay v6.2 CE (Tomcat 7)" ).select();
        bot.button( BUTTON_NEXT ).click();

        bot.textWithLabel( "Liferay Tomcat directory" ).setText(
            "D:\\6.2\\liferay-portal-tomcat-6.2-ce-ga6-20160112152609836\\liferay-portal-6.2-ce-ga6" );

        bot.button( BUTTON_NEXT ).click();
        bot.button( BUTTON_FINISH ).click();
        assertEquals( "", bot.textWithLabel( LABEL_NEW_PROFILE_ID ).getText() );
        bot.comboBoxWithLabel( LABEL_LIFERAY_VERSION ).setSelection( "6.2.5" );

        bot.button( BUTTON_OK ).click();
        bot.textWithLabel( LABEL_ACTIVE_PROFILES ).setText( "Liferay-v6.2-CE-(Tomcat-7)" );

        bot.toolbarButtonWithTooltip( "Select Active Profiles..." ).click();
        bot.button( BUTTON_OK ).click();
        bot.button( BUTTON_NEXT ).click();

        /*
         * check the next page (Choose from available portlet frameworks depending on which technology is most
         * appropriate for this project.)
         */

        assertTrue( bot.radio( RADIO_LIFERAY_MVC ).isSelected() );
        assertFalse( bot.radio( RADIO_JSF_2X ).isSelected() );
        assertFalse( bot.radio( RADIO_SPRING_MVC ).isSelected() );
        assertFalse( bot.radio( RADIO_VAADIN ).isSelected() );

        assertEquals( "", bot.textWithLabel( LABEL_PORTLET_NAME ).getText() );
        assertEquals( "", bot.textWithLabel( LABEL_DISPLAY_NAME ).getText() );
        assertEquals( "", bot.textWithLabel( LABEL_ARCHETYPE ).getText() );

        assertTrue( bot.button( BUTTON_BACK ).isEnabled() );
        assertFalse( bot.button( BUTTON_NEXT ).isEnabled() );
        assertTrue( bot.button( BUTTON_FINISH ).isEnabled() );
        assertTrue( bot.button( BUTTON_CANCEL ).isEnabled() );
        bot.button( BUTTON_FINISH ).click();
        bot.sleep( 2000 );

        /* After created the project .check the java and xml files */

        bot.tree().getTreeItem( TEXT_PROJECT_NAME ).expand();
        bot.tree().getTreeItem( TEXT_PROJECT_NAME ).getNode( "pom.xml" ).select();
        assertTrue( bot.tree().getTreeItem( TEXT_PROJECT_NAME ).getNode( "pom.xml" ).isVisible() );
        bot.sleep( 2000 );
        bot.tree().getTreeItem( TEXT_PROJECT_NAME ).expand();
        bot.tree().getTreeItem( TEXT_PROJECT_NAME ).getNode( "src" ).expand();
        bot.tree().getTreeItem( TEXT_PROJECT_NAME ).getNode( "src" ).getNode( "main" ).expand();
        bot.tree().getTreeItem( TEXT_PROJECT_NAME ).getNode( "src" ).getNode( "main" ).getNode( "webapp" ).expand();
        bot.tree().getTreeItem( TEXT_PROJECT_NAME ).getNode( "src" ).getNode( "main" ).getNode( "webapp" ).getNode(
            "view.jsp" ).select();
        assertTrue(
            bot.tree().getTreeItem( TEXT_PROJECT_NAME ).getNode( "src" ).getNode( "main" ).getNode( "webapp" ).getNode(
                "view.jsp" ).isVisible() );
    }

    @Test
    public void newMavenProjectWithLaunchAndWithoutCreateMavenProfile()
    {

        /* check if the Build type is Ant (liferay-plugins-sdk) or Maven (liferay-maven-plugin) */

        if( ( bot.comboBoxWithLabel( LABEL_BUILD_TYPE ).getText() ).equals( COMBO_ANT ) )
        {
            assertEquals( COMBO_ANT, bot.comboBoxWithLabel( LABEL_BUILD_TYPE ).getText() );
            assertEquals( COMBO_PORTLET, bot.comboBoxWithLabel( LABEL_PLUGIN_TYPE ).getText() );
        }
        else
        {
            assertEquals( COMBO_MAVEN, bot.comboBoxWithLabel( LABEL_BUILD_TYPE ).getText() );
            assertEquals( "", bot.textWithLabel( LABEL_ARTIFACT_VERSION ).getText() );
            assertEquals( "", bot.textWithLabel( LABEL_GROUP_ID ).getText() );
            assertEquals( "", bot.textWithLabel( LABEL_ACTIVE_PROFILES ).getText() );
            assertTrue( bot.checkBox( CHECKBOX_USE_DEFAULT_LOCATION ).isChecked() );
        }

        assertEquals( COMBO_PORTLET, bot.comboBoxWithLabel( LABEL_PLUGIN_TYPE ).getText() );

        assertTrue( bot.checkBox( "Include sample code" ).isChecked() );
        assertFalse( bot.checkBox( "Launch New Portlet Wizard after project is created" ).isChecked() );
        assertFalse( bot.checkBox( "Add project to working set" ).isChecked() );

        assertFalse( bot.button( BUTTON_BACK ).isEnabled() );
        assertFalse( bot.button( BUTTON_NEXT ).isEnabled() );
        assertFalse( bot.button( BUTTON_FINISH ).isEnabled() );
        assertTrue( bot.button( BUTTON_CANCEL ).isEnabled() );

        /* Input the project name */

        bot.textWithLabel( LABEL_PROJECT_NAME ).setText( TEXT_PROJECT_NAME );
        bot.sleep( 2000 );
        String validationMessage1 = bot.text( 2 ).getText();
        assertEquals( "Create a new project configured as a Liferay plugin", validationMessage1 );

        /* After input the project name ,next/finish button is enabled */

        assertFalse( bot.button( BUTTON_BACK ).isEnabled() );
        assertTrue( bot.button( BUTTON_NEXT ).isEnabled() );
        bot.sleep( 2000 );
        assertFalse( bot.button( BUTTON_FINISH ).isEnabled() );
        assertTrue( bot.button( BUTTON_CANCEL ).isEnabled() );

        /* select the Maven project */

        bot.comboBoxWithLabel( LABEL_BUILD_TYPE ).setSelection( COMBO_MAVEN );
        assertFalse( bot.button( BUTTON_BACK ).isEnabled() );
        assertTrue( bot.button( BUTTON_NEXT ).isEnabled() );
        bot.sleep( 2000 );
        assertTrue( bot.button( BUTTON_FINISH ).isEnabled() );
        assertTrue( bot.button( BUTTON_CANCEL ).isEnabled() );

        assertEquals( "", bot.textWithLabel( LABEL_ARTIFACT_VERSION ).getText() );
        assertEquals( "", bot.textWithLabel( LABEL_GROUP_ID ).getText() );
        assertEquals( "", bot.textWithLabel( LABEL_ACTIVE_PROFILES ).getText() );

        assertTrue( bot.checkBox( CHECKBOX_USE_DEFAULT_LOCATION ).isChecked() );;

        bot.checkBox( "Launch New Portlet Wizard after project is created" ).click();

        bot.button( BUTTON_NEXT ).click();

        /* check the next page */

        assertTrue( bot.radio( RADIO_LIFERAY_MVC ).isSelected() );
        assertFalse( bot.radio( RADIO_JSF_2X ).isSelected() );
        assertFalse( bot.radio( RADIO_SPRING_MVC ).isSelected() );
        assertFalse( bot.radio( RADIO_VAADIN ).isSelected() );

        assertEquals( "", bot.textWithLabel( LABEL_PORTLET_NAME ).getText() );
        assertEquals( "", bot.textWithLabel( LABEL_DISPLAY_NAME ).getText() );
        assertEquals( "", bot.textWithLabel( LABEL_ARCHETYPE ).getText() );

        assertTrue( bot.button( BUTTON_BACK ).isEnabled() );
        assertFalse( bot.button( BUTTON_NEXT ).isEnabled() );
        assertTrue( bot.button( BUTTON_FINISH ).isEnabled() );
        assertTrue( bot.button( BUTTON_CANCEL ).isEnabled() );
        bot.button( BUTTON_FINISH ).click();
        bot.sleep( 2000 );
        bot.comboBoxWithLabel( "Superclass:" ).setSelection( "javax.portlet.GenericPortlet" );
        bot.button( BUTTON_FINISH ).click();

        /* After created the project .check the java and xml files */

        bot.tree().getTreeItem( TEXT_PROJECT_NAME ).expand();
        bot.tree().getTreeItem( TEXT_PROJECT_NAME ).getNode( "pom.xml" ).select();
        assertTrue( bot.tree().getTreeItem( TEXT_PROJECT_NAME ).getNode( "pom.xml" ).isVisible() );

        bot.sleep( 2000 );
        bot.tree().getTreeItem( TEXT_PROJECT_NAME ).expand();
        bot.tree().getTreeItem( TEXT_PROJECT_NAME ).getNode( "src" ).expand();
        bot.tree().getTreeItem( TEXT_PROJECT_NAME ).getNode( "src" ).getNode( "main" ).expand();
        bot.tree().getTreeItem( TEXT_PROJECT_NAME ).getNode( "src" ).getNode( "main" ).getNode( "webapp" ).expand();
        bot.tree().getTreeItem( TEXT_PROJECT_NAME ).getNode( "src" ).getNode( "main" ).getNode( "webapp" ).getNode(
            "view.jsp" ).select();
        assertTrue(
            bot.tree().getTreeItem( TEXT_PROJECT_NAME ).getNode( "src" ).getNode( "main" ).getNode( "webapp" ).getNode(
                "view.jsp" ).isVisible() );

    }

    @AfterClass
    public static void afterClass()
    {
        bot.sleep( 5000 );
    }

}

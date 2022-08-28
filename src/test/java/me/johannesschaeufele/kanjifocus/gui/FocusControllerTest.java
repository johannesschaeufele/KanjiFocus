package me.johannesschaeufele.kanjifocus.gui;

import java.awt.event.WindowEvent;
import javax.swing.JTextField;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class FocusControllerTest {

    private FocusController controller;

    @Test
    public void testOverviewTags() {
        FocusView view = this.controller.getView();

        JTextField textFieldOverviewTags = view.getTextFieldKanjiOverviewTags();

        textFieldOverviewTags.setText("invalidtag");
        assertThat(this.controller.getKanjiOverviewModel().getRowCount(), is(0));

        textFieldOverviewTags.setText("jlpt5");
        int jlpt5_count = this.controller.getKanjiOverviewModel().getRowCount();
        assertThat(jlpt5_count, greaterThan(0));

        textFieldOverviewTags.setText("jlpt4");
        int jlpt4_count = this.controller.getKanjiOverviewModel().getRowCount();
        assertThat(jlpt4_count, greaterThan(0));

        textFieldOverviewTags.setText("jlpt5 jlpt4");
        int jlpt54_count = this.controller.getKanjiOverviewModel().getRowCount();
        assertThat(jlpt54_count, is(jlpt5_count + jlpt4_count));

        textFieldOverviewTags.setText("");
        int total_count = this.controller.getKanjiOverviewModel().getRowCount();
        assertThat(total_count, greaterThanOrEqualTo(jlpt54_count));
    }

    @Test
    public void testDraw() throws InterruptedException {
        FocusView view = this.controller.getView();

        view.setVisible(true);

        view.setActiveTab(0);
        Thread.sleep(100L);

        view.setActiveTab(1);
        Thread.sleep(100L);
    }

    @Before
    public void setUp() {
        this.controller = new FocusController(true);
        this.controller.start(false);
    }

    @After
    public void tearDown() {
        FocusView view = this.controller.getView();
        view.dispatchEvent(new WindowEvent(view, WindowEvent.WINDOW_CLOSING));
    }

}

package me.johannesschaeufele.kanjifocus.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.im.InputContext;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import me.johannesschaeufele.kanjifocus.configuration.Configuration;
import me.johannesschaeufele.kanjifocus.database.data.Kanji;
import me.johannesschaeufele.kanjifocus.database.data.Reading;
import me.johannesschaeufele.kanjifocus.database.user.Progress;
import me.johannesschaeufele.kanjifocus.gui.kanjidetail.KanjiDetailTableCellRenderer;
import me.johannesschaeufele.kanjifocus.gui.kanjidetail.KanjiDetailTableModel;
import me.johannesschaeufele.kanjifocus.gui.kanjioverview.KanjiOverviewTable;
import me.johannesschaeufele.kanjifocus.gui.kanjioverview.KanjiOverviewTableCellRenderer;
import me.johannesschaeufele.kanjifocus.gui.kanjioverview.KanjiOverviewTableModel;
import me.johannesschaeufele.kanjifocus.util.Resources;
import me.johannesschaeufele.kanjifocus.util.RomajiUtil;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

/**
 * Main controller class for the entire application
 *
 * This follows the MVC (model view controller) approach
 */
public class FocusController {

    private static final SAXSVGDocumentFactory documentFactory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());

    private final FocusModel model;
    private FocusView view;

    private final KanjiOverviewTableModel kanjiOverviewModel;
    private final KanjiDetailTableModel kanjiDetailModel;
    private final VocabularyTableModel kanjiDetailVocabularyModel;

    private JSVGCanvas canvasKanjiDetails;

    public FocusController() {
        this(false);
    }

    public FocusController(boolean mock) {
        this.model = new FocusModel(mock ? new Configuration() : null);

        this.kanjiOverviewModel = new KanjiOverviewTableModel(this.model);
        this.kanjiDetailModel = new KanjiDetailTableModel(this.model);
        this.kanjiDetailVocabularyModel = new VocabularyTableModel(this.model);
    }

    public FocusModel getModel() {
        return this.model;
    }

    public FocusView getView() {
        return this.view;
    }

    public KanjiOverviewTableModel getKanjiOverviewModel() {
        return this.kanjiOverviewModel;
    }

    public KanjiDetailTableModel getKanjiDetailModel() {
        return this.kanjiDetailModel;
    }

    public VocabularyTableModel getKanjiDetailVocabularyModel() {
        return this.kanjiDetailVocabularyModel;
    }

    public void start() {
        this.start(true);
    }

    public void start(boolean visible) {
        this.view = new FocusView();

        InputContext inputContext = this.view.getInputContext();
        inputContext.selectInputMethod(Locale.JAPANESE);

        canvasKanjiDetails = new JSVGCanvas();

        canvasKanjiDetails.setBackground(new Color(0x00FFFFFF, true));

        //this.view.getPanelKanjiStroke().add(canvas);
        JPanel panelKanjiStroke = this.view.getPanelKanjiStroke();

        GroupLayout panelKanjiStrokeLayout = new GroupLayout(panelKanjiStroke);
        panelKanjiStroke.setLayout(panelKanjiStrokeLayout);
        panelKanjiStrokeLayout.setHorizontalGroup(panelKanjiStrokeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(canvasKanjiDetails, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
        );
        panelKanjiStrokeLayout.setVerticalGroup(panelKanjiStrokeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(canvasKanjiDetails, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
        );

        canvasKanjiDetails.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
        canvasKanjiDetails.setAnimationLimitingFPS(200.0F);

        //--
        JTable kanjiOverviewTable = new KanjiOverviewTable();
        kanjiOverviewTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        kanjiOverviewTable.setRowHeight(40);
        kanjiOverviewTable.setDefaultRenderer(ValuePair.class, new KanjiOverviewTableCellRenderer());

        kanjiOverviewTable.setModel(this.kanjiOverviewModel);

        kanjiOverviewTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        kanjiOverviewTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        for(int i = 2; i < kanjiOverviewTable.getColumnCount() - 1; i++) {
            kanjiOverviewTable.getColumnModel().getColumn(i).setPreferredWidth(150);
        }
        kanjiOverviewTable.getColumnModel().getColumn(kanjiOverviewTable.getColumnCount() - 1).setPreferredWidth(1000);

        kanjiOverviewTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent me) {
                if(me.getButton() == MouseEvent.BUTTON1 && me.getClickCount() == 2) {
                    Point p = me.getPoint();
                    int kanjiRow = kanjiOverviewTable.rowAtPoint(p) / 2;

                    FocusController.this.updateKanjiDetails(FocusController.this.kanjiOverviewModel.getKanji(kanjiRow));
                    FocusController.this.showKanjiDetails();
                }
            }

        });

        this.view.setTableKanjiOverview(kanjiOverviewTable);

        //--
        JTable kanjiDetailTable = new JTable();
        kanjiDetailTable.setTableHeader(null);
        kanjiDetailTable.setShowGrid(false);
        kanjiDetailTable.setIntercellSpacing(new Dimension(0, 0));
        kanjiDetailTable.setBackground(Color.GRAY);

        kanjiDetailTable.setModel(this.kanjiDetailModel);
        kanjiDetailTable.setDefaultRenderer(ValuePair.class, new KanjiDetailTableCellRenderer());
        kanjiDetailTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        kanjiDetailTable.setRowHeight(40);
        kanjiDetailTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        kanjiDetailTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        for(int i = 2; i < kanjiDetailTable.getColumnCount(); i++) {
            kanjiDetailTable.getColumnModel().getColumn(i).setPreferredWidth(150);
        }
        kanjiDetailTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent me) {
                if(me.getClickCount() >= 1) {
                    Point p = me.getPoint();

                    int row = kanjiDetailTable.rowAtPoint(p);
                    int column = kanjiDetailTable.columnAtPoint(p);

                    int value = 0;
                    if(me.getButton() == MouseEvent.BUTTON1) {
                        value = 1;
                    }
                    else if(me.getButton() == MouseEvent.BUTTON3) {
                        value = -1;
                    }

                    if(value != 0) {
                        onKanjiDetailProgressChange(row, column, value);
                    }
                }
            }

        });

        this.view.setTableKanjiDetail(kanjiDetailTable);

        if(this.kanjiOverviewModel.getRowCount() > 0) {
            this.updateKanjiDetails(this.kanjiOverviewModel.getKanji(0));
        }

        //--
        JTable kanjiDetailVocabularyTable = new JTable();
        kanjiDetailVocabularyTable.setTableHeader(null);
        kanjiDetailVocabularyTable.setShowGrid(false);
        kanjiDetailVocabularyTable.setIntercellSpacing(new Dimension(0, 0));
        kanjiDetailVocabularyTable.setBackground(Color.GRAY);

        kanjiDetailVocabularyTable.setModel(this.kanjiDetailVocabularyModel);
        kanjiDetailVocabularyTable.setDefaultRenderer(ValuePair.class, new VocabularyTableCellRenderer());
        kanjiDetailVocabularyTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        kanjiDetailVocabularyTable.setRowHeight(40);
        kanjiDetailVocabularyTable.getColumnModel().getColumn(0).setMinWidth(200);
        kanjiDetailVocabularyTable.getColumnModel().getColumn(0).setPreferredWidth(500);
        kanjiDetailVocabularyTable.getColumnModel().getColumn(0).setMaxWidth(500);
        kanjiDetailVocabularyTable.getColumnModel().getColumn(1).setPreferredWidth(2000);
        kanjiDetailVocabularyTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent me) {
                if(me.getButton() == MouseEvent.BUTTON1 && me.getClickCount() == 2) {
                    Point p = me.getPoint();

                    FocusController.this.updateVocabularyDetails(FocusController.this.kanjiDetailVocabularyModel.getVocabulary(kanjiOverviewTable.rowAtPoint(p)));
                    FocusController.this.showVocabularyDetails();
                }
            }

        });

        this.view.setTableKanjiDetailVocabulary(kanjiDetailVocabularyTable);
        //--
        this.view.setProgressModel(new AbstractListModel<String>() {

            @Override
            public int getSize() {
                return Progress.byId.length;
            }

            @Override
            public String getElementAt(int i) {
                return Progress.byId[i].getDisplay();
            }

        });

        //--
        this.view.addKanjiOverviewProgressCallback(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                onKanjiOverviewProgress();
            }

        });
        this.view.addKanjiOverviewSearchCallback(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                onKanjiOverviewSearch();
            }

        });
        this.view.addKanjiOverviewSearchAllCallback(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                onKanjiOverviewSearchAll();
            }

        });
        this.view.addKanjiOverviewTagsCallback(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                onKanjiOverviewTags();
            }

        });
        this.view.addKanjiOverviewShuffleCallback(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                onKanjiOverviewShuffle();
            }

        });
        this.view.addKanjiOverviewLearnModeCallback(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                onKanjiOverviewLearnMode();
            }

        });
        this.view.addKanjiOverviewDisplayOnCallback(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                onKanjiOverviewDisplayOn();
            }

        });
        this.view.addKanjiOverviewDisplayKunCallback(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                onKanjiOverviewDisplayKun();
            }

        });

        this.view.addKanjiDetailTagsCallback(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                onKanjiDetailVocabularyTags();
            }

        });
        this.view.addKanjiDetailVocabularyReadingCallback(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                onKanjiDetailVocabularyReading();
            }

        });
        this.view.addKanjiDetailSearchCallback(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                onKanjiDetailVocabularySearch();
            }

        });
        this.view.addKanjiDetailSearchAllCallback(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                onKanjiDetailSearchAll();
            }

        });

        this.view.addExitCallback(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                onExit();
            }

        });

        Configuration configuration = this.model.getConfiguration();

        this.view.setKanjiOverviewTags(configuration.getKanjiOverviewTags());
        this.view.setKanjiDetailVocabularyTags(configuration.getKanjiDetailVocabularyTags());

        this.onKanjiOverviewProgress();
        this.onKanjiOverviewTags();
        this.onKanjiOverviewSearch();
        this.onKanjiOverviewSearchAll();
        this.onKanjiOverviewShuffle();
        this.onKanjiOverviewLearnMode();

        this.onKanjiDetailVocabularyTags();
        this.onKanjiDetailVocabularySearch();
        this.onKanjiDetailSearchAll();

        //--
        this.view.setVisible(visible);
    }

    private List<String> splitStringRomaji(String string) {
        String[] split = string.split(" ");

        List<String> result = new ArrayList<>(split.length);
        for(String s : split) {
            if(!s.isEmpty()) {
                result.add(s);
            }
        }

        if(result.isEmpty()) {
            result = null;
        }

        return result;
    }

    private List<String> splitString(String string) {
        String[] split = string.split(" ");

        List<String> result = new ArrayList<>(split.length);
        for(String s : split) {
            if(!s.isEmpty()) {
                result.add(RomajiUtil.kanaToRomaji(s));
            }
        }

        if(result.isEmpty()) {
            result = null;
        }

        return result;
    }

    private List<Progress> arrayToProgress(int[] ids) {
        List<Progress> result = new ArrayList<>(ids.length);

        for(int id : ids) {
            result.add(Progress.byId[id]);
        }

        if(result.isEmpty()) {
            result = null;
        }

        return result;
    }

    private void onKanjiDetailProgressChange(int row, int column, int value) {
        this.kanjiDetailModel.changeProgress(row, column, value);
    }

    private void onKanjiOverviewProgress() {
        this.kanjiOverviewModel.setProgressFilter(this.arrayToProgress(this.view.getKanjiOverviewProgress()));

        this.onKanjiOverviewResults();
    }

    private void onKanjiOverviewTags() {
        if(this.view.isKanjiOverviewSearchAll()) {
            this.kanjiOverviewModel.setTags(null, null);
        }
        else {
            String[] split = this.view.getKanjiOverviewTags().split(" ");

            List<String> tagsInclude = new ArrayList<>(split.length / 2);
            List<String> tagsExclude = new ArrayList<>(split.length / 2);
            for(String s : split) {
                if(!s.isEmpty()) {
                    if(s.charAt(0) == '-') {
                        if(s.length() > 1) {
                            tagsExclude.add(s.substring(1));
                        }
                    }
                    else {
                        tagsInclude.add(s);
                    }
                }
            }

            if(tagsInclude.isEmpty()) {
                tagsInclude = null;
            }
            if(tagsExclude.isEmpty()) {
                tagsExclude = null;
            }

            this.kanjiOverviewModel.setTags(tagsInclude, tagsExclude);
        }

        this.onKanjiOverviewResults();
    }

    private void onKanjiDetailVocabularyTags() {
        if(this.view.isKanjiDetailSearchAll()) {
            this.kanjiDetailVocabularyModel.setTags(null);
        }
        else {
            this.kanjiDetailVocabularyModel.setTags(this.splitString(this.view.getKanjiDetailVocabularyTags()));
        }
    }

    private void onKanjiDetailVocabularySearch() {
        this.kanjiDetailVocabularyModel.setSearchTerms(this.splitString(this.view.getKanjiDetailVocabularySearch()));
    }

    private void onKanjiOverviewSearch() {
        this.kanjiOverviewModel.setReadingSearch(this.splitStringRomaji(this.view.getKanjiOverviewSearch()));

        this.onKanjiOverviewResults();
    }

    private void onKanjiOverviewSearchAll() {
        if(this.view.isKanjiOverviewSearchAll()) {
            this.kanjiOverviewModel.setTags(null, null);

            this.onKanjiOverviewResults();
        }
        else {
            this.onKanjiOverviewTags();
        }
    }

    private void updateKanjiDetails(String kanji) {
        this.model.setKanji(kanji);
        this.kanjiDetailModel.update();

        Kanji k = this.model.getDataDatabase().getKanji(kanji);

        this.view.setKanjiDetailText("<html>"
                + k.getMeanings() + "<br>"
                + "<br>"
                + k.getTags() + "<br>"
                + k.getRadicals() + "<br>"
                + "</html>"
        );

        List<Reading> onyomi = k.getOnyomi();
        List<Reading> kunyomi = k.getKunyomi();

        String[] readingOptions = new String[1 + onyomi.size() + kunyomi.size()];
        int i = 0;

        readingOptions[i++] = "<any>";
        for(int j = 0; j < onyomi.size(); j++) {
            readingOptions[i++] = onyomi.get(j).getReading();
        }
        for(int j = 0; j < kunyomi.size(); j++) {
            readingOptions[i++] = kunyomi.get(j).getReading();
        }

        this.view.setKanjiDetailReadingModel(new DefaultComboBoxModel<>(readingOptions));

        this.kanjiDetailVocabularyModel.setKanji(kanji);
        this.kanjiDetailVocabularyModel.setReading(null);
        this.kanjiDetailVocabularyModel.setMustIncludes(Arrays.asList(kanji));

        Document doc = null;

        try {
            String cn = String.format("%05x", Character.codePointAt(kanji, 0) & 0x0FFFFF);

            FileSystem fileSystem = FileSystems.newFileSystem(Resources.getFile("res/kanjivg.zip").toPath(), null);

            Path path = fileSystem.getPath(cn + ".svg");
            if(Files.exists(path)) {
                URL url = path.toUri().toURL();
                doc = documentFactory.createDocument(url.toString());

                doc.getElementById("kvg:StrokePaths_" + cn).setAttribute("style", "fill:none;stroke:#000000;stroke-width:4;stroke-linecap:round;stroke-linejoin:round;");

                doc.getElementById("kvg:StrokeNumbers_" + cn).setAttribute("style", "font-size:7;fill:#8080FF");
            }
        }
        catch(IOException | DOMException ex) {
            Logger.getLogger(FocusController.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.canvasKanjiDetails.setDocument(doc);
    }

    private void onKanjiOverviewResults() {
        int resultCount = this.kanjiOverviewModel.getRowCount() / 2;

        this.view.setKanjiOverviewResults(resultCount + " result" + (resultCount != 1 ? "s" : ""));
    }

    private void showKanjiDetails() {
        this.view.setActiveTab(1);
    }

    private void updateVocabularyDetails(int vocabulary) {
    }

    private void showVocabularyDetails() {
        // this.view.setActiveTab(3);
    }

    private void onKanjiDetailSearchAll() {
        if(this.view.isKanjiDetailSearchAll()) {
            this.kanjiDetailVocabularyModel.setTags(null);
        }
        else {
            this.onKanjiDetailVocabularyTags();
        }
    }

    private void onKanjiDetailVocabularyReading() {
        String reading = null;

        int readingIndex = this.view.getKanjiDetailVocabularyReadingIndex() - 1;

        if(readingIndex >= 0) {
            String kanji = this.model.getKanji();

            if(kanji != null) {
                Kanji k = this.model.getDataDatabase().getKanji(kanji);

                List<Reading> onyomi = k.getOnyomi();
                if(readingIndex < onyomi.size()) {
                    reading = onyomi.get(readingIndex).getReading();
                }
                else {
                    reading = k.getKunyomi().get(readingIndex - onyomi.size()).getReading();
                }
            }
        }

        this.kanjiDetailVocabularyModel.setReading(reading);
    }

    private void onExit() {
        Configuration configuration = this.model.getConfiguration();

        configuration.setKanjiOverviewTags(this.view.getKanjiOverviewTags());
        configuration.setKanjiDetailVocabularyTags(this.view.getKanjiDetailVocabularyTags());

        this.model.saveConfiguration();
    }

    private void onKanjiOverviewShuffle() {
        this.kanjiOverviewModel.setShuffe(this.view.isKanjiOverviewShuffle());
    }

    private void onKanjiOverviewLearnMode() {
        this.kanjiOverviewModel.setLearnMode(this.view.getKanjiOverviewLearnMode());
    }

    private void onKanjiOverviewDisplayOn() {
        this.kanjiOverviewModel.setDisplayOn(this.view.isKanjiOverviewDisplayOn());
    }

    private void onKanjiOverviewDisplayKun() {
        this.kanjiOverviewModel.setDisplayKun(this.view.isKanjiOverviewDisplayKun());
    }

}

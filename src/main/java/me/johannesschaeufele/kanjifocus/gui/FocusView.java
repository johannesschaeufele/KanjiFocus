package me.johannesschaeufele.kanjifocus.gui;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Main view class for the application that encapsulates the main frame and GUI elements of the application
 * a side from the internals of the kanji list and kanji detail tables
 *
 * This follows the MVC (model view controller) approach
 */
public class FocusView extends javax.swing.JFrame {

    public FocusView() {
        initComponents();

        this.listKanjiOverviewProgress.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        this.listKanjiOverviewProgress.setVisibleRowCount(1);
        this.listKanjiOverviewProgress.setFixedCellWidth(90);

        this.listKanjiDetailProgress.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        this.listKanjiDetailProgress.setVisibleRowCount(1);
        this.listKanjiDetailProgress.setFixedCellWidth(90);

        this.labelKanjiDetailInfo.setVerticalAlignment(SwingConstants.TOP);
        this.labelKanjiDetailInfo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));

        this.setLocationRelativeTo(null);
    }

    public void addKanjiOverviewLearnModeCallback(ActionListener listener) {
        this.radioButtonKanjiOverviewLearnAll.addActionListener(listener);
        this.radioButtonKanjiOverviewLearnLeft.addActionListener(listener);
        this.radioButtonKanjiOverviewLearnRight.addActionListener(listener);
        this.radioButtonKanjiOverviewLearnMeaning.addActionListener(listener);
    }

    public int getKanjiOverviewLearnMode() {
        if(this.radioButtonKanjiOverviewLearnLeft.isSelected()) {
            return 1;
        }
        else if(this.radioButtonKanjiOverviewLearnRight.isSelected()) {
            return 2;
        }
        else if(this.radioButtonKanjiOverviewLearnMeaning.isSelected()) {
            return 3;
        }

        return 0;
    }

    public void setKanjiOverviewTags(String text) {
        this.textFieldKanjiOverviewTags.setText(text);
    }

    public String getKanjiOverviewTags() {
        return this.textFieldKanjiOverviewTags.getText();
    }

    public void setKanjiDetailVocabularyTags(String text) {
        this.textFieldKanjiDetailTags.setText(text);
    }

    public String getKanjiDetailVocabularyTags() {
        return this.textFieldKanjiDetailTags.getText();
    }

    public String getKanjiDetailVocabularySearch() {
        return this.textFieldKanjiDetailSearch.getText();
    }

    public void setKanjiOverviewResults(String text) {
        this.labelKanjiOverviewResults.setText(text);
    }

    public String getKanjiOverviewSearch() {
        return this.textFieldKanjiOverviewSearch.getText();
    }

    public JPanel getPanelKanjiStroke() {
        return this.panelKanjiStroke;
    }

    public boolean isKanjiDetailSearchAll() {
        return this.checkBoxKanjiDetailSearchAll.isSelected();
    }

    public boolean isKanjiOverviewSearchAll() {
        return this.checkBoxKanjiOverviewSearchAll.isSelected();
    }

    public boolean isKanjiOverviewShuffle() {
        return this.checkBoxKanjiOverviewShuffle.isSelected();
    }

    public boolean isKanjiOverviewDisplayOn() {
        return this.checkBoxKanjiOverviewDisplayOn.isSelected();
    }

    public boolean isKanjiOverviewDisplayKun() {
        return this.checkBoxKanjiOverviewDisplayKun.isSelected();
    }

    public void addKanjiDetailSearchAllCallback(ActionListener listener) {
        this.checkBoxKanjiDetailSearchAll.addActionListener(listener);
    }

    public void addKanjiOverviewDisplayOnCallback(ActionListener listener) {
        this.checkBoxKanjiOverviewDisplayOn.addActionListener(listener);
    }

    public void addKanjiOverviewDisplayKunCallback(ActionListener listener) {
        this.checkBoxKanjiOverviewDisplayKun.addActionListener(listener);
    }

    public void addKanjiOverviewShuffleCallback(ActionListener listener) {
        this.checkBoxKanjiOverviewShuffle.addActionListener(listener);
    }

    public void addKanjiOverviewSearchAllCallback(ActionListener listener) {
        this.checkBoxKanjiOverviewSearchAll.addActionListener(listener);
    }

    public void setKanjiDetailReadingModel(ComboBoxModel<String> comboBoxModel) {
        this.comboBoxKanjiDetailReading.setModel(comboBoxModel);
    }

    public void addExitCallback(ActionListener listener) {
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                listener.actionPerformed(null);
            }

        });
    }

    public void setProgressModel(ListModel<String> listModel) {
        this.listKanjiOverviewProgress.setModel(listModel);
        this.listKanjiDetailProgress.setModel(listModel);
    }

    public int[] getKanjiOverviewProgress() {
        return this.listKanjiOverviewProgress.getSelectedIndices();
    }

    public int getKanjiDetailVocabularyReadingIndex() {
        return this.comboBoxKanjiDetailReading.getSelectedIndex();
    }

    public JTable getKanjiDetailTable() {
        return this.jTable2;
    }

    public void setKanjiDetailText(String text) {
        this.labelKanjiDetailInfo.setText(text);
    }

    public void addKanjiOverviewProgressCallback(ActionListener listener) {
        this.listKanjiOverviewProgress.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if(!lse.getValueIsAdjusting()) {
                    listener.actionPerformed(null);
                }
            }

        });
    }

    public void addKanjiOverviewTagsCallback(ActionListener listener) {
        this.textFieldKanjiOverviewTags.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent de) {
                listener.actionPerformed(null);
            }

            @Override
            public void insertUpdate(DocumentEvent de) {
                listener.actionPerformed(null);
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                listener.actionPerformed(null);
            }

        });
    }

    public void addKanjiDetailTagsCallback(ActionListener listener) {
        this.textFieldKanjiDetailTags.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent de) {
                listener.actionPerformed(null);
            }

            @Override
            public void insertUpdate(DocumentEvent de) {
                listener.actionPerformed(null);
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                listener.actionPerformed(null);
            }

        });
    }

    public void addKanjiDetailSearchCallback(ActionListener listener) {
        this.textFieldKanjiDetailSearch.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent de) {
                listener.actionPerformed(null);
            }

            @Override
            public void insertUpdate(DocumentEvent de) {
                listener.actionPerformed(null);
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                listener.actionPerformed(null);
            }

        });
    }

    public void setActiveTab(int index) {
        this.tabbedPane.setSelectedIndex(index);
    }

    public void addKanjiDetailVocabularyReadingCallback(ActionListener listener) {
        this.comboBoxKanjiDetailReading.addActionListener(listener);
    }

    public void addKanjiOverviewSearchCallback(ActionListener listener) {
        this.textFieldKanjiOverviewSearch.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent de) {
                listener.actionPerformed(null);
            }

            @Override
            public void insertUpdate(DocumentEvent de) {
                listener.actionPerformed(null);
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                listener.actionPerformed(null);
            }

        });
    }

    public void setTableKanjiOverview(JTable table) {
        scrollPaneKanjiOverview.setViewportView(table);
    }

    public void setTableKanjiDetail(JTable table) {
        jScrollPane2.setViewportView(table);
    }

    public void setTableKanjiDetailVocabulary(JTable table) {
        scrollPaneKanjiDetailVocabulary.setViewportView(table);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupKanjiOverviewLearnMode = new ButtonGroup();
        tabbedPane = new JTabbedPane();
        panelTabKanjiOverview = new JPanel();
        panelKanjiOverviewFilter = new JPanel();
        scrollPaneKanjiOverviewProgress = new JScrollPane();
        listKanjiOverviewProgress = new JList<>();
        labelKanjiOverviewTags = new JLabel();
        textFieldKanjiOverviewTags = new JTextField();
        labelKanjiOverviewSearch = new JLabel();
        textFieldKanjiOverviewSearch = new JTextField();
        checkBoxKanjiOverviewSearchAll = new JCheckBox();
        labelKanjiOverviewResults = new JLabel();
        checkBoxKanjiOverviewShuffle = new JCheckBox();
        radioButtonKanjiOverviewLearnAll = new JRadioButton();
        radioButtonKanjiOverviewLearnLeft = new JRadioButton();
        radioButtonKanjiOverviewLearnRight = new JRadioButton();
        radioButtonKanjiOverviewLearnMeaning = new JRadioButton();
        checkBoxKanjiOverviewDisplayOn = new JCheckBox();
        checkBoxKanjiOverviewDisplayKun = new JCheckBox();
        scrollPaneKanjiOverview = new JScrollPane();
        panelTabKanji = new JPanel();
        jPanel5 = new JPanel();
        checkBoxKanjiDetailLearn = new JCheckBox();
        jPanel6 = new JPanel();
        panelKanjiStroke = new JPanel();
        jButton2 = new JButton();
        jScrollPane2 = new JScrollPane();
        jTable2 = new JTable();
        labelKanjiDetailInfo = new JLabel();
        panelKanjiDetailVocabulary = new JPanel();
        scrollPaneKanjiDetailProgress = new JScrollPane();
        listKanjiDetailProgress = new JList<>();
        labelKanjiDetailTags = new JLabel();
        textFieldKanjiDetailTags = new JTextField();
        comboBoxKanjiDetailReading = new JComboBox<>();
        labelKanjiDetailSearch = new JLabel();
        textFieldKanjiDetailSearch = new JTextField();
        checkBoxKanjiDetailSearchAll = new JCheckBox();
        scrollPaneKanjiDetailVocabulary = new JScrollPane();
        jTable3 = new JTable();
        panelTabVocabularyOverview = new JPanel();
        jPanel10 = new JPanel();
        jLabel5 = new JLabel();
        jTextField5 = new JTextField();
        jLabel6 = new JLabel();
        jCheckBox6 = new JCheckBox();
        jTextField6 = new JTextField();
        jScrollPane7 = new JScrollPane();
        jList3 = new JList<>();
        jTextField7 = new JTextField();
        jLabel7 = new JLabel();
        jComboBox1 = new JComboBox<>();
        jScrollPane3 = new JScrollPane();
        panelTabVocabulary = new JPanel();
        jPanel9 = new JPanel();
        jCheckBox7 = new JCheckBox();
        jScrollPane8 = new JScrollPane();
        jTable1 = new JTable();
        jScrollPane9 = new JScrollPane();
        jTable4 = new JTable();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("KanjiFocus");

        scrollPaneKanjiOverviewProgress.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneKanjiOverviewProgress.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        scrollPaneKanjiOverviewProgress.setViewportView(listKanjiOverviewProgress);

        labelKanjiOverviewTags.setText("Tags:");

        labelKanjiOverviewSearch.setText("Search:");

        checkBoxKanjiOverviewSearchAll.setText("Search all");

        labelKanjiOverviewResults.setHorizontalAlignment(SwingConstants.RIGHT);
        labelKanjiOverviewResults.setText(" ");

        checkBoxKanjiOverviewShuffle.setText("Shuffle");

        buttonGroupKanjiOverviewLearnMode.add(radioButtonKanjiOverviewLearnAll);
        radioButtonKanjiOverviewLearnAll.setSelected(true);
        radioButtonKanjiOverviewLearnAll.setText("All");

        buttonGroupKanjiOverviewLearnMode.add(radioButtonKanjiOverviewLearnLeft);
        radioButtonKanjiOverviewLearnLeft.setText("Left");

        buttonGroupKanjiOverviewLearnMode.add(radioButtonKanjiOverviewLearnRight);
        radioButtonKanjiOverviewLearnRight.setText("Right");

        buttonGroupKanjiOverviewLearnMode.add(radioButtonKanjiOverviewLearnMeaning);
        radioButtonKanjiOverviewLearnMeaning.setText("Meaning");

        checkBoxKanjiOverviewDisplayOn.setSelected(true);
        checkBoxKanjiOverviewDisplayOn.setText("On");

        checkBoxKanjiOverviewDisplayKun.setSelected(true);
        checkBoxKanjiOverviewDisplayKun.setText("Kun");
        checkBoxKanjiOverviewDisplayKun.setToolTipText("");

        GroupLayout panelKanjiOverviewFilterLayout = new GroupLayout(panelKanjiOverviewFilter);
        panelKanjiOverviewFilter.setLayout(panelKanjiOverviewFilterLayout);
        panelKanjiOverviewFilterLayout.setHorizontalGroup(panelKanjiOverviewFilterLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, panelKanjiOverviewFilterLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelKanjiOverviewFilterLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(scrollPaneKanjiOverviewProgress, GroupLayout.Alignment.LEADING)
                                .addGroup(GroupLayout.Alignment.LEADING, panelKanjiOverviewFilterLayout.createSequentialGroup()
                                        .addGroup(panelKanjiOverviewFilterLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(labelKanjiOverviewTags)
                                                .addComponent(labelKanjiOverviewSearch))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(panelKanjiOverviewFilterLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(textFieldKanjiOverviewSearch)
                                                .addComponent(textFieldKanjiOverviewTags))
                                        .addGap(6, 6, 6)
                                        .addComponent(checkBoxKanjiOverviewSearchAll))
                                .addGroup(panelKanjiOverviewFilterLayout.createSequentialGroup()
                                        .addComponent(checkBoxKanjiOverviewShuffle)
                                        .addGap(132, 132, 132)
                                        .addComponent(radioButtonKanjiOverviewLearnAll)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(radioButtonKanjiOverviewLearnLeft)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(radioButtonKanjiOverviewLearnRight)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(radioButtonKanjiOverviewLearnMeaning)
                                        .addGap(18, 18, 18)
                                        .addComponent(checkBoxKanjiOverviewDisplayOn)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(checkBoxKanjiOverviewDisplayKun)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(labelKanjiOverviewResults, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
        );
        panelKanjiOverviewFilterLayout.setVerticalGroup(panelKanjiOverviewFilterLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(panelKanjiOverviewFilterLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(scrollPaneKanjiOverviewProgress, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addGroup(panelKanjiOverviewFilterLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(labelKanjiOverviewTags)
                                .addComponent(textFieldKanjiOverviewTags, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelKanjiOverviewFilterLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(labelKanjiOverviewSearch)
                                .addComponent(checkBoxKanjiOverviewSearchAll)
                                .addComponent(textFieldKanjiOverviewSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelKanjiOverviewFilterLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(panelKanjiOverviewFilterLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelKanjiOverviewResults, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(checkBoxKanjiOverviewDisplayOn)
                                        .addComponent(checkBoxKanjiOverviewDisplayKun))
                                .addGroup(panelKanjiOverviewFilterLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(checkBoxKanjiOverviewShuffle)
                                        .addComponent(radioButtonKanjiOverviewLearnAll)
                                        .addComponent(radioButtonKanjiOverviewLearnLeft)
                                        .addComponent(radioButtonKanjiOverviewLearnRight)
                                        .addComponent(radioButtonKanjiOverviewLearnMeaning))))
        );

        GroupLayout panelTabKanjiOverviewLayout = new GroupLayout(panelTabKanjiOverview);
        panelTabKanjiOverview.setLayout(panelTabKanjiOverviewLayout);
        panelTabKanjiOverviewLayout.setHorizontalGroup(panelTabKanjiOverviewLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(panelKanjiOverviewFilter, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrollPaneKanjiOverview)
        );
        panelTabKanjiOverviewLayout.setVerticalGroup(panelTabKanjiOverviewLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, panelTabKanjiOverviewLayout.createSequentialGroup()
                        .addComponent(panelKanjiOverviewFilter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollPaneKanjiOverview, GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Kanji overview", panelTabKanjiOverview);

        checkBoxKanjiDetailLearn.setText("Learn mode");

        GroupLayout jPanel5Layout = new GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        // .addComponent(checkBoxKanjiDetailLearn)
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                // .addComponent(checkBoxKanjiDetailLearn, GroupLayout.Alignment.TRAILING)
        );

        GroupLayout panelKanjiStrokeLayout = new GroupLayout(panelKanjiStroke);
        panelKanjiStroke.setLayout(panelKanjiStrokeLayout);
        panelKanjiStrokeLayout.setHorizontalGroup(panelKanjiStrokeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGap(0, 157, Short.MAX_VALUE)
        );
        panelKanjiStrokeLayout.setVerticalGroup(panelKanjiStrokeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGap(0, 157, Short.MAX_VALUE)
        );

        jButton2.setText("Show stroke order");

        jScrollPane2.setViewportView(jTable2);

        GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(panelKanjiStroke, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addGap(29, 29, 29)
                                        //.addComponent(jButton2)
                                ))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                        .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(panelKanjiStroke, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        //.addComponent(jButton2)
                                        .addGap(22, 22, Short.MAX_VALUE)))
                        .addContainerGap())
        );

        panelKanjiDetailVocabulary.setBorder(BorderFactory.createTitledBorder("Vocabulary"));

        scrollPaneKanjiDetailProgress.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneKanjiDetailProgress.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        scrollPaneKanjiDetailProgress.setViewportView(listKanjiDetailProgress);

        labelKanjiDetailTags.setText("Filter:");

        labelKanjiDetailSearch.setText("Search:");

        checkBoxKanjiDetailSearchAll.setText("Search all");

        scrollPaneKanjiDetailVocabulary.setViewportView(jTable3);

        GroupLayout panelKanjiDetailVocabularyLayout = new GroupLayout(panelKanjiDetailVocabulary);
        panelKanjiDetailVocabulary.setLayout(panelKanjiDetailVocabularyLayout);
        panelKanjiDetailVocabularyLayout.setHorizontalGroup(panelKanjiDetailVocabularyLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(scrollPaneKanjiDetailVocabulary, GroupLayout.DEFAULT_SIZE, 684, Short.MAX_VALUE)
                .addGroup(GroupLayout.Alignment.TRAILING, panelKanjiDetailVocabularyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelKanjiDetailVocabularyLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                // .addComponent(scrollPaneKanjiDetailProgress)
                                .addGroup(panelKanjiDetailVocabularyLayout.createSequentialGroup()
                                        .addGroup(panelKanjiDetailVocabularyLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(labelKanjiDetailTags)
                                                .addComponent(labelKanjiDetailSearch))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(panelKanjiDetailVocabularyLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(textFieldKanjiDetailSearch, GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
                                                .addComponent(textFieldKanjiDetailTags))
                                        .addGap(6, 6, 6)
                                        .addGroup(panelKanjiDetailVocabularyLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(checkBoxKanjiDetailSearchAll, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(comboBoxKanjiDetailReading, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addContainerGap())
        );
        panelKanjiDetailVocabularyLayout.setVerticalGroup(panelKanjiDetailVocabularyLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, panelKanjiDetailVocabularyLayout.createSequentialGroup()
                        // .addComponent(scrollPaneKanjiDetailProgress, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addGroup(panelKanjiDetailVocabularyLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(labelKanjiDetailTags)
                                .addComponent(textFieldKanjiDetailTags, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(comboBoxKanjiDetailReading, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelKanjiDetailVocabularyLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(labelKanjiDetailSearch)
                                .addComponent(checkBoxKanjiDetailSearchAll)
                                .addComponent(textFieldKanjiDetailSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(scrollPaneKanjiDetailVocabulary, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
        );

        GroupLayout panelTabKanjiLayout = new GroupLayout(panelTabKanji);
        panelTabKanji.setLayout(panelTabKanjiLayout);
        panelTabKanjiLayout.setHorizontalGroup(panelTabKanjiLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, panelTabKanjiLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelTabKanjiLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(labelKanjiDetailInfo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(panelKanjiDetailVocabulary, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
        );
        panelTabKanjiLayout.setVerticalGroup(panelTabKanjiLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(panelTabKanjiLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelKanjiDetailInfo, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelKanjiDetailVocabulary, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
        );

        tabbedPane.addTab("Kanji detail", panelTabKanji);

        jLabel5.setText("Filter:");

        jLabel6.setText("Search:");

        jCheckBox6.setText("Search all");

        jScrollPane7.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane7.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jScrollPane7.setViewportView(jList3);

        jLabel7.setText("Must:");

        jComboBox1.setModel(new DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));

        GroupLayout jPanel10Layout = new GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane7)
                                .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel5)
                                                .addComponent(jLabel6)
                                                .addComponent(jLabel7))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(jTextField6, GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
                                                .addComponent(jTextField5)
                                                .addComponent(jTextField7, GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(jCheckBox6)
                                                .addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGap(4, 4, 4)))
                        .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane7, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(jTextField5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7)
                                .addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6)
                                .addComponent(jCheckBox6))
                        .addContainerGap())
        );

        GroupLayout panelTabVocabularyOverviewLayout = new GroupLayout(panelTabVocabularyOverview);
        panelTabVocabularyOverview.setLayout(panelTabVocabularyOverviewLayout);
        panelTabVocabularyOverviewLayout.setHorizontalGroup(panelTabVocabularyOverviewLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jPanel10, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3)
        );
        panelTabVocabularyOverviewLayout.setVerticalGroup(panelTabVocabularyOverviewLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, panelTabVocabularyOverviewLayout.createSequentialGroup()
                        .addComponent(jPanel10, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE))
        );

        // tabbedPane.addTab("Vocabulary overview", panelTabVocabularyOverview);

        jCheckBox7.setText("Learn mode");

        GroupLayout jPanel9Layout = new GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addComponent(jCheckBox7)
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jCheckBox7, GroupLayout.Alignment.TRAILING)
        );

        jScrollPane8.setViewportView(jTable1);

        jScrollPane9.setViewportView(jTable4);

        GroupLayout panelTabVocabularyLayout = new GroupLayout(panelTabVocabulary);
        panelTabVocabulary.setLayout(panelTabVocabularyLayout);
        panelTabVocabularyLayout.setHorizontalGroup(panelTabVocabularyLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, panelTabVocabularyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel9, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                .addComponent(jScrollPane8, GroupLayout.DEFAULT_SIZE, 716, Short.MAX_VALUE)
                .addComponent(jScrollPane9)
        );
        panelTabVocabularyLayout.setVerticalGroup(panelTabVocabularyLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(panelTabVocabularyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel9, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane8, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane9, GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE))
        );

        // tabbedPane.addTab("Vocabulary", panelTabVocabulary);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tabbedPane)
                        .addContainerGap())
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tabbedPane)
                        .addContainerGap())
        );

        tabbedPane.getAccessibleContext().setAccessibleName("");
        tabbedPane.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public JTextField getTextFieldKanjiDetailSearch() {
        return this.textFieldKanjiDetailSearch;
    }

    public JTextField getTextFieldKanjiDetailTags() {
        return this.textFieldKanjiDetailTags;
    }

    public JTextField getTextFieldKanjiOverviewSearch() {
        return this.textFieldKanjiOverviewSearch;
    }

    public JTextField getTextFieldKanjiOverviewTags() {
        return this.textFieldKanjiOverviewTags;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ButtonGroup buttonGroupKanjiOverviewLearnMode;
    private JCheckBox checkBoxKanjiDetailLearn;
    private JCheckBox checkBoxKanjiDetailSearchAll;
    private JCheckBox checkBoxKanjiOverviewDisplayKun;
    private JCheckBox checkBoxKanjiOverviewDisplayOn;
    private JCheckBox checkBoxKanjiOverviewSearchAll;
    private JCheckBox checkBoxKanjiOverviewShuffle;
    private JComboBox<String> comboBoxKanjiDetailReading;
    private JButton jButton2;
    private JCheckBox jCheckBox6;
    private JCheckBox jCheckBox7;
    private JComboBox<String> jComboBox1;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JList<String> jList3;
    private JPanel jPanel10;
    private JPanel jPanel5;
    private JPanel jPanel6;
    private JPanel jPanel9;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JScrollPane jScrollPane7;
    private JScrollPane jScrollPane8;
    private JScrollPane jScrollPane9;
    private JTable jTable1;
    private JTable jTable2;
    private JTable jTable3;
    private JTable jTable4;
    private JTextField jTextField5;
    private JTextField jTextField6;
    private JTextField jTextField7;
    private JLabel labelKanjiDetailInfo;
    private JLabel labelKanjiDetailSearch;
    private JLabel labelKanjiDetailTags;
    private JLabel labelKanjiOverviewResults;
    private JLabel labelKanjiOverviewSearch;
    private JLabel labelKanjiOverviewTags;
    private JList<String> listKanjiDetailProgress;
    private JList<String> listKanjiOverviewProgress;
    private JPanel panelKanjiDetailVocabulary;
    private JPanel panelKanjiOverviewFilter;
    private JPanel panelKanjiStroke;
    private JPanel panelTabKanji;
    private JPanel panelTabKanjiOverview;
    private JPanel panelTabVocabulary;
    private JPanel panelTabVocabularyOverview;
    private JRadioButton radioButtonKanjiOverviewLearnAll;
    private JRadioButton radioButtonKanjiOverviewLearnLeft;
    private JRadioButton radioButtonKanjiOverviewLearnMeaning;
    private JRadioButton radioButtonKanjiOverviewLearnRight;
    private JScrollPane scrollPaneKanjiDetailProgress;
    private JScrollPane scrollPaneKanjiDetailVocabulary;
    private JScrollPane scrollPaneKanjiOverview;
    private JScrollPane scrollPaneKanjiOverviewProgress;
    private JTabbedPane tabbedPane;
    private JTextField textFieldKanjiDetailSearch;
    private JTextField textFieldKanjiDetailTags;
    private JTextField textFieldKanjiOverviewSearch;
    private JTextField textFieldKanjiOverviewTags;
    // End of variables declaration//GEN-END:variables

}

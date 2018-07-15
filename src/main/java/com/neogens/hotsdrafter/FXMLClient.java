package com.neogens.hotsdrafter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

/**
 *
 * @author François Gourdeau
 */
public class FXMLClient implements Initializable {

    Thread th;
    DraftTree tree;
    SequenceState userState;
    SequenceState trialState;
    boolean reinit = false;
    boolean quit = false;
    boolean pause = false;
    boolean teamAIsLeft = false;

    ObservableList<Object> scenarioList = FXCollections.synchronizedObservableList(FXCollections.observableList(new ArrayList<>()));
    ObservableList<Object> alternativesList = FXCollections.synchronizedObservableList(FXCollections.observableList(new ArrayList<>()));

    @FXML
    TableColumn scoreUCT;
    @FXML
    private Button rechercher;
    @FXML
    private CheckBox debug;
    @FXML
    private CheckBox powA;
    @FXML
    private CheckBox powB;
    @FXML
    private TableView scenario;
    @FXML
    private TableView alternatives;
    @FXML
    private ComboBox map;
    @FXML
    private Label labelA;
    @FXML
    private Label labelB;
    @FXML
    private ComboBox ban0A;
    @FXML
    private ComboBox ban0B;
    @FXML
    private ComboBox ban1A;
    @FXML
    private ComboBox ban1B;
    @FXML
    private ComboBox draft1A;
    @FXML
    private ComboBox draft1B;
    @FXML
    private ComboBox draft2B;
    @FXML
    private ComboBox draft2A;
    @FXML
    private ComboBox draft3A;
    @FXML
    private ComboBox ban2B;
    @FXML
    private ComboBox ban2A;
    @FXML
    private ComboBox draft3B;
    @FXML
    private ComboBox draft4B;
    @FXML
    private ComboBox draft4A;
    @FXML
    private ComboBox draft5A;
    @FXML
    private ComboBox draft5B;

    @FXML
    private void reinit(ActionEvent event) {
        ban0A.getSelectionModel().clearSelection();
        ban0B.getSelectionModel().clearSelection();
        ban1A.getSelectionModel().clearSelection();
        ban1B.getSelectionModel().clearSelection();
        draft1A.getSelectionModel().clearSelection();
        draft1B.getSelectionModel().clearSelection();
        draft2B.getSelectionModel().clearSelection();
        draft2A.getSelectionModel().clearSelection();
        draft3A.getSelectionModel().clearSelection();
        ban2B.getSelectionModel().clearSelection();
        ban2A.getSelectionModel().clearSelection();
        draft3B.getSelectionModel().clearSelection();
        draft4B.getSelectionModel().clearSelection();
        draft4A.getSelectionModel().clearSelection();
        draft5A.getSelectionModel().clearSelection();
        draft5B.getSelectionModel().clearSelection();
        reinit = true;
    }

    @FXML
    private void switchTeams(ActionEvent event) {
        if (!teamAIsLeft) {
            labelA.setTranslateX(208);
            labelB.setTranslateX(-208);
            ban0A.setTranslateX(208);
            ban0B.setTranslateX(-208);
            ban1A.setTranslateX(208);
            ban1B.setTranslateX(-208);
            draft1A.setTranslateX(208);
            draft1B.setTranslateX(-208);
            draft2B.setTranslateX(-208);
            draft2A.setTranslateX(208);
            draft3A.setTranslateX(208);
            ban2B.setTranslateX(-208);
            ban2A.setTranslateX(208);
            draft3B.setTranslateX(-208);
            draft4B.setTranslateX(-208);
            draft4A.setTranslateX(208);
            draft5A.setTranslateX(208);
            draft5B.setTranslateX(-208);
            powA.setTranslateX(208);
            powB.setTranslateX(-208);
            teamAIsLeft = true;
        } else {
            labelA.setTranslateX(0);
            labelB.setTranslateX(-0);
            ban0A.setTranslateX(0);
            ban0B.setTranslateX(-0);
            ban1A.setTranslateX(0);
            ban1B.setTranslateX(-0);
            draft1A.setTranslateX(0);
            draft1B.setTranslateX(-0);
            draft2B.setTranslateX(-0);
            draft2A.setTranslateX(0);
            draft3A.setTranslateX(0);
            ban2B.setTranslateX(-0);
            ban2A.setTranslateX(0);
            draft3B.setTranslateX(-0);
            draft4B.setTranslateX(-0);
            draft4A.setTranslateX(0);
            draft5A.setTranslateX(0);
            draft5B.setTranslateX(-0);
            powA.setTranslateX(0);
            powB.setTranslateX(-0);
            teamAIsLeft = false;
        }
    }

    @FXML
    private void quit(ActionEvent event) {
        quit = true;
        Stage stage = (Stage) map.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void search(ActionEvent event) throws Exception {
        rechercher.setDisable(true);
        pause = false;
        if (th == null) {
            th = new Thread(searchTask);
            th.setDaemon(true);
            th.start();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        scenario.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                alternativesList.clear();
                alternativesList.addAll(trialState.justification.get(((Node) newSelection).move).children.values());
                alternatives.getSortOrder().addAll(scoreUCT);
                alternatives.sort();
            }
        });

        scenario.setItems(scenarioList);
        alternatives.setItems(alternativesList);
        SequenceState userState = new SequenceState(tree);

        int defaultMap = Maps.BRAXIS;

        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(Maps.maps));
        ObservableList obList = FXCollections.observableList(list);
        map.setItems(obList);
        map.setValue(Maps.maps[defaultMap]);
        powA.setSelected(true);
        powB.setSelected(true);

        List<String> heroesList = FXCollections.observableList(Arrays.asList(Hero.heroes.clone()));
        
        Collections.sort(heroesList, (String o1, String o2) -> o1.compareTo(o2));
        initPopup(ban0A, heroesList);
        initPopup(ban0B, heroesList);
        initPopup(ban1A, heroesList);
        initPopup(ban1B, heroesList);
        initPopup(draft1A, heroesList);
        initPopup(draft1B, heroesList);
        initPopup(draft2B, heroesList);
        initPopup(draft2A, heroesList);
        initPopup(draft3A, heroesList);
        initPopup(ban2B, heroesList);
        initPopup(ban2A, heroesList);
        initPopup(ban1B, heroesList);
        initPopup(draft3B, heroesList);
        initPopup(draft4B, heroesList);
        initPopup(ban1A, heroesList);
        initPopup(draft4A, heroesList);
        initPopup(draft5A, heroesList);
        initPopup(draft5B, heroesList);
    }

    Task searchTask = new Task<Void>() {
        @Override
        public Void call() throws InterruptedException {

            try {

                while (!quit) {
                    userState = getUserChoices();
                    //VÃ©rifier si l'arbre doit Ãªtre rÃ©initialisÃ©
                    boolean reload = false;
                    if (reinit){
                        reinit=false;
                        reload=true;
                    }
                   
                    boolean realDraft = true;
                    if (tree == null) {
                        reload = true;
                    } else {
                        for (int i = 0; i < userState.moves.length; i++) {
                            if (userState.moves[i] == Hero.OPEN) {
                                realDraft = false;
                            } else {
                                if (!realDraft) {
                                    if (userState.moves[i] != trialState.moves[i]) {
                                        reload = true;
                                    }
                                }
                            }
                        }

                        if (tree.map != Arrays.asList(Maps.maps).indexOf(map.getValue())) {
                            reload = true;
                        }
                        if (tree.powA != powA.isSelected() || tree.powB != powB.isSelected()) {
                            reload = true;
                        }
                    }
                    if (reload) {
                        // if (debug.isSelected()){
                        System.out.println("Reload");
                        // }
                        tree = new DraftTree(Arrays.asList(Maps.maps).indexOf(map.getValue()));
                    }

                    tree.debug = false;
                    tree.powA = powA.isSelected();
                    tree.powB = powB.isSelected();
                    
                    trialState=tree.getSimulation(userState);

                 
                    updateView("Reprise");
                    while (pause) {
                        Thread.sleep(1000);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };

    int getBan(int level) {
        int[] moves = new int[trialState.moves.length];
        for (int i = 0; i < moves.length; i++) {
            moves[i] = -1;
        }
        for (int i = 0; i < level; i++) {
            moves[i] = trialState.moves[i];
        }
        SequenceState state = new SequenceState(tree, moves);
        return trialState.justification.get(trialState.moves[level]).getBestMoveUCT(state, true);
    }

    void updateView(final String message) {

        pause = true;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
//                togglePause.setText(message);
                /**/
                SequenceState actualChoices = getUserChoices();
                int nextChoice = 0;
                for (; nextChoice < actualChoices.moves.length; nextChoice++) {
                    if (actualChoices.moves[nextChoice] == Hero.OPEN) {
                        break;
                    }
                }
                if (nextChoice< actualChoices.moves.length){
                alternativesList.clear();
                alternativesList.addAll(trialState.justification.get(trialState.moves[nextChoice]).children.values());
                } 
                
                alternatives.getSortOrder().addAll(scoreUCT);
                alternatives.sort();
                rechercher.setDisable(false);

                scenarioList.clear();
                scenarioList.addAll(tree.getScenario(trialState));
                scenario.getSelectionModel().clearAndSelect(nextChoice);
                scenario.getFocusModel().focus(nextChoice);

            }
        });

    }

    com.neogens.hotsdrafter.SequenceState getUserChoices() {
        List<String> heroesList = Arrays.asList(Hero.heroes);
        return new SequenceState(tree, new int[]{
            heroesList.indexOf(ban0A.getValue()), // BanA    
            heroesList.indexOf(ban0B.getValue()), // BanB
            heroesList.indexOf(ban1A.getValue()), // BanA    
            heroesList.indexOf(ban1B.getValue()), // BanB
            heroesList.indexOf(draft1A.getValue()), // DraftA
            heroesList.indexOf(draft1B.getValue()), heroesList.indexOf(draft2B.getValue()), //DraftB
            heroesList.indexOf(draft2A.getValue()), heroesList.indexOf(draft3A.getValue()), // DraftA
            heroesList.indexOf(ban2B.getValue()), // BanB  
            heroesList.indexOf(ban2A.getValue()), // BanA999999
            heroesList.indexOf(draft3B.getValue()), heroesList.indexOf(draft4B.getValue()), //DraftB
            heroesList.indexOf(draft4A.getValue()), heroesList.indexOf(draft5A.getValue()), // DraftA
            heroesList.indexOf(draft5B.getValue()) //DraftB
        });
    }

    private void initPopup(ComboBox popup, List heroesList) {

        popup.setEditable(true);

        FilteredList<String> filteredItems = new FilteredList<>(FXCollections.observableList(heroesList), p -> true);

        // Add a listener to the textProperty of the combobox editor. The
        // listener will simply filter the list every time the input is changed
        // as long as the user hasn't selected an item in the list.
        popup.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            final TextField editor = popup.getEditor();
            final String selected = (popup.getSelectionModel().getSelectedItem() != null) ? popup.getSelectionModel().getSelectedItem().toString() : "";

            // This needs run on the GUI thread to avoid the error described
            // here: https://bugs.openjdk.java.net/browse/JDK-8081700.
            Platform.runLater(() -> {
                // If the no item in the list is selected or the selected item
                // isn't equal to the current input, we refilter the list.
                if (selected == null || !selected.equals(editor.getText())) {
                    filteredItems.setPredicate(item -> {
                        // We return true for any items that starts with the
                        // same letters as the input. We use toUpperCase to
                        // avoid case sensitivity.
                        if (item.toUpperCase().startsWith(newValue.toUpperCase())) {
                            return true;
                        } else {
                            return false;
                        }
                    });
                }
            });
        });

        popup.setItems(filteredItems);
    }

}

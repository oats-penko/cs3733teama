package com.teama.controllers;

import java.util.Map;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRadioButton;
import com.teama.drawing.MapDisplay;
import com.teama.mapsubsystem.MapSubsystem;
import com.teama.mapsubsystem.data.*;
import com.teama.mapsubsystem.pathfinding.AStar.AStar;
import com.teama.mapsubsystem.pathfinding.AStar.BeamSearch;
import com.teama.mapsubsystem.pathfinding.BreathFrist.BreathFirst;
import com.teama.mapsubsystem.pathfinding.Dijkstras.Dijkstras;
import com.teama.mapsubsystem.pathfinding.PathAlgorithm;
import com.teama.requestsubsystem.RequestType;
import com.teama.requestsubsystem.StaffType;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sun.misc.Request;

import java.io.IOException;
import java.util.ArrayList;

public class MainScreenSidebarController {
    @FXML
    private JFXListView directions;
    @FXML
    private TitledPane algorithmSelect;
    @FXML
    private JFXRadioButton aStar;

    @FXML
    private JFXRadioButton breadthFirst;

    @FXML
    private JFXRadioButton dijkstra;

    @FXML
    private JFXRadioButton beamSearch;

    @FXML
    private Text selectedNode;

    @FXML
    private JFXButton login;

    //@FXML
    //private VBox generalInfo;

    @FXML
    private JFXComboBox<String> building = new JFXComboBox<>();

    @FXML
    private JFXComboBox<Floor> floor = new JFXComboBox<>();
    private Floor floorName;

    @FXML
    private JFXComboBox<MapNode> longName = new JFXComboBox<>();

    @FXML
    private JFXComboBox<RequestType> typeOfRequest = new JFXComboBox<>();

    //@FXML
    //private JFXTextArea additionalInformation;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton submitButton;


    private ToggleGroup algoToggleGroup;

    private MapSubsystem mapSubsystem;

    private MapDisplay map;

    public void initialize() {
        mapSubsystem = MapSubsystem.getInstance();

        // Add all of the radio buttons to a toggle group
        algoToggleGroup = new ToggleGroup();
        aStar.setToggleGroup(algoToggleGroup);
        breadthFirst.setToggleGroup(algoToggleGroup);
        dijkstra.setToggleGroup(algoToggleGroup);
        beamSearch.setToggleGroup(algoToggleGroup);

        aStar.setUserData(new AStar());
        breadthFirst.setUserData(new BreathFirst());
        dijkstra.setUserData(new Dijkstras());
        beamSearch.setUserData(new BeamSearch(20)); // TODO: make queue size editable

        // Select the default algorithm
        mapSubsystem.setPathGeneratorStrategy((PathAlgorithm)algoToggleGroup.getSelectedToggle().getUserData());

        // When the toggle group changes, make the algorithm reflect that
        algoToggleGroup.selectedToggleProperty().addListener((Observable obs) -> {
            System.out.println("Changed to "+algoToggleGroup.getSelectedToggle().getUserData());
            mapSubsystem.setPathGeneratorStrategy((PathAlgorithm)algoToggleGroup.getSelectedToggle().getUserData());
        });

        //Service Request TidlePane
        building.getItems().clear();
        building.getItems().add("BTM");

        floor.getItems().clear();
        floor.getItems().addAll(
                Floor.SUBBASEMENT,Floor.BASEMENT, Floor.GROUND, Floor.ONE,  Floor.TWO, Floor.THREE);

        typeOfRequest.getItems().clear();
        typeOfRequest.getItems().addAll(
                RequestType.FOOD, RequestType.INTR, RequestType.MAIN, RequestType.SEC, RequestType.TRANS);

        longName.getItems().clear();


    }
/*
    public void setNodeData(){
        floorName = floor.getSelectionModel().getSelectedItem();
        longName.getItems().clear();

        System.out.println(floorName);
        Map<String, MapNode> nodes = HospitalMap.getInstance().getFloorNodes(floorName);
        System.out.println(nodes.keySet());
        for(MapNode n : nodes.values()) {
            if(!n.getNodeType().equals(NodeType.HALL)) {
                longName.getItems().add(n);
            }
        }



    }
    */

    /**
     * Sets the map display in this controller, must be ran before anything else is to be done regarding the map
     * @param map
     */
    public void setMapDisplay(MapDisplay map) {
        this.map = map;
    }

    @FXML
    void onAddEdge(ActionEvent event) {
        // TODO: After the user clicks this button the user should be able to click on another node to add an edge from
        // TODO: the selected node to that one
    }

    @FXML
    void onAddNode(ActionEvent event) {
        // TODO: After the user clicks this button the user should be able to click a point on the screen
        // TODO: and specify attributes for the nodes creation
    }

    @FXML
    void onSelectNode(ActionEvent event) {
        // TODO: After the button is clicked the user should be able to select a node with the mouse cursor
        // TODO: and it should display in the selectedNode text
    }

    private boolean shownHiddenNodesAndEdges = false;

    private ArrayList<String> shownNodes;
    private ArrayList<String> shownEdges;
    private Floor hiddenDispFloor;

    @FXML
    void onToggleHiddenNodesAndEdges() {
        if(!shownHiddenNodesAndEdges || !map.getCurrentFloor().equals(hiddenDispFloor)) {
            // Show all hidden nodes and edges
            shownNodes = new ArrayList<>();
            shownEdges = new ArrayList<>();
            for (MapNode n : mapSubsystem.getInvisibleFloorNodes(map.getCurrentFloor()).values()) {
                new DrawNodeInstantly(n).displayOnScreen(map);
                shownNodes.add(n.getId());
                for (MapEdge e : n.getEdges()) {
                    new DrawEdgeInstantly(e).displayOnScreen(map);
                    shownEdges.add(e.getId());
                }
            }
            shownHiddenNodesAndEdges = true;
            hiddenDispFloor = map.getCurrentFloor();
        } else {
            // Hide all hidden nodes and edges
            for(String id : shownNodes) {
                map.deletePoint(id);
            }
            for(String id : shownEdges) {
                map.deleteLine(id);
            }
            shownHiddenNodesAndEdges = false;
        }
    }


    @FXML
    void onLogIn(ActionEvent event) {
        ///Dialog d = new Dialog();
        try {
            /*
            d.getDialogPane().setContent(FXMLLoader.load(getClass().getResource("/StaffLogIn.fxml")));
            d.show();
            */
            Stage loginPopup = new Stage();

            loginPopup.setTitle("B&W Login");
            FXMLLoader loader = new FXMLLoader();
            //StaffLoginController loginController = loader.getController();


            Scene loginScene = new Scene(loader.load(getClass().getResource("/LogInScreen.fxml")));
            //loginPopup.setScene((AnchorPane)));
            StaffLoginController loginController = new StaffLoginController();

            loginController.setLoggedIn(false);

            loginController.getLoggedInProperty().addListener((obs, before, nowLoggedIn) -> {
                if (nowLoggedIn) {
                    loginPopup.hide();
                    login.setVisible(false);
                }
            });


            loginPopup.setScene(loginScene);
            loginPopup.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hideLoginButton() {
        login.setVisible(false);
    }
}

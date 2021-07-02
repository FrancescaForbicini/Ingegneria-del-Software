package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.turn_taker.ClientPlayer;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.Eligible;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.view.gui.scene_controller.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages the scene of the GUI
 */
public class SceneManager {
    private static final String CONNECTION = "Connection";
    private static final String LOGIN = "Login";
    private static final String PICK_AN_ACTION = "PickAnAction";
    private static final String SHOW_MARKET = "ShowMarket";
    private static final String SHOW_DEVELOPMENT_CARDS = "ShowDevelopmentCards";
    private static final String SHOW_PLAYER = "ShowPlayer";
    private static final String CHOOSE_PLAYER = "ChoosePlayer";
    private static final String PICK_LEADER_CARDS = "PickLeaderCards";
    private static final String PICK_RESOURCE = "PickResource";
    private static final String CHOOSE_DEPOT = "ChooseDepot";
    private static final String CHOOSE_TRADING_RULES = "ChooseTradingRules";
    private static final String CHOOSE_SLOT = "ChooseSlot";
    private static final String CHOOSE_QUANTITY = "ChooseQuantity";

    private Map<String,ImageView> nodesCache = new HashMap<>();
    private Map<String, Parent> scenes;
    private Stage stage;
    private String livingSceneFileNames[] = {PICK_AN_ACTION, SHOW_MARKET, SHOW_DEVELOPMENT_CARDS};
    private static String BASE_CONTROLLER_PATH = "it.polimi.ingsw.view.gui.scene_controller";
    private static SceneManager instance;
    private boolean ready = false;
    private ClientGameObserverProducer gameObserverProducer;

    public SceneManager() {}

    /**
     * Initializes the scene manager
     * @return instance of the scene manager
     */
    public static SceneManager getInstance() {
        if (instance == null)
            instance = new SceneManager();
        return instance;
    }

    /**
     * Sets up the scene
     * @param stage where to show the scene
     */
    public synchronized void setup(Stage stage) {
        stage.setScene(new Scene(new Pane()));
        this.stage = stage;
        scenes = new HashMap<>();
        stage.show();
        ready = true;
        notifyAll();
    }

    /**
     * Starts the scene
     *
     * @param gameObserverProducer observer of the game
     */
    public void start(ClientGameObserverProducer gameObserverProducer) {
        try {
            initScenes(gameObserverProducer);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }



    /**
     * Initializes the scene that are reactive, so has to be updated
     *
     * @param gameObserverProducer the observer of the game
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public void initScenes(ClientGameObserverProducer gameObserverProducer) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        this.gameObserverProducer = gameObserverProducer;
        for (String sceneFileName : livingSceneFileNames) {
            String sceneController = "." + sceneFileName + "Controller";
            ReactiveObserver reactiveObserver = (ReactiveObserver) Class.forName(BASE_CONTROLLER_PATH + sceneController)
                    .getConstructor(ClientGameObserverProducer.class).newInstance(gameObserverProducer);
            scenes.put(sceneFileName, loadScene(sceneFileName, Optional.of(reactiveObserver)));
        }
    }

    /**
     * Shows a message where the client has to choose from two buttons
     *
     * @param message message to show
     */
    public void showConfirmation(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message);
            alert.showAndWait().ifPresent(response -> {
                if (ButtonType.OK.equals(response)) {
                    GUIController.getInstance().setAckMessage(true);
                } else if (ButtonType.CANCEL.equals(response)) {
                    GUIController.getInstance().setAckMessage(false);
                }
            });
        });
    }

    public void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
            alert.showAndWait().ifPresent(buttonType -> GUIController.getInstance().setAckMessage(true));
        });
    }

    /**
     * Loads a scene
     * @param fileName file where to take the scene
     * @param controller controller corresponding to the scene
     * @return node corresponding to the file fxml
     */
    public Parent loadScene(String fileName, Optional<Object> controller) {
        FXMLLoader loader = new FXMLLoader(GUIController.class.getClassLoader().getResource("FXML/" + fileName + ".fxml"));
        controller.ifPresent(loader::setController);
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    /**
     * Switches the scene
     * @param parent scene to show
     */
    public void switchScene(Parent parent) {
        System.gc();
        Platform.runLater(() -> stage.getScene().setRoot(parent));
    }

    private void switchScene(String sceneFileName) {
        switchScene(scenes.get(sceneFileName));
    }

    public void connection() {
        switchScene(loadScene(CONNECTION, Optional.empty()));
    }

    public void login() {
        switchScene(loadScene(LOGIN, Optional.empty()));
    }

    public void pickAnAction() {
        switchScene(PICK_AN_ACTION);
    }

    public void showMarket() {
        switchScene(SHOW_MARKET);
    }

    public void showPlayer(ClientPlayer player) {
        ShowPlayerController controller = new ShowPlayerController(player);
        switchScene(loadScene(SHOW_PLAYER, Optional.of(controller)));
    }

    public void buyDevelopmentCards(ArrayList<DevelopmentCard> developmentCards){
        ShowDevelopmentCardsController controller = new ShowDevelopmentCardsController(gameObserverProducer,true,developmentCards);
        switchScene(loadScene(SHOW_DEVELOPMENT_CARDS,Optional.of(controller)));
    }

    public void showShowDevelopmentCards(ArrayList<DevelopmentCard> developmentCards) {
        ShowDevelopmentCardsController controller = new ShowDevelopmentCardsController(gameObserverProducer,false,developmentCards);
        switchScene(loadScene(SHOW_DEVELOPMENT_CARDS,Optional.of(controller)));
    }

    public void chooseSlot(ArrayList<DevelopmentSlot> slotsAvailable){
        ChooseSlotController controller = new ChooseSlotController(slotsAvailable);
        switchScene(loadScene(CHOOSE_SLOT,Optional.of(controller)));
    }

    /**
     * Waits that all messages to start the game have been received
     */
    public synchronized void waitStarted() {
        while(!ready){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void choosePlayer(ArrayList<ClientPlayer> clientPlayersToChoose) {
        ChoosePlayerController controller = new ChoosePlayerController(clientPlayersToChoose);
        switchScene(loadScene(CHOOSE_PLAYER, Optional.of(controller)));
    }

    public void pickLeaderCards(ArrayList<LeaderCard> proposedCards) {
        PickLeaderCardsController controller = new PickLeaderCardsController(proposedCards);
        switchScene(loadScene(PICK_LEADER_CARDS, Optional.of(controller)));
    }

    public void chooseResource(ArrayList<ResourceType> resourcesToChoose) {
        PickResourceController controller = new PickResourceController(resourcesToChoose);
        switchScene(loadScene(PICK_RESOURCE, Optional.of(controller)));
    }

    public void chooseResource() {
        PickResourceController controller = new PickResourceController(ResourceType.getAllValidResources());
        switchScene(loadScene(PICK_RESOURCE, Optional.of(controller)));
    }

    public void chooseDepot(ArrayList<WarehouseDepot> depotsToChoose) {
        ChooseDepotController controller = new ChooseDepotController(depotsToChoose);
        switchScene(loadScene(CHOOSE_DEPOT, Optional.of(controller)));
    }

    public void chooseLine() {
        ShowMarketController controller = new ShowMarketController(gameObserverProducer, true);
        switchScene(loadScene(SHOW_MARKET, Optional.of(controller)));
    }

    public void chooseProductionToActivate(ArrayList<Eligible> availableProductions) {
        ChooseProductionToActivate controller = new ChooseProductionToActivate(availableProductions);
        switchScene(loadScene(CHOOSE_TRADING_RULES, Optional.of(controller)));
    }

    public void chooseQuantity(ResourceType resourceToTake, int maxQuantity){
        ChooseQuantityController controller = new ChooseQuantityController(resourceToTake, maxQuantity);
        switchScene(loadScene(CHOOSE_QUANTITY, Optional.of(controller)));
    }

    /**
     * Gets the image from a specific path
     * @param path path of the image to take
     * @return image corresponding to the path
     */
    public Node getNode(String path){
        if (nodesCache.containsKey(path))
            return nodesCache.get(path);
        Image cardFile = new Image(path);
        ImageView imageView = new ImageView();
        imageView.setImage(cardFile);
        nodesCache.put(path,imageView);
        return imageView;
    }

    /**
     * Gets the image from a specific path
     * @param path path of the image to take
     * @param height height of the image
     * @param width width of the image
     * @return image corresponding to the path , set with the right height and width
     */
    public Node getNode(String path, double height, double width){
        ImageView imageView = (ImageView) getNode(path);
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        return imageView;
    }

    /**
     * Gets the image of a resource
     *
     * @param resourceType type of resource to take the image
     * @return image of the resource
     */
    public ImageView getResourceImage(ResourceType resourceType){
        return (ImageView) getNode(ResourceType.getPath(resourceType));
    }

}

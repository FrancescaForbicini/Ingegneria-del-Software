package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.client.turn_taker.ClientPlayer;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.AdditionalDepot;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.view.gui.GUIController;
import it.polimi.ingsw.view.gui.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;


public class ShowPlayerController {
    private final ClientPlayer clientPlayer;
    @FXML
    private ImageView card00;
    @FXML
    private ImageView card01;
    @FXML
    private ImageView card02;
    @FXML
    private ImageView card10;
    @FXML
    private ImageView card11;
    @FXML
    private ImageView card12;
    @FXML
    private ImageView card20;
    @FXML
    private ImageView card21;
    @FXML
    private ImageView card22;
    @FXML
    private ImageView depot10;
    @FXML
    private ImageView depot20;
    @FXML
    private ImageView depot21;
    @FXML
    private ImageView depot30;
    @FXML
    private ImageView depot31;
    @FXML
    private ImageView depot32;
    @FXML
    private Label coinsStrongbox;
    @FXML
    private Label stonesStrongbox;
    @FXML
    private Label servantsStrongbox;
    @FXML
    private Label shieldsStrongbox;
    @FXML
    private Label usernameLabel;
    @FXML
    private ImageView leader0;
    @FXML
    private ImageView leader1;
    @FXML
    private ImageView additionalDepot00;
    @FXML
    private ImageView additionalDepot01;
    @FXML
    private ImageView additionalDepot10;
    @FXML
    private ImageView additionalDepot11;
    @FXML
    private ImageView cell0;
    @FXML
    private ImageView cell1;
    @FXML
    private ImageView cell2;
    @FXML
    private ImageView cell3;
    @FXML
    private ImageView cell4;
    @FXML
    private ImageView cell5;
    @FXML
    private ImageView cell6;
    @FXML
    private ImageView cell7;
    @FXML
    private ImageView cell8;
    @FXML
    private ImageView cell9;
    @FXML
    private ImageView cell10;
    @FXML
    private ImageView cell11;
    @FXML
    private ImageView cell12;
    @FXML
    private ImageView cell13;
    @FXML
    private ImageView cell14;
    @FXML
    private ImageView cell15;
    @FXML
    private ImageView cell16;
    @FXML
    private ImageView cell17;
    @FXML
    private ImageView cell18;
    @FXML
    private ImageView cell19;
    @FXML
    private ImageView cell20;
    @FXML
    private ImageView cell21;
    @FXML
    private ImageView cell22;
    @FXML
    private ImageView cell23;
    @FXML
    private ImageView cell24;
    @FXML
    private ImageView tile2;
    @FXML
    private ImageView tile3;
    @FXML
    private ImageView tile4;
    @FXML
    private Button back;


    public ShowPlayerController(ClientPlayer clientPlayer) {
        this.clientPlayer = clientPlayer;
    }

    public void initialize(){
        back.setOnAction(actionEvent -> GUIController.getInstance().setAckMessage(true));
        usernameLabel.setText(clientPlayer.getUsername());
        setLeaderCardsActive();
        setDevelopmentSlots();
        setStrongbox();
        setWarehouse();
        setFaithTrack();
    }

    private void setLeaderCardsActive(){//TODO custom
        List<LeaderCard> leaderCards = clientPlayer.getActiveLeaderCards();
        if(leaderCards.size()>0){
            ImageView imageView = (ImageView) SceneManager.getInstance().getNode(leaderCards.get(0).getPath());
            leader0.setImage(imageView.getImage());
            if(leaderCards.get(0) instanceof AdditionalDepot){
                setResourcesInAdditionalDepot(clientPlayer.getWarehouse().getAdditionalDepots().get(0),0);
            }
            if(leaderCards.size()>1){
                ImageView imageView1 = (ImageView) SceneManager.getInstance().getNode(leaderCards.get(1).getPath());
                leader1.setImage(imageView1.getImage());
                if(leaderCards.get(0) instanceof AdditionalDepot){
                    setResourcesInAdditionalDepot(clientPlayer.getWarehouse().getAdditionalDepots().get(1),1);
                }
            }
        }
    }

    private void setDevelopmentSlots(){//TODO custom
        ImageView imageView;
        DevelopmentCard developmentCard;
        DevelopmentSlot[] developmentSlots = clientPlayer.getDevelopmentSlots();
        for(int i=0; i<developmentSlots.length; i++){
            for(int j=0; j<developmentSlots[i].getCards().size(); j++){
                ArrayList<DevelopmentCard> slot = new ArrayList<>(developmentSlots[i].getCards());
                developmentCard = slot.get(j);
                imageView = getDevelopmentImageView(i,j);
                ImageView cached = (ImageView) SceneManager.getInstance().getNode(developmentCard.getPath());
                imageView.setImage(cached.getImage());
            }
        }
    }
    private void setStrongbox(){
        Label label;
        for(ResourceType resourceType : clientPlayer.getStrongbox().keySet()){
            label = getStrongboxLabel(resourceType);
            label.setText(String.valueOf(clientPlayer.getStrongbox().get(resourceType)));
        }
    }

    private void setWarehouse(){
        ArrayList<WarehouseDepot> depots = clientPlayer.getWarehouse().getWarehouseDepots();
        for(WarehouseDepot depot : depots){
            setResourcesInDepot(depot);
        }
    }
    private void setFaithTrack(){//TODO custom
        ImageView cell = getCell(clientPlayer.getFaithTrack().getMarkers().get(clientPlayer.getUsername()));
        ImageView cached = (ImageView) SceneManager.getInstance().getNode("GUIResources/Punchboard/Faith/Faithpoint.png");
        cell.setImage(cached.getImage());
    }

    private void setResourcesInAdditionalDepot(WarehouseDepot additionalDepot, int leaderSlot){
        ResourceType resourceType = additionalDepot.getResourceType();
        ImageView imageView0;
        ImageView imageView1;
        if(leaderSlot==0){
            imageView0 = additionalDepot00;
            imageView1 = additionalDepot01;
        }else{
            imageView0 = additionalDepot10;
            imageView1 = additionalDepot11;
        }
        if(!additionalDepot.isEmpty()){
            imageView0.setImage(SceneManager.getInstance().getResourceImage(resourceType).getImage());
            if(additionalDepot.getQuantity()>1){
                imageView1.setImage(SceneManager.getInstance().getResourceImage(resourceType).getImage());
            }
        }
    }

    private ImageView getDevelopmentImageView(int slot, int card){
        if(slot==0){
            if(card==0){
                return card00;
            }
            if(card==1){
                return card01;
            }
            if(card==2){
                return card02;
            }
        }
        if(slot==1){
            if(card==0){
                return card10;
            }
            if(card==1){
                return card11;
            }
            if(card==2){
                return card12;
            }
        }
        if(slot==2){
            if(card==0){
                return card20;
            }
            if(card==1){
                return card21;
            }
            if(card==2){
                return card22;
            }
        }
        return card22;
    }

    private Label getStrongboxLabel(ResourceType resourceType){
        if(resourceType.equals(ResourceType.Coins)) {
            return coinsStrongbox;
        }
        if(resourceType.equals(ResourceType.Stones)) {
            return stonesStrongbox;
        }
        if(resourceType.equals(ResourceType.Servants)) {
            return servantsStrongbox;
        }
        if(resourceType.equals(ResourceType.Shields)) {
            return shieldsStrongbox;
        }
        return null;
    }

    private void setResourcesInDepot(WarehouseDepot depot){
        ImageView imageView;
        int level = depot.getLevel();
        if(!depot.isEmpty()){
            switch(level){
                case 1:
                    imageView = depot10;
                    imageView.setImage(SceneManager.getInstance().getResourceImage(depot.getResourceType()).getImage());
                    break;
                case 2:
                    imageView = depot20;
                    imageView.setImage(SceneManager.getInstance().getResourceImage(depot.getResourceType()).getImage());
                    if(depot.getQuantity()>1){
                        imageView = depot21;
                        imageView.setImage(SceneManager.getInstance().getResourceImage(depot.getResourceType()).getImage());
                    }
                    break;
                    
                case 3:
                    imageView = depot30;
                    imageView.setImage(SceneManager.getInstance().getResourceImage(depot.getResourceType()).getImage());
                    if(depot.getQuantity()>1){
                        imageView = depot31;
                        imageView.setImage(SceneManager.getInstance().getResourceImage(depot.getResourceType()).getImage());
                        if(depot.getQuantity()>2){
                            imageView = depot32;
                            imageView.setImage(SceneManager.getInstance().getResourceImage(depot.getResourceType()).getImage());
                        }
                    }
                    break;
                    
                default:
                    
            }
        }
    }

    private ImageView getCell (int position){
        switch(position){
            case 0:
                return cell0;
            case 1:
                return cell1;
            case 2:
                return cell2;
            case 3:
                return cell3;
            case 4:
                return cell4;
            case 5:
                return cell5;
            case 6:
                return cell6;
            case 7:
                return cell7;
            case 8:
                return cell8;
            case 9:
                return cell9;
            case 10:
                return cell10;
            case 11:
                return cell11;
            case 12:
                return cell12;
            case 13:
                return cell13;
            case 14:
                return cell14;
            case 15:
                return cell15;
            case 16:
                return cell16;
            case 17:
                return cell17;
            case 18:
                return cell18;
            case 19:
                return cell19;
            case 20:
                return cell20;
            case 21:
                return cell21;
            case 22:
                return cell22;
            case 23:
                return cell23;
            case 24:
                return cell24;
            default:
                return null;
        }
    }

}

package it.polimi.ingsw.client.action;

import it.polimi.ingsw.message.MessageDTO;
import it.polimi.ingsw.message.action_message.PickStartingResourcesDTO;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ActionUtils {
    private ArrayList<Predicate<MessageDTO>> noActionPredicates;
    private ArrayList<Supplier<MessageDTO>> noActionResolvers;
    private static ActionUtils instance;

    public static ActionUtils getInstance() {
        if (instance == null)
            instance = new ActionUtils();
        return instance;
    }

    private ActionUtils() {
        noActionPredicates = new ArrayList<>();
        noActionResolvers = new ArrayList<>();
        noActionPredicates.add(messageDTO -> messageDTO instanceof PickStartingResourcesDTO && ((PickStartingResourcesDTO) messageDTO).getNumber() == 0);
        noActionResolvers.add(() -> new PickStartingResourcesDTO(0, new ArrayList<>()));
    }


    public Optional<MessageDTO> noUserRequiredAction(MessageDTO messageDTO) {
        for (int i = 0; i < noActionPredicates.size(); i++) {
            if (noActionPredicates.get(i).test(messageDTO)) {
                return Optional.of(noActionResolvers.get(i).get());
            }
        }
        return Optional.empty();
    }
}

package it.polimi.ingsw.client.action;

import it.polimi.ingsw.message.MessageDTO;
import it.polimi.ingsw.message.action_message.PickStartingResourcesDTO;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Manages the messages to operate the actions
 */
public class ActionUtils {
    private final ArrayList<Predicate<MessageDTO>> noActionPredicates;
    private final ArrayList<Supplier<MessageDTO>> noActionResolvers;
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


    /**
     * Handles a set of actions which require no user interaction. E.g. if an user receives a message with the semantic
     * "pick 0 resources", he can do nothing and his response can be generated without him
     *
     * @param messageDTO A message which could cause a "no action"
     * @return Empty if the user action is required, the "no action" message otherwise
     */
    public Optional<MessageDTO> noUserRequiredAction(MessageDTO messageDTO) {
        for (int i = 0; i < noActionPredicates.size(); i++) {
            if (noActionPredicates.get(i).test(messageDTO)) {
                return Optional.of(noActionResolvers.get(i).get());
            }
        }
        return Optional.empty();
    }
}

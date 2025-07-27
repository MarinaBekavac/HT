package mb.projects.ht.enums;

import java.util.Arrays;
import java.util.Objects;

public enum ActionEnum {

    ADD(1, "add"),
    DELETE(2, "delete"),
    MODIFY(3, "modify");

    private final Integer id;
    private final String name;

    ActionEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Optional: Get enum by ID
    public static ActionEnum fromId(Integer id) {
        for (ActionEnum action : ActionEnum.values()) {
            if (Objects.equals(action.id, id)) {
                return action;
            }
        }
        throw new IllegalArgumentException("No ActionEnum with id " + id + " found");
    }

    // Optional: Get enum by name (case insensitive)
    public static ActionEnum fromName(String name) {
        for (ActionEnum action : ActionEnum.values()) {
            if (action.name.equalsIgnoreCase(name)) {
                return action;
            }
        }
        throw new IllegalArgumentException("No ActionEnum with name " + name + " found");
    }

    public static boolean isValidActionId(int actionId) {
        return Arrays.stream(values())
                .anyMatch(action -> action.id == actionId);
    }

    @Override
    public String toString() {
        return name;
    }
}

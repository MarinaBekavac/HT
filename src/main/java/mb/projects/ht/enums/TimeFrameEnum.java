package mb.projects.ht.enums;

public enum TimeFrameEnum {

    DAY(1, "day"),
    MONTH(2, "month"),
    YEAR(3, "year");

    private final Integer id;
    private final String name;

    TimeFrameEnum(Integer id, String name) {
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
    public static TimeFrameEnum fromId(long id) {
        for (TimeFrameEnum timeFrame : TimeFrameEnum.values()) {
            if (timeFrame.id == id) {
                return timeFrame;
            }
        }
        throw new IllegalArgumentException("No TimeFrame with id " + id + " found");
    }

    // Optional: Get enum by name (case insensitive)
    public static TimeFrameEnum fromName(String name) {
        for (TimeFrameEnum timeFrame : TimeFrameEnum.values()) {
            if (timeFrame.name.equalsIgnoreCase(name)) {
                return timeFrame;
            }
        }
        throw new IllegalArgumentException("No TimeFrame with name " + name + " found");
    }

    @Override
    public String toString() {
        return name;
    }

}

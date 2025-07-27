package mb.projects.ht.enums;

public enum PricingTypeEnum {

    ONE_TIME(1, "one_time"),
    RECURRING(2, "recurring");

    private final long id;
    private final String name;

    PricingTypeEnum(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Optional: Get enum by ID
    public static PricingTypeEnum fromId(long id) {
        for (PricingTypeEnum pricingType : PricingTypeEnum.values()) {
            if (pricingType.id == id) {
                return pricingType;
            }
        }
        throw new IllegalArgumentException("No PricingTypeEnum with id " + id + " found");
    }

    // Optional: Get enum by name (case insensitive)
    public static PricingTypeEnum fromName(String name) {
        for (PricingTypeEnum pricingType : PricingTypeEnum.values()) {
            if (pricingType.name.equalsIgnoreCase(name)) {
                return pricingType;
            }
        }
        throw new IllegalArgumentException("No PricingTypeEnum with name " + name + " found");
    }

    @Override
    public String toString() {
        return name;
    }

}

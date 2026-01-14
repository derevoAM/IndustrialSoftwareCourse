package zoo.cages;

public enum EnclosureType {
    MAMMAL_ENCLOSURE("Вольер для млекопитающих"),
    BIRD_AVIARY("Птичник"),
    REPTILE_TERRARIUM("Террариум для рептилий");

    private final String typeName;

    EnclosureType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public String toString() {
        return typeName;
    }
}
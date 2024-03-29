package algos;

public enum NodeTagEnum {
    WHITE(0),
    GRAY(1),
    BLACK(2);

    private final int value;

    private NodeTagEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

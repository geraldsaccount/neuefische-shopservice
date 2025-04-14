public enum OrderStatus {
    PROCESSING("Processing"),
    IN_DELIVERY("In Delivery"),
    COMPLETED("Completed");

    String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

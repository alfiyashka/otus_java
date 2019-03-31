public enum Fruits {
    Banan("banan"), Apple("apple"), Lemon("lemon");
    private String value;

    Fruits(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

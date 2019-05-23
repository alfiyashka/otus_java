package atm;

public enum Nominal {
    RUB_50,
    RUB_100,
    RUB_500,
    RUB_1000,
    RUB_2000,
    RUB_5000;

    public int getValue() {
        switch (this){
            case RUB_50:
                return 50;
            case RUB_100:
                return 100;
            case RUB_500:
                return 500;
            case RUB_1000:
                return 1000;
            case RUB_2000:
                return 2000;
            case RUB_5000:
                return 5000;
            default:
                throw new RuntimeException("Unsupported nominal type");
        }
    }
}

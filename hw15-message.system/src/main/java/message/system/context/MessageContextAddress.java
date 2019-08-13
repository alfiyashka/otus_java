package message.system.context;

import message.system.messageSystem.Address;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "message-address")
public class MessageContextAddress {

    private Address frontAddress;
    private Address dbAddress;

    public Address getFrontAddress() {
        return frontAddress;
    }

    public void setFrontAddress(Address frontAddress) {
        this.frontAddress = frontAddress;
    }

    public Address getDbAddress() {
        return dbAddress;
    }

    public void setDbAddress(Address dbAddress) {
        this.dbAddress = dbAddress;
    }

}

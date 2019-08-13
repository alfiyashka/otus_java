package message.system.messageSystem;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public final class MessageSystem {
    private final static Logger logger = Logger.getLogger(MessageSystem.class.getName());

    private final List<Thread> workers;
    private final Map<Address, LinkedBlockingQueue<Message>> messagesMap;

    public MessageSystem() {
        workers = new ArrayList<>();
        messagesMap = new HashMap<>();
    }

    public void addAddressee(Addressee addressee) {
        messagesMap.put(addressee.getAddress(), new LinkedBlockingQueue<>());
        start(addressee.getAddress(), addressee);
    }

    public void sendMessage(Message message) {
        messagesMap.get(message.getTo()).add(message);
    }


    public void start(Address address, Addressee addressee) {
        String name = "MS-worker-" + address.getId();
        Thread thread = new Thread(() -> {
            LinkedBlockingQueue<Message> queue = messagesMap.get(address);
            while (true) {
                try {
                    Message message = queue.take();
                    message.exec(addressee);
                } catch (InterruptedException e) {
                    logger.log(Level.INFO, "Thread interrupted. Finishing: " + name);
                    return;
                }
                catch (Exception e) {
                    logger.log(Level.WARNING,
                            String.format("Thread %s. Occurred following exception: %s",
                                    name, e.getMessage()));
                    e.printStackTrace();
                }
            }
        });
        thread.setName(name);
        thread.start();
        workers.add(thread);
    }

    public void dispose() {
        workers.forEach(Thread::interrupt);
    }
}
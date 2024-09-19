package pl.pwr.recruitringcore.factory;

import org.springframework.stereotype.Component;
import pl.pwr.recruitringcore.model.enums.Role;
import pl.pwr.recruitringcore.producer.UserDataProducer;

import java.util.List;

@Component
public class UserDataProducerFactoryImpl implements UserDataProducerFactory {

    private final List<UserDataProducer> userDataProducers;

    public UserDataProducerFactoryImpl(List<UserDataProducer> userDataProducers) {
        this.userDataProducers = userDataProducers;
    }

    @Override
    public UserDataProducer get(Role role) {
        return userDataProducers.stream().filter(producer -> producer.supports(role)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Role not implemented"));
    }
}

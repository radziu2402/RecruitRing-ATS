package pl.pwr.recruitringcore.factory;

import pl.pwr.recruitringcore.model.enums.Role;
import pl.pwr.recruitringcore.producer.UserDataProducer;

public interface UserDataProducerFactory {

    UserDataProducer get(Role role);
}

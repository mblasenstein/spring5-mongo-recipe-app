package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Set;

public interface UnitOfMeasureService {

    Flux<UnitOfMeasureCommand> listAllUoms();
}

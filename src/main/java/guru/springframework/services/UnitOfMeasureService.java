package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;

import java.util.List;
import java.util.Set;

public interface UnitOfMeasureService {

    List<UnitOfMeasureCommand> listAllUoms();
}

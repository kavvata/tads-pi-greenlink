package ifpr.pgua.eic.greenlink.controllers;

import java.util.function.Consumer;

import ifpr.pgua.eic.greenlink.models.entities.Tarefa;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class TarefaListCell {

    public static Callback<ListView<Tarefa>, ListCell<Tarefa>> geraCellFactory(Consumer<Tarefa> marcarFeito, Callback<Tarefa, String> toString) {

        Callback<Tarefa, ObservableValue<Boolean>> acaoCheckBox = new Callback<>() {

            @Override
            public ObservableValue<Boolean> call(Tarefa tarefa) {

                BooleanProperty observable = new SimpleBooleanProperty();

                observable.addListener((obs, antigo, novo) -> {
                    if (novo) {
                        marcarFeito.accept(tarefa);
                    }
                });

                return observable;
            }
        };
        StringConverter<Tarefa> converter = new StringConverter<Tarefa>() {

            @Override
            public Tarefa fromString(String arg0) {
                return null;
            }

            @Override
            public String toString(Tarefa t) {
                return toString.call(t);
            }
            
        };
        return CheckBoxListCell.forListView(acaoCheckBox, converter);
    }
}
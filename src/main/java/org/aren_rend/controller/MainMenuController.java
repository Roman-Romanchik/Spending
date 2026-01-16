package org.aren_rend.controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import lombok.RequiredArgsConstructor;
import org.aren_rend.SpendingService;
import org.aren_rend.model.MainMenuModel;
import org.aren_rend.utilities.Validator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MainMenuController implements Initializable {
	@FXML
	private Button buttonAdd;
	@FXML
	private TextField fieldSpendingName, fieldSpendingPrice, fieldAmount, fieldOtherCategory, fieldOtherSubcategory;
	@FXML
	private ChoiceBox<String> choiceBoxCategory, choiceBoxSubcategory;
	@FXML
	private Label labelMonthSpending, labelAllSpending;
	@FXML
	private ListView<String> listViewForNotes;

	private final ObservableList<String> notes = FXCollections.observableArrayList();
	MainMenuModel mmm;
	Validator validator;
    private final SpendingService spendingService;
    private boolean isReadyToSave = false;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
        setButtonAddProperty();
        disableHideCategoryField();
        disableHideSubcategoryField();
        displayNotesInBase();
        fillCategoryChoiceBox();
		validator = new Validator();
		mmm = new MainMenuModel();

	}

    private void disableHideCategoryField() {
        fieldOtherCategory.clear();
        fieldOtherCategory.setDisable(true);
        fieldOtherCategory.setVisible(false);
    }

    private void disableHideSubcategoryField() {
        fieldOtherSubcategory.clear();
        fieldOtherSubcategory.setDisable(true);
        fieldOtherSubcategory.setVisible(false);
    }

    private void enableCategoriesField() {
        fieldOtherCategory.setDisable(false);
        fieldOtherCategory.setVisible(true);
    }

    private void enableSubcategoryField() {
        fieldOtherSubcategory.setDisable(false);
        fieldOtherSubcategory.setVisible(true);
    }

	private void displayNotesInBase() {
        notes.setAll(spendingService.displaySavedNotes());
        listViewForNotes.setItems(notes);
	}


    private void fillCategoryChoiceBox() {
        ObservableList<String> items = choiceBoxCategory.getItems();
        items.addAll("Food", "Sport", "Tech", "Transport", "Other");
        choiceBoxCategory.setValue("Choose");
    }

    @FXML
    private void changeSubcategory() {
        if(choiceBoxCategory.getValue().equals("Other")) {
            enableCategoriesField();
            enableSubcategoryField();
        } else {
            disableHideSubcategoryField();
            disableHideCategoryField();
        }
            choiceBoxSubcategory.getItems().clear();
            fillSubcategoryChoiceBox();
    }

    private void fillSubcategoryChoiceBox() {
        ObservableList<String> items = choiceBoxSubcategory.getItems();
        choiceBoxSubcategory.setValue("Choose");
        switch(choiceBoxCategory.getValue()) {
            case "Food" -> items.addAll("Water", "FF", "Normal");
            case "Sport" -> items.addAll("GYM", "Protein", "Creatine", "Stuff");
            case "Tech" -> items.addAll("Audio", "Video", "Monitor", "Phone", "PC");
            case "Transport" -> items.addAll("Taxi", "Bus", "Car");
            default -> items.add("Other");
        }
    }


    @FXML
    private void saveNote() {
        checkReadyNote();
        if(isReadyToSave) {
            String note;
            if(!choiceBoxCategory.getValue().equals("Other")) {
                note = spendingService.saveNote(LocalDate.now(), choiceBoxCategory.getValue(), choiceBoxSubcategory.getValue(),
                        fieldSpendingName.getText(), Integer.parseInt(fieldAmount.getText()), Integer.parseInt(fieldSpendingPrice.getText()));
            } else {
                note = spendingService.saveNote(LocalDate.now(), fieldOtherCategory.getText(), fieldOtherSubcategory.getText(),
                        fieldSpendingName.getText(), Integer.parseInt(fieldAmount.getText()), Integer.parseInt(fieldSpendingPrice.getText()));
            }
            notes.add(note);
            isReadyToSave = false;
        }
    }

    private void setButtonAddProperty() {
        buttonAdd.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                    boolean commonFieldsEmpty = fieldSpendingName.getText().trim().isEmpty() ||
                            fieldSpendingPrice.getText().trim().isEmpty() ||
                            fieldAmount.getText().trim().isEmpty();

                    boolean categoryNotSelected = choiceBoxCategory.getValue() == null ||
                            choiceBoxCategory.getValue().equals("Choose");

                    boolean otherFieldsInvalid = isOtherFieldsInvalid();

                    return commonFieldsEmpty || categoryNotSelected || otherFieldsInvalid;
                },
                fieldSpendingName.textProperty(),
                fieldSpendingPrice.textProperty(),
                fieldAmount.textProperty(),
                choiceBoxCategory.valueProperty(),
                choiceBoxSubcategory.valueProperty()
        ));
    }

    private boolean isOtherFieldsInvalid() {
        boolean otherFieldsInvalid;
        if ("Other".equals(choiceBoxCategory.getValue())) {
            otherFieldsInvalid = fieldOtherCategory.getText().trim().isEmpty() ||
            fieldOtherSubcategory.getText().trim().isEmpty() ||
            choiceBoxSubcategory.getValue() == null;
        } else {
            otherFieldsInvalid = choiceBoxSubcategory.getValue() == null ||
                    choiceBoxSubcategory.getValue().equals("Choose");
        }
        return otherFieldsInvalid;
    }

    private void checkReadyNote() {
        if(!fieldSpendingName.getText().isEmpty() && !fieldSpendingPrice.getText().isEmpty()
                && (!choiceBoxCategory.getValue().equals("Choose")) && (!choiceBoxSubcategory.getValue().equals("Choose"))
                && !fieldAmount.getText().isEmpty()) {
            isReadyToSave = true;
        }
    }

	private void updateSpendingSum() {
		labelAllSpending.setText(mmm.getAllSpending(notes));
		labelMonthSpending.setText(mmm.getMonthSpending(notes));
	}
}

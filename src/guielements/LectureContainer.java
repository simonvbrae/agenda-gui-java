/*
Van Braeckel Simon
 */

package guielements;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import timetable.MainWindowController;

public class LectureContainer extends VBox implements InvalidationListener {
    private VBox contentBox;
    private MainWindowController mainWindowController;

    private ObservableList<LectureRepresentation> lectureList; //veld voor corresponderende cel in Model

    public LectureContainer() {//MainWindowController mainWindowController){
        this.setFillWidth(true);

        this.contentBox = new VBox();
        contentBox.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHvalue(0.5);
        scrollPane.setContent(contentBox);

        this.getChildren().add(scrollPane);
        this.getStyleClass().add("lecture");

        this.mainWindowController = mainWindowController;
    }

    public void updateStyleClass(){
        int amounfOfChildren = contentBox.getChildren().size();
        if (amounfOfChildren > 1){
            this.getStyleClass().removeAll("correctlecture");
            this.getStyleClass().add("incorrectlecture");
        } else if (amounfOfChildren == 1){
            this.getStyleClass().removeAll("incorrectlecture");
            this.getStyleClass().add("correctlecture");
        } else {
            this.getStyleClass().removeAll("incorrectlecture", "correctlecture");
        }
    }

    public void addLecture(LectureRepresentation lecture){
        contentBox.getChildren().add(lecture);
    }

    @Override
    public void invalidated(Observable observable) {
        contentBox.getChildren().clear();

        for (LectureRepresentation lectureRepresentation : lectureList){
            contentBox.getChildren().add(lectureRepresentation);
        }

        updateStyleClass();
    }

    public void setModel(ObservableList<LectureRepresentation> lectureList){
        this.lectureList = lectureList;
        lectureList.addListener(this);
    }
}

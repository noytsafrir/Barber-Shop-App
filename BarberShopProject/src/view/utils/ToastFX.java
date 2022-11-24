package view.utils;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public final class ToastFX {
	
	public static Stage makeText(Stage ownerStage, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay) {
		Stage toastStage = new Stage();
		toastStage.initOwner(ownerStage);
		toastStage.setResizable(false);
		toastStage.initStyle(StageStyle.TRANSPARENT);
		toastStage.setHeight(90);
		toastStage.setWidth(335);
		toastStage.setY(ownerStage.getY() + (ownerStage.getHeight()) - 100 - 10);
		toastStage.setX(ownerStage.getX() + 20/* + ((ownerStage.getWidth() / 2) - (toastStage.getWidth() / 2)) */);

		Label lblText = new Label(toastMsg);
		lblText.setWrapText(true);
		lblText.setFont(Font.font("Roboto", 14));
		lblText.setTextFill(Color.WHITE);
		StackPane root = new StackPane(lblText);
		root.setPadding(new Insets(10));

		root.setStyle("-fx-background-radius: 20; -fx-background-color: rgba(0, 0, 0, 0.6);");
		root.setOpacity(0);

		Scene scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);

		toastStage.setScene(scene);
		toastStage.show();

		Timeline fadeInTimeline = new Timeline();
		KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay),
				new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 1));
		fadeInTimeline.getKeyFrames().add(fadeInKey1);
		fadeInTimeline.setOnFinished((ae) -> {
			new Thread(() -> {
				try {
					Thread.sleep(toastDelay);
				} catch (InterruptedException e) {
				}
				Timeline fadeOutTimeline = new Timeline();
				KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay),
						new KeyValue(toastStage.getScene().getRoot().opacityProperty(), 0));
				fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
				fadeOutTimeline.setOnFinished((aeb) -> toastStage.close());
				fadeOutTimeline.play();
			}).start();
		});
		fadeInTimeline.play();
		return toastStage;
	}
}
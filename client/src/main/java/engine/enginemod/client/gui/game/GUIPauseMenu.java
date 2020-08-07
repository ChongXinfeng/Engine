package engine.enginemod.client.gui.game;

import engine.Platform;
import engine.client.EngineClientImpl;
import engine.graphics.GraphicsManager;
import engine.graphics.font.Font;
import engine.gui.GUIManager;
import engine.gui.Scene;
import engine.gui.control.Button;
import engine.gui.control.Text;
import engine.gui.layout.FlowPane;
import engine.gui.layout.VBox;
import engine.gui.misc.Background;
import engine.gui.misc.HPos;
import engine.gui.misc.Pos;
import engine.input.KeyCode;
import engine.util.Color;

public final class GUIPauseMenu extends FlowPane {

    public static Scene create() {
        Scene scene = new Scene(new GUIPauseMenu());
        scene.setOnKeyPressed(event -> {
            if (event.getKey() == KeyCode.ESCAPE) {
                GraphicsManager.instance().getGUIManager().close();
            }
        });
        return scene;
    }

    private GUIPauseMenu() {
        alignment().set(Pos.CENTER);
        setBackground(new Background(Color.fromARGB(0x7FAAAAAA)));

        VBox vBox = new VBox();
        vBox.spacing().set(5);
        vBox.alignment().set(HPos.CENTER);
        getChildren().add(vBox);

        Text text = new Text();
        text.setText("Game Menu");
        text.setFont(new Font(Font.getDefaultFont(), 20));
        vBox.getChildren().add(text);

        Button backtoGame = new Button("Back To Game");
        backtoGame.setOnMouseClicked(event -> Platform.getEngineClient().getGraphicsManager().getGUIManager().close());
        vBox.getChildren().add(backtoGame);

        Button terminateGame = new Button("Terminate");
        terminateGame.setOnMouseClicked(mouseClickEvent -> {
            var engine = Platform.getEngineClient();
            engine.getCurrentClientGame().terminate();
            if (((EngineClientImpl) engine).isIntegratedServerRunning()) {
                ((EngineClientImpl) engine).stopIntegratedGame();
            }
            GUIManager guiManager = engine.getGraphicsManager().getGUIManager();
            guiManager.show(new Scene(new GUIMainMenu()));
        });
        vBox.getChildren().add(terminateGame);
    }
}

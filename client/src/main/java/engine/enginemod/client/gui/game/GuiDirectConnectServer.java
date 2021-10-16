package engine.enginemod.client.gui.game;

import engine.Platform;
import engine.client.i18n.I18n;
import engine.gui.Scene;
import engine.gui.control.Button;
import engine.gui.control.Text;
import engine.gui.control.TextField;
import engine.gui.layout.FlowPane;
import engine.gui.layout.HBox;
import engine.gui.layout.VBox;
import engine.gui.misc.Background;
import engine.gui.misc.HPos;
import engine.gui.misc.Pos;
import engine.util.Color;

public class GuiDirectConnectServer extends FlowPane {
    public GuiDirectConnectServer(){
        alignment().set(Pos.CENTER);
        var vmain = new VBox();
        vmain.alignment().set(HPos.CENTER);
        var vbox = new VBox();
        vbox.alignment().set(HPos.CENTER);
        vmain.getChildren().add(vbox);
        getChildren().add(vmain);
        var label1 = new Text(I18n.translate("engine.gui.direct_connect_server.title"));

        var addressFieldGroup = new VBox();
        var lblAddress = new Text(I18n.translate("engine.gui.direct_connect_server.address"));
        var txtboxAddress = new TextField();
        txtboxAddress.setPrefSize(200, 23);
        addressFieldGroup.getChildren().addAll(lblAddress, txtboxAddress);
        var hbox = new HBox();
        hbox.spacing().set(10f);
        var butConnect = new Button(I18n.translate("engine.gui.direct_connect_server.connect"));
        butConnect.setOnMouseClicked(e -> {
            var fullAddress = txtboxAddress.text().get();
            var port = 18104;
            var colonIndex = fullAddress.lastIndexOf(":");
            if (colonIndex != -1) {
                try {
                    port = Integer.parseInt(fullAddress.substring(colonIndex + 1));
                } catch (NumberFormatException ex) {

                }
                fullAddress = fullAddress.substring(0, colonIndex);
            }
            Platform.getEngineClient().getGraphicsManager().getGUIManager().show(new Scene(new GuiServerConnectingStatus(fullAddress, port)));
        });
        var butBack = new Button(I18n.translate("engine.gui.back"));
        butBack.setOnMouseClicked(e -> {
            var guiManager = Platform.getEngineClient().getGraphicsManager().getGUIManager();
            guiManager.showLast();
        });
        hbox.getChildren().addAll(butConnect, butBack);
        vbox.getChildren().addAll(label1, addressFieldGroup, hbox);
        setBackground(Background.fromColor(Color.fromRGB(0x7f7f7f)));
    }
}

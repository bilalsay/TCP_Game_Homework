package sample.services;

import javafx.scene.control.Button;

public class Rectangle extends Button {

    private boolean isShip = false;
    private Game game;
    private static int createdShipNumber = 0;

    public Rectangle(Game game, boolean isShip) {
        this.game = game;
        this.isShip = isShip;

        this.setOnMouseClicked(event -> {

            try {

                if ((game.start && game.move)) {
                    game.move = false;
                    if (game.getIsServer()) {
                        game.socketServer.sendData(("point/" + game.getEnemyGrid().indexOf(this) + "/").getBytes());
                    } else {
                        game.socketClient.sendData(("point/" + game.getEnemyGrid().indexOf(this)+ "/").getBytes());
                    }
                }

                if (!game.start) {
                    int currentIndex = game.getGameGrid().indexOf(this);
                    if (!game.getGameGrid().get(currentIndex).getText().equalsIgnoreCase("S") && createdShipNumber != game.getShipNumber()) {
                        if (currentIndex != 19 && !game.getGameGrid().get(currentIndex + 1).getText().equalsIgnoreCase("S")) {
                            this.setText("S");
                            game.getGameGrid().get(currentIndex + 1).setText("S");
                            createdShipNumber++;
                        } else if(currentIndex != 0 && !game.getGameGrid().get(currentIndex - 1).getText().equalsIgnoreCase("S")) {
                            this.setText("S");
                            game.getGameGrid().get(currentIndex - 1).setText("S");
                            createdShipNumber++;
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public boolean isShip() {
        return isShip;
    }

    public void setShip(boolean ship) {
        isShip = ship;
    }
}

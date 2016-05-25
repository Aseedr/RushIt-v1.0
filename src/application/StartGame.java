package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class StartGame extends Application implements Constants {
  private final int CREATE_BOT = 5;
  private final int SHOOT_START = 10;
  private final int RANDOM = 20;
  private final int STEP = 2;
  private int gameLevel = 0;
  private int createShoot = 0;
  private int skip = 0;
  private String path = "replay.save";
  private Direction direction = Direction.DIRECTION_DOWN;
  private HashMap<KeyCode, Boolean> keys = new HashMap<>();
  private ArrayList<Bots> bots = new ArrayList<>();
  private Scene scene = null;
  private RushIt backToMainMenu = null;
  private Pane root = null;
  private Bots bot = null;
  private Shoot shoot = null;

  // creating main hero
  Image mainHeroImage = new Image(getClass().
      getResourceAsStream("resorses/models/mainHero.png"));
  ImageView mainHeroView = new ImageView(mainHeroImage);
  Character player = new Character(mainHeroView);

  // save main hero position in file
  public void writePosInFile(double heroPosX, double heroPosY) {
    try (FileWriter writer = new FileWriter(path, true)) {
      writer.write((int) heroPosX);
      writer.write((int) heroPosY);
      writer.flush();
    } catch (IOException exception) {
      System.out.println(exception.getMessage());
    }
  }

  // save shoot in file
  public void writeShootInFile() {
    try (FileWriter writer = new FileWriter(path, true)) {
      writer.write(SCREEN_WIDTH + HERO_CENTER);
      writer.flush();
    } catch (IOException exception) {
      System.out.println(exception.getMessage());
    }
  }

  // save bots in file
  public void writeBotsInFile(int botPosX, int botPosY) {
    try (FileWriter writer = new FileWriter(path, true)) {
      writer.write(SCREEN_WIDTH + HERO_SIZE);
      writer.write(botPosX);
      writer.write(botPosY);
      writer.flush();
    } catch (IOException exception) {
      System.out.println(exception.getMessage());
    }
  }

  public void createBots() {
    // creating bots with random coordinates
    int random = (int) Math.floor(Math.random() * (RANDOM * gameLevel));
    int x = (int) Math.floor(Math.random() * SCREEN_WIDTH);
    int y = (int) Math.floor(Math.random() * SCREEN_HEIGHT);
    if (random == CREATE_BOT) {
      Image botsImage = new Image(getClass().
          getResourceAsStream("resorses/models/Bots.png"));
      ImageView botsView = new ImageView(botsImage);
      bot = new Bots(botsView);
      bot.setTranslateX(x);
      bot.setTranslateY(y);
      writeBotsInFile(x, y);
      bots.add(bot);
      root.getChildren().addAll(bot);
    }
  }

  // load game and watch
  public void replay(Text score, Text health) {
    try (FileReader reader = new FileReader("javaReplaySort/replay_100.save")) {
      reader.skip(skip);
      int posX = 0;
      int posY = 0;

      // update score and health on screen
      score.setText("Score: " + player.getScore());
      health.setText("Health: " + player.getHealth());

      // moving bots
      bots.forEach((bot) -> {
        bot.moveToHero(player.getBoundsInParent().getMaxX() - HERO_SIZE / 2,
            player.getBoundsInParent().getMaxY() - HERO_SIZE / 2);
      });

      // moving main hero
      posX = reader.read();
      skip++;

      if (posX == SCREEN_WIDTH + HERO_CENTER) {
        Shoot shoot = new Shoot();
        root.getChildren().addAll(shoot.shootMake(scene, player.
            getTranslateX(), player.getTranslateY(), direction, root, bots));
        createShoot = 0;
        player.addScore(shoot.getScore());
        shoot.clearScore();
        return;
      }

      if (posX == SCREEN_WIDTH + HERO_SIZE) {
        posX = reader.read();
        skip++;
        posY = reader.read();
        skip++;
        Image botsImage = new Image(getClass().
            getResourceAsStream("resorses/models/Bots.png"));
        ImageView botsView = new ImageView(botsImage);
        bot = new Bots(botsView);
        bot.setTranslateX(posX);
        bot.setTranslateY(posY);
        bots.add(bot);
        root.getChildren().addAll(bot);
        return;
      }

      posY = reader.read();
      skip++;
      if (posX > 0 && posY > 0) {
        if (player.getBoundsInParent().getMaxY() - HERO_SIZE / 2 > posY) {
          moveUp();
        } else if (player.getBoundsInParent().getMaxY() - HERO_SIZE / 2 < posY) {
          moveDown();
        } else if (player.getBoundsInParent().getMaxX() - HERO_SIZE / 2 < posX) {
          moveRight();
        } else if (player.getBoundsInParent().getMaxX() - HERO_SIZE / 2 > posX) {
          moveLeft();
        } else {
          player.animation.stop();
        }
      }
      player.isBotEat(root, bots);
    } catch (IOException exception) {
      System.out.println(exception.getMessage());
    }
  }

  public void computerGame(Text score, Text health) {
    // update score and health on screen
    score.setText("Score: " + player.getScore());
    health.setText("Health: " + player.getHealth());

    // moving bots
    bots.forEach((bot) -> {
      bot.moveToHero(player.getBoundsInParent().getMaxX() - HERO_SIZE / 2,
          player.getBoundsInParent().getMaxY() - HERO_SIZE / 2);
    });

    int random = (int) Math.floor(Math.random() * RANDOM);
    // random moving main hero
    if (random == Sides.DOWN.value && player.getTranslateY() >= 0) {
      moveUp();
    } else if (random == Sides.LEFT.value &&
        player.getTranslateY() <= SCREEN_HEIGHT - HERO_SIZE) {
      moveDown();
    } else if (random == Sides.RIGHT.value &&
        player.getTranslateX() <= SCREEN_WIDTH - HERO_SIZE) {
      moveRight();
    } else if (random == Sides.UP.value && player.getTranslateX() >= 0) {
      moveLeft();
    } else {
      player.animation.stop();
    }
    player.isBotEat(root, bots);

    // creating shoots
    if (random >= CREATE_BOT - 1) {
      createShoot++;
      if (createShoot == SHOOT_START) {
        writeShootInFile();
        shoot = new Shoot();
        root.getChildren().addAll(shoot.shootMake(scene, player.
            getTranslateX(), player.getTranslateY(), direction, root, bots));
        createShoot = 0;
        player.addScore(shoot.getScore());
        shoot.clearScore();
      }
    }

    // write status in file
    writePosInFile(player.getBoundsInParent().getMaxX() - HERO_SIZE / 2,
        player.getBoundsInParent().getMaxY() - HERO_SIZE / 2);
  }

  public void update(Text score, Text health) {
    // update score and health on screen
    score.setText("Score: " + player.getScore());
    health.setText("Health: " + player.getHealth());

    // moving bots
    bots.forEach((bot) -> {
      bot.moveToHero(player.getBoundsInParent().getMaxX() - HERO_SIZE / 2,
          player.getBoundsInParent().getMaxY() - HERO_SIZE / 2);
    });

    // moving main hero
    if (isPressed(KeyCode.UP) && player.getTranslateY() >= 0) {
      moveUp();
    } else if (isPressed(KeyCode.DOWN) &&
        player.getTranslateY() <= SCREEN_HEIGHT - HERO_SIZE) {
      moveDown();
    } else if (isPressed(KeyCode.RIGHT) &&
        player.getTranslateX() <= SCREEN_WIDTH - HERO_SIZE) {
      moveRight();
    } else if (isPressed(KeyCode.LEFT) && player.getTranslateX() >= 0) {
      moveLeft();
    } else {
      player.animation.stop();
    }
    player.isBotEat(root, bots);

    // creating shoots
    if (isPressed(KeyCode.SPACE)) {
      createShoot++;
      if (createShoot == SHOOT_START) {
        writeShootInFile();
        shoot = new Shoot();
        root.getChildren().addAll(shoot.shootMake(scene, player.
            getTranslateX(), player.getTranslateY(), direction, root, bots));
        createShoot = 0;
        player.addScore(shoot.getScore());
        shoot.clearScore();
      }
    }

    // write status in file
    writePosInFile(player.getBoundsInParent().getMaxX() - HERO_SIZE / 2,
        player.getBoundsInParent().getMaxY() - HERO_SIZE / 2);
  }

  public void moveUp() {
    direction = Direction.DIRECTION_UP;
    player.animation.play();
    player.animation.setOffsetY(HERO_SIZE * Sides.UP.value);
    player.moveY(-STEP, root, bots);
  }

  public void moveDown() {
    direction = Direction.DIRECTION_DOWN;
    player.animation.play();
    player.animation.setOffsetY(HERO_SIZE * Sides.DOWN.value);
    player.moveY(STEP, root, bots);
  }

  public void moveRight() {
    direction = Direction.DIRECTION_RIGHT;
    player.animation.play();
    player.animation.setOffsetY(HERO_SIZE * Sides.RIGHT.value);
    player.moveX(STEP, root, bots);
  }

  public void moveLeft() {
    direction = Direction.DIRECTION_LEFT;
    player.animation.play();
    player.animation.setOffsetY(HERO_SIZE * Sides.LEFT.value);
    player.moveX(-STEP, root, bots);
  }

  public boolean isPressed(KeyCode key) {
    // is pressed or not
    return keys.getOrDefault(key, false);
  }

  public void start(Stage primaryStage, Levels type, Maps map, Games game) {
    root = new Pane();

    // delete save file
    if (game != Games.REPLAY) {
      new File(path).delete();
    }

    // load map
    if (map == Maps.DESERT_MAP) { // desert map = 1
      Image gameMap = new Image(getClass().
          getResourceAsStream("resorses/maps/desert.png"));
      ImageView mapView = new ImageView(gameMap);
      mapView.setFitHeight(SCREEN_HEIGHT);
      mapView.setFitWidth(SCREEN_WIDTH);
      root.getChildren().add(mapView);
    } else if (map == Maps.FIELD_MAP) { // field map = 2
      Image gameMap = new Image(getClass().
          getResourceAsStream("resorses/maps/field.png"));
      ImageView mapView = new ImageView(gameMap);
      mapView.setFitHeight(SCREEN_HEIGHT);
      mapView.setFitWidth(SCREEN_WIDTH);
      root.getChildren().add(mapView);
    } else if (map == Maps.SANDY_ROAD_MAP) { // sandy road map = 3
      Image gameMap = new Image(getClass().
          getResourceAsStream("resorses/maps/sandyRoad.png"));
      ImageView mapView = new ImageView(gameMap);
      mapView.setFitHeight(SCREEN_HEIGHT);
      mapView.setFitWidth(SCREEN_WIDTH);
      root.getChildren().add(mapView);
    }

    // load difficulty
    switch (type) {
    case EASY_LEVEL:
      gameLevel = Levels.EASY_LEVEL.value;
      break;
    case NORMAL_LEVEL:
      gameLevel = Levels.NORMAL_LEVEL.value;
      break;
    case HARD_LEVEL:
      gameLevel = Levels.HARD_LEVEL.value;
      break;
    case STRONG_LEVEL:
      gameLevel = Levels.STRONG_LEVEL.value;
      break;
    }

    // write score on screen
    Text score = new Text("Score: " + player.getScore());
    score.setFont(Font.loadFont(getClass().
        getResourceAsStream("resorses/font/Chiller-Regular.ttf"), 50));
    score.setFill(Color.RED);
    root.getChildren().add(score);
    score.setTranslateX(1050);
    score.setTranslateY(40);
    score.setVisible(true);

    // write health on screen
    Text health = new Text("Health: " + player.getHealth());
    health.setFont(Font.loadFont(getClass().
        getResourceAsStream("resorses/font/Chiller-Regular.ttf"), 50));
    health.setFill(Color.RED);
    root.getChildren().add(health);
    health.setTranslateX(1080);
    health.setTranslateY(90);
    health.setVisible(true);

    // inset main hero
    player.setTranslateX(SCREEN_WIDTH / 2 - HERO_SIZE / 2);
    player.setTranslateY(SCREEN_HEIGHT / 2 - HERO_SIZE / 2);
    root.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);
    root.getChildren().addAll(player);

    // analysis pressed key
    scene = new Scene(root);
    scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
    scene.setOnKeyReleased(event -> {
      keys.put(event.getCode(), false);
    });

    AnimationTimer timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        // update process
        if (game == Games.STANDARD) {
          update(score, health);
          createBots();
        } else if (game == Games.COMPUTER) {
          computerGame(score, health);
          createBots();
        } else if (game == Games.REPLAY) {
          replay(score, health);
        }

        // exit if escape
        if (isPressed(KeyCode.ESCAPE)) {
          stop();
          bots.clear();
          backToMainMenu = new RushIt();
          backToMainMenu.start(primaryStage);
        }

        // exit if game over
        if (player.getHealth() <= 0) {
          health.setText("Health: " + player.getHealth());
          stop();
          bots.clear();

          // write game over
          Text gameOver = new Text("Game Over");
          gameOver.setFont(Font.loadFont(getClass().
              getResourceAsStream("resorses/font/Chiller-Regular.ttf"), 300));
          gameOver.setFill(Color.RED);
          root.getChildren().add(gameOver);
          gameOver.setTranslateX((SCREEN_WIDTH - gameOver.getBoundsInParent()
              .getMaxX() - gameOver.getBoundsInParent().getMinX()) / 2);
          gameOver.setTranslateY((SCREEN_HEIGHT - gameOver.getBoundsInParent()
              .getMaxY() - gameOver.getBoundsInParent().getMinY()) / 2);
          gameOver.setVisible(true);

          // wait escape
          scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
              switch (event.getCode()) {
              case ESCAPE: {
                backToMainMenu = new RushIt();
                backToMainMenu.start(primaryStage);
                break;
              }
              default: {
                break;
              }
              }
            }
          });
        }
      }
    };

    // start game
    timer.start();

    primaryStage.setTitle("Rush It");
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    //it's not part of program, it's use for test and debugging
    start(primaryStage, null, null, null);
  }
}
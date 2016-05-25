package application;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RushIt extends Application implements Constants {
  private Levels type = Levels.EASY_LEVEL;
  private Maps map = Maps.DESERT_MAP;
  private Games game = Games.STANDARD;
  private Sorts sort = Sorts.BOTS_SORT;
  private Sort_Type sortTe = Sort_Type.JAVA_SORT;
  private StartGame startGame = null;
  private Scene scene = null;
  private Pane root = null;
  private Text averagePlayingTime;
  private Text averageNumberOfOpponents;
  private Text averageNumberOfShoots;
  private Text goodGames;

  @Override
  public void start(Stage primaryStage) {
    root = new Pane();

    // load pictures of the main menu
    Image image = new Image(getClass().
        getResourceAsStream("resorses/screen/mainImage.jpg"));
    ImageView img = new ImageView(image);
    img.setFitHeight(SCREEN_HEIGHT);
    img.setFitWidth(SCREEN_WIDTH);
    root.getChildren().add(img);

    // load and play sound of the main menu
    Media media = new Media(getClass().
        getResource("resorses/media/mainAudio.mp3").toString());
    MediaPlayer player = new MediaPlayer(media);
    player.setOnEndOfMedia(new Runnable() {
      public void run() {
        player.seek(Duration.ZERO);
      }
    });
    player.play();

    // write on main screen name of the game
    Text gameName = new Text("Rush It v1.0 2016");
    gameName.setFont(Font.loadFont(getClass().
        getResourceAsStream("resorses/font/Chiller-Regular.ttf"), 40));
    gameName.setFill(Color.RED);
    root.getChildren().add(gameName);
    gameName.setTranslateX(250);
    gameName.setTranslateY(710);
    gameName.setVisible(true);

    // first menu buttons
    MenuItems newGame = new MenuItems("New Game");
    MenuItems loadGame = new MenuItems("Load Game");
    MenuItems computerGame = new MenuItems("Computer Game");
    MenuItems replaySort = new MenuItems("Replay Sort");
    MenuItems highScores = new MenuItems("High Scores");
    MenuItems credits = new MenuItems("Credits");
    MenuItems exit = new MenuItems("Exit");
    PlaceMenu mainMenu = new PlaceMenu(newGame, loadGame, computerGame,
        replaySort, highScores, credits, exit);

    // second menu buttons
    MenuItems rush = new MenuItems("Rush");
    MenuItems extraRush = new MenuItems("Extra Rush");
    MenuItems survival = new MenuItems("Survival");
    MenuItems superSurvival = new MenuItems("Super Survival");
    MenuItems backToMainMenu = new MenuItems("Back");
    PlaceMenu gameType = new PlaceMenu(rush, extraRush,
        survival, superSurvival, backToMainMenu);
    MenuItems jbotsSort = new MenuItems("Java Bots Sort");
    MenuItems jshootsSort = new MenuItems("Java Shoots Sort");
    MenuItems jlenghtSort = new MenuItems("Java Lenght Sort");
    MenuItems sbotsSort = new MenuItems("Scala Bots Sort");
    MenuItems sshootsSort = new MenuItems("Scala Shoots Sort");
    MenuItems slenghtSort = new MenuItems("Scala Lenght Sort");
    MenuItems scalaTranscript = new MenuItems("Transcript Replays");
    MenuItems backMainMenu = new MenuItems("Back");
    PlaceMenu sortType = new PlaceMenu(jbotsSort, jshootsSort,
        jlenghtSort, sbotsSort, sshootsSort, slenghtSort,
        scalaTranscript, backMainMenu);
    MenuItems toMainMenu = new MenuItems("Back");
    PlaceMenu statistics = new PlaceMenu(toMainMenu);

    // third menu buttons
    MenuItems desert = new MenuItems("Desert");
    MenuItems field = new MenuItems("Field");
    MenuItems sandyRoad = new MenuItems("Sandy Road");
    MenuItems backToGameTypeMenu = new MenuItems("Back");
    PlaceMenu maps = new PlaceMenu(desert, field,
        sandyRoad, backToGameTypeMenu);
    MenuBoxs menuBox = new MenuBoxs(mainMenu);

    // the appointment of the first buttons
    newGame.setOnMouseClicked(event -> menuBox.setMenuBoxs(gameType));
    loadGame.setOnMouseClicked(event -> {
      startGame = new StartGame();
      game = Games.REPLAY;
      player.stop();
      startGame.start(primaryStage, type, map, game);
    });
    computerGame.setOnMouseClicked(event -> {
      startGame = new StartGame();
      game = Games.COMPUTER;
      type = Levels.STRONG_LEVEL;
      player.stop();
      startGame.start(primaryStage, type, map, game);
    });
    highScores.setOnMouseClicked(event -> {
      menuBox.setMenuBoxs(statistics);
      JavaReplaySort getStatistics = new JavaReplaySort(1, replays);

      // write average playing time on screen
      averagePlayingTime = new Text("Average Playing Time: "
          + getStatistics.statisticsAveragePlayingTime() + "s");
      averagePlayingTime.setFont(Font.loadFont(getClass().
          getResourceAsStream("resorses/font/Chiller-Regular.ttf"), 50));
      averagePlayingTime.setFill(Color.RED);
      root.getChildren().add(averagePlayingTime);
      averagePlayingTime.setTranslateX(50);
      averagePlayingTime.setTranslateY(150);
      averagePlayingTime.setVisible(true);

      // write average number of opponents on screen
      averageNumberOfOpponents = new Text("Average Number Of Opponents: "
          + getStatistics.statisticsAverageNumberOfOpponents());
      averageNumberOfOpponents.setFont(Font.loadFont(getClass().
          getResourceAsStream("resorses/font/Chiller-Regular.ttf"), 50));
      averageNumberOfOpponents.setFill(Color.RED);
      root.getChildren().add(averageNumberOfOpponents);
      averageNumberOfOpponents.setTranslateX(50);
      averageNumberOfOpponents.setTranslateY(220);
      averageNumberOfOpponents.setVisible(true);

      // write average number of shoots on screen
      averageNumberOfShoots = new Text("Average Number Of Shoots: "
          + getStatistics.statisticsAverageNumberOfShoots());
      averageNumberOfShoots.setFont(Font.loadFont(getClass().
          getResourceAsStream("resorses/font/Chiller-Regular.ttf"), 50));
      averageNumberOfShoots.setFill(Color.RED);
      root.getChildren().add(averageNumberOfShoots);
      averageNumberOfShoots.setTranslateX(50);
      averageNumberOfShoots.setTranslateY(290);
      averageNumberOfShoots.setVisible(true);

      // write good games on screen
      goodGames = new Text("Good Games: " + getStatistics.statisticsGoodGames() + " (more than 300 points)");
      goodGames.setFont(Font.loadFont(getClass().
          getResourceAsStream("resorses/font/Chiller-Regular.ttf"), 50));
      goodGames.setFill(Color.RED);
      root.getChildren().add(goodGames);
      goodGames.setTranslateX(50);
      goodGames.setTranslateY(360);
      goodGames.setVisible(true);
    });
    replaySort.setOnMouseClicked(event -> menuBox.setMenuBoxs(sortType));
    exit.setOnMouseClicked(event -> System.exit(0));

    // the appointment of the second buttons
    rush.setOnMouseClicked(event -> {
      menuBox.setMenuBoxs(maps);
      type = Levels.EASY_LEVEL;
    });
    extraRush.setOnMouseClicked(event -> {
      menuBox.setMenuBoxs(maps);
      type = Levels.NORMAL_LEVEL;
    });
    survival.setOnMouseClicked(event -> {
      menuBox.setMenuBoxs(maps);
      type = Levels.HARD_LEVEL;
    });
    superSurvival.setOnMouseClicked(event -> {
      menuBox.setMenuBoxs(maps);
      type = Levels.STRONG_LEVEL;
    });
    backToMainMenu.setOnMouseClicked(event -> menuBox.setMenuBoxs(mainMenu));

    // the appointment of the third buttons
    desert.setOnMouseClicked(event -> {
      startGame = new StartGame();
      map = Maps.DESERT_MAP; // desert map = 1
      player.stop();
      startGame.start(primaryStage, type, map, game);
    });
    field.setOnMouseClicked(event -> {
      startGame = new StartGame();
      map = Maps.FIELD_MAP; // field map = 2
      player.stop();
      startGame.start(primaryStage, type, map, game);
    });
    sandyRoad.setOnMouseClicked(event -> {
      startGame = new StartGame();
      map = Maps.SANDY_ROAD_MAP; // sandy road map = 3
      player.stop();
      startGame.start(primaryStage, type, map, game);
    });
    backToGameTypeMenu.setOnMouseClicked(event -> menuBox.setMenuBoxs(gameType));

    // the appointment of the second buttons
    jbotsSort.setOnMouseClicked(event -> {
      long startTime = System.currentTimeMillis();
      JavaReplaySort javaReplaySort = new JavaReplaySort(1, replays);
      sort = Sorts.BOTS_SORT;
      javaReplaySort.sort(sort, sortTe);
      menuBox.setMenuBoxs(mainMenu);
      long timeSpent = System.currentTimeMillis() - startTime;
      System.out.println("Java bots sort: " + timeSpent + " miliseconds");
    });
    jshootsSort.setOnMouseClicked(event -> {
      long startTime = System.currentTimeMillis();
      JavaReplaySort javaReplaySort = new JavaReplaySort(1, replays);
      sort = Sorts.SHOOTS_SORT;
      javaReplaySort.sort(sort, sortTe);
      menuBox.setMenuBoxs(mainMenu);
      long timeSpent = System.currentTimeMillis() - startTime;
      System.out.println("Java shoots sort: " + timeSpent + " miliseconds");
    });
    jlenghtSort.setOnMouseClicked(event -> {
      long startTime = System.currentTimeMillis();
      JavaReplaySort javaReplaySort = new JavaReplaySort(1, replays);
      sort = Sorts.LENGTH_SORT;
      javaReplaySort.sort(sort, sortTe);
      menuBox.setMenuBoxs(mainMenu);
      long timeSpent = System.currentTimeMillis() - startTime;
      System.out.println("Java length sort: " + timeSpent + " miliseconds");
    });
    sbotsSort.setOnMouseClicked(event -> {
      long startTime = System.currentTimeMillis();
      JavaReplaySort scalaReplaySort = new JavaReplaySort(1, replays);
      sort = Sorts.BOTS_SORT;
      sortTe = Sort_Type.SCALA_SORT;
      scalaReplaySort.sort(sort, sortTe);
      menuBox.setMenuBoxs(mainMenu);
      long timeSpent = System.currentTimeMillis() - startTime;
      System.out.println("Scala bots sort: " + timeSpent + " miliseconds");
    });
    sshootsSort.setOnMouseClicked(event -> {
      long startTime = System.currentTimeMillis();
      JavaReplaySort scalaReplaySort = new JavaReplaySort(1, replays);
      sort = Sorts.SHOOTS_SORT;
      sortTe = Sort_Type.SCALA_SORT;
      scalaReplaySort.sort(sort, sortTe);
      menuBox.setMenuBoxs(mainMenu);
      long timeSpent = System.currentTimeMillis() - startTime;
      System.out.println("Scala shoots sort: " + timeSpent + " miliseconds");
    });
    slenghtSort.setOnMouseClicked(event -> {
      long startTime = System.currentTimeMillis();
      JavaReplaySort scalaReplaySort = new JavaReplaySort(1, replays);
      sort = Sorts.LENGTH_SORT;
      sortTe = Sort_Type.SCALA_SORT;
      scalaReplaySort.sort(sort, sortTe);
      menuBox.setMenuBoxs(mainMenu);
      long timeSpent = System.currentTimeMillis() - startTime;
      System.out.println("Scala length sort: " + timeSpent + " miliseconds");
    });
    scalaTranscript.setOnMouseClicked(event -> {
      JavaReplaySort scalaReplaysTranscript = new JavaReplaySort(1, replays);
      try {
        scalaReplaysTranscript.replaysTranscript();
      } catch (Exception e) {
        e.printStackTrace();
      }
      menuBox.setMenuBoxs(mainMenu);
    });
    backMainMenu.setOnMouseClicked(event -> menuBox.setMenuBoxs(mainMenu));
    toMainMenu.setOnMouseClicked(event -> {
      root.getChildren().remove(averagePlayingTime);
      root.getChildren().remove(averageNumberOfOpponents);
      root.getChildren().remove(averageNumberOfShoots);
      root.getChildren().remove(goodGames);
      menuBox.setMenuBoxs(mainMenu);
    });

    root.getChildren().addAll(menuBox);
    menuBox.setVisible(true);

    scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

    // load main icon of the game
    Image icon = new Image(getClass().
        getResourceAsStream("resorses/icon/icon.png"));
    primaryStage.getIcons().add(icon);

    primaryStage.setTitle("Rush It");
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  private class MenuItems extends StackPane {
    public MenuItems(String nameOfItem) {
      // make buttons sections and make buttons animation
      Rectangle place = new Rectangle(250, 30, Color.BLACK);
      place.setOpacity(0.4);

      Text itemText = new Text(nameOfItem);
      itemText.setFill(Color.DARKGRAY);
      itemText.setFont(Font.loadFont(getClass().
          getResourceAsStream("resorses/font/Chiller-Regular.ttf"), 30));

      setAlignment(Pos.CENTER);
      getChildren().addAll(place, itemText);

      FillTransition itemAnimation = new
          FillTransition(Duration.seconds(0.4), place);
      setOnMouseEntered(event -> {
        itemAnimation.setFromValue(Color.RED);
        itemAnimation.setToValue(Color.BLACK);
        itemAnimation.setCycleCount(Animation.INDEFINITE);
        itemAnimation.setAutoReverse(true);
        itemAnimation.play();
      });
      setOnMouseExited(event -> {
        itemAnimation.stop();
        place.setFill(Color.BLACK);
      });
    }
  }

  private class PlaceMenu extends VBox {
    public PlaceMenu(MenuItems... items) {
      // install button in buttons area
      setSpacing(10);
      setTranslateX(100);
      setTranslateY(40);
      for (MenuItems item : items) {
        getChildren().addAll(item);
      }
    }
  }

  private class MenuBoxs extends Pane {
    // install buttons area on main screen
    PlaceMenu placeMenu;

    public MenuBoxs(PlaceMenu plcMenu) {
      placeMenu = plcMenu;

      setVisible(false);
      Rectangle screenBox = new
          Rectangle(SCREEN_WIDTH, SCREEN_HEIGHT, Color.ALICEBLUE);
      screenBox.setOpacity(0.04);
      getChildren().addAll(screenBox, placeMenu);
    }

    public void setMenuBoxs(PlaceMenu plcMenu) {
      getChildren().remove(placeMenu);
      placeMenu = plcMenu;
      getChildren().add(placeMenu);
    }
  }
}
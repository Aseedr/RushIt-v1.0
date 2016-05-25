package application;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JavaReplaySort implements Constants {
  private final String path = "replay/";
  private final String jsortedPath = "javaReplaySort/";
  private final String ssortedPath = "scalaReplaySort/";
  private final String transcriptPath = "replayTranscript/";
  private int replayNumber[];
  private int sortCount[];;
  private int firstNumber;
  private int lastNumber;

  public JavaReplaySort(int firstNumber, int lastNumber) {
    this.firstNumber = firstNumber;
    this.lastNumber = lastNumber;
    this.replayNumber = new int[lastNumber];
    this.sortCount = new int[lastNumber];
  }

  public void sort(Sorts sortMode, Sort_Type sortType) {
    // read replays for sort
    for (int i = firstNumber; i <= lastNumber; i++) {
      replayNumber[i - 1] = i;
      Path sourcePath = Paths.get(path + "replay_" +
          Integer.toString(i) + ".save");
      try (FileReader reader = new FileReader(sourcePath.toString())) {
        int buff = 0;
        while (buff != -1) {
          buff = reader.read();
          switch (sortMode) {
          case BOTS_SORT: {
            if (buff == SCREEN_WIDTH + HERO_SIZE) {
              sortCount[i - 1]++;
            }
            break;
          }
          case SHOOTS_SORT: {
            if (buff == SCREEN_WIDTH + HERO_CENTER) {
              sortCount[i - 1]++;
            }
            break;
          }
          case LENGTH_SORT:
            sortCount[i - 1]++;
            break;
          }
        }
      } catch (IOException exception) {
        System.out.println(exception.getMessage());
      }
    }

    // sort massive for replay
    if (sortType == Sort_Type.JAVA_SORT) {
      for (int i = lastNumber - 1; i >= 0; i--) {
        for (int j = 0; j < i; j++) {
          if (sortCount[j] > sortCount[j + 1]) {
            int tempOne = sortCount[j];
            int tempTwo = replayNumber[j]; 
            sortCount[j] = sortCount[j + 1];
            replayNumber[j] = replayNumber[j + 1];
            sortCount[j + 1] = tempOne;
            replayNumber[j + 1] = tempTwo;
          }
        }
      }
    } else {
      ScalaReplaySort scalaSort = new ScalaReplaySort();
      replayNumber = scalaSort.sortRepOne(lastNumber, replayNumber, sortCount);
      sortCount = scalaSort.sortRepTwo(lastNumber, replayNumber, sortCount);
    }

    // write sorted replays
    for (int i = firstNumber; i <= lastNumber; i++) {
      try {
        File sourceFile = new File(path + "replay_" +
            Integer.toString(replayNumber[i - 1]) + ".save");
        if (sortType == Sort_Type.JAVA_SORT) {
          File sortFile = new File(jsortedPath + "replay_" +
              Integer.toString(i) + ".save");
          Files.copy(sourceFile.toPath(), sortFile.toPath());
        } else {
          File sortFile = new File(ssortedPath + "replay_" +
              Integer.toString(i) + ".save");
          Files.copy(sourceFile.toPath(), sortFile.toPath());
        }
      } catch (IOException exception) {
        System.out.println(exception.getMessage());
      }
    }
  }

  public int statisticsAveragePlayingTime() {
    int averagePlayingTime = 0;

    // read replays for statistics
    for (int i = firstNumber; i <= lastNumber; i++) {
      Path sourcePath = Paths.get(path + "replay_" + Integer.toString(i) + ".save");
      try (FileReader reader = new FileReader(sourcePath.toString())) {
        int buff = 0;
        while (buff != -1) {
          buff = reader.read();
          sortCount[i - 1]++;
        }
      } catch (IOException exception) {
        System.out.println(exception.getMessage());
      }
    }

    ScalaReplaySort scalaAveragePlayingTime = new ScalaReplaySort();
    averagePlayingTime = scalaAveragePlayingTime
        .getAveragePlayingTime(lastNumber, sortCount);

    return averagePlayingTime;
  }

  public int statisticsAverageNumberOfOpponents() {
    int averageNumberOfOpponents = 0;

    // read replays for statistics
    for (int i = firstNumber; i <= lastNumber; i++) {
      Path sourcePath = Paths.get(path + "replay_" + Integer.toString(i) + ".save");
      try (FileReader reader = new FileReader(sourcePath.toString())) {
        int buff = 0;
        while (buff != -1) {
          buff = reader.read();
          if (buff == SCREEN_WIDTH + HERO_SIZE) {
            sortCount[i - 1]++;
          }
        }
      } catch (IOException exception) {
        System.out.println(exception.getMessage());
      }
    }

    ScalaReplaySort scalaAverageNumberOfOpponents = new ScalaReplaySort();
    averageNumberOfOpponents = scalaAverageNumberOfOpponents
        .getAverageNumberOfOpponents(lastNumber, sortCount);

    return averageNumberOfOpponents;
  }

  public int statisticsAverageNumberOfShoots() {
    int averageNumberOfShoots = 0;

    // read replays for statistics
    for (int i = firstNumber; i <= lastNumber; i++) {
      Path sourcePath = Paths.get(path + "replay_" + Integer.toString(i) + ".save");
      try (FileReader reader = new FileReader(sourcePath.toString())) {
        int buff = 0;
        while (buff != -1) {
          buff = reader.read();
          if (buff == SCREEN_WIDTH + HERO_CENTER) {
            sortCount[i - 1]++;
          }
        }
      } catch (IOException exception) {
        System.out.println(exception.getMessage());
      }
    }

    ScalaReplaySort scalaAverageNumberOfShoots = new ScalaReplaySort();
    averageNumberOfShoots = scalaAverageNumberOfShoots
        .getAverageNumberOfShoots(lastNumber, sortCount);

    return averageNumberOfShoots;
  }

  public int statisticsGoodGames() {
    int goodGames = 0;

    // read replays for statistics
    for (int i = firstNumber; i <= lastNumber; i++) {
      Path sourcePath = Paths.get(path + "replay_" + Integer.toString(i) + ".save");
      try (FileReader reader = new FileReader(sourcePath.toString())) {
        int buff = 0;
        while (buff != -1) {
          buff = reader.read();
          sortCount[i - 1]++;
        }
      } catch (IOException exception) {
        System.out.println(exception.getMessage());
      }
    }

    ScalaReplaySort scalaGoodGames = new ScalaReplaySort();
    goodGames = scalaGoodGames
        .getGoodGames(lastNumber, sortCount);

    return goodGames;
  }

  @SuppressWarnings("resource")
public void replaysTranscript() throws IOException {
    ScalaReplaySort scalaTranscript = new ScalaReplaySort();

    // read and write replays for transcript
    for (int i = firstNumber; i <= lastNumber; i++) {
      Path sourcePath = Paths.get(path + "replay_" +
          Integer.toString(i) + ".save");
      Path scriptPath = Paths.get(transcriptPath + "replay_" +
          Integer.toString(i) + ".save");
      FileReader reader = new FileReader(sourcePath.toString());
      FileWriter writer = new FileWriter(scriptPath.toString(), false);

      int buffForX = 0;
      int buffForY = 0;
      int previousBuffForX = 0;
      int previousBuffForY = 0;
      previousBuffForX = reader.read();
      previousBuffForY = reader.read();

      Boolean flag;
      while (buffForX != -1) {
        flag = false;
        buffForX = reader.read();

        if (buffForX == SCREEN_WIDTH + HERO_CENTER) {
          buffForX = SHOOT;
          flag = true;
          writer.write(scalaTranscript.replayTranscript(buffForX));
          writer.flush();
        }

        if (buffForX == SCREEN_WIDTH + HERO_SIZE) {
          buffForX = reader.read();
          buffForX = reader.read();
          buffForX = BOT;
          flag = true;
          writer.write(scalaTranscript.replayTranscript(buffForX));
          writer.flush();
        }

        if (flag == false) {
          buffForY = reader.read();
          if (buffForX > 0 && buffForY > 0) {
            if (previousBuffForY > buffForY) {
              flag = true;
              previousBuffForY = buffForY;
              buffForX = Sides.UP.value;
            } else if (previousBuffForY < buffForY) {
              flag = true;
              previousBuffForY = buffForY;
              buffForX = Sides.DOWN.value;
            } else if (previousBuffForX < buffForX) {
              flag = true;
              previousBuffForX = buffForX;
              buffForX = Sides.RIGHT.value;
            } else if (previousBuffForX > buffForX) {
              flag = true;
              previousBuffForX = buffForX;
              buffForX = Sides.LEFT.value;
            }
          }

          if (flag == true) {
            writer.write(scalaTranscript.replayTranscript(buffForX));
            writer.flush();
          }
        }
      }
    }
  }
}

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Leaderboard {
  private String fileName = "leaderboard.txt";
  private List<PlayerScore> scores;

  public Leaderboard() {
    scores = new ArrayList<>();
    loadScores();
  }

  public void addScore(String name, int score) {
    scores.add(new PlayerScore(name, score));
    Collections.sort(scores, new ScoreComparator());
    saveScores();
  }

  public List<PlayerScore> getScores() {
    return scores;
  }

  private void loadScores() {
    try {
      File file = new File(fileName);
      if (file.exists()) {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
          String[] parts = line.split(",");
          scores.add(new PlayerScore(parts[0], Integer.parseInt(parts[1])));
        }
        reader.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void saveScores() {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
     
      for (PlayerScore score : scores) {       
        writer.write(score.getName() + "," + score.getScore());
        writer.newLine();
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void addScoreAndUpdate(String name, int score) {

    boolean playerExists = false;

    for (PlayerScore playerScore : scores) {
      if (playerScore.getName().equals(name)) {
        playerScore.setScore(playerScore.getScore() + score);
        playerExists = true;
        break;
      }
    }
    if (!playerExists) {
      scores.add(new PlayerScore(name, score));
    }

    Collections.sort(scores, new ScoreComparator());
    saveScores();
  }

  class ScoreComparator implements Comparator<PlayerScore> {
    @Override
    public int compare(PlayerScore o1, PlayerScore o2) {
      return Integer.compare(o2.getScore(), o1.getScore());
    }
  }
}

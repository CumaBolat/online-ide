package tr.mu.posta.cuma.projects.gol.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class GameOfLifePatternLoader implements ApplicationRunner {

  private final String PATTERNS_PATH = "src/main/java/tr/mu/posta/cuma/projects/gol/patterns/grids";
  private final int EXTRA_PADDING_SIZE = 40;

  private Map<String, int[][]> patternGrids = new ConcurrentHashMap<>();

  @Override
  public void run(ApplicationArguments args) throws Exception {
    System.out.println("Loading patterns...");
    loadPatterns();
  }

  private void loadPatterns() {
    File patternsFolder = new File(PATTERNS_PATH);

    //System.out.println("patternsFolder= " + patternsFolder.toString());
    File[] patternFiles = patternsFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
    //printAr(patternFiles);
    if (patternFiles != null) {
      for (File file : patternFiles) {
        String patternName = file.getName().replaceAll("\\.txt$", "");
        try {
          int[][] patternGrid = readPatternFromFile(file);
          patternGrids.put(patternName, patternGrid);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    for (String s : this.patternGrids.keySet()) {
      System.out.println("pattern name= " + s);
    }

    //System.out.println("GUNSTAR= ");
    //printArray(this.patternGrids.get("GUNSTAR"));
  }

  private int[][] readPatternFromFile(File file) throws IOException {
    int length = 0;
    int gridSize = 0;

    BufferedReader counter = new BufferedReader(new FileReader(file));
    String line = counter.readLine();
    while (line != null) {
      length++;
      gridSize = Math.max(gridSize, line.length());
      line = counter.readLine();
    }

    counter.close();

    gridSize = Math.max(gridSize, length) + this.EXTRA_PADDING_SIZE;

    int[][] patternGrid = new int[gridSize][gridSize];
    //System.out.println("file naem" + file.getName());
    //System.out.println("gridSize= " + gridSize);

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      int row = 0;
      String line2 = reader.readLine();
      while (line2 != null) {
        for (int col = 0; col < line2.length(); col++) {
          patternGrid[row + this.EXTRA_PADDING_SIZE / 2][col + this.EXTRA_PADDING_SIZE / 2] = line2.charAt(col) == 'O' ? 1 : 0;
        }
        row++;
        line2 = reader.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    //System.out.println("grid= " );
    //printArray(patternGrid);
    //System.out.println("grid length= " + patternGrid.length);
    //System.out.println("grid[0] length= " + patternGrid[0].length);
    return patternGrid;
  }

  public int[][] getPatternGrid(String patternName) {
    //System.out.println("get pattern name= " + patternName);
    return this.patternGrids.getOrDefault(patternName, new int[100][100]);
  }

  private void printArray(int[][] arr) {
    for (int i = 0; i < arr.length; i++) {
      for (int j = 0; j < arr[i].length; j++) {
        System.out.print(arr[i][j]);
      }
      System.out.println();
    }
  }

  private void printAr(File[] arr) {
    for (int i = 0; i < arr.length; i++) {
      System.out.println(arr[i].getName());
    }
  }
}

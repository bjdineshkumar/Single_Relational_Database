import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileHandler {


    public FileHandler() {

    }

  public Boolean createFileIfNotExists(String fileName) throws
      IOException {

    var file = new File(fileName);

    if (file.createNewFile()) {
      System.out.println("File Created At: " + file.getPath());
    } else {
      System.out.println("File already present at: " + file.getPath());
    }

    return file.exists();
  }


  public String getTempFile(String keyword, String dirName)
      throws IOException {

    Path dirPath = Paths.get(dirName);

    String prefix = "temp-";
    String suffix = "." + keyword;

    Path temp = Files.createTempFile(dirPath, prefix, suffix);

    return temp.toString();
  }

  public Boolean checkIfFileExists(String filePath) {
    File fp = new File(filePath);

    return fp.exists();
  }

  public Boolean createDirIfNotExists(String dirName) {
    File file = new File(dirName);

    if (file.mkdir()) {
//            logger.event("Directory Created At: " + file.getPath());
      return true;
    } else {
//            logger.event("Directory already present at: " + file.getPath());
      return false;
    }

  }

  public File[] getAllFilesInDir(String dirName) {
    File folderPath = new File(dirName);
    return folderPath.listFiles();
  }

  public Boolean checkIfFileContains(String item, String filePath)
      throws IOException {

    List<String> lines = Files.lines(Path.of(filePath))
        .filter(line -> line.contains(item)).collect(
            Collectors.toList());

    return !lines.isEmpty();
  }

  public void writeToFile(String filepath, String message) {
    try (var fw = new FileWriter(filepath, true)) {
      fw.write(message);
      fw.write(System.lineSeparator());
    } catch (Exception e) {
//            logger.event(e.getMessage());
    }
  }

//    public void createFile(String filepath) {
//        try (var fw = new FileWriter(filepath, true)) {
//            fw.write(message);
//            fw.write(System.lineSeparator());
//        } catch (Exception e) {
////            logger.event(e.getMessage());
//        }
//    }

  public long getLineCount(String filePath) throws IOException {
    long lines = 0;

    if (checkIfFileExists(filePath)) {
      Path path = Paths.get(filePath);
      lines = Files.lines(path).count();

    }

    return lines;
  }

  public long getFileSize(String filePath) {
    long bytes = 0;
    Path path = Paths.get(filePath);
    try {
      bytes = Files.size(path);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return bytes;
  }

}

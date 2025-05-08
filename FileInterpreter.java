import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;


// This class does everything related to file reading.
// It keeps track of the program counter too.
public class FileInterpreter {
    private final String fileName;
    private int pc;
    private BufferedReader rd;
    private int lineCount;

    public FileInterpreter(String fileName) {
        this.fileName = fileName;
        this.pc = 0;
        this.lineCount = 0;
        countLines();
        try {
            rd = new BufferedReader(new FileReader(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // This method just returns next nonempty line.
    // If there are no lines left, it returns an empty line.
    // All the lower level String processing happens in the Main file.
    public String nextInstruction() {
        String nextLine = "";
        while (true) {
            try {
                nextLine = rd.readLine();
                pc++;
                if (nextLine == null) {
                    return "";
                } else if (nextLine.isEmpty() || nextLine.charAt(0) == '#') {
                    continue;
                }
                // Replacing all the special register names with their "x" alternatives.
                nextLine = nextLine.replaceAll("ra", "x1");
                nextLine = nextLine.replaceAll("sp", "x2");
                return nextLine;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    // This method resets the program counter to the passed value.
    public void setProgramCounter(int val) {
        try {
            rd.close();
            rd = new BufferedReader(new FileReader(fileName));
            for (pc = 0; pc < val; pc++) {
                rd.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public int getProgramCounter() {
        return pc;
    }


    // This method should only be used when reading the whole file at once.
    // If other method is called before one gets all the labels
    // and sets the pc to zero, the code won't work properly.
    public String nextLabel() {
        String nextLine = "";
        while (true) {
            try {
                nextLine = rd.readLine();
                pc++;
                if (nextLine == null) {
                    return null;
                }
                if (nextLine.contains(":")) {
                    return nextLine;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // This methods just returns the number of all the lines.
    private void countLines() {
        String a = "";
        try {
            rd = new BufferedReader(new FileReader(fileName));
            while ((a = rd.readLine()) != null) {
                lineCount++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getLineCount() {
        return lineCount;
    }
}

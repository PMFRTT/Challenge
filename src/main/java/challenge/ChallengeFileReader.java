package challenge;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChallengeFileReader {

    private static ChallengeMain main;

    public ChallengeFileReader(ChallengeMain main) {
        this.main = main;
    }


    public static void readDataFile() {
        try {

            File challengeDataFolder = main.getDataFolder();

            if (!challengeDataFolder.exists()) {
                challengeDataFolder.mkdir();
            }

            File challengeData = new File(main.getDataFolder(), "challengedata.yml");

            if (!challengeData.exists()) {
                challengeData.createNewFile();
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(challengeData));

            String currentLine;

            while ((currentLine = bufferedReader.readLine()) != null) {


                if (currentLine.contains("record: ")) ;
                String recordInSecondsString = currentLine.replace("record: ", "");
                int recordInSeconds = Integer.parseInt(recordInSecondsString);
                ChallengeMain.recordInSeconds = recordInSeconds;

            }
            bufferedReader.close();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void writeDataFile(int recordInSeconds) {
        try {

            File challengeDataFolder = main.getDataFolder();

            if (!challengeDataFolder.exists()) {
                challengeDataFolder.mkdir();
            }

            File challengeData = new File(main.getDataFolder(), "challengedata.yml");

            if (!challengeData.exists()) {
                challengeData.createNewFile();
            }


            PrintWriter printWriter = new PrintWriter(new FileWriter(challengeData));


            List<String> previousSave = new ArrayList<String>();

            String currentLine;


            printWriter.write("record: " + recordInSeconds);

            printWriter.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package challenge;


import java.io.*;

public class ChallengeFileReader {

    private static ChallengeMain main;

    public ChallengeFileReader(ChallengeMain main) {
        ChallengeFileReader.main = main;
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


                String recordInSecondsString = currentLine.replace("record: ", "");
                ChallengeMain.recordInSeconds = Integer.parseInt(recordInSecondsString);

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

            printWriter.write("record: " + recordInSeconds);

            printWriter.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

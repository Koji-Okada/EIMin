package jp.ac.tcu.okadak.ei_mining.text_mining.copycat_analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import jp.ac.tcu.okadak.ei_mining.data_loader.CSVTokenizer;

/**
 * 模倣戦略分析器.
 *
 * @author K.Okada
 * @version 2019.01.05
 */
public class CopycatAnalyzer {

    /**
     * 戦略ワードを抽出する.
     *
     * @param args
     *            ダミー引数
     */
    public static void main(final String[] args) {

        System.out.println("Start analyzing copycats ...");

        CopycatAnalyzer cca = new CopycatAnalyzer();
        cca.analyze();

        System.out.println("... Fin");
        return;
    }

    /**
     * 模倣戦略を分析する.
     */
    private void analyze() {

        String targetPath;
        String outputPath;

        // 設定ファイルから入出力パスを取得する
        try {
            File confFile = new File("conf/CopycatAnalyzer.txt");
            FileReader fr = new FileReader(confFile);
            BufferedReader br = new BufferedReader(fr);

            targetPath = br.readLine();
            outputPath = br.readLine();

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // 候補ワードを読込む
        loadWords(targetPath);

    }

    // =================================================================
    /**
     * 候補ワードを読込む.
     *
     * @param targetPath
     *            入力パス
     */
    private void loadWords(final String targetPath) {

        System.out.println(targetPath);

        File file = new File(targetPath);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String str = br.readLine();

            while (null != (str = br.readLine())) {

                CSVTokenizer tknzr = new CSVTokenizer(str);
                String type = tknzr.nextToken();
                String word = tknzr.nextToken();
                String ent = tknzr.nextToken();

                System.out.println(type + "\t" + word + "\t" + ent);

                //				String word = tknzr.nextToken();
                //				String gram = tknzr.nextToken();
                //				String freq = tknzr.nextToken();
                //				String val = tknzr.nextToken();
                //				Double d = Double.parseDouble(val);

                //				epiDM.putData(entName, year, word, d);
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return;
    }

}

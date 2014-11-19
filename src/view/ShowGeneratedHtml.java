package view;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URI;

class ShowGeneratedHtml {

	private static String URI = "source.html";
	
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(
            new FileReader("part2_sample.txt"));

        File f = new File(URI);
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        bw.write("<html>");
        bw.write("<body>");
        bw.write("<h1 style=\"color: blue; text-align:center\">BeaverBeverGo</h1>");
        bw.write("<textarea cols=75 rows=30>");

        String line;
        while ((line=br.readLine())!=null) {
            bw.write(line);
            bw.newLine();
        }

        bw.write("</text" + "area>");
        bw.write("</body>");
        bw.write("</html>");

        br.close();
        bw.close();

        Desktop.getDesktop().browse(new URI(URI));
    }
}
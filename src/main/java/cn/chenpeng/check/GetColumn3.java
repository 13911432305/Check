package cn.chenpeng.check;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.UnicodeDetector;

public class GetColumn3 {

	public static void main(String[] args) throws IOException, URISyntaxException {
		// 先判断dos输入的命令参数是否全，正确的情况下，应该是输入路径并加文件类型
		if (args.length > 1) {
			throw new RuntimeException("Just input the path, no need to enter other parameters!");
		}

		// 先判断一下输入的路径是否是一个有效的路径
		File fileDir = new File(args[0]);
		if (!fileDir.isDirectory()) {
			throw new RuntimeException("Not an effective path!");
		}

		File rootDir = new File(args[0]);
		// 通过根路径获取下面的子文件夹，此处默认为跟路径下的文件都是文件夹！
		File[] subDirs = rootDir.listFiles();
		ArrayList<File> alBH = new ArrayList<File>();
		ArrayList<File> alDa = new ArrayList<File>();
		for (int i = 0; i < subDirs.length; i++) {
			if (subDirs[i].isDirectory()) {
				String projectName = subDirs[i].getName().substring(0, 2);
				// 判断该子目录文件是否为一个有效的目录文件夹
				if (!subDirs[i].isDirectory()) {
					continue;
				} else {

					// 此处默认二级目录下都是文件，而不是文件夹！
					File[] files = subDirs[i].listFiles();
					// 对二级目录下的文件进行遍历
					for (int j = 0; j < files.length; j++) {

						if (files[j].isDirectory()) {
							continue;
						} else {
							if (files[j].getName().startsWith("BH")) {
								if(files[j].getName().endsWith(".csv")|| files[j].getName().endsWith(".CSV")) {
									alBH.add(files[j]);
								}else {
									System.out.println("一个文件不符合格式标准，请检查：");
									System.out.println("	"+files[j].getAbsolutePath());
								}
							} else if (files[j].getName().startsWith("Da")) {
								if(files[j].getName().endsWith(".csv") || files[j].getName().endsWith(".CSV")) {
									alDa.add(files[j]);
								}else {
									System.out.println("一个文件不符合格式标准，请检查：");
									System.out.println("	"+files[j].getAbsolutePath());
								}
								
							} else {

							}
						}
					}
				}
			}
		}

		int rowBHCount = 0;
		TreeMap<String, Integer> mapBH = new TreeMap<String, Integer>();
		for (int c = 0; c < alBH.size(); c++) {

			BufferedReader br = new BufferedReader(new FileReader(alBH.get(c)));

			String line = br.readLine();
			String[] cells = line.split(",");
			int columnNum = cells.length;
			String lineText = null;
			int lineNum = 0;

			while ((lineText = br.readLine()) != null) {

				lineNum++;
				String dataDate = lineText.split(",")[0].replace("\"", "");
				if (dataDate.length() < 10) {
					mapBH.put(dataDate, 1);
				}
			}
			rowBHCount = rowBHCount + lineNum;

			System.out
					.println("RowCount=" + lineNum + "	ColumnCount=" + columnNum + " ----- " + alBH.get(c).getName());
			String encode2 = getEncode(alBH.get(c));
			System.out.println("Encoding : " + encode2);
			Iterator it = mapBH.keySet().iterator();

			System.out.println("Date list:");
			while (it.hasNext()) {

				System.out.print(it.next() + ";");
			}
			System.out.println();
			br.close();

		}

		System.out.println("------------------------------------------------");
		System.out.println("");
		System.out.println("Total row count:" + (rowBHCount + 1) + " ！Files count：" + alBH.size());
		System.out.println("");
		System.out.println("------------------------------------------------");
		int rowDaCount = 0;
		TreeMap<String, Integer> mapDa = new TreeMap<String, Integer>();
		for (int c = 0; c < alDa.size(); c++) {
			BufferedReader br = new BufferedReader(new FileReader(alDa.get(c)));

			String line = br.readLine();
			String[] cells = line.split(",");
			int columnNum = cells.length;
			String lineText = null;
			int lineNum = 0;

			while ((lineText = br.readLine()) != null) {

				lineNum++;

				String dataDate = lineText.split(",")[0].replace("\"", "");
				if (dataDate.length() < 10) {
					mapDa.put(dataDate, 1);
				}
			}
			rowDaCount = rowDaCount + lineNum;

			System.out
					.println("RowCount=" + lineNum + "	ColumnCount=" + columnNum + " ----- " + alDa.get(c).getName());
			String encode = getEncode(alDa.get(c));
			System.out.println("Encoding : " + encode);
			Iterator it = mapDa.keySet().iterator();
			System.out.println("Date List:");
			while (it.hasNext()) {

				System.out.print(it.next() + ";");
			}
			System.out.println();
			br.close();
		}
		System.out.println("————————————————————");
		System.out.println("");
		System.out.println("Total row count:" + (rowDaCount + 1) + " ！Total files count：" + alDa.size());
		System.out.println("");
		System.out.println("————————————————————");
	}
	public static String getEncode(File f) throws MalformedURLException, IOException {
		
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(UnicodeDetector.getInstance());
		detector.add(JChardetFacade.getInstance());
		detector.add(ASCIIDetector.getInstance());

		Charset charset = detector.detectCodepage(f.toURI().toURL());

		return charset.toString();
	}
}
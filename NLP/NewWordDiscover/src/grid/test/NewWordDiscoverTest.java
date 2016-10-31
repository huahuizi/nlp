package grid.test;

import grid.common.TextDatReader;
import grid.text.evolution.NewWordDiscover;
import grid.text.index.CnPreviewTextIndexer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

public class NewWordDiscoverTest {
	public static void writefile(String m) {

		try {
			File file = new File("result.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fileWritter = new FileWriter(file.getName(), true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(m);
			bufferWritter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		// ��ʼ֮ǰ�����result.txt�����������ظ�
		File filere = new File("result.txt");
		filere.delete();

		Scanner scan = new Scanner(System.in);
		System.out.println("��������Ҫ������ļ�����:\n");
		String path = scan.next();
		File file = new File(path);
		if (!file.exists() || (!file.isFile())) {
			throw new Exception("ָ���ļ������ڣ�");
		}
		long maxsize = 1024 * 1024 * 1024;// 1G,�������ֵ��Ҫ���ļ��з�
		long size = 1024 * 1024 * 5; // ���ļ����Ϊ100M
		long fileLength = file.length();
		if (size <= 0) {
			size = fileLength / 2;
		}
		// ȡ�ñ��ָ���С�ļ�����Ŀ
		int num = (fileLength % size != 0) ? (int) (fileLength / size + 1)
				: (int) (fileLength / size);
		if (file.length() >= maxsize) {
			System.out.println("�ļ���С����1G���Ƿ�ʼ�����ļ��и1:�� 0:��\n");

			int t = scan.nextInt();
			if (t == 1) {
				TextDatReader.divide(path, size);
				System.out.println("�и����\n");
				System.out.println("��������ڵ�ǰĿ¼�µ�dat�ļ�����\n");

			}
			// System.out.println("��������Ҫ������ļ���ţ�����1����dat�ļ����µ�text1.dat\n");
			// int m = scans.nextInt();
			for (int m = 1; m <= num; m++) {
				String pathdived = "./dat/text" + m + ".dat";
				System.out.println("��ʼ��ȡ��" + m + "���ļ�����");
				discovrWord(pathdived);
			}

		} else {
			System.out.println("��ʼ��ȡ�ļ�����");
			discovrWord(path);
		}
	}

	private static void discovrWord(String path) throws IOException {
		String document = TextDatReader.read(path);
		NewWordDiscover discover = new NewWordDiscover();
		Set<String> words = discover.discover(document);
		CnPreviewTextIndexer ci = new CnPreviewTextIndexer(document);
//		long start = System.currentTimeMillis();
//		System.out.println("��ʱ: " + (double) document.length()
//				/ (System.currentTimeMillis() - start) * 1000);
		System.out.println("�´ʸ���: " + words.size());
		System.out.println("���ֵ��´�:" + "\n");
		for (String newword : words) {
			System.out.println(newword + "," + ci.count(newword) + "\n");// �����´ʺ�ͳ��ÿ���´ʳ��ֵĴ���
			writefile(newword + "," + ci.count(newword) + "\n");
		}
	}
}
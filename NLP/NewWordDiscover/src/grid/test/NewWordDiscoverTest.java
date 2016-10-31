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
		// 开始之前，清空result.txt，避免数据重复
		File filere = new File("result.txt");
		filere.delete();

		Scanner scan = new Scanner(System.in);
		System.out.println("请输入您要处理的文件名称:\n");
		String path = scan.next();
		File file = new File(path);
		if (!file.exists() || (!file.isFile())) {
			throw new Exception("指定文件不存在！");
		}
		long maxsize = 1024 * 1024 * 1024;// 1G,超过这个值需要做文件切分
		long size = 1024 * 1024 * 5; // 子文件最大为100M
		long fileLength = file.length();
		if (size <= 0) {
			size = fileLength / 2;
		}
		// 取得被分割后的小文件的数目
		int num = (fileLength % size != 0) ? (int) (fileLength / size + 1)
				: (int) (fileLength / size);
		if (file.length() >= maxsize) {
			System.out.println("文件大小超出1G，是否开始进行文件切割？1:是 0:否\n");

			int t = scan.nextInt();
			if (t == 1) {
				TextDatReader.divide(path, size);
				System.out.println("切割完成\n");
				System.out.println("结果保存在当前目录下的dat文件夹中\n");

			}
			// System.out.println("请输入您要处理的文件序号，例如1代表dat文件架下的text1.dat\n");
			// int m = scans.nextInt();
			for (int m = 1; m <= num; m++) {
				String pathdived = "./dat/text" + m + ".dat";
				System.out.println("开始提取第" + m + "个文件……");
				discovrWord(pathdived);
			}

		} else {
			System.out.println("开始提取文件……");
			discovrWord(path);
		}
	}

	private static void discovrWord(String path) throws IOException {
		String document = TextDatReader.read(path);
		NewWordDiscover discover = new NewWordDiscover();
		Set<String> words = discover.discover(document);
		CnPreviewTextIndexer ci = new CnPreviewTextIndexer(document);
//		long start = System.currentTimeMillis();
//		System.out.println("耗时: " + (double) document.length()
//				/ (System.currentTimeMillis() - start) * 1000);
		System.out.println("新词个数: " + words.size());
		System.out.println("发现的新词:" + "\n");
		for (String newword : words) {
			System.out.println(newword + "," + ci.count(newword) + "\n");// 发现新词后，统计每个新词出现的次数
			writefile(newword + "," + ci.count(newword) + "\n");
		}
	}
}
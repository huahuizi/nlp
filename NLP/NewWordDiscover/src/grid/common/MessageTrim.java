package grid.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Xingliu
 * @category ��ȡ��Ϣ��¼��text.txt
 */
public class MessageTrim {

	// �ж��ַ����Ƿ���ʼ��prefix
	private static final String fullPath = "message.dat";
	private static final String baseFile = "ȫ����Ϣ��¼.txt";

	private boolean IsStartWith(String text, String prefix) {

		return text.contains(prefix);

	}

	// ����ظ��ļ�
	private void fileClear(String path) {
		File files = new File(path);
		// if file doesnt exists, then create it
		if (files.exists()) {
			files.delete();
		}
	}

	// �ж��ַ����Ƿ���ʱ���ʽ
	// \\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{2}:\\d{2}:\\d{2} ���ж�yyyy-MM-dd hh:mm:ss��ʽ
	// \\d{4}-\\d{1,2}-\\d{1,2} ���ж�yyyy-MM-dd��ʽ
	private boolean IsTime(String text) {

		if (text.matches("\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{2}:\\d{2}:\\d{2}")
				|| text.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
			return true;

		}
		return false;

	}

	// �ж��ַ����Ƿ����ʱ���ʽ
	private boolean IsStartWithTime(String text) {
		if (text.length() >= 20 && IsTime(text.substring(0, 10)))
			return true;
		return false;
	}

	public void GBKtoUtf8(String file) throws Exception {
		BufferedReader bre = null;
		BufferedWriter bw = null;// ����һ����

		bre = new BufferedReader(new FileReader(file));// ��ʱ��ȡ����bre���������ļ��Ļ�����
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				"text.dat"), Charset.forName("UTF-8")));// ȷ����������ļ��ͱ����ʽ���˹��̴����ˡ�test.txt��ʵ��
		String str;
		while ((str = bre.readLine()) != null) // �ж����һ�в����ڣ�Ϊ�ս���ѭ��
		{
			bw.write(str);
		}
		;
		bw.close();// �ر���
		bre.close();// �ر���
	}

	private void write(String text, String path) throws IOException {
		File file = new File(path);

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		// true = append file
		FileWriter fileWritter = new FileWriter(file.getName(), true);
		BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		bufferWritter.write(text);
		bufferWritter.close();
	}

	private void read(String path) throws Exception {
		fileClear(fullPath);
		File file = new File(path);
		FileInputStream s = new FileInputStream(file);
		String text;

		// ƥ��[�ַ�]���͵��ַ�������"[����] [123] [abc]֮���"
		String strRegex = "\\[*[\u4e00-\u9fa5]*\\]|\\[*[0-9a-zA-Z]*\\]|\\[*^\\d+$*\\]";

		Pattern pattern = Pattern.compile(strRegex);

		BufferedReader reader = new BufferedReader(new InputStreamReader(s,
				"utf8"));
		for (String line = reader.readLine(); line != null; line = reader
				.readLine()) {
			if (IsStartWith(line, "��Ϣ����:")) {
				text = line.substring(5, line.length());
				String str = "@@@User:" + text;
				write(str, fullPath);
			} else if (IsStartWithTime(line)) {
				write("", fullPath);

			} else if (!IsStartWith(line, "��Ϣ����:") && !IsStartWith(line, "==")
					&& !IsStartWith(line, "��Ϣ��¼")) {
				Matcher m = pattern.matcher(line);
				write(m.replaceAll("").replaceAll("\\[]", "") + "\n", fullPath);

			}
		}
		reader.close();
		
	}

	public static void MsgExtract() throws Exception {
		MessageTrim mt = new MessageTrim();
		mt.read(baseFile);
		mt.GBKtoUtf8(fullPath);
		mt.fileClear(fullPath);
		System.out.println("��ȡ��Ϣ���ϳɹ������������text.dat");

	}
}

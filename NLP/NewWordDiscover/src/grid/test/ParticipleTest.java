package grid.test;

import grid.text.participle.MechanicalParticiple;

import java.util.Vector;

public class ParticipleTest {

	private static String document = "�����й���";

	public static void main(String args[]) {
		MechanicalParticiple participle = new MechanicalParticiple();
		Vector<String> vec = participle.partition(document);
		System.out.println(vec);
	}
}

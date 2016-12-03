package dota2KNN;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KNearestNeighbors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

public class Estimater {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Dataset set = null;
		Dataset test = null;
		Preprocessor preprocessor = new Preprocessor();
		int num = 4000;
		try {
			preprocessor.grab("jdbc:mysql://mf.luokerenz.com:3306/dota", "dotaRead", "ggezteam", num,num/5);
			set = preprocessor.getData();
			test = preprocessor.getTestdata();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int maxid=0;
		double max = 0;

		for (int i = (int) (num*0.1); i < num*0.1 +1 ; i++) {
		Classifier knn = new KNearestNeighbors(i);
			knn.buildClassifier(set);
			int correct = 0, wrong = 0;

			for (Instance inst : set) {
				Object predictedClassValue = knn.classify(inst);
				Object realClassValue = inst.classValue();
				try{
				if (predictedClassValue.equals(realClassValue))
					correct++;
				else
					wrong++;
				} catch(Exception e)
				{}
			}
			double p = (double) correct / (double) (correct + wrong);

			if (max < p) {
				maxid = i;
				max = p;
			}
		}
		System.out.println(maxid + "   " + max);
		Classifier knn = new KNearestNeighbors((int) (maxid/10));
		knn.buildClassifier(set);
		int correct = 0, wrong = 0;

		for (Instance inst : test) {
			Object predictedClassValue = knn.classify(inst);
			Object realClassValue = inst.classValue();
			try{
			if (predictedClassValue.equals(realClassValue))
				correct++;
			else
				wrong++;
			} catch(Exception e)
			{}
		}
		double p = (double) correct / (double) (correct + wrong);
		System.out.println(maxid + "   " + p);
		
	}

}

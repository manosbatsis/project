package testGenerateCode;
import main.GenerateCode;

public class testGenerateCode {
	public static void main(String[] args) {
		GenerateCode generate = new GenerateCode();
		String[] tables = {"t_order"};
		String dbName = "derp-order";
		//这个可以填绝对路径，这是相对路径，生成在当前项目的src/main/java/下；绝对路径例如E:\java
		String dirPath = "src/main/java/";
		String packagePath = "cn.com";
		
		try {
			generate.Generate(tables, dbName, dirPath, packagePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

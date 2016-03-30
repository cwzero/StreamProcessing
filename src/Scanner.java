import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

public class Scanner {
	public static Object x, y;

	public static void main(String argv[]) throws IOException {
		InputStreamReader reader;
		if (argv.length > 0)
			reader = new InputStreamReader(new FileInputStream(argv[0]));
		else
			reader = new InputStreamReader(System.in);

		// create the tokenizer:

		StreamTokenizer tokens = new StreamTokenizer(reader);
		tokens.eolIsSignificant(false);
		tokens.slashStarComments(true);
		tokens.slashSlashComments(true);

		// keep current token in variable "next":
		int next;

		String partition = "";
		List<String> partitions = new ArrayList<String>();

		// while more input, split input stream up into tokens and display them
		// on stdout:
		while ((next = tokens.nextToken()) != StreamTokenizer.TT_EOF) {

			if (next == StreamTokenizer.TT_WORD) {
				partition += tokens.sval;
			} else if (next == StreamTokenizer.TT_NUMBER) {
				partition += tokens.nval;
			} else {

				if (((char) next) == ';' || ((char) next) == '{' || ((char) next) == '}') {
					process(partition);
					partitions.add(partition);
					partition = "";
				} else if (((char) next) == '\'') {
					partition += '\'' + tokens.sval + '\'';
				} else if (((char) next) == '"') {
					partition += '"' + tokens.sval + '"';
				} else {
					partition += (char) next;
				}
			}

			partition += ' ';
		}
	}

	public static void process(String partition) {
		String classname = "";
		if (partition.contains("public class ") && !partition.contains("\"public class \"")) {
			int end = 0;

			if (partition.contains("extends")) {
				end = partition.indexOf("extends");
			}
			if (partition.contains("implements")) {
				end = Math.min(end, partition.indexOf("implements"));
			}

			if (end == 0) {
				end = partition.length();
			}

			classname = partition.substring(partition.indexOf("public class ") + "public class ".length(), end).trim();
			System.out.println("Class: " + classname);
		} else if (partition.contains("public ") && !partition.contains("\"public ")) {
			if (partition.contains(" (")) {
				String obj = partition.substring(partition.substring(0, partition.indexOf(" (")).lastIndexOf(" ") + 1, partition.indexOf(" ("));
				if (!obj.trim().equals(classname))
					System.out.println(obj);
			} else {
				if (partition.contains("=")) {
					partition = partition.substring(0, partition.indexOf('=')).trim();
				}
				
				if (partition.contains(",")) {
					String[] obj = partition.split(",");
					if (obj[0].contains(" ")) {
						obj[0] = obj[0].trim().substring(obj[0].trim().lastIndexOf(' '));
					}
					
					for (String ob: obj) {
						System.out.println(ob.trim());
					}
				}
			}
		}
	}
}
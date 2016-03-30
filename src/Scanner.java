import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scanner {
	public static Object x, y;

	public static List<String> modifiers = Arrays.asList("public", "static", "abstract", "final", "volatile",
			"protected", "private");
	public static List<String> types = Arrays.asList("class", "enum", "interface");

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

		boolean isPublic = false;
		boolean isClass = false;
		boolean isRetType = true;
		boolean isField = false;
		boolean isMethod = false;

		// while more input, split input stream up into tokens and display them
		// on stdout:
		while ((next = tokens.nextToken()) != StreamTokenizer.TT_EOF) {
			if (next == StreamTokenizer.TT_WORD) {
				if (tokens.sval.equals("public")) {
					isPublic = true;
				} else if (isPublic) {
					if (!(modifiers.contains(tokens.sval))) {
						if (types.contains(tokens.sval)) {
							isClass = true;
							isPublic = false;
						} else if (!isRetType) {
							String name = tokens.sval;
							List<String> addFields = new ArrayList<String>();

							while ((next = tokens.nextToken()) != ';' && next != '=' && next != '(') {
								if (next == StreamTokenizer.TT_WORD) {
									addFields.add(tokens.sval);
								}
							}

							if (next == '(') {
								isMethod = true;
							} else {
								isField = true;
							}

							if (isMethod) {
								while ((next = tokens.nextToken()) != ')') {
								}
								while ((next = tokens.nextToken()) != '{') {
								}
								int skip = 0;
								while ((next = tokens.nextToken()) != '}' || skip > 0) {
									if (next == '{')
										skip++;
									else if (next == '}')
										skip--;
								}
								System.out.println("METHOD: " + name);
							} else if (isField) {
								System.out.println("FIELD: " + name);
								for (String f : addFields) {
									System.out.println("FIELD: " + f);
								}
							}

							isField = false;
							isMethod = false;
							isRetType = true;
							isPublic = false;
						} else {
							if ((next = tokens.nextToken()) == '<') {
								while ((next = tokens.nextToken()) != '>') {
								}
							} else {
								tokens.pushBack();
							}
							isRetType = false;
						}
					}
				} else if (isClass) {
					System.out.println("CLASS: " + tokens.sval);
					isClass = false;
				}
			}
		}
	}
}
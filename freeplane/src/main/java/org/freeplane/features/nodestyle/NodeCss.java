package org.freeplane.features.nodestyle;

import javax.swing.text.html.StyleSheet;

import org.freeplane.core.extension.IExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class NodeCss implements IExtension{
	public static final NodeCss EMPTY = new NodeCss("");
	public  String css;
	private StyleSheet styleSheet;

	private final String default_css; //DEFAULT_CSS = "h1 { color:blue; } ";

	public NodeCss(String css) {
		this.css = css;
		this.styleSheet = null;
		this.default_css = getContentFromInputStream(getFileFromResourceAsStream("markdown.css"));
	}

	public StyleSheet getStyleSheet() {
		if(styleSheet == null) {
			//System.out.println("styleSheet is null");
			styleSheet = new StyleSheet();

			if(css!="") {
				//System.out.println("css is not empty.");
				//System.out.println(css);
			}else{
				//System.out.println("css is empty.");
				css = default_css;
			}
			styleSheet.addRule(css);
		}//else{
		//System.out.println("styleSheet is not null");
		//}

		return styleSheet;
	}

	// 以下オリジナル。

	// get a file from the resources folder
	// works everywhere, IDEA, unit test and JAR file.
	private  InputStream getFileFromResourceAsStream(String fileName) {

		// The class loader that loaded the class
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);

		// the stream holding the file content
		if (inputStream == null) {
			throw new IllegalArgumentException("file not found! " + fileName);
		} else {
			return inputStream;
		}

	}


	private static String getContentFromInputStream(InputStream is) {
		StringBuffer css_text = new StringBuffer();
		try (InputStreamReader streamReader =
					 new InputStreamReader(is, StandardCharsets.UTF_8);
			 BufferedReader reader = new BufferedReader(streamReader)) {

			String line;
			while ((line = reader.readLine()) != null) {
				//System.out.println(line);
				css_text.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			css_text.append("");
		}
		return css_text.toString();
	}

}
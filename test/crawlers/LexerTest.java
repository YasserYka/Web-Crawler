package crawlers;

import static org.junit.jupiter.api.Assertions.*;
 

import org.junit.jupiter.api.Test;

class LexerTest {

	@Test
	void testLexer() {
		
		String titleinput = "<title>Google Search</title>";
		tokenByload actualTitle = Lexer.lexer(titleinput);
		String expectedTitle = "Google Search";
		assertEquals(expectedTitle, actualTitle.getData());
				
		String srcInput = "src='/images/nav_logo229.png'";
		tokenByload actualSrc = Lexer.lexer(srcInput);
		String expectedSrc = "/images/nav_logo229.png";
		assertEquals(expectedSrc, actualSrc.getData());
		
		String hrefInput = "href='https://www.google.com/webhp?tab=ww'";
		tokenByload actualHref = Lexer.lexer(hrefInput);
		String expectedHref= "https://www.google.com/webhp?tab=ww";
		assertEquals(expectedHref, actualHref.getData());
		
	}

}

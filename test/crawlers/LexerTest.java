package crawlers;

import static org.junit.jupiter.api.Assertions.*;
 

import org.junit.jupiter.api.Test;

class LexerTest {

	@Test
	void testLexer() {
		
		System.out.println(Token.values()[0].getClass().getName());
		String titleinput = "<title>Google Search</title>";
		Webpage actualTitle = Lexer.lexer(titleinput);
		String expectedTitle = "Google Search";
		assertEquals(expectedTitle, actualTitle.getTitle());
				
		String srcInput = "src='/images/nav_logo229.png'";
		Webpage actualSrc = Lexer.lexer(srcInput);
		String expectedSrc = "/images/nav_logo229.png";
		assertEquals(expectedSrc, actualSrc.getTitle());
		
		String hrefInput = "href='https://www.google.com/webhp?tab=ww'";
		Webpage actualHref = Lexer.lexer(hrefInput);
		String expectedHref= "https://www.google.com/webhp?tab=ww";
		assertEquals(expectedHref, actualHref.getTitle());
		
		String descriptionInput = "<meta name='description' content='Free Web tutorials'>";
		Webpage actualDescription = Lexer.lexer(descriptionInput);
		String expectedDescription= "Free Web tutorials";
		assertEquals(expectedDescription, actualDescription.getTitle());
		
	}

}

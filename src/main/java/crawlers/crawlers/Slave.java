package crawlers.crawlers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import crawlers.modules.DNSResolution;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slave {

	public static void main(String args[]) {
		logger.info("THE CRAWLER IS UP AND RUNNING");
		new Slave().init();
	}
	String html ="<body class=\"light\">\n" + 
			"<div id=\"regex-app\"><div class=\"_2ITh2\"><header class=\"qTp7n\"><a href=\"/\" class=\"_1sfyy _2sp9O _13Cpk _198Rl\"><span class=\"_1Dpdr\"><span class=\"_3Wriv\">Regular</span><span class=\"_3Z8iV\">Reg</span></span><span class=\"pJLAE\"><span class=\"_3Wriv\">Expressions</span><span class=\"_3Z8iV\">Ex</span></span><span class=\"_1UZVO\">101</span></a><div class=\"HFUvg\"><div class=\"_2PLNZ\"><a href=\"https://twitter.com/regex101\" class=\"_1sfyy nF9oJ _2sYhi\" target=\"_blank\"><div class=\"kztpV t6LQ_ _2R3D9\"></div><span class=\"_28LYx\">@regex101</span></a></div><div class=\"_2PLNZ\"><a href=\"https://www.paypal.com/cgi-bin/webscr?cmd=_donations&amp;business=firas%2edib%40gmail%2ecom&amp;lc=US&amp;item_name=Regex101&amp;no_note=0&amp;currency_code=USD&amp;bn=PP%2dDonationsBF%3abtn_donate_SM%2egif%3aNonHostedGuest\" class=\"_1sfyy nF9oJ _1aDRh\" target=\"_blank\"><div class=\"kztpV _2pWui _2R3D9\"></div><span class=\"_28LYx\">Donate</span></a></div><div class=\"_2PLNZ\"><a href=\"mailto:contact@regex101.com\" class=\"_1sfyy nF9oJ _3azOt\" target=\"_blank\"><div class=\"kztpV Km9ZA _2R3D9\"></div><span class=\"_28LYx\">Contact</span></a></div><div class=\"_2PLNZ\"><a href=\"https://github.com/firasdib/Regex101/issues\" class=\"_1sfyy nF9oJ _3N0Hg\" target=\"_blank\"><div class=\"kztpV _3zkKr _2R3D9\"></div><span class=\"_28LYx\">Bug Reports &amp; Feedback</span></a></div><div class=\"_2PLNZ\"><a href=\"https://github.com/firasdib/Regex101/wiki\" class=\"_1sfyy nF9oJ _1Q8NC\" target=\"_blank\"><div class=\"kztpV _2-Op2 _2R3D9\"></div><span class=\"_28LYx\">Wiki</span></a></div></div></header><nav class=\"_2q2Gx\"><div class=\"_2eQHI\"><a class=\"_1sfyy _2Q760 _3nfQ5\" aria-current=\"true\" href=\"/\"><div class=\"_3LSQH _1idGU\" style=\"height: 34px; width: 34px;\"><div class=\"kztpV _2V_Bb _2l_ud\"></div></div></a><a class=\"_1sfyy _2Q760\" aria-current=\"false\" href=\"/library\"><div class=\"_3LSQH _1idGU\" style=\"height: 34px; width: 34px;\"><div class=\"kztpV -pk71 _2l_ud\"></div></div></a><a class=\"_1sfyy _2Q760\" aria-current=\"false\" href=\"/account\"><div class=\"_3LSQH _1idGU\" style=\"height: 34px; width: 34px;\"><div class=\"kztpV _24twB _2l_ud\"></div></div></a><a class=\"_1sfyy _2Q760\" aria-current=\"false\" href=\"/quiz\"><div class=\"_3LSQH _1idGU\" style=\"height: 34px; width: 34px;\"><div class=\"kztpV _18AVo _2l_ud\"></div></div></a><a class=\"_1sfyy _2Q760\" aria-current=\"false\" href=\"/settings\"><div class=\"_3LSQH _1idGU\" style=\"height: 34px; width: 34px;\"><div class=\"kztpV _3VvpC _2l_ud\"></div></div></a><a href=\"http://webchat.freenode.net/?channels=regex&amp;nick=re101-goose-...\" class=\"_1sfyy _2Q760\" target=\"_blank\"><div class=\"_3LSQH _1idGU\" style=\"height: 34px; width: 34px;\"><div class=\"kztpV _1wjsZ _2l_ud\"></div></div></a><a href=\"https://www.paypal.com/cgi-bin/webscr?cmd=_donations&amp;business=firas%2edib%40gmail%2ecom&amp;lc=US&amp;item_name=Regex101&amp;no_note=0&amp;currency_code=USD&amp;bn=PP%2dDonationsBF%3abtn_donate_SM%2egif%3aNonHostedGuest\" class=\"_1sfyy _2Q760 D-Unr\" target=\"_blank\"><div class=\"_3LSQH _1idGU\" style=\"height: 34px; width: 34px;\"><div class=\"kztpV _2pWui _2l_ud\"></div></div></a></div><div class=\"_3tDL-\"><div class=\"_3nQkc\"><div class=\"_1ksHK _1TLPw\"><div class=\"_2pcE6 _1XK4v _2tmCK _161o4\"><span class=\"_1XK4v\">Save &amp; Share</span></div><ul class=\"_2v5h0\"><li class=\"_15gkk\"><div class=\"_3FxKO _18Ike _3U--R _3EYLo\"><div class=\"kztpV zfYzP _38v59\"></div><div class=\"S8Y5s\">Save Regex</div><div class=\"_28Jw2\">ctrl+s</div></div></li></ul></div><div class=\"_1ksHK _1TLPw\"><div class=\"_2pcE6 _1XK4v _2tmCK _161o4\"><span class=\"_1XK4v\">Flavor</span></div><ul class=\"_2v5h0\"><li class=\"_15gkk\"><div class=\"_3FxKO _18Ike\"><div class=\"kztpV _2V_Bb _38v59\"></div><div class=\"_2fu3E _2qFpL\"><div class=\"rdUxR\">PCRE (PHP)</div><div class=\"kztpV _3Q1D2 _22-Pt\"></div></div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO _18Ike\"><div class=\"kztpV _2V_Bb _38v59\"></div><div class=\"_2fu3E\"><div class=\"rdUxR\">ECMAScript (JavaScript)</div></div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO _18Ike\"><div class=\"kztpV _2V_Bb _38v59\"></div><div class=\"_2fu3E\"><div class=\"rdUxR\">Python</div></div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO _18Ike\"><div class=\"kztpV _2V_Bb _38v59\"></div><div class=\"_2fu3E\"><div class=\"rdUxR\">Golang</div></div></div></li></ul></div><div class=\"_1TLPw\"><div class=\"_2pcE6 _1XK4v _2tmCK _161o4\"><span class=\"_1XK4v\">Tools</span></div><ul class=\"_2v5h0\"><li class=\"_15gkk\"><a class=\"_1sfyy _1mPL2\" href=\"/codegen?language=php\"><div class=\"_3FxKO _18Ike\"><div class=\"kztpV WdDiJ _38v59\"></div>Code Generator</div></a></li><li class=\"_15gkk\"><a class=\"_1sfyy _1mPL2\" href=\"/debugger\"><div class=\"_3FxKO _18Ike\"><div class=\"kztpV _2ZuG8 _38v59\"></div>Regex Debugger</div></a></li></ul></div></div><div class=\"_234pj y-FD0\"><div class=\"_39_-O\">Sponsor</div></div></div></nav><div class=\"fy8Gb\"><div class=\"Yn9Hf\"><div class=\"_1Eaxs\"><div class=\"_2VrEH\"></div><div class=\"_3gSSf _3Jyur _2dnUC\"><div class=\"_2pcE6 _1XK4v _2tmCK _2YMI3 _3h7q5 EEcFf\"><span class=\"_1XK4v\">Explanation</span><div class=\"kztpV _2ARzF _3v_zA\"></div></div><div class=\"_1UjST _1Rgjl\"><div class=\"_2DSo9\"><div class=\"R5C_f\"><div class=\"_3S8bD\"><div class=\"_3k2EX\"><div class=\"_1aBlp\"></div></div><div class=\"tUney\"><div class=\"vttQq _3XIyS\">/</div><div class=\"_1-NVk _1gy8R\"><span class=\"\">href=</span><span class=\"A4U96 _13Ps7\">[</span><span class=\"LtTRm _3edcn\">\\\"</span><span class=\"LtTRm\">|'</span><span class=\"LtTRm A4U96 _13Ps7\">]</span><span class=\"_1xGnW\">(</span><span class=\"_13Ps7\">.</span><span class=\"_1L8oL\">*?</span><span class=\"_1xGnW\">)</span><span class=\"A4U96 _13Ps7\">[</span><span class=\"LtTRm _3edcn\">\\\"</span><span class=\"LtTRm\">|'</span><span class=\"LtTRm A4U96 _13Ps7\">]</span></div><div class=\"xuMhS _3XIyS\">/</div><div class=\"_16lz9\">gm</div></div></div><div class=\"gNOnH\"><div><span class=\"_2EVlr\">href=</span> <span>matches the characters <span class=\"_3Qv4l\">href=</span> literally (case sensitive)</span></div><div class=\"R5C_f\"><div class=\"_3S8bD\"><div class=\"_3k2EX\"><div class=\"_1aBlp\"></div></div><span><span class=\"_2pDcO WVzuP\">Match a single character present in the list below</span> <div class=\"_1-NVk WVzuP\"><span class=\"A4U96 _13Ps7\">[</span><span class=\"LtTRm _3edcn\">\\\"</span><span class=\"LtTRm\">|'</span><span class=\"LtTRm A4U96 _13Ps7\">]</span></div></span></div><div class=\"gNOnH\"><div><span class=\"LtTRm _3edcn _2EVlr\">\\\"</span> <span>matches the character <span class=\"_3Qv4l\">\"</span> literally (case sensitive)</span></div><div><span class=\"LtTRm _2EVlr\">|'</span> <span>matches a single character in the list <span class=\"_3Qv4l\">|'</span> (case sensitive)</span></div></div></div><div class=\"R5C_f\"><div class=\"_3S8bD\"><div class=\"_3k2EX\"><div class=\"_1aBlp\"></div></div><span><span class=\"_2pDcO WVzuP\">1st Capturing Group</span> <div class=\"_1-NVk WVzuP\"><span class=\"_1xGnW\">(</span><span class=\"_13Ps7\">.</span><span class=\"_1L8oL\">*?</span><span class=\"_1xGnW\">)</span></div></span></div><div class=\"gNOnH\"><div class=\"R5C_f\"><div class=\"_3S8bD\"><div class=\"_3k2EX\"><div class=\"_1aBlp\"></div></div><span><div class=\"_1-NVk WVzuP\"><span class=\"_13Ps7\">.</span><span class=\"_1L8oL\">*?</span></div> <span class=\"_8uUO3\">matches any character (except for line terminators)<div class=\"OyRPR\"><div class=\"kztpV _38Blf\"></div></div></span></span></div><div class=\"gNOnH\"><div><span class=\"_1L8oL _2EVlr\">*?</span> <span><strong>Quantifier</strong> — Matches between <span class=\"Z3H4l\">zero</span> and <span class=\"Z3H4l\">unlimited</span> times, as few times as possible, expanding as needed <span class=\"_2P7Bb\">(lazy)</span></span></div></div></div></div></div><div class=\"R5C_f\"><div class=\"_3S8bD\"><div class=\"_3k2EX\"><div class=\"_1aBlp\"></div></div><span><span class=\"_2pDcO WVzuP\">Match a single character present in the list below</span> <div class=\"_1-NVk WVzuP\"><span class=\"A4U96 _13Ps7\">[</span><span class=\"LtTRm _3edcn\">\\\"</span><span class=\"LtTRm\">|'</span><span class=\"LtTRm A4U96 _13Ps7\">]</span></div></span></div><div class=\"gNOnH\"><div><span class=\"LtTRm _3edcn _2EVlr\">\\\"</span> <span>matches the character <span class=\"_3Qv4l\">\"</span> literally (case sensitive)</span></div><div><span class=\"LtTRm _2EVlr\">|'</span> <span>matches a single character in the list <span class=\"_3Qv4l\">|'</span> (case sensitive)</span></div></div></div><div class=\"R5C_f\"><div class=\"_3S8bD\"><div class=\"_3k2EX\"><div class=\"_1aBlp\"></div></div><span><span class=\"_2pDcO WVzuP\">Global pattern flags</span> </span></div><div class=\"gNOnH\"><div><span class=\"TEFRq\">g modifier:</span> <span><strong>g</strong>lobal. All matches (don't return after first match)</span></div><div><span class=\"TEFRq\">m modifier:</span> <span><strong>m</strong>ulti line. Causes <span class=\"_3Qv4l\">^</span> and <span class=\"_3Qv4l\">$</span> to match the begin/end of each line (not only begin/end of string)</span></div></div></div></div></div></div></div></div><div class=\"_3gSSf _3Jyur _2dnUC\"><div class=\"_2pcE6 _1XK4v _2tmCK _2YMI3 _3h7q5 EEcFf\"><span class=\"_1XK4v\">Match Information</span><div class=\"kztpV _2ARzF _3v_zA\"></div></div><div class=\"_1UjST _1Rgjl\"><div class=\"_1XfJW\">Your regular expression does not match the subject string.</div></div></div><div class=\"_3gSSf _3Jyur\"><div class=\"_2pcE6 _1XK4v _2tmCK _2YMI3 _3h7q5 EEcFf\"><span class=\"_1XK4v\">Quick Reference</span><div class=\"kztpV _2ARzF _3v_zA\"></div></div><div class=\"_1UjST _1Rgjl\"><div class=\"_3kyJ0\"><div class=\"_3Wa2V\"><div class=\"_1ZKL5 _2xfGV\"><input type=\"text\" placeholder=\"Search reference\" class=\"_2AEZR _29pkB\" value=\"\"></div><div class=\"\"><ul class=\"_2v5h0\"><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh\"><div class=\"kztpV _2Kabi _38v59\"></div><div class=\"_2fu3E\"><div class=\"rdUxR\">All Tokens</div></div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh\"><div class=\"kztpV _2PhH6 _38v59\"></div><div class=\"_2fu3E _2qFpL\"><div class=\"rdUxR\">Common Tokens</div><div class=\"kztpV _3Q1D2 _22-Pt\"></div></div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh\"><div class=\"kztpV _2ceKL _38v59\"></div><div class=\"_2fu3E\"><div class=\"rdUxR\">General Tokens</div></div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh\"><div class=\"kztpV _66uNe _38v59\"></div><div class=\"_2fu3E\"><div class=\"rdUxR\">Anchors</div></div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh\"><div class=\"kztpV Gtw4X _38v59\"></div><div class=\"_2fu3E\"><div class=\"rdUxR\">Meta Sequences</div></div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh\"><div class=\"kztpV lLtlJ _38v59\"></div><div class=\"_2fu3E\"><div class=\"rdUxR\">Quantifiers</div></div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh\"><div class=\"kztpV _2ceKL _38v59\"></div><div class=\"_2fu3E\"><div class=\"rdUxR\">Group Constructs</div></div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh\"><div class=\"kztpV _2VIGX _38v59\"></div><div class=\"_2fu3E\"><div class=\"rdUxR\">Character Classes</div></div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh\"><div class=\"kztpV MyHSu _38v59\"></div><div class=\"_2fu3E\"><div class=\"rdUxR\">Flags/Modifiers</div></div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh\"><div class=\"kztpV Xf7v5 _38v59\"></div><div class=\"_2fu3E\"><div class=\"rdUxR\">Substitution</div></div></div></li></ul></div></div><div class=\"_3VUam\"></div><div class=\"_2M3yW\"><ul class=\"_2v5h0\"><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">A single character of: a, b or c</div><div class=\"_28Jw2\">[abc]</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">A character except: a, b or c</div><div class=\"_28Jw2\">[^abc]</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">A character in the range: a-z</div><div class=\"_28Jw2\">[a-z]</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">A character not in the range: a-z</div><div class=\"_28Jw2\">[^a-z]</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">A character in the range: a-z or A-Z</div><div class=\"_28Jw2\">[a-zA-Z]</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">Any single character</div><div class=\"_28Jw2\">.</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">Any whitespace character</div><div class=\"_28Jw2\">\\s</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">Any non-whitespace character</div><div class=\"_28Jw2\">\\S</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">Any digit</div><div class=\"_28Jw2\">\\d</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">Any non-digit</div><div class=\"_28Jw2\">\\D</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">Any word character</div><div class=\"_28Jw2\">\\w</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">Any non-word character</div><div class=\"_28Jw2\">\\W</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">Capture everything enclosed</div><div class=\"_28Jw2\">(...)</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">Match either a or b</div><div class=\"_28Jw2\">(a|b)</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">Zero or one of a</div><div class=\"_28Jw2\">a?</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">Zero or more of a</div><div class=\"_28Jw2\">a*</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">One or more of a</div><div class=\"_28Jw2\">a+</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">Exactly 3 of a</div><div class=\"_28Jw2\">a{3}</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">3 or more of a</div><div class=\"_28Jw2\">a{3,}</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">Between 3 and 6 of a</div><div class=\"_28Jw2\">a{3,6}</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">Start of string</div><div class=\"_28Jw2\">^</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">End of string</div><div class=\"_28Jw2\">$</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">A word boundary</div><div class=\"_28Jw2\">\\b</div></div></li><li class=\"_15gkk\"><div class=\"_3FxKO TiBVh _3U--R\"><div class=\"S8Y5s\">Non-word boundary</div><div class=\"_28Jw2\">\\B</div></div></li></ul></div></div></div></div></div><div class=\"_1q2k8\"><div class=\"_3Sqfl\"><div class=\"_2t_G5\"><div class=\"_2pcE6 _2tmCK\"><span class=\"\">Regular Expression</span><div class=\"_2owEq _2ZM28\">No Match, 0 steps (~0ms)</div></div></div><div class=\"_153v9\"><div class=\"oW_T9\"><div class=\"_3bjf5\"><div class=\"kztpV -jCun _3ZHnv\"></div>/</div></div><div class=\"_3kj4K\"><div class=\"\"><textarea autocomplete=\"off\" style=\"display: none;\"></textarea><div class=\"CodeMirror cm-s-default CodeMirror-wrap\"><div style=\"overflow: hidden; position: relative; width: 3px; height: 0px; top: 2px; left: 208.922px;\"><textarea autocorrect=\"off\" autocapitalize=\"off\" spellcheck=\"false\" style=\"position: absolute; bottom: -1em; padding: 0px; width: 1000px; height: 1em; outline: none;\" tabindex=\"1\"></textarea></div><div class=\"CodeMirror-vscrollbar\" tabindex=\"-1\" cm-not-content=\"true\"><div style=\"min-width: 1px; height: 0px;\"></div></div><div class=\"CodeMirror-hscrollbar\" tabindex=\"-1\" cm-not-content=\"true\"><div style=\"height: 100%; min-height: 1px; width: 0px;\"></div></div><div class=\"CodeMirror-scrollbar-filler\" cm-not-content=\"true\"></div><div class=\"CodeMirror-gutter-filler\" cm-not-content=\"true\"></div><div class=\"CodeMirror-scroll\" tabindex=\"-1\"><div class=\"CodeMirror-sizer\" style=\"margin-left: 0px; margin-bottom: -15px; border-right-width: 15px; min-height: 26px; padding-right: 0px; padding-bottom: 0px;\"><div style=\"position: relative; top: 0px;\"><div class=\"CodeMirror-lines\" role=\"presentation\"><div role=\"presentation\" style=\"position: relative; outline: none;\"><div class=\"CodeMirror-measure\"><pre>x</pre></div><div class=\"CodeMirror-measure\"></div><div style=\"position: relative; z-index: 1;\"></div><div class=\"CodeMirror-cursors\" style=\"\"><div class=\"CodeMirror-cursor\" style=\"left: 208.922px; top: 0px; height: 22px;\">&nbsp;</div></div><div class=\"CodeMirror-code\" role=\"presentation\"><pre class=\" CodeMirror-line \" role=\"presentation\"><span role=\"presentation\" style=\"padding-right: 0.1px;\">href=<span class=\" A4U96 _13Ps7\">[</span><span class=\" LtTRm _3edcn\">\\\"</span><span class=\" LtTRm\">|'</span><span class=\" LtTRm A4U96 _13Ps7\">]</span><span class=\" _1xGnW\">(</span><span class=\" _13Ps7\">.</span><span class=\" _1L8oL\">*?</span><span class=\" _1xGnW\">)</span><span class=\" A4U96 _13Ps7\">[</span><span class=\" LtTRm _3edcn\">\\\"</span><span class=\" LtTRm\">|'</span><span class=\" LtTRm A4U96 _13Ps7\">]</span></span></pre></div></div></div></div></div><div style=\"position: absolute; height: 15px; width: 1px; border-bottom: 0px solid transparent; top: 26px;\"></div><div class=\"CodeMirror-gutters\" style=\"display: none; height: 41px;\"></div></div></div></div></div><div class=\"_3gUKC\" tabindex=\"2\"><div class=\"_22YEW\"><div class=\"_3s7QJ\">/</div><div class=\"_3odKe\">gm</div><div class=\"kztpV MyHSu _16HX4\"></div></div></div></div></div><div class=\"_3933o\"><div class=\"_1NmJe\"><div class=\"_3KtAM\"><div class=\"_2pcE6 _1XK4v _2tmCK _3EU8D\"><span class=\"_1XK4v\">Test String</span><div class=\"_3Teag\"><a class=\"_1sfyy _3h_QS\" href=\"/tests\">Switch to Unit Tests <div class=\"kztpV ryfpM _2-xT8\"></div></a></div></div><div class=\"_137ys\"><div class=\"_3o7og\"><textarea autocomplete=\"off\" style=\"display: none;\"></textarea><div class=\"CodeMirror cm-s-default CodeMirror-wrap CodeMirror-empty\"><div style=\"overflow: hidden; position: relative; width: 3px; height: 0px; top: 0px; left: 0px;\"><textarea autocorrect=\"off\" autocapitalize=\"off\" spellcheck=\"false\" style=\"position: absolute; bottom: -1em; padding: 0px; width: 1000px; height: 1em; outline: none;\" tabindex=\"3\"></textarea></div><div class=\"CodeMirror-vscrollbar\" tabindex=\"-1\" cm-not-content=\"true\"><div style=\"min-width: 1px; height: 0px;\"></div></div><div class=\"CodeMirror-hscrollbar\" tabindex=\"-1\" cm-not-content=\"true\"><div style=\"height: 100%; min-height: 1px; width: 0px;\"></div></div><div class=\"CodeMirror-scrollbar-filler\" cm-not-content=\"true\"></div><div class=\"CodeMirror-gutter-filler\" cm-not-content=\"true\"></div><div class=\"CodeMirror-scroll\" tabindex=\"-1\"><div class=\"CodeMirror-sizer\" style=\"margin-left: 0px; margin-bottom: -15px; border-right-width: 15px; min-height: 22px; padding-right: 0px; padding-bottom: 0px;\"><div style=\"position: relative; top: 0px;\"><div class=\"CodeMirror-lines\" role=\"presentation\"><div role=\"presentation\" style=\"position: relative; outline: none;\"><pre class=\"CodeMirror-placeholder\" style=\"height: 0px; overflow: visible; direction: ltr;\">insert your test string here</pre><div class=\"CodeMirror-measure\"><pre><span>xxxxxxxxxx</span></pre></div><div class=\"CodeMirror-measure\"></div><div style=\"position: relative; z-index: 1;\"></div><div class=\"CodeMirror-cursors\" style=\"visibility: hidden;\"><div class=\"CodeMirror-cursor\" style=\"left: 0px; top: 0px; height: 22px;\">&nbsp;</div></div><div class=\"CodeMirror-code\" role=\"presentation\"><pre class=\" CodeMirror-line \" role=\"presentation\"><span role=\"presentation\" style=\"padding-right: 0.1px;\"><span cm-text=\"\">&#8203;</span></span></pre></div></div></div></div></div><div style=\"position: absolute; height: 15px; width: 1px; border-bottom: 0px solid transparent; top: 22px;\"></div><div class=\"CodeMirror-gutters\" style=\"display: none; height: 37px;\"></div></div></div></div><canvas class=\"_1VqDL\" width=\"1002\" height=\"1124\" style=\"left: 0px;\"></canvas></div></div><div class=\"_3nOf6\"><div class=\"_2pcE6 _1XK4v _2tmCK _2YMI3\"><span class=\"_1XK4v\">Substitution</span><div class=\"kztpV Yb_kX _3v_zA\"></div></div></div></div></div></div></div></div></div></div>\n" + 
			"\n" + 
			"\n" + 
			"</body>";
	private static final Logger logger = LoggerFactory.getLogger(Slave.class);
	//Address of slave
	private String address;
	//Dealer socket for Dealer-Router pattern
	private ZMQ.Socket DLR; 
	//subscriber socket for Subscriber-Publisher pattern used to receive heart beat from master
	private ZMQ.Socket SUB;
	//to read from multiple sockets
	private ZMQ.Poller poller;
	//This set to true if work not done yet
	private boolean busy;
	//Master will send heart beats every 5mscs
	private final static int HEARTBEAT_INTERVAL = 5000;
	//Liveness of the master (when we don't receive heart beat form master 10 times (10 heart beat intervals) means the master is down)
	private final static int livenessOfMaster = 10;
	//event heart-beat
	private final static String HEARTBEAT = "001";
	//event ready-for-work;
	private final static String READY_FOR_WORK = "002";
	//event task is done
	private final static String WORK_FINISHED = "003";
	//event task to be done
	private final static String WORK_TO_BE_DONE = "004";
	//Counter for liveness of master
    private int liveness;
	//Address to bind-to for Dealer-Router locally
	private final String DEALER_ADDRESS = "tcp://127.0.0.1:5555";
	//Address to bind-to for Subscriber-Publisher locally
	private final String SUBSCRIBER_ADDRESS = "tcp://localhost:5556";
	//Used to generate unique identity
	private Random random;
	//Instance of Redis
	private RedissonClient redisson;
	//Redis based distributed Map
	private RMap<String, String> cache;
	//Counter for RMap
	private int counter;
	//Apache's http client instance
    private  CloseableHttpClient httpClient;
    //default URL of Redis
    private final static String REDIS_ADDRESS = "redis://127.0.0.1:6379";

	protected Slave() {
        busy = false;
        liveness = livenessOfMaster;
        random = new Random(System.nanoTime());
	}
	
	public void init() {
		try (ZContext context = new ZContext()) {
			
			DLR = context.createSocket(SocketType.DEALER);
			SUB = context.createSocket(SocketType.SUB);
		    poller = context.createPoller(2);
		    
		    //Set identity for master
		    address = String.format("%04X-%04X", random.nextInt(), random.nextInt());
		    DLR.setIdentity(address.getBytes(ZMQ.CHARSET));
		    logger.info("ADDRESS CREATED {}", address);
		    
		    //Sub subscribe to all kind of message of master (disable filtering)
		    SUB.subscribe(ZMQ.SUBSCRIPTION_ALL);
		    
		    DLR.connect(DEALER_ADDRESS);
		    SUB.connect(SUBSCRIBER_ADDRESS);
		    
		    poller.register(DLR, ZMQ.Poller.POLLIN);
		    poller.register(SUB, ZMQ.Poller.POLLIN); 
		    
		    establishConnectionToCache();

		    while (true) {
		    	//check for message in this interval
		    	poller.poll(HEARTBEAT_INTERVAL);
		    	
		    	//Received message from master via dealer
		    	if(poller.pollin(0)) {
		    		String event = DLR.recvStr();
		    		String body = DLR.recvStr();
		    		if(event.equals(WORK_TO_BE_DONE)) {
			    		logger.info("WORK RECIVED FORM MASTER WITH BODY {}", body);
		    			crawl(body);
		    		}
		    	}
		        
		    	//Heart beat from master
		    	if(poller.pollin(1)) {
		    		String messageReceived = SUB.recvStr();

		    		//If message from master is a heart beat handle it other wise 
		    		if(messageReceived.equals(HEARTBEAT))
		    			handleHeartbeat();
		    		else
		    			handleWrongMessage(messageReceived);
		    	}else
		    		//if liveness equal zero means master is down call selfDestruction
		    		if(--liveness == 0)
		    			selfDestruction(context);
		      }
		}
	}
	
	public String generateKey() {
		return DLR.getIdentity().toString() + counter++;
	}
	
	public void addToCache(String key, String document) {
		cache.fastPutAsync(key, document);
	}
	
	//Takes key of where the document was stored in cache
	public void handleFinishedWork(String key) {
		DLR.sendMore(WORK_FINISHED);
		DLR.send(key);
		logger.info("FINISHED WORK SENT");
		busy = false;
	}
	
	//when received message not like what we expected
	public void handleWrongMessage(String messageReceived) {logger.error("INVALID MESSAGE RECEIVED {}", messageReceived);}
	
	//check if heart beat is correct message if true reset liveness if no call handleWrongMessage
	public void handleHeartbeat(){
		logger.info("HEARTBEAT RECIVED");
		liveness = HEARTBEAT_INTERVAL;
    	if(!busy)
    		sendRequestForWork();
	}

	//When master dosen't send a heart beat for long time kill this whole thread
	public void selfDestruction(ZContext context){
		logger.info("OPERATING SELF DESTRUCTION");
		context.destroySocket(DLR);
		context.destroySocket(SUB);
		System.exit(0);
	}
	
	public void establishConnectionToCache() {
		redisson = Redisson.create(cacheConfiguration());
		cache = redisson.getMap("test");
	}
	
	public void sendRequestForWork() {
		DLR.sendMore(READY_FOR_WORK);
		DLR.send(" ");
		logger.info("REQUEST FOR WORK SENT");
	}
	
	public Config cacheConfiguration() {
		Config config = new Config();
		config.useSingleServer().setAddress(REDIS_ADDRESS);
		return config;
	}
	
	//Gets host-name's as IntetAddress format then makes get request and returns it's body as String
	public String makeRequest(URI uri) {
		//getHostAddress convert InetAddress to string presentation
		HttpGet request = new HttpGet(uri);
		httpClient = HttpClients.createDefault();
        HttpEntity entity = null;
		String content = "";
		
		request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");
		
        try (CloseableHttpResponse response = httpClient.execute(request)) {
        	
        	logger.info("HTTP REQUEST HAS BEEN SENT TO {}", uri.toURL());
        	
        	response.getAllHeaders().toString();
        	
        	if(response.getStatusLine().getStatusCode() == 200){
        		entity = response.getEntity();
        		content = response.getAllHeaders().toString();
        	}
        		
        	if(entity != null)
        		content += EntityUtils.toString(entity);
		}
        catch(ClientProtocolException cpe) {logger.error("SOMETHING WENT WORNG {}",cpe);}
        catch (IOException ie) {logger.error("SOMETHING WENT WORNG {}",ie);}
        
        return content;
	}
	
	//TODO: Call resolver in some way
	//TODO: for now ill call it via fs
	public void crawl(String domainName){
		logger.info("REQUEST SENT TO DOMAINNAME {}", domainName);
		busy = true; 
		String address = DNSResolution.resolveHostnameToIP(domainName).getHostAddress();
		URI uri = buildUri(address);
		String conent = html;/*(uri);*/
		addToCache(domainName, conent);
		handleFinishedWork(domainName);
	}	
	
	public URI buildUri(String address) {
		try {return new URIBuilder().setScheme("http").setHost(address).build();} catch (URISyntaxException e) {logger.debug(e.toString());}
		return null;
	}
}

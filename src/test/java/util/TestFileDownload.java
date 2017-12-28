package util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestFileDownload {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testDownload() {
		FileDownload.download("http://www.bochenek.ca/gallery/pictures/1_Davos/folder.png", "/tmp/test.png");
	}

}

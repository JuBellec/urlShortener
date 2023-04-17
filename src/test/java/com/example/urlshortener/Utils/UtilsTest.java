package com.example.urlshortener.Utils;

import com.example.urlshortener.utils.Utils;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class UtilsTest {

    @Test
    public void testEncode() {
        long input = 123456789;
        String expectedOutput = "21i3V9";

        String actualOutput = Utils.encode(input);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDecode() {
        String input = "21i3V9";
        long expectedOutput = 123456789;

        long actualOutput = Utils.decode(input);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testIsUrlValid() throws Exception {
        String validUrl = "https://www.google.com";
        String invalidUrl = "not a valid url";

        URL mockUrl = mock(URL.class);
        when(mockUrl.toURI()).thenReturn(null);

        URL realUrl = new URL(validUrl);

        assertTrue(Utils.isUrlValid(validUrl));
        assertFalse(Utils.isUrlValid(invalidUrl));

        verify(mockUrl, times(1)).toURI();
        realUrl.toURI(); // to make sure the real URL is valid
    }
}

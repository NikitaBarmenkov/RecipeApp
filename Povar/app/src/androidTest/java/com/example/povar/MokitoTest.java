package com.example.povar;

import com.example.povar.Adapters.SearchResultsAdapter;

import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MokitoTest {

    @Mock
    SearchResultsAdapter m;

    @Test
    public void Test() {
        m = mock(SearchResultsAdapter.class);
        when(m.getItemCount()).thenReturn(6);
        assertEquals(6, m.getItemCount());
    }
}
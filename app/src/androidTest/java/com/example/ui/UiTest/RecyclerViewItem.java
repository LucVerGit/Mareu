package com.example.ui.UiTest;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;

public class RecyclerViewItem implements ViewAssertion {

    private final Matcher<Integer> matcher;

    public static RecyclerViewItem withItemCount(int expectedCount) {
        return withItemCount(Matchers.is(expectedCount));
    }

    public static RecyclerViewItem withItemCount(Matcher<Integer> matcher) {
        return new RecyclerViewItem(matcher);
    }

    private RecyclerViewItem(Matcher<Integer> matcher) {
        this.matcher = matcher;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {

        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null) Assert.assertThat(adapter.getItemCount(), matcher);

    }
}

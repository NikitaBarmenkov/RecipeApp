package com.example.povar;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MyNewTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void myNewTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.dishpage_name_to_edit),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fl_content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.dishpage_name_to_edit),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fl_content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("мое"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.dishpage_name_to_edit), withText("мое"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fl_content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText3.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.dishpage_name_to_edit), withText("мое"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fl_content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("мое блюдо"));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.dishpage_name_to_edit), withText("мое блюдо"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fl_content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText5.perform(closeSoftKeyboard());

        pressBack();

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.category_spinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fl_content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction checkedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(0);
        checkedTextView.perform(click());

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_recipe), withContentDescription("Рецепт"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.dishpage_recipe_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fl_content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("шаги"), closeSoftKeyboard());

        pressBack();

        ViewInteraction bottomNavigationItemView2 = onView(
                allOf(withId(R.id.navigation_ingredient), withContentDescription("Ингредиенты"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation),
                                        0),
                                2),
                        isDisplayed()));
        bottomNavigationItemView2.perform(click());

        ViewInteraction appCompatAutoCompleteTextView = onView(
                allOf(withId(R.id.ingredient_name_to_edit),
                        childAtPosition(
                                allOf(withId(R.id.ingredient_to_edit),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatAutoCompleteTextView.perform(replaceText("пом"), closeSoftKeyboard());

        DataInteraction appCompatTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(0);
        appCompatTextView.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.ingredient_quantity_to_edit),
                        childAtPosition(
                                allOf(withId(R.id.ingredient_to_edit),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatEditText7.perform(replaceText("1 шт"), closeSoftKeyboard());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.ingredient_accept_but),
                        childAtPosition(
                                allOf(withId(R.id.ingredient_buttons_container),
                                        childAtPosition(
                                                withId(R.id.ingredient_to_edit),
                                                2)),
                                0),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_add_dish), withContentDescription("Поиск"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar_dish_activity),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

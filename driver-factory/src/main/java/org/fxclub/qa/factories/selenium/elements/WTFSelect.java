package org.fxclub.qa.factories.selenium.elements;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.htmlelements.element.Select;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Extended Select element
 */
public class WTFSelect extends Select {

    public WTFSelect(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public WebElement getFirstSelectedOption() {
        try {
            return super.getFirstSelectedOption();
        } catch (NoSuchElementException noOptionSelected) {
            return getOptions().get(0);
        }
    }

    public String getSelectedLabel() {
        return Optional.ofNullable(getFirstSelectedOption().getText()).orElse("").trim();
    }

    public String getSelectedValue() {
        return Optional.ofNullable(getFirstSelectedOption().getAttribute("value")).orElse("");
    }

    public void selectByPartialText(final String partialText) {
        getOptions().stream()
                .filter(
                        option -> StringUtils.containsIgnoreCase(option.getText(), partialText)
                ).findFirst()
                .orElseThrow(
                        () -> new org.openqa.selenium.NoSuchElementException("Cannot locate element with partial text: " + partialText)
                ).click();
    }

    public Pair<String, String> selectRandomOption() throws Exception {
        return selectRandomOption(new ArrayList<>());
    }

    public Pair<String, String> selectRandomOption(List<String> EXCEPTIONS) throws Exception {
        List<WebElement> options = getOptions();

        if (options.isEmpty())
            throw new WebDriverException(getName() + " is empty");

        Collections.shuffle(options);

        Optional<WebElement> option = options.stream().filter((p) -> {
            String value = p.getAttribute("value");
            return StringUtils.isNotEmpty(value) && !EXCEPTIONS.contains(value);
        }).findFirst();

        if (option.isPresent()) {
            option.get().click();
            return Pair.of(
                    option.get().getAttribute("value"),
                    option.get().getText()
            );
        } else {
            throw new WebDriverException(getName() + ": no options match criterias");
        }
    }

    public List<String> getValues() {
        List<WebElement> options = getOptions();
        return options.stream()
                .map(option -> option.getAttribute("value"))
                .collect(Collectors.toList());
    }


    public List<String> getLabels() {
        List<WebElement> options = getOptions();
        return options.stream()
                .map(option -> option.getText().trim())
                .collect(Collectors.toList());
    }

    public Pair<String, String> selectByAttribute(final String attribute, final String value) {
        List<WebElement> options = getOptions();
        Optional<WebElement> match = options.stream()
                .filter(option -> StringUtils.equalsIgnoreCase(option.getAttribute(attribute), value))
                .findFirst();

        if (match.isPresent()) {
            match.get().click();
            return Pair.of(
                    match.get().getAttribute("value"),
                    match.get().getText()
            );
        } else {
            throw new WebDriverException(getName() + ": no options match criterias");
        }
    }

}
